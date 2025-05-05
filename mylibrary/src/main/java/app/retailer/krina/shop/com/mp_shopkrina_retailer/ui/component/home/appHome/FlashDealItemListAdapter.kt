package app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.appHome

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Paint
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Handler
import android.os.Looper
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.ItemListModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.FlashDealBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.interfaces.OnItemClick
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.SharePrefs
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.HomeActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.CustomRunnable
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.CustomRunnableNonPrime
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.CustomRunnablePrime
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Logger
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.RetailerSDKApp
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.TextUtils
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils
import com.bumptech.glide.Glide
import com.bumptech.glide.MemoryCategory
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.firebase.analytics.FirebaseAnalytics
import com.squareup.picasso.Picasso
import com.squareup.picasso.Picasso.LoadedFrom
import com.squareup.picasso.Target
import pl.droidsonroids.gif.GifImageView
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone
class FlashDealItemListAdapter(
    context: Context,
    private var itemListArrayList: List<ItemListModel>?,
    private var listSize: Int,
    private val flashDealBackImage: String?
) : RecyclerView.Adapter<FlashDealItemListAdapter.ViewHolder>(), OnItemClick {
    private val activity: HomeActivity
    val handler = Handler(Looper.getMainLooper())
    val mPrimehandler = Handler(Looper.getMainLooper())
    val mNonPrimehandler = Handler(Looper.getMainLooper())
    private var customRunnable: CustomRunnable? = null
    private var mPrimeCustomerCustomRunnable: CustomRunnablePrime? = null
    private var mNonPrimeCustomerCustomRunnable: CustomRunnableNonPrime? = null
    private var progressBar: GifImageView? = null

    init {
        activity = context as HomeActivity
    }

    fun setItemListCategory(itemListArrayList: ArrayList<ItemListModel>?, listSize: Int) {
        this.itemListArrayList = itemListArrayList
        this.listSize = listSize
        notifyDataSetChanged()
        Glide.get(activity).setMemoryCategory(MemoryCategory.HIGH)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        return ViewHolder(
            FlashDealBinding.inflate(
                LayoutInflater.from(viewGroup.context),
               viewGroup, false
            )
        )
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        try {
            //set String
            viewHolder.mBinding.tvMrpText.text =
                RetailerSDKApp.getInstance().dbHelper.getString(R.string.item_mrp)
            viewHolder.mBinding.tvItemLeftText.text =
                RetailerSDKApp.getInstance().dbHelper.getString(R.string.item_left)
            viewHolder.mBinding.tvFreeItemNotActiveText.text =
                RetailerSDKApp.getInstance().dbHelper.getString(R.string.inactive_customer_msg_flash)
            viewHolder.mBinding.addItemBtn.text =
                RetailerSDKApp.getInstance().dbHelper.getString(R.string.add_btn)
            viewHolder.mBinding.tvFlashDealEndTxt.text =
                RetailerSDKApp.getInstance().dbHelper.getString(R.string.flash_sale_end_in)
            viewHolder.mBinding.tvNoPrimeText.text =
                RetailerSDKApp.getInstance().dbHelper.getString(R.string.tv_non_prime_text_regular)
            viewHolder.mBinding.tvDealPriceText.text =
                RetailerSDKApp.getInstance().dbHelper.getString(R.string.deal_price)
            viewHolder.mBinding.tvFlashDealStartTxt.text =
                RetailerSDKApp.getInstance().dbHelper.getString(R.string.flash_sale_start_in)
            val vectorDrawable = AppCompatResources.getDrawable(activity, R.drawable.logo_grey)
            val listModel = itemListArrayList!![i]
            viewHolder.tvItemName.text = listModel.itemname
            viewHolder.tvMoq.text =
                RetailerSDKApp.getInstance().dbHelper.getString(R.string.item_moq) + " " + listModel.minOrderQty
            /*for inactive customer*/if (!SharePrefs.getInstance(activity)
                    .getBoolean(SharePrefs.CUST_ACTIVE)
            ) {
                viewHolder.mBinding.llInActiveUser.visibility = View.VISIBLE
                viewHolder.mBinding.addItemBtn.visibility = View.GONE
            }
            if (SharePrefs.getInstance(activity).getBoolean(SharePrefs.IS_PRIME_ACTIVE)) {
                viewHolder.mBinding.llPrimeTimer.visibility = View.VISIBLE
                viewHolder.mBinding.tvFlashDealStartTxt.visibility = View.GONE
                viewHolder.mBinding.tvPrimeText.text =
                    SharePrefs.getInstance(activity).getString(SharePrefs.PRIME_NAME)
            } else {
                viewHolder.mBinding.llPrimeTimer.visibility = View.GONE
                viewHolder.mBinding.tvFlashDealStartTxt.visibility = View.VISIBLE
            }
            // set offer UI
            if (!listModel.isFlashDealStart) {
                viewHolder.mBinding.addItemBtn.visibility = View.GONE
                viewHolder.mBinding.llTimer.visibility = View.VISIBLE
                viewHolder.mBinding.llRightBack.visibility = View.GONE
                // timer
                val sdf1 = SimpleDateFormat(Utils.myFormat, Locale.getDefault())
                sdf1.timeZone = TimeZone.getDefault()
                if (!TextUtils.isNullOrEmpty(listModel.offerStartTime)) {
                    val primeTime = sdf1.parse(listModel.offerStartTime)
                    val primeCurrentTime = sdf1.parse(listModel.currentStartTime)
                    val primeEpoch = primeTime.time
                    val primeCurrentEpoch = primeCurrentTime.time
                    val primeMillse = primeEpoch - primeCurrentEpoch
                    if (primeMillse < 0) {
                        viewHolder.mBinding.tvPrimeUserTime.text = "Started"
                    } else {
                        viewHolder.primeUserTimer(primeMillse, listModel.itemId)
                    }
                }
                if (!TextUtils.isNullOrEmpty(listModel.noPrimeOfferStartTime)) {
                    val noPrimeTime = sdf1.parse(listModel.noPrimeOfferStartTime)
                    val primeCurrentTime = sdf1.parse(listModel.currentStartTime)
                    val noPrimeEpoch = noPrimeTime.time
                    val noPrimeCurrentEpoch = primeCurrentTime.time
                    val noPrimeMillse = noPrimeEpoch - noPrimeCurrentEpoch
                    viewHolder.nonPrimeUserTime(noPrimeMillse, listModel.itemId)
                }
            } else {
                viewHolder.mBinding.llTimer.visibility = View.GONE
                viewHolder.mBinding.addItemBtn.visibility = View.VISIBLE
                viewHolder.mBinding.llRightBack.visibility = View.VISIBLE
                if (listModel.isOffer) {
                    //timer
                    //  long currMillis = System.currentTimeMillis();
                    val sdf1 = SimpleDateFormat(Utils.myFormat, Locale.getDefault())
                    sdf1.timeZone = TimeZone.getDefault()
                    val currentTime = sdf1.parse(listModel.currentStartTime)
                    val currentEpoch = currentTime.time
                    val endTime = sdf1.parse(listModel.offerEndTime)
                    val endEpoch = endTime.time
                    val millse = endEpoch - currentEpoch
                    viewHolder.timerExpire(millse, listModel.itemId)
                }
            }
            viewHolder.leftItemsTV.text = listModel.offerQtyAvaiable.toString()
            //blink
            val anim: Animation = AlphaAnimation(0.0f, 1.0f)
            anim.duration = 500 //You can manage the blinking time with this parameter
            anim.startOffset = 20
            anim.repeatMode = Animation.REVERSE
            anim.repeatCount = Animation.INFINITE
            viewHolder.leftItemsTV.startAnimation(anim)
            if (!TextUtils.isNullOrEmpty(
                    itemListArrayList!![i].logoUrl
                )
            ) {
                Glide.with(activity).load(itemListArrayList!![i].logoUrl)
                    .diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(vectorDrawable)
                    .into(viewHolder.ivItemImage)
            } else {
                viewHolder.ivItemImage.setImageDrawable(vectorDrawable)
            }
            val sPRICE =
                "| <font color=#FF4500>&#8377; " + DecimalFormat("##.##").format(listModel.unitPrice) + "</font>"
            val sMargin =
                "| " + RetailerSDKApp.getInstance().dbHelper.getString(R.string.moq_margin) + " " + DecimalFormat(
                    "##.##"
                ).format(listModel.marginPoint!!.toDouble()) + "%"
            val sMRP = DecimalFormat("##.##").format(listModel.price)
            if (listModel.flashDealSpecialPrice != 0.0) {
                val specialPRICE =
                    "<font color=#000000>&#8377; " + DecimalFormat("##.##").format(listModel.flashDealSpecialPrice) + "</font>"
                viewHolder.tvItemSpecialPrice.text = Html.fromHtml(specialPRICE)
            }
            // set values
            viewHolder.tvMrp.text = sMRP
            viewHolder.tvMrp.paintFlags = viewHolder.tvMrp.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            viewHolder.tvPrice.text = Html.fromHtml(sPRICE)
            viewHolder.tvMargin.text = sMargin
            viewHolder.tvDreamPoint.text =
                RetailerSDKApp.getInstance().dbHelper.getString(R.string.Dp) + " " + listModel.dreamPoint
            val cartModel =
                RetailerSDKApp.getInstance().noteRepository.getCartItem1(listModel.itemId)
            var isItemFound: Boolean
            if (cartModel != null) {
                isItemFound = true
                val itemQuantity = cartModel.qty
                if (itemQuantity > 0) {
                    viewHolder.tvselectedItemQuantity.text = "" + itemQuantity
                    val remainingLeft = listModel.offerQtyAvaiable - itemQuantity
                    if (!listModel.isFlashDealUsed) {
                        if (remainingLeft > 0) {
                            viewHolder.leftItemsTV.text = "" + remainingLeft
                        } else {
                            viewHolder.leftItemsTV.text = "0"
                        }
                        val price = "<font color=#FF4500>&#8377; " + DecimalFormat("##.##").format(
                            itemQuantity * listModel.flashDealSpecialPrice
                        )
                        viewHolder.tvSelectedItemPrice.text = Html.fromHtml(price)
                    } else {
                        viewHolder.leftItemsTV.text = "" + listModel.offerQtyAvaiable
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
                Logger.logD("Adapter", "ItemQ3::$itemQuantity")
                if (listModel.flashDealSpecialPrice != 0.0 && !listModel.isFlashDealUsed) {
                    val price =
                        "<font color=#FF4500>&#8377; " + DecimalFormat("##.##").format(itemQuantity * listModel.flashDealSpecialPrice)
                    viewHolder.tvSelectedItemPrice.text = Html.fromHtml(price)
                }
                if (!listModel.isFlashDealStart) {
                    viewHolder.mBinding.addItemBtn.visibility = View.GONE
                    viewHolder.mBinding.visible.visibility = View.GONE
                } else if (!SharePrefs.getInstance(activity).getBoolean(SharePrefs.CUST_ACTIVE)) {
                    viewHolder.mBinding.addItemBtn.visibility = View.GONE
                    viewHolder.mBinding.visible.visibility = View.GONE
                } else {
                    viewHolder.mBinding.visible.visibility = View.GONE
                    viewHolder.mBinding.addItemBtn.visibility = View.VISIBLE
                }
            } else {
                if (!listModel.isFlashDealUsed) {
                    if (!listModel.isFlashDealStart) {
                        viewHolder.mBinding.addItemBtn.visibility = View.GONE
                        viewHolder.mBinding.visible.visibility = View.GONE
                    } else if (!SharePrefs.getInstance(activity)
                            .getBoolean(SharePrefs.CUST_ACTIVE)
                    ) {
                        viewHolder.mBinding.addItemBtn.visibility = View.GONE
                        viewHolder.mBinding.visible.visibility = View.GONE
                    } else {
                        viewHolder.mBinding.addItemBtn.visibility = View.GONE
                        viewHolder.mBinding.visible.visibility = View.VISIBLE
                    }
                }
            }
            listModel.isChecked = true
            // Minis Btn clicked
            viewHolder.mBinding.minusBtn.setOnClickListener { view: View? ->
                // progress bar
                progressBar = viewHolder.mBinding.ivProgress
                if (progressBar != null) progressBar!!.visibility = View.INVISIBLE
                Utils.buttonEffect(viewHolder.mBinding.minusBtn)
                var itemQuantity = viewHolder.tvselectedItemQuantity.text.toString().toInt()
                val FreeItemQuantity = 0
                val FreeWalletPoint = 0.0
                if (itemQuantity > 0) {
                    itemQuantity -= listModel.minOrderQty
                    viewHolder.tvselectedItemQuantity.text = "" + itemQuantity
                    val remainingLeft = listModel.offerQtyAvaiable - itemQuantity
                    if (remainingLeft > 0) {
                        viewHolder.leftItemsTV.text = "" + remainingLeft
                    } else {
                        viewHolder.leftItemsTV.text = "0"
                    }
                    val price =
                        "<font color=#FF4500>&#8377; " + DecimalFormat("##.##").format(itemQuantity * listModel.flashDealSpecialPrice)
                    viewHolder.tvSelectedItemPrice.text = Html.fromHtml(price)
                    progressBar!!.visibility = View.VISIBLE
                    activity.addItemInCartItemArrayList(
                        listModel.itemId, itemQuantity,
                        listModel.flashDealSpecialPrice, listModel,
                        FreeItemQuantity, FreeWalletPoint, false, this
                    )
                } else {
                    // analytics for remove from cart
                    RetailerSDKApp.getInstance()
                        .updateAnalyticsCart(FirebaseAnalytics.Event.REMOVE_FROM_CART, listModel)
                }
            }
            //plus Btn clicked
            viewHolder.mBinding.plusBtn.setOnClickListener { view: View? ->
                // progress bar
                progressBar = viewHolder.mBinding.ivProgress
                if (progressBar != null) progressBar!!.visibility = View.INVISIBLE
                Utils.buttonEffect(viewHolder.mBinding.plusBtn)
                val FreeItemQuantity = 0
                val FreeWalletPoint = 0.0
                var itemQuantity = 0
                itemQuantity = viewHolder.tvselectedItemQuantity.text.toString().toInt()
                itemQuantity += listModel.minOrderQty
                if (listModel.flashDealMaxQtyPersonCanTake >= itemQuantity && listModel.offerQtyAvaiable >= itemQuantity) {
                    if (itemQuantity > 0) {
                        if (listModel.billLimitQty != 0) {
                            if (listModel.billLimitQty >= itemQuantity) {
                                viewHolder.tvselectedItemQuantity.text = "" + itemQuantity
                                val remainingLeft = listModel.offerQtyAvaiable - itemQuantity
                                if (remainingLeft > 0) {
                                    viewHolder.leftItemsTV.text = "" + remainingLeft
                                } else {
                                    viewHolder.leftItemsTV.text = "0"
                                }
                            } else {
                                Toast.makeText(
                                    activity,
                                    RetailerSDKApp.getInstance().dbHelper.getString(R.string.you_can_not_add_more_than) + " " + listModel.billLimitQty + " " + RetailerSDKApp.getInstance().dbHelper.getString(
                                        R.string.item_t
                                    ),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } else {
                            viewHolder.tvselectedItemQuantity.text = "" + itemQuantity
                            val remainingLeft = listModel.offerQtyAvaiable - itemQuantity
                            if (remainingLeft > 0) {
                                viewHolder.leftItemsTV.text = "" + remainingLeft
                            } else {
                                viewHolder.leftItemsTV.text = "0"
                            }
                        }
                        val price = "<font color=#FF4500>&#8377; " +
                                DecimalFormat("##.##").format(
                                    itemQuantity
                                            * listModel.flashDealSpecialPrice
                                )
                        viewHolder.tvSelectedItemPrice.text = Html.fromHtml(price)
                        // progress bar
                        progressBar!!.visibility = View.VISIBLE
                        activity.addItemInCartItemArrayList(
                            listModel.itemId, itemQuantity,
                            listModel.flashDealSpecialPrice, listModel,
                            FreeItemQuantity, FreeWalletPoint, false, this
                        )
                    }
                } else {
                    if (listModel.offerQtyAvaiable <= itemQuantity) {
                        Toast.makeText(
                            activity,
                            RetailerSDKApp.getInstance().dbHelper.getString(R.string.no_item_available),
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            activity,
                            RetailerSDKApp.getInstance().dbHelper.getString(R.string.only_add_maximum_item) + " " + listModel.flashDealMaxQtyPersonCanTake,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
            // Add Btn clicked
            viewHolder.mBinding.addItemBtn.setOnClickListener { v: View? ->
                var itemQuantity: Int
                if (!listModel.isFlashDealUsed) {
                    viewHolder.mBinding.visible.visibility = View.VISIBLE
                    viewHolder.mBinding.addItemBtn.visibility = View.GONE
                    itemQuantity = viewHolder.tvselectedItemQuantity.text.toString().toInt()
                    itemQuantity += listModel.minOrderQty
                    if (listModel.flashDealMaxQtyPersonCanTake >= itemQuantity && listModel.offerQtyAvaiable >= itemQuantity) {
                        if (itemQuantity > 0) {
                            viewHolder.tvselectedItemQuantity.text = "" + itemQuantity
                            val remainingLeft = listModel.offerQtyAvaiable - itemQuantity
                            if (remainingLeft > 0) {
                                viewHolder.leftItemsTV.text = "" + remainingLeft
                            } else {
                                viewHolder.leftItemsTV.text = "0"
                            }
                            val price =
                                "<font color=#FF4500>&#8377; " + DecimalFormat("##.##").format(
                                    itemQuantity * listModel.flashDealSpecialPrice
                                )
                            viewHolder.tvSelectedItemPrice.text = Html.fromHtml(price)
                            // progress bar
                            progressBar = viewHolder.mBinding.ivProgress
                            progressBar!!.visibility = View.VISIBLE
                            activity.addItemInCartItemArrayList(
                                listModel.itemId, itemQuantity,
                                listModel.flashDealSpecialPrice, listModel, 0,
                                0.0, false, this
                            )
                            // analytics for add to cart
                            RetailerSDKApp.getInstance()
                                .updateAnalyticsCart(FirebaseAnalytics.Event.ADD_TO_CART, listModel)
                        }
                    } else {
                        Toast.makeText(
                            activity,
                            RetailerSDKApp.getInstance().dbHelper.getString(R.string.qty_is_not_available),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        activity,
                        RetailerSDKApp.getInstance().dbHelper.getString(R.string.you_already_taken_flash_deal),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            if (!TextUtils.isNullOrEmpty(
                    flashDealBackImage
                )
            ) {
                Picasso.get().load(flashDealBackImage!!.replace(" ".toRegex(), "%20"))
                    .into(object : Target {
                        override fun onBitmapLoaded(bitmap: Bitmap, from: LoadedFrom) {
                            viewHolder.mBinding.llFullBack.background = BitmapDrawable(bitmap)
                        }

                        override fun onBitmapFailed(e: Exception, errorDrawable: Drawable) {
                            viewHolder.mBinding.llFullBack.setBackgroundResource(R.drawable.background_flass_offer)
                        }

                        override fun onPrepareLoad(placeHolderDrawable: Drawable) {}
                    })
            } else {
                viewHolder.mBinding.llFullBack.setBackgroundResource(R.drawable.background_flass_offer)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun getItemCount(): Int {
        return if (itemListArrayList == null) 0 else listSize
    }

    override fun onItemClick(position: Int, itemAdded: Boolean) {
        progressBar!!.visibility = View.INVISIBLE
        if (!itemAdded) notifyDataSetChanged()
    }

    inner class ViewHolder(var mBinding: FlashDealBinding) : RecyclerView.ViewHolder(
        mBinding.root
    ) {
        val ivItemImage: ImageView
        val tvItemName: TextView
        val tvSelectedItemPrice: TextView
        val tvselectedItemQuantity: TextView
        val tvMrp: TextView
        val tvPrice: TextView
        val tvMoq: TextView
        val tvMargin: TextView
        val tvDreamPoint: TextView
        val leftItemsTV: TextView
        val tvItemSpecialPrice: TextView
        private val remaingSecTV: TextView

        init {
            tvSelectedItemPrice = mBinding.tvSelectedItemPrice
            tvselectedItemQuantity = mBinding.tvSelectedItemQuantity
            ivItemImage = mBinding.productImage
            tvItemName = mBinding.tvItemName
            tvMrp = mBinding.tvMrp
            tvPrice = mBinding.tvPrice
            tvMoq = mBinding.tvMoq
            tvMargin = mBinding.tvMargin
            tvDreamPoint = mBinding.tvDreamPoint
            leftItemsTV = mBinding.tvLeftItems
            tvItemSpecialPrice = mBinding.tvItemSpecialPrice
            remaingSecTV = mBinding.tvRemaingSecand
            mBinding.llLeftBack.setBackgroundResource(R.drawable.ic_item_left_flash)
            mBinding.llRightBack.setBackgroundResource(R.drawable.ic_end_in_flash)
            mBinding.llTimer.setBackgroundResource(R.drawable.ic_end_in_flash)
            mBinding.llBottomCorner.setBackgroundResource(R.color.white1)
            customRunnable = CustomRunnable(handler, remaingSecTV, 10000, 0)
            mPrimeCustomerCustomRunnable = CustomRunnablePrime(
                mPrimehandler,
                mBinding.tvPrimeUserTime,
                10000,
                0,
                SharePrefs.getInstance(activity).getBoolean(SharePrefs.IS_PRIME_MEMBER)
            )
            mNonPrimeCustomerCustomRunnable = CustomRunnableNonPrime(
                mNonPrimehandler,
                mBinding.tvNonPrimeUserTime,
                10000,
                0,
                SharePrefs.getInstance(activity).getBoolean(SharePrefs.IS_PRIME_MEMBER)
            )
        }

        fun timerExpire(endTime: Long, itemId: Int) {
            handler.removeCallbacks(customRunnable!!)
            customRunnable!!.holder = remaingSecTV
            customRunnable!!.millisUntilFinished = endTime //Current time - received time
            customRunnable!!.itemId = itemId //ItemId
            handler.postDelayed(customRunnable!!, 1000)
        }

        fun primeUserTimer(time: Long, itemId: Int) {
            mPrimehandler.removeCallbacks(mPrimeCustomerCustomRunnable!!)
            mPrimeCustomerCustomRunnable!!.holder = mBinding.tvPrimeUserTime
            mPrimeCustomerCustomRunnable!!.millisUntilFinished = time //Current time - received time
            mPrimeCustomerCustomRunnable!!.itemId = itemId //ItemId
            mPrimehandler.postDelayed(mPrimeCustomerCustomRunnable!!, 1000)
        }

        fun nonPrimeUserTime(time: Long, itemId: Int) {
            mNonPrimehandler.removeCallbacks(mNonPrimeCustomerCustomRunnable!!)
            mNonPrimeCustomerCustomRunnable!!.holder = mBinding.tvNonPrimeUserTime
            mNonPrimeCustomerCustomRunnable!!.millisUntilFinished =
                time //Current time - received time
            mNonPrimeCustomerCustomRunnable!!.itemId = itemId //ItemId
            mNonPrimehandler.postDelayed(mNonPrimeCustomerCustomRunnable!!, 1000)
        }
    }
}