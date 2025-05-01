package app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home

import android.Manifest
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentSender.SendIntentException
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.text.Html
import android.util.DisplayMetrics
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.animation.AnticipateInterpolator
import android.view.animation.OvershootInterpolator
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.community.FeedActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.api.observe
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.BottomCall
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.CartAddItemModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.HomeMenuHeaderModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.ItemListModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.MurliStoryResponse
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.MurliStoryResponse.MurliStoryPageListBean
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.offer.BillDiscountModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.shoppingCart.CheckoutCartResponse
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.shoppingCart.ShopingCartItemDetailsResponse
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.repository.AppRepository
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.response.Response
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ActivityHomeBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ActivityMobileSignUpBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.firebase.FirebaseLanguageFetch
import app.retailer.krina.shop.com.mp_shopkrina_retailer.interfaces.OnButtonClick
import app.retailer.krina.shop.com.mp_shopkrina_retailer.interfaces.OnItemClick
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.activity.DirectUdharActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.activity.EditProfileActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.activity.MyWalletActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.activity.ScaleUpActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.activity.WebViewActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.dialog.ProfileDialogFragment
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.direct.TradeActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.gullak.mygullak.MyGullakActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.murli.ShowMurli
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.murli.StoryActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.murli.WriteAudioFile
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.myIssues.AddIssueActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.AnalyticPost
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.EndPointPref
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.SharePrefs
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.StoryBordSharePrefs
import app.retailer.krina.shop.com.mp_shopkrina_retailer.showcaseviewlib.DismissType
import app.retailer.krina.shop.com.mp_shopkrina_retailer.showcaseviewlib.Gravity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.showcaseviewlib.GuideView
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.auth.CustomerAddressActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.allBrands.AllBrandFragItemList
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.allBrands.BrandOrderFragment
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.appHome.FlashDealOfferFragment
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.appHome.HomeFragment
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.category.HomeCategoryFragment
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.offer.OfferDetailFragment
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.offer.OffersFragment
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.searchItem.SearchItemFragment
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.notification.NotificationActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.shoppingCart.ShoppingCartActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.AnimatorUtils
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Constant
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.LocaleHelper
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.MarshmallowPermissions
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.MyApplication
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.TextUtils
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.JsonObject
import com.nabinbhandari.android.permissions.PermissionHandler
import com.nabinbhandari.android.permissions.Permissions
import com.squareup.picasso.Picasso
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.text.DateFormat
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HomeActivity : AppCompatActivity(), View.OnClickListener, OnButtonClick {
    @JvmField
    var mBinding: ActivityHomeBinding? = null
    private lateinit var viewModel: HomeViewModel

    var tvItemCount: TextView? = null
    var mDrawerLayout: DrawerLayout? = null

    @JvmField
    var bottomNavigationView: BottomNavigationView? = null

    @JvmField
    var searchText: LinearLayout? = null

    @JvmField
    var spLayout: LinearLayout? = null

    @JvmField
    var rightSideIcon: LinearLayout? = null
    var titletxt: LinearLayout? = null
    var lerCateSelected: LinearLayout? = null

    @JvmField
    var SearchIcon: ImageView? = null
    var imgAngleArrow: ImageView? = null
    var notification: ImageView? = null
    var tvCateSelected: TextView? = null

    @JvmField
    var topToolbarTitle: TextView? = null
    private var listDataHeader: MutableList<HomeMenuHeaderModel>? = null
    private var ratingList: ArrayList<BottomCall>? = null
    private var utils: Utils? = null
    private var showMurli: ShowMurli? = null
    private var murliCustomDialog: ProgressDialog? = null
    private var dialog: ProgressDialog? = null
    private var mGuideView: GuideView? = null
    private var builder: GuideView.Builder? = null
    private var bottomCallAdapter: BottomCallAdapter? = null

    @JvmField
    var custId = 0
    var wId = 0
    var lastItemId = 0
    private var skCode: String? = null
    private var lang = ""
    private var finboxtype: String? = null
    private var isMurliClicked = false
    private var doubleBackToExitPressedOnce = false
    private var apiRunning = false
    private var notificationAudioFileURL: String? = ""
    private var sectionName: String? = null
    private var onItemClick: OnItemClick? = null
    private val FLUTTER_ENGINE_ID = "my_flutter_engine"
    private val CHANNEL = "com.ScaleUP"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(mBinding!!.root)
        val appRepository = AppRepository(applicationContext)
        viewModel = ViewModelProvider(
            this,
            HomeViewModelFactory(application, appRepository)
        )[HomeViewModel::class.java]
        setSupportActionBar(mBinding!!.toolbarH.homeToolbar)
        if (intent.extras != null) {
            if (intent.hasExtra("sectionName")) {
                sectionName = intent.getStringExtra("sectionName")
            }
            if (intent.hasExtra("notificationCategory")) finboxtype =
                intent.getStringExtra("notificationCategory")
        }
        // init
        initialization()
        if (finboxtype != null && (finboxtype.equals(
                "FinBox",
                ignoreCase = true
            ) || finboxtype.equals("CreditLine", ignoreCase = true))
        ) {
            val args = Bundle()
            if (finboxtype.equals("CreditLine", ignoreCase = true)) args.putString(
                "type",
                "CreditLine"
            ) else args.putString("type", "FinBox")
            pushFragments(HomeFragment.newInstance(""), false, true, args)
            MyApplication.getInstance().updateAnalytics("Finbox_click")
        }
        if (SharePrefs.getInstance(this)
                .getBoolean(SharePrefs.IS_UDHAAR_OVERDUE) && SharePrefs.getInstance(this)
                .getBoolean(SharePrefs.IS_UDHAAR_POPUP_OPEN_IN_FIRST_TIME)
        ) {
            SharePrefs.getInstance(this)
                .putBoolean(SharePrefs.IS_UDHAAR_POPUP_OPEN_IN_FIRST_TIME, false)
            viewModel.getUdhaarOverDue(custId, lang)
            viewModel.getUdhaarOverDueData.observe(this) {
                try {
                    val msg = it["Msg"].asString
                    val isOrder = it["IsOrder"].asBoolean
                    if (!TextUtils.isNullOrEmpty(msg)) {
                        SharePrefs.getInstance(this@HomeActivity)
                            .putBoolean(SharePrefs.IS_UDHAAR_ORDER, isOrder)
                        checkUdhaarOverDue(msg)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        if (SharePrefs.getInstance(this).getBoolean(SharePrefs.IS_SELLER_AVAIL)) {
            mBinding!!.toolbarH.liStoreH.visibility = View.VISIBLE
        } else {
            mBinding!!.toolbarH.liStoreH.visibility = View.GONE
        }
        if (!EndPointPref.getInstance(applicationContext).getBoolean(EndPointPref.IS_SHOW_SELLER)) {
            mBinding!!.toolbarH.liStoreH.visibility = View.GONE
        }
        permission()
        observe(viewModel.scaleUpLeadInitiateData, ::handleScaleUpLeadInitiateResult)
        observe(viewModel.getMurliAudioForMobileData, ::handleMurliAudioForMobileResult)
        observe(viewModel.downloadFileWithUrlData, ::handleMurliDownloadFileWithUrlResult)
        observe(viewModel.getMurliPublishedStoryData, ::handleMurliPublishedStoryResult)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        utils!!.addShortcut(this)
        inAppUpdate()
        //Notification
        if (intent.extras != null) {
            if (intent.hasExtra("notificationCategory") && intent.getStringExtra("notificationCategory")
                    .equals("Flash Deal", ignoreCase = true)
            ) {
                pushFragments(FlashDealOfferFragment.newInstance(), true, true, null)
                MyApplication.getInstance().updateAnalytics("flashdeal_notification_click")
            } else if (intent.hasExtra("notificationCategory") && intent.getStringExtra("notificationCategory")
                    .equals("offer", ignoreCase = true)
            ) {
                pushFragments(OffersFragment.newInstance(), false, true, null)
                MyApplication.getInstance().updateAnalytics("offer_notification_click")
            } else if (intent.hasExtra("notificationCategory") && intent.getStringExtra("notificationCategory")
                    .equals("category", ignoreCase = true)
            ) {
                pushFragments(HomeCategoryFragment(), true, true, null)
            } else if (intent.hasExtra("notificationCategory") && intent.getStringExtra("notificationCategory")
                    .equals("search", ignoreCase = true)
            ) {
                pushFragments(SearchItemFragment.newInstance(), true, true, null)
            } else if (intent.hasExtra("notificationCategory") && intent.getStringExtra("notificationCategory")
                    .equals("brand", ignoreCase = true)
            ) {
                pushFragments(AllBrandFragItemList.newInstance(), true, true, null)
            } else if (intent.hasExtra("notificationCategory") && intent.getStringExtra("notificationCategory")
                    .equals("brandItem", ignoreCase = true)
            ) {
                val args = Bundle()
                args.putInt(
                    "subCatId",
                    if (intent.hasExtra("subCategory")) intent.getIntExtra("subCategory", 0) else 1
                )
                args.putInt(
                    "Categoryid",
                    if (intent.hasExtra("categoryId")) intent.getIntExtra("categoryId", 0) else 1
                )
                pushFragments(BrandOrderFragment.newInstance(), true, true, null)
            } else if (intent.hasExtra("notificationCategory") && intent.getStringExtra("notificationCategory")
                    .equals("Rating", ignoreCase = true)
            ) {
                callAPIs()
            } else {
                if (intent.hasExtra("audioFileName") && intent.getStringExtra("audioFileName") != null) {
                    notificationAudioFileURL = intent.getStringExtra("audioFileName")
                    mBinding!!.toolbarH.btnMurli1.callOnClick()
                }
            }
            if (intent.extras != null && intent.hasExtra("notificationId")) {
                val notificationId = intent.extras!!.getInt("notificationId")
                MyApplication.getInstance().notificationView(notificationId)
                intent.extras!!.clear()
            }
            if (intent.hasExtra("OfferAddMore")) {
                val b = intent.extras
                val isOfferAddMore = b!!.getBoolean("OfferAddMore", false)
                if (isOfferAddMore) {
                    val discountModel = b.getSerializable("OfferModel") as BillDiscountModel?
                    val args = Bundle()
                    args.putSerializable("model", discountModel)
                    pushFragments(OfferDetailFragment(), false, true, args)
                }
            }
        }
        if (SharePrefs.getInstance(applicationContext).getBoolean(SharePrefs.IS_FETCH_LANGUAGE)) {
            FirebaseLanguageFetch(applicationContext).fetchLanguage()
        }
        if (!SharePrefs.getInstance(applicationContext)
                .getBoolean(SharePrefs.IS_SHOW_LANG_DIALOG)
        ) {
            showChangeLangDialog()
        }
        callAPIs()
        MyApplication.getInstance().noteRepository.cartValue.observe(this) { totalAmt: Double? ->
            if (totalAmt != null && totalAmt > 0) {
                tvItemCount!!.visibility = View.VISIBLE
                val sTotalAmount =
                    "<font color=#FFFFFF>&#8377; " + DecimalFormat("##.##").format(totalAmt) + "</font>"
                tvItemCount!!.text = Html.fromHtml(sTotalAmount)
            } else {
                tvItemCount!!.visibility = View.GONE
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        if (intent.extras != null) {
            if (intent.hasExtra("notificationCategory") && intent.getStringExtra("notificationCategory")
                    .equals("Flash Deal", ignoreCase = true)
            ) {
                pushFragments(FlashDealOfferFragment.newInstance(), true, true, null)
            } else if (intent.hasExtra("notificationCategory") && intent.getStringExtra("notificationCategory")
                    .equals("offer", ignoreCase = true)
            ) {
                pushFragments(OffersFragment.newInstance(), false, true, null)
            } else if (intent.hasExtra("notificationCategory") && intent.getStringExtra("notificationCategory")
                    .equals("Rating", ignoreCase = true)
            ) {
                callAPIs()
            } else {
                if (intent.hasExtra("audioFileName") && intent.getStringExtra("audioFileName") != null) {
                    notificationAudioFileURL = intent.getStringExtra("audioFileName")
                    mBinding!!.toolbarH.btnMurli1.callOnClick()
                }
            }
            if (intent.extras != null && intent.hasExtra("notificationId")) {
                val notificationId = intent.extras!!.getInt("notificationId")
                MyApplication.getInstance().notificationView(notificationId)
                intent.extras!!.clear()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        isMurliClicked = false
        if (showMurli != null) {
            showMurli!!.onResume()
        }
        bottomNavigationView!!.menu.getItem(0).isChecked = true
        mBinding!!.userName.text =
            SharePrefs.getInstance(applicationContext).getString(SharePrefs.CUSTOMER_NAME)
        val vectorDrawable = AppCompatResources.getDrawable(
            applicationContext, R.drawable.profile_round
        )
        if (!TextUtils.isNullOrEmpty(
                SharePrefs.getInstance(
                    applicationContext
                ).getString(SharePrefs.USER_PROFILE_IMAGE)
            )
        ) {
            Picasso.get().load(
                Constant.BASE_URL_PROFILE + SharePrefs.getInstance(
                    applicationContext
                ).getString(SharePrefs.USER_PROFILE_IMAGE)
            ).placeholder(
                vectorDrawable!!
            ).error(vectorDrawable).into(mBinding!!.profileImageNav)
        } else {
            mBinding!!.profileImageNav.setImageDrawable(vectorDrawable)
        }
        if (SharePrefs.getInstance(applicationContext).getInt(SharePrefs.NOTIFICATION_COUNT) > 0) {
            mBinding!!.toolbarH.tvNotificationCount.visibility = View.VISIBLE
            mBinding!!.toolbarH.tvNotificationCount.text = if (SharePrefs.getInstance(this)
                    .getInt(SharePrefs.NOTIFICATION_COUNT) > 99
            ) "99+" else "" + SharePrefs.getInstance(
                applicationContext
            ).getInt(SharePrefs.NOTIFICATION_COUNT)
        } else {
            mBinding!!.toolbarH.tvNotificationCount.visibility = View.GONE
        }
        mBinding!!.txtWalletPT.text =
            SharePrefs.getInstance(applicationContext).getString(SharePrefs.CURRENT_WALLET_POINT)
        mBinding!!.tvGullakAmt.text =
            "₹" + SharePrefs.getInstance(applicationContext).getString(SharePrefs.GULLAK_BALANCE)
        mBinding!!.tvRtgsAmt.text =
            "₹" + SharePrefs.getInstance(applicationContext).getString(SharePrefs.RTGS_BAL)
        callGullakAPI()
        if (MyApplication.getInstance().noteRepository.cartCount == 0) {
            tvItemCount!!.visibility = View.GONE
        }
    }

    override fun onPause() {
        super.onPause()
        if (showMurli != null) {
            showMurli!!.onPause()
        }
        isMurliClicked = false
        mBinding!!.drawer.closeDrawers()
    }

    override fun onBackPressed() {
        if (mDrawerLayout!!.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout!!.closeDrawer(GravityCompat.START)
        } else {
            if (supportFragmentManager.backStackEntryCount <= 1) {
                if (doubleBackToExitPressedOnce) {
                    super.onBackPressed()
                    finishAffinity()
                    return
                }
                doubleBackToExitPressedOnce = true
                Snackbar.make(
                    mDrawerLayout!!,
                    MyApplication.getInstance().dbHelper.getString(R.string.tap_again_to_exit),
                    Snackbar.LENGTH_SHORT
                ).show()
                Handler(Looper.getMainLooper()).postDelayed(
                    { doubleBackToExitPressedOnce = false },
                    2000
                )
            } else {
                super.onBackPressed()
                System.gc()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        isMurliClicked = false
        //   murliImageObserver.dispose()
    }

    override fun onClick(v: View) {
        mDrawerLayout!!.closeDrawers()
        when (v.id) {
            R.id.btnMurli -> {
                MyApplication.getInstance().updateAnalytics("murli_menu_click")
                showMenu()
            }

            R.id.menu_layout, R.id.fab -> hideMenu()
            R.id.btn_murli1 -> {
                MyApplication.getInstance().updateAnalytics("murli_click")
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.READ_MEDIA_IMAGES
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    showMurli()
                } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU && MarshmallowPermissions.isPermissionAllowed(
                        this,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ) && MarshmallowPermissions.isPermissionAllowed(
                        this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ) && MarshmallowPermissions.isPermissionAllowed(
                        this,
                        Manifest.permission.RECORD_AUDIO
                    )
                ) {
                    showMurli()
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(
                                Manifest.permission.RECORD_AUDIO,
                                Manifest.permission.READ_MEDIA_IMAGES
                            ),
                            0
                        )
                    } else requestStoragePermission()
                }
            }

            R.id.btn_story -> {
                MyApplication.getInstance().updateAnalytics("story_click")
                mBinding!!.toolbarH.btnStory.isEnabled = false
                mBinding!!.toolbarH.btnStory.isClickable = false
                hideMenu()
                viewModel.getMurliPublishedStory(custId, wId)
                mBinding!!.toolbarH.btnStory.isEnabled = true
                mBinding!!.toolbarH.btnStory.isClickable = true
            }

            R.id.fabClose -> {
                MyApplication.getInstance().updateAnalytics("murli_close_click")
                mBinding!!.toolbarH.btnMurli1.callOnClick()
                isMurliClicked = false
            }

            R.id.frag_search_edt, R.id.searchText -> {
                MyApplication.getInstance().updateAnalytics("search_click")
                pushFragments(SearchItemFragment.newInstance(), true, true, null)
            }

            R.id.fabAddIssue -> {
                MyApplication.getInstance().updateAnalytics("app_home_issue_click")
                startActivity(Intent(applicationContext, AddIssueActivity::class.java))
            }

            R.id.notification -> {
                MyApplication.getInstance().updateAnalytics("notification_bell_click")
                startActivity(
                    Intent(applicationContext, NotificationActivity::class.java).putExtra(
                        "Notification",
                        false
                    )
                )
            }

            R.id.menuButton -> mDrawerLayout!!.openDrawer(GravityCompat.START)
            R.id.ivBrands -> {
                MyApplication.getInstance().updateAnalytics("brand_click")
                pushFragments(AllBrandFragItemList.newInstance(), true, true, null)
            }

            R.id.llayout -> {
                if (TextUtils.isNullOrEmpty(
                        SharePrefs.getInstance(
                            applicationContext
                        ).getString(SharePrefs.CLUSTER_ID)
                    )
                ) {
                    val phone = SharePrefs.getInstance(applicationContext).getString(SharePrefs.COMPANY_CONTACT)
                    val msg = MyApplication.getInstance().dbHelper.getString(R.string.msg_cluster_null)+phone
                    AlertDialog.Builder(this@HomeActivity).setTitle(
                        MyApplication.getInstance().dbHelper.getString(
                            R.string.alert
                        )
                    ).setMessage(msg).setNegativeButton(getString(R.string.ok), null).show()
                } else {
                    MyApplication.getInstance().updateAnalytics("drawer_profile_click")
                    startActivity(Intent(applicationContext, EditProfileActivity::class.java))
                    Utils.fadeTransaction(this)
                }

            }

            R.id.liWallet -> {
                MyApplication.getInstance().updateAnalytics("wallet_click")
                startActivity(Intent(applicationContext, MyWalletActivity::class.java))
            }

            R.id.liGullak -> {
                MyApplication.getInstance().updateAnalytics("gullak_click")
                SharePrefs.getInstance(applicationContext)
                    .putBoolean(SharePrefs.IS_GULLAK_BAL, false)
                callGullakAPI()
                startActivity(
                    Intent(
                        applicationContext,
                        MyGullakActivity::class.java
                    ).putExtra("screen", 1)
                )
            }

            R.id.liRtgs -> {
                MyApplication.getInstance().updateAnalytics("rtgs_menu_click")
                SharePrefs.getInstance(applicationContext)
                    .putBoolean(SharePrefs.IS_GULLAK_BAL, false)
                callGullakAPI()
                startActivity(
                    Intent(applicationContext, MyGullakActivity::class.java)
                        .putExtra("screen", 2)
                )
            }

            R.id.llSellerStore -> {
                val analyticPost = AnalyticPost()
                analyticPost.source = "home"
                MyApplication.getInstance().updateAnalytics("community_click", analyticPost)
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
                )
                Utils.fadeTransaction(this)
            }

            else -> {}
        }
        Utils.fadeTransaction(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 101 && resultCode != RESULT_OK) {
            onBackPressed()
        } else if (requestCode == 333) {
            // SharePrefs.getInstance(activity).putBoolean(SharePrefs.IS_REQUIRED_LOCATION, false);
            println(" onActivityResult call")
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

    // rating-agent+delivery
    override fun onButtonClick(pos: Int, clicked: Boolean) {
        try {
            if (ratingList!!.size > 0) {
                ratingList!!.removeAt(pos)
                bottomCallAdapter!!.notifyItemRemoved(pos)
                bottomCallAdapter!!.notifyItemRangeChanged(pos, ratingList!!.size)
                bottomCallAdapter!!.notifyDataSetChanged()
            }
            if (ratingList!!.size == 0) {
                mBinding!!.toolbarH.indicator.visibility = View.GONE
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @SuppressLint("RestrictedApi")
    private fun initialization() {
        utils = Utils(this)
        lang = LocaleHelper.getLanguage(this)
        custId = SharePrefs.getInstance(this).getInt(SharePrefs.CUSTOMER_ID)
        wId = SharePrefs.getInstance(this).getInt(SharePrefs.WAREHOUSE_ID)
        skCode = SharePrefs.getInstance(this).getString(SharePrefs.SK_CODE)
        if (custId == 0) {
            MyApplication.getInstance().logout(this)
        }
        mDrawerLayout = mBinding!!.drawer
        searchText = mBinding!!.toolbarH.searchText
        SearchIcon = mBinding!!.toolbarH.searchIcon
        lerCateSelected = mBinding!!.toolbarH.liCategory
        tvCateSelected = mBinding!!.toolbarH.tvCatSelected
        imgAngleArrow = mBinding!!.toolbarH.angleArrow
        spLayout = mBinding!!.toolbarH.spLayout
        rightSideIcon = mBinding!!.toolbarH.rightSideIcon
        titletxt = mBinding!!.toolbarH.titletxt
        notification = mBinding!!.toolbarH.notification
        mBinding!!.toolbarH.tvBrandStore.text =
            MyApplication.getInstance().dbHelper.getString(R.string.brand_store)
        mBinding!!.toolbarH.tvSellerStore.text =
            MyApplication.getInstance().dbHelper.getString(R.string.seller_store)
        mBinding!!.toolbarH.fragSearchEdt.hint =
            MyApplication.getInstance().dbHelper.getString(R.string.hint_search)
        mBinding!!.toolbarH.tvAdviceT.text =
            MyApplication.getInstance().dbHelper.getString(R.string.advice)
        mBinding!!.toolbarH.tvStoryT.text =
            MyApplication.getInstance().dbHelper.getString(R.string.story)
        mBinding!!.tvVersion.text = "App Version "
        mBinding!!.tvEditProfile.text =
            MyApplication.getInstance().dbHelper.getString(R.string.edit)
        mBinding!!.tvWalletPointH.text =
            MyApplication.getInstance().dbHelper.getString(R.string.your_walet_total)
        mBinding!!.tvGullakH.text =
            MyApplication.getInstance().dbHelper.getString(R.string.gullak_balance)
        mBinding!!.tvRtgsH.text =
            MyApplication.getInstance().dbHelper.getString(R.string.rtgs_balance)
        topToolbarTitle = mBinding!!.toolbarH.titleTop
        bottomNavigationView = mBinding!!.toolbarH.navigation
        bottomNavigationView!!.itemIconTintList = null
        mBinding!!.toolbarH.fragSearchEdt.setOnClickListener(this)
        searchText!!.setOnClickListener(this)
        mBinding!!.toolbarH.fabAddIssue.setOnClickListener(this)
        mBinding!!.toolbarH.ivBrands.setOnClickListener(this)
        mBinding!!.toolbarH.notification.setOnClickListener(this)
        mBinding!!.toolbarH.menuButton.setOnClickListener(this)
        mBinding!!.liWallet.setOnClickListener(this)
        mBinding!!.liGullak.setOnClickListener(this)
        mBinding!!.liRtgs.setOnClickListener(this)
        mBinding!!.toolbarH.llSellerStore.setOnClickListener(this)
        titletxt!!.visibility = View.GONE
        searchText!!.visibility = View.VISIBLE
        rightSideIcon!!.visibility = View.VISIBLE
        prepareListData()
        mBinding!!.llayout.setOnClickListener(this)
        mBinding!!.rvMenu.adapter = HomeMenuAdapter(this, listDataHeader!!)
        mBinding!!.toolbarH.menuLayout.setOnClickListener(this)
        mBinding!!.toolbarH.btnMurli.setOnClickListener(this)
        mBinding!!.toolbarH.fab.setOnClickListener(this)
        mBinding!!.toolbarH.fabClose.setOnClickListener(this)
        mBinding!!.toolbarH.btnMurli1.setOnClickListener(this)
        mBinding!!.toolbarH.btnStory.setOnClickListener(this)
        mBinding!!.toolbarH.fab.hide()
        setFullWidth()
        mBinding!!.userName.text =
            SharePrefs.getInstance(applicationContext).getString(SharePrefs.CUSTOMER_NAME)
        if (EndPointPref.getInstance(applicationContext).getBoolean(EndPointPref.Show_Chat_Bot)) {
            mBinding!!.toolbarH.fabAddIssue.visibility = View.VISIBLE
        } else {
            mBinding!!.toolbarH.fabAddIssue.visibility = View.GONE
        }
        if (SharePrefs.getInstance(applicationContext).getBoolean(SharePrefs.IS_PRIME_MEMBER)) {
            mBinding!!.userSkcode.text = skCode + " (" + SharePrefs.getInstance(applicationContext)
                .getString(SharePrefs.PRIME_NAME) + ")"
            mBinding!!.toolbarH.ivPrime.visibility = View.VISIBLE
            mBinding!!.userName.setTextColor(resources.getColor(R.color.prime))
            mBinding!!.ivPrime.visibility = View.VISIBLE
        } else {
            mBinding!!.userName.setTextColor(resources.getColor(R.color.Black))
            mBinding!!.userSkcode.text = skCode
            mBinding!!.toolbarH.ivPrime.visibility = View.GONE
            mBinding!!.ivPrime.visibility = View.GONE
        }
        val criticalInfoMissMSG = SharePrefs.getInstance(applicationContext)
            .getString(SharePrefs.CRITICAL_INFO_MISSING_MSG)
        if (SharePrefs.getInstance(this)
                .getString(SharePrefs.CUSTOMER_VERIFY) != "Full Verified" && !TextUtils.isNullOrEmpty(
                criticalInfoMissMSG
            )
        ) {
            if (SharePrefs.getInstance(applicationContext).getBoolean(SharePrefs.DOC_DAY_EMPTY)) {
                val simpleDateFormat: DateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
                val date = Date()
                val currentDate = simpleDateFormat.format(date)
                if (SharePrefs.getInstance(applicationContext)
                        .getString(SharePrefs.PROFILE_LAST_DATE_SHOW) != currentDate
                ) {
                    ProfileDialogFragment.newInstance(criticalInfoMissMSG, currentDate).show(
                        supportFragmentManager, "a"
                    )
                    SharePrefs.getInstance(applicationContext)
                        .putString(SharePrefs.PROFILE_LAST_DATE_SHOW, currentDate)
                }
            }
        }
        if (SharePrefs.getInstance(applicationContext)
                .getString(SharePrefs.LATITUDE) == "0.0" || SharePrefs.getInstance(
                applicationContext
            ).getString(SharePrefs.LONGITUDE) == "0.0" || SharePrefs.getInstance(applicationContext)
                .getString(SharePrefs.LATITUDE) == "0" || SharePrefs.getInstance(applicationContext)
                .getString(SharePrefs.LONGITUDE) == "0"
        ) {
            showLocationDialog()
        }
        // default home
        if (!SharePrefs.getInstance(this).getBoolean(SharePrefs.IS_WAREHOUSE_AVAIL)) {
            mBinding!!.liWallet.visibility = View.GONE
            mBinding!!.liRtgs.visibility = View.GONE
            mBinding!!.liGullak.visibility = View.GONE
            mBinding!!.toolbarH.navigation.visibility = View.INVISIBLE
            mBinding!!.toolbarH.ivBrands.visibility = View.INVISIBLE
            mBinding!!.toolbarH.fragSearchEdt.isEnabled = false
            mBinding!!.toolbarH.searchText.isEnabled = false
            val view = layoutInflater.inflate(
                R.layout.layout_not_available,
                mBinding!!.toolbarH.content,
                true
            )
            val phone = SharePrefs.getInstance(this).getString(SharePrefs.COMPANY_CONTACT)
            view.findViewById<View>(R.id.btnCall).setOnClickListener { v: View? ->
                startActivity(
                    Intent(
                        Intent.ACTION_DIAL, Uri.parse(
                            "tel:$phone"
                        )
                    )
                )
            }
        } else {
            pushFragments(HomeFragment.newInstance(sectionName), false, true, null)
        }
        val bottomNavigationMenuView =
            bottomNavigationView!!.getChildAt(0) as BottomNavigationMenuView
        val v = bottomNavigationMenuView.getChildAt(4)
        val itemView = v as BottomNavigationItemView
        val badge = LayoutInflater.from(this)
            .inflate(R.layout.home_screen_count, bottomNavigationMenuView, false)
        tvItemCount = badge.findViewById(R.id.notification_badge)
        tvItemCount!!.setVisibility(View.GONE)
        itemView.addView(badge)
        val menu = bottomNavigationView!!.menu
        val home = menu.findItem(R.id.home)
        val category = menu.findItem(R.id.category)
        val trade = menu.findItem(R.id.trade)
        val offers = menu.findItem(R.id.offers)
        val basket = menu.findItem(R.id.basket)
        home.setTitle(MyApplication.getInstance().dbHelper.getString(R.string.title_home))
        category.setTitle(MyApplication.getInstance().dbHelper.getString(R.string.title_categories))
        trade.setTitle(MyApplication.getInstance().dbHelper.getString(R.string.title_direct))
        offers.setTitle(MyApplication.getInstance().dbHelper.getString(R.string.title_offers))
        basket.setTitle(MyApplication.getInstance().dbHelper.getString(R.string.checkout))
        bottomNavigationView!!.setOnNavigationItemSelectedListener { item: MenuItem ->
            var selectedFragment: Fragment? = null
            when (item.itemId) {
                R.id.home -> {
                    MyApplication.getInstance().updateAnalytics("nav_home_click")
                    if (SharePrefs.getInstance(this)
                            .getBoolean(SharePrefs.IS_WAREHOUSE_AVAIL)
                    ) selectedFragment = HomeFragment.newInstance(sectionName)
                }

                R.id.trade -> {
                    MyApplication.getInstance().updateAnalytics("community_click")
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
                    )
                }

                R.id.category -> {
                    MyApplication.getInstance().updateAnalytics("nav_category_click")
                    selectedFragment = HomeCategoryFragment()
                }

                R.id.offers -> {
                    MyApplication.getInstance().updateAnalytics("nav_offer_click")
                    selectedFragment = OffersFragment.newInstance()
                }

                R.id.basket -> {
                    MyApplication.getInstance().updateAnalytics("nav_checkout_click")
                    if (!apiRunning && !handler.hasMessages(0)) {
                        startActivity(Intent(applicationContext, ShoppingCartActivity::class.java))
                        Utils.fadeTransaction(this)
                    } else {
                        dialog = ProgressDialog(this)
                        dialog!!.setMessage("Please wait")
                        dialog!!.show()
                        dialog!!.setOnCancelListener { dialog1: DialogInterface? ->
                            dialog = null
                            mBinding!!.toolbarH.navigation.findViewById<View>(R.id.basket)
                                .performClick()
                        }
                    }
                }

                else -> {}
            }
            if (selectedFragment != null) {
                pushFragments(selectedFragment, false, true, null)
            }
            true
        }
    }

    fun appStoryView() {
        builder = GuideView.Builder(this)
            .setTitle(MyApplication.getInstance().dbHelper.getString(R.string.menu_home))
            .setContentText(MyApplication.getInstance().dbHelper.getString(R.string.menu_home_detail))
            .setGravity(
                Gravity.center
            ).setDismissType(DismissType.anywhere).setTargetView(
                mBinding!!.toolbarH.menuButton
            ).setGuideListener { view: View ->
                when (view.id) {
                    R.id.menuButton -> builder!!.setTitle(
                        MyApplication.getInstance().dbHelper.getString(
                            R.string.brand_home
                        )
                    )
                        .setContentText(MyApplication.getInstance().dbHelper.getString(R.string.brand_home_detail))
                        .setTargetView(
                            mBinding!!.toolbarH.ivBrands
                        ).build()

                    R.id.ivBrands -> builder!!.setTitle(
                        MyApplication.getInstance().dbHelper.getString(
                            R.string.notification_home
                        )
                    )
                        .setContentText(MyApplication.getInstance().dbHelper.getString(R.string.notification_home_detail))
                        .setTargetView(
                            mBinding!!.toolbarH.notification
                        ).build()

                    R.id.notification -> {
                        StoryBordSharePrefs.getInstance(applicationContext)
                            .putBoolean(StoryBordSharePrefs.HOMEPAGE, true)
                        return@setGuideListener
                    }
                }
                mGuideView = builder!!.build()
                mGuideView!!.show()
            }
        mGuideView = builder!!.build()
        mGuideView!!.show()
    }

    private fun updateCartAllValue(mShoppingCart: ShopingCartItemDetailsResponse) {
        // save cart data here
        MyApplication.getInstance().noteRepository.addToCart(mShoppingCart.shoppingCartItemDcs)
    }

    fun postImageObserver() {
        if (!TextUtils.isNullOrEmpty(notificationAudioFileURL)) {
            try {
                hideMurliProgressDialog()
                isMurliClicked = true
                val url = notificationAudioFileURL
                Constant.AUDIO_FILE_NAME = url!!.substring(
                    url.lastIndexOf("/") + 1
                )
                var file = File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                        .toString() + Constant.AUDIO_FOLDER + Constant.AUDIO_FILE_NAME
                )
                if (file.exists()) {
                    file = File(
                        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                            .toString() + Constant.AUDIO_FOLDER + Constant.AUDIO_FILE_NAME
                    )
                    if (file.exists()) {
                        //  if (isMurliClicked) {
                        mBinding!!.toolbarH.btnMurli1.isEnabled = true
                        mBinding!!.toolbarH.btnMurli1.isClickable = true
                        closeMurli()
                        showMurli = ShowMurli(this@HomeActivity, mBinding)
                        //  }
                    } else {
                        Utils.setToast(
                            applicationContext,
                            MyApplication.getInstance().dbHelper.getString(R.string.no_offer_available_h)
                        )
                        mBinding!!.toolbarH.btnMurli1.isEnabled = true
                        mBinding!!.toolbarH.btnMurli1.isClickable = true
                    }
                } else {
                    Constant.AUDIO_FILE_NAME = url.substring(
                        url.lastIndexOf("/") + 1
                    )
                    if (isMurliClicked) {
                        hideMurliProgressDialog()
                    }
                    viewModel.downloadFileWithUrl(url)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else {
            viewModel.getMurliAudioForMobile(custId, wId, "Home")
        }
    }

    fun shareOnWhatsApp(isWhatsApp: Boolean) {
        try {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.setType("text/plain")
            if (isWhatsApp) {
                shareIntent.setPackage("com.whatsapp")
            }
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "SK Retailer App")
            var shareMessage = """
                
                ${MyApplication.getInstance().dbHelper.getString(R.string.share_msg)}
                
                
                """.trimIndent()
            shareMessage = shareMessage + "play.google.com/store/apps/details?id=" + packageName
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
            startActivity(Intent.createChooser(shareIntent, "choose one"))
        } catch (e: Exception) {
            e.printStackTrace()
        }
        MyApplication.getInstance().updateAnalyticShare(javaClass.simpleName, "App Share WhatsApp")
    }

    private fun setFullWidth() {
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val params = mBinding!!.navView.layoutParams as DrawerLayout.LayoutParams
        params.width = (displayMetrics.widthPixels / 1.2).toInt()
        mBinding!!.navView.layoutParams = params
    }

    // API's
    private fun callAPIs() {
        observe(viewModel.shoppingCartResponseData, ::handleShoppingCartResult)
        observe(viewModel.appHomeBottomData, ::handleBottomCallResult)
        if (!SharePrefs.getInstance(applicationContext)
                .getBoolean(SharePrefs.CART_AMOUNT_API_CALL)
        ) {
            viewModel.getCustomerCart(custId, wId, lang, "HomePage")
        }
        viewModel.getAppHomeBottomCall(custId)

        viewModel.getWalletPointNew(
            custId,
            0,
            "Navigation Drawer"
        )
        viewModel.getWalletPointData.observe(this) {
            try {
                val totalEarnPoint = it.totalEarnPoint
                val walletPoint = DecimalFormat("##.##").format(totalEarnPoint)
                SharePrefs.getInstance(applicationContext)
                    .putString(SharePrefs.CURRENT_WALLET_POINT, "" + walletPoint)
                mBinding!!.txtWalletPT.text = walletPoint
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun callGullakAPI() {
        if (!SharePrefs.getInstance(applicationContext).getBoolean(SharePrefs.IS_GULLAK_BAL)) {
            viewModel.getGullakBalance(custId)
            viewModel.getGullakBalanceData.observe(this) {
                try {
                    if (it != null) {
                        val num = it["Amount"].asBigDecimal
                        val balance = DecimalFormat("#.##").format(num)
                        SharePrefs.getInstance(applicationContext)
                            .putString(SharePrefs.GULLAK_BALANCE, balance)
                        mBinding!!.tvGullakAmt.text =
                            "₹" + SharePrefs.getInstance(applicationContext)
                                .getString(SharePrefs.GULLAK_BALANCE)
                        SharePrefs.getInstance(applicationContext)
                            .putBoolean(SharePrefs.IS_GULLAK_BAL, true)
                    }
                    viewModel.getRTGSBalance(custId)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            viewModel.getRTGSBalanceData.observe(this) {
                try {
                    if (it != null) {
                        val rtgsBalance = DecimalFormat("#.##").format(it)
                        SharePrefs.getInstance(applicationContext)
                            .putString(SharePrefs.RTGS_BAL, rtgsBalance)
                        mBinding!!.tvRtgsAmt.text = "₹$rtgsBalance"
                    } else mBinding!!.tvRtgsAmt.text = "₹0"
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    fun callVAtmApi() {
        Utils.showProgressDialog(this)
        viewModel.getVATMUrl(custId, wId)
        viewModel.getVATMUrlData.observe(this) {
            it.let {
                Utils.hideProgressDialog()
                if (!TextUtils.isNullOrEmpty(it)) {
                    startActivity(
                        Intent(
                            applicationContext,
                            WebViewActivity::class.java
                        ).putExtra("url", it)
                    )
                }
            }
        }
    }

    fun getNotifyItems(warehouseId: Int, itemNumber: String?) {
        viewModel.getNotifyItems(custId, warehouseId, itemNumber!!)
        viewModel.getNotifyItemsData.observe(this) {
            if (it) {
                Snackbar.make(
                    mDrawerLayout!!,
                    MyApplication.getInstance().dbHelper.getString(R.string.txt_Notify_msg),
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun inAppUpdate() {
        val appUpdateManager = AppUpdateManagerFactory.create(this)
        val appUpdateInfoTask = appUpdateManager.appUpdateInfo
        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo: AppUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE && appUpdateInfo.isUpdateTypeAllowed(
                    AppUpdateType.FLEXIBLE
                )
            ) {
                try {
                    appUpdateManager.startUpdateFlowForResult(
                        appUpdateInfo,
                        AppUpdateType.IMMEDIATE,
                        this,
                        101
                    )
                } catch (e: SendIntentException) {
                    e.printStackTrace()
                }
            }
        }
    }

    fun callLeadApi(url: String) {
        var url = url
        Utils.showProgressDialog(this)
        if (url != null) {
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
                SharePrefs.getInstance(applicationContext).getString(SharePrefs.SK_CODE)
            )
            url = url.replace(
                "[WAREHOUSEID]",
                "" + SharePrefs.getInstance(applicationContext).getInt(SharePrefs.WAREHOUSE_ID)
            )
            url = url.replace("[LANG]", LocaleHelper.getLanguage(applicationContext))
            url = url.replace(
                "[MOBILE]",
                SharePrefs.getInstance(applicationContext).getString(SharePrefs.MOBILE_NUMBER)
            )
        }
        viewModel.generateLead(url)
        viewModel.generateLeadData.observe(this) {
            Utils.hideProgressDialog()
            val isSuccess = it["Result"].asBoolean
            if (isSuccess) {
                val URL = it["Data"].asString
                startActivity(
                    Intent(
                        applicationContext,
                        DirectUdharActivity::class.java
                    ).putExtra("url", URL)
                )
            } else {
                val msg = it["Msg"].asString
                AlertDialog.Builder(this@HomeActivity)
                    .setTitle(MyApplication.getInstance().dbHelper.getString(R.string.alert))
                    .setMessage(msg).setNegativeButton(getString(R.string.ok), null)
                    .show()
            }
        }
    }

    fun callScaleUpApi() {
        viewModel.scaleUpLeadInitiate("", custId, true)
    }

    fun callScaleUpApiUsingUrl(url: String?) {
        viewModel.scaleUpLeadInitiate(url, custId, false)
    }

    // manage back stake
    fun pushFragments(
        fragment: Fragment?,
        shouldAnimate: Boolean,
        shouldAdd: Boolean,
        args: Bundle?
    ) {
        try {
            if (fragment != null) {
                val manager = supportFragmentManager
                val fragmentPopped = manager.popBackStackImmediate(fragment.javaClass.simpleName, 0)
                if (!fragmentPopped) { //fragment not in back stack, create it.
                    val ft = manager.beginTransaction()
                    ft.setCustomAnimations(
                        R.anim.fade_in,
                        R.anim.fade_out,
                        R.anim.fade_in,
                        R.anim.fade_out
                    )
                    if (args != null) {
                        fragment.arguments = args
                    }
                    if (shouldAdd) ft.addToBackStack(fragment.javaClass.simpleName)
                    if (shouldAnimate) {
                        ft.setCustomAnimations(
                            R.anim.fade_in,
                            R.anim.fade_out,
                            R.anim.fade_in,
                            R.anim.fade_out
                        )
                    }
                    ft.replace(R.id.content, fragment, fragment.javaClass.simpleName)
                    ft.commit()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // drawer menu
    private fun prepareListData() {
        listDataHeader = ArrayList()
        // Adding child data
        val moreToolList: MutableList<String> = ArrayList()
        if (SharePrefs.getInstance(applicationContext).getBoolean(SharePrefs.IS_SHOW_HISAB)) {
            moreToolList.add(MyApplication.getInstance().dbHelper.getString(R.string.hishab_kitab_wudu))
        }
        moreToolList.add(MyApplication.getInstance().dbHelper.getString(R.string.kisan_dan))
        if (SharePrefs.getInstance(applicationContext)
                .getBoolean(SharePrefs.IS_SHOW_RETURN_ORDER)
        ) moreToolList.add(MyApplication.getInstance().dbHelper.getString(R.string.title_activity_return_order))
        if (SharePrefs.getInstance(applicationContext).getBoolean(SharePrefs.IS_SHOW_VATM)) {
            moreToolList.add(MyApplication.getInstance().dbHelper.getString(R.string.v_atm))
        }
        moreToolList.add(MyApplication.getInstance().dbHelper.getString(R.string.title_business_card))
        moreToolList.add(MyApplication.getInstance().dbHelper.getString(R.string.request_brand))
        if (SharePrefs.getInstance(applicationContext).getBoolean(SharePrefs.IS_PRIME_ACTIVE)) {
            moreToolList.add(
                SharePrefs.getInstance(applicationContext).getString(SharePrefs.PRIME_NAME)
            )
        }
        moreToolList.add(MyApplication.getInstance().dbHelper.getString(R.string.feedback))
        moreToolList.add(MyApplication.getInstance().dbHelper.getString(R.string.title_game))
        moreToolList.add(MyApplication.getInstance().dbHelper.getString(R.string.txt_My_Favourite))
        moreToolList.add(MyApplication.getInstance().dbHelper.getString(R.string.txt_My_Dream))
        moreToolList.add(MyApplication.getInstance().dbHelper.getString(R.string.share))

        // Header data
        if (SharePrefs.getInstance(this).getBoolean(SharePrefs.IS_WAREHOUSE_AVAIL)) {
            if (EndPointPref.getInstance(this).getBoolean(EndPointPref.IS_SCALEUP))
                listDataHeader!!.add(
                    HomeMenuHeaderModel(
                        MyApplication.getInstance().dbHelper.getString(
                            R.string.scale_up
                        ), false
                    )
                )
            if (SharePrefs.getInstance(this)
                    .getBoolean(SharePrefs.IS_DIRECT_UDHAR)
            ) listDataHeader!!.add(
                HomeMenuHeaderModel(
                    MyApplication.getInstance().dbHelper.getString(
                        R.string.direct_udhar
                    ), false
                )
            )
            listDataHeader!!.add(
                HomeMenuHeaderModel(
                    MyApplication.getInstance().dbHelper.getString(R.string.myOrder),
                    null
                )
            )
            listDataHeader!!.add(
                HomeMenuHeaderModel(
                    MyApplication.getInstance().dbHelper.getString(R.string.my_target),
                    null
                )
            )
            listDataHeader!!.add(
                HomeMenuHeaderModel(
                    MyApplication.getInstance().dbHelper.getString(R.string.my_ledger),
                    null
                )
            )
            listDataHeader!!.add(
                HomeMenuHeaderModel(
                    MyApplication.getInstance().dbHelper.getString(R.string.my_agents),
                    null
                )
            )

            listDataHeader!!.add(
                HomeMenuHeaderModel(
                    MyApplication.getInstance().dbHelper.getString(R.string.more_tool),
                    moreToolList
                )
            )
        }
        listDataHeader!!.add(
            HomeMenuHeaderModel(
                MyApplication.getInstance().dbHelper.getString(R.string.contact),
                null
            )
        )
        if (SharePrefs.getInstance(this).getBoolean(SharePrefs.IS_WAREHOUSE_AVAIL))
            listDataHeader!!.add(
                HomeMenuHeaderModel(
                    MyApplication.getInstance().dbHelper.getString(R.string.refer_and_earn),
                    null
                )
            )
        listDataHeader!!.add(
            HomeMenuHeaderModel(
                MyApplication.getInstance().dbHelper.getString(R.string.setting),
                null
            )
        )
        if (SharePrefs.getInstance(this)
                .getBoolean(SharePrefs.IS_SHOW_TICKET_MENU)
        ) listDataHeader!!.add(
            HomeMenuHeaderModel(
                MyApplication.getInstance().dbHelper.getString(R.string.help),
                null
            )
        )
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
                    if (handler.hasMessages(lastItemId)) handler.sendEmptyMessage(lastItemId)
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
                MyApplication.getInstance().dbHelper.getString(R.string.internet_connection)
            )
            onItemClick.onItemClick(0, false)
        }
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
                        if (it["Status"].asBoolean) {
                            onItemClick!!.onItemClick(0, true)
                        } else {
                            Utils.setToast(
                                applicationContext,
                                MyApplication.getInstance().dbHelper.getString(R.string.unable_to_add_cart)
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
                    onItemClick!!.onItemClick(0, false)
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
                lang,
                isPrimeItem,
                false
            ),
            "Home Item"
        )

        /*  commonClassForAPI!!.postAddCartItem(
              object : DisposableObserver<JsonObject?>() {
                  override fun onNext(`object`: JsonObject) {
                      if (`object`["Status"].asBoolean) {
                          onItemClick!!.onItemClick(0, true)
                      } else {
                          Utils.setToast(
                              applicationContext,
                              MyApplication.getInstance().dbHelper.getString(R.string.unable_to_add_cart)
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
                      onItemClick!!.onItemClick(0, false)
                      apiRunning = false
                      if (dialog != null) {
                          dialog!!.cancel()
                      }
                  }

                  override fun onComplete() {}
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
              "Home Item"
          )*/
    }

    private fun requestStoragePermission() {
        val permissions: Array<String>
        permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.READ_MEDIA_IMAGES,
                Manifest.permission.RECORD_AUDIO
            )
        } else {
            arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO
            )
        }
        Permissions.check(this, permissions, null, null, object : PermissionHandler() {
            override fun onGranted() {}
            override fun onDenied(context: Context, deniedPermissions: ArrayList<String>) {
                super.onDenied(context, deniedPermissions)
            }
        })
    }

    private fun permission() {
        val permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arrayOf(
                Manifest.permission.POST_NOTIFICATIONS,
                Manifest.permission.CAMERA,
                Manifest.permission.READ_MEDIA_IMAGES,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.WRITE_CONTACTS
            )
        } else {
            arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.WRITE_CONTACTS
            )
        }
        Permissions.check(this, permissions, null, null, object : PermissionHandler() {
            override fun onGranted() {}
            override fun onDenied(context: Context, deniedPermissions: ArrayList<String>) {
                super.onDenied(context, deniedPermissions)
            }
        })
    }

    private fun showMenu() {
        mBinding!!.toolbarH.fab.show()
        mBinding!!.toolbarH.btnMurli.visibility = View.INVISIBLE
        mBinding!!.toolbarH.menuLayout.visibility = View.VISIBLE
        val animList: MutableList<Animator> = ArrayList()
        var i = 0
        val len = mBinding!!.toolbarH.arcLayout.childCount
        while (i < len) {
            animList.add(createShowItemAnimator(mBinding!!.toolbarH.arcLayout.getChildAt(i)))
            i++
        }
        val animSet = AnimatorSet()
        animSet.setDuration(400)
        animSet.interpolator = OvershootInterpolator()
        animSet.playTogether(animList)
        animSet.start()
    }

    private fun hideMenu() {
        val animList: MutableList<Animator> = ArrayList()
        for (i in mBinding!!.toolbarH.arcLayout.childCount - 1 downTo 0) {
            animList.add(createHideItemAnimator(mBinding!!.toolbarH.arcLayout.getChildAt(i)))
        }
        val animSet = AnimatorSet()
        animSet.setDuration(400)
        animSet.interpolator = AnticipateInterpolator()
        animSet.playTogether(animList)
        animSet.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                mBinding!!.toolbarH.btnMurli.visibility = View.VISIBLE
                mBinding!!.toolbarH.fab.hide()
                mBinding!!.toolbarH.menuLayout.visibility = View.INVISIBLE
            }
        })
        animSet.start()
    }

    private fun createShowItemAnimator(item: View): Animator {
        val dx = mBinding!!.toolbarH.fab.x - item.x
        val dy = mBinding!!.toolbarH.fab.y - item.y
        item.rotation = 0f
        item.translationX = dx
        item.translationY = dy
        return ObjectAnimator.ofPropertyValuesHolder(
            item,
            AnimatorUtils.rotation(0f, 720f),
            AnimatorUtils.translationX(dx, 0f),
            AnimatorUtils.translationY(dy, 0f)
        )
    }

    private fun createHideItemAnimator(item: View): Animator {
        val dx = mBinding!!.toolbarH.fab.x - item.x
        val dy = mBinding!!.toolbarH.fab.y - item.y
        val anim: Animator = ObjectAnimator.ofPropertyValuesHolder(
            item,
            AnimatorUtils.rotation(720f, 0f),
            AnimatorUtils.translationX(0f, dx),
            AnimatorUtils.translationY(0f, dy)
        )
        anim.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                item.translationX = 0f
                item.translationY = 0f
            }
        })
        return anim
    }

    fun openMurli(fileDownloaded: Boolean) {
        if (fileDownloaded) {
            closeMurli()
            showMurli = ShowMurli(this, mBinding)
        }
    }

    fun closeMurli() {
        mBinding!!.toolbarH.fabClose.hide()
        mBinding!!.toolbarH.liMurli.murliSpeakingView.visibility = View.INVISIBLE
        if (showMurli != null) {
            showMurli!!.onDestroy()
        }
        showMurli = null
    }

    fun showMurliProgressDialog(activity: Activity) {
        if (murliCustomDialog != null) {
            murliCustomDialog = null
        }
        murliCustomDialog = ProgressDialog(activity)
        murliCustomDialog!!.setMessage("Please wait")
        if (!murliCustomDialog!!.isShowing && !activity.isFinishing) {
            murliCustomDialog!!.show()
        }
        murliCustomDialog!!.setOnKeyListener { arg0: DialogInterface?, keyCode: Int, event: KeyEvent? ->
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                mBinding!!.toolbarH.btnMurli1.isEnabled = true
                mBinding!!.toolbarH.btnMurli1.isClickable = true
                murliCustomDialog!!.dismiss()
            }
            true
        }
    }

    fun hideMurliProgressDialog() {
        if (murliCustomDialog != null && murliCustomDialog!!.isShowing) {
            murliCustomDialog!!.dismiss()
        }
    }

    private fun showMurli() {
        isMurliClicked = true
        hideMenu()
        if (mBinding!!.toolbarH.liMurli.root.visibility == View.VISIBLE) {
            mBinding!!.toolbarH.liMurli.root.visibility = View.INVISIBLE
            closeMurli()
            mBinding!!.toolbarH.btnMurli1.isEnabled = true
            mBinding!!.toolbarH.btnMurli1.isClickable = true
        } else {
            mBinding!!.toolbarH.btnMurli1.isEnabled = false
            mBinding!!.toolbarH.btnMurli1.isClickable = false
            if (isMurliClicked) {
                showMurliProgressDialog(this)
            }
            postImageObserver()
        }
    }

    // dialog
    private fun showLocationDialog() {
        val dialog = Dialog(this)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.dialog_location_info)
        dialog.setCancelable(false)
        val tvTitle = dialog.findViewById<TextView>(R.id.pd_title)
        val okBtn = dialog.findViewById<Button>(R.id.ok_btn)
        tvTitle.text =
            MyApplication.getInstance().dbHelper.getString(R.string.location_address_not_updated)
        okBtn.setOnClickListener { v: View? ->
            dialog.dismiss()
            val intent = Intent(applicationContext, CustomerAddressActivity::class.java)
            intent.putExtra("REDIRECT_FLAG", 3)
            intent.putExtra(
                "cityName",
                SharePrefs.getInstance(this).getString(SharePrefs.CITY_NAME)
            )
            startActivity(intent)
            Utils.leftTransaction(this)
        }
        dialog.window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        dialog.show()
        // update analytic
        MyApplication.getInstance().updateAnalytics("locationDialog")
    }

    private fun showChangeLangDialog() {
        val dialog = Dialog(this)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.dialog_change_language)
        dialog.setCanceledOnTouchOutside(false)
        val tvTitle = dialog.findViewById<TextView>(R.id.tvTitle)
        val tvDesc = dialog.findViewById<TextView>(R.id.tvDesc)
        tvTitle.text = MyApplication.getInstance().dbHelper.getString(R.string.select_language)
        tvDesc.text =
            MyApplication.getInstance().dbHelper.getString(R.string.if_want_to_change_lang)
        val radioHindi = dialog.findViewById<RadioButton>(R.id.radioHindi)
        val radioEnglish = dialog.findViewById<RadioButton>(R.id.radioEnglish)
        val btnContinue = dialog.findViewById<Button>(R.id.btnContinue)
        btnContinue.setOnClickListener { v: View? ->
            dialog.dismiss()
            var isChanged = false
            if (LocaleHelper.getLanguage(applicationContext) == "en" && !radioEnglish.isChecked) {
                SharePrefs.getInstance(applicationContext)
                    .putString(SharePrefs.SELECTED_LANGUAGE, radioHindi.text.toString())
                isChanged = true
            } else if (LocaleHelper.getLanguage(applicationContext) == "hi" && !radioHindi.isChecked) {
                SharePrefs.getInstance(applicationContext)
                    .putString(SharePrefs.SELECTED_LANGUAGE, radioEnglish.text.toString())
                isChanged = true
            }
            if (isChanged) {
                MyApplication.getInstance().clearLangData()
                val database = FirebaseDatabase.getInstance()
                val language = database.reference
                language.addChildEventListener(object : ChildEventListener {
                    override fun onChildAdded(
                        snapshot: DataSnapshot,
                        previousChildName: String?
                    ) {
                        dialog.dismiss()
                        Utils.showProgressDialog(this@HomeActivity)
                        MyApplication.getInstance().dbHelper.deleteAndUpdateTable(snapshot)
                    }

                    override fun onChildChanged(
                        snapshot: DataSnapshot,
                        previousChildName: String?
                    ) {
                    }

                    override fun onChildRemoved(snapshot: DataSnapshot) {}
                    override fun onChildMoved(
                        snapshot: DataSnapshot,
                        previousChildName: String?
                    ) {
                    }

                    override fun onCancelled(error: DatabaseError) {}
                })
            }
        }
        if (LocaleHelper.getLanguage(applicationContext) == "en") {
            radioEnglish.isChecked = true
        } else {
            radioHindi.isChecked = true
        }
        SharePrefs.getInstance(applicationContext)
            .putBoolean(SharePrefs.IS_SHOW_LANG_DIALOG, true)
        dialog.window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.show()
    }

    @SuppressLint("HandlerLeak")
    private val handler: Handler = object : Handler(Looper.myLooper()!!) {
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
    /* private val murliImageObserver: DisposableObserver<JsonObject> =
         object : DisposableObserver<JsonObject>() {
             override fun onNext(`object`: JsonObject) {
                 if (`object`.has("Images") && !`object`["Images"].isJsonNull) {
                     val jsonArray = `object`.getAsJsonArray("Images")
                     if (jsonArray != null) {
                         val url1 = jsonArray[0].asJsonObject["ImagePath"].asString
                         Constant.IMAGE_FILE_NAME1 = url1.substring(url1.lastIndexOf("/") + 1)
                         val file = File(
                             Environment.getExternalStorageDirectory()
                                 .toString() + Constant.IMAGE_FOLDER + Constant.IMAGE_FILE_NAME1
                         )
                         val url2 = jsonArray[1].asJsonObject["ImagePath"].asString
                         Constant.IMAGE_FILE_NAME2 = url2.substring(url2.lastIndexOf("/") + 1)
                         val url3 = jsonArray[2].asJsonObject["ImagePath"].asString
                         Constant.IMAGE_FILE_NAME3 = url3.substring(url3.lastIndexOf("/") + 1)
                         if (!file.exists()) {
                             commonClassForAPI!!.downloadFileWithUrl(
                                 CreateImageFile(
                                     this@HomeActivity,
                                     Constant.IMAGE_FILE_NAME1
                                 ).imageFileObserver, url1
                             )
                             commonClassForAPI!!.downloadFileWithUrl(
                                 CreateImageFile(
                                     this@HomeActivity,
                                     Constant.IMAGE_FILE_NAME2
                                 ).imageFileObserver, url2
                             )
                             commonClassForAPI!!.downloadFileWithUrl(
                                 CreateImageFile(
                                     this@HomeActivity,
                                     Constant.IMAGE_FILE_NAME3
                                 ).imageFileObserver, url3
                             )
                         } else {
                             postImageObserver()
                         }
                     }
                 } else {
                     try {
                         val dir = File(
                             Environment.getExternalStorageDirectory()
                                 .toString() + Constant.IMAGE_FOLDER
                         )
                         if (dir.isDirectory) {
                             val children = dir.list()
                             for (child in children) {
                                 File(dir, child).delete()
                             }
                         }
                         postImageObserver()
                     } catch (e: Exception) {
                         e.printStackTrace()
                     }
                 }
             }

             override fun onError(e: Throwable) {
                 e.printStackTrace()
                 postImageObserver()
                 hideMurliProgressDialog()
                 Toast.makeText(
                     this@HomeActivity,
                     MyApplication.getInstance().dbHelper.getString(R.string.please_try_again_later),
                     Toast.LENGTH_SHORT
                 ).show()
                 mBinding!!.toolbarH.btnMurli1.isEnabled = true
                 mBinding!!.toolbarH.btnMurli1.isClickable = true
             }

             override fun onComplete() {
                 dispose()
             }
         }*/


    private fun checkUdhaarOverDue(textMsg: String) {
        val dialog = BottomSheetDialog(this, R.style.BottomTheme)
        dialog.setContentView(R.layout.dialog_check_udahr_overdue)
        dialog.setCanceledOnTouchOutside(true)
        val tvMsg = dialog.findViewById<TextView>(R.id.tvMsg)
        val imClose = dialog.findViewById<ImageView>(R.id.im_close)
        val btnUdharPayNow = dialog.findViewById<Button>(R.id.btnUdharPayNow)
        tvMsg!!.text = textMsg
        btnUdharPayNow!!.setOnClickListener { view: View? ->
            callLeadApi(EndPointPref.getInstance(this).baseUrl + "/api/Udhar/GenerateLead?CustomerId=[CustomerId]")
            dialog.dismiss()
        }
        imClose!!.setOnClickListener { view: View? -> dialog.dismiss() }
        dialog.show()
    }

    private fun handleShoppingCartResult(it: Response<CheckoutCartResponse>) {
        when (it) {
            is Response.Loading -> {}
            is Response.Success -> {
                it.data?.let {
                    if (it.status) {
                        val mShopingCart = it.shoppingCartItemDetailsResponse
                        if (mShopingCart != null && mShopingCart.shoppingCartItemDcs != null) {
                            if (mShopingCart.shoppingCartItemDcs.size != 0) {
                                updateCartAllValue(mShopingCart)
                                SharePrefs.getInstance(this@HomeActivity)
                                    .putBoolean(SharePrefs.CART_AMOUNT_API_CALL, true)
                                if (mShopingCart.cartTotalAmt + mShopingCart.totalDiscountAmt > 0) {
                                    tvItemCount!!.visibility = View.VISIBLE
                                    val sTotalAmount =
                                        "<font color=#FFFFFF>&#8377; " + DecimalFormat("##.##").format(
                                            mShopingCart.cartTotalAmt + mShopingCart.totalDiscountAmt
                                        ) + "</font>"
                                    tvItemCount!!.text = Html.fromHtml(sTotalAmount)
                                } else {
                                    tvItemCount!!.visibility = View.GONE
                                    MyApplication.getInstance().noteRepository.truncateCart()
                                }
                            } else {
                                tvItemCount!!.visibility = View.GONE
                                MyApplication.getInstance().noteRepository.truncateCart()
                            }
                        } else {
                            tvItemCount!!.visibility = View.GONE
                            MyApplication.getInstance().noteRepository.truncateCart()
                        }
                    } else {
                        MyApplication.getInstance().clearCartData()
                    }

                }
            }

            is Response.Error -> {
            }
        }
    }

    private fun handleBottomCallResult(it: Response<ArrayList<BottomCall>>) {
        when (it) {
            is Response.Loading -> {}
            is Response.Success -> {
                it.data?.let {
                    if (it.size != 0) {
                        ratingList = it
                        bottomCallAdapter =
                            BottomCallAdapter(this@HomeActivity, ratingList, viewModel)
                        mBinding!!.toolbarH.rvBottomCall.adapter = bottomCallAdapter
                        mBinding!!.toolbarH.indicator.setViewPager(mBinding!!.toolbarH.rvBottomCall)
                        if (ratingList!!.size == 1) {
                            mBinding!!.toolbarH.indicator.visibility = View.GONE
                        }
                    }
                }
            }

            is Response.Error -> {
            }
        }
    }

    private fun handleScaleUpLeadInitiateResult(it: Response<JsonObject>) {
        when (it) {
            is Response.Loading -> {
                Utils.showProgressDialog(this)
            }

            is Response.Success -> {
                it.data?.let {
                    Utils.hideProgressDialog()
                    val isSuccess = it["status"].asBoolean
                    if (isSuccess) {
                        val Company: String = it.get("Company").getAsString()
                        val Product: String = it.get("Product").getAsString()
                        val MobileNo: String = it.get("MobileNo").getAsString()
                        val url: String = it.get("response").getAsString()
                        val baseUrl: String = it.get("BaiseUrl").getAsString()


                        /* if (Product.equalsIgnoreCase("BusinessLoan")) {
                            startActivity(new Intent(getApplicationContext(), ScaleUpActivity.class)
                                    .putExtra("url", url));
                        } else {
                            startActivity(
                                    FlutterActivity
                                            .withCachedEngine(FLUTTER_ENGINE_ID)
                                            .build(getApplicationContext())
                            );
                        }*/


                        /* if (Product.equalsIgnoreCase("BusinessLoan")) {
                            startActivity(new Intent(getApplicationContext(), ScaleUpActivity.class)
                                    .putExtra("url", url));
                        } else {
                            startActivity(
                                    FlutterActivity
                                            .withCachedEngine(FLUTTER_ENGINE_ID)
                                            .build(getApplicationContext())
                            );
                        }*/
                        if (EndPointPref.getInstance(applicationContext)
                                .getBoolean(EndPointPref.IS_SCALE_UP_SDK)
                        ) {
                            if (Product.equals("BusinessLoan", ignoreCase = true)) {
                                startActivity(
                                    Intent(applicationContext, ScaleUpActivity::class.java)
                                        .putExtra("url", url)
                                )
                            } else {
//                                flutterEngine = FlutterEngine(applicationContext)
//                                flutterEngine!!.dartExecutor.executeDartEntrypoint(
//                                    DartExecutor.DartEntrypoint.createDefault()
//                                )
//
//                                methodChannel = MethodChannel(
//                                    flutterEngine!!.dartExecutor.binaryMessenger,
//                                    CHANNEL
//                                )
//
//                                FlutterEngineCache
//                                    .getInstance()
//                                    .put(FLUTTER_ENGINE_ID, flutterEngine)

                                val json = JSONObject()
                                try {
                                    json.put("mobileNumber", MobileNo)
                                    json.put("companyID", Company)
                                    json.put("productID", Product)
                                    json.put("isPayNow", false)
                                    json.put("baseUrl", baseUrl)
                                } catch (e: JSONException) {
                                    e.printStackTrace()
                                }
//                                methodChannel!!.invokeMethod("ScaleUP", json.toString())
//                                startActivity(
//                                    FlutterActivity
//                                        .withCachedEngine(FLUTTER_ENGINE_ID)
//                                        .build(applicationContext)
//                                )
                            }
                        } else {
                            startActivity(
                                Intent(applicationContext, ScaleUpActivity::class.java)
                                    .putExtra("url", url)
                            )
                        }
                    } else {
                        val msg = it["message"].asString
                        AlertDialog.Builder(this@HomeActivity)
                            .setTitle(MyApplication.getInstance().dbHelper.getString(R.string.alert))
                            .setMessage(msg).setNegativeButton(getString(R.string.ok), null)
                            .show()
                    }
                }
            }

            is Response.Error -> {
                Utils.hideProgressDialog()
                Utils.setToast(applicationContext, it.errorMesssage.toString())
            }
        }
    }

    private fun handleMurliAudioForMobileResult(it: Response<JsonObject>) {
        when (it) {
            is Response.Loading -> {}

            is Response.Success -> {
                hideMurliProgressDialog()
                it.data?.let {
                    try {
                        if (it.has("Mp3url") && !it["Mp3url"].isJsonNull) {
                            val url = it["Mp3url"].asString
                            Constant.AUDIO_FILE_NAME = url.substring(url.lastIndexOf("/") + 1)
                            var file = File(
                                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                                    .toString() + Constant.AUDIO_FOLDER + Constant.AUDIO_FILE_NAME
                            )
                            if (file.exists()) {
                                file = File(
                                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                                        .toString() + Constant.AUDIO_FOLDER + Constant.AUDIO_FILE_NAME
                                )
                                if (file.exists()) {
                                    if (isMurliClicked) {
                                        mBinding!!.toolbarH.btnMurli1.isEnabled = true
                                        mBinding!!.toolbarH.btnMurli1.isClickable = true
                                        closeMurli()
                                        showMurli = ShowMurli(this@HomeActivity, mBinding)
                                    } else {
                                    }
                                } else {
                                    Utils.setToast(
                                        applicationContext,
                                        MyApplication.getInstance().dbHelper.getString(R.string.no_offer_available)
                                    )
                                    mBinding!!.toolbarH.btnMurli1.isEnabled = true
                                    mBinding!!.toolbarH.btnMurli1.isClickable = true
                                }
                            } else {
                                Constant.AUDIO_FILE_NAME = url.substring(url.lastIndexOf("/") + 1)
                                if (isMurliClicked) {
                                    hideMurliProgressDialog()
                                }
                                viewModel.downloadFileWithUrl(url)
                            }
                        } else {
                            mBinding!!.toolbarH.btnMurli1.isEnabled = true
                            mBinding!!.toolbarH.btnMurli1.isClickable = true
                            Toast.makeText(
                                applicationContext,
                                MyApplication.getInstance().dbHelper.getString(R.string.no_offer_available),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }

            is Response.Error -> {
                hideMurliProgressDialog()
                Utils.setToast(
                    applicationContext,
                    MyApplication.getInstance().dbHelper.getString(R.string.please_try_again_later)
                )
                mBinding!!.toolbarH.btnMurli1.isEnabled = true
                mBinding!!.toolbarH.btnMurli1.isClickable = true
            }
        }
    }

    private fun handleMurliDownloadFileWithUrlResult(it: Response<ResponseBody>) {
        when (it) {
            is Response.Loading -> {}

            is Response.Success -> {
                it.data?.let {
                    try {
                        Utils.hideProgressDialog()
                        if (Utils.getAvailableSpaceInMB() > 200) {
                            WriteAudioFile(
                                this@HomeActivity,
                                it
                            ).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
                        } else {
                            Utils.setToast(
                                baseContext,
                                "You do not have sufficient space available!"
                            )
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }

            is Response.Error -> {}
        }
    }

    private fun handleMurliPublishedStoryResult(it: Response<MurliStoryResponse>) {
        when (it) {
            is Response.Loading -> {
                Utils.showProgressDialog(this)
            }

            is Response.Success -> {
                it.data?.let {
                    try {
                        Utils.hideProgressDialog()
                        if (it.murliStoryPageList != null && it.murliStoryPageList!!.size > 0) {
                            it.murliStoryPageList!!.add(
                                MurliStoryPageListBean(
                                    it.murliStoryPageList!!.size + 1,
                                    "",
                                    it.murliStoryPageList!!.size + 1,
                                    it.murliStoryPageList!!.size + 1
                                )
                            )
                            startActivity(
                                Intent(
                                    applicationContext,
                                    StoryActivity::class.java
                                ).putExtra("list", it.murliStoryPageList)
                            )
                            Utils.leftTransaction(this@HomeActivity)
                        } else {
                            Utils.setToast(
                                baseContext,
                                MyApplication.getInstance().dbHelper.getString(R.string.no_story_available)
                            )
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }

            is Response.Error -> {
                Utils.hideProgressDialog()
                Utils.setToast(
                    baseContext,
                    MyApplication.getInstance().dbHelper.getString(R.string.no_story_available)
                )
            }
        }
    }

}