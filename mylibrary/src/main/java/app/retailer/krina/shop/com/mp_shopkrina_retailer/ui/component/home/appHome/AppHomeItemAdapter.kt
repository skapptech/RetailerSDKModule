package app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.appHome

import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.os.Handler
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.ItemListModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.interfaces.AdapterInterface
import app.retailer.krina.shop.com.mp_shopkrina_retailer.interfaces.OnItemClick
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.activity.MembershipPlanActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.productDetails.ProductDetailsActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.SharePrefs
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.HomeActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.MoqAdapter
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.CustomRunnable
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.MyApplication
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.TextUtils
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.analytics.FirebaseAnalytics
import com.squareup.picasso.Picasso
import org.json.JSONException
import org.json.JSONObject
import pl.droidsonroids.gif.GifImageView
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class AppHomeItemAdapter : RecyclerView.Adapter<AppHomeItemAdapter.ViewHolder>, OnItemClick {
    private val handler = Handler()
    private val activity: HomeActivity
    private var list: ArrayList<ItemListModel>? = null
    private var adapter: MoqAdapter? = null
    private var customRunnable: CustomRunnable? = null
    private var listSize = 0
    private var totalAvailQty = 0
    private var flashofferFlag = false
    private var isSlider = false
    private var progressBar: GifImageView? = null

    internal constructor(activity: HomeActivity) {
        this.activity = activity
    }

    fun setItemsInAdapter(tileSlider: Boolean) {
        isSlider = tileSlider
        notifyDataSetChanged()
    }

    constructor(activity: HomeActivity, list: ArrayList<ItemListModel>?, listSize: Int) {
        this.activity = activity
        this.list = list
        this.listSize = listSize
    }

    fun setItemListCategory(list: ArrayList<ItemListModel>?, listSize: Int) {
        this.list = list
        this.listSize = listSize
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(viewGroup.context)
                .inflate(
                    if (isSlider) R.layout.category_item_hz else R.layout.category_order_items,
                    null
                )
        )
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        val model = list!![i]
        //set String
        viewHolder.tvMrpText.text =
            MyApplication.getInstance().dbHelper.getString(R.string.item_mrp) + " "
        viewHolder.tvRemainingQtyText.text =
            MyApplication.getInstance().dbHelper.getString(R.string.remaining_qty) + " "
        viewHolder.tvItemLeftText.text =
            MyApplication.getInstance().dbHelper.getString(R.string.item_left)
        viewHolder.tvEndInText.text =
            MyApplication.getInstance().dbHelper.getString(R.string.end_in_text)
        viewHolder.tvFreeItemNotActiveText.text =
            MyApplication.getInstance().dbHelper.getString(R.string.inactive_customer_msg)
        viewHolder.btnAdd.text = MyApplication.getInstance().dbHelper.getString(R.string.add_btn)

        // set MOQ
        if (model.moqList.size > 0) {
            viewHolder.tvMoq.visibility = View.GONE
            viewHolder.tvMultiMoq.visibility = View.VISIBLE
        } else {
            viewHolder.tvMoq.visibility = View.VISIBLE
            viewHolder.tvMultiMoq.visibility = View.GONE
        }
        //ii;
        viewHolder.tvMoq.text =
            MyApplication.getInstance().dbHelper.getString(R.string.item_moq) + " " + model.minOrderQty
        viewHolder.tvMultiMoq.text =
            MyApplication.getInstance().dbHelper.getString(R.string.item_moq) + " " + model.minOrderQty
        if (!TextUtils.isNullOrEmpty(model.scheme)) {
            viewHolder.tvSchemeText.visibility = View.VISIBLE
            viewHolder.tvSchemeText.text = "" + model.scheme
        } else {
            viewHolder.tvSchemeText.visibility = View.GONE
        }
        viewHolder.leftItemsTV.setBackgroundResource(R.drawable.ic_count_bg)
        viewHolder.remaingSecTV.setBackgroundResource(R.drawable.ic_count_bg)
        // set  value on UI
        setValueInUI(viewHolder, model)
        model.isChecked = true
        // Minis Btn clicked
        viewHolder.ivMinusBtn.setOnClickListener { view: View? ->
            // click effect
            Utils.buttonEffect(viewHolder.ivMinusBtn)
            itemAddRemove(viewHolder, model, false)
        }
        // plus Btn clicked
        viewHolder.ivPlusBtn.setOnClickListener { view: View? ->
            Utils.buttonEffect(viewHolder.ivPlusBtn)
            itemAddRemove(viewHolder, model, true)
        }
        // fav section
        viewHolder.favItem.setOnClickListener { v: View? -> addRemoveFav(model, viewHolder) }

        // Add Btn clicked
        viewHolder.btnAdd.setOnClickListener { v: View? ->
            viewHolder.visible.visibility = View.VISIBLE
            viewHolder.btnAdd.visibility = View.GONE
            itemAddRemove(viewHolder, model, true)
            // analytics for add to cart
            MyApplication.getInstance()
                .updateAnalyticsCart(FirebaseAnalytics.Event.ADD_TO_CART, model)
        }
        // checkout clicked
        viewHolder.LLItemMain.setOnClickListener { v: View? -> detailsScree(model, viewHolder) }

        // MOQ popup open here
        viewHolder.tvMultiMoq.setOnClickListener { v: View? ->
            val dialog = BottomSheetDialog(activity)
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
            item_name.text = model.itemname
            val mMoqPriceList = dialogLayout.findViewById<ListView>(R.id.listview_moq_price)
            val listener = AdapterInterface { pos ->
                val moq = list!![i].moqList
                list!![i] = list!![i].moqList[pos]
                list!![i].moqList = moq
                for (j in list!![i].moqList.indices) {
                    list!![i].moqList[j].isChecked = pos == j
                }
                list!![i].moqList[pos].isChecked = true
                notifyDataSetChanged()
                handler.postDelayed(object : Runnable {
                    override fun run() {
                        //Do something after 100ms
                        dialog.dismiss()
                        handler.postDelayed(this, 500)
                        handler.removeCallbacks(this)
                    }
                }, 500)
            }
            adapter =
                MoqAdapter(
                    activity,
                    model.moqList,
                    listener
                )
            mMoqPriceList.adapter = adapter
            dialog.show()
        }
        viewHolder.tvUnlock.setOnClickListener { v: View? ->
            if (!SharePrefs.getInstance(activity)
                    .getBoolean(SharePrefs.IS_PRIME_MEMBER) && model.isPrimeItem
            ) {
                activity.startActivity(Intent(activity, MembershipPlanActivity::class.java))
            }
        }
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
        intent.putExtra("remainingqty", viewHolder.availQty.text)
        args.putSerializable("ITEM_LIST", model)
        intent.putExtras(args)
        activity.startActivity(intent)
        Utils.leftTransaction(activity)
    }

    private fun addRemoveFav(model: ItemListModel, viewHolder: ViewHolder) {
        if (MyApplication.getInstance().noteRepository.isItemWishList(model.itemId)) {
            viewHolder.favItem.setImageResource(R.drawable.ic_favourite)
            MyApplication.getInstance().noteRepository.deleteTask(model)
            Utils.addFav(model.itemId.toString().toInt(), false, activity)
        } else {
            viewHolder.favItem.setImageResource(R.drawable.ic_favorite_red)
            MyApplication.getInstance().noteRepository.insertTask(model)
            Utils.addFav(model.itemId.toString().toInt(), true, activity)
            MyApplication.getInstance().analyticAddWishList(model)
        }
    }

    private fun setValueInUI(viewHolder: ViewHolder, model: ItemListModel) {
        if (isSlider) {
            /*Fav section*/
            flashofferFlag = false
            val calUnitPrice = 0.0
            viewHolder.availQtyLayout.visibility = View.GONE
            viewHolder.pricelayout!!.visibility = View.VISIBLE
            viewHolder.moqLayout!!.visibility = View.GONE
            viewHolder.mrpLayout!!.visibility = View.VISIBLE
            viewHolder.offerLayout!!.visibility = View.GONE
            viewHolder.dreampointlayout.visibility = View.GONE
            viewHolder.price!!.text = "₹ " + model.unitPrice
        }
        viewHolder.tvItemName.text = model.itemname

        /*Fav section*/flashofferFlag = false
        try {
            // check item in wishList
            if (MyApplication.getInstance().noteRepository.isItemWishList(model.itemId)) {
                viewHolder.favItem.setImageResource(R.drawable.ic_favorite_red)
            } else {
                viewHolder.favItem.setImageResource(R.drawable.ic_favourite)
            }

            // set offer UI
            if (model.isOffer) {
                /*for inactive customer*/
                if (!SharePrefs.getInstance(activity).getBoolean(SharePrefs.CUST_ACTIVE)) {
                    viewHolder.llInActiveCustomer.visibility = View.VISIBLE
                }
                viewHolder.tvOffer.visibility = View.GONE
                viewHolder.mainOfferViewLL.visibility = View.VISIBLE
                viewHolder.flashOfferViewLL.visibility = View.GONE
                viewHolder.favItem.visibility = View.GONE
                viewHolder.tvFlashdealPrice.visibility = View.GONE
                val spItemName = model.itemname
                val freeOfferTextBuy =
                    "<font color=#fe4e4e>Buy&nbsp;" + model.offerMinimumQty + "&nbsp;pcs&nbsp;</font>" + "<font color=#000000>&nbsp;" + spItemName + " </font>"
                var freeOfferTextGet = ""
                viewHolder.buyValueText.text = Html.fromHtml(freeOfferTextBuy)
                when (model.offerType) {
                    "WalletPoint" -> {
                        viewHolder.freeItemQutTV.text = "0"
                        viewHolder.freeItemIV.setBackgroundResource(R.drawable.ic_gift_bg)
                        viewHolder.freeDepePointTV.text =
                            MyApplication.getInstance().dbHelper.getString(R.string.Dp)
                        val sfreewalletDP = DecimalFormat("##.##").format(model.offerWalletPoint)
                        freeOfferTextGet =
                            "<font color=#fe4e4e>Get&nbsp;$sfreewalletDP&nbsp;Free</font><font color=#000000>&nbsp;Dream Points</font>"
                    }

                    "ItemMaster" -> {
                        if (!TextUtils.isNullOrEmpty(model.offerFreeItemImage)) {
                            Picasso.get().load(model.logoUrl).placeholder(R.drawable.logo_grey)
                                .error(R.drawable.logo_grey).resize(1000, 1000).onlyScaleDown()
                                .into(viewHolder.ivItemImage)
                        } else {
                            viewHolder.freeItemIV.setImageResource(R.drawable.logo_grey)
                        }
                        viewHolder.freeItemQutTV.text = "0"
                        viewHolder.freeDepePointTV.text =
                            MyApplication.getInstance().dbHelper.getString(R.string.free)
                        freeOfferTextGet =
                            "<font color=#fe4e4e>Get " + model.offerFreeItemQuantity + "&nbsp;Free </font>" + "<font color=#000000>&nbsp;" + model.offerFreeItemName + " </font>"
                    }

                    "FlashDeal" -> {
                        var isflashDealUsed = false
                        val jsonFlashString = SharePrefs.getStringSharedPreferences(
                            activity,
                            SharePrefs.ITEM_FLASH_DEAL_USED_JSON
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
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                        if (!isflashDealUsed) {
                            viewHolder.leftItemsTV.text = model.offerQtyAvaiable.toString()
                            viewHolder.mainOfferViewLL.visibility = View.GONE
                            viewHolder.flashOfferViewLL.visibility = View.VISIBLE
                            viewHolder.tvFlashdealPrice.visibility = View.VISIBLE
                            flashofferFlag = true
                            //end time
                            if (model.flashDealSpecialPrice != 0.0) {
                                viewHolder.tvPrice.paintFlags =
                                    viewHolder.tvMrp.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                                viewHolder.tvFlashdealPrice.text =
                                    " | " + DecimalFormat("##.##").format(model.flashDealSpecialPrice)
                            }
                            //  long currMillis = System.currentTimeMillis();
                            val sdf1 = SimpleDateFormat(Utils.myFormat, Locale.getDefault())
                            sdf1.timeZone = TimeZone.getDefault()
                            val currentTime = sdf1.parse(model.currentStartTime)
                            val currentEpoch = currentTime.time
                            val endTime = sdf1.parse(model.offerEndTime)
                            val millse = endTime.time - currentEpoch
                            viewHolder.timerExpire(millse)
                        } else {
                            flashofferFlag = false
                            viewHolder.mainOfferViewLL.visibility = View.GONE
                            viewHolder.flashOfferViewLL.visibility = View.GONE
                            viewHolder.favItem.visibility = View.VISIBLE
                            viewHolder.tvFlashdealPrice.visibility = View.GONE
                            viewHolder.tvPrice.paintFlags = 0
                        }
                    }
                }
                viewHolder.GetValueTextView.text = Html.fromHtml(freeOfferTextGet)
            } else {
                viewHolder.mainOfferViewLL.visibility = View.GONE
                viewHolder.flashOfferViewLL.visibility = View.GONE
                viewHolder.favItem.visibility = View.VISIBLE
                viewHolder.tvFlashdealPrice.visibility = View.GONE
                viewHolder.tvPrice.paintFlags = 0
            }
            if (!TextUtils.isNullOrEmpty(model.logoUrl)) {
                Picasso.get().load(model.logoUrl).placeholder(R.drawable.logo_grey)
                    .error(R.drawable.logo_grey).resize(1000, 1000).onlyScaleDown()
                    .into(viewHolder.ivItemImage)
            } else {
                viewHolder.ivItemImage.setImageResource(R.drawable.logo_grey)
            }
            val sPRICE =
                "| <font color=#FF4500>&#8377; " + DecimalFormat("##.##").format(model.unitPrice) + "</font>"
            val sMargin =
                MyApplication.getInstance().dbHelper.getString(R.string.moq_margin) + " " + DecimalFormat(
                    "##.##"
                ).format(model.marginPoint!!.toDouble()) + "%"
            val sMRP = DecimalFormat("##.##").format(model.price)
            val cartModel = MyApplication.getInstance().noteRepository.getItemByMrpId(
                model.itemId,
                model.itemMultiMRPId
            )
            val qtyDTO = MyApplication.getInstance().noteRepository.getQtyTotalQtyByMrpId(
                model.itemId,
                model.itemMultiMRPId
            )
            if (model.isItemLimit) {
                var totalItemInCart = 0
                var itemlimitQuantity = 0
                if (isSlider) {
                    viewHolder.availQtyLayout.visibility = View.GONE
                } else {
                    viewHolder.availQtyLayout.visibility = View.VISIBLE
                }
                itemlimitQuantity = model.itemLimitQty
                if (qtyDTO != null) {
                    totalItemInCart += qtyDTO.totalQuantity
                    totalAvailQty = itemlimitQuantity - totalItemInCart
                } else {
                    totalAvailQty = model.itemLimitQty
                }
                if (totalAvailQty > 0) {
                    viewHolder.availQty.text = "" + totalAvailQty
                } else {
                    viewHolder.availQty.text = "0"
                }
            } else {
                viewHolder.availQtyLayout.visibility = View.GONE
            }

            //set values
            viewHolder.tvMrp.text = sMRP
            viewHolder.tvMrp.paintFlags = viewHolder.tvMrp.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            viewHolder.tvPrice.text = Html.fromHtml(sPRICE)
            viewHolder.tvMargin.text = sMargin
            // set prime item price
            if (model.isPrimeItem) {
                viewHolder.liPrime.visibility = View.VISIBLE
                viewHolder.tvPPrice.text = SharePrefs.getInstance(activity)
                    .getString(SharePrefs.PRIME_NAME) + " " + MyApplication.getInstance().dbHelper.getString(
                    R.string.price
                ) + ": ₹" + DecimalFormat("##.##").format(model.primePrice)
                viewHolder.tvPrice.setTextColor(activity.resources.getColor(R.color.grey))
            } else {
                viewHolder.liPrime.visibility = View.GONE
                viewHolder.tvPrice.setTextColor(Color.parseColor("#FF4500"))
            }
            if (SharePrefs.getInstance(activity)
                    .getBoolean(SharePrefs.IS_PRIME_MEMBER) && model.isPrimeItem
            ) {
                viewHolder.tvUnlock.text = ""
                viewHolder.tvUnlock.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.ic_lock_open,
                    0,
                    0,
                    0
                )
            } else {
                viewHolder.tvUnlock.text =
                    " " + MyApplication.getInstance().dbHelper.getString(R.string.text_unlock)
                viewHolder.tvUnlock.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.ic_lock,
                    0,
                    R.drawable.ic_right_arrow,
                    0
                )
            }
            viewHolder.tvDreamPoint.text =
                MyApplication.getInstance().dbHelper.getString(R.string.Dp) + " " + model.dreamPoint
            var isItemFound = false
            if (cartModel != null && model.itemId == cartModel.itemId) {
                isItemFound = true
                val itemQuantity = cartModel.qty
                /*offer section start*/if (cartModel.isOffer) {
                    if (cartModel.offerType.equals("WalletPoint", ignoreCase = true)) {
                        val freeWalletPoint = cartModel.totalFreeWalletPoint
                        val sfreewalletDP = DecimalFormat("##.##").format(freeWalletPoint)
                        if (freeWalletPoint > 0) {
                            viewHolder.freeItemQutTV.text = sfreewalletDP
                        }
                    } else if (cartModel.offerType.equals("ItemMaster", ignoreCase = true)) {
                        val freeItemQuantity = cartModel.offerFreeItemQuantity!!.toInt()
                        if (freeItemQuantity > 0) {
                            viewHolder.freeItemQutTV.text = "" + freeItemQuantity
                        }
                    } else {
                        val remainingLeft = cartModel.offerQtyAvaiable - itemQuantity
                        if (remainingLeft > 0) {
                            viewHolder.leftItemsTV.text = "" + remainingLeft
                        } else {
                            viewHolder.leftItemsTV.text = "0"
                        }
                    }
                }
                // offer section end
                if (itemQuantity > 0) {
                    viewHolder.tvselectedItemQuantity.text = "" + itemQuantity
                    if (flashofferFlag) {
                        val price = "<font color=#FF4500>&#8377; " + DecimalFormat("##.##").format(
                            itemQuantity * model.flashDealSpecialPrice
                        )
                        viewHolder.tvSelectedItemPrice.text = Html.fromHtml(price)
                    } else if (SharePrefs.getInstance(activity)
                            .getBoolean(SharePrefs.IS_PRIME_MEMBER) && model.isPrimeItem
                    ) {
                        val price = "<font color=#FF4500>&#8377; " + DecimalFormat("##.##").format(
                            itemQuantity * model.primePrice
                        )
                        viewHolder.tvSelectedItemPrice.text = Html.fromHtml(price)
                    } else {
                        val price = "<font color=#FF4500>&#8377; " + DecimalFormat("##.##").format(
                            itemQuantity * model.unitPrice
                        )
                        viewHolder.tvSelectedItemPrice.text = Html.fromHtml(price)
                    }
                } else {
                    isItemFound = false
                }
            } else {
                isItemFound = false
            }
            if (!isItemFound) {
                val itemQuantity = 0
                viewHolder.tvselectedItemQuantity.text = "" + itemQuantity
                val price =
                    "<font color=#FF4500>&#8377; " + DecimalFormat("##.##").format(itemQuantity * model.unitPrice)
                viewHolder.tvSelectedItemPrice.text = Html.fromHtml(price)
                viewHolder.btnAdd.visibility = View.VISIBLE
                viewHolder.visible.visibility = View.GONE
            } else {
                viewHolder.btnAdd.visibility = View.GONE
                viewHolder.visible.visibility = View.VISIBLE
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun itemAddRemove(viewHolder: ViewHolder, model: ItemListModel, addItem: Boolean) {
        // progress bar
        if (viewHolder.progressBar != null) viewHolder.progressBar.visibility = View.INVISIBLE
        progressBar = viewHolder.progressBar
        try {
            var FreeItemQuantity = 0
            var FreeWalletPoint = 0.0
            var addFlag = false
            var flashDealFlag = false
            flashofferFlag = false
            var calUnitPrice = 0.0
            var isflashDealUsed = false
            var isPrimeItem = false

            //get item limit
            var itemQuantity = viewHolder.tvselectedItemQuantity.text.toString().toInt()
            if (model.isOffer) {
                val jsonFlashString = SharePrefs.getStringSharedPreferences(
                    activity,
                    SharePrefs.ITEM_FLASH_DEAL_USED_JSON
                )
                try {
                    if (!jsonFlashString.isEmpty()) {
                        val jsonObject = JSONObject(jsonFlashString)
                        if (jsonObject.has(model.itemId.toString())) {
                            if (jsonObject[model.itemId.toString()] == "1") {
                                isflashDealUsed = true
                            }
                        }
                    }
                    if (model.offerType != null) {
                        if (model.offerType.equals("FlashDeal", ignoreCase = true)) {
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
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
            if (addItem) {
                //plus btn
                if (!flashDealFlag) {
                    itemQuantity += model.minOrderQty
                    //Item Limit code
                    if (model.isItemLimit) {
                        addFlag = setlimit(viewHolder, model, addFlag, itemQuantity, true)
                    } else {
                        if (itemQuantity > 0) {
                            if (model.billLimitQty != 0) {
                                addFlag = setBillLimit(viewHolder, model, addFlag, itemQuantity)
                            } else {
                                viewHolder.tvselectedItemQuantity.text = "" + itemQuantity
                                addFlag = true
                            }
                        }
                    }
                } else {
                    if (model.offerQtyAvaiable <= itemQuantity + model.minOrderQty) {
                        Toast.makeText(
                            activity,
                            MyApplication.getInstance().dbHelper.getString(R.string.no_item_available),
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            activity,
                            MyApplication.getInstance().dbHelper.getString(R.string.only_add_maximum_item) + " " + model.flashDealMaxQtyPersonCanTake,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } else {
                //minus btn
                if (model.isItemLimit) {
                    if (itemQuantity > 0) {
                        itemQuantity -= model.minOrderQty
                        addFlag = setlimit(viewHolder, model, addFlag, itemQuantity, false)
                    } else {
                        viewHolder.btnAdd.visibility = View.VISIBLE
                        // analytics for remove from cart
                        MyApplication.getInstance()
                            .updateAnalyticsCart(FirebaseAnalytics.Event.REMOVE_FROM_CART, model)
                    }
                } else {
                    if (itemQuantity > 0) {
                        itemQuantity -= model.minOrderQty
                        viewHolder.tvselectedItemQuantity.text = "" + itemQuantity
                        addFlag = true
                    } else {
                        viewHolder.btnAdd.visibility = View.VISIBLE
                        // analytics for remove from cart
                        MyApplication.getInstance()
                            .updateAnalyticsCart(FirebaseAnalytics.Event.REMOVE_FROM_CART, model)
                    }
                }
            }
            if (addFlag) {
                /*offer section*/
                if (model.isOffer) {
                    val offerMinimumQty = model.offerMinimumQty
                    val customerActive =
                        SharePrefs.getInstance(activity).getBoolean(SharePrefs.CUST_ACTIVE)
                    when (model.offerType) {
                        "WalletPoint" -> if (customerActive) {
                            if (offerMinimumQty != 0) {
                                if (model.offerWalletPoint != null) {
                                    if (itemQuantity >= offerMinimumQty) {
                                        FreeWalletPoint = model.offerWalletPoint!!
                                        val calfreeItemQty = itemQuantity / offerMinimumQty
                                        FreeWalletPoint *= calfreeItemQty.toDouble()
                                        val sfreewalletDP =
                                            DecimalFormat("##.##").format(FreeWalletPoint)
                                        if (FreeWalletPoint > 0) {
                                            viewHolder.freeItemQutTV.text = sfreewalletDP
                                        }
                                    } else {
                                        viewHolder.freeItemQutTV.text = "0"
                                    }
                                } else {
                                    Toast.makeText(
                                        activity,
                                        MyApplication.getInstance().dbHelper.getString(R.string.mini_qty_should_not_be_zero),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }

                        "ItemMaster" -> if (customerActive) {
                            if (offerMinimumQty != 0) {
                                if (model.offerFreeItemQuantity != null) {
                                    if (itemQuantity >= offerMinimumQty) {
                                        FreeItemQuantity = model.offerFreeItemQuantity!!.toInt()
                                        val calfreeItemQty = itemQuantity / offerMinimumQty
                                        FreeItemQuantity *= calfreeItemQty
                                        if (FreeItemQuantity > 0) {
                                            viewHolder.freeItemQutTV.text =
                                                FreeItemQuantity.toString()
                                        }
                                    } else {
                                        viewHolder.freeItemQutTV.text = "0"
                                    }
                                }
                            } else {
                                Toast.makeText(
                                    activity,
                                    "Minimum Qty should not be zero",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                        "FlashDeal" -> if (!isflashDealUsed) {
                            val remainingLeft = model.offerQtyAvaiable - itemQuantity
                            if (remainingLeft > 0) {
                                viewHolder.leftItemsTV.text = "" + remainingLeft
                            } else {
                                viewHolder.leftItemsTV.text = "0"
                            }
                            flashofferFlag = true
                        } else {
                            flashofferFlag = false
                        }
                    }
                }
                if (flashofferFlag) {
                    val price =
                        "<font color=#FF4500>&#8377; " + DecimalFormat("##.##").format(itemQuantity * model.flashDealSpecialPrice)
                    viewHolder.tvSelectedItemPrice.text = Html.fromHtml(price)
                    calUnitPrice = model.flashDealSpecialPrice
                } else if (SharePrefs.getInstance(activity)
                        .getBoolean(SharePrefs.IS_PRIME_MEMBER) && model.isPrimeItem
                ) {
                    val price = "<font color=#FF4500>&#8377; " + DecimalFormat("##.##").format(
                        itemQuantity * model.primePrice
                    )
                    viewHolder.tvSelectedItemPrice.text = Html.fromHtml(price)
                    calUnitPrice = model.primePrice
                    isPrimeItem = true
                } else {
                    val price =
                        "<font color=#FF4500>&#8377; " + DecimalFormat("##.##").format(itemQuantity * model.unitPrice)
                    viewHolder.tvSelectedItemPrice.text = Html.fromHtml(price)
                    calUnitPrice = model.unitPrice
                }
                viewHolder.progressBar!!.visibility = View.VISIBLE
                activity.addItemInCartItemArrayList(
                    model.itemId,
                    itemQuantity,
                    calUnitPrice,
                    model,
                    FreeItemQuantity,
                    FreeWalletPoint,
                    isPrimeItem,
                    this
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setlimit(
        viewHolder: ViewHolder,
        model: ItemListModel,
        addFlag: Boolean,
        itemQuantity: Int,
        add: Boolean
    ): Boolean {
        var addFlag = addFlag
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
        if (add) {
            if (total + itemQuantity > itemlimitqty) {
                Utils.setToast(
                    activity,
                    MyApplication.getInstance().dbHelper.getString(R.string.additemToast) + " " + model.itemLimitQty + " " + MyApplication.getInstance().dbHelper.getString(
                        R.string.additemToast_2
                    )
                )
            } else {
                if (model.billLimitQty != 0) {
                    addFlag = setBillLimit(viewHolder, model, addFlag, itemQuantity)
                    if (addFlag) {
                        availqty = itemlimitqty - itemQuantity
                        if (availqty >= 0) {
                            viewHolder.availQty.text = availqty.toString()
                        }
                    }
                } else {
                    viewHolder.tvselectedItemQuantity.text = "" + itemQuantity
                    addFlag = true
                    availqty = itemlimitqty - totalItemqty
                    if (availqty >= 0) {
                        viewHolder.availQty.text = availqty.toString()
                    }
                }
            }
        } else {
            availqty = itemlimitqty - totalItemqty
            if (availqty >= 0) {
                viewHolder.availQty.text = availqty.toString()
            }
            viewHolder.tvselectedItemQuantity.text = "" + itemQuantity
            addFlag = true
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
                    viewHolder.tvselectedItemQuantity.text = "" + itemQuantity
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
                        viewHolder.tvselectedItemQuantity.text = "" + itemQuantity
                        addFlag = true
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return addFlag
    }

    override fun getItemCount(): Int {
        return if (list == null) 0 else listSize
    }

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

    override fun onItemClick(position: Int, itemAdded: Boolean) {
        progressBar!!.visibility = View.INVISIBLE
        if (!itemAdded) notifyDataSetChanged()
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivItemImage: ImageView
        val favItem: ImageView
        val ivMinusBtn: ImageView
        val ivPlusBtn: ImageView
        val tvItemName: TextView
        val tvSelectedItemPrice: TextView
        val tvselectedItemQuantity: TextView
        var price: TextView? = null
        val tvMrp: TextView
        val tvPrice: TextView
        val tvMoq: TextView
        val tvMultiMoq: TextView
        val tvPPrice: TextView
        val tvUnlock: TextView
        val tvSchemeText: TextView
        val tvMargin: TextView
        val tvDreamPoint: TextView
        val tvOffer: TextView
        val freeItemQutTV: TextView
        val freeDepePointTV: TextView
        val availQty: TextView
        val btnAdd: Button
        val leftItemsTV: TextView
        val buyValueText: TextView
        val GetValueTextView: TextView
        val tvFlashdealPrice: TextView
        val remaingSecTV: TextView
        val freeItemIV: ImageView
        val visible: LinearLayout
        val availQtyLayout: LinearLayout
        val mainOfferViewLL: LinearLayout
        val flashOfferViewLL: LinearLayout
        var mrpLayout: LinearLayout? = null
        var moqLayout: LinearLayout? = null
        var pricelayout: LinearLayout? = null
        val dreampointlayout: LinearLayout
        val liPrime: LinearLayout
        val LLItemMain: LinearLayout
        var offerLayout: RelativeLayout? = null
        val progressBar: GifImageView?
        val llInActiveCustomer: LinearLayout
        val tvMrpText: TextView
        val tvRemainingQtyText: TextView
        val tvItemLeftText: TextView
        val tvEndInText: TextView
        val tvFreeItemNotActiveText: TextView

        init {
            progressBar = view.findViewById(R.id.iv_progress)
            ivItemImage = view.findViewById(R.id.product_image)
            favItem = view.findViewById(R.id.favItem)
            if (isSlider){
                mrpLayout = view.findViewById(R.id.mrp_layout)
                moqLayout = view.findViewById(R.id.moq_layout)
                offerLayout = view.findViewById(R.id.offer_layout)
                pricelayout = view.findViewById(R.id.pricelayout)
                price = view.findViewById(R.id.price)
            }
            dreampointlayout = view.findViewById(R.id.dreampointlayout)
            liPrime = view.findViewById(R.id.liPrime)
            tvSelectedItemPrice = view.findViewById(R.id.tvSelectedItemPrice)
            LLItemMain = view.findViewById(R.id.LLItemMain)
            tvselectedItemQuantity = view.findViewById(R.id.tv_selected_itemQuantity)
            ivMinusBtn = view.findViewById(R.id.minus_btn)
            ivPlusBtn = view.findViewById(R.id.plus_btn)
            tvItemName = view.findViewById(R.id.tv_item_name)
            tvMrp = view.findViewById(R.id.tv_mrp)
            tvPrice = view.findViewById(R.id.tv_price)
            tvMoq = view.findViewById(R.id.tvMoq)
            tvMargin = view.findViewById(R.id.tvMargin)
            tvDreamPoint = view.findViewById(R.id.tv_dream_point)
            tvMultiMoq = view.findViewById(R.id.tvMultiMoq)
            tvSchemeText = view.findViewById(R.id.tvSchemeText)
            tvPPrice = view.findViewById(R.id.tvPPrice)
            tvUnlock = view.findViewById(R.id.tvUnlock)
            tvOffer = view.findViewById(R.id.tv_offer)
            btnAdd = view.findViewById(R.id.add_item_btn)
            freeItemIV = view.findViewById(R.id.iv_free_iteam)
            freeItemQutTV = view.findViewById(R.id.tv_free_item_qut)
            visible = view.findViewById(R.id.visible)
            availQtyLayout = view.findViewById(R.id.avail_qty_layout)
            mainOfferViewLL = view.findViewById(R.id.liOfferView)
            flashOfferViewLL = view.findViewById(R.id.ll_flash_offer_view)
            freeDepePointTV = view.findViewById(R.id.tv_free_depe_point)
            leftItemsTV = view.findViewById(R.id.tv_left_items)
            buyValueText = view.findViewById(R.id.tv_buy_value)
            GetValueTextView = view.findViewById(R.id.tv_get_value)
            availQty = view.findViewById(R.id.avail_qty)
            remaingSecTV = view.findViewById(R.id.tv_remaing_secand)
            tvFlashdealPrice = view.findViewById(R.id.tv_flashdeal_price)
            llInActiveCustomer = view.findViewById(R.id.ll_in_active_user)
            tvMrpText = view.findViewById(R.id.tv_mrp_text)
            tvRemainingQtyText = view.findViewById(R.id.tv_remaining_qty_text)
            tvItemLeftText = view.findViewById(R.id.tv_item_left_text)
            tvEndInText = view.findViewById(R.id.tv_end_in_text)
            tvFreeItemNotActiveText = view.findViewById(R.id.tv_free_item_not_active_text)
            customRunnable = CustomRunnable(handler, remaingSecTV, 10000)
        }

        fun timerExpire(endTime: Long) {
            handler.removeCallbacks(customRunnable!!)
            customRunnable!!.holder = remaingSecTV
            customRunnable!!.millisUntilFinished = endTime //Current time - received time
            handler.postDelayed(customRunnable!!, 1000)
        }
    }
}