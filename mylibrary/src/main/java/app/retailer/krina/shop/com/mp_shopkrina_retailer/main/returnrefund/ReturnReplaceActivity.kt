package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.returnrefund

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.webkit.WebView
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.api.CommonClassForAPI
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ActivityReturnReplaceBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.adapter.ReturnOrderAdapter
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.ReturnOrderItemModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.postModels.PostReturnOrderModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.postModels.PostReturnOrderModel.DetailsEntity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.SharePrefs
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.StoryBordSharePrefs
import app.retailer.krina.shop.com.mp_shopkrina_retailer.showcaseviewlib.DismissType
import app.retailer.krina.shop.com.mp_shopkrina_retailer.showcaseviewlib.Gravity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.showcaseviewlib.GuideListener
import app.retailer.krina.shop.com.mp_shopkrina_retailer.showcaseviewlib.GuideView
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.RetailerSDKApp
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils
import io.reactivex.observers.DisposableObserver

class ReturnReplaceActivity : AppCompatActivity(), View.OnClickListener {
    private var mBinding: ActivityReturnReplaceBinding? = null
    private lateinit var commonClassForAPI: CommonClassForAPI
    private var orderList: ArrayList<Int>? = null
    private var list: ArrayList<ReturnOrderItemModel>? = null
    private var orderAdapter: ArrayAdapter<String>? = null
    private var itemListAdapter: ReturnOrderAdapter? = null
    private var orderId = 0

