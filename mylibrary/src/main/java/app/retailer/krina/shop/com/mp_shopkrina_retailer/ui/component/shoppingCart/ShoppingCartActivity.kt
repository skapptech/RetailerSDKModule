package app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.shoppingCart

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.text.Html
import android.util.DisplayMetrics
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.api.CommonClassForAPI
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.api.observe
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.CartAddItemModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.ItemListModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.shoppingCart.CartDealResponse
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.shoppingCart.CheckoutCartResponse
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.shoppingCart.CompanyWheelConfig
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.shoppingCart.ShopingCartItemDetailsResponse
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.repository.AppRepository
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.response.Response
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ActivityCheckoutBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ActivityHomeBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.interfaces.BasketFilterClicked
import app.retailer.krina.shop.com.mp_shopkrina_retailer.interfaces.OnItemClick
import app.retailer.krina.shop.com.mp_shopkrina_retailer.interfaces.RemoveItemInterface
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.activity.DialWheelActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.activity.DirectUdharActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.adapter.BasketOfferTiltleAdapter
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.direct.TradeActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.EndPointPref
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.SharePrefs
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.StoryBordSharePrefs
import app.retailer.krina.shop.com.mp_shopkrina_retailer.showcaseviewlib.DismissType
import app.retailer.krina.shop.com.mp_shopkrina_retailer.showcaseviewlib.Gravity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.showcaseviewlib.GuideView
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.auth.MobileSignUpActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.HomeActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.payment.PaymentOptionActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.LocaleHelper
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.MyApplication
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.TextUtils
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONArray
import java.text.DecimalFormat
import java.util.Collections

class ShoppingCartActivity : AppCompatActivity(), BasketFilterClicked, RemoveItemInterface {
    private var mBinding: ActivityCheckoutBinding? = null
    private lateinit var viewModel: ShoppingCartViewModel
    private var commonClassForAPI: CommonClassForAPI? = null
    private val offerItemList: ArrayList<ItemListModel>? = null
    private var allDealList: ArrayList<ItemListModel>? = null
    private var dealList: ArrayList<ItemListModel>? = null
    private var mShoppingItemCartList: ArrayList<ItemListModel>? = ArrayList()
    private var mShopingCart: ShopingCartItemDetailsResponse? = null
    private var utils: Utils? = null
    private var shopingCartAdapter: ShoppingCartAdapter? = null
    private var basketFilterAdapter: BasketFilterAdapter? = null
    private var cartDealAdapter: CartDealAdapter? = null
    private var custId = 0
    private var wId = 0
    private var selectedName = "All"
    private var lang = ""
    private val status = "Error"
    private var flag = true
    private var overlay = false
    private var mGuideView: GuideView? = null
    private var builder: GuideView.Builder? = null
    private var saveQty = 0
    private var saveItemId = 0
    private var saveModel: ItemListModel? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!MyApplication.getInstance().prefManager.isLoggedIn) {
            startActivity(Intent(applicationContext, MobileSignUpActivity::class.java))
            finish()
        }
        if (TextUtils.isNullOrEmpty(
                SharePrefs.getInstance(
                    applicationContext
                ).getString(SharePrefs.CLUSTER_ID)
            )
        ) {
            startActivity(Intent(applicationContext, TradeActivity::class.java))
            finish()
        }
        mBinding = ActivityCheckoutBinding.inflate(layoutInflater)
        setContentView(mBinding!!.root)
        val appRepository = AppRepository(applicationContext)
        viewModel = ViewModelProvider(
            this,
            ShoppingCartViewModelFactory(application, appRepository)
        )[ShoppingCartViewModel::class.java]
        // init view
        initialization()
        // back btn clicked
        mBinding!!.toolbarBasket.back.setOnClickListener { onBackPressed() }
        // add more items btn
        mBinding!!.cartFragAddMoreItems.setOnClickListener {
            startActivity(Intent(applicationContext, HomeActivity::class.java))
            finish()
            Utils.rightTransaction(this)
        }
        // check out button
        mBinding!!.checkoutBtn.setOnClickListener { view: View? ->
            if (SharePrefs.getInstance(this).getBoolean(SharePrefs.IS_UDHAAR_OVERDUE)) {
                viewModel.getUdhaarOverDue(custId, lang)
                viewModel.getUdhaarOverDueData.observe(this) {
                    try {
                        val msg = it["Msg"].asString
                        val isOrder = it["IsOrder"].asBoolean
                        if (!TextUtils.isNullOrEmpty(msg)) {
                            SharePrefs.getInstance(this@ShoppingCartActivity)
                                .putBoolean(SharePrefs.IS_UDHAAR_ORDER, isOrder)
                            checkUdhaarOverDue(msg, isOrder)
                        } else {
                            viewModel.getMinOrderAmount(custId)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            } else {
                viewModel.getMinOrderAmount(custId)
            }
        }
        mBinding!!.toolbarBasket.llCartClear.setOnClickListener { v: View? ->
            if (mShoppingItemCartList != null && mShoppingItemCartList!!.size > 0) {
                cartClearPopup()
            } else {
                Utils.setToast(
                    applicationContext,
                    MyApplication.getInstance().dbHelper.getString(R.string.no_items_available)
                )
            }
        }
        mBinding!!.dialAvailableUse.setOnClickListener {
            if (mBinding!!.dialAvailable.text.toString().equals("0", ignoreCase = true)) {
                Toast.makeText(
                    applicationContext,
                    MyApplication.getInstance().dbHelper.getString(R.string.no_dial_available),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                val i = Intent(applicationContext, DialWheelActivity::class.java)
                i.putExtra("DIAL", mBinding!!.dialAvailable.text.toString())
                startActivity(i)
                Utils.fadeTransaction(this)
            }
        }
        // Refresh cart when offer available
        shoppingCartAPICall()

        // Wheel config
        viewModel.getCompanyWheelConfig(wId)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        callOfferItemApi()
        if (!StoryBordSharePrefs.getInstance(applicationContext)
                .getBoolean(StoryBordSharePrefs.SHOPPINGCART)
        ) {
            appStoryView()
        }
        mBinding!!.nestedScroll.setOnScrollChangeListener { v: NestedScrollView?, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int ->
            if (scrollY > 0) {
                val params = mBinding!!.rlDeal.layoutParams as RelativeLayout.LayoutParams
                params.height = RelativeLayout.LayoutParams.WRAP_CONTENT
                params.addRule(RelativeLayout.BELOW, R.id.rvCartItem)
                params.setMargins(0, 12, 0, 0)
                mBinding!!.rlDeal.layoutParams = params
                mBinding!!.rlDeal.setBackgroundColor(Color.TRANSPARENT)
            }
        }
        if (intent.extras != null && intent.hasExtra("notificationId")) {
            val notificationId = intent.extras!!.getInt("notificationId")
            MyApplication.getInstance().notificationView(notificationId)
            intent.extras!!.clear()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (intent.getBooleanExtra("REVIEW_SCREEN", false)) {
            startActivity(Intent(applicationContext, HomeActivity::class.java))
        } else {
            super.onBackPressed()
        }
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

    override fun onClicked(selectedName: String) {
        this.selectedName = selectedName
        val dataSaved =
            SharePrefs.getInstance(applicationContext).getString(SharePrefs.BASKET_OFFER)
        if (!dataSaved.isEmpty()) {
            try {
                val mOfferList = Gson().fromJson<ArrayList<ItemListModel>>(
                    dataSaved,
                    object : TypeToken<ArrayList<ItemListModel?>?>() {}.type
                )
                retrieveData(mOfferList)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun RemoveItemClicked(itemId: Int) {
        val dialog = Dialog(this, R.style.CustomDialog)
        dialog.setContentView(R.layout.cart_clear_popup)
        val okBtn = dialog.findViewById<TextView>(R.id.ok_btn)
        val cancelBtn = dialog.findViewById<TextView>(R.id.cancel_btn)
        val description = dialog.findViewById<TextView>(R.id.pd_description)
        val pd_title = dialog.findViewById<TextView>(R.id.pd_title)
        pd_title.text = MyApplication.getInstance().dbHelper.getString(R.string.cart_item_clear)
        description.text =
            MyApplication.getInstance().dbHelper.getString(R.string.txt_delete_item_in_cart)
        okBtn.text = MyApplication.getInstance().dbHelper.getString(R.string.ok)
        cancelBtn.text = MyApplication.getInstance().dbHelper.getString(R.string.cancel)
        okBtn.setOnClickListener { v: View? ->
            MyApplication.getInstance().updateAnalyticRCart(
                FirebaseAnalytics.Event.REMOVE_FROM_CART,
                itemId, wId.toString()
            )
            Utils.showProgressDialog(this)
            viewModel.getCartDelResponse(
                CartAddItemModel(
                    custId,
                    wId,
                    itemId,
                    0,
                    0.0,
                    false,
                    true,
                    lang,
                    false,
                    false
                )
            )
            viewModel.getCartDelResponseData.observe(this) {
                Utils.hideProgressDialog()
                try {
                    if (it["Status"].asBoolean) {
                        // update cart data
                        MyApplication.getInstance().noteRepository.deleteCartItem1(
                            itemId
                        )
                        val checkoutCart = Gson().fromJson(
                            it,
                            CheckoutCartResponse::class.java
                        )
                        mShopingCart =
                            checkoutCart.shoppingCartItemDetailsResponse
                        setShoppingCartInAdapter(mShopingCart)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            dialog.dismiss()
        }
        cancelBtn.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

    private fun initialization() {
        observe(viewModel.getMinOrderAmountData, ::handleMinOrderResult)
        observe(viewModel.shoppingCartResponseData, ::handleShoppingCartResult)
        observe(viewModel.getCompanyWheelConfigData, ::handleCompanyWheelResult)
        observe(viewModel.getOfferItemData, ::handleOfferItemResult)
        observe(viewModel.getCartDealItemData, ::handleCartDealItemResult)

        mBinding!!.toolbarBasket.title.text =
            MyApplication.getInstance().dbHelper.getString(R.string.shopping_cart)
        mBinding!!.toolbarBasket.llCartClear.text =
            MyApplication.getInstance().dbHelper.getString(R.string.Clear_all)
        mBinding!!.tvNoCartItem.text =
            MyApplication.getInstance().dbHelper.getString(R.string.Noitemincart)
        mBinding!!.tvMissingSomething.text =
            MyApplication.getInstance().dbHelper.getString(R.string.missed_something)
        mBinding!!.relEmptyItem.text =
            MyApplication.getInstance().dbHelper.getString(R.string.itemavailable)
        mBinding!!.cartFragAddMoreItems.text =
            MyApplication.getInstance().dbHelper.getString(R.string.add_more_items)
        mBinding!!.dialAvailableUse.text =
            MyApplication.getInstance().dbHelper.getString(R.string.use_dial)
        mBinding!!.checkoutBtn.text =
            MyApplication.getInstance().dbHelper.getString(R.string.txt_Continue)
        mBinding!!.tvEmpty.text =
            MyApplication.getInstance().dbHelper.getString(R.string.cart_empty_message)
        mBinding!!.tvCartDeals.text =
            MyApplication.getInstance().dbHelper.getString(R.string.deals_for_you)
        lang = LocaleHelper.getLanguage(this)
        utils = Utils(this)
        val title = ArrayList<String>()
        title.add(getString(R.string.all))
        title.add(getString(R.string.discount))
        title.add(getString(R.string.freebies))
        custId = SharePrefs.getInstance(this).getInt(SharePrefs.CUSTOMER_ID)
        wId = SharePrefs.getInstance(this).getInt(SharePrefs.WAREHOUSE_ID)
        commonClassForAPI = CommonClassForAPI.getInstance(this)
        mBinding!!.rvCategory.isNestedScrollingEnabled = false
        mBinding!!.rvOfferItem.isNestedScrollingEnabled = false
        val basketOfferTiltleAdapter = BasketOfferTiltleAdapter(
            this,
            title, this
        )
        mBinding!!.rvCategory.adapter = basketOfferTiltleAdapter
        basketFilterAdapter = BasketFilterAdapter(this, offerItemList)
        mBinding!!.rvOfferItem.adapter = basketFilterAdapter
        allDealList = ArrayList()
        dealList = ArrayList()
        cartDealAdapter =
            CartDealAdapter(
                this,
                dealList
            )
        mBinding!!.rvDeals.adapter = cartDealAdapter
        mBinding!!.rvCartItem.isNestedScrollingEnabled = false
        shopingCartAdapter = ShoppingCartAdapter(
            this, mShoppingItemCartList,
            this@ShoppingCartActivity
        )
        mBinding!!.rvCartItem.adapter = shopingCartAdapter
    }

    private fun cartClearPopup() {
        val dialog = Dialog(this, R.style.CustomDialog)
        dialog.setContentView(R.layout.cart_clear_popup)
        val okBtn = dialog.findViewById<TextView>(R.id.ok_btn)
        val pd_title = dialog.findViewById<TextView>(R.id.pd_title)
        val cancelBtn = dialog.findViewById<TextView>(R.id.cancel_btn)
        val description = dialog.findViewById<TextView>(R.id.pd_description)
        pd_title.text = MyApplication.getInstance().dbHelper.getString(R.string.cart_item_clear)
        description.text = MyApplication.getInstance().dbHelper.getString(R.string.txt_cart_clear)
        okBtn.text = MyApplication.getInstance().dbHelper.getString(R.string.ok)
        cancelBtn.text = MyApplication.getInstance().dbHelper.getString(R.string.cancel)
        okBtn.setOnClickListener { v: View? ->
            Utils.showProgressDialog(this)
            viewModel.clearCartItem(
                SharePrefs.getInstance(applicationContext)
                    .getInt(SharePrefs.CUSTOMER_ID),
                SharePrefs.getInstance(
                    applicationContext
                ).getInt(SharePrefs.WAREHOUSE_ID)
            )
            viewModel.getClearCartItemData.observe(this) {
                Utils.hideProgressDialog()
                if (it["Status"].asBoolean) {
                    clearCartData()
                    startActivity(
                        Intent(
                            applicationContext,
                            HomeActivity::class.java
                        )
                    )
                    Utils.fadeTransaction(this@ShoppingCartActivity)
                    dialog.dismiss()
                }
            }
        }
        cancelBtn.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

    private fun callOfferItemApi() {
        viewModel.getOfferItem(
            SharePrefs.getInstance(applicationContext).getInt(SharePrefs.CUSTOMER_ID),
            SharePrefs.getInstance(
                applicationContext
            ).getInt(SharePrefs.WAREHOUSE_ID),
            lang
        )
    }

    private fun shoppingCartAPICall() {
        viewModel.getCustomerCart(custId, wId, lang, "ShoppingCart")
    }

    private fun callCartDealAPI() {
        viewModel.getCartDealItem(custId, wId, 0, 5, lang)
    }

    private fun appStoryView() {
        builder = GuideView.Builder(this)
            .setTitle(MyApplication.getInstance().dbHelper.getString(R.string.clear_cart))
            .setContentText(MyApplication.getInstance().dbHelper.getString(R.string.clear_cart_deatil))
            .setGravity(Gravity.center)
            .setDismissType(DismissType.anywhere)
            .setTargetView(mBinding!!.toolbarBasket.llCartClear)
            .setGuideListener { view: View ->
                when (view.id) {
                    R.id.ll_cart_clear -> builder!!.setTitle("").setContentText(
                        MyApplication.getInstance().dbHelper.getString(
                            R.string.checkout_btn
                        )
                    ).setTargetView(
                        mBinding!!.checkoutBtn
                    ).build()

                    R.id.checkoutBtn -> {
                        StoryBordSharePrefs.getInstance(applicationContext)
                            .putBoolean(StoryBordSharePrefs.SHOPPINGCART, true)
                        return@setGuideListener
                    }
                }
                mGuideView = builder!!.build()
                mGuideView!!.show()
            }
        mGuideView = builder!!.build()
        mGuideView!!.show()
    }

    private fun updateDealItems() {
        if (allDealList != null && allDealList!!.size != 0) {
            dealList!!.clear()
            for (model in allDealList!!) {
                var exist = false
                for (i in mShoppingItemCartList!!.indices) {
                    if (model.itemId == mShoppingItemCartList!![i].itemId) {
                        exist = true
                        break
                    }
                }
                if (!exist) dealList!!.add(model)
            }
            cartDealAdapter!!.notifyDataSetChanged()
            if (dealList!!.size > 0) {
                println("canScroll " + canScroll())
                val params = mBinding!!.rlDeal.layoutParams as RelativeLayout.LayoutParams
                if (canScroll() && !overlay) {
                    val displayMetrics = DisplayMetrics()
                    windowManager.defaultDisplay.getMetrics(displayMetrics)
                    val height = (displayMetrics.heightPixels / displayMetrics.density).toInt()
                    println(height)
                    params.setMargins(0, if (height == 0) 800 else height + 230, 0, 0)
                    //                    mBinding.rlDeal.setBackgroundColor(Color.WHITE);
                } else {
                    params.height = RelativeLayout.LayoutParams.WRAP_CONTENT
                    params.addRule(RelativeLayout.BELOW, R.id.rvCartItem)
                }
                mBinding!!.rlDeal.layoutParams = params
                overlay = true
            } else {
                mBinding!!.rlDeal.visibility = View.GONE
            }
        } else {
            mBinding!!.rlDeal.visibility = View.GONE
        }
    }

    private fun canScroll(): Boolean {
        val child = mBinding!!.nestedScroll.getChildAt(0)
        if (child != null) {
            val childHeight = child.height
            return mBinding!!.nestedScroll.height < childHeight
        }
        return false
    }

    private inner class ComparatorOfNumericString : Comparator<ItemListModel> {
        @SuppressLint("NewApi")
        override fun compare(lhs: ItemListModel, rhs: ItemListModel): Int {
            val i1 = lhs.minOrderQty
            val i2 = rhs.minOrderQty
            return Integer.compare(i1, i2)
        }
    }

    // add items to card
    fun addItemInCartItemArrayList(
        itemId: Int,
        qty: Int,
        itemUnitPrice: Double,
        model: ItemListModel,
        FreeItemQuantity: Int,
        FreeTotalWalletPoint: Double,
        isPrimeItem: Boolean,
        isDealItem: Boolean,
        onItemClick: OnItemClick,
        CartRefress: Boolean
    ): String {
        saveQty = qty
        saveItemId = itemId
        saveModel = model
        viewModel.addItemInCartData.observe(this) {
            when (it) {
                is Response.Loading -> {}
                is Response.Success -> {
                    it.data?.let {
                        Utils.hideProgressDialog()
                        if (it["Status"].asBoolean) {
                            try {
                                // update cart database
                                if (saveQty == 0) {
                                    MyApplication.getInstance().noteRepository.deleteCartItem1(
                                        saveModel!!.itemId
                                    )
                                } else {
                                    saveModel!!.qty = saveQty
                                    if (MyApplication.getInstance().noteRepository.isItemInCart(
                                            saveItemId
                                        )
                                    ) {
                                        MyApplication.getInstance().noteRepository.updateCartItem(
                                            saveModel
                                        )
                                    } else {
                                        MyApplication.getInstance().noteRepository.addToCart(
                                            saveModel
                                        )
                                    }
                                }
                                val checkoutCart =
                                    Gson().fromJson(it, CheckoutCartResponse::class.java)
                                mShopingCart = checkoutCart.shoppingCartItemDetailsResponse
                                setTotalItemValue(mShopingCart)
                                if (CartRefress) {
                                    if (mShopingCart!!.shoppingCartItemDcs!!.size != 0) {
                                        mBinding!!.rl2.visibility = View.VISIBLE
                                        mBinding!!.rvCartItem.visibility = View.VISIBLE
                                        mBinding!!.liEmpty.visibility = View.GONE
                                        mShoppingItemCartList =
                                            sortAndAddSections(mShopingCart!!.shoppingCartItemDcs)
                                        shopingCartAdapter!!.setShopingCartItem(
                                            mShoppingItemCartList
                                        )
                                    }
                                }
                                if (isDealItem) {
                                    updateDealItems()
                                }
                                //  shopingCartAdapter.notifyDataSetChanged();
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        } else {
                            Utils.setToast(
                                applicationContext,
                                MyApplication.getInstance().dbHelper.getString(R.string.unable_to_add_cart)
                            )
                            onItemClick.onItemClick(0, false)
                        }
                    }
                }

                is Response.Error -> {
                    Utils.hideProgressDialog()
                    onItemClick.onItemClick(0, false)
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
                true,
                lang,
                isPrimeItem,
                isDealItem
            ),
            "Shopping Item"
        )

        return status
    }


    private fun setTotalItemValue(mShopingCart: ShopingCartItemDetailsResponse?) {
        mBinding!!.tvTotalPrice.text =
            (MyApplication.getInstance().dbHelper.getString(R.string.total_amount)
                    + " " + DecimalFormat("##.##")
                .format(mShopingCart!!.cartTotalAmt + mShopingCart.totalDiscountAmt))
        mBinding!!.tvTotalItemQuantity.text =
            MyApplication.getInstance().dbHelper.getString(R.string.total_item) + " " + mShopingCart.totalQty
        //tvCartFragTotalDpPointTv.setText((getString(R.string.total_dreamPoint) + mShopingCart.getDeamPoint()));
        mBinding!!.dialAvailable.text = "" + mShopingCart.wheelCount
        mBinding!!.cartFragDeliveryChargesTv.text =
            MyApplication.getInstance().dbHelper.getString(R.string.delivery_charges) + " " + mShopingCart.deliveryCharges
    }

    private fun clearCartData() {
        MyApplication.getInstance().noteRepository.truncateCart()
        MyApplication.getInstance().updateAnalytics("clear_cart")
    }

    private fun retrieveData(list: ArrayList<ItemListModel>) {
        offerItemList!!.clear()
        if (list.size != 0) {
            for (i in list.indices) {
                if (selectedName.equals(getString(R.string.all), ignoreCase = true)) {
                    offerItemList.add(list[i])
                    if (offerItemList.size == 0) {
                        offerItemList.add(list[i])
                    } else {
                        var ispresent = false
                        for (j in offerItemList.indices) {
                            if (offerItemList[j].itemNumber.equals(
                                    list[i].itemNumber,
                                    ignoreCase = true
                                )
                            ) {
                                ispresent = true
                                break
                            }
                        }
                        if (!ispresent) {
                            offerItemList.add(list[i])
                        }
                    }
                } else if (selectedName.equals(getString(R.string.discount), ignoreCase = true)) {
                    if (list[i].isOffer) {
                        if (list[i].offerType.equals("WalletPoint", ignoreCase = true)) {
                            offerItemList.add(list[i])
                            if (offerItemList.size == 0) {
                                offerItemList.add(list[i])
                            } else {
                                var ispresent = false
                                for (j in offerItemList.indices) {
                                    if (offerItemList[j].itemNumber.equals(
                                            list[i].itemNumber,
                                            ignoreCase = true
                                        )
                                    ) {
                                        ispresent = true
                                        break
                                    }
                                }
                                if (!ispresent) {
                                    offerItemList.add(list[i])
                                }
                            }
                        } else {
                            mBinding!!.rvOfferItem.visibility = View.GONE
                            mBinding!!.relEmptyItem.visibility = View.VISIBLE
                        }
                    } else {
                        mBinding!!.rvOfferItem.visibility = View.GONE
                        mBinding!!.relEmptyItem.visibility = View.VISIBLE
                    }
                } else if (selectedName.equals(getString(R.string.freebies), ignoreCase = true)) {
                    if (list[i].isOffer) {
                        if (list[i].offerType.equals("ItemMaster", ignoreCase = true)) {
                            offerItemList.add(list[i])
                            if (offerItemList.size == 0) {
                                offerItemList.add(list[i])
                            } else {
                                var ispresent = false
                                for (j in offerItemList.indices) {
                                    if (offerItemList[j].itemNumber.equals(
                                            list[i].itemNumber,
                                            ignoreCase = true
                                        )
                                    ) {
                                        ispresent = true
                                        break
                                    }
                                }
                                if (!ispresent) {
                                    offerItemList.add(list[i])
                                }
                            }
                        } else {
                            mBinding!!.rvOfferItem.visibility = View.GONE
                            mBinding!!.relEmptyItem.visibility = View.VISIBLE
                        }
                    } else {
                        mBinding!!.rvOfferItem.visibility = View.GONE
                        mBinding!!.relEmptyItem.visibility = View.VISIBLE
                    }
                }
            }
            if (list.size != 0) {
                Collections.sort(list, ComparatorOfNumericString())
                basketFilterAdapter!!.setItemListCategory(list)
                basketFilterAdapter!!.notifyDataSetChanged()
                mBinding!!.rvOfferItem.visibility = View.VISIBLE
                mBinding!!.relEmptyItem.visibility = View.GONE
            }
        } else {
            mBinding!!.rvOfferItem.visibility = View.GONE
            mBinding!!.llMissedSmnt.visibility = View.GONE
        }
    }

    private fun setShoppingCartInAdapter(mShopingCart: ShopingCartItemDetailsResponse?) {
        mShoppingItemCartList = sortAndAddSections(mShopingCart!!.shoppingCartItemDcs)
        if (mShoppingItemCartList != null) {
            if (mShoppingItemCartList!!.size != 0) {
                mBinding!!.rl2.visibility = View.VISIBLE
                mBinding!!.rvCartItem.visibility = View.VISIBLE
                mBinding!!.liEmpty.visibility = View.GONE
                setShopingCartAdapter(mShopingCart)
            } else {
                mBinding!!.rvCartItem.visibility = View.GONE
                if (allDealList!!.size == 0) mBinding!!.liEmpty.visibility = View.VISIBLE
                mBinding!!.rl2.visibility = View.GONE
                shopingCartAdapter!!.setShopingCartItem(mShoppingItemCartList)
                clearCartData()
            }
        } else {
            mBinding!!.rvCartItem.visibility = View.GONE
            if (allDealList!!.size == 0) mBinding!!.liEmpty.visibility = View.VISIBLE
            mBinding!!.rl2.visibility = View.GONE
            clearCartData()
        }
        updateDealItems()
    }

    private fun setShopingCartAdapter(mCart: ShopingCartItemDetailsResponse?) {
        try {
            //mCart is Backend response cart
            val list = mCart!!.shoppingCartItemDcs
            for (i in list!!.indices) {
                val isSuccess = list[i].isSuccess
                val msg = list[i].message
                if (!isSuccess && msg != null && flag) {
                    Utils.setToast(
                        applicationContext, list[i].message
                    )
                    flag = false
                }
            }
            if (list.size != 0) {
                shopingCartAdapter!!.setShopingCartItem(mShoppingItemCartList)
                setTotalItemValue(mShopingCart)
                MyApplication.getInstance().noteRepository.addToCart(mShoppingItemCartList)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun sortAndAddSections(itemList: ArrayList<ItemListModel>?): ArrayList<ItemListModel> {
        Collections.sort(itemList)
        val tempList = ArrayList<ItemListModel>()
        //First we sort the array
        // Collections.sort(itemList);
        //Loops thorugh the list and add a section before each sectioncell start
        var header: String? = ""
        for (i in itemList!!.indices) {
            //If it is the st
            // art of a new section we create a new listcell and add it to our array
            if (!header.equals(itemList[i].storeName, ignoreCase = true)) {
                val model = ItemListModel(itemList[i].storeName)
                model.setToSectionHeader()
                tempList.add(model)
                header = itemList[i].storeName
            }
            tempList.add(itemList[i])
        }
        return tempList
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
            btnOrder.setOnClickListener { view: View? ->
                viewModel.getMinOrderAmount(custId)
                dialog.dismiss()
            }
        }
        tvMsg!!.text = textMsg
        btnUdharPayNow!!.setOnClickListener { view: View? ->
            callLeadApi()
            dialog.dismiss()
        }
        imClose!!.setOnClickListener { view: View? -> dialog.dismiss() }
        dialog.show()
    }

    private fun callLeadApi() {
        var url =
            EndPointPref.getInstance(this).baseUrl + "/api/Udhar/GenerateLead?CustomerId=[CustomerId]"
        Utils.showProgressDialog(this)
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
        viewModel.generateLead(url)
        viewModel.generateLeadData.observe(this) {
            Utils.hideProgressDialog()
            val isSuccess = it["Result"].asBoolean
            if (isSuccess) {
                startActivity(
                    Intent(
                        applicationContext,
                        DirectUdharActivity::class.java
                    ).putExtra("url", it["Data"].asString)
                )
            } else {
                val msg = it["Msg"].asString
                AlertDialog.Builder(this@ShoppingCartActivity).setTitle(
                    MyApplication.getInstance().dbHelper.getString(
                        R.string.alert
                    )
                ).setMessage(msg).setNegativeButton(getString(R.string.ok), null).show()
            }
        }
    }

    private fun handleMinOrderResult(it: Response<Double>) {
        when (it) {
            is Response.Loading -> {
                Utils.showProgressDialog(this)
            }

            is Response.Success -> {
                it.data?.let {
                    Utils.hideProgressDialog()
                    if (it != 0.0 && mShopingCart!!.cartTotalAmt > 0) {
                        if (mShopingCart!!.cartTotalAmt + mShopingCart!!.totalDiscountAmt < it) {
                            val mView = layoutInflater.inflate(R.layout.checkout_popup, null)
                            val customDialog =
                                Dialog(this@ShoppingCartActivity, R.style.CustomDialog)
                            customDialog.setContentView(mView)
                            val okBtn = mView.findViewById<TextView>(R.id.ok_btn)
                            val description = mView.findViewById<TextView>(R.id.pd_description)
                            val title = mView.findViewById<TextView>(R.id.pd_title)
                            title.text =
                                MyApplication.getInstance().dbHelper.getString(R.string.msg_yourorder)
                            okBtn.text =
                                MyApplication.getInstance().dbHelper.getString(R.string.msg_addmore)
                            description.text =
                                (MyApplication.getInstance().dbHelper.getString(R.string.msg_orderlessthan700) +
                                        " " + Html.fromHtml(
                                    "<font color=#424242>&#8377; " +
                                            DecimalFormat("##.##").format(it)
                                ) + " "
                                        + MyApplication.getInstance().dbHelper.getString(R.string.msg_orderless))
                            okBtn.setOnClickListener {
                                customDialog.dismiss()
                                startActivity(Intent(applicationContext, HomeActivity::class.java))
                                finish()
                                Utils.fadeTransaction(this@ShoppingCartActivity)
                            }
                            customDialog.show()
                        } else {
                            SharePrefs.getInstance(applicationContext)
                                .putString(
                                    SharePrefs.AVAIL_DIAL,
                                    mBinding!!.dialAvailable.text.toString()
                                )
                            val args = Bundle()
                            args.putSerializable("SHOPING_CART", mShopingCart)
                            startActivity(
                                Intent(applicationContext, PaymentOptionActivity::class.java)
                                    .putExtras(args)
                                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            )
                            finish()
                            Utils.fadeTransaction(this@ShoppingCartActivity)
                        }
                    } else {
                        SharePrefs.getInstance(applicationContext)
                            .putString(
                                SharePrefs.AVAIL_DIAL,
                                mBinding!!.dialAvailable.text.toString()
                            )
                        val args = Bundle()
                        args.putSerializable("SHOPING_CART", mShopingCart)
                        startActivity(
                            Intent(applicationContext, PaymentOptionActivity::class.java).putExtras(
                                args
                            )
                                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        )
                        Utils.fadeTransaction(this@ShoppingCartActivity)
                    }
                }
            }

            is Response.Error -> {
                Utils.setToast(this, it.errorMesssage.toString())
                Utils.hideProgressDialog()
            }
        }
    }

    private fun handleShoppingCartResult(it: Response<CheckoutCartResponse>) {
        when (it) {
            is Response.Loading -> {
                mBinding!!.liLoader.visibility = View.VISIBLE
            }

            is Response.Success -> {
                it.data?.let {
                    mBinding!!.liLoader.visibility = View.GONE
                    try {
                        if (it.status) {
                            mShopingCart = it.shoppingCartItemDetailsResponse
                            setShoppingCartInAdapter(mShopingCart)
                            if (mShopingCart!!.shoppingCartItemDcs != null) MyApplication.getInstance()
                                .updateAnalyticVC(
                                    mShopingCart!!.cartTotalAmt, mShopingCart!!.shoppingCartItemDcs
                                )
                        } else {
                            mBinding!!.rvCartItem.visibility = View.GONE
                            mBinding!!.liEmpty.visibility = View.VISIBLE
                            mBinding!!.rl2.visibility = View.GONE
                            shopingCartAdapter!!.setShopingCartItem(mShoppingItemCartList)
                            clearCartData()
                        }
                        callCartDealAPI()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                }
            }

            is Response.Error -> {
                Utils.setToast(this, it.errorMesssage.toString())
                mBinding!!.liLoader.visibility = View.GONE
            }
        }
    }

    private fun handleCompanyWheelResult(it: Response<CompanyWheelConfig>) {
        when (it) {
            is Response.Loading -> {}
            is Response.Success -> {
                it.data?.let {
                    var wheelMgs = ""
                    val lineItem = it.lineItemCount
                    val OrderAmount = it.orderAmount
                    if (it.isKPPRequiredWheel && SharePrefs.getInstance(this@ShoppingCartActivity)
                            .getBoolean(SharePrefs.IsKPP) || !SharePrefs.getInstance(this@ShoppingCartActivity)
                            .getBoolean(SharePrefs.IsKPP)
                    ) {
                        if (lineItem > 0 && OrderAmount > 0) {
                            mBinding!!.tvWheelConfig.visibility = View.VISIBLE
                            wheelMgs =
                                (MyApplication.getInstance().dbHelper.getString(R.string.free_wheel_on_min_order) + " "
                                        + DecimalFormat("##.##").format(Math.round(OrderAmount))
                                        + MyApplication.getInstance().dbHelper.getString(R.string.and) +
                                        lineItem + MyApplication.getInstance().dbHelper.getString(R.string.line_items))
                            mBinding!!.tvWheelConfig.text = wheelMgs
                        } else if (lineItem > 0) {
                            mBinding!!.tvWheelConfig.visibility = View.VISIBLE
                            wheelMgs =
                                MyApplication.getInstance().dbHelper.getString(R.string.free_wheel) +
                                        " " + lineItem + MyApplication.getInstance().dbHelper.getString(
                                    R.string.line_items
                                )
                            mBinding!!.tvWheelConfig.text = wheelMgs
                        } else if (OrderAmount > 0) {
                            mBinding!!.tvWheelConfig.visibility = View.VISIBLE
                            wheelMgs =
                                (MyApplication.getInstance().dbHelper.getString(R.string.free_wheel_on_min_order) + " "
                                        + MyApplication.getInstance().dbHelper.getString(R.string.and)
                                        + DecimalFormat("##.##").format(Math.round(OrderAmount)))
                            mBinding!!.tvWheelConfig.text = wheelMgs
                        } else {
                            mBinding!!.tvWheelConfig.visibility = View.GONE
                        }
                    } else {
                        mBinding!!.tvWheelConfig.visibility = View.GONE
                    }

                }
            }

            is Response.Error -> {
                Utils.setToast(this, it.errorMesssage.toString())
            }
        }
    }

    private fun handleOfferItemResult(it: Response<ArrayList<ItemListModel>>) {
        when (it) {
            is Response.Loading -> {}
            is Response.Success -> {
                it.data?.let {
                    val jsonInString = Gson().toJson(it)
                    val mJsonArray = JSONArray(jsonInString)
                    if (it.size != 0) {
                        SharePrefs.getInstance(applicationContext)
                            .putString(SharePrefs.BASKET_OFFER, mJsonArray.toString())
                        offerItemList!!.clear()
                        mBinding!!.rvOfferItem.visibility = View.VISIBLE
                        mBinding!!.llMissedSmnt.visibility = View.VISIBLE
                        retrieveData(it)
                    } else {
                        mBinding!!.rvOfferItem.adapter = null
                        mBinding!!.rvOfferItem.visibility = View.GONE
                        mBinding!!.llMissedSmnt.visibility = View.GONE
                    }

                }
            }

            is Response.Error -> {
                Utils.setToast(this, it.errorMesssage.toString())
            }
        }
    }

    private fun handleCartDealItemResult(it: Response<CartDealResponse>) {
        when (it) {
            is Response.Loading -> {}
            is Response.Success -> {
                it.data?.let {
                    if (it.itemDataDCs != null && it.itemDataDCs.size != 0) {
                        mBinding!!.liEmpty.visibility = View.GONE
                        mBinding!!.rlDeal.visibility = View.VISIBLE
                        dealList!!.clear()
                        allDealList = it.itemDataDCs
                        updateDealItems()
                    } else {
                        mBinding!!.rlDeal.visibility = View.GONE
                    }
                }
            }

            is Response.Error -> {
                Utils.setToast(this, it.errorMesssage.toString())
            }
        }
    }
}