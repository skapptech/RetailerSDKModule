package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.activity

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.api.CommonClassForAPI
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ActivityMyFavouriteBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.interfaces.OnItemClick
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.adapter.FavItemListAdapter
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.AppHomeItemModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.FavItemsDetails
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.ItemListModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.MyFavModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.CartAddItemModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.SharePrefs
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.shoppingCart.ShoppingCartActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.LocaleHelper
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.RetailerSDKApp
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils
import com.google.gson.JsonObject
import io.reactivex.observers.DisposableObserver
import java.text.DecimalFormat

class FavouriteActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var mBinding: ActivityMyFavouriteBinding
    private var utils: Utils? = null
    private var commonClassForAPI: CommonClassForAPI? = null

    private var mFaveAdapter: FavItemListAdapter? = null
    private val favItemList = ArrayList<FavItemsDetails>()
    private val mItemListArrayList = ArrayList<ItemListModel>()
    private var lang = ""
    private var lastItemId = 0
    private var apiRunning = false
    private var dialog: ProgressDialog? = null
    private var onItemClick: OnItemClick? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMyFavouriteBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        mBinding.toolbarFav.title.text =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.myFavourite)
        mBinding.fragSearchEdt.hint =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.search_item)
        mBinding.checkoutBtn.text =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.checkout)
        mBinding.noOneFav.text =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.no_one_is_favorite_item)
        // back Btn
        mBinding.toolbarFav.back.setOnClickListener(this)
        // check out btn
        mBinding.checkoutBtn.setOnClickListener(this)
        // shimmer
        mBinding.shimmerViewContainer.visibility = View.VISIBLE
        mBinding.shimmerViewContainer.startShimmer()
        lang = LocaleHelper.getLanguage(this)
        utils = Utils(this)
        commonClassForAPI = CommonClassForAPI.getInstance(this)

        // set adapter
        setAdapterValue()
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        RetailerSDKApp.getInstance().noteRepository.tasks.observe(this) { itemListModels: List<FavItemsDetails> ->
            favItemList.clear()
            for (i in itemListModels.indices) favItemList.add(FavItemsDetails(itemListModels[i].itemId))
            // fav json
            callFavAPI()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        Utils.fadeTransaction(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.back -> onBackPressed()
            R.id.checkoutBtn -> if (!apiRunning) {
                startActivity(Intent(applicationContext, ShoppingCartActivity::class.java))
                Utils.leftTransaction(this)
            } else {
                dialog = ProgressDialog(this)
                dialog!!.setMessage("Please wait")
                dialog!!.show()
                dialog!!.setOnCancelListener { dialog1: DialogInterface? ->
                    dialog = null
                    mBinding.checkoutBtn.callOnClick()
                }
            }
        }
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

    private fun callFavAPI() {
        val myFavModel = MyFavModel(
            SharePrefs.getInstance(this).getInt(SharePrefs.CUSTOMER_ID),
            SharePrefs.getInstance(this).getInt(SharePrefs.WAREHOUSE_ID),
            LocaleHelper.getLanguage(this), favItemList
        )
        if (utils!!.isNetworkAvailable) {
            if (commonClassForAPI != null) {
                commonClassForAPI!!.fetchFaveItemList(itemListObserver, myFavModel)
            }
        } else {
            Utils.setToast(
                applicationContext,
                RetailerSDKApp.getInstance().dbHelper.getString(R.string.internet_connection)
            )
        }
    }

    private fun setAdapterValue() {
        mFaveAdapter = FavItemListAdapter(this, mItemListArrayList)
        mBinding.rvFav.adapter = mFaveAdapter
        RetailerSDKApp.getInstance().noteRepository.cartValue.observe(this) { totalAmt: Double? ->
                if (totalAmt != null && totalAmt > 0) {
                    mBinding.tvTotalPrice.text =
                        RetailerSDKApp.getInstance().dbHelper.getData("txt_total_amount") + ":" + DecimalFormat(
                            "##.##"
                        ).format(totalAmt)
                    val count = RetailerSDKApp.getInstance().noteRepository.cartCount
                    mBinding.saveTotalItem.text =
                        RetailerSDKApp.getInstance().dbHelper.getData("itequantity") + " : " + count
                }
            }
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
        if (utils!!.isNetworkAvailable) {
            try {
                model.unitPrice = itemUnitPrice
                model.qty = qty
                model.offerFreeItemQuantity = "" + freeItemQty
                model.offerWalletPoint = totalFreeWalletPoint
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
                    handler.sendEmptyMessage(lastItemId)
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
                RetailerSDKApp.getInstance().dbHelper.getData("internet_connection")
            )
            onItemClick.onItemClick(0, false)
        }
    }

    @SuppressLint("HandlerLeak")
    private val handler: Handler = object : Handler() {
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

    private fun callAddToCartAPI(
        itemId: Int,
        qty: Int,
        itemUnitPrice: Double,
        isPrimeItem: Boolean,
        onItemClick: OnItemClick?
    ) {
        commonClassForAPI!!.postAddCartItem(
            object : DisposableObserver<JsonObject?>() {
                override fun onNext(`object`: JsonObject) {
                    if (`object`["Status"].asBoolean) {
                        onItemClick!!.onItemClick(0, true)
                        Utils.hideProgressDialog()
                    } else {
                        Utils.setToast(
                            applicationContext,
                            RetailerSDKApp.getInstance().dbHelper.getData("unable_to_add_cart")
                        )
                        onItemClick!!.onItemClick(0, false)
                    }
                    apiRunning = false
                    if (dialog != null) {
                        dialog!!.cancel()
                    }
                }

                override fun onError(e: Throwable) {
                    e.printStackTrace()
                    Utils.hideProgressDialog()
                    onItemClick!!.onItemClick(0, false)
                    apiRunning = false
                    if (dialog != null) {
                        dialog!!.cancel()
                    }
                }

                override fun onComplete() {}
            },
            CartAddItemModel(
                SharePrefs.getInstance(applicationContext).getInt(SharePrefs.CUSTOMER_ID),
                SharePrefs.getInstance(this).getInt(SharePrefs.WAREHOUSE_ID),
                itemId, qty, itemUnitPrice, false, false, lang, isPrimeItem, false
            ), "Favorite Item"
        )
    }

    fun getNotfayItems(warehouseId: Int, itemNumber: String?) {
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

    // item List
    private val itemListObserver: DisposableObserver<AppHomeItemModel> =
        object : DisposableObserver<AppHomeItemModel>() {
            override fun onNext(favModel: AppHomeItemModel) {
                Utils.hideProgressDialog()
                mBinding.shimmerViewContainer.stopShimmer()
                mBinding.shimmerViewContainer.visibility = View.INVISIBLE
                if (favModel.itemListModels.size != 0) {
                    mBinding.noOneFav.visibility = View.GONE
                    mBinding.rvFav.visibility = View.VISIBLE
                    mFaveAdapter!!.setFavItemList(favModel.itemListModels)
                } else {
                    mBinding.rl1.visibility = View.GONE
                    mBinding.noOneFav.visibility = View.VISIBLE
                    mBinding.rvFav.visibility = View.GONE
                }
            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
                dispose()
                Utils.hideProgressDialog()
                mBinding.shimmerViewContainer.stopShimmer()
                mBinding.shimmerViewContainer.visibility = View.INVISIBLE
                mBinding.noOneFav.visibility = View.VISIBLE
                mBinding.rvFav.visibility = View.GONE
            }

            override fun onComplete() {}
        }
}