    private var mGuideView: GuideView? = null
    private var builder: GuideView.Builder? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_return_replace)

        // setContentView(R.layout.activity_return_replace)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        title = RetailerSDKApp.getInstance().dbHelper.getString(R.string.title_activity_return_order)
        if (intent.extras != null) {
            orderId = intent.getIntExtra("orderId", 0)
        }
        initViews()
        if (!SharePrefs.getInstance(applicationContext)
                .getBoolean(SharePrefs.IS_TERMS_ACCEPT)
        ) showTermsConditionDialog()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        onBackPressed()
        return super.onOptionsItemSelected(item)
    }

    override fun onClick(v: View) {
        if (v.id == R.id.btnSubmit) {
            checkFormData()
        }
    }


    private fun initViews() {
        // set String
        mBinding!!.etOrderId.setText(RetailerSDKApp.getInstance().dbHelper.getString(R.string.order_id))
        mBinding!!.btnSubmit.text =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.text_add_request)

        mBinding!!.recyclerOffer.layoutManager = LinearLayoutManager(applicationContext)
        mBinding!!.btnSubmit.setOnClickListener(this)
        mBinding!!.etOrderId.setOnTouchListener { _: View?, event: MotionEvent ->
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= mBinding!!.etOrderId.right - mBinding!!.etOrderId.compoundDrawables[2].bounds.width()) {
                    // your action here
                    if (mBinding!!.etOrderId.text.toString().isNotEmpty()) {
                        orderId = mBinding!!.etOrderId.text.toString().toInt()
                        Utils.showProgressDialog(this)
                        commonClassForAPI.getOrderById(
                            orderItemObserver, SharePrefs.getInstance(applicationContext)
                                .getInt(SharePrefs.CUSTOMER_ID), orderId
                        )
                    } else {
                        Utils.setToast(
                            applicationContext,
                            RetailerSDKApp.getInstance().dbHelper.getString(R.string.text_enter_order_id)
                        )
                    }
                    return@setOnTouchListener true
                }
            }
            return@setOnTouchListener false
        }
        mBinding!!.spOrderList.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    if (position != 0) {
                        orderId = orderList!![position - 1]
                        Utils.showProgressDialog(this@ReturnReplaceActivity)
                        commonClassForAPI.getOrderById(
                            orderItemObserver, SharePrefs.getInstance(applicationContext)
                                .getInt(SharePrefs.CUSTOMER_ID), orderId
                        )
                    }
                }
            }

        orderList = ArrayList()
        list = ArrayList()

        orderAdapter = ArrayAdapter(applicationContext, android.R.layout.simple_list_item_1)
        mBinding!!.spOrderList.adapter = orderAdapter

        commonClassForAPI = CommonClassForAPI.getInstance(this)
        Utils.showProgressDialog(this)
        commonClassForAPI.getLast7DayOrders(
            orderListObserver,
            SharePrefs.getInstance(applicationContext).getInt(SharePrefs.CUSTOMER_ID)
        )

        if (orderId != 0) {
            mBinding!!.etOrderId.setText("" + orderId)
            Utils.showProgressDialog(this)
            commonClassForAPI.getOrderById(
                orderItemObserver,
                SharePrefs.getInstance(applicationContext).getInt(SharePrefs.CUSTOMER_ID),
                orderId
            )
        }
        if (!StoryBordSharePrefs.getInstance(applicationContext)
                .getBoolean(StoryBordSharePrefs.RETURNREPLACE)
        ) {
            appStoryView()
        }
    }

    private fun checkFormData() {
        if (mBinding!!.spReturnType.selectedItemPosition == 0) {
            Utils.setToast(
                applicationContext,
                RetailerSDKApp.getInstance().dbHelper.getString(R.string.text_plz_select_return_replace)
            )
        } else if (list!!.size == 0) {
            Utils.setToast(
                applicationContext,
                RetailerSDKApp.getInstance().dbHelper.getString(R.string.text_plz_select_order)
            )
        } else {
            val list = ArrayList<ReturnOrderItemModel>()
            val postModel = PostReturnOrderModel()
            val detailList = ArrayList<DetailsEntity>()
            for (model: ReturnOrderItemModel in itemListAdapter!!.list) {
                if (model.isSelected) {
                    list.add(model)
                    val detailsEntity = DetailsEntity()
                    detailsEntity.setOrderDetailsId(model.orderDetailsId)
                    detailsEntity.setReturnQty(model.returnQty)
                    detailsEntity.setCustComment(model.comment)
                    detailList.add(detailsEntity)
                }
            }
            if (list.size > 0) {
                postModel.customerid =
                    SharePrefs.getInstance(applicationContext).getInt(SharePrefs.CUSTOMER_ID)
                postModel.orderid = orderId
                postModel.requesttype = mBinding!!.spReturnType.selectedItemPosition - 1
                postModel.status = "Pending to Pick"
                postModel.custComment = ""
                postModel.details = detailList

                Utils.showProgressDialog(this)
                commonClassForAPI.postReturnOrder(postOrderObserver, postModel)
            } else {
                Utils.setToast(
                    applicationContext,
                    RetailerSDKApp.getInstance().dbHelper.getString(R.string.text_plz_select_at_lease_one_item)
                )
            }
        }
    }

    private fun showTermsConditionDialog() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_return_terms)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)
        val tvTerms: WebView = dialog.findViewById(R.id.tv_terms)
        val cbTerms: AppCompatCheckBox = dialog.findViewById(R.id.cb_terms)
        tvTerms.loadData(
            """<ul>
          <li><span style="font-size: 14px;">Any Return replacement is allowed within 7 days from the date of Invoice, post that no return replacement will be done.</span></li>
          <li><span style="font-size: 14px;">Expired product will not be returned / replaced.</span></li>
          <li><span style="font-size: 14px;">One item can not be replaced with another item.</span></li>
          <li><span style="font-size: 14px;">In case any return of order same amount will be credited in wallet.</span></li>
          <li><span style="font-size: 14px;">Return Item should be in sale-able condition.</span></li>
          <li><span style="font-size: 14px;">If customer received an item which is damaged upon arrival, Please contact our customer executive immediately.</span></li>
        </ul>""", "text/html", "utf-8"
        )
        dialog.findViewById<View>(R.id.btn_agree).setOnClickListener {
            if (cbTerms.isChecked) {
                dialog.dismiss()
                SharePrefs.getInstance(applicationContext)
                    .putBoolean(SharePrefs.IS_TERMS_ACCEPT, true)
            } else {
                Utils.setToast(
                    applicationContext,
                    RetailerSDKApp.getInstance().dbHelper.getString(R.string.text_accept_terms_condition)
                )
            }
        }
        val dm = DisplayMetrics()
        dialog.window!!.windowManager.defaultDisplay.getMetrics(dm)
        dialog.window!!.setLayout(dm.widthPixels - 60, WindowManager.LayoutParams.WRAP_CONTENT)
        dialog.show()
    }

    private fun appStoryView() {
        builder = GuideView.Builder(this)
            .setTitle(RetailerSDKApp.getInstance().dbHelper.getString(R.string.return_replace))
            .setContentText(RetailerSDKApp.getInstance().dbHelper.getString(R.string.return_replace_detail))
            .setGravity(Gravity.center)
            .setDismissType(DismissType.anywhere)
            .setTargetView(mBinding!!.spReturnType)
            .setGuideListener(GuideListener { view ->
                when (view.id) {
                    R.id.spReturnType -> builder!!.setTitle(
                        RetailerSDKApp.getInstance().dbHelper.getString(
                            R.string.Select_Order
                        )
                    )
                        .setContentText(RetailerSDKApp.getInstance().dbHelper.getString(R.string.Select_Order_detail))
                        .setTargetView(mBinding!!.spOrderList).build()
                    R.id.spOrderList -> builder!!.setTitle(
                        RetailerSDKApp.getInstance().dbHelper.getString(
                            R.string.Submit_Request
                        )
                    )
                        .setContentText(RetailerSDKApp.getInstance().dbHelper.getString(R.string.Submit_Request_detail))
                        .setTargetView(mBinding!!.btnSubmit).build()
                    R.id.btnSubmit -> {
                        StoryBordSharePrefs.getInstance(this@ReturnReplaceActivity)
                            .putBoolean(StoryBordSharePrefs.RETURNREPLACE, true)
                        return@GuideListener
                    }
                }
                mGuideView = builder!!.build()
                mGuideView!!.show()
            })
        mGuideView = builder!!.build()
        mGuideView!!.show()
    }


    private val orderListObserver: DisposableObserver<ArrayList<Int>?> =
        object : DisposableObserver<ArrayList<Int>?>() {
            override fun onNext(arrayList: ArrayList<Int>) {
                try {
                    Utils.hideProgressDialog()
                    if (arrayList.size > 0) {
                        orderList = arrayList
                        orderAdapter?.add(RetailerSDKApp.getInstance().dbHelper.getString(R.string.text_select_order))
                        for (i in orderList!!) {
                            orderAdapter?.add("" + i)
                        }
                        orderAdapter?.notifyDataSetChanged()
                    } else {
                        orderAdapter?.add(RetailerSDKApp.getInstance().dbHelper.getString(R.string.text_select_order))
                        orderAdapter?.notifyDataSetChanged()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
                Utils.hideProgressDialog()
                dispose()
            }

            override fun onComplete() {}
        }

    private val orderItemObserver: DisposableObserver<ArrayList<ReturnOrderItemModel>?> =
        object : DisposableObserver<ArrayList<ReturnOrderItemModel>?>() {
            override fun onNext(arrayList: ArrayList<ReturnOrderItemModel>) {
                try {
                    Utils.hideProgressDialog()
                    if (arrayList.size > 0) {
                        list = arrayList
                        itemListAdapter = ReturnOrderAdapter(this@ReturnReplaceActivity, list!!)
                        mBinding!!.recyclerOffer.adapter = itemListAdapter
                    } else {
                        list?.clear()
                        itemListAdapter?.notifyDataSetChanged()
                        Utils.setToast(
                            applicationContext,
                            RetailerSDKApp.getInstance().dbHelper.getString(R.string.text_this_order_can_not_replce)
                        )
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    list?.clear()
                    orderAdapter?.notifyDataSetChanged()
                }
            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
                Utils.hideProgressDialog()
                list?.clear()
                orderAdapter?.notifyDataSetChanged()
                dispose()
            }

            override fun onComplete() {}
        }

    private val postOrderObserver: DisposableObserver<Boolean?> =
        object : DisposableObserver<Boolean?>() {
            override fun onNext(aBoolean: Boolean) {
                try {
                    Utils.hideProgressDialog()
                    val intent = Intent()
                    setResult(Activity.RESULT_OK, intent)
                    onBackPressed()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
                dispose()
                Utils.hideProgressDialog()
            }

            override fun onComplete() {}
        }
}