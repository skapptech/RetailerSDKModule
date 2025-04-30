package app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.freebies

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.text.Html
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.api.observe
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.ItemListModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.freebies.OfferDataModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.repository.AppRepository
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.response.Response
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ActivityFreebiesOffersBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.interfaces.AddToCartInterface
import app.retailer.krina.shop.com.mp_shopkrina_retailer.interfaces.OnItemClick
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.shoppingCart.ShoppingCartActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.CartAddItemModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.SharePrefs
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.HomeActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.LocaleHelper
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.MyApplication
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils
import java.text.DecimalFormat
import java.util.Collections

class FreebiesOfferActivity : AppCompatActivity(), View.OnClickListener, AddToCartInterface {
    private var mBinding: ActivityFreebiesOffersBinding? = null
    private lateinit var viewModel: FreebiesViewModel
    private val list = ArrayList<ItemListModel>()
    private var itemListAdapter: FreebiesAdapter? = null
    private var custId = 0
    private var wId = 0
    private var lastItemId = 0
    private var apiRunning = false
    private var selectedSSCIdFlag = false
    private var dialog: ProgressDialog? = null
    private var onItemClick: OnItemClick? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_freebies_offers)
        val appRepository = AppRepository(applicationContext)
        viewModel = ViewModelProvider(
            this,
            FreebiesViewModelFactory(application, appRepository)
        )[FreebiesViewModel::class.java]
        setSupportActionBar(mBinding!!.toolbarFreebies.arrowToolbar)
        // init view
        initView()
        // Freebies API Call
        observe(viewModel.getFreebiesItemData, ::handleFreebiesItemResult)

        callFreebiesItemApi()

    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        MyApplication.getInstance().noteRepository.cartValue.observe(this) { totalAmt: Double? ->
            if (totalAmt != null && totalAmt > 0) {
                val sTotalAmount =
                    "<font color=#000000>&#8377; " + DecimalFormat("##.##").format(totalAmt) + "</font>"
                mBinding!!.tvTotalPrice.text = Html.fromHtml(sTotalAmount)
            } else {
                mBinding!!.tvTotalPrice.text = "0"
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (selectedSSCIdFlag) {
            callFreebiesItemApi()
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.back -> {
                startActivity(Intent(this, HomeActivity::class.java))
                Utils.fadeTransaction(this)
            }

            R.id.checkoutBtn -> if (!apiRunning && !handler.hasMessages(0)) {
                startActivity(Intent(this, ShoppingCartActivity::class.java))
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
        super.onBackPressed()
        Utils.fadeTransaction(this)
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

    override fun addToCart(
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
            if (MyApplication.getInstance().noteRepository.isItemInCart(itemId)) {
                MyApplication.getInstance().noteRepository.updateCartItem(model)
            } else {
                MyApplication.getInstance().noteRepository.addToCart(model)
            }
            if (lastItemId == itemId) {
                handler.removeCallbacksAndMessages(null)
                handler.sendMessageDelayed(Message.obtain(handler, lastItemId), 1000)
            } else {
                handler.sendEmptyMessage(lastItemId)
                callAddToCartAPI(itemId, qty, itemUnitPrice, isPrimeItem, onItemClick)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        if (lastItemId != itemId) {
            lastItemId = itemId
        }
    }

    private fun initView() {
        custId = SharePrefs.getInstance(applicationContext).getInt(SharePrefs.CUSTOMER_ID)
        wId = SharePrefs.getInstance(applicationContext).getInt(SharePrefs.WAREHOUSE_ID)
        mBinding!!.rvOffers.setHasFixedSize(true)
        itemListAdapter =
            FreebiesAdapter(
                this,
                list,
                this
            )
        mBinding!!.rvOffers.adapter = itemListAdapter
        mBinding!!.toolbarFreebies.back.setOnClickListener(this)
        mBinding!!.checkoutBtn.setOnClickListener(this)
        mBinding!!.toolbarFreebies.title.text =
            MyApplication.getInstance().dbHelper.getString(R.string.freebies)
        mBinding!!.tvNoOffer.text =
            MyApplication.getInstance().dbHelper.getString(R.string.no_offer)
        mBinding!!.saveTotalItem.text =
            MyApplication.getInstance().dbHelper.getString(R.string.total_amount)
        mBinding!!.checkoutBtn.text =
            MyApplication.getInstance().dbHelper.getString(R.string.checkout)
    }

    fun callFreebiesItemApi() {
        viewModel.getFreebiesItem(custId, wId, LocaleHelper.getLanguage(this), 0, false)
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
                        } else {
                            Utils.setToast(
                                applicationContext, "unable to add cart"
                            )
                            onItemClick!!.onItemClick(0, false)
                        }
                        apiRunning = false
                        if (dialog != null) {
                            dialog!!.cancel()
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
                wId,
                itemId,
                qty,
                itemUnitPrice,
                false,
                false,
                LocaleHelper.getLanguage(this),
                isPrimeItem,
                false
            ),
            "Free Item"
        )
    }


    inner class ComparatorOfNumericString : Comparator<ItemListModel> {
        override fun compare(lhs: ItemListModel, rhs: ItemListModel): Int {
            val i1 = lhs.minOrderQty
            val i2 = rhs.minOrderQty
            return Integer.compare(i1, i2)
        }
    }

    @SuppressLint("HandlerLeak")
    private val handler: Handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            try {
                val model = MyApplication.getInstance().noteRepository.getCartItem1(msg.what)
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

    private fun handleFreebiesItemResult(it: Response<OfferDataModel>) {
        when (it) {
            is Response.Loading -> {
                Utils.showProgressDialog(this)
            }

            is Response.Success -> {
                it.data?.let {
                    Utils.hideProgressDialog()
                    if (it.isStatus) {
                        mBinding!!.rl1.visibility = View.VISIBLE
                        mBinding!!.rlNoOffer.visibility = View.GONE
                        mBinding!!.llOfferList.visibility = View.VISIBLE
                        list.clear()
                        val itemList = it.itemListModels
                        if (itemList!!.size != 0) {
                            for (i in itemList.indices) {
                                var ispresent = false
                                for (j in list.indices) {
                                    if (list[j].itemNumber.equals(
                                            itemList[i].itemNumber,
                                            ignoreCase = true
                                        )
                                    ) {
                                        ispresent = true
                                        if (list[j].moqList.size == 0) {
                                            list[j].moqList.add(list[j])
                                            list[j].moqList[0].isChecked = true
                                        }
                                        list[j].moqList.add(itemList[i])
                                        break
                                    }
                                }
                                if (!ispresent) {
                                    list.add(itemList[i])
                                }
                            }
                        }
                        if (list.size != 0) {
                            Collections.sort(list, ComparatorOfNumericString())
                            itemListAdapter!!.setItemListCategory(list)
                        }
                    } else {
                        mBinding!!.rlNoOffer.visibility = View.VISIBLE
                        mBinding!!.llOfferList.visibility = View.GONE
                    }
                    selectedSSCIdFlag = true
                }
            }

            is Response.Error -> {
                Utils.hideProgressDialog()
                list.clear()
                mBinding!!.rlNoOffer.visibility = View.VISIBLE
                mBinding!!.llOfferList.visibility = View.GONE
            }
        }
    }

}