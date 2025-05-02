package app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.notification

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.api.observe
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.ItemListModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.repository.AppRepository
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.response.Response
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ActivityActionNotificationBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.interfaces.OnItemClick
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.shoppingCart.ShoppingCartActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.CartAddItemModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.SharePrefs
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.LocaleHelper
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.RetailerSDKApp
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils
import java.text.DecimalFormat
import java.util.Collections

class ActionBleNotificationActivity : AppCompatActivity(), View.OnClickListener {
    private var mBinding: ActivityActionNotificationBinding? = null
    private lateinit var viewModel: NotificationViewModel
    private var custId = 0
    private var notificationType: String? = null
    private var warehouseId = 0
    private var typeId = 0
    private val list = ArrayList<ItemListModel>()
    private var itemListAdapter: NotificationItemListAdapter? = null
    private var selectedSSCIdFlag = false
    private var mNotificationClick = false
    private var notificationId = 0
    private var lang = ""
    private var lastItemId = 0
    private var apiRunning = false
    private var dialog: ProgressDialog? = null
    private var onItemClick: OnItemClick? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_action_notification)
        val appRepository = AppRepository(applicationContext)
        viewModel = ViewModelProvider(
            this,
            NotificationViewModelFactory(application, appRepository)
        )[NotificationViewModel::class.java]
        val extras = intent.extras
        if (extras != null) {
            notificationType = extras.getString("notificationCategory")
            typeId = extras.getInt("typeId", 0)
            mNotificationClick = extras.getBoolean("Notification")
            notificationId = extras.getInt("NotificationId")
        }
        observe(viewModel.getNotificationItemData, ::getNotificationItemResult)

    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        // init view
        initialization()
        // get Notification API call
        warehouseId = SharePrefs.getInstance(this@ActionBleNotificationActivity)
            .getInt(SharePrefs.WAREHOUSE_ID)
        viewModel.getNotificationItem(custId, warehouseId, notificationType, typeId, lang)
        if (mNotificationClick) {
            viewModel.notificationClick(custId, notificationId)
        }
        RetailerSDKApp.getInstance().noteRepository.cartValue.observe(this) { totalAmt: Double? ->
            if (totalAmt != null && totalAmt > 0) {
                mBinding!!.tvTotalPrice.visibility = View.VISIBLE
                mBinding!!.tvTotalPrice.text = "" + DecimalFormat("##.##").format(totalAmt)
            } else {
                mBinding!!.tvTotalPrice.visibility = View.GONE
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        if (intent.extras != null) {
            notificationType = intent.extras!!.getString("notificationCategory")
            typeId = intent.extras!!.getInt("typeId", 0)
            mNotificationClick = intent.extras!!.getBoolean("Notification")
            notificationId = intent.extras!!.getInt("NotificationId")
            intent.extras!!.clear()
            viewModel.notificationClick(custId, notificationId)
        }
    }

    override fun onClick(v: View) {
        if (v.id == R.id.back) {
            onBackPressed()
        } else if (v.id == R.id.checkoutBtn) {
            if (!apiRunning && !handler.hasMessages(0)) {
                startActivity(Intent(applicationContext, ShoppingCartActivity::class.java))
                Utils.leftTransaction(this)
            } else {
                dialog = ProgressDialog(this)
                dialog!!.setMessage("Please wait")
                dialog!!.show()
                dialog!!.setOnCancelListener { dialog1: DialogInterface? ->
                    dialog = null
                    mBinding!!.checkoutBtn.callOnClick()
                }
            }
        }
    }

    override fun onBackPressed() {
        val manager = getSystemService(ACTIVITY_SERVICE) as ActivityManager
        val taskList = manager.getRunningTasks(10)
        if (taskList[0].numActivities <= 1) {
            startActivity(Intent(applicationContext, NotificationActivity::class.java))
            finish()
        } else {
            super.onBackPressed()
        }
    }

    override fun onResume() {
        super.onResume()
        try {
            if (selectedSSCIdFlag && itemListAdapter != null) {
                itemListAdapter!!.notifyDataSetChanged()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase))
    }

    override fun applyOverrideConfiguration(overrideConfiguration: Configuration) {
        if (overrideConfiguration != null) {
            val uiMode = overrideConfiguration.uiMode
            overrideConfiguration.setTo(baseContext.resources.configuration)
            overrideConfiguration.uiMode = uiMode
        }
        super.applyOverrideConfiguration(overrideConfiguration)
    }

    private fun initialization() {
        mBinding!!.saveTotalItem.text =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.total_amount)
        mBinding!!.checkoutBtn.text =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.payment)
        mBinding!!.tvNoNotification.text =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.no_notification)
        mBinding!!.toolbar.title.text =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.notification_item)
        lang = LocaleHelper.getLanguage(this)
        custId = SharePrefs.getInstance(applicationContext).getInt(SharePrefs.CUSTOMER_ID)
        mBinding!!.toolbar.back.setOnClickListener(this)
        mBinding!!.toolbar.back.setOnClickListener { v: View? -> onBackPressed() }
        mBinding!!.checkoutBtn.setOnClickListener(this)
        itemListAdapter = NotificationItemListAdapter(this, list)
        mBinding!!.rvNotification.adapter = itemListAdapter
    }

    // add items to card
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
        this.onItemClick = onItemClick
        apiRunning = true
        if (lastItemId == 0) {
            lastItemId = itemId
        }
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
                    handler.sendMessageDelayed(
                        Message.obtain(handler, Integer.valueOf(lastItemId)),
                        1000
                    )
                } else {
                    handler.sendEmptyMessage(Integer.valueOf(lastItemId))
                    callAddToCartAPI(itemId, qty, itemUnitPrice, isPrimeItem, onItemClick)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            if (lastItemId != itemId) {
                lastItemId = itemId
            }
    }

    private fun callAddToCartAPI(
        itemId: Int,
        qty: Int,
        itemUnitPrice: Double,
        isPrimeItem: Boolean,
        onItemClick: OnItemClick?
    ) {
        viewModel.addItemInCartData.observe(this) {
            when (it) {
                is Response.Loading -> {}
                is Response.Success -> {
                    it.data?.let {
                        Utils.hideProgressDialog()
                        if (it["Status"].asBoolean) {
                            onItemClick!!.onItemClick(0, true)
                            Utils.hideProgressDialog()
                            apiRunning = false
                            if (dialog != null) {
                                dialog!!.cancel()
                            }
                        } else {
                            Utils.setToast(
                                applicationContext, "unable to add cart"
                            )
                            onItemClick!!.onItemClick(0, false)
                        }
                    }
                }

                is Response.Error -> {
                    Utils.hideProgressDialog()
                    onItemClick!!.onItemClick(0, false)
                    apiRunning = false
                    if (dialog != null) {
                        dialog!!.cancel()
                    }
                    Utils.setToast(
                        applicationContext,
                        it.errorMesssage
                    )
                }
            }
        }
        viewModel.addItemIncCart(
            CartAddItemModel(
                custId,
                warehouseId,
                itemId,
                qty,
                itemUnitPrice,
                false,
                false,
                LocaleHelper.getLanguage(this),
                isPrimeItem,
                false
            ),
            "Notification Item"
        )
    }

    @SuppressLint("HandlerLeak")
    private val handler: Handler = object : Handler(Looper.myLooper()!!) {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            try {
                val model = RetailerSDKApp.getInstance().noteRepository.getCartItem1(msg.what)
                callAddToCartAPI(
                    model.itemId,
                    model.qty,
                    model.unitPrice,
                    model.isPrimeItem,
                    onItemClick
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    private inner class ComparatorOfNumericString : Comparator<ItemListModel> {
        @SuppressLint("NewApi")
        override fun compare(lhs: ItemListModel, rhs: ItemListModel): Int {
            return Integer.compare(lhs.minOrderQty, rhs.minOrderQty)
        }
    }

    private fun getNotificationItemResult(it: Response<ArrayList<ItemListModel>>) {
        when (it) {
            is Response.Loading -> {
                Utils.showProgressDialog(this)
            }
            is Response.Success -> {
                it.data?.let {
                    Utils.hideProgressDialog()
                    selectedSSCIdFlag = true
                    val list = viewModel.getMoqList(it)
                    if (list.size != 0) {
                        Collections.sort(list, ComparatorOfNumericString())
                        itemListAdapter!!.setItemListCategory(list)
                    }
                }
            }

            is Response.Error -> {
                Utils.hideProgressDialog()
            }
        }
    }

}