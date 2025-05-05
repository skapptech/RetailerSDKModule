package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.target

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.text.Editable
import android.text.Html
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.RadioButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.viewpager2.widget.ViewPager2
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.api.CommonClassForAPI
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.CartAddItemModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.ItemListModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ActivityCustomerTargetBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ActivityTargetOrderListBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.interfaces.OnItemClick
import app.retailer.krina.shop.com.mp_shopkrina_retailer.interfaces.SearchInterface
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.adapter.TargetViewPagerTabAdapter
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.SharePrefs
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.shoppingCart.ShoppingCartActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.LocaleHelper
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.RetailerSDKApp
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.TextUtils
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils
import com.google.gson.JsonObject
import io.reactivex.observers.DisposableObserver
import java.text.DecimalFormat

class TargetOrderListActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityTargetOrderListBinding

    lateinit var etSearch: EditText

    private var commonClassForAPI: CommonClassForAPI? = null
    private val dialog: ProgressDialog? = null

    private var mAboutDataListener: SearchInterface? = null
    private var onItemClicked: OnItemClick? = null

    private var apiRunning = false
    private var keyValue = ""
    public var companyId: Int = 0
    private var storeId: Int = 0
    var custId = 0
    var wId: Int = 0
    var lastItemId: Int = 0
    var lang: String? = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityTargetOrderListBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        if (intent.extras != null) {
            companyId = intent.getIntExtra("companyId", 0)
        }
        init()
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        RetailerSDKApp.getInstance().noteRepository.cartValue.observe(this) { totalAmt: Double? ->
            if (totalAmt != null && totalAmt > 0) {
                mBinding.tvCount.visibility = View.VISIBLE
                val sTotalAmount =
                    "<font color=#FFFFFF>&#8377; " + DecimalFormat("##.##")
                        .format(totalAmt) + "</font>"
                mBinding.tvCount.text = Html.fromHtml(sTotalAmount)
            } else {
                mBinding.tvCount.visibility = View.GONE
            }
        }
    }


    private fun init() {
        lang = LocaleHelper.getLanguage(applicationContext)!!
        custId = SharePrefs.getInstance(applicationContext).getInt(SharePrefs.CUSTOMER_ID)
        wId = SharePrefs.getInstance(applicationContext).getInt(SharePrefs.WAREHOUSE_ID)
        //
        commonClassForAPI = CommonClassForAPI.getInstance(this)
        etSearch = mBinding.etSearch

        mBinding.rgTargetRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            val radio: RadioButton = findViewById(checkedId)
            if (radio.text == "Target Items") {
                mBinding.viewPager.currentItem = 0
            } else if (radio.text == "Already Bought Items") {
                mBinding.viewPager.currentItem = 1
            }
        }
        mBinding.back.setOnClickListener {
            onBackPressed()
        }
        mBinding.ivCart.setOnClickListener {
            startActivity(Intent(applicationContext, ShoppingCartActivity::class.java))
            Utils.leftTransaction(this)
        }

        search()
        setupViewPager()
    }

    private fun search() {
        etSearch.setOnEditorActionListener(
            TextView.OnEditorActionListener { v, actionId, event ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (TextUtils.isNullOrEmpty(v.text.toString().trim())) {
                        /*ViewUtils.toast(
                            applicationContext,
                            "Please enter item name"
                        )*/
                        keyValue = ""
                    } else {
                        keyValue = etSearch.text.toString()
                    }
                    Utils.hideKeyboard(this, etSearch)
                    mAboutDataListener?.searchBtn(keyValue)
                    return@OnEditorActionListener true
                }
                false
            })

        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                if (p0.isNullOrEmpty()) {
                    keyValue = ""
                    mAboutDataListener?.searchBtn(keyValue)
                }
            }
        })
    }

    private fun setupViewPager() {
        val adapter = TargetViewPagerTabAdapter(this, storeId)
        mBinding.viewPager.adapter = adapter
        mBinding.viewPager.currentItem = 0
        mBinding.viewPager.registerOnPageChangeCallback(doppelgangerPageChangeCallback)
    }

    fun setAboutDataListener(listener: SearchInterface) {
        this.mAboutDataListener = listener
    }

    fun getNotifyItems(warehouseId: Int, itemNumber: String?) {
        commonClassForAPI!!.getNotfayItems(
            object : DisposableObserver<Boolean?>() {
                override fun onNext(jsonArray: Boolean) {
                    if (jsonArray) {
                        Utils.setToast(
                            applicationContext,
                            RetailerSDKApp.getInstance().dbHelper.getString(R.string.txt_Notify_msg)
                        )
                    }
                }

                override fun onError(e: Throwable) {}
                override fun onComplete() {}
            },
            SharePrefs.getInstance(applicationContext).getInt(SharePrefs.CUSTOMER_ID),
            warehouseId,
            itemNumber
        )
    }

    fun addItemInCartItemArrayList(
        itemId: Int,
        qty: Int,
        itemUnitPrice: Double,
        model: ItemListModel,
        freeItemQty: Int,
        totalFreeWalletPoint: Double,
        isPrimeItem: Boolean,
        onItemClick: OnItemClick
    ) {
        onItemClicked = onItemClick
        apiRunning = true
        if (lastItemId == 0) {
            lastItemId = itemId
        }
        if (Utils.isNetworkAvailable(this)) {
            try {
                model.unitPrice = itemUnitPrice
                model.qty = qty
                model.totalFreeItemQty = freeItemQty
                model.totalFreeWalletPoint = totalFreeWalletPoint
                // update cart database
                if (RetailerSDKApp.getInstance().noteRepository.isItemInCart(itemId)) {
                    RetailerSDKApp.getInstance().noteRepository.updateCartItem(model)
                } else {
                    RetailerSDKApp.getInstance().noteRepository.addToCart(model)
                }
                if (lastItemId == itemId) {
                    handler.removeCallbacksAndMessages(null)
                    handler.sendMessageDelayed(Message.obtain(handler, lastItemId), 1000)
                } else {
                    if (handler.hasMessages(lastItemId)) handler.sendEmptyMessage(lastItemId)
                    callAddToCartAPI(itemId, qty, itemUnitPrice, isPrimeItem, onItemClick)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            if (lastItemId != itemId) {
                lastItemId = itemId
            }
        } else {
            Utils.setToast(
                applicationContext,
                RetailerSDKApp.getInstance().dbHelper.getString(R.string.internet_connection)
            )
            onItemClick.onItemClick(0, false)
        }
    }

    private fun callAddToCartAPI(
        itemId: Int,
        qty: Int,
        itemUnitPrice: Double,
        isPrimeItem: Boolean,
        onItemClick: OnItemClick
    ) {
        commonClassForAPI!!.postAddCartItem(
            object : DisposableObserver<JsonObject?>() {
                override fun onNext(`object`: JsonObject) {
                    if (`object`["Status"].asBoolean) {
                        onItemClick.onItemClick(0, true)
                        Utils.hideProgressDialog()
                    } else {
                        Utils.setToast(
                            applicationContext, "unable to add cart"
                        )
                        onItemClick.onItemClick(0, false)
                    }
                    apiRunning = false
                    if (dialog != null) {
                        dialog.cancel()
                    }
                }

                override fun onError(e: Throwable) {
                    e.printStackTrace()
                    Utils.hideProgressDialog()
                    onItemClick.onItemClick(0, false)
                    apiRunning = false
                    if (dialog != null) {
                        dialog.cancel()
                    }
                }

                override fun onComplete() {}
            },
            CartAddItemModel(
                custId,
                wId,
                itemId,
                qty,
                itemUnitPrice,
                false,
                false,
                lang!!,
                isPrimeItem,
                false
            ),
            "Free Item"
        )
    }


    private var doppelgangerPageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            if (position == 0) {
                mBinding.rgTargetRadioGroup.check(mBinding.rgTargetRadioGroup.getChildAt(0).id)
            } else if (position == 1) {
                mBinding.rgTargetRadioGroup.check(mBinding.rgTargetRadioGroup.getChildAt(1).id)
            }
            super.onPageSelected(position)
        }
    }

    @SuppressLint("HandlerLeak")
    private val handler: Handler = object : Handler(Looper.myLooper()!!) {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            try {
                val model = RetailerSDKApp.getInstance().noteRepository.getCartItem1(msg.what)
                callAddToCartAPI(
                    model.itemId, model.qty, model.unitPrice, model.isPrimeItem,
                    onItemClicked!!
                )
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }
    }
}