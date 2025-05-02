package app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.notification

import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.content.res.AppCompatResources
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.ItemListModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.CategoryOrderItemsBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.interfaces.AdapterInterface
import app.retailer.krina.shop.com.mp_shopkrina_retailer.interfaces.OnItemClick
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.activity.MembershipPlanActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.SharePrefs
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.CategoryItemOrderInfo
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.MoqAdapter
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.productDetails.ProductDetailsActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.CustomRunnable
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Logger
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.RetailerSDKApp
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.TextUtils
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import org.json.JSONException
import org.json.JSONObject
import pl.droidsonroids.gif.GifImageView
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class NotificationItemListAdapter(
    private val activity: ActionBleNotificationActivity,
    private var list: ArrayList<ItemListModel>?
) : RecyclerView.Adapter<NotificationItemListAdapter.ViewHolder>(), OnItemClick {

    private var customAlertDialog: AlertDialog? = null
    private val handler = Handler(Looper.getMainLooper())
    private var adapter: MoqAdapter? = null
    private val vectorDrawable: Drawable?
    private var flashofferFlag = false
    private var customRunnable: CustomRunnable? = null
    private var progressBar: GifImageView? = null


    fun setItemListCategory(list: ArrayList<ItemListModel>?) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        return ViewHolder(
            CategoryOrderItemsBinding.inflate(
                LayoutInflater.from(viewGroup.context),
                 viewGroup, false
            )
        )
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        val model = list!![i]
        //set String
        viewHolder.mBinding.tvMrpText.text =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.item_mrp)
        viewHolder.mBinding.tvRemainingQtyText.text =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.remaining_qty) + " "
        viewHolder.mBinding.tvItemLeftText.text =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.item_left)
        viewHolder.mBinding.tvEndInText.text =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.end_in_text)
        viewHolder.mBinding.tvFreeItemNotActiveText.text =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.inactive_customer_msg)
        viewHolder.btnAdd.text = RetailerSDKApp.getInstance().dbHelper.getString(R.string.add_btn)

        // set MOQ
        if (model.moqList != null && model.moqList.size > 0) {
            viewHolder.tvMoq.visibility = View.GONE
            viewHolder.tvMultiMoq.visibility = View.VISIBLE
        } else {
            viewHolder.tvMoq.visibility = View.VISIBLE
            viewHolder.tvMultiMoq.visibility = View.GONE
        }
        viewHolder.tvMoq.text =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.item_moq) + " " + model.minOrderQty
        viewHolder.tvMultiMoq.text =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.item_moq) + " " + model.minOrderQty
        if (!TextUtils.isNullOrEmpty(model.scheme)) {
            viewHolder.mBinding.tvSchemeText.visibility = View.VISIBLE
            viewHolder.mBinding.tvSchemeText.text = "" + model.scheme
        } else {
            viewHolder.mBinding.tvSchemeText.visibility = View.GONE
        }
        viewHolder.tvItemName.text = model.itemname
        viewHolder.leftItemsTV.setBackgroundResource(R.drawable.ic_count_bg)
        viewHolder.remaingSecTV.setBackgroundResource(R.drawable.ic_count_bg)
        //set  value on UI
        setValueInUI(viewHolder, model)
        model.isChecked = true
        //Minis Btn clicked
        viewHolder.ivMinusBtn.setOnClickListener { view: View? ->
            //click effect
            Utils.buttonEffect(viewHolder.ivMinusBtn)
            itemAddRemove(viewHolder, model, false, false)
        }
        //plus Btn clicked
        viewHolder.ivPlusBtn.setOnClickListener { view: View? ->
            Utils.buttonEffect(viewHolder.ivPlusBtn)
            itemAddRemove(viewHolder, model, true, false)
        }
        // fav section
        viewHolder.mBinding.favItem.setOnClickListener { v: View? ->
            addRemoveFav(
                model,
                viewHolder
            )
        }

        //Add Btn clicked
        viewHolder.btnAdd.setOnClickListener { v: View? ->
            viewHolder.mBinding.visible.visibility = View.VISIBLE
            viewHolder.btnAdd.visibility = View.GONE
            itemAddRemove(viewHolder, model, true, true)
        }
        //checkout clicked
        viewHolder.LLItemMain.setOnClickListener { v: View? ->
            viewHolder.LLItemMain.isClickable = false
            detailsScree(model)
        }
        /*MOQ popup open here*/viewHolder.tvMultiMoq.setOnClickListener {
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
            ivClose.setOnClickListener { dialog.dismiss() }

            tvDSelectQty.text =
                RetailerSDKApp.getInstance().dbHelper.getString(R.string.select_quantities_for)
            tvDMoq.text = RetailerSDKApp.getInstance().dbHelper.getString(R.string.moq)
            tvDMrp.text = RetailerSDKApp.getInstance().dbHelper.getString(R.string.mrp)
            tvDRs.text = RetailerSDKApp.getInstance().dbHelper.getString(R.string.rs)
            tvDMargin.text = RetailerSDKApp.getInstance().dbHelper.getString(R.string.margins_d)
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
                        customAlertDialog!!.dismiss()
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
            customAlertDialog!!.show()
        }
        viewHolder.mBinding.tvUnlock.setOnClickListener { v: View? ->
            if (!SharePrefs.getInstance(
                    activity
                ).getBoolean(SharePrefs.IS_PRIME_MEMBER) && model.isPrimeItem
            ) {
                activity.startActivity(Intent(activity, MembershipPlanActivity::class.java))
            }
        }
    }

    override fun getItemCount(): Int {
        return if (list == null) 0 else list!!.size
    }

    private fun detailsScree(model: ItemListModel) {
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
        args.putSerializable("ITEM_LIST", model)
        intent.putExtras(args)
        activity.startActivity(intent)
        Utils.leftTransaction(activity)
    }

    private fun addRemoveFav(model: ItemListModel, viewHolder: ViewHolder) {
        if (RetailerSDKApp.getInstance().noteRepository.isItemWishList(model.itemId)) {
            viewHolder.mBinding.favItem.setImageResource(R.drawable.ic_favourite)
            RetailerSDKApp.getInstance().noteRepository.deleteTask(model)
            Utils.addFav(model.itemId, false, activity)
        } else {
            viewHolder.mBinding.favItem.setImageResource(R.drawable.ic_favorite_red)
            RetailerSDKApp.getInstance().noteRepository.insertTask(model)
            Utils.addFav(model.itemId, true, activity)
            RetailerSDKApp.getInstance().analyticAddWishList(model)
        }
    }

    private fun setValueInUI(viewHolder: ViewHolder, model: ItemListModel) {
        flashofferFlag = false
        try {
            // check item in wishList
            if (RetailerSDKApp.getInstance().noteRepository.isItemWishList(model.itemId)) {
                viewHolder.mBinding.favItem.setImageResource(R.drawable.ic_favorite_red)
            } else {
                viewHolder.mBinding.favItem.setImageResource(R.drawable.ic_favourite)
            }

            //set offer UI
            if (model.isOffer) {
                /*for inactive customer*/
                if (!SharePrefs.getInstance(activity).getBoolean(SharePrefs.CUST_ACTIVE)) {
                    viewHolder.mBinding.llInActiveUser.visibility = View.VISIBLE
                }
                viewHolder.tvOffer.visibility = View.GONE
                viewHolder.mainOfferViewLL.visibility = View.VISIBLE
                viewHolder.flashOfferViewLL.visibility = View.GONE
                viewHolder.mBinding.favItem.visibility = View.GONE
                viewHolder.tvFlashdealPrice.visibility = View.GONE
                val spItemName = model.itemname
                val freeOfferTextBuy =
                    "<font color=#fe4e4e>Buy&nbsp;" + model.offerMinimumQty + "&nbsp;pcs&nbsp;</font>" + "<font color=#000000>&nbsp;" + spItemName + " </font>"
                viewHolder.buyValueText.text = Html.fromHtml(freeOfferTextBuy)
                var freeOfferTextGet = ""
                when (model.offerType) {
                    "WalletPoint" -> {
                        // String sfreewalletDP = new DecimalFormat("##.##").format(model.getOfferWalletPoint());
                        viewHolder.freeItemQutTV.text = "0"
                        viewHolder.freeItemIV.setBackgroundResource(R.drawable.ic_gift_bg)
                        viewHolder.freeDepePointTV.text =
                            RetailerSDKApp.getInstance().dbHelper.getString(R.string.Dp)
                        val sfreewalletDP = DecimalFormat("##.##").format(model.offerWalletPoint)
                        freeOfferTextGet =
                            "<font color=#fe4e4e>Get&nbsp;$sfreewalletDP&nbsp;Free</font><font color=#000000>&nbsp;Dream Points</font>"
                    }

                    "ItemMaster" -> {
                        // viewHolder.freeItemQutTV.setText(String.valueOf(model.getOfferFreeItemQuantity()));
                        if (!TextUtils.isNullOrEmpty(model.offerFreeItemImage)) {
                            Glide.with(activity).load(model.offerFreeItemImage)
                                .placeholder(vectorDrawable).into(viewHolder.freeItemIV)
                        } else {
                            viewHolder.freeItemIV.setImageDrawable(vectorDrawable)
                        }
                        viewHolder.freeItemQutTV.text = "0"
                        viewHolder.freeDepePointTV.text =
                            RetailerSDKApp.getInstance().dbHelper.getString(R.string.free)
                        // String[] spItemFreeItemName = model.getOfferFreeItemName().split("(?<=\\D)(?=\\d)");
                        freeOfferTextGet =
                            "<font color=#fe4e4e>Get " + model.offerFreeItemQuantity + "&nbsp;Free </font>" + "<font color=#000000>&nbsp;" + model.offerFreeItemName + " </font>"
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
                            //  timeDeference(moqPojoArrayList, viewHolder);
                            if (model.flashDealSpecialPrice != 0.0) {
                                viewHolder.tvPrice.paintFlags =
                                    viewHolder.tvMrp.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                                viewHolder.tvFlashdealPrice.text =
                                    " | " + DecimalFormat("##.##").format(model.flashDealSpecialPrice)
                            }
                            val currMillis = System.currentTimeMillis()
                            val sdf1 = SimpleDateFormat(Utils.myFormat, Locale.getDefault())
                            sdf1.timeZone = TimeZone.getDefault()
                            val endTime = sdf1.parse(model.offerEndTime)
                            val currentTime = sdf1.parse(model.currentStartTime)
                            val currentEpoch = currentTime.time
                            val millse = endTime.time - currentEpoch
                            viewHolder.timerExpire(millse)
                        } else {
                            flashofferFlag = false
                            viewHolder.mainOfferViewLL.visibility = View.GONE
                            viewHolder.flashOfferViewLL.visibility = View.GONE
                            viewHolder.mBinding.favItem.visibility = View.VISIBLE
                            viewHolder.tvFlashdealPrice.visibility = View.GONE
                            viewHolder.tvPrice.paintFlags = 0
                        }
                    }
                }
                viewHolder.GetValueTextView.text = Html.fromHtml(freeOfferTextGet)
            } else {
                viewHolder.mainOfferViewLL.visibility = View.GONE
                viewHolder.flashOfferViewLL.visibility = View.GONE
                viewHolder.mBinding.favItem.visibility = View.VISIBLE
                viewHolder.tvFlashdealPrice.visibility = View.GONE
                viewHolder.tvPrice.paintFlags = 0
            }
            if (!TextUtils.isNullOrEmpty(model.logoUrl)) {
                Glide.with(activity).load(model.logoUrl).placeholder(vectorDrawable)
                    .into(viewHolder.ivItemImage)
            } else {
                viewHolder.ivItemImage.setImageDrawable(vectorDrawable)
            }
            val sPRICE = "| &#8377; " + DecimalFormat("##.##").format(model.unitPrice)
            val sMargin =
                RetailerSDKApp.getInstance().dbHelper.getString(R.string.moq_margin) + " " + DecimalFormat(
                    "##.##"
                ).format(model.marginPoint!!.toDouble()) + "%"
            val sMRP = DecimalFormat("##.##").format(model.price)
            //set values
            viewHolder.tvMrp.text = sMRP
            viewHolder.tvMrp.paintFlags = viewHolder.tvMrp.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            viewHolder.tvPrice.text = Html.fromHtml(sPRICE)
            viewHolder.tvMargin.text = sMargin
            if (model.isPrimeItem) {
                viewHolder.mBinding.liPrime.visibility = View.VISIBLE
                viewHolder.mBinding.tvPPrice.text =
                    (SharePrefs.getInstance(activity).getString(SharePrefs.PRIME_NAME)
                            + " " + RetailerSDKApp.getInstance().dbHelper.getString(R.string.price)
                            + ": ₹" + DecimalFormat("##.##").format(model.primePrice))
                viewHolder.mBinding.tvPrice.setTextColor(activity.resources.getColor(R.color.grey))
            } else {
                viewHolder.mBinding.liPrime.visibility = View.GONE
                viewHolder.mBinding.tvPrice.setTextColor(Color.parseColor("#FF4500"))
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
                viewHolder.mBinding.tvUnlock.text = "Unlock"
                viewHolder.mBinding.tvUnlock.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.ic_lock,
                    0,
                    R.drawable.ic_right_arrow,
                    0
                )
            }
            val cartModel = RetailerSDKApp.getInstance().noteRepository.getItemByMrpId(
                model.itemId,
                model.itemMultiMRPId
            )
            val qtyDTO = RetailerSDKApp.getInstance().noteRepository.getQtyTotalQtyByMrpId(
                model.itemId,
                model.itemMultiMRPId
            )

            //set UI for ItemLimit
            if (model.isItemLimit) {
                var totalItemInCart = 0
                var itemlimitQuantity = 0
                var totalAvailQty = 0
                viewHolder.mBinding.availQtyLayout.visibility = View.VISIBLE
                itemlimitQuantity = model.itemLimitQty
                if (qtyDTO != null) {
                    totalItemInCart += qtyDTO.totalQuantity
                    totalAvailQty = itemlimitQuantity - totalItemInCart
                } else {
                    totalAvailQty = model.itemLimitQty
                }
                viewHolder.mBinding.availQty.text = totalAvailQty.toString()
            } else {
                viewHolder.mBinding.availQtyLayout.visibility = View.GONE
            }
            viewHolder.tvDreamPoint.text =
                RetailerSDKApp.getInstance().dbHelper.getString(R.string.Dp) + " " + model.dreamPoint
            var isItemFound: Boolean
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
                /*offer section end*/if (itemQuantity > 0) {
                    viewHolder.tvselectedItemQuantity.text = "" + itemQuantity
                    var price = "0"
                    price = if (flashofferFlag) {
                        "<font color=#FF4500>&#8377; " + DecimalFormat("##.##").format(itemQuantity * model.flashDealSpecialPrice)
                    } else if (SharePrefs.getInstance(activity)
                            .getBoolean(SharePrefs.IS_PRIME_MEMBER) && model.isPrimeItem
                    ) {
                        "<font color=#FF4500>&#8377; " + DecimalFormat("##.##").format(itemQuantity * model.primePrice)
                    } else {
                        "<font color=#FF4500>&#8377; " + DecimalFormat("##.##").format(itemQuantity * model.unitPrice)
                    }
                    viewHolder.tvSelectedItemPrice.text = Html.fromHtml(price)
                } else {
                    isItemFound = false
                }
            } else {
                isItemFound = false
            }
            if (!isItemFound) {
                val itemQuantity = 0
                viewHolder.tvselectedItemQuantity.text = "" + itemQuantity
                Logger.logD("Adapter", "ItemQ3::$itemQuantity")
                val price = "<font color=#FF4500>&#8377; " + DecimalFormat("##.##").format(
                    itemQuantity * model.unitPrice
                )
                viewHolder.tvSelectedItemPrice.text = Html.fromHtml(price)
                viewHolder.btnAdd.visibility = View.VISIBLE
                viewHolder.mBinding.visible.visibility = View.GONE
            } else {
                viewHolder.btnAdd.visibility = View.GONE
                viewHolder.mBinding.visible.visibility = View.VISIBLE
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
                            if (model.flashDealMaxQtyPersonCanTake > itemQuantity && model.offerQtyAvaiable > itemQuantity) {
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
                        addFlag = setItemLimit(viewHolder, model, addFlag, itemQuantity, true)
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
                    if (addBtn) {
                        viewHolder.mBinding.visible.visibility = View.GONE
                        viewHolder.btnAdd.visibility = View.VISIBLE
                        Toast.makeText(activity, "No items available", Toast.LENGTH_SHORT).show()
                    } else {
                        if (model.offerQtyAvaiable <= itemQuantity) {
                            Toast.makeText(activity, "No items available", Toast.LENGTH_SHORT)
                                .show()
                        } else {
                            Toast.makeText(
                                activity,
                                "Only add maximum item  " + model.flashDealMaxQtyPersonCanTake,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            } else {
                //minus btn
                if (model.isItemLimit) {
                    if (itemQuantity > 0) {
                        itemQuantity -= model.minOrderQty
                        addFlag = setItemLimit(viewHolder, model, addFlag, itemQuantity, false)
                    } else {
                        viewHolder.btnAdd.visibility = View.VISIBLE
                    }
                } else {
                    if (itemQuantity > 0) {
                        itemQuantity -= model.minOrderQty
                        viewHolder.tvselectedItemQuantity.text = "" + itemQuantity
                        addFlag = true
                    } else {
                        viewHolder.btnAdd.visibility = View.VISIBLE
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
                                    //event trigger
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
                                        "Minimum Qty should not be zero",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        } /* else {
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
                        } /* else {
                                Toast.makeText(activity, R.string.inactive_customer_msg, Toast.LENGTH_SHORT).show();
                            }*/
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
                    val price = "<font color=#FF4500>&#8377; " + DecimalFormat("##.##").format(
                        itemQuantity * model.unitPrice
                    )
                    viewHolder.tvSelectedItemPrice.text = Html.fromHtml(price)
                    calUnitPrice = model.unitPrice
                }
                progressBar!!.visibility = View.VISIBLE
                activity.addItemInCartItemArrayList(
                    model.itemId, itemQuantity, calUnitPrice,
                    model, FreeItemQuantity, FreeWalletPoint, isPrimeItem, this
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
        val qtyDTO = RetailerSDKApp.getInstance().noteRepository.getQtyTotalQtyByMrpId(
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
        if (total > 0) {
            if (total + itemQuantity > itemlimitqty) {
                Utils.setToast(
                    activity,
                    RetailerSDKApp.getInstance().dbHelper.getString(R.string.additemToast) + " " + model.itemLimitQty + " " + RetailerSDKApp.getInstance().dbHelper.getString(
                        R.string.additemToast_2
                    )
                )
            } else {
                if (model.billLimitQty != 0) {
                    addFlag = setBillLimit(viewHolder, model, addFlag, itemQuantity)
                    if (addFlag) {
                        availqty = itemlimitqty - totalItemqty
                        if (availqty >= 0) {
                            viewHolder.mBinding.availQty.text = availqty.toString()
                        }
                    }
                } else {
                    viewHolder.tvselectedItemQuantity.text = "" + itemQuantity
                    addFlag = true
                    availqty = itemlimitqty - totalItemqty
                    if (availqty >= 0) {
                        viewHolder.mBinding.availQty.text = availqty.toString()
                    }
                }
            }
        } else {
            if (itemQuantity > 0) {
                if (itemQuantity > model.itemLimitQty) {
                    Utils.setToast(
                        activity,
                        RetailerSDKApp.getInstance().dbHelper.getString(R.string.additemToast) + " " + model.itemLimitQty + " " + RetailerSDKApp.getInstance().dbHelper.getString(
                            R.string.additemToast_2
                        )
                    )
                } else {
                    if (model.billLimitQty != 0) {
                        addFlag = setBillLimit(viewHolder, model, addFlag, itemQuantity)
                        if (addFlag) {
                            availqty = itemlimitqty - itemQuantity
                            if (availqty >= 0) {
                                viewHolder.mBinding.availQty.text = availqty.toString()
                            }
                        }
                    } else {
                        viewHolder.tvselectedItemQuantity.text = "" + itemQuantity
                        addFlag = true
                        availqty = itemlimitqty - itemQuantity
                        if (availqty >= 0) {
                            viewHolder.mBinding.availQty.text = availqty.toString()
                        }
                    }
                }
            }
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
            val total =
                RetailerSDKApp.getInstance().noteRepository.getQtyByMultiMrp(
                    model.itemId,
                    model.itemMultiMRPId
                )
            var itemlimitqty = 0
            itemlimitqty = model.billLimitQty
            if (total > 0) {
                if (total + itemQuantity > itemlimitqty) {
                    Utils.setToast(
                        activity,
                        RetailerSDKApp.getInstance().dbHelper.getString(R.string.bill_limit_text) + " " + model.billLimitQty + " item"
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
                            RetailerSDKApp.getInstance().dbHelper.getString(R.string.bill_limit_text) + " " + model.billLimitQty + " item"
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

    override fun onItemClick(position: Int, itemAdded: Boolean) {
        progressBar!!.visibility = View.INVISIBLE
        if (!itemAdded) {
            notifyDataSetChanged()
        }
    }

    inner class ViewHolder(val mBinding: CategoryOrderItemsBinding) : RecyclerView.ViewHolder(
        mBinding.root
    ) {
        val ivItemImage: ImageView
        val tvItemName: TextView
        val tvSelectedItemPrice: TextView
        val tvselectedItemQuantity: TextView
        val ivMinusBtn: ImageView
        val ivPlusBtn: ImageView
        val tvMrp: TextView
        val tvPrice: TextView
        val tvMoq: TextView
        val tvMultiMoq: TextView
        val tvMargin: TextView
        val tvDreamPoint: TextView
        val tvOffer: TextView
        val freeItemQutTV: TextView
        val freeDepePointTV: TextView
        val btnAdd: Button
        val leftItemsTV: TextView
        val buyValueText: TextView
        val GetValueTextView: TextView
        val tvFlashdealPrice: TextView
        val remaingSecTV: TextView
        val freeItemIV: ImageView
        val mainOfferViewLL: LinearLayout
        val flashOfferViewLL: LinearLayout
        var relItemDetails: RelativeLayout? = null
        var LLItemMain: LinearLayout
        fun bind(obj: CategoryItemOrderInfo?) {
           // mBinding.executePendingBindings()
        }

        fun timerExpire(endTime: Long) {
            handler.removeCallbacks(customRunnable!!)
            customRunnable!!.holder = remaingSecTV
            customRunnable!!.millisUntilFinished = endTime //Current time - received time
            handler.postDelayed(customRunnable!!, 1000)
        }

        init {
            tvSelectedItemPrice = mBinding.tvSelectedItemPrice
            LLItemMain = mBinding.LLItemMain
            tvselectedItemQuantity = mBinding.tvSelectedItemQuantity
            ivMinusBtn = mBinding.minusBtn
            ivPlusBtn = mBinding.plusBtn
            ivItemImage = mBinding.productImage
            tvItemName = mBinding.tvItemName
            tvMrp = mBinding.tvMrp
            tvPrice = mBinding.tvPrice
            tvMoq = mBinding.tvMoq
            tvMargin = mBinding.tvMargin
            tvDreamPoint = mBinding.tvDreamPoint
            tvMultiMoq = mBinding.tvMultiMoq
            tvOffer = mBinding.tvOffer
            btnAdd = mBinding.addItemBtn
            freeItemIV = mBinding.ivFreeIteam
            freeItemQutTV = mBinding.tvFreeItemQut
            mainOfferViewLL = mBinding.liOfferView
            freeDepePointTV = mBinding.tvFreeDepePoint
            flashOfferViewLL = mBinding.llFlashOfferView
            leftItemsTV = mBinding.tvLeftItems
            buyValueText = mBinding.tvBuyValue
            GetValueTextView = mBinding.tvGetValue
            remaingSecTV = mBinding.tvRemaingSecand
            tvFlashdealPrice = mBinding.tvFlashdealPrice
            customRunnable = CustomRunnable(handler, remaingSecTV, 10000)
        }
    }

    init {
        vectorDrawable = AppCompatResources.getDrawable(activity, R.drawable.logo_grey)
    }
}