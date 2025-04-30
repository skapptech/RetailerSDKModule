package app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.productDetails

import android.Manifest
import android.app.ProgressDialog
import android.content.ContentValues
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.provider.MediaStore
import android.text.Html
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import app.retailer.krina.shop.com.mp_shopkrina_retailer.BuildConfig
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.ItemListModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.repository.AppRepository
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.response.Response
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ActivityProductDetailBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.activity.MembershipPlanActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.shoppingCart.ShoppingCartActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.NotifyModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.CartAddItemModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.SharePrefs
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.HomeActivity
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
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.Timer
import java.util.TimerTask
import java.util.concurrent.TimeUnit

class ProductDetailsActivity : AppCompatActivity(), View.OnClickListener {
    private var mBinding: ActivityProductDetailBinding? = null
    private lateinit var viewModel: ProductDetailsViewModel
    private var vectorDrawable: Drawable? = null
    private var bitmapx: Bitmap? = null
    private var itemListModel: ItemListModel? = null
    private var custId = 0
    private var wId = 0
    private var lang = ""
    private val handler = Handler()
    private var apiRunning = false
    private var dialog: ProgressDialog? = null
    private var remainingqty: String? = null
    private val FORMAT = "%02d:%02d:%02d"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_product_detail)
        val appRepository = AppRepository(applicationContext)
        viewModel = ViewModelProvider(
            this,
            ProductDetailsViewModelFactory(application, appRepository)
        )[ProductDetailsViewModel::class.java]

        if (supportActionBar != null) supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        title = MyApplication.getInstance().dbHelper.getString(R.string.txt_product_deatils)
        vectorDrawable = AppCompatResources.getDrawable(
            applicationContext, R.drawable.logo_grey
        )
        // Intent data
        val bundle = intent.extras
        if (intent.extras != null) {
            itemListModel = bundle!!.getSerializable("ITEM_LIST") as ItemListModel?
            remainingqty = bundle.getString("remainingqty")
        }
        //init view
        initialization()
        setValue()
        updateItemQuantity()
        mBinding!!.tvLeftItems.setBackgroundResource(R.drawable.ic_count_bg)
        mBinding!!.tvRemaingSecand.setBackgroundResource(R.drawable.ic_count_bg)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        MyApplication.getInstance().updateAnalyticsVItem(itemListModel)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        onBackPressed()
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        super.onBackPressed()
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
            R.id.btn_share -> shareProduct()
            R.id.buy_now -> if (!apiRunning) {
                startActivity(Intent(applicationContext, ShoppingCartActivity::class.java))
                Utils.leftTransaction(this)
            } else {
                dialog = ProgressDialog(this)
                dialog!!.setMessage("Please wait")
                dialog!!.show()
                dialog!!.setOnCancelListener { dialog1: DialogInterface? ->
                    dialog = null
                    mBinding!!.buyNow.callOnClick()
                }
            }

            R.id.go_to_home -> {
                startActivity(Intent(applicationContext, HomeActivity::class.java))
                finish()
            }

            else -> {}
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
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

    private fun initialization() {
        // set data
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
        lang = LocaleHelper.getLanguage(this)
        custId = SharePrefs.getInstance(applicationContext).getInt(SharePrefs.CUSTOMER_ID)
        wId = SharePrefs.getInstance(applicationContext).getInt(SharePrefs.WAREHOUSE_ID)
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
        mBinding!!.btItemNotyfy.setOnClickListener { v: View? ->
            getNotifyItems(itemListModel!!.warehouseId, itemListModel!!.itemNumber)
            MyApplication.getInstance().noteRepository.insertNotifyItemTask(
                NotifyModel(
                    itemListModel!!.itemId
                )
            )
            mBinding!!.btItemNotyfy.background =
                resources.getDrawable(R.drawable.background_for_buttons_disble)
            mBinding!!.btItemNotyfy.isClickable = false
            mBinding!!.btItemNotyfy.isEnabled = false
        }
    }

    private fun shareProduct() {
        try {
            if (bitmapx != null) {
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
                    if (checkPermission()) {
                        var imageUri: Uri? = null
                        val fos: OutputStream
                        val directory = Environment.DIRECTORY_PICTURES
                        val resolver = contentResolver
                        val contentValues = ContentValues()
                        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, "image.png")
                        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/png")
                        contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, directory)
                        imageUri = resolver.insert(
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            contentValues
                        )
                        fos = resolver.openOutputStream(imageUri!!)!!
                        bitmapx!!.compress(Bitmap.CompressFormat.PNG, 90, fos)
                        fos.flush()
                        fos.close()
                        shareWithImage()
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
                    val out = FileOutputStream("$dest/image.png")
                    bitmapx!!.compress(Bitmap.CompressFormat.PNG, 90, out)
                    out.flush()
                    out.close()
                    shareWithImage()
                }
            } else {
                shareWithoutImage()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun shareWithImage() {
        val share = Intent(Intent.ACTION_SEND)
        share.setType("image/*")
        share.putExtra(
            Intent.EXTRA_TEXT, itemListModel!!.itemname + " " +
                    Constant.SHARE_URL + "number=" + itemListModel!!.itemNumber + "&multiMrpId=" +
                    itemListModel!!.itemMultiMRPId + "&warehouseId=" +
                    SharePrefs.getInstance(this@ProductDetailsActivity)
                        .getInt(SharePrefs.WAREHOUSE_ID)
        )
        share.putExtra(
            Intent.EXTRA_STREAM, FileProvider.getUriForFile(
                applicationContext,
                BuildConfig.APPLICATION_ID + ".provider",
                File(
                    Environment.getExternalStorageDirectory().toString() + File.separator
                            + Environment.DIRECTORY_PICTURES + File.separator + "image.png"
                )
            )
        )
        startActivity(Intent.createChooser(share, "Share Product"))
        MyApplication.getInstance().updateAnalyticShareProd(itemListModel)
    }

    private fun shareWithoutImage() {
        val share = Intent(Intent.ACTION_SEND)
        share.setType("text/plain")
        share.putExtra(
            Intent.EXTRA_TEXT, itemListModel!!.itemname + " " + Constant.SHARE_URL
                    + "number=" + itemListModel!!.itemNumber + "&multiMrpId=" +
                    itemListModel!!.itemMultiMRPId + "&warehouseId=" +
                    SharePrefs.getInstance(this@ProductDetailsActivity)
                        .getInt(SharePrefs.WAREHOUSE_ID)
        )
        startActivity(Intent.createChooser(share, "Share Product"))
        MyApplication.getInstance().updateAnalyticShareProd(itemListModel)
    }

    fun getNotifyItems(warehouseId: Int, itemNumber: String?) {
        viewModel.getNotifyItems(custId, warehouseId, itemNumber!!)
        viewModel.getNotifyItemsData.observe(this) {
            if (it) {
                Utils.setToast(
                    applicationContext,
                    MyApplication.getInstance().dbHelper.getString(R.string.txt_Notify_msg)
                )
            }
        }
    }

    private fun setValue() {
        // check item in wishList
        if (MyApplication.getInstance().noteRepository.isItemWishList(itemListModel!!.itemId)) {
            mBinding!!.ivFav.setImageResource(R.drawable.ic_favorite_red)
            mBinding!!.ivFav.tag = 1
        } else {
            mBinding!!.ivFav.setImageResource(R.drawable.ic_favourite)
            mBinding!!.ivFav.tag = 0
        }

        //offer section
        if (itemListModel!!.isOffer) {
            /* for inactive customer*/
            if (!SharePrefs.getInstance(applicationContext).getBoolean(SharePrefs.CUST_ACTIVE)) {
                mBinding!!.llInActiveUser.visibility = View.VISIBLE
            }
            mBinding!!.llMainOfferView.visibility = View.VISIBLE
            val spItemName = itemListModel!!.itemname
            val freeOfferTextBuy =
                "<font color=#fe4e4e>Buy&nbsp;" + itemListModel!!.offerMinimumQty + "&nbsp;pcs&nbsp;</font>" + "<font color=#000000>&nbsp;" + spItemName + " </font>"
            var freeOfferTextGet = ""
            mBinding!!.tvBuyValue.text = Html.fromHtml(freeOfferTextBuy)
            when (itemListModel!!.offerType) {
                "WalletPoint" -> {
                    mBinding!!.tvFlashdealPrice.visibility = View.GONE
                    mBinding!!.tvFreeItemQut.text = "0"
                    mBinding!!.tvFreeDepePoint.text = "DP"
                    val sfreewalletDP = DecimalFormat(Utils.pattern).format(
                        itemListModel!!.offerWalletPoint
                    )
                    freeOfferTextGet =
                        "<font color=#fe4e4e>Get&nbsp;$sfreewalletDP&nbsp;Free</font><font color=#000000>&nbsp;Dream Points</font>"
                    mBinding!!.ivFreeIteam.setBackgroundResource(R.drawable.ic_gift_bg)
                }

                "ItemMaster" -> {
                    mBinding!!.tvFlashdealPrice.visibility = View.GONE
                    mBinding!!.tvFreeItemQut.text = "0"
                    mBinding!!.tvFreeDepePoint.text = "Free"
                    freeOfferTextGet =
                        "<font color=#fe4e4e>Get " + itemListModel!!.offerFreeItemQuantity + "&nbsp;Free </font>" + "<font color=#000000>&nbsp;" + itemListModel!!.offerFreeItemName + " </font>"
                    if (!TextUtils.isNullOrEmpty(
                            itemListModel!!.offerFreeItemImage
                        )
                    ) {
                        Glide.with(applicationContext).load(itemListModel!!.offerFreeItemImage)
                            .placeholder(vectorDrawable).into(
                            mBinding!!.ivFreeIteam
                        )
                    } else {
                        mBinding!!.ivFreeIteam.setImageDrawable(vectorDrawable)
                    }
                }

                "FlashDeal" -> {
                    mBinding!!.tvLeftItems.text = itemListModel!!.offerQtyAvaiable.toString()
                    mBinding!!.llMainOfferView.visibility = View.GONE
                    mBinding!!.llFlashOfferView.visibility = View.VISIBLE
                    timeDeference(itemListModel)
                    if (itemListModel!!.flashDealSpecialPrice != 0.0) {
                        mBinding!!.tvFlashdealPrice.visibility = View.VISIBLE
                        mBinding!!.productPrice.paintFlags =
                            mBinding!!.tvMrp.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                        mBinding!!.tvFlashdealPrice.text = " | " + DecimalFormat("##.##").format(
                            itemListModel!!.flashDealSpecialPrice
                        )
                    }
                }

                else -> {}
            }
            mBinding!!.tvGetValue.text = Html.fromHtml(freeOfferTextGet)
        } else {
            mBinding!!.tvFlashdealPrice.visibility = View.GONE
            mBinding!!.llMainOfferView.visibility = View.GONE
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
                }).into(mBinding!!.productImage)
        } else {
            mBinding!!.productImage.setImageResource(R.drawable.logo_grey)
        }
        val sMRP = DecimalFormat(Utils.pattern).format(
            itemListModel!!.price
        )
        val productPrice = DecimalFormat(Utils.pattern).format(
            itemListModel!!.unitPrice
        )
        val sMargin =
            " | " + MyApplication.getInstance().dbHelper.getString(R.string.moq_margin) + DecimalFormat(
                Utils.pattern
            ).format(
                itemListModel!!.marginPoint!!.toDouble()
            ) + "%"
        val text = "<font color=#FF4500>&#8377; $productPrice"
        mBinding!!.productName.text = itemListModel!!.itemname
        mBinding!!.productPrice.text = Html.fromHtml(text).toString()
        mBinding!!.tvMoq.text =
            MyApplication.getInstance().dbHelper.getString(R.string.item_moq) + itemListModel!!.minOrderQty
        if (!TextUtils.isNullOrEmpty(
                itemListModel!!.scheme
            )
        ) {
            mBinding!!.tvSchemeTextD.visibility = View.VISIBLE
            mBinding!!.tvSchemeTextD.text = "" + itemListModel!!.scheme
        } else {
            mBinding!!.tvSchemeTextD.visibility = View.GONE
        }
        mBinding!!.tvMrp.text = sMRP
        mBinding!!.tvMargin.text = sMargin
        mBinding!!.tvDreamPoint.text =
            MyApplication.getInstance().dbHelper.getString(R.string.total_dp) + itemListModel!!.dreamPoint
        if (itemListModel!!.itemLimitQty > 0) {
            remainingqty =
                if (remainingqty == null) "" + itemListModel!!.itemLimitQty else remainingqty
            mBinding!!.availQtyLayout.visibility = View.VISIBLE
            mBinding!!.tvAvailQty.text = remainingqty
        }
        if (itemListModel!!.isPrimeItem) {
            mBinding!!.liPrime.visibility = View.VISIBLE
            mBinding!!.tvPPrice.text =
                (SharePrefs.getInstance(applicationContext).getString(SharePrefs.PRIME_NAME)
                        + " " + MyApplication.getInstance().dbHelper.getString(R.string.price)
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
            mBinding!!.tvUnlock.text =
                MyApplication.getInstance().dbHelper.getString(R.string.unlock)
            mBinding!!.tvUnlock.setCompoundDrawablesWithIntrinsicBounds(
                R.drawable.ic_lock,
                0,
                R.drawable.ic_right_arrow,
                0
            )
        }
        if (itemListModel!!.active) {
            mBinding!!.ivFav.visibility = View.VISIBLE
            mBinding!!.btItemNotyfy.visibility = View.GONE
            mBinding!!.llBuyAndAdd.visibility = View.VISIBLE
        } else {
            mBinding!!.btItemNotyfy.visibility = View.VISIBLE
            mBinding!!.ivFav.visibility = View.INVISIBLE
            mBinding!!.llBuyAndAdd.visibility = View.GONE
            mBinding!!.tvTotalAmount.visibility = View.GONE
            mBinding!!.availQtyLayout.visibility = View.GONE
            if (MyApplication.getInstance().noteRepository.isNotifyDisable(itemListModel!!.itemId)) {
                mBinding!!.btItemNotyfy.background =
                    resources.getDrawable(R.drawable.background_for_buttons_disble)
                mBinding!!.btItemNotyfy.isClickable = false
                mBinding!!.btItemNotyfy.isEnabled = false
            } else {
                mBinding!!.btItemNotyfy.background =
                    resources.getDrawable(R.drawable.background_for_buttons)
                mBinding!!.btItemNotyfy.isClickable = true
                mBinding!!.btItemNotyfy.isEnabled = true
            }
        }
    }

    private fun checkPermission(): Boolean {
        val result =
            ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
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
                PERMISSION_REQUEST_CODE
            )
        }
    }

    private fun updateItemQuantity() {
        val cartModel = MyApplication.getInstance().noteRepository.getItemByMrpId(
            itemListModel!!.itemId, itemListModel!!.itemMultiMRPId
        )
        if (cartModel != null && itemListModel!!.itemId == cartModel.itemId) {
            val itemQuantity = cartModel.qty
            val unitPrice = cartModel.unitPrice
            val totalPriceSt = "₹" + DecimalFormat(Utils.pattern).format(itemQuantity * unitPrice)
            mBinding!!.tvTotalAmount.text = totalPriceSt
            /*offer section start*/if (cartModel.isOffer) {
                if (cartModel.offerType.equals("WalletPoint", ignoreCase = true)) {
                    val freeWalletPoint = cartModel.totalFreeWalletPoint
                    val sfreewalletDP = DecimalFormat(Utils.pattern).format(freeWalletPoint)
                    if (freeWalletPoint > 0) {
                        mBinding!!.tvFreeItemQut.text = sfreewalletDP
                    }
                } else {
                    val freeItemQuantity = cartModel.totalFreeItemQty
                    if (freeItemQuantity > 0) {
                        mBinding!!.tvFreeItemQut.text = "" + freeItemQuantity
                    }
                }
            }

            // offer section end
            if (itemQuantity != 0) {
                mBinding!!.itemRowQuantityTv.text = "" + itemQuantity
                mBinding!!.addToCart.visibility = View.GONE
                mBinding!!.addRemoveHolder.visibility = View.VISIBLE
            } else {
                mBinding!!.addToCart.visibility = View.VISIBLE
                mBinding!!.addRemoveHolder.visibility = View.GONE
            }
        } else {
            val itemQuantity = 0
            mBinding!!.itemRowQuantityTv.text = "" + itemQuantity
        }
    }

    private fun itemAddRemove(addItem: Boolean, addBtn: Boolean) {
        try {
            var mFreeItemQuantity = 0
            var mFreeWalletPoint = 0.0
            var addFlag = false
            var flashDealFlag = false
            var flashOfferFlag = false
            var calUnitPrice = 0.0
            var isflashDealUsed = false
            var isPrimeItem = false
            var itemQuantity = mBinding!!.itemRowQuantityTv.text.toString().toInt()
            if (itemListModel!!.isOffer) {
                if (itemListModel!!.offerType != null) {
                    if (itemListModel!!.offerType.equals("FlashDeal", ignoreCase = true)) {
                        val jsonFlashString = SharePrefs.getStringSharedPreferences(
                            applicationContext, SharePrefs.ITEM_FLASH_DEAL_USED_JSON
                        )
                        if (!jsonFlashString.isEmpty()) {
                            val jsonObject = JSONObject(jsonFlashString)
                            if (jsonObject.has(itemListModel!!.itemId.toString())) {
                                if (jsonObject[itemListModel!!.itemId.toString()] == "1") {
                                    isflashDealUsed = true
                                }
                            }
                        }
                        if (itemListModel!!.flashDealMaxQtyPersonCanTake >= itemQuantity + itemListModel!!.minOrderQty && itemListModel!!.offerQtyAvaiable >= itemQuantity + itemListModel!!.minOrderQty) {
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
            }
            if (addItem) {
                //plus btn
                if (!flashDealFlag) {
                    itemQuantity += itemListModel!!.minOrderQty
                    if (itemListModel!!.isItemLimit) {
                        addFlag = setLimit(itemQuantity, true)
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
                    if (itemListModel!!.isItemLimit) {
                        addFlag = setLimit(itemQuantity, false)
                    } else {
                        mBinding!!.itemRowQuantityTv.text = "" + itemQuantity
                        addFlag = true
                    }
                }
            }
            if (addFlag) {
                /*offer section*/
                if (itemListModel!!.isOffer) {
                    val offerMinimumQty = itemListModel!!.offerMinimumQty
                    val customerActive = SharePrefs.getInstance(applicationContext)
                        .getBoolean(SharePrefs.CUST_ACTIVE)
                    when (itemListModel!!.offerType) {
                        "WalletPoint" -> if (customerActive) {
                            if (offerMinimumQty != 0) {
                                if (itemListModel!!.offerWalletPoint != null) {
                                    //event trigger
                                    if (itemQuantity >= offerMinimumQty) {
                                        mFreeWalletPoint = itemListModel!!.offerWalletPoint!!
                                        val calfreeItemQty = itemQuantity / offerMinimumQty
                                        mFreeWalletPoint *= calfreeItemQty.toDouble()
                                        val sfreewalletDP =
                                            DecimalFormat(Utils.pattern).format(mFreeWalletPoint)
                                        if (mFreeWalletPoint > 0) {
                                            mBinding!!.tvFreeItemQut.text = sfreewalletDP
                                        }
                                    } else {
                                        mBinding!!.tvFreeItemQut.text = "0"
                                    }
                                } else {
                                    Toast.makeText(
                                        applicationContext,
                                        MyApplication.getInstance().dbHelper.getString(
                                            R.string.minimum_qty_should_not_be_zero
                                        ),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }

                        "ItemMaster" -> if (customerActive) {
                            if (offerMinimumQty != 0) {
                                if (itemListModel!!.offerFreeItemQuantity != null) {
                                    // event trigger
                                    if (itemQuantity >= offerMinimumQty) {
                                        mFreeItemQuantity =
                                            itemListModel!!.offerFreeItemQuantity!!.toInt()
                                        val calfreeItemQty = itemQuantity / offerMinimumQty
                                        mFreeItemQuantity *= calfreeItemQty
                                        if (mFreeItemQuantity > 0) {
                                            mBinding!!.tvFreeItemQut.text = "" + mFreeItemQuantity
                                        }
                                    } else {
                                        mBinding!!.tvFreeItemQut.text = "0"
                                    }
                                }
                            } else {
                                Toast.makeText(
                                    applicationContext,
                                    MyApplication.getInstance().dbHelper.getString(
                                        R.string.minimum_qty_should_not_be_zero
                                    ),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                        "FlashDeal" -> if (!isflashDealUsed) {
                            val remainingLeft = itemListModel!!.offerQtyAvaiable - itemQuantity
                            if (remainingLeft > 0) {
                                mBinding!!.tvLeftItems.text = "" + remainingLeft
                            } else {
                                mBinding!!.tvLeftItems.text = "0"
                            }
                            flashOfferFlag = true
                        } else {
                            flashOfferFlag = false
                        }

                        else -> {}
                    }
                }
                if (flashOfferFlag) {
                    val price =
                        "<font color=#FF4500>&#8377; " + DecimalFormat(Utils.pattern).format(
                            itemQuantity * itemListModel!!.flashDealSpecialPrice
                        )
                    mBinding!!.tvTotalAmount.text = Html.fromHtml(price)
                    calUnitPrice = itemListModel!!.flashDealSpecialPrice
                } else if (SharePrefs.getInstance(applicationContext)
                        .getBoolean(SharePrefs.IS_PRIME_MEMBER) && itemListModel!!.isPrimeItem
                ) {
                    val price =
                        "<font color=#FF4500>&#8377; " + DecimalFormat("##.##").format(itemQuantity * itemListModel!!.primePrice)
                    mBinding!!.tvTotalAmount.text = Html.fromHtml(price)
                    calUnitPrice = itemListModel!!.primePrice
                    isPrimeItem = true
                } else {
                    val price =
                        "<font color=#FF4500>&#8377; " + DecimalFormat(Utils.pattern).format(
                            itemQuantity * itemListModel!!.unitPrice
                        )
                    mBinding!!.tvTotalAmount.text = Html.fromHtml(price)
                    calUnitPrice = itemListModel!!.unitPrice
                }
                addItemInCart(
                    itemListModel!!.itemId, itemQuantity, calUnitPrice, itemListModel,
                    mFreeItemQuantity, mFreeWalletPoint, isPrimeItem
                )
                RxBus.getInstance().sendEvent(true)
            }

            //Please don't change this condition
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
            var itemlimitqty = 0
            itemlimitqty = itemListModel.billLimitQty
            if (total > 0) {
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

    fun addItemInCart(
        itemId: Int, qty: Int, itemUnitPrice: Double, model: ItemListModel?,
        freeItemQty: Int, totalFreeWalletPoint: Double, isPrimeItem: Boolean
    ): String {
        apiRunning = true
            mBinding!!.ivProgress.visibility = View.VISIBLE
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
                val runnable: Runnable = object : Runnable {
                    override fun run() {
                        handler.removeCallbacks(this)
                        callAddToCartAPI(itemId, qty, itemUnitPrice, isPrimeItem)
                    }
                }
                handler.removeCallbacksAndMessages(null)
                handler.postDelayed(runnable, 1000)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        return ""
    }

    private fun callAddToCartAPI(
        itemId: Int,
        qty: Int,
        itemUnitPrice: Double,
        isPrimeItem: Boolean
    ) {
        viewModel.addItemInCartData.observe(this) {
            when (it) {
                is Response.Loading -> {}
                is Response.Success -> {
                    it.data?.let {
                        if (it["Status"].asBoolean) {
                            mBinding!!.ivProgress.visibility = View.INVISIBLE
                        } else {
                            Utils.setToast(
                                applicationContext, "unable to add cart"
                            )
                        }
                        apiRunning = false
                        if (dialog != null) {
                            dialog!!.cancel()
                        }
                    }
                }

                is Response.Error -> {
                    mBinding!!.ivProgress.visibility = View.INVISIBLE
                    apiRunning = false
                    if (dialog != null) {
                        dialog!!.cancel()
                    }
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
            "Product details Item"
        )
    }

    private fun timeDeference(itemListModel: ItemListModel?) {
        try {
            val updateTimer = Timer()
            val sdf = SimpleDateFormat(Utils.myFormat, Locale.ENGLISH)
            sdf.timeZone = TimeZone.getDefault()
            val currentDate = sdf.parse(itemListModel!!.currentStartTime)
            val endTime = sdf.parse(itemListModel.offerEndTime)
            if (endTime.time > currentDate.time) {
                updateTimer.schedule(object : TimerTask() {
                    override fun run() {
                        try {
                            val myDate = sdf.format(Date())
                            val currentTime = sdf.parse(myDate)
                            val endTime = sdf.parse(itemListModel.offerEndTime)
                            val millse = endTime.time - currentTime.time
                            val mills = Math.abs(millse)
                            runOnUiThread {
                                mBinding!!.tvRemaingSecand.text = "" + String.format(
                                    FORMAT,
                                    TimeUnit.MILLISECONDS.toHours(mills),
                                    TimeUnit.MILLISECONDS.toMinutes(mills) - TimeUnit.HOURS.toMinutes(
                                        TimeUnit.MILLISECONDS.toHours(mills)
                                    ),
                                    TimeUnit.MILLISECONDS.toSeconds(mills) - TimeUnit.MINUTES.toSeconds(
                                        TimeUnit.MILLISECONDS.toMinutes(mills)
                                    )
                                )
                            }
                        } catch (e: Exception) {
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
            MyApplication.getInstance().analyticAddWishList(itemListModel)
        }
    }

    companion object {
        private const val PERMISSION_REQUEST_CODE = 100
    }
}