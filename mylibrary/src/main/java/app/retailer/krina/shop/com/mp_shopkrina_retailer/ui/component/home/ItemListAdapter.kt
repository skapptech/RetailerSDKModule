package app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home

import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.os.Handler
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.ItemListModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.CategoryOrderItemsBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.interfaces.AdapterInterface
import app.retailer.krina.shop.com.mp_shopkrina_retailer.interfaces.MoqInterface
import app.retailer.krina.shop.com.mp_shopkrina_retailer.interfaces.OnItemClick
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.activity.MembershipPlanActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.NotifyModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.SharePrefs
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.CategoryItemOrderInfo
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.productDetails.ProductDetailsActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.CustomRunnable
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.MyApplication
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.TextUtils
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.analytics.FirebaseAnalytics
import com.squareup.picasso.Picasso
import org.json.JSONObject
import pl.droidsonroids.gif.GifImageView
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class ItemListAdapter(
    private val activity: HomeActivity,
    private var list: MutableList<ItemListModel>?
) : RecyclerView.Adapter<ItemListAdapter.ViewHolder>(), OnItemClick, MoqInterface {
    private val handler = Handler()
    private var adapter: MoqAdapter? = null
    private var flashOfferFlag = false
    private var customRunnable: CustomRunnable? = null
    private var progressBar: GifImageView? = null

    init {
        notifyDataSetChanged()
    }

    fun setItemListCategory(list: ArrayList<ItemListModel>?) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        return ViewHolder(
            DataBindingUtil.inflate<CategoryOrderItemsBinding>(
                LayoutInflater.from(viewGroup.context),
                R.layout.category_order_items, viewGroup, false
            )
        )
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        val model = list!![i]

        // set String
        viewHolder.mBinding.tvRemainingQtyText.text =
            MyApplication.getInstance().dbHelper.getString(
                R.string.remaining_qty
            ) + " "
        viewHolder.mBinding.tvItemLeftText.text =
            MyApplication.getInstance().dbHelper.getString(R.string.item_left)
        viewHolder.mBinding.tvEndInText.text =
            MyApplication.getInstance().dbHelper.getString(R.string.end_in_text)
        viewHolder.mBinding.tvFreeItemNotActiveText.text =
            MyApplication.getInstance().dbHelper.getString(
                R.string.inactive_customer_msg
            )
        viewHolder.mBinding.addItemBtn.text =
            MyApplication.getInstance().dbHelper.getString(R.string.add_btn)
        viewHolder.mBinding.btItemNotyfy.setOnClickListener { v: View? ->
            activity.getNotifyItems(model.warehouseId, model.itemNumber)
            MyApplication.getInstance().noteRepository.insertNotifyItemTask(NotifyModel(model.itemId))
            viewHolder.mBinding.btItemNotyfy.background =
                activity.resources.getDrawable(R.drawable.background_for_buttons_disble)
            viewHolder.mBinding.btItemNotyfy.isClickable = false
            viewHolder.mBinding.btItemNotyfy.isEnabled = false
        }

        // set MOQ
        if (model.moqList != null && model.moqList.size > 0) {
            viewHolder.mBinding.tvMoq.visibility = View.GONE
            viewHolder.mBinding.tvMultiMoq.visibility = View.VISIBLE
        } else {
            viewHolder.mBinding.tvMoq.visibility = View.VISIBLE
            viewHolder.mBinding.tvMultiMoq.visibility = View.GONE
        }
        viewHolder.mBinding.tvMoq.text =
            (MyApplication.getInstance().dbHelper.getString(R.string.item_moq)
                    + " " + model.minOrderQty)
        viewHolder.mBinding.tvMultiMoq.text =
            (MyApplication.getInstance().dbHelper.getString(R.string.item_moq)
                    + " " + model.minOrderQty)
        if (!TextUtils.isNullOrEmpty(model.scheme)) {
            viewHolder.mBinding.tvSchemeText.visibility = View.VISIBLE
            viewHolder.mBinding.tvSchemeText.text = "" + model.scheme
        } else {
            viewHolder.mBinding.tvSchemeText.visibility = View.GONE
        }
        viewHolder.mBinding.tvLeftItems.setBackgroundResource(R.drawable.ic_count_bg)
        viewHolder.mBinding.tvRemaingSecand.setBackgroundResource(R.drawable.ic_count_bg)
        //set  value on UI
        setValueInUI(viewHolder, model)
        model.isChecked = true
        // Minus Btn clicked
        viewHolder.mBinding.minusBtn.setOnClickListener { view: View? ->
            //click effect
            Utils.buttonEffect(viewHolder.mBinding.minusBtn)
            itemAddRemove(viewHolder, model, false, false)
        }
        //plus Btn clicked
        viewHolder.mBinding.plusBtn.setOnClickListener { view: View? ->
            Utils.buttonEffect(viewHolder.mBinding.plusBtn)
            itemAddRemove(viewHolder, model, true, false)
        }
        // fav section
        viewHolder.mBinding.favItem.setOnClickListener { v: View? ->
            if (MyApplication.getInstance().noteRepository.isItemWishList(model.itemId)) {
                viewHolder.mBinding.favItem.setImageResource(R.drawable.ic_favourite)
                MyApplication.getInstance().noteRepository.deleteTask(model)
                Utils.addFav(model.itemId, false, activity)
            } else {
                viewHolder.mBinding.favItem.setImageResource(R.drawable.ic_favorite_red)
                MyApplication.getInstance().noteRepository.insertTask(model)
                Utils.addFav(model.itemId, true, activity)
                MyApplication.getInstance().analyticAddWishList(model)
            }
        }
        // Add Btn clicked
        viewHolder.mBinding.addItemBtn.setOnClickListener { v: View? ->
            viewHolder.mBinding.visible.visibility = View.VISIBLE
            viewHolder.mBinding.addItemBtn.visibility = View.GONE
            itemAddRemove(viewHolder, model, true, true)
            // analytics for add to cart
            MyApplication.getInstance()
                .updateAnalyticsCart(FirebaseAnalytics.Event.ADD_TO_CART, model)
        }
        // checkout clicked
        viewHolder.mBinding.LLItemMain.setOnClickListener { v: View? ->
            viewHolder.mBinding.LLItemMain.isClickable = false
            detailsScree(model, viewHolder)
        }
        // MOQ popup open here
        viewHolder.mBinding.tvMultiMoq.setOnClickListener { v: View? ->
            val dialog = BottomSheetDialog(activity, R.style.Theme_Design_BottomSheetDialog)
            val dialogLayout = LayoutInflater.from(activity).inflate(R.layout.moq_price_popup, null)
            dialog.setContentView(dialogLayout)
            val item_name = dialogLayout.findViewById<TextView>(R.id.itemName)
            val tvDSelectQty = dialogLayout.findViewById<TextView>(R.id.tvDSelectQty)
            val tvDMoq = dialogLayout.findViewById<TextView>(R.id.tvDMoq)
            val tvDMrp = dialogLayout.findViewById<TextView>(R.id.tvDMrp)
            val tvDRs = dialogLayout.findViewById<TextView>(R.id.tvDRs)
            val tvDMargin = dialogLayout.findViewById<TextView>(R.id.tvDMargin)
            val ivClose = dialogLayout.findViewById<ImageView>(R.id.ivClose)
            ivClose.setOnClickListener { v1: View? -> dialog.dismiss() }
            tvDSelectQty.text =
                MyApplication.getInstance().dbHelper.getString(R.string.select_quantities_for)
            tvDMoq.text = MyApplication.getInstance().dbHelper.getString(R.string.moq)
            tvDMrp.text = MyApplication.getInstance().dbHelper.getString(R.string.mrp)
            tvDRs.text = MyApplication.getInstance().dbHelper.getString(R.string.rs)
            tvDMargin.text = MyApplication.getInstance().dbHelper.getString(R.string.margins_d)
            item_name.text = list!![i].itemname
            val mMoqPriceList = dialogLayout.findViewById<ListView>(R.id.listview_moq_price)
            val listener = AdapterInterface { pos: Int ->
                val moq = list!![i].moqList
                list!![i] = list!![i].moqList[pos]
                list!![i].moqList = moq
                for (j in list!![i].moqList.indices) {
                    list!![i].moqList[j].isChecked = pos == j
                }
                list!![i].moqList[pos].isChecked = true
                notifyDataSetChanged()
                handler.postDelayed({
                    //Do something after 100ms
                    dialog.dismiss()
                }, 100)
            }
            adapter = MoqAdapter(activity, model.moqList, listener)
            mMoqPriceList.adapter = adapter
            dialog.show()
        }
        viewHolder.mBinding.tvUnlock.setOnClickListener { v: View? ->
            if (!SharePrefs.getInstance(
                    activity
                ).getBoolean(SharePrefs.IS_PRIME_MEMBER) && list!![i].isPrimeItem
            ) {
                activity.startActivity(Intent(activity, MembershipPlanActivity::class.java))
            }
        }
    }

    override fun getItemCount(): Int {
        return if (list == null) 0 else list!!.size
    }

    private fun detailsScree(model: ItemListModel, viewHolder: ViewHolder) {
        val args = Bundle()
        val intent = Intent(activity, ProductDetailsActivity::class.java)
        intent.putExtra("PRODUCT_IMAGE", model.logoUrl)
        intent.putExtra("PRODUCT_NAME", model.itemname)
        intent.putExtra("PRODUCT_PRICE", model.unitPrice)
        intent.putExtra("PRODUCT_MOQ", model.minOrderQty)
        intent.putExtra("PRODUCT_ITEM_ID", model.itemId)
        intent.putExtra("PRODUCT_DP", model.dreamPoint)
        intent.putExtra("WAREHOUSE_ID", model.warehouseId)
        intent.putExtra("COMPANY_ID", model.companyId)
        intent.putExtra("PRICE", model.price)
        intent.putExtra("MARGIN_POINT", model.marginPoint)
        intent.putExtra("NUMBER", model.itemNumber)
        intent.putExtra("ItemMultiMRPId", model.itemMultiMRPId)
        intent.putExtra("remainingqty", viewHolder.mBinding.availQty.text)
        args.putSerializable("ITEM_LIST", model)
        intent.putExtras(args)
        activity.startActivity(intent)
        Utils.leftTransaction(activity)
    }

    private fun setValueInUI(viewHolder: ViewHolder, model: ItemListModel) {
        /*Fav section*/
        flashOfferFlag = false

        // check item in wishList
        if (MyApplication.getInstance().noteRepository.isItemWishList(model.itemId)) {
            viewHolder.mBinding.favItem.setImageResource(R.drawable.ic_favorite_red)
        } else {
            viewHolder.mBinding.favItem.setImageResource(R.drawable.ic_favourite)
        }
        try {
            viewHolder.mBinding.tvItemName.text = model.itemname
            //set offer UI
            if (model.isOffer) {
                /*for inactive customer*/
                if (!SharePrefs.getInstance(activity).getBoolean(SharePrefs.CUST_ACTIVE)) {
                    viewHolder.mBinding.llInActiveUser.visibility = View.VISIBLE
                }
                viewHolder.mBinding.tvOffer.visibility = View.GONE
                viewHolder.mBinding.liOfferView.visibility = View.VISIBLE
                viewHolder.mBinding.llFlashOfferView.visibility = View.GONE
                viewHolder.mBinding.favItem.visibility = View.GONE
                viewHolder.mBinding.tvFlashdealPrice.visibility = View.GONE
                val spItemName = model.itemname
                val freeOfferTextBuy =
                    "<font color=#fe4e4e>" + MyApplication.getInstance().dbHelper.getString(
                        R.string.buy
                    ) + "&nbsp;" + model.offerMinimumQty + "&nbsp;" + MyApplication.getInstance().dbHelper.getString(
                        R.string.pcs
                    ) + "&nbsp;</font>" + "<font color=#000000>&nbsp;" + spItemName + " </font>"
                var freeOfferTextGet = ""
                viewHolder.mBinding.tvBuyValue.text = Html.fromHtml(freeOfferTextBuy)
                if (model.offerType != null) {
                    when (model.offerType) {
                        "WalletPoint" -> {
                            viewHolder.mBinding.tvFreeItemQut.text = "0"
                            viewHolder.mBinding.ivFreeIteam.setBackgroundResource(R.drawable.ic_gift_bg)
                            viewHolder.mBinding.tvFreeDepePoint.text =
                                MyApplication.getInstance().dbHelper.getString(
                                    R.string.Dp
                                )
                            val sfreewalletDP =
                                DecimalFormat("##.##").format(model.offerWalletPoint)
                            freeOfferTextGet =
                                "<font color=#fe4e4e>" + MyApplication.getInstance().dbHelper.getString(
                                    R.string.get
                                ) + "&nbsp;" + sfreewalletDP + "&nbsp;" + MyApplication.getInstance().dbHelper.getString(
                                    R.string.free
                                ) + "</font>" + "<font color=#000000>&nbsp;" + MyApplication.getInstance().dbHelper.getString(
                                    R.string.dream_points
                                ) + "</font>"
                        }

                        "ItemMaster" -> {
                            if (!TextUtils.isNullOrEmpty(model.offerFreeItemImage)) {
                                Picasso.get().load(model.offerFreeItemImage)
                                    .placeholder(R.drawable.logo_grey)
                                    .error(R.drawable.logo_grey)
                                    .into(viewHolder.mBinding.ivFreeIteam)
                            } else {
                                viewHolder.mBinding.ivFreeIteam.setImageResource(R.drawable.logo_grey)
                            }
                            viewHolder.mBinding.tvFreeItemQut.text = "0"
                            viewHolder.mBinding.tvFreeDepePoint.text =
                                MyApplication.getInstance().dbHelper.getString(
                                    R.string.free
                                )
                            freeOfferTextGet =
                                "<font color=#fe4e4e>" + MyApplication.getInstance().dbHelper.getString(
                                    R.string.get
                                ) + "&nbsp;" + model.offerFreeItemQuantity + "&nbsp;" + MyApplication.getInstance().dbHelper.getString(
                                    R.string.free
                                ) + " </font>" + "<font color=#000000>&nbsp;" + model.offerFreeItemName + " </font>"
                        }

                        "FlashDeal" -> {
                            var isflashDealUsed = false
                            val jsonFlashString = SharePrefs.getStringSharedPreferences(
                                activity, SharePrefs.ITEM_FLASH_DEAL_USED_JSON
                            )
                            try {
                                if (!jsonFlashString.isEmpty()) {
                                    val jsonFlashUsed = JSONObject(jsonFlashString)
                                    if (jsonFlashUsed.has(model.itemId.toString())) {
                                        if (jsonFlashUsed[model.itemId.toString()] == "1") {
                                            isflashDealUsed = true
                                        }
                                    }
                                }
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                            if (!isflashDealUsed) {
                                viewHolder.mBinding.tvLeftItems.text =
                                    model.offerQtyAvaiable.toString()
                                viewHolder.mBinding.liOfferView.visibility = View.GONE
                                viewHolder.mBinding.llFlashOfferView.visibility = View.VISIBLE
                                viewHolder.mBinding.tvFlashdealPrice.visibility = View.VISIBLE
                                flashOfferFlag = true
                                //end time
                                //  timeDeference(moqPojoArrayList, viewHolder);
                                if (model.flashDealSpecialPrice != 0.0) {
                                    viewHolder.mBinding.tvPrice.paintFlags =
                                        viewHolder.mBinding.tvMrp.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                                    viewHolder.mBinding.tvFlashdealPrice.text =
                                        " | " + DecimalFormat("##.##").format(model.flashDealSpecialPrice)
                                }
                                val sdf1 = SimpleDateFormat(Utils.myFormat, Locale.ENGLISH)
                                sdf1.timeZone = TimeZone.getDefault()
                                val currentTime = sdf1.parse(model.currentStartTime)
                                val currentEpoch = currentTime.time
                                val endTime = sdf1.parse(model.offerEndTime)
                                val millse = endTime.time - currentEpoch
                                viewHolder.timerExpire(millse)
                            } else {
                                flashOfferFlag = false
                                viewHolder.mBinding.liOfferView.visibility = View.GONE
                                viewHolder.mBinding.llFlashOfferView.visibility = View.GONE
                                viewHolder.mBinding.favItem.visibility = View.VISIBLE
                                viewHolder.mBinding.tvFlashdealPrice.visibility = View.GONE
                                viewHolder.mBinding.tvPrice.paintFlags = 0
                            }
                        }
                    }
                }
                viewHolder.mBinding.tvGetValue.text = Html.fromHtml(freeOfferTextGet)
            } else {
                viewHolder.mBinding.liOfferView.visibility = View.GONE
                viewHolder.mBinding.llFlashOfferView.visibility = View.GONE
                viewHolder.mBinding.favItem.visibility = View.VISIBLE
                viewHolder.mBinding.tvFlashdealPrice.visibility = View.GONE
                viewHolder.mBinding.tvPrice.paintFlags = 0
                viewHolder.mBinding.llInActiveUser.visibility = View.GONE
            }
            if (!TextUtils.isNullOrEmpty(model.logoUrl)) {
                Picasso.get().load(model.logoUrl)
                    .placeholder(R.drawable.logo_grey)
                    .error(R.drawable.logo_grey)
                    .resize(1000, 1000)
                    .onlyScaleDown()
                    .into(viewHolder.mBinding.productImage)
            } else {
                viewHolder.mBinding.productImage.setImageResource(R.drawable.logo_grey)
            }
            val sPRICE = "| ₹" + DecimalFormat("##.##").format(model.unitPrice)
            val sMargin =
                MyApplication.getInstance().dbHelper.getString(R.string.moq_margin) + " " + DecimalFormat(
                    "##.##"
                ).format((if (model.marginPoint != null) model.marginPoint else "0")!!.toDouble()) + "%"
            val sMRP = DecimalFormat("##.##").format(model.price)
            //set values
            viewHolder.mBinding.tvMrp.text = sMRP
            viewHolder.mBinding.tvMrp.paintFlags =
                viewHolder.mBinding.tvMrp.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            viewHolder.mBinding.tvPrice.text = sPRICE
            viewHolder.mBinding.tvMargin.text = sMargin
            // set prime item price
            if (model.isPrimeItem) {
                viewHolder.mBinding.liPrime.visibility = View.VISIBLE
                viewHolder.mBinding.tvPPrice.text =
                    (SharePrefs.getInstance(activity).getString(SharePrefs.PRIME_NAME)
                            + " " + MyApplication.getInstance().dbHelper.getString(R.string.price)
                            + ": ₹" + DecimalFormat("##.##").format(model.primePrice))
                viewHolder.mBinding.tvPrice.setTextColor(activity.resources.getColor(R.color.back_arrow_grey))
            } else {
                viewHolder.mBinding.liPrime.visibility = View.GONE
                viewHolder.mBinding.tvPrice.setTextColor(activity.resources.getColor(R.color.colorAccent))
            }
            if (SharePrefs.getInstance(activity)
                    .getBoolean(SharePrefs.IS_PRIME_MEMBER) && model.isPrimeItem
            ) {
                viewHolder.mBinding.tvUnlock.text = ""
                viewHolder.mBinding.tvUnlock.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.ic_lock_open,
                    0,
                    0,
                    0
                )
            } else {
                viewHolder.mBinding.tvUnlock.text =
                    " " + MyApplication.getInstance().dbHelper.getString(
                        R.string.text_unlock
                    )
                viewHolder.mBinding.tvUnlock.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.ic_lock,
                    0,
                    R.drawable.ic_right_arrow,
                    0
                )
            }
            val cartModel = MyApplication.getInstance().noteRepository.getItemByMrpId(
                model.itemId,
                model.itemMultiMRPId
            )
            val qtyDTO = MyApplication.getInstance().noteRepository.getQtyTotalQtyByMrpId(
                model.itemId,
                model.itemMultiMRPId
            )

            // set UI for ItemLimit
            if (model.isItemLimit) {
                var totalItemInCart = 0
                var itemlimitQuantity = 0
                if (model.active) {
                    viewHolder.mBinding.availQtyLayout.visibility = View.VISIBLE
                } else {
                    viewHolder.mBinding.availQtyLayout.visibility = View.GONE
                }
                itemlimitQuantity = model.itemLimitQty
                var totalAvailQty = 0
                if (qtyDTO != null) {
                    totalItemInCart += qtyDTO.totalQuantity
                    totalAvailQty = itemlimitQuantity - totalItemInCart
                } else {
                    totalAvailQty = model.itemLimitQty
                }
                if (totalAvailQty > 0) {
                    viewHolder.mBinding.availQty.text = "" + totalAvailQty
                } else {
                    viewHolder.mBinding.availQty.text = "0"
                }
            } else {
                viewHolder.mBinding.availQtyLayout.visibility = View.GONE
            }
            viewHolder.mBinding.tvDreamPoint.text =
                MyApplication.getInstance().dbHelper.getString(R.string.Dp) + " " + model.dreamPoint
            var isItemFound: Boolean
            if (cartModel != null && model.itemId == cartModel.itemId) {
                isItemFound = true
                val itemQuantity = cartModel.qty
                /*offer section start*/if (cartModel.isOffer) {
                    if (cartModel.offerType.equals("WalletPoint", ignoreCase = true)) {
                        val freeWalletPoint = cartModel.totalFreeWalletPoint
                        val sfreewalletDP = DecimalFormat("##.##").format(freeWalletPoint)
                        if (freeWalletPoint > 0) {
                            viewHolder.mBinding.tvFreeItemQut.text = sfreewalletDP
                        }
                    } else if (cartModel.offerType.equals("ItemMaster", ignoreCase = true)) {
                        val freeItemQuantity = cartModel.totalFreeItemQty
                        if (freeItemQuantity > 0) {
                            viewHolder.mBinding.tvFreeItemQut.text = "" + freeItemQuantity
                        }
                    } else {
                        val remainingLeft = cartModel.offerQtyAvaiable - itemQuantity
                        if (remainingLeft > 0) {
                            viewHolder.mBinding.tvLeftItems.text = "" + remainingLeft
                        } else {
                            viewHolder.mBinding.tvLeftItems.text = "0"
                        }
                    }
                }
                // offer section end
                if (itemQuantity > 0) {
                    viewHolder.mBinding.tvSelectedItemQuantity.text = "" + itemQuantity
                    if (flashOfferFlag) {
                        val price = "<font color=#FF4500>&#8377; " + DecimalFormat("##.##").format(
                            itemQuantity * model.flashDealSpecialPrice
                        )
                        viewHolder.mBinding.tvSelectedItemPrice.text = Html.fromHtml(price)
                    } else if (SharePrefs.getInstance(activity)
                            .getBoolean(SharePrefs.IS_PRIME_MEMBER) && model.isPrimeItem
                    ) {
                        val price = "<font color=#FF4500>&#8377; " + DecimalFormat("##.##").format(
                            itemQuantity * model.primePrice
                        )
                        viewHolder.mBinding.tvSelectedItemPrice.text = Html.fromHtml(price)
                    } else {
                        val price = "<font color=#FF4500>&#8377; " + DecimalFormat("##.##").format(
                            itemQuantity * model.unitPrice
                        )
                        viewHolder.mBinding.tvSelectedItemPrice.text = Html.fromHtml(price)
                    }
                } else {
                    isItemFound = false
                }
            } else {
                isItemFound = false
            }

            // item notify code
            if (model.active) {
                viewHolder.mBinding.tvMrpText.text = MyApplication.getInstance().dbHelper.getString(
                    R.string.item_mrp
                ) + " "
                viewHolder.mBinding.tvMrpText.setTextColor(activity.resources.getColor(android.R.color.darker_gray))
                viewHolder.mBinding.btItemNotyfy.visibility = View.GONE
                viewHolder.mBinding.addItemBtn.visibility = View.VISIBLE
                viewHolder.mBinding.tvMrp.visibility = View.VISIBLE
                viewHolder.mBinding.tvPrice.visibility = View.VISIBLE
                viewHolder.mBinding.tvSelectedItemPrice.visibility = View.VISIBLE
                viewHolder.mBinding.tvMargin.visibility = View.VISIBLE
                viewHolder.mBinding.favItem.visibility = View.VISIBLE
                if (!isItemFound) {
                    val itemQuantity = 0
                    viewHolder.mBinding.tvSelectedItemQuantity.text = "" + itemQuantity
                    val price =
                        "<font color=#FF4500>&#8377; " + DecimalFormat("##.##").format(itemQuantity * model.unitPrice)
                    viewHolder.mBinding.tvSelectedItemPrice.text = Html.fromHtml(price)
                    viewHolder.mBinding.addItemBtn.visibility = View.VISIBLE
                    viewHolder.mBinding.visible.visibility = View.GONE
                } else {
                    viewHolder.mBinding.addItemBtn.visibility = View.GONE
                    viewHolder.mBinding.visible.visibility = View.VISIBLE
                }
            } else {
                viewHolder.mBinding.tvMrpText.text = MyApplication.getInstance().dbHelper.getString(
                    R.string.text_out_of_stock
                )
                viewHolder.mBinding.tvMrpText.setTextColor(activity.resources.getColor(R.color.colorAccent))
                viewHolder.mBinding.btItemNotyfy.visibility = View.VISIBLE
                viewHolder.mBinding.addItemBtn.visibility = View.GONE
                viewHolder.mBinding.tvMrp.visibility = View.GONE
                viewHolder.mBinding.tvPrice.visibility = View.GONE
                viewHolder.mBinding.tvSelectedItemPrice.visibility = View.GONE
                viewHolder.mBinding.tvMargin.visibility = View.GONE
                viewHolder.mBinding.favItem.visibility = View.GONE
                if (MyApplication.getInstance().noteRepository.isNotifyDisable(model.itemId)) {
                    viewHolder.mBinding.btItemNotyfy.background =
                        activity.resources.getDrawable(R.drawable.background_for_buttons_disble)
                    viewHolder.mBinding.btItemNotyfy.isClickable = false
                    viewHolder.mBinding.btItemNotyfy.isEnabled = false
                } else {
                    viewHolder.mBinding.btItemNotyfy.background =
                        activity.resources.getDrawable(R.drawable.background_for_buttons)
                    viewHolder.mBinding.btItemNotyfy.isClickable = true
                    viewHolder.mBinding.btItemNotyfy.isEnabled = true
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun itemAddRemove(
        viewHolder: ViewHolder,
        model: ItemListModel,
        addItem: Boolean,
        addBtn: Boolean
    ) {
        // progress bar
        if (progressBar != null) progressBar!!.visibility = View.INVISIBLE
        progressBar = viewHolder.mBinding.ivProgress
        try {
            if (Utils(activity).isNetworkAvailable) {
                var FreeItemQuantity = 0
                var FreeWalletPoint = 0.0
                var addFlag = false
                var flashDealFlag = false
                flashOfferFlag = false
                var calUnitPrice = 0.0
                var isflashDealUsed = false
                var isPrimeItem = false

                // get item limit
                var itemQuantity =
                    viewHolder.mBinding.tvSelectedItemQuantity.text.toString().toInt()

                //get cart data
                if (model.isOffer) {
                    try {
                        if (model.offerType != null) {
                            if (model.offerType.equals("FlashDeal", ignoreCase = true)) {
                                val jsonFlashString = SharePrefs.getStringSharedPreferences(
                                    activity, SharePrefs.ITEM_FLASH_DEAL_USED_JSON
                                )
                                if (!jsonFlashString.isEmpty()) {
                                    val jsonObject = JSONObject(jsonFlashString)
                                    if (jsonObject.has(model.itemId.toString())) {
                                        if (jsonObject[model.itemId.toString()] == "1") {
                                            isflashDealUsed = true
                                        }
                                    }
                                }
                                if (model.flashDealMaxQtyPersonCanTake >= itemQuantity + model.minOrderQty && model.offerQtyAvaiable >= itemQuantity + model.minOrderQty) {
                                    flashDealFlag = false
                                } else {
                                    if (!isflashDealUsed) {
                                        flashDealFlag = true
                                    }
                                }
                            } else {
                                flashDealFlag = false
                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
                if (addItem) {
                    // plus btn
                    if (!flashDealFlag) {
                        itemQuantity += model.minOrderQty
                        //Item Limit code
                        if (model.isItemLimit) {
                            addFlag = setItemLimit(viewHolder, model, addFlag, itemQuantity, true)
                        } else {
                            if (itemQuantity > 0) {
                                if (model.billLimitQty != 0) {
                                    //bill limit
                                    addFlag = setBillLimit(viewHolder, model, addFlag, itemQuantity)
                                } else {
                                    viewHolder.mBinding.tvSelectedItemQuantity.text =
                                        "" + itemQuantity
                                    addFlag = true
                                }
                            }
                        }
                    } else {
                        if (addBtn) {
                            viewHolder.mBinding.visible.visibility = View.VISIBLE
                            viewHolder.mBinding.addItemBtn.visibility = View.GONE
                            Toast.makeText(
                                activity, MyApplication.getInstance().dbHelper.getString(
                                    R.string.no_item_available
                                ), Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            if (model.offerQtyAvaiable <= itemQuantity + model.minOrderQty) {
                                Toast.makeText(
                                    activity, MyApplication.getInstance().dbHelper.getString(
                                        R.string.no_item_available
                                    ), Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                Toast.makeText(
                                    activity, MyApplication.getInstance().dbHelper.getString(
                                        R.string.only_add_maximum_item
                                    ) + " " + model.flashDealMaxQtyPersonCanTake, Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                } else {
                    // minus btn
                    if (itemQuantity > 0) {
                        itemQuantity -= model.minOrderQty
                        if (model.isItemLimit) {
                            addFlag = setItemLimit(viewHolder, model, addFlag, itemQuantity, false)
                        } else {
                            viewHolder.mBinding.tvSelectedItemQuantity.text = "" + itemQuantity
                            addFlag = true
                        }
                    } else {
                        viewHolder.mBinding.addItemBtn.visibility = View.VISIBLE
                        // analytics for remove from cart
                        MyApplication.getInstance()
                            .updateAnalyticsCart(FirebaseAnalytics.Event.REMOVE_FROM_CART, model)
                    }
                }
                if (addFlag) {
                    progressBar!!.visibility = View.VISIBLE
                    // offer section
                    if (model.isOffer) {
                        val customerActive =
                            SharePrefs.getInstance(activity).getBoolean(SharePrefs.CUST_ACTIVE)
                        val offerMinimumQty = model.offerMinimumQty
                        when (model.offerType) {
                            "WalletPoint" -> if (customerActive) {
                                if (offerMinimumQty != 0) {
                                    if (model.offerWalletPoint != null) {
                                        //event trigger
                                        if (itemQuantity >= offerMinimumQty) {
                                            FreeWalletPoint = model.offerWalletPoint!!
                                            val calfreeItemQty = itemQuantity / offerMinimumQty
                                            FreeWalletPoint *= calfreeItemQty.toDouble()
                                            val sfreewalletDP =
                                                DecimalFormat("##.##").format(FreeWalletPoint)
                                            if (FreeWalletPoint > 0) {
                                                viewHolder.mBinding.tvFreeItemQut.text =
                                                    sfreewalletDP
                                            }
                                        } else {
                                            viewHolder.mBinding.tvFreeItemQut.text = "0"
                                        }
                                    } else {
                                        progressBar!!.visibility = View.INVISIBLE
                                        Toast.makeText(
                                            activity,
                                            MyApplication.getInstance().dbHelper.getString(
                                                R.string.mini_qty_should_not_be_zero
                                            ),
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            } /*else {
                                    Toast.makeText(activity, R.string.inactive_customer_msg, Toast.LENGTH_SHORT).show();
                                }*/
                            "ItemMaster" -> if (customerActive) {
                                if (offerMinimumQty != 0) {
                                    if (model.offerFreeItemQuantity != null) {
                                        //event trigger
                                        if (itemQuantity >= offerMinimumQty) {
                                            FreeItemQuantity = model.offerFreeItemQuantity!!.toInt()
                                            val calfreeItemQty = itemQuantity / offerMinimumQty
                                            FreeItemQuantity *= calfreeItemQty
                                            if (FreeItemQuantity > 0) {
                                                viewHolder.mBinding.tvFreeItemQut.text =
                                                    FreeItemQuantity.toString()
                                            }
                                        } else {
                                            viewHolder.mBinding.tvFreeItemQut.text = "0"
                                        }
                                    }
                                } else {
                                    progressBar!!.visibility = View.INVISIBLE
                                    Toast.makeText(
                                        activity, MyApplication.getInstance().dbHelper.getString(
                                            R.string.mini_qty_should_not_be_zero
                                        ), Toast.LENGTH_SHORT
                                    ).show()
                                }
                            } /* else {
                                    Toast.makeText(activity, R.string.inactive_customer_msg, Toast.LENGTH_SHORT).show();
                                }*/
                            "FlashDeal" -> if (!isflashDealUsed) {
                                val remainingLeft = model.offerQtyAvaiable - itemQuantity
                                if (remainingLeft > 0) {
                                    viewHolder.mBinding.tvLeftItems.text = "" + remainingLeft
                                } else {
                                    viewHolder.mBinding.tvLeftItems.text = "0"
                                }
                                flashOfferFlag = true
                            } else {
                                flashOfferFlag = false
                            }
                        }
                    }
                    if (flashOfferFlag) {
                        val price = "<font color=#FF4500>&#8377; " + DecimalFormat("##.##").format(
                            itemQuantity * model.flashDealSpecialPrice
                        )
                        viewHolder.mBinding.tvSelectedItemPrice.text = Html.fromHtml(price)
                        calUnitPrice = model.flashDealSpecialPrice
                    } else if (SharePrefs.getInstance(activity)
                            .getBoolean(SharePrefs.IS_PRIME_MEMBER) && model.isPrimeItem
                    ) {
                        val price = "<font color=#FF4500>&#8377; " + DecimalFormat("##.##").format(
                            itemQuantity * model.primePrice
                        )
                        viewHolder.mBinding.tvSelectedItemPrice.text = Html.fromHtml(price)
                        calUnitPrice = model.primePrice
                        isPrimeItem = true
                    } else {
                        val price = "<font color=#FF4500>&#8377; " + DecimalFormat("##.##").format(
                            itemQuantity * model.unitPrice
                        )
                        viewHolder.mBinding.tvSelectedItemPrice.text = Html.fromHtml(price)
                        calUnitPrice = model.unitPrice
                    }
                    activity.addItemInCartItemArrayList(
                        model.itemId,
                        itemQuantity, calUnitPrice, model,
                        FreeItemQuantity, FreeWalletPoint, isPrimeItem, this
                    )
                }
            } else {
                Utils.setToast(
                    activity,
                    MyApplication.getInstance().dbHelper.getString(R.string.internet_connection)
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setItemLimit(
        viewHolder: ViewHolder,
        model: ItemListModel,
        addFlag: Boolean,
        itemQuantity: Int,
        add: Boolean
    ): Boolean {
        var addFlag = addFlag
        try {
            val qtyDTO = MyApplication.getInstance().noteRepository.getQtyTotalQtyByMrpId(
                model.itemId,
                model.itemMultiMRPId
            )
            val total = qtyDTO.quantity
            var availqty = 0
            var totalItemqty = qtyDTO.totalQuantity
            var itemlimitqty = 0
            if (add) {
                totalItemqty += model.minOrderQty
            } else {
                totalItemqty -= model.minOrderQty
            }
            itemlimitqty = model.itemLimitQty
            //            if (total > 0) {
            if (add) {
                if (itemQuantity + total > itemlimitqty) {
                    Utils.setToast(
                        activity,
                        MyApplication.getInstance().dbHelper.getString(R.string.additemToast)
                                + " " + model.itemLimitQty + " " +
                                MyApplication.getInstance().dbHelper.getString(R.string.additemToast_2)
                    )
                } else {
                    if (model.billLimitQty != 0) {
                        addFlag = setBillLimit(viewHolder, model, addFlag, itemQuantity)
                    } else {
                        viewHolder.mBinding.tvSelectedItemQuantity.text = "" + itemQuantity
                        addFlag = true
                    }
                    if (addFlag) {
                        availqty = itemlimitqty - totalItemqty
                        if (availqty >= 0) {
                            viewHolder.mBinding.availQty.text = availqty.toString()
                        }
                    }
                }
            } else {
                availqty = itemlimitqty - totalItemqty
                if (availqty >= 0) {
                    viewHolder.mBinding.availQty.text = availqty.toString()
                }
                viewHolder.mBinding.tvSelectedItemQuantity.text = "" + itemQuantity
                addFlag = true
            }
            //            } else {
//                if (itemQuantity > 0) {
//                    if (itemQuantity > model.getItemlimitQty()) {
//                        Utils.setToast(activity, MyApplication.getInstance().dbHelper.getString(R.string.additemToast)
//                                + " " + model.getItemlimitQty() + " " + MyApplication.getInstance().dbHelper.getString(R.string.additemToast_2));
//                    } else {
//                        if (model.getBillLimitQty() != 0) {
//                            addFlag = setBillLimit(viewHolder, model, addFlag, itemQuantity);
//                        } else {
//                            viewHolder.mBinding.tvSelectedItemQuantity.setText("" + itemQuantity);
//                            addFlag = true;
//                        }
//                        if (addFlag) {
//                            availqty = itemlimitqty - itemQuantity;
//                            if (availqty >= 0) {
//                                viewHolder.mBinding.availQty.setText(String.valueOf(availqty));
//                            }
//                        }
//                    }
//                }
//            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return addFlag
    }

    private fun setBillLimit(
        viewHolder: ViewHolder,
        model: ItemListModel,
        addFlag: Boolean,
        itemQuantity: Int
    ): Boolean {
        var addFlag = addFlag
        try {
            val total = MyApplication.getInstance().noteRepository.getQtyByMultiMrp(
                model.itemId,
                model.itemMultiMRPId
            )
            var itemlimitqty = 0
            itemlimitqty = model.billLimitQty
            if (total > 0) {
                if (total + itemQuantity > itemlimitqty) {
                    Utils.setToast(
                        activity,
                        MyApplication.getInstance().dbHelper.getString(R.string.bill_limit_text) + " " + model.billLimitQty + " item"
                    )
                } else {
                    viewHolder.mBinding.tvSelectedItemQuantity.text = "" + itemQuantity
                    addFlag = true
                }
            } else {
                if (itemQuantity > 0) {
                    if (itemQuantity > model.billLimitQty) {
                        Utils.setToast(
                            activity,
                            MyApplication.getInstance().dbHelper.getString(R.string.bill_limit_text) + " " + model.billLimitQty + " item"
                        )
                    } else {
                        viewHolder.mBinding.tvSelectedItemQuantity.text = "" + itemQuantity
                        addFlag = true
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return addFlag
    }

    override fun onItemClick(position: Int, itemAdded: Boolean) {
        progressBar!!.visibility = View.INVISIBLE
        if (!itemAdded) notifyDataSetChanged()
    }

    // mow click
    override fun onSelect(model: ItemListModel?, pos: Int) {
//        ArrayList<ItemListModel> moq = model.getMoqList();
//        list.set(i, list.get(i).getMoqList().get(pos));
//        list.get(i).setMoqList(moq);
//        for (int j = 0; j < list.get(i).getMoqList().size(); j++) {
//            list.get(i).getMoqList().get(j).setChecked(pos == j);
//        }
//        list.get(i).getMoqList().get(pos).setChecked(true);
//        notifyDataSetChanged();
//
//        handler.postDelayed(() -> {
//            //Do something after 100ms
//            customAlertDialog.dismiss();
//        }, 200);
    }

    inner class ViewHolder(var mBinding: CategoryOrderItemsBinding) : RecyclerView.ViewHolder(
        mBinding.root
    ) {
        init {
            customRunnable = CustomRunnable(handler, mBinding.tvRemaingSecand, 10000)
        }

        fun bind(obj: CategoryItemOrderInfo?) {
            mBinding.executePendingBindings()
        }

        fun timerExpire(endTime: Long) {
            handler.removeCallbacks(customRunnable!!)
            customRunnable!!.holder = mBinding.tvRemaingSecand
            customRunnable!!.millisUntilFinished = endTime //Current time - received time
            handler.postDelayed(customRunnable!!, 1000)
        }
    }
}