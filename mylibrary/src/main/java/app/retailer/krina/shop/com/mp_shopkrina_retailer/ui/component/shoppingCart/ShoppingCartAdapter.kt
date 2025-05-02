package app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.shoppingCart

import android.graphics.Paint
import android.os.Handler
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.ItemListModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.CheckoutItemsBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ItemMainListSectionBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ShoppingCartHeaderBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.interfaces.OnItemClick
import app.retailer.krina.shop.com.mp_shopkrina_retailer.interfaces.RemoveItemInterface
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.SharePrefs
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.CustomRunnable
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.RetailerSDKApp
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.TextUtils
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils
import com.squareup.picasso.Picasso
import org.json.JSONException
import org.json.JSONObject
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

/**
 * Created by user on 5/4/2017.
 */
class ShoppingCartAdapter(
    private val activity: ShoppingCartActivity,
    private var itemListArrayList: ArrayList<ItemListModel>?,
    private val removeItemInterface: RemoveItemInterface
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), OnItemClick {
    val HEADER = 0
    val VIEW = 1
    private val handler = Handler()
    private var customRunnable: CustomRunnable? = null
    fun setShopingCartItem(list: ArrayList<ItemListModel>?) {
        itemListArrayList = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            HEADER -> HeaderViewHolder(
                ShoppingCartHeaderBinding.inflate(
                    LayoutInflater.from(viewGroup.context),
                    viewGroup,
                    false
                )
            )

            VIEW -> ViewHolder(
                CheckoutItemsBinding.inflate(
                    LayoutInflater.from(viewGroup.context),
                    viewGroup,
                    false
                )
            )

            else -> ViewHolder(
                CheckoutItemsBinding.inflate(
                    LayoutInflater.from(viewGroup.context),
                    viewGroup,
                    false
                )
            )
        }
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, i: Int) {
        var flashofferFlag = false
        try {
            //set String
            val cartItemDetailsList = itemListArrayList!![i]
            if (viewHolder.itemViewType == HEADER) {
                (viewHolder as HeaderViewHolder).mBinding.title.text = cartItemDetailsList.storeName
                if (!TextUtils.isNullOrEmpty(cartItemDetailsList.storeLogo)) {
                    Picasso.get().load(cartItemDetailsList.storeLogo)
                        .placeholder(R.drawable.logo_grey)
                        .error(R.drawable.logo_grey)
                        .into(viewHolder.mBinding.ivStoreLogo)
                } else {
                    viewHolder.mBinding.ivStoreLogo.setImageResource(R.drawable.logo_grey)
                }
            } else {
                (viewHolder as ViewHolder).mBinding.tvMrpText.text =
                    RetailerSDKApp.getInstance().dbHelper.getString(
                        R.string.item_mrp
                    ) + " "
                viewHolder.mBinding.tvOutOfStock.text =
                    RetailerSDKApp.getInstance().dbHelper.getString(
                        R.string.text_out_of_stock
                    )
                viewHolder.mBinding.tvRemove.text =
                    RetailerSDKApp.getInstance().dbHelper.getString(R.string.text_remove)
                viewHolder.mBinding.tvRemainingQtyText.text =
                    RetailerSDKApp.getInstance().dbHelper.getString(
                        R.string.remaining_qty
                    ) + " "
                viewHolder.mBinding.tvItemLeftText.text =
                    RetailerSDKApp.getInstance().dbHelper.getString(
                        R.string.item_left
                    )
                viewHolder.mBinding.tvEndInText.text =
                    RetailerSDKApp.getInstance().dbHelper.getString(
                        R.string.end_in_text
                    )
                viewHolder.mBinding.tvItemExpair.text =
                    RetailerSDKApp.getInstance().dbHelper.getString(
                        R.string.text_free_item_is_out_of_stock
                    )

                // if item inactive this case is working
                if (!cartItemDetailsList.isSuccess) {
                    if (!TextUtils.isNullOrEmpty(cartItemDetailsList.messageKey)) {
                        if (cartItemDetailsList.messageKey == "FreeItemExpired") {
                            viewHolder.mBinding.tvItemExpair.visibility = View.VISIBLE
                            viewHolder.mBinding.viewDivider.visibility = View.VISIBLE
                        }
                        removeInactivteItem(viewHolder, cartItemDetailsList)
                        if (cartItemDetailsList.messageKey == "UnitPriceChange") {
                            viewHolder.ivNewPrice.visibility = View.VISIBLE
                            viewHolder.tvOldPrice.visibility = View.VISIBLE
                            val sOLDPrice =
                                " | <font color=#000000>&#8377;" + DecimalFormat("##.##").format(
                                    cartItemDetailsList.cartUnitPrice!!.toDouble()
                                ) + "</font>"
                            viewHolder.tvOldPrice.text = Html.fromHtml(sOLDPrice)
                            viewHolder.tvOldPrice.paintFlags =
                                viewHolder.tvOldPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                        } else if (cartItemDetailsList.messageKey == "MinOrderQty") {
                            viewHolder.mBinding.tvItemExpair.visibility = View.VISIBLE
                            viewHolder.mBinding.viewDivider.visibility = View.VISIBLE
                            viewHolder.mBinding.tvItemExpair.text = cartItemDetailsList.message
                        } else if (cartItemDetailsList.messageKey == "SupplierNotEligible") {
                            viewHolder.mBinding.tvItemExpair.visibility = View.VISIBLE
                            viewHolder.mBinding.viewDivider.visibility = View.VISIBLE
                            viewHolder.mBinding.tvItemExpair.text = cartItemDetailsList.message
                        } else {
                            viewHolder.ivNewPrice.visibility = View.GONE
                            viewHolder.tvOldPrice.visibility = View.GONE
                        }
                    }
                } else {
                    viewHolder.llItemBox.visibility = View.VISIBLE
                    viewHolder.mainOfferViewLL.visibility = View.VISIBLE
                    viewHolder.llItemOos.visibility = View.GONE
                    viewHolder.ivNewPrice.visibility = View.GONE
                    viewHolder.tvOldPrice.visibility = View.GONE
                    viewHolder.mBinding.tvItemExpair.visibility = View.GONE
                    viewHolder.mBinding.viewDivider.visibility = View.GONE
                }
                viewHolder.mBinding.llRemoveItem.setOnClickListener { v: View? ->
                    removeItemInterface.RemoveItemClicked(
                        cartItemDetailsList.itemId
                    )
                }
                viewHolder.tvItemName.text = cartItemDetailsList.itemname
                // check item in wishList
                if (RetailerSDKApp.getInstance().noteRepository.isItemWishList(cartItemDetailsList.itemId)) {
                    viewHolder.mBinding.favItem.setImageResource(R.drawable.ic_favorite_red)
                } else {
                    viewHolder.mBinding.favItem.setImageResource(R.drawable.ic_favourite)
                }

                //set offer UI
                if (cartItemDetailsList.isOffer) {
                    viewHolder.mBinding.llMainOfferView.visibility = View.VISIBLE
                    viewHolder.mBinding.llFlashOfferView.visibility = View.GONE
                    viewHolder.mBinding.favItem.visibility = View.GONE
                    viewHolder.mBinding.tvFlashdealPrice.visibility = View.GONE
                    val spItemName = cartItemDetailsList.itemname
                    val freeOfferTextBuy =
                        "<font color=#fe4e4e>Buy&nbsp;" + cartItemDetailsList.offerMinimumQty + "&nbsp;pcs&nbsp;</font>" + "<font color=#000000>&nbsp;" + spItemName + " </font>"
                    var freeOfferTextGet = ""
                    viewHolder.mBinding.tvBuyValue.text = Html.fromHtml(freeOfferTextBuy)
                    when (cartItemDetailsList.offerType) {
                        "WalletPoint" -> {
                            viewHolder.mBinding.tvFreeItemQut.text = "0"
                            viewHolder.freeItemIV.setBackgroundResource(R.drawable.ic_gift_bg)
                            viewHolder.freeDepePointTV.text =
                                RetailerSDKApp.getInstance().dbHelper.getString(
                                    R.string.Dp
                                )
                            val sfreewalletDP =
                                DecimalFormat("##.##").format(cartItemDetailsList.offerWalletPoint)
                            freeOfferTextGet =
                                ("<font color=#fe4e4e>Get&nbsp;" + sfreewalletDP + "&nbsp;Free</font>"
                                        + "<font color=#000000>&nbsp;Dream Points</font>")
                        }

                        "ItemMaster" -> {
                            if (!TextUtils.isNullOrEmpty(cartItemDetailsList.offerFreeItemImage)) {
                                Picasso.get().load(cartItemDetailsList.offerFreeItemImage)
                                    .placeholder(R.drawable.logo_grey)
                                    .error(R.drawable.logo_grey)
                                    .into(viewHolder.freeItemIV)
                            } else {
                                viewHolder.freeItemIV.setImageResource(R.drawable.logo_grey)
                            }
                            viewHolder.mBinding.tvFreeItemQut.text = "0"
                            viewHolder.freeDepePointTV.text =
                                RetailerSDKApp.getInstance().dbHelper.getString(
                                    R.string.free
                                )
                            freeOfferTextGet =
                                "<font color=#fe4e4e>Get " + cartItemDetailsList.offerFreeItemQuantity + "&nbsp;Free </font>" + "<font color=#000000>&nbsp;" + cartItemDetailsList.offerFreeItemName + " </font>"
                        }

                        "FlashDeal" -> {
                            var isflashDealUsed = false
                            val jsonFlashString = SharePrefs.getStringSharedPreferences(
                                activity, SharePrefs.ITEM_FLASH_DEAL_USED_JSON
                            )
                            try {
                                if (!jsonFlashString.isEmpty()) {
                                    val jsonFlashUsed = JSONObject(jsonFlashString)
                                    if (jsonFlashUsed.has(cartItemDetailsList.itemId.toString())) {
                                        if (jsonFlashUsed[cartItemDetailsList.itemId.toString()] == "1") {
                                            isflashDealUsed = true
                                        }
                                    }
                                }
                            } catch (e: JSONException) {
                                e.printStackTrace()
                            }
                            if (!isflashDealUsed) {
                                viewHolder.leftItemsTV.text =
                                    cartItemDetailsList.offerQtyAvaiable.toString()
                                viewHolder.mainOfferViewLL.visibility = View.GONE
                                viewHolder.flashOfferViewLL.visibility = View.VISIBLE
                                viewHolder.tvFlashdealPrice.visibility = View.VISIBLE
                                flashofferFlag = true
                                //end time
                                //  timeDeference(itemListArrayList, viewHolder);
                                if (cartItemDetailsList.flashDealSpecialPrice != 0.0) {
                                    viewHolder.tvPrice.paintFlags =
                                        viewHolder.tvMrp.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                                    viewHolder.tvFlashdealPrice.text =
                                        " | " + DecimalFormat("##.##").format(cartItemDetailsList.flashDealSpecialPrice)
                                }
                                val currMillis = System.currentTimeMillis()
                                val sdf1 = SimpleDateFormat(Utils.myFormat, Locale.getDefault())
                                sdf1.timeZone = TimeZone.getDefault()
                                val endTime = sdf1.parse(cartItemDetailsList.offerEndTime)
                                val millse = endTime.time - currMillis
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
                    viewHolder.GetValueTextView.text = Html.fromHtml(freeOfferTextGet)
                } else {
                    viewHolder.mBinding.llMainOfferView.visibility = View.GONE
                    viewHolder.mBinding.llFlashOfferView.visibility = View.GONE
                    viewHolder.mBinding.favItem.visibility = View.VISIBLE
                    viewHolder.mBinding.tvFlashdealPrice.visibility = View.GONE
                    viewHolder.mBinding.tvPrice.paintFlags = 0
                    flashofferFlag = false
                }

                //set item images
                if (!TextUtils.isNullOrEmpty(cartItemDetailsList.logoUrl)) {
                    Picasso.get().load(cartItemDetailsList.logoUrl)
                        .placeholder(R.drawable.logo_grey)
                        .into(viewHolder.mBinding.productImage)
                } else {
                    viewHolder.mBinding.productImage.setImageResource(R.drawable.logo_grey)
                }
                val sMoq =
                    RetailerSDKApp.getInstance().dbHelper.getString(R.string.item_moq) + " " + cartItemDetailsList.minOrderQty
                val sMRP = DecimalFormat("##.##").format(cartItemDetailsList.price)
                val sPRICE = " | " + DecimalFormat("##.##").format(cartItemDetailsList.unitPrice)
                val sMargin =
                    RetailerSDKApp.getInstance().dbHelper.getString(R.string.moq_margin) + " " + DecimalFormat(
                        "##.##"
                    ).format(java.lang.Double.valueOf(cartItemDetailsList.marginPoint)) + "%"

                //set values
                viewHolder.tvItemName.text = cartItemDetailsList.itemname
                viewHolder.mBinding.tvMrp.text = sMRP
                viewHolder.mBinding.tvMrp.paintFlags =
                    viewHolder.tvMrp.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                if (cartItemDetailsList.isPrimeItem) {
                    viewHolder.mBinding.tvPrice.setTextColor(
                        activity.resources.getColor(R.color.prime)
                    )
                    viewHolder.mBinding.tvPrime.visibility = View.VISIBLE
                    viewHolder.mBinding.tvPrime.text = SharePrefs.getInstance(
                        activity
                    ).getString(SharePrefs.PRIME_NAME)
                    viewHolder.mBinding.tvPrice.text =
                        " | " + DecimalFormat("##.##").format(cartItemDetailsList.primePrice)
                } else {
                    viewHolder.mBinding.tvPrice.setTextColor(
                        activity.resources.getColor(R.color.colorAccent)
                    )
                    viewHolder.mBinding.tvPrime.visibility = View.GONE
                    viewHolder.mBinding.tvPrice.text = sPRICE
                }
                viewHolder.mBinding.tvMoq.text = Html.fromHtml(sMoq)
                viewHolder.mBinding.tvMargin.text = Html.fromHtml(sMargin)
                viewHolder.mBinding.tvDreamPoint.text =
                    RetailerSDKApp.getInstance().dbHelper.getString(
                        R.string.Dp
                    ) + " " + cartItemDetailsList.dreamPoint
                if (!TextUtils.isNullOrEmpty(cartItemDetailsList.scheme)) {
                    viewHolder.mBinding.tvSchemeText.visibility = View.VISIBLE
                    viewHolder.mBinding.tvSchemeText.text = "" + cartItemDetailsList.scheme
                } else {
                    viewHolder.mBinding.tvSchemeText.visibility = View.GONE
                }

                //set UI for ItemLimit
                if (cartItemDetailsList.isItemLimit) {
                    var totalItemInCart = 0
                    var itemlimitQuantity = 0
                    var totalAvailQty = 0
                    viewHolder.mBinding.availQtyLayout.visibility = View.VISIBLE
                    itemlimitQuantity = cartItemDetailsList.itemLimitQty
                    if (cartItemDetailsList.isDealItem) {
                        if (cartItemDetailsList.itemMultiMRPId == cartItemDetailsList.itemMultiMRPId) {
                            if (cartItemDetailsList.itemId == cartItemDetailsList.itemId) {
                                totalItemInCart += cartItemDetailsList.qty
                            }
                        }
                    } else {
                        for (itemMod in itemListArrayList!!) {
                            if (cartItemDetailsList.itemMultiMRPId == itemMod.itemMultiMRPId) {
                                totalItemInCart += itemMod.qty
                            }
                        }
                    }
                    totalAvailQty = itemlimitQuantity - totalItemInCart
                    viewHolder.mBinding.availQty.text = totalAvailQty.toString()
                } else {
                    viewHolder.mBinding.availQtyLayout.visibility = View.GONE
                }
                var isItemFound = true
                //new shopping cart code
                var itemQuantity = cartItemDetailsList.qty
                /*offer section start*/if (cartItemDetailsList.isOffer) {
                    if (cartItemDetailsList.offerType.equals("WalletPoint", ignoreCase = true)) {
                        val freeWalletPoint = cartItemDetailsList.totalFreeWalletPoint
                        val sfreewalletDP = DecimalFormat("##.##").format(freeWalletPoint)
                        if (freeWalletPoint > 0) {
                            viewHolder.mBinding.tvFreeItemQut.text = sfreewalletDP
                        }
                    } else if (cartItemDetailsList.offerType.equals(
                            "ItemMaster",
                            ignoreCase = true
                        )
                    ) {
                        val freeItemQuantity = cartItemDetailsList.totalFreeItemQty
                        if (freeItemQuantity > 0) {
                            viewHolder.mBinding.tvFreeItemQut.text = "" + freeItemQuantity
                        }
                    } else {
                        val remainingLeft = cartItemDetailsList.offerQtyAvaiable - itemQuantity
                        if (remainingLeft > 0) {
                            viewHolder.leftItemsTV.text = "" + remainingLeft
                        } else {
                            viewHolder.leftItemsTV.text = "0"
                        }
                    }
                }
                /*offer section end*/if (itemQuantity > 0) {
                    viewHolder.tvselectedItemQuantity.text = "" + itemQuantity
                    if (flashofferFlag) {
                        val price = "<font color=#FF4500>&#8377; " + DecimalFormat("##.##").format(
                            itemQuantity * cartItemDetailsList.flashDealSpecialPrice
                        )
                        viewHolder.tvSelectedItemPrice.text = Html.fromHtml(price)
                    } else if (SharePrefs.getInstance(activity)
                            .getBoolean(SharePrefs.IS_PRIME_MEMBER) && cartItemDetailsList.isPrimeItem
                    ) {
                        val price = "<font color=#FF4500>&#8377; " + DecimalFormat("##.##").format(
                            itemQuantity * cartItemDetailsList.primePrice
                        )
                        viewHolder.tvSelectedItemPrice.text = Html.fromHtml(price)
                    } else {
                        val price = "<font color=#FF4500>&#8377; " + DecimalFormat("##.##").format(
                            itemQuantity * cartItemDetailsList.unitPrice
                        )
                        viewHolder.tvSelectedItemPrice.text = Html.fromHtml(price)
                    }
                } else {
                    isItemFound = false
                }
                if (!isItemFound) {
                    itemQuantity = 0
                    viewHolder.mBinding.tvSelectedItemQuantity.text = "" + itemQuantity
                    if (SharePrefs.getInstance(activity)
                            .getBoolean(SharePrefs.IS_PRIME_MEMBER) && cartItemDetailsList.isPrimeItem
                    ) {
                        val price = "<font color=#FF4500>&#8377; " + DecimalFormat("##.##")
                            .format(itemQuantity * cartItemDetailsList.primePrice)
                        viewHolder.mBinding.tvSelectedItemPrice.text = Html.fromHtml(price)
                    } else {
                        val price = "<font color=#FF4500>&#8377; " + DecimalFormat("##.##")
                            .format(itemQuantity * cartItemDetailsList.unitPrice)
                        viewHolder.mBinding.tvSelectedItemPrice.text = Html.fromHtml(price)
                    }
                }
                viewHolder.ivMinusBtn.setOnClickListener { view: View? ->
                    itemAddRemove(
                        viewHolder, cartItemDetailsList, false, false
                    )
                }
                viewHolder.ivPlusBtn.setOnClickListener { view: View? ->
                    itemAddRemove(
                        viewHolder, cartItemDetailsList, true, false
                    )
                }
                viewHolder.favItem.setOnClickListener { v: View? ->
                    if (RetailerSDKApp.getInstance().noteRepository.isItemWishList(
                            cartItemDetailsList.itemId
                        )
                    ) {
                        viewHolder.mBinding.favItem.setImageResource(R.drawable.ic_favourite)
                        RetailerSDKApp.getInstance().noteRepository.deleteTask(cartItemDetailsList)
                        Utils.addFav(cartItemDetailsList.itemId, false, activity)
                    } else {
                        viewHolder.mBinding.favItem.setImageResource(R.drawable.ic_favorite_red)
                        RetailerSDKApp.getInstance().noteRepository.insertTask(cartItemDetailsList)
                        Utils.addFav(cartItemDetailsList.itemId, true, activity)
                        RetailerSDKApp.getInstance().analyticAddWishList(cartItemDetailsList)
                    }
                }
                viewHolder.mBinding.DelItem.setOnClickListener { view: View? ->
                    removeItemInterface.RemoveItemClicked(
                        cartItemDetailsList.itemId
                    )
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun getItemCount(): Int {
        val s = itemListArrayList!!.size /* > 50 ? 10 : itemListArrayList.size()*/
        return if (itemListArrayList == null) 0 else s
    }

    override fun getItemViewType(position: Int): Int {
        return if (itemListArrayList!![position].isSectionHeader) {
            HEADER
        } else {
            position
        }
    }

    override fun onItemClick(position: Int, itemAdded: Boolean) {
        if (!itemAdded) {
            notifyDataSetChanged()
        }
    }

    private fun itemAddRemove(
        viewHolder: ViewHolder,
        cartItemDetailsList: ItemListModel,
        addItem: Boolean,
        addBtn: Boolean
    ) {
        try {
            var FreeItemQuantity = 0
            var FreeWalletPoint = 0.0
            var addFlag = false
            var flashDealFlag = false
            var flashofferFlag = false
            var calUnitPrice = 0.0
            var isflashDealUsed = false
            var isPrimeItem = false
            var isCartRefress = false

            //get item limit
            var itemQuantity = viewHolder.tvselectedItemQuantity.text.toString().toInt()
            if (cartItemDetailsList.isOffer) {
                try {
                    if (cartItemDetailsList.offerType != null) {
                        if (cartItemDetailsList.offerType.equals("FlashDeal", ignoreCase = true)) {
                            val jsonFlashString = SharePrefs.getStringSharedPreferences(
                                activity, SharePrefs.ITEM_FLASH_DEAL_USED_JSON
                            )
                            if (!jsonFlashString.isEmpty()) {
                                val jsonObject = JSONObject(jsonFlashString)
                                if (jsonObject.has(cartItemDetailsList.itemId.toString())) {
                                    if (jsonObject[cartItemDetailsList.itemId.toString()] == "1") {
                                        isflashDealUsed = true
                                    }
                                }
                            }
                            if (cartItemDetailsList.flashDealMaxQtyPersonCanTake >= itemQuantity + cartItemDetailsList.minOrderQty && cartItemDetailsList.offerQtyAvaiable >= itemQuantity + cartItemDetailsList.minOrderQty) {
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
                //plus btn
                if (!flashDealFlag) {
                    itemQuantity += cartItemDetailsList.minOrderQty
                    //Item Limit code
                    if (cartItemDetailsList.isItemLimit) {
                        addFlag =
                            setLimit(viewHolder, cartItemDetailsList, addFlag, itemQuantity, true)
                    } else {
                        if (itemQuantity > 0) {
                            if (cartItemDetailsList.billLimitQty != 0) {
                                addFlag = setBillLimit(
                                    viewHolder,
                                    cartItemDetailsList,
                                    addFlag,
                                    itemQuantity
                                )
                            } else {
                                viewHolder.tvselectedItemQuantity.text = "" + itemQuantity
                                addFlag = true
                            }
                        }
                    }
                } else {
                    if (addBtn) {
                        Toast.makeText(
                            activity,
                            RetailerSDKApp.getInstance().dbHelper.getString(R.string.no_item_available),
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        if (cartItemDetailsList.offerQtyAvaiable <= itemQuantity + cartItemDetailsList.minOrderQty) {
                            Toast.makeText(
                                activity, RetailerSDKApp.getInstance().dbHelper.getString(
                                    R.string.no_item_available
                                ), Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            Toast.makeText(
                                activity,
                                RetailerSDKApp.getInstance().dbHelper.getString(
                                    R.string.only_add_maximum_item
                                ) + " " + cartItemDetailsList.flashDealMaxQtyPersonCanTake,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            } else {
                //minus btn
                if (cartItemDetailsList.isItemLimit) {
                    if (itemQuantity > 0) {
                        itemQuantity -= cartItemDetailsList.minOrderQty
                        addFlag =
                            setLimit(viewHolder, cartItemDetailsList, addFlag, itemQuantity, false)
                    }
                } else {
                    if (itemQuantity > 0) {
                        itemQuantity -= cartItemDetailsList.minOrderQty
                        viewHolder.tvselectedItemQuantity.text = "" + itemQuantity
                        addFlag = true
                    }
                }
            }
            if (addFlag) {
                /*offer section*/
                if (cartItemDetailsList.isOffer) {
                    val offerMinimumQty = cartItemDetailsList.offerMinimumQty
                    when (cartItemDetailsList.offerType) {
                        "WalletPoint" -> if (offerMinimumQty != 0) {
                            if (cartItemDetailsList.offerWalletPoint != null) {
                                //event trigger
                                if (itemQuantity >= offerMinimumQty) {
                                    FreeWalletPoint = cartItemDetailsList.offerWalletPoint!!
                                    val calfreeItemQty = itemQuantity / offerMinimumQty
                                    FreeWalletPoint *= calfreeItemQty.toDouble()
                                    val sfreewalletDP =
                                        DecimalFormat("##.##").format(FreeWalletPoint)
                                    if (FreeWalletPoint > 0) {
                                        viewHolder.mBinding.tvFreeItemQut.text = sfreewalletDP
                                    }
                                } else {
                                    viewHolder.mBinding.tvFreeItemQut.text = "0"
                                }
                            } else {
                                Toast.makeText(
                                    activity, RetailerSDKApp.getInstance().dbHelper.getString(
                                        R.string.mini_qty_should_not_be_zero
                                    ), Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                        "ItemMaster" -> if (offerMinimumQty != 0) {
                            if (cartItemDetailsList.offerFreeItemQuantity != null) {
                                //event trigger
                                if (itemQuantity >= offerMinimumQty) {
                                    FreeItemQuantity =
                                        cartItemDetailsList.offerFreeItemQuantity!!.toInt()
                                    val calfreeItemQty = itemQuantity / offerMinimumQty
                                    FreeItemQuantity *= calfreeItemQty
                                    if (FreeItemQuantity > 0) {
                                        viewHolder.mBinding.tvFreeItemQut.text =
                                            FreeItemQuantity.toString()
                                    }
                                    isCartRefress = true
                                } else {
                                    viewHolder.mBinding.tvFreeItemQut.text = "0"
                                }
                            }
                        } else {
                            Toast.makeText(
                                activity, RetailerSDKApp.getInstance().dbHelper.getString(
                                    R.string.mini_qty_should_not_be_zero
                                ), Toast.LENGTH_SHORT
                            ).show()
                        }

                        "FlashDeal" -> if (!isflashDealUsed) {
                            val remainingLeft = cartItemDetailsList.offerQtyAvaiable - itemQuantity
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
                        "<font color=#FF4500>&#8377; " + DecimalFormat("##.##").format(itemQuantity * cartItemDetailsList.flashDealSpecialPrice)
                    viewHolder.tvSelectedItemPrice.text = Html.fromHtml(price)
                    calUnitPrice = cartItemDetailsList.flashDealSpecialPrice
                } else if (SharePrefs.getInstance(activity)
                        .getBoolean(SharePrefs.IS_PRIME_MEMBER) && cartItemDetailsList.isPrimeItem
                ) {
                    val price = "<font color=#FF4500>&#8377; " + DecimalFormat("##.##").format(
                        itemQuantity * cartItemDetailsList.primePrice
                    )
                    viewHolder.tvSelectedItemPrice.text = Html.fromHtml(price)
                    calUnitPrice = cartItemDetailsList.primePrice
                    isPrimeItem = true
                } else {
                    val price =
                        "<font color=#FF4500>&#8377; " + DecimalFormat("##.##").format(itemQuantity * cartItemDetailsList.unitPrice)
                    viewHolder.tvSelectedItemPrice.text = Html.fromHtml(price)
                    calUnitPrice = cartItemDetailsList.unitPrice
                }
                activity.addItemInCartItemArrayList(
                    cartItemDetailsList.itemId,
                    itemQuantity,
                    calUnitPrice,
                    cartItemDetailsList,
                    FreeItemQuantity,
                    FreeWalletPoint,
                    isPrimeItem,
                    cartItemDetailsList.isDealItem,
                    this,
                    isCartRefress
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setLimit(
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
        if (add) {
            if (total + itemQuantity > itemlimitqty) {
                Utils.setToast(
                    activity, RetailerSDKApp.getInstance().dbHelper.getString(R.string.additemToast)
                            + " " + model.itemLimitQty + " " +
                            RetailerSDKApp.getInstance().dbHelper.getString(R.string.additemToast_2)
                )
            } else {
                if (itemQuantity > 0) {
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
            }
        } else {
            availqty = itemlimitqty - totalItemqty
            if (availqty >= 0) {
                viewHolder.mBinding.availQty.text = availqty.toString()
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
            val total = RetailerSDKApp.getInstance().noteRepository.getQtyByMultiMrp(
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

    private fun removeInactivteItem(viewHolder: ViewHolder, cartItemDetailsList: ItemListModel) {
        if (cartItemDetailsList.messageKey == "ItemNotActive") {
            viewHolder.llItemBox.visibility = View.GONE
            viewHolder.mainOfferViewLL.visibility = View.GONE
            viewHolder.llItemOos.visibility = View.VISIBLE
            viewHolder.tvOosItemName.text = cartItemDetailsList.itemname
        } else {
            viewHolder.llItemBox.visibility = View.VISIBLE
            viewHolder.mainOfferViewLL.visibility = View.VISIBLE
            viewHolder.llItemOos.visibility = View.GONE
        }
    }

    inner class HeaderViewHolder(var mBinding: ShoppingCartHeaderBinding) : RecyclerView.ViewHolder(
        mBinding.root
    )

    inner class ViewHolder(var mBinding: CheckoutItemsBinding) : RecyclerView.ViewHolder(mBinding.root) {
        val tvItemName: TextView
        val tvSelectedItemPrice: TextView
        val tvselectedItemQuantity: TextView
        val ivMinusBtn: ImageView
        val ivPlusBtn: ImageView
        val tvMrp: TextView
        val tvPrice: TextView
        val freeDepePointTV: TextView
        private val remaingSecTV: TextView
        val tvFlashdealPrice: TextView
        val mainOfferViewLL: LinearLayout
        val flashOfferViewLL: LinearLayout
        val llItemOos: LinearLayout
        val llItemBox: LinearLayout
        private val llRemoveItem: LinearLayout
        val GetValueTextView: TextView
        val leftItemsTV: TextView
        val tvOosItemName: TextView
        val tvOldPrice: TextView
        val freeItemIV: ImageView
        val ivNewPrice: ImageView
        val favItem: ImageView
        private val DelItem: ImageView
        init {
            tvselectedItemQuantity = mBinding.tvSelectedItemQuantity
            favItem = mBinding.favItem
            DelItem = mBinding.DelItem
            ivMinusBtn = mBinding.minusBtn
            ivPlusBtn = mBinding.plusBtn
            tvItemName = mBinding.tvItemName
            tvMrp = mBinding.tvMrp
            tvPrice = mBinding.tvPrice
            tvSelectedItemPrice = mBinding.tvSelectedItemPrice
            mainOfferViewLL = mBinding.llMainOfferView
            llItemBox = mBinding.llItemBox
            llItemOos = mBinding.llItemOos
            GetValueTextView = mBinding.tvGetValue
            leftItemsTV = mBinding.tvLeftItems
            freeItemIV = mBinding.ivFreeIteam
            freeDepePointTV = mBinding.tvFreeDepePoint
            flashOfferViewLL = mBinding.llFlashOfferView
            remaingSecTV = mBinding.tvRemaingSecand
            tvFlashdealPrice = mBinding.tvFlashdealPrice
            tvOldPrice = mBinding.tvOldPrice
            tvOosItemName = mBinding.tvOosItemName
            llRemoveItem = mBinding.llRemoveItem
            ivNewPrice = mBinding.ivNewPrice
            leftItemsTV.setBackgroundResource(R.drawable.ic_count_bg)
            remaingSecTV.setBackgroundResource(R.drawable.ic_count_bg)
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