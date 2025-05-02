package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.clearance

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.api.CommonClassForAPI
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ActivityClearanceBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.interfaces.OnButtonClick
import app.retailer.krina.shop.com.mp_shopkrina_retailer.interfaces.SubCategoryInterface
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.activity.DirectUdharActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.adapter.ClearanceCatAdapter
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.adapter.ClearanceItemAdapter
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.categoryBean.CategoriesModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.ClearanceItemModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.postModels.SearchClearanceItemDc
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.EndPointPref
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.SharePrefs
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.LocaleHelper
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.RetailerSDKApp
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.TextUtils
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.JsonObject
import io.reactivex.observers.DisposableObserver
import java.text.DecimalFormat

class ClearanceActivity : AppCompatActivity(), SubCategoryInterface, OnButtonClick {
    private lateinit var mBinding: ActivityClearanceBinding

    private var categoryList = ArrayList<CategoriesModel>()
    private var list = ArrayList<ClearanceItemModel>()
    private var cartList: MutableList<ClearanceItemModel> = ArrayList()

    private var categoryAdapter: ClearanceCatAdapter? = null
    private var adapter: ClearanceItemAdapter? = null

    private var categoryId = 0
    private var skip = 0
    private var loading = true
    private var custId = 0
    private var lang = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityClearanceBinding.inflate(layoutInflater)
        setContentView(mBinding!!.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = RetailerSDKApp.getInstance().noteRepository
            .getString(R.string.clearance)
        custId = SharePrefs.getInstance(this).getInt(SharePrefs.CUSTOMER_ID)
        lang = LocaleHelper.getLanguage(this)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        init()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        onBackPressed()
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (cartList.any { it.qty > 0 }) {
            showAlert("Alert", "Exit from screen will clear the cart")
        } else {
            super.onBackPressed()
        }
    }


    override fun SubCategoryClicked(subCategoryId: Int, categoryId: Int) {
        callItemAPI(categoryId)
    }

    override fun onButtonClick(pos: Int, itemAdded: Boolean) {
        val total = cartList.sumOf { it.unitPrice * it.qty }
        mBinding.tvTotalPrice.text = "₹ " + DecimalFormat("##.##").format(total)
    }


    private fun init() {
        mBinding.noItems.text =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.no_item_available)
        mBinding.tvTotalItemH.text =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.total_amount)
        mBinding.btnCheckout.text =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.checkout)

        categoryAdapter = ClearanceCatAdapter(this, categoryList, this)
        mBinding.rvSubCategory.adapter = categoryAdapter

        val layoutManager = LinearLayoutManager(applicationContext)
        adapter = ClearanceItemAdapter(this, list, cartList, this)
        mBinding.rvCategoryItem.adapter = adapter

        if (Utils.isNetworkAvailable(this)) {
            Utils.showProgressDialog(this)
            CommonClassForAPI.getInstance(this).getClearanceItemCategory(
                categoryObserver,
                SharePrefs.getInstance(applicationContext).getInt(SharePrefs.WAREHOUSE_ID),
                SharePrefs.getInstance(applicationContext).getInt(SharePrefs.CUSTOMER_ID),
                LocaleHelper.getLanguage(applicationContext)
            )
        } else {
            Utils.setToast(
                applicationContext,
                RetailerSDKApp.getInstance().dbHelper.getString(R.string.internet_connection)
            )
        }
        mBinding.btnCheckout.setOnClickListener {
            SharePrefs.getInstance(this@ClearanceActivity)
                .putBoolean(SharePrefs.IS_UDHAAR_ORDER, false)
            if (cartList.any { it.qty > 0 }) {
                if (EndPointPref.getInstance(applicationContext)
                        .getLong(EndPointPref.CLEARANCE_MIN_ORDER) != 0L &&
                    cartList.sumOf { it.unitPrice * it.qty } > EndPointPref.getInstance(
                        applicationContext
                    ).getLong(EndPointPref.CLEARANCE_MIN_ORDER)
                ) {
                    if (SharePrefs.getInstance(this).getBoolean(SharePrefs.IS_UDHAAR_OVERDUE)) {
                        CommonClassForAPI.getInstance(this)
                            .getUdhaarOverDue(object : DisposableObserver<JsonObject?>() {
                                override fun onNext(jsonObject: JsonObject) {
                                    try {
                                        val msg = jsonObject["Msg"].asString
                                        val isOrder = jsonObject["IsOrder"].asBoolean
                                        if (!TextUtils.isNullOrEmpty(msg)) {
                                            SharePrefs.getInstance(this@ClearanceActivity)
                                                .putBoolean(SharePrefs.IS_UDHAAR_ORDER, isOrder)
                                            checkUdhaarOverDue(msg, isOrder)
                                        } else {
                                            startActivity(
                                                Intent(
                                                    applicationContext,
                                                    ClearancePaymentActivity::class.java
                                                ).putExtra(
                                                    "list",
                                                    ArrayList(cartList.filter { it.qty > 0 })
                                                )
                                            )
                                        }
                                    } catch (e: java.lang.Exception) {
                                        e.printStackTrace()
                                    }
                                }

                                override fun onError(e: Throwable) {
                                    e.printStackTrace()
                                    Utils.hideProgressDialog()
                                }

                                override fun onComplete() {}
                            }, custId, lang)
                    } else {
                        startActivity(
                            Intent(
                                applicationContext,
                                ClearancePaymentActivity::class.java
                            ).putExtra("list", ArrayList(cartList.filter { it.qty > 0 }))
                        )
                    }
                } else {
                    showAlert(
                        "Minimum Order Value",
                        RetailerSDKApp.getInstance().dbHelper.getString(R.string.msg_orderlessthan700) + " " + DecimalFormat(
                            "##.##"
                        ).format(
                            EndPointPref.getInstance(applicationContext)
                                .getLong(EndPointPref.CLEARANCE_MIN_ORDER)
                        ) + " " + RetailerSDKApp.getInstance().dbHelper.getString(R.string.msg_orderless)
                    )
                }
            } else {
                Utils.setToast(applicationContext, "Cart is Empty,\nadd item to cart.")
            }
        }

        mBinding.nestedScroll.setOnScrollChangeListener { v: NestedScrollView, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int ->
            if (v.getChildAt(v.childCount - 1) != null) {
                if (scrollY >= v.getChildAt(v.childCount - 1).measuredHeight - v.measuredHeight && scrollY > oldScrollY) {
                    val visibleItemCount = layoutManager.childCount
                    val totalItemCount = layoutManager.itemCount
                    val pastVisiblesItems = layoutManager.findFirstVisibleItemPosition()
                    if (visibleItemCount + pastVisiblesItems >= totalItemCount) {
                        Log.i("Nested", "BOTTOM SCROLL")
                        loading = false
                        skip += 10
                        callItemAPI1(categoryId)
                    }
                }
            }
        }
        showTermsDialog()
    }

    private fun checkUdhaarOverDue(textMsg: String, isOrder: Boolean) {
        val dialog = BottomSheetDialog(this, R.style.BottomTheme)
        dialog.setContentView(R.layout.dialog_check_udahr_overdue)
        dialog.setCanceledOnTouchOutside(true)
        val tvMsg = dialog.findViewById<TextView>(R.id.tvMsg)
        val imClose = dialog.findViewById<ImageView>(R.id.im_close)
        val btnUdharPayNow = dialog.findViewById<Button>(R.id.btnUdharPayNow)
        val btnOrder = dialog.findViewById<Button>(R.id.btnOrder)
        if (isOrder) {
            btnOrder!!.visibility = View.VISIBLE
            btnOrder.setOnClickListener {
                startActivity(
                    Intent(
                        applicationContext,
                        ClearancePaymentActivity::class.java
                    ).putExtra("list", ArrayList(cartList.filter { it.qty > 0 }))
                )
                dialog.dismiss()
            }
        }
        tvMsg!!.text = textMsg
        btnUdharPayNow!!.setOnClickListener {
            callLeadApi(
                EndPointPref.getInstance(this).baseUrl +
                        "/api/Udhar/GenerateLead?CustomerId=[CustomerId]"
            )
            dialog.dismiss()
        }
        imClose!!.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

    private fun setCategory(categoryList: ArrayList<CategoriesModel>) {
        mBinding.LLSubMainLayout.visibility = View.VISIBLE
        if (this.categoryList.size > 1) {
            this.categoryList.add(
                0,
                CategoriesModel(RetailerSDKApp.getInstance().dbHelper.getString(R.string.all), 0)
            )
        }
        categoryAdapter?.notifyDataSetChanged()
    }

    private fun callItemAPI(categoryId: Int) {
        list.clear()
        adapter!!.notifyDataSetChanged()
        skip = 0
        Utils.showProgressDialog(this)
        val model = SearchClearanceItemDc()
        model.warehouseId =
            SharePrefs.getInstance(applicationContext).getInt(SharePrefs.WAREHOUSE_ID)
        model.categoryId = categoryId
        model.keyword = ""
        model.skip = 0
        model.take = 10
        model.customerId = SharePrefs.getInstance(applicationContext).getInt(SharePrefs.CUSTOMER_ID)
        model.lang = LocaleHelper.getLanguage(applicationContext)
        CommonClassForAPI.getInstance(this).getClearanceItem(itemObserver, model)
    }

    private fun callItemAPI1(categoryId: Int) {
        Utils.showProgressDialog(this)
        val model = SearchClearanceItemDc()
        model.warehouseId =
            SharePrefs.getInstance(applicationContext).getInt(SharePrefs.WAREHOUSE_ID)
        model.categoryId = categoryId
        model.keyword = ""
        model.skip = skip
        model.take = 10
        model.customerId = SharePrefs.getInstance(applicationContext).getInt(SharePrefs.CUSTOMER_ID)
        model.lang = LocaleHelper.getLanguage(applicationContext)
        CommonClassForAPI.getInstance(this).getClearanceItem(itemObserver1, model)
    }

    private fun layoutHideUnHide(value: Boolean) {
        if (value) {
            mBinding.noItems.visibility = View.GONE
            mBinding.rvCategoryItem.visibility = View.VISIBLE
        } else {
            mBinding.noItems.visibility = View.VISIBLE
            mBinding.rvCategoryItem.visibility = View.GONE
        }
    }

    private fun showTermsDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_clearance_terms)
        dialog.setCancelable(false)

        val tvTitle = dialog.findViewById<TextView>(R.id.tvTitle)
        val tvDesc = dialog.findViewById<TextView>(R.id.tvDesc)
        val cbTerms = dialog.findViewById<CheckBox>(R.id.cbTerms)
        val btnContinue = dialog.findViewById<Button>(R.id.btnContinue)
        val btnCancel = dialog.findViewById<Button>(R.id.btnCancel)

        tvTitle.text =
            RetailerSDKApp.getInstance().noteRepository.getString(R.string.terms_and_condition)
        tvDesc.text = RetailerSDKApp.getInstance().noteRepository.getString(R.string.clearance_terms)
        cbTerms.text =
            RetailerSDKApp.getInstance().noteRepository.getString(R.string.accept_and_continue)
        btnContinue.text =
            RetailerSDKApp.getInstance().noteRepository.getString(R.string.txt_Continue)
        btnCancel.text = RetailerSDKApp.getInstance().noteRepository.getString(R.string.cancel)

        btnContinue.setOnClickListener {
            if (cbTerms.isChecked) {
                dialog.dismiss()
            } else {
                Utils.setToast(
                    applicationContext,
                    RetailerSDKApp.getInstance().noteRepository.getString(R.string.please_check_terms_conditions)
                )
            }
        }
        btnCancel.setOnClickListener {
            dialog.dismiss()
            finish()
        }

        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.show()
    }

    private fun showAlert(title: String, message: String) {
        val dialog = AlertDialog.Builder(this)
        dialog.setTitle(title)
        dialog.setMessage(message)
        if (title != "Minimum Order Value") {
            dialog.setNegativeButton("No") { dialog, i -> dialog.dismiss() }
            dialog.setPositiveButton("Yes") { dialog, i ->
                dialog.dismiss()
                super.onBackPressed()
            }
        } else {
            dialog.setNegativeButton("Ok") { dialog, i -> dialog.dismiss() }
        }
        dialog.show()
    }


    // filter cate items
    private val categoryObserver: DisposableObserver<ArrayList<CategoriesModel>> =
        object : DisposableObserver<ArrayList<CategoriesModel>>() {
            override fun onNext(list: ArrayList<CategoriesModel>) {
                try {
                    Utils.hideProgressDialog()
                    categoryList.clear()
                    categoryList.addAll(list)
                    //set category
                    setCategory(categoryList)
                    callItemAPI(0)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
                Utils.hideProgressDialog()
                dispose()
                mBinding.LLSubMainLayout.visibility = View.VISIBLE
            }

            override fun onComplete() {
            }
        }

    private val itemObserver: DisposableObserver<ArrayList<ClearanceItemModel>> =
        object : DisposableObserver<ArrayList<ClearanceItemModel>>() {
            override fun onNext(itemList: ArrayList<ClearanceItemModel>) {
                try {
                    Utils.hideProgressDialog()
                    list.clear()
                    list.addAll(itemList)
                    adapter = ClearanceItemAdapter(
                        this@ClearanceActivity,
                        list,
                        cartList,
                        this@ClearanceActivity
                    )
                    adapter?.notifyDataSetChanged()
                    mBinding.rvCategoryItem.adapter = adapter
                    layoutHideUnHide(list.size > 0)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
                Utils.hideProgressDialog()
            }

            override fun onComplete() {
            }
        }

    private val itemObserver1: DisposableObserver<ArrayList<ClearanceItemModel>> =
        object : DisposableObserver<ArrayList<ClearanceItemModel>>() {
            override fun onNext(itemList: ArrayList<ClearanceItemModel>) {
                try {
                    Utils.hideProgressDialog()
                    list.addAll(itemList)
                    adapter?.notifyDataSetChanged()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
                Utils.hideProgressDialog()
            }

            override fun onComplete() {
            }
        }

    private fun callLeadApi(url: String) {
        var url = url
        Utils.showProgressDialog(this)
        if (url != null) {
            url = url.replace(
                "[CustomerId]",
                "" + SharePrefs.getInstance(applicationContext).getInt(SharePrefs.CUSTOMER_ID)
            )
            url = url.replace(
                "[CUSTOMERID]",
                "" + SharePrefs.getInstance(applicationContext).getInt(SharePrefs.CUSTOMER_ID)
            )
            url = url.replace(
                "[SKCODE]",
                "" + SharePrefs.getInstance(applicationContext).getString(SharePrefs.SK_CODE)
            )
            url = url.replace(
                "[WAREHOUSEID]",
                "" + SharePrefs.getInstance(applicationContext).getInt(SharePrefs.WAREHOUSE_ID)
            )
            url = url.replace("[LANG]", "" + LocaleHelper.getLanguage(applicationContext))
            url = url.replace(
                "[MOBILE]",
                "" + SharePrefs.getInstance(applicationContext).getString(SharePrefs.MOBILE_NUMBER)
            )
        }
        CommonClassForAPI.getInstance(this)
            .generateLead(object : DisposableObserver<JsonObject?>() {
                override fun onNext(jsonObject: JsonObject) {
                    Utils.hideProgressDialog()
                    try {
                        if (jsonObject != null) {
                            val isSuccess = jsonObject["Result"].asBoolean
                            if (isSuccess) {
                                val url = jsonObject["Data"].asString
                                startActivity(
                                    Intent(
                                        applicationContext,
                                        DirectUdharActivity::class.java
                                    ).putExtra("url", url)
                                )
                            } else {
                                val msg = jsonObject["Msg"].asString
                                android.app.AlertDialog.Builder(this@ClearanceActivity)
                                    .setTitle(RetailerSDKApp.getInstance().dbHelper.getString(R.string.alert))
                                    .setMessage(msg).setNegativeButton(getString(R.string.ok), null)
                                    .show()
                            }
                        }
                    } catch (e: java.lang.Exception) {
                        e.printStackTrace()
                    }
                }

                override fun onError(e: Throwable) {
                    e.printStackTrace()
                    Utils.hideProgressDialog()
                }

                override fun onComplete() {
                    dispose()
                }
            }, url)
    }
}