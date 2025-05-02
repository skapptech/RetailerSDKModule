package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.myIssues

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.text.TextWatcher
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.widget.NestedScrollView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.api.CommonClassForAPI
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ActivityAddIssueBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.interfaces.AddIssueInterface
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.adapter.IssueOptionAdapter
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.postModels.GenerateTicketPostModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.postModels.PostIssuesCategoryModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.responseModel.IssueThreadModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.responseModel.IssueTicketsModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.responseModel.IssuesCategoryModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.SharePrefs
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.LocaleHelper
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.MyApplication
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.TextUtils
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils
import io.reactivex.observers.DisposableObserver
import java.text.SimpleDateFormat
import java.util.Date

class AddIssueActivity : AppCompatActivity(), AddIssueInterface {
    private lateinit var mBinding: ActivityAddIssueBinding

    private var utils: Utils? = null
    private var commonClassForAPI: CommonClassForAPI? = null

    private var ticketDataList: ArrayList<IssueTicketsModel>? = null
    private var categoryList: ArrayList<IssueTicketsModel>? = null
    private var chatList: ArrayList<IssueThreadModel> = ArrayList()
    lateinit var scrollview: NestedScrollView

    private var adapter: IssueOptionAdapter? = null
    private var ticketDetails: IssueTicketsModel? = null
    private lateinit var genTktDetails: GenerateTicketPostModel
    private lateinit var postIssuesCategoryModel: PostIssuesCategoryModel

    private var customerId = 0
    private var isFirstCall = true
    private var lang = ""
    private lateinit var currentDateTime: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_issue)

        mBinding.toolbar.title.text =
            MyApplication.getInstance().dbHelper.getString(R.string.title_activity_direct_support)
        mBinding.tvNoIssues.text =
            MyApplication.getInstance().dbHelper.getString(R.string.no_issues_found)
        title = MyApplication.getInstance().dbHelper.getString(R.string.title_activity_add_issues)
        initialization()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        onBackPressed()
        return super.onOptionsItemSelected(item)
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase))
    }


    private fun initialization() {
        mBinding.toolbar.back.setOnClickListener { onBackPressed() }
        mBinding.toolbar.subtitle.visibility = View.VISIBLE
        mBinding.toolbar.subtitle.setOnClickListener {
            startActivity(Intent(applicationContext, MyIssuesActivity::class.java))
        }
        mBinding.tvTitle.text =
            MyApplication.getInstance().dbHelper.getString(R.string.que_category_do_you_have)
        mBinding.tvYes.text = MyApplication.getInstance().dbHelper.getString(R.string.yes_title)
        mBinding.tvNo.text = MyApplication.getInstance().dbHelper.getString(R.string.no_title)

        mBinding.tvYes.setOnClickListener {
            mBinding.liNextQues.visibility = View.GONE
            this.RadioBtnClick("Yes")
        }
        mBinding.tvNo.setOnClickListener {
            mBinding.liNextQues.visibility = View.GONE
            this.RadioBtnClick("No")
        }

        lang = LocaleHelper.getLanguage(applicationContext)
        customerId = SharePrefs.getInstance(applicationContext).getInt(SharePrefs.CUSTOMER_ID)

        commonClassForAPI = CommonClassForAPI.getInstance(this)
        utils = Utils(this)
        ticketDataList = ArrayList()
        categoryList = ArrayList()

        val layoutManager = LinearLayoutManager(applicationContext)
        layoutManager.stackFromEnd = false
        layoutManager.isSmoothScrollbarEnabled = true
        mBinding.rvMyIssues.layoutManager = layoutManager

        currentDateTime = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(Date())
        chatList.add(
            IssueThreadModel(
                MyApplication.getInstance().dbHelper.getString(R.string.ticket_hello)
                        + SharePrefs.getInstance(applicationContext)
                    .getString(SharePrefs.CUSTOMER_NAME), false,
                currentDateTime
            )
        )
        chatList.add(
            IssueThreadModel(
                MyApplication.getInstance().dbHelper.getString(R.string.que_how_may_i_help),
                false, currentDateTime
            )
        )
        adapter = IssueOptionAdapter(this, chatList, ticketDataList!!, this@AddIssueActivity)
        mBinding.rvMyIssues.adapter = adapter

        genTktDetails = GenerateTicketPostModel(0, "")
        postIssuesCategoryModel = PostIssuesCategoryModel(0, 1, customerId, "", "", lang)

        callApi(postIssuesCategoryModel)
    }

    private fun showProfileDialog() {
        val maxCount = 200
        val mView = layoutInflater.inflate(R.layout.dialog_post_issue, null)
        val customDialog = Dialog(this, android.R.style.Theme_NoTitleBar_Fullscreen)
        customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        customDialog.setContentView(mView)
        customDialog.setCancelable(false)

        val yesBtn: Button = mView.findViewById(R.id.btn_yes)
        val noBtn: Button = mView.findViewById(R.id.btn_no)
        val etDescription: EditText = mView.findViewById(R.id.et_description)
        val dialogTv: AppCompatTextView = mView.findViewById(R.id.tv_confirmation)
        val tvCharCount: TextView = mView.findViewById(R.id.tv_char_count)
        etDescription.filters = arrayOf<InputFilter>(LengthFilter(maxCount))

        tvCharCount.text = "$maxCount/$maxCount"
        etDescription.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, aft: Int) {}
            override fun afterTextChanged(s: Editable) { // this will show characters remaining
                val charCount = maxCount - s.toString().length
                tvCharCount.text = charCount.toString() + "/" + maxCount
            }
        })

        dialogTv.text = MyApplication.getInstance().dbHelper.getString(R.string.popup_are_u_sure)

        yesBtn.setOnClickListener {
            if (!TextUtils.isNullOrEmpty(etDescription.text.toString().trim())) {
                postIssuesCategoryModel = PostIssuesCategoryModel(
                    ticketDetails!!.CategoryId,
                    1,
                    customerId,
                    "",
                    etDescription.text.toString(),
                    lang
                )
                callApi(postIssuesCategoryModel)
                customDialog.dismiss()
                adapter!!.notifyDataSetChanged()
            } else {
                Utils.setToast(
                    this,
                    MyApplication.getInstance().dbHelper.getString(R.string.plz_enter_des)
                )
            }
        }
        noBtn.setOnClickListener {
            isFirstCall = true
            chatList.add(
                IssueThreadModel(
                    MyApplication.getInstance().dbHelper.getString(R.string.no_title),
                    true,
                    currentDateTime
                )
            )
            chatList.add(
                IssueThreadModel(
                    MyApplication.getInstance().dbHelper.getString(R.string.que_category_then_what_issue),
                    false,
                    currentDateTime
                )
            )
            postIssuesCategoryModel = PostIssuesCategoryModel(
                0, 1, customerId,
                "", "", lang
            )
            callApi(postIssuesCategoryModel)
            customDialog.dismiss()
            adapter!!.notifyDataSetChanged()
        }

        customDialog.show()
    }

    private fun callApi(postIssuesCategoryModel: PostIssuesCategoryModel) {
        if (utils!!.isNetworkAvailable) {
            if (commonClassForAPI != null) {
                Utils.showProgressDialog(this)
                commonClassForAPI!!.getIssueTopics(
                    ticketUserDis, postIssuesCategoryModel
                )
            }
        } else {
            Utils.setToast(
                this,
                MyApplication.getInstance().dbHelper.getString(R.string.no_internet)
            )
        }
    }


    override fun loadOptions(list: IssueTicketsModel, categoryAnsware: String) {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        currentDateTime = sdf.format(Date())
        chatList.add(IssueThreadModel(list.CategoryName.toString(), true, currentDateTime))
        ticketDetails = list
        adapter!!.notifyDataSetChanged()
        ticketDataList!!.clear()
        if (!list.IsAskQuestion) {
            postIssuesCategoryModel =
                PostIssuesCategoryModel(list.CategoryId, 1, customerId, "", "", lang)
            callApi(postIssuesCategoryModel)
        } else {
            if (!TextUtils.isNullOrEmpty(list.AfterSelectMessage)) {
                chatList.add(
                    IssueThreadModel(
                        list.AfterSelectMessage.toString(),
                        false,
                        currentDateTime
                    )
                )
            } else {
                chatList.add(
                    IssueThreadModel(
                        "",
                        false,
                        currentDateTime
                    )
                )
            }
            list.isSelected = true
            ticketDataList!!.add(list)
            adapter!!.notifyDataSetChanged()
        }
    }

    override fun doneBtnClick(list: IssueTicketsModel, categoryAnsware: String) {
        ticketDataList!!.clear()
        postIssuesCategoryModel =
            PostIssuesCategoryModel(list.CategoryId, 1, customerId, categoryAnsware, "", lang)
        callApi(postIssuesCategoryModel)
    }

    override fun RadioBtnClick(sRadio: String) {
        if (sRadio == "Yes" || sRadio == "हाँ") {
            chatList.add(
                IssueThreadModel(
                    sRadio,
                    true, currentDateTime
                )
            )
            chatList.add(
                IssueThreadModel(
                    MyApplication.getInstance().dbHelper.getString(R.string.que_category_what_issue),
                    false, currentDateTime
                )
            )
            isFirstCall = true
            postIssuesCategoryModel = PostIssuesCategoryModel(0, 1, customerId, "", "", lang)
            callApi(postIssuesCategoryModel)
        } else {
            ticketDataList!!.clear()
            chatList.add(
                IssueThreadModel(
                    sRadio,
                    true,
                    currentDateTime
                )
            )
            chatList.add(
                IssueThreadModel(
                    MyApplication.getInstance().dbHelper.getString(R.string.thank_you_msg),
                    false, currentDateTime
                )
            )
            adapter!!.notifyDataSetChanged()
        }
    }


    private var ticketUserDis: DisposableObserver<IssuesCategoryModel> =
        object : DisposableObserver<IssuesCategoryModel>() {
            override fun onNext(response: IssuesCategoryModel) {
                Utils.hideProgressDialog()
                try {
                    ticketDataList!!.clear()
                    if (response.category!!.size != 0) {
                        if (!isFirstCall) {
                            if (!TextUtils.isNullOrEmpty(ticketDetails!!.AfterSelectMessage)) {
                                chatList.add(
                                    IssueThreadModel(
                                        ticketDetails!!.AfterSelectMessage.toString(),
                                        false, currentDateTime
                                    )

                                )
                            }
                        }
                        ticketDataList!!.addAll(response.category!!)
                    } else if (TextUtils.isNullOrEmpty(response.ticketmessage) && response.category!!.size == 0) {
                        if (!isFirstCall) {
                            showProfileDialog()
                        }
                    } else {
                        chatList.add(
                            IssueThreadModel(
                                response.ticketmessage.toString(),
                                isSelected = false, currentDateTime
                            )
                        )
                        mBinding.liNextQues.visibility = View.VISIBLE
                    }
                    isFirstCall = false
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                adapter!!.notifyDataSetChanged()
                mBinding.rvMyIssues.scrollToPosition(mBinding.rvMyIssues.adapter!!.itemCount - 1)
            }

            override fun onError(e: Throwable) {
                Utils.hideProgressDialog()
                Toast.makeText(
                    applicationContext,
                    MyApplication.getInstance().dbHelper.getString(R.string.please_try_again),
                    Toast.LENGTH_SHORT
                ).show()
                e.printStackTrace()
            }

            override fun onComplete() {
                Utils.hideProgressDialog()
            }
        }
}
