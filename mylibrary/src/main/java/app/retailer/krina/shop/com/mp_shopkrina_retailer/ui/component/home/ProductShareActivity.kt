package app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home

import android.Manifest
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.text.Html
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import app.retailer.krina.shop.com.mp_shopkrina_retailer.BuildConfig
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.community.FeedActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.api.CommonClassForAPI
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.ItemListModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ActivityProductDetailBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.activity.MembershipPlanActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.shoppingCart.ShoppingCartActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.direct.TradeActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.CartAddItemModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.postModels.ItemIdPostModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.EndPointPref
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.SharePrefs
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.splash.SplashScreenActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Constant
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.LocaleHelper
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.MyApplication
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.RxBus
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.TextUtils
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.gson.JsonObject
import io.reactivex.observers.DisposableObserver
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.text.DecimalFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.Objects
import java.util.Timer
import java.util.TimerTask

class ProductShareActivity : AppCompatActivity(), View.OnClickListener {
    private var mBinding: ActivityProductDetailBinding? = null
    var vectorDrawable: Drawable? = null
    private var bitmapx: Bitmap? = null
    private var itemListModel: ItemListModel? = null
    private var ivProductImage: ImageView? = null
    private var ivProductName: TextView? = null
    private var tvProductPrice: TextView? = null
    private var tvItemRowQuantityTv: TextView? = null
    private var tvMoQTV: TextView? = null
    private var TvMrpTV: TextView? = null
    private var tvMargin: TextView? = null
    private var tvDreemPointTV: TextView? = null
    var flashofferFlag = false
    private var freeItemQutTV: TextView? = null
    private var freeDepePointTV: TextView? = null
    private var buyValueText: TextView? = null
    private var GetValueTextView: TextView? = null
    private var leftItemsTV: TextView? = null
    private var remaingSecTV: TextView? = null
    private var freeItemIV: ImageView? = null
    private var mainOfferViewLL: LinearLayoutCompat? = null
    private var flashOfferViewLL: LinearLayoutCompat? = null
    private var utils: Utils? = null
    private var commonClassForAPI: CommonClassForAPI? = null
    private var custId = 0
    private var wId = 0
    private val status = "Error"
    private var lang = ""
    private var urlNumber: String? = null
    private var urlItemMultiMRPId: String? = null
    private var urlwarehouseId: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_product_detail)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        title = MyApplication.getInstance().dbHelper.getString(R.string.txt_product_deatils)
        vectorDrawable = AppCompatResources.getDrawable(this, R.drawable.logo_grey)
        //init view
        initialization()

        //get uri data
        try {
            if (intent.extras != null) {
                urlNumber = intent.getStringExtra("number")
                urlItemMultiMRPId = intent.getStringExtra("multiMRPId")
                urlwarehouseId = intent.getStringExtra("warehouseId")
                if (urlNumber == null || urlItemMultiMRPId == null) {
                    val data = intent.data
                    fetchParameters(data)
                }
                if (intent.hasExtra("notificationId")) {
                    val notificationId = intent.extras!!.getInt("notificationId")
                    MyApplication.getInstance().notificationView(notificationId)
                    intent.extras!!.clear()
                }
            } else {
                val data = intent.data
                fetchParameters(data)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            onBackPressed()
        }
        //check for login
        if (MyApplication.getInstance().prefManager.isLoggedIn) {
            if (utils!!.isNetworkAvailable) {
                if (intent.data != null && intent.data.toString().contains("trade")) {
                    if (EndPointPref.getInstance(applicationContext)
                            .getBoolean(EndPointPref.showNewSocial)
                    ) startActivity(
                        Intent(
                            applicationContext, FeedActivity::class.java
                        )
                    ) else startActivity(
                        Intent(
                            applicationContext, TradeActivity::class.java
                        )
                            .setData(intent.data)
                    )
                    finish()
                } else {
                    urlwarehouseId = if (urlwarehouseId == null) "" +
                            SharePrefs.getInstance(applicationContext)
                                .getInt(SharePrefs.WAREHOUSE_ID) else urlwarehouseId
                    if (urlItemMultiMRPId != null) {
                        Utils.showProgressDialog(this)
                        commonClassForAPI!!.fetchProductDetails(
                            productDetailsDes,
                            ItemIdPostModel(
                                urlNumber,
                                urlItemMultiMRPId!!.toInt(),
                                urlwarehouseId!!.toInt()
                            )
                        )
                    } else {
                        onBackPressed()
                    }
                }
            } else {
                Utils.setToast(
                    applicationContext,
                    MyApplication.getInstance().dbHelper.getString(R.string.internet_connection)
                )
            }
        } else {
            Utils.setToast(
                applicationContext, "Please login first"
            )
            startActivity(Intent(applicationContext, SplashScreenActivity::class.java))
            finish()
        }
        leftItemsTV!!.setBackgroundResource(R.drawable.ic_count_bg)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        onBackPressed()
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        val manager = getSystemService(ACTIVITY_SERVICE) as ActivityManager
        val taskList = manager.getRunningTasks(10)
        if (taskList[0].numActivities <= 1) {
            startActivity(Intent(applicationContext, HomeActivity::class.java))
            finish()
        } else {
            super.onBackPressed()
        }
        Utils.rightTransaction(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.iv_fav -> if (mBinding!!.ivFav.tag as Int == 1) {
                addRemoveFav()
            } else {
                addRemoveFav()
            }

            R.id.add_item, R.id.add_to_cart -> itemAddRemove(true, false)
            R.id.remove_item -> itemAddRemove(false, false)
            R.id.btn_share -> shareProduct(bitmapx)
            R.id.buy_now -> {
                val `in` = Intent(applicationContext, ShoppingCartActivity::class.java)
                startActivity(`in`)
                Utils.leftTransaction(this)
            }

            R.id.go_to_home -> {
                val intent = Intent(applicationContext, HomeActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase))
    }

    override fun applyOverrideConfiguration(overrideConfiguration: Configuration) {
        if (overrideConfiguration != null) {
            val uiMode = overrideConfiguration.uiMode
            overrideConfiguration.setTo(resources.configuration)
            overrideConfiguration.uiMode = uiMode
        }
        super.applyOverrideConfiguration(overrideConfiguration)
    }

    private fun initialization() {
        utils = Utils(this)
        commonClassForAPI = CommonClassForAPI.getInstance(this)
        lang = LocaleHelper.getLanguage(this)
        custId = SharePrefs.getInstance(this).getInt(SharePrefs.CUSTOMER_ID)
        wId = SharePrefs.getInstance(this).getInt(SharePrefs.WAREHOUSE_ID)
        ivProductImage = mBinding!!.productImage
        ivProductName = mBinding!!.productName
        tvProductPrice = mBinding!!.productPrice
        tvItemRowQuantityTv = mBinding!!.itemRowQuantityTv
        tvMoQTV = mBinding!!.tvMoq
        TvMrpTV = mBinding!!.tvMrp
        tvMargin = mBinding!!.tvMargin
        tvDreemPointTV = mBinding!!.tvDreamPoint
        buyValueText = mBinding!!.tvBuyValue
        GetValueTextView = mBinding!!.tvGetValue
        flashOfferViewLL = mBinding!!.llFlashOfferView
        leftItemsTV = mBinding!!.tvLeftItems
        remaingSecTV = mBinding!!.tvRemaingSecand
        mainOfferViewLL = mBinding!!.llMainOfferView
        freeItemQutTV = mBinding!!.tvFreeItemQut
        freeDepePointTV = mBinding!!.tvFreeDepePoint
        freeItemIV = mBinding!!.ivFreeIteam
        mBinding!!.tvMrpHead.text =
            MyApplication.getInstance().dbHelper.getString(R.string.item_mrp)
        mBinding!!.tvQtLeft.text =
            MyApplication.getInstance().dbHelper.getString(R.string.remaining_qty)
        mBinding!!.tvUnlock.text =
            MyApplication.getInstance().dbHelper.getString(R.string.unlock_price)
        mBinding!!.tvItemLeftD.text =
            MyApplication.getInstance().dbHelper.getString(R.string.item_left_d)
        mBinding!!.tvEndInD.text = MyApplication.getInstance().dbHelper.getString(R.string.end_in)
        mBinding!!.tvInactiveOrder.text =
            MyApplication.getInstance().dbHelper.getString(R.string.inactive_customer_msg)
        mBinding!!.tvShare.text =
            MyApplication.getInstance().dbHelper.getString(R.string.share_product_txt)
        mBinding!!.btnShare.text = MyApplication.getInstance().dbHelper.getString(R.string.share)
        mBinding!!.addToCart.text =
            MyApplication.getInstance().dbHelper.getString(R.string.add_to_basket)
        mBinding!!.buyNow.text = MyApplication.getInstance().dbHelper.getString(R.string.buy_now)
        mBinding!!.tvSorryNoItem.text =
            MyApplication.getInstance().dbHelper.getString(R.string.sorry_this_item_is_not_available_at_the_moment_nplease_check_back_later)
        mBinding!!.goToHome.text =
            MyApplication.getInstance().dbHelper.getString(R.string.go_to_home)
        mBinding!!.tvMrp.paintFlags = mBinding!!.tvMrp.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        mBinding!!.ivFav.setOnClickListener(this)
        mBinding!!.addItem.setOnClickListener(this)
        mBinding!!.removeItem.setOnClickListener(this)
        mBinding!!.addToCart.setOnClickListener(this)
        mBinding!!.btnShare.setOnClickListener(this)
        mBinding!!.buyNow.setOnClickListener(this)
        mBinding!!.tvUnlock.setOnClickListener { v: View? ->
            if (!SharePrefs.getInstance(
                    applicationContext
                ).getBoolean(SharePrefs.IS_PRIME_MEMBER) && itemListModel!!.isPrimeItem
            ) {
                startActivity(Intent(applicationContext, MembershipPlanActivity::class.java))
            }
        }
    }

    fun fetchParameters(url: Any?) {
        var urlS = url.toString()
        urlS = urlS.replace("&".toRegex(), "")
        val parameters = urlS.split("=".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        urlNumber = parameters[parameters.size - 3].replace("multiMrpId".toRegex(), "")
        urlItemMultiMRPId = parameters[parameters.size - 2].replace("warehouseId".toRegex(), "")
        urlwarehouseId = parameters[parameters.size - 1]
    }

    private fun shareProduct(bitmap: Bitmap?) {
        if (bitmap != null) {
            if (Build.VERSION.SDK_INT >= 23) {
                if (checkPermission()) {
                    val filename = Constant.PRODUCT_IMAGE_FOLDER
                    val sd = Environment.getExternalStorageDirectory()
                    val dest = File(sd, filename)
                    if (!dest.exists()) {
                        dest.mkdirs()
                    }
                    try {
                        val out = FileOutputStream("$dest/image.png")
                        bitmap.compress(Bitmap.CompressFormat.PNG, 90, out)
                        out.flush()
                        out.close()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    try {
                        shareWithImage()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } else {
                    requestPermission() // Code for permission
                }
            } else {
                val filename = Constant.PRODUCT_IMAGE_FOLDER
                val sd = Environment.getExternalStorageDirectory()
                val dest = File(sd, filename)
                if (!dest.exists()) {
                    dest.mkdirs()
                }
                try {
                    val out = FileOutputStream("$dest/image.png")
                    bitmap.compress(Bitmap.CompressFormat.PNG, 90, out)
                    out.flush()
                    out.close()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                try {
                    shareWithImage()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        } else {
            shareWithoutImage()
        }
    }

    private fun shareWithImage() {
        val share = Intent(Intent.ACTION_SEND)
        share.setType("image/*")
        share.putExtra(
            Intent.EXTRA_TEXT,
            itemListModel!!.itemname + " " + Constant.SHARE_URL + "number=" + itemListModel!!.itemNumber + "&multiMrpId=" + itemListModel!!.itemMultiMRPId + "&warehouseId=" +
                    SharePrefs.getInstance(applicationContext).getInt(SharePrefs.WAREHOUSE_ID)
        )
        share.putExtra(
            Intent.EXTRA_STREAM, FileProvider.getUriForFile(
                Objects.requireNonNull(
                    applicationContext
                ),
                BuildConfig.APPLICATION_ID + ".provider",
                File(
                    Environment.getExternalStorageDirectory()
                        .toString() + Constant.PRODUCT_IMAGE_FOLDER + "image.png"
                )
            )
        )
        startActivity(Intent.createChooser(share, "Share Image"))
        MyApplication.getInstance().updateAnalyticShareProd(itemListModel)
    }

    private fun shareWithoutImage() {
        val share = Intent(Intent.ACTION_SEND)
        share.setType("text/plain")
        share.putExtra(
            Intent.EXTRA_TEXT,
            itemListModel!!.itemname + " " + Constant.SHARE_URL + "number=" + itemListModel!!.itemNumber + "&multiMrpId=" + itemListModel!!.itemMultiMRPId + "&warehouseId=" +
                    SharePrefs.getInstance(applicationContext).getInt(SharePrefs.WAREHOUSE_ID)
        )
        startActivity(Intent.createChooser(share, "Share Image"))
        MyApplication.getInstance().updateAnalyticShareProd(itemListModel)
    }

    private fun setValue() {
        try {
            // check item in wishList
            if (MyApplication.getInstance().noteRepository.isItemWishList(itemListModel!!.itemId)) {
                mBinding!!.ivFav.setImageResource(R.drawable.ic_favorite_red)
            } else {
                mBinding!!.ivFav.setImageResource(R.drawable.ic_favourite)
            }

            //offer section
            if (itemListModel!!.isOffer) {
                mainOfferViewLL!!.visibility = View.VISIBLE
                val spItemName = itemListModel!!.itemname
                // String[] spItemName = itemListModel.getItemname().split("(?<=\\D)(?=\\d)");
                val freeOfferTextBuy =
                    "<font color=#fe4e4e>Buy&nbsp;" + itemListModel!!.offerMinimumQty + "&nbsp;pcs&nbsp;</font>" + "<font color=#000000>&nbsp;" + spItemName + " </font>"
                var freeOfferTextGet = ""
                buyValueText!!.text = Html.fromHtml(freeOfferTextBuy)
                when (itemListModel!!.offerType) {
                    "WalletPoint" -> {
                        freeItemQutTV!!.text = "0"
                        freeDepePointTV!!.text = "DP"
                        val sfreewalletDP = DecimalFormat("##.##").format(
                            itemListModel!!.offerWalletPoint
                        )
                        freeOfferTextGet =
                            "<font color=#fe4e4e>Get&nbsp;$sfreewalletDP&nbsp;Free</font><font color=#000000>&nbsp;Dream Points</font>"
                        freeItemIV!!.setBackgroundResource(R.drawable.ic_gift_bg)
                    }

                    "ItemMaster" -> {
                        freeItemQutTV!!.text = "0"
                        freeDepePointTV!!.text = "Free"
                        freeOfferTextGet =
                            "<font color=#fe4e4e>Get " + itemListModel!!.offerFreeItemQuantity + "&nbsp;Free </font>" + "<font color=#000000>&nbsp;" + itemListModel!!.offerFreeItemName + " </font>"
                        if (!TextUtils.isNullOrEmpty(
                                itemListModel!!.offerFreeItemImage
                            )
                        ) {
                            Glide.with(applicationContext).load(itemListModel!!.offerFreeItemImage)
                                .placeholder(vectorDrawable).into(
                                freeItemIV!!
                            )
                        } else {
                            freeItemIV!!.setImageDrawable(vectorDrawable)
                        }
                    }

                    "FlashDeal" -> {
                        leftItemsTV!!.text = itemListModel!!.offerQtyAvaiable.toString()
                        mainOfferViewLL!!.visibility = View.GONE
                        flashOfferViewLL!!.visibility = View.VISIBLE
                        timeDeference(itemListModel)
                    }
                }
                GetValueTextView!!.text = Html.fromHtml(freeOfferTextGet)
            } else {
                mainOfferViewLL!!.visibility = View.GONE
            }
            if (!TextUtils.isNullOrEmpty(
                    itemListModel!!.logoUrl
                )
            ) {
                Glide.with(this).load(itemListModel!!.logoUrl)
                    .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                    .addListener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any,
                            target: Target<Drawable>,
                            isFirstResource: Boolean
                        ): Boolean {
                            return false
                        }

                        override fun onResourceReady(
                            resource: Drawable,
                            model: Any,
                            target: Target<Drawable>,
                            dataSource: DataSource,
                            isFirstResource: Boolean
                        ): Boolean {
                            Utils.hideProgressDialog()
                            bitmapx = (resource as BitmapDrawable).bitmap
                            return false
                        }
                    }).into(ivProductImage!!)
            } else {
                Glide.with(applicationContext).load(R.drawable.logo_grey).into(
                    ivProductImage!!
                )
            }
            val sMRP = DecimalFormat("##.##").format(itemListModel!!.unitPrice)
            val productPrice = DecimalFormat("##.##").format(
                itemListModel!!.unitPrice
            )
            val sMargin =
                " | " + MyApplication.getInstance().dbHelper.getString(R.string.moq_margin) + " " + DecimalFormat(
                    "##.##"
                ).format(
                    itemListModel!!.marginPoint!!.toDouble()
                ) + "%"
            val text = "<font color=#FF4500>&#8377; $productPrice"
            ivProductName!!.text = itemListModel!!.itemname
            tvProductPrice!!.text = Html.fromHtml(text).toString()
            tvMoQTV!!.text =
                MyApplication.getInstance().dbHelper.getString(R.string.item_moq) + " " + itemListModel!!.minOrderQty
            TvMrpTV!!.text = sMRP
            tvMargin!!.text = sMargin
            tvDreemPointTV!!.text = "DP " + itemListModel!!.dreamPoint
            if (itemListModel!!.isPrimeItem) {
                mBinding!!.liPrime.visibility = View.VISIBLE
                mBinding!!.tvPPrice.text =
                    (SharePrefs.getInstance(applicationContext).getString(SharePrefs.PRIME_NAME)
                            + ": ₹" + DecimalFormat("##.##").format(itemListModel!!.primePrice))
                mBinding!!.productPrice.setTextColor(resources.getColor(R.color.grey))
            } else {
                mBinding!!.liPrime.visibility = View.GONE
                mBinding!!.productPrice.setTextColor(Color.parseColor("#FF4500"))
            }
            if (SharePrefs.getInstance(applicationContext)
                    .getBoolean(SharePrefs.IS_PRIME_MEMBER) && itemListModel!!.isPrimeItem
            ) {
                mBinding!!.tvUnlock.text = ""
                mBinding!!.tvUnlock.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.ic_lock_open,
                    0,
                    0,
                    0
                )
            } else {
                mBinding!!.tvUnlock.text = "Unlock"
                mBinding!!.tvUnlock.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.ic_lock,
                    0,
                    R.drawable.ic_right_arrow,
                    0
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun checkPermission(): Boolean {
        val result = ContextCompat.checkSelfPermission(
            applicationContext,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        return result == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        ) {
            Toast.makeText(
                applicationContext,
                MyApplication.getInstance().dbHelper.getString(R.string.write_external_permission_setting),
                Toast.LENGTH_LONG
            ).show()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                100
            )
        }
    }

    private fun updateItemQuantity() {
        val model = MyApplication.getInstance().noteRepository.getCartItem1(
            itemListModel!!.itemId
        )
        if (model != null) {
            val itemQuantity = model.qty
            val unitPrice = model.unitPrice
            val totalPriceSt = "₹" + DecimalFormat("##.##").format(itemQuantity * unitPrice)
            mBinding!!.tvTotalAmount.text = totalPriceSt

            // offer section start
            if (model.isOffer) {
                if (model.offerType.equals("WalletPoint", ignoreCase = true)) {
                    val freeWalletPoint = model.totalFreeWalletPoint
                    val sfreewalletDP = DecimalFormat("##.##").format(freeWalletPoint)
                    if (freeWalletPoint > 0) {
                        freeItemQutTV!!.text = sfreewalletDP
                    }
                } else {
                    val freeItemQuantity = model.offerFreeItemQuantity!!.toInt()
                    if (freeItemQuantity > 0) {
                        freeItemQutTV!!.text = "" + freeItemQuantity
                    }
                }
            }
            // offer section end
            if (itemQuantity != 0) {
                tvItemRowQuantityTv!!.text = "" + itemQuantity
                mBinding!!.addToCart.visibility = View.GONE
                mBinding!!.addRemoveHolder.visibility = View.VISIBLE
            } else {
                mBinding!!.addToCart.visibility = View.VISIBLE
                mBinding!!.addRemoveHolder.visibility = View.GONE
            }
        } else {
            val itemQuantity = 0
            tvItemRowQuantityTv!!.text = "" + itemQuantity
        }
    }

    private fun itemAddRemove(addItem: Boolean, addBtn: Boolean) {
        try {
            var FreeItemQuantity = 0
            var total = 0
            var FreeWalletPoint = 0.0
            var addFlag = false
            var flashDealFlag = false
            flashofferFlag = false
            var calUnitPrice = 0.0
            var isflashDealUsed = false
            var isPrimeItem = false
            var itemQuantity = mBinding!!.itemRowQuantityTv.text.toString().toInt()
            if (itemListModel!!.isOffer) {
                try {
                    if (itemListModel!!.offerType != null) {
                        if (itemListModel!!.offerType.equals("FlashDeal", ignoreCase = true)) {
                            val jsonFlashString = SharePrefs.getStringSharedPreferences(
                                this,
                                SharePrefs.ITEM_FLASH_DEAL_USED_JSON
                            )
                            if (!jsonFlashString.isEmpty()) {
                                val jsonObject = JSONObject(jsonFlashString)
                                if (jsonObject.has(itemListModel!!.itemId.toString())) {
                                    if (jsonObject[itemListModel!!.itemId.toString()] == "1") {
                                        isflashDealUsed = true
                                    }
                                }
                            }
                            if (itemListModel!!.flashDealMaxQtyPersonCanTake > itemQuantity && itemListModel!!.offerQtyAvaiable > itemQuantity) {
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
                    itemQuantity += itemListModel!!.minOrderQty
                    if (itemListModel!!.isItemLimit) {
                        if (MyApplication.getInstance().noteRepository.cartCount > 0) {
                            val qtyDTO =
                                MyApplication.getInstance().noteRepository.getQtyTotalQtyByMrpId(
                                    itemListModel!!.itemId, itemListModel!!.itemMultiMRPId
                                )
                            total = qtyDTO.quantity
                            if (total + itemQuantity > itemListModel!!.itemLimitQty) {
                                Utils.setToast(
                                    applicationContext,
                                    MyApplication.getInstance().dbHelper.getString(
                                        R.string.additemToast
                                    )
                                            + " " + itemListModel!!.itemLimitQty
                                )
                            } else {
                                if (itemListModel!!.billLimitQty != 0) {
                                    addFlag = setBillLimit(itemListModel, addFlag, itemQuantity)
                                } else {
                                    mBinding!!.itemRowQuantityTv.text = "" + itemQuantity
                                    addFlag = true
                                }
                            }
                        } else {
                            if (itemQuantity > 0) {
                                if (itemQuantity > itemListModel!!.itemLimitQty) {
                                    Utils.setToast(
                                        applicationContext,
                                        MyApplication.getInstance().dbHelper.getString(
                                            R.string.additemToast
                                        )
                                                + " " + itemListModel!!.itemLimitQty
                                    )
                                } else {
                                    if (itemListModel!!.billLimitQty != 0) {
                                        addFlag = setBillLimit(itemListModel, addFlag, itemQuantity)
                                    } else {
                                        mBinding!!.itemRowQuantityTv.text = "" + itemQuantity
                                        addFlag = true
                                    }
                                }
                            }
                        }
                    } else {
                        if (itemListModel!!.billLimitQty != 0) {
                            addFlag = setBillLimit(itemListModel, addFlag, itemQuantity)
                        } else {
                            mBinding!!.itemRowQuantityTv.text = "" + itemQuantity
                            addFlag = true
                        }
                    }
                } else {
                    if (addBtn) {
//                        viewHolder.mBinding.visible.setVisibility(View.GONE);
//                        viewHolder.btnAdd.setVisibility(View.VISIBLE);
                        Toast.makeText(
                            applicationContext, MyApplication.getInstance().dbHelper.getString(
                                R.string.no_items_avl
                            ), Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        if (itemListModel!!.offerQtyAvaiable <= itemQuantity) {
                            Toast.makeText(
                                applicationContext, MyApplication.getInstance().dbHelper.getString(
                                    R.string.no_items_avl
                                ), Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            Toast.makeText(
                                applicationContext, MyApplication.getInstance().dbHelper.getString(
                                    R.string.only_add_max_item
                                ) + itemListModel!!.flashDealMaxQtyPersonCanTake, Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            } else {
                //minus btn
                if (itemQuantity > 0) {
                    itemQuantity -= itemListModel!!.minOrderQty
                    mBinding!!.itemRowQuantityTv.text = "" + itemQuantity
                    addFlag = true
                }
            }
            val totalPriceSt =
                "₹" + DecimalFormat("##.##").format(itemQuantity * itemListModel!!.unitPrice)
            mBinding!!.tvTotalAmount.text = totalPriceSt
            if (addFlag) {
                /*offer section*/
                if (itemListModel!!.isOffer) {
                    val offerMinimumQty = itemListModel!!.offerMinimumQty
                    when (itemListModel!!.offerType) {
                        "WalletPoint" -> if (offerMinimumQty != 0) {
                            if (itemListModel!!.offerWalletPoint != null) {
                                //event trigger
                                if (itemQuantity >= offerMinimumQty) {
                                    FreeWalletPoint = itemListModel!!.offerWalletPoint!!
                                    val calfreeItemQty = itemQuantity / offerMinimumQty
                                    FreeWalletPoint *= calfreeItemQty.toDouble()
                                    val sfreewalletDP =
                                        DecimalFormat("##.##").format(FreeWalletPoint)
                                    if (FreeWalletPoint > 0) {
                                        mBinding!!.tvFreeItemQut.text = sfreewalletDP
                                    }
                                } else {
                                    mBinding!!.tvFreeItemQut.text = "0"
                                }
                            } else {
                                Toast.makeText(
                                    this, MyApplication.getInstance().dbHelper.getString(
                                        R.string.minimum_qty_should_not_be_zero
                                    ), Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                        "ItemMaster" -> if (offerMinimumQty != 0) {
                            if (itemListModel!!.offerFreeItemQuantity != null) {
                                //event trigger
                                if (itemQuantity >= offerMinimumQty) {
                                    FreeItemQuantity =
                                        itemListModel!!.offerFreeItemQuantity!!.toInt()
                                    val calfreeItemQty = itemQuantity / offerMinimumQty
                                    FreeItemQuantity *= calfreeItemQty
                                    if (FreeItemQuantity > 0) {
                                        mBinding!!.tvFreeItemQut.text = FreeItemQuantity.toString()
                                    }
                                } else {
                                    mBinding!!.tvFreeItemQut.text = "0"
                                }
                            }
                        } else {
                            Toast.makeText(
                                this,
                                MyApplication.getInstance().dbHelper.getString(R.string.minimum_qty_should_not_be_zero),
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        "FlashDeal" -> if (!isflashDealUsed) {
                            val remainingLeft = itemListModel!!.offerQtyAvaiable - itemQuantity
                            if (remainingLeft > 0) {
                                mBinding!!.tvLeftItems.text = "" + remainingLeft
                            } else {
                                mBinding!!.tvLeftItems.text = "0"
                            }
                            flashofferFlag = true
                        } else {
                            flashofferFlag = false
                        }
                    }
                }
                if (flashofferFlag) {
                    calUnitPrice = itemListModel!!.flashDealSpecialPrice
                } else if (SharePrefs.getInstance(applicationContext)
                        .getBoolean(SharePrefs.IS_PRIME_MEMBER) && itemListModel!!.isPrimeItem
                ) {
                    calUnitPrice = itemListModel!!.primePrice
                    isPrimeItem = true
                } else {
                    calUnitPrice = itemListModel!!.unitPrice
                }
                addItemInCartItemArrayList(
                    itemListModel!!.itemId, itemQuantity, calUnitPrice, itemListModel,
                    FreeItemQuantity, FreeWalletPoint, isPrimeItem
                )
                RxBus.getInstance().sendEvent(true)
            }

            //Please dont change this condition
            if (itemQuantity != 0) {
                mBinding!!.addToCart.visibility = View.GONE
                mBinding!!.addRemoveHolder.visibility = View.VISIBLE
            } else {
                mBinding!!.addToCart.visibility = View.VISIBLE
                mBinding!!.addRemoveHolder.visibility = View.GONE
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setLimit(itemQuantity: Int, add: Boolean): Boolean {
        val qtyDTO = MyApplication.getInstance().noteRepository.getQtyTotalQtyByMrpId(
            itemListModel!!.itemId, itemListModel!!.itemMultiMRPId
        )
        val total = qtyDTO.quantity
        var availqty = 0
        var totalItemqty = qtyDTO.totalQuantity
        var itemlimitqty = 0
        var addFlag = false
        if (add) {
            totalItemqty += itemListModel!!.minOrderQty
        } else {
            totalItemqty -= itemListModel!!.minOrderQty
        }
        itemlimitqty = itemListModel!!.itemLimitQty
        if (total > 0) {
            if (total + itemQuantity > itemListModel!!.itemLimitQty) {
                Utils.setToast(
                    applicationContext,
                    MyApplication.getInstance().dbHelper.getString(R.string.additemToast) +
                            " " + itemListModel!!.itemLimitQty + " " +
                            MyApplication.getInstance().dbHelper.getString(R.string.additemToast_2)
                )
            } else {
                if (itemListModel!!.billLimitQty != 0) {
                    addFlag = setBillLimit(itemListModel, addFlag, itemQuantity)
                } else {
                    mBinding!!.itemRowQuantityTv.text = "" + itemQuantity
                    addFlag = true
                }
                if (addFlag) {
                    availqty = itemlimitqty - totalItemqty
                    if (availqty >= 0) {
                        mBinding!!.tvAvailQty.text = availqty.toString()
                    }
                }
            }
        } else {
            if (itemQuantity > 0) {
                if (itemQuantity > itemListModel!!.itemLimitQty) {
                    Utils.setToast(
                        applicationContext,
                        MyApplication.getInstance().dbHelper.getString(R.string.additemToast) +
                                " " + itemListModel!!.itemLimitQty + " " +
                                MyApplication.getInstance().dbHelper.getString(R.string.additemToast_2)
                    )
                } else {
                    if (itemListModel!!.billLimitQty != 0) {
                        addFlag = setBillLimit(itemListModel, addFlag, itemQuantity)
                    } else {
                        mBinding!!.itemRowQuantityTv.text = "" + itemQuantity
                        addFlag = true
                    }
                    if (addFlag) {
                        availqty = itemlimitqty - totalItemqty
                        if (availqty >= 0) {
                            mBinding!!.tvAvailQty.text = availqty.toString()
                        }
                    }
                }
            }
        }
        return addFlag
    }

    private fun setBillLimit(
        itemListModel: ItemListModel?,
        addFlag: Boolean,
        itemQuantity: Int
    ): Boolean {
        var addFlag = addFlag
        try {
            val total = MyApplication.getInstance().noteRepository.getQtyByMultiMrp(
                itemListModel!!.itemId, itemListModel.itemMultiMRPId
            )
            val itemlimitqty = itemListModel.billLimitQty
            if (total != 0) {
                if (total + itemQuantity > itemlimitqty) {
                    Utils.setToast(
                        applicationContext,
                        MyApplication.getInstance().dbHelper.getString(R.string.bill_limit_text) + itemListModel.billLimitQty + " item"
                    )
                } else {
                    mBinding!!.itemRowQuantityTv.text = "" + itemQuantity
                    addFlag = true
                }
            } else {
                if (itemQuantity > 0) {
                    if (itemQuantity > itemListModel.billLimitQty) {
                        Utils.setToast(
                            applicationContext,
                            MyApplication.getInstance().dbHelper.getString(R.string.bill_limit_text) + itemListModel.billLimitQty + " item"
                        )
                    } else {
                        mBinding!!.itemRowQuantityTv.text = "" + itemQuantity
                        addFlag = true
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return addFlag
    }

    fun addItemInCartItemArrayList(
        itemId: Int, qty: Int, itemUnitPrice: Double, model: ItemListModel?, freeItemQty: Int,
        totalFreeWalletPoint: Double, isPrimeItem: Boolean
    ): String {
        if (utils!!.isNetworkAvailable) {
            mBinding!!.ivProgress.visibility = View.VISIBLE
            commonClassForAPI!!.postAddCartItem(
                object : DisposableObserver<JsonObject?>() {
                    override fun onNext(`object`: JsonObject) {
                        if (`object`["Status"].asBoolean) {
                            mBinding!!.ivProgress.visibility = View.GONE
                            try {
                                model!!.unitPrice = itemUnitPrice
                                model.qty = qty
                                model.totalFreeItemQty = freeItemQty
                                model.totalFreeWalletPoint = totalFreeWalletPoint
                                // update cart database
                                if (MyApplication.getInstance().noteRepository.isItemInCart(itemId)) {
                                    MyApplication.getInstance().noteRepository.updateCartItem(model)
                                } else {
                                    MyApplication.getInstance().noteRepository.addToCart(model)
                                }
                                // update cart database
                                if (MyApplication.getInstance().noteRepository.isItemInCart(itemId)) {
                                    MyApplication.getInstance().noteRepository.updateCartItem(model)
                                } else {
                                    MyApplication.getInstance().noteRepository.addToCart(model)
                                }
                                RxBus.getInstance().sendEvent(true)
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        } else {
                            Utils.setToast(
                                applicationContext, "unable to add cart"
                            )
                        }
                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                        mBinding!!.ivProgress.visibility = View.GONE
                    }

                    override fun onComplete() {
                        mBinding!!.ivProgress.visibility = View.GONE
                    }
                },
                CartAddItemModel(
                    custId,
                    wId,
                    itemId,
                    qty,
                    itemUnitPrice,
                    false,
                    false,
                    lang,
                    isPrimeItem,
                    false
                ),
                "Product share Item"
            )
        } else {
            Utils.setToast(
                applicationContext,
                MyApplication.getInstance().dbHelper.getString(R.string.internet_connection)
            )
        }
        return status
    }

    private fun timeDeference(itemListModel: ItemListModel?) {
        try {
            val updateTimer = Timer()
            val sdf = SimpleDateFormat(myFormat, Locale.ENGLISH)
            val currentDate = sdf.parse(itemListModel!!.currentStartTime)
            val endTime = sdf.parse(itemListModel.offerEndTime)
            if (endTime.time > currentDate.time) {
                updateTimer.schedule(object : TimerTask() {
                    override fun run() {
                        try {
                            val systemDate = Calendar.getInstance().time
                            val myDate = sdf.format(systemDate)
                            val currentTime = sdf.parse(myDate)
                            val endTime = sdf.parse(itemListModel.offerEndTime)
                            val millse = endTime.time - currentTime.time
                            val mills = Math.abs(millse)
                            val Hours = (mills / (1000 * 60 * 60)).toInt()
                            val Mins = (mills / (1000 * 60)).toInt() % 60
                            val Secs = ((mills / 1000).toInt() % 60).toLong()
                            runOnUiThread { remaingSecTV!!.text = Secs.toString() }
                        } catch (e: ParseException) {
                            e.printStackTrace()
                        }
                    }
                }, 0, 1000)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun addRemoveFav() {
        if (MyApplication.getInstance().noteRepository.isItemWishList(itemListModel!!.itemId)) {
            mBinding!!.ivFav.setImageResource(R.drawable.ic_favourite)
            MyApplication.getInstance().noteRepository.deleteTask(itemListModel)
            Utils.addFav(itemListModel!!.itemId.toString().toInt(), false, this)
        } else {
            mBinding!!.ivFav.setImageResource(R.drawable.ic_favorite_red)
            MyApplication.getInstance().noteRepository.insertTask(itemListModel)
            Utils.addFav(itemListModel!!.itemId.toString().toInt(), true, this)
        }
    }

    private val productDetailsDes: DisposableObserver<ItemListModel> =
        object : DisposableObserver<ItemListModel>() {
            override fun onNext(obj: ItemListModel) {
                Utils.hideProgressDialog()
                itemListModel = obj
                setValue()
                updateItemQuantity()
            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
                Utils.hideProgressDialog()
                mBinding!!.successScreen.visibility = View.GONE
                mBinding!!.errorScreen.visibility = View.VISIBLE
            }

            override fun onComplete() {
                Utils.hideProgressDialog()
            }
        }

    companion object {
        var myFormat = "yyyy-mm-dd'T'hh:mm:ss"
    }
}