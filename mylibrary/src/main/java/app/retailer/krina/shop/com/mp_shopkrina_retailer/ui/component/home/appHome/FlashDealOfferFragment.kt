package app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.appHome

import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.api.observe
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.appHome.HomeOfferFlashDealModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.repository.AppRepository
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.response.Response
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.FragmentFlashDealOfferBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.EndFlashDealModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.ItemListModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.SharePrefs
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.HomeActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.LocaleHelper
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Logger
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.RetailerSDKApp
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.RxBus
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.TextUtils
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils
import com.bumptech.glide.Glide
import com.google.gson.JsonObject
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.Picasso
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone
import java.util.concurrent.TimeUnit

class FlashDealOfferFragment : Fragment() {
    var homeActivity = activity as? HomeActivity

    private val TAG = FlashDealOfferFragment::class.java.name
    private lateinit var appCtx: RetailerSDKApp
    private lateinit var mBinding: FragmentFlashDealOfferBinding
    private lateinit var appHomeViewModel: AppHomeViewModel
    private var mFlashDealItemListAdapter: FlashDealItemListAdapter? = null
    private var warehouseId = 0
    private var customerId = 0
    var sSectionId: String? = ""
    var flashDealBackImage: String? = ""
    private var mFlashHome = false
    private val mItemListArrayList = ArrayList<ItemListModel>()
    private var SectionID = "-1"
    private val FORMAT = "%02d:%02d:%02d"
    private val handler = Handler()
    private var mCounter: MyCount? = null
    private var mNonPrimeUserTimer: NonPrimeUserTimer? = null
    private var acceptFlashStartTimeDes: Disposable? = null


    override fun onAttach(_context: Context) {
        super.onAttach(_context)
        homeActivity = _context as HomeActivity
        appCtx = homeActivity!!.application as RetailerSDKApp

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding =
            FragmentFlashDealOfferBinding.inflate(inflater, container, false)
        val appRepository = AppRepository(homeActivity!!.applicationContext)
        appHomeViewModel =
            ViewModelProvider(
                homeActivity!!,
                AppHomeViewModelFactory(appCtx, appRepository)
            )[AppHomeViewModel::class.java]
        homeActivity!!.searchText!!.visibility = View.VISIBLE
        homeActivity!!.rightSideIcon!!.visibility = View.VISIBLE

        // init
        initialization()
        observe(appHomeViewModel.getFlashDealItemData, ::handleFlashDealResult)
        observe(appHomeViewModel.getFlashDealExistsTimeData, ::handleFlashDealExitTimeResult)

        // flash deal offer
        if (arguments != null) {
            if (arguments?.containsKey("isStore") !!&& arguments?.getBoolean("isStore")!!) {
                val subCatId = arguments?.getInt("subCategoryId")
                sSectionId = arguments?.getString("SECTION_ID")
                flashDealBackImage = arguments?.getString("FlashDealBackImage")
                appHomeViewModel.getStoreFlashDeal(
                    customerId,
                    warehouseId,
                    sSectionId,
                    subCatId!!,
                    LocaleHelper.getLanguage(activity),
                    "Flash Fragment"
                )
            } else {
                sSectionId = arguments?.getString("SECTION_ID")
                flashDealBackImage = arguments?.getString("FlashDealBackImage")
                callFlashDealAPI(sSectionId)
                handleStartFlashDeal(sSectionId)
            }
        } else {
            appHomeViewModel.getFlashDealExistsTime(
                customerId,
                warehouseId,
                "Load more flashdeal Screen"
            )
        }
        appHomeViewModel.getFlashDealBannerImage(customerId, warehouseId)
        appHomeViewModel.getFlashDealBannerImageData.observe(homeActivity!!) {
            if (!TextUtils.isNullOrEmpty(it)) {
                Picasso.get().load(it)
                    .placeholder(R.drawable.logo_grey_wide)
                    .error(R.drawable.flash_deal_img)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .into(mBinding.ivFlashDealBanner)
            }
        }
        return mBinding.root
    }

    override fun onResume() {
        super.onResume()
        RetailerSDKApp.getInstance().mFirebaseAnalytics.setCurrentScreen(
            homeActivity!!,
            this.javaClass.simpleName, null
        )
        if (mFlashHome) {
            if (mFlashDealItemListAdapter != null) {
                mFlashDealItemListAdapter!!.notifyDataSetChanged()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (mCounter != null) {
            mCounter!!.cancel()
        }
        if (mNonPrimeUserTimer != null) {
            mNonPrimeUserTimer!!.cancel()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (acceptFlashStartTimeDes != null) {
            acceptFlashStartTimeDes!!.dispose()
        }
    }

    fun initialization() {
        warehouseId = SharePrefs.getInstance(activity).getInt(SharePrefs.WAREHOUSE_ID)
        customerId = SharePrefs.getInstance(activity).getInt(SharePrefs.CUSTOMER_ID)
        mBinding.NoFlashDealAvailable.text =
            RetailerSDKApp.getInstance().dbHelper.getData("No_flash_Deal_available")
        mBinding.tvLeftTimeMsg.text =
            RetailerSDKApp.getInstance().dbHelper.getData("tv_get_ready_to_in")
        mBinding.tvPrimeText.text = RetailerSDKApp.getInstance().dbHelper.getData("text_prime")
        mBinding.tvNoPrimeText.text =
            RetailerSDKApp.getInstance().dbHelper.getData("text_no_prime")
        val rvFlashDealOffer = mBinding.rvFlashDeal
        rvFlashDealOffer.layoutManager = LinearLayoutManager(activity)
        mFlashDealItemListAdapter =
            FlashDealItemListAdapter(
                homeActivity!!,
                mItemListArrayList,
                mItemListArrayList.size,
                flashDealBackImage!!
            )
        rvFlashDealOffer.adapter = mFlashDealItemListAdapter
        homeActivity!!.bottomNavigationView!!.visibility = View.VISIBLE
    }

    private fun callFlashDealAPI(sectionId: String?) {
        appHomeViewModel.getFlashDealItem(
            customerId,
            warehouseId,
            sectionId,
            LocaleHelper.getLanguage(activity),
            "Load More FlashDeal"
        )
    }


    inner class MyCount internal constructor(
        millisInFuture: Long,
        countDownInterval: Long,
        var IsPrimeCustomer: Boolean
    ) : CountDownTimer(millisInFuture, countDownInterval) {
        override fun onFinish() {
            mBinding.tvPrimeUserTime.text = "done!"
            handler.postDelayed(object : Runnable {
                override fun run() {
                    //Do something after 100ms
                    handler.postDelayed(this, 3500)
                    handler.removeCallbacks(this)
                    mBinding.tvPrimeText.visibility = View.GONE
                    mBinding.tvNoPrimeText.visibility = View.GONE
                    mBinding.tvPrimeUserTime.visibility = View.GONE
                    if (IsPrimeCustomer) {
                        mBinding.llFlashdealData.visibility = View.VISIBLE
                        mBinding.llNoRemainingTime.visibility = View.GONE
                        mBinding.llNoFlash.visibility = View.GONE
                        Utils.showProgressDialog(getActivity())
                        //Do something after 100ms
                        callFlashDealAPI(SectionID)
                    }
                }
            }, 3500)
        }

        override fun onTick(millisUntilFinished: Long) {
            // long millis = millisUntilFinished;
            /*(TimeUnit.MILLISECONDS.toDays(millis)) + "Day"+*/
            val mills = Math.abs(millisUntilFinished)
            mBinding.tvPrimeUserTime.text = "" + String.format(
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
    }

    inner class NonPrimeUserTimer internal constructor(
        millisInFuture: Long,
        countDownInterval: Long,
        var IsPrimeCustomer: Boolean
    ) : CountDownTimer(millisInFuture, countDownInterval) {
        private val handler = Handler()
        override fun onFinish() {
            mBinding.tvNonPrimeUserTime.text = "done!"
            handler.postDelayed(object : Runnable {
                override fun run() {
                    //Do something after 100ms
                    handler.postDelayed(this, 3500)
                    handler.removeCallbacks(this)
                    if (!IsPrimeCustomer) {
                        mBinding.llFlashdealData.visibility = View.VISIBLE
                        mBinding.llNoRemainingTime.visibility = View.GONE
                        mBinding.llNoFlash.visibility = View.GONE
                        Utils.showProgressDialog(getActivity())
                        //Do something after 100ms
                        callFlashDealAPI(SectionID)
                    }
                }
            }, 3500)
        }

        override fun onTick(millisUntilFinished: Long) {
            val mills = Math.abs(millisUntilFinished)
            val time = "" + String.format(
                FORMAT,
                TimeUnit.MILLISECONDS.toHours(mills),
                TimeUnit.MILLISECONDS.toMinutes(mills) - TimeUnit.HOURS.toMinutes(
                    TimeUnit.MILLISECONDS.toHours(mills)
                ),
                TimeUnit.MILLISECONDS.toSeconds(mills) - TimeUnit.MINUTES.toSeconds(
                    TimeUnit.MILLISECONDS.toMinutes(mills)
                )
            )
            mBinding.tvNonPrimeUserTime.text = time
        }
    }

    // get accept assigned condition
    private fun handleStartFlashDeal(sectionId: String?) {
        acceptFlashStartTimeDes = RxBus.getInstance().flashDealStartEvent
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { o: EndFlashDealModel ->
                Logger.logD(TAG, "Start Flash deal time")
                if (o.isStatus) {
                    callFlashDealAPI(sectionId)
                    acceptFlashStartTimeDes!!.dispose()
                }
            }
    }

    companion object {
        var myFormat = "yyyy-mm-dd'T'hh:mm:ss"

        @JvmStatic
        fun newInstance(): FlashDealOfferFragment {
            return FlashDealOfferFragment()
        }
    }

    private fun handleFlashDealResult(it: Response<HomeOfferFlashDealModel>) {
        when (it) {
            is Response.Loading -> {
                Utils.showProgressDialog(activity)
            }

            is Response.Success -> {
                it.data?.let {
                    Utils.hideProgressDialog()
                    mBinding.llFlashdealData.visibility = View.VISIBLE
                    mBinding.llNoRemainingTime.visibility = View.GONE
                    mBinding.llNoFlash.visibility = View.GONE
                    mFlashHome = true
                    mItemListArrayList.clear()
                    val itemListModels = it.itemListModels
                    if (itemListModels!!.size != 0) {
                        mFlashDealItemListAdapter!!.setItemListCategory(
                            itemListModels,
                            itemListModels.size
                        )
                    }
                }
            }

            is Response.Error -> {
                Utils.hideProgressDialog()
                mBinding.llFlashdealData.visibility = View.GONE
                mBinding.llNoRemainingTime.visibility = View.GONE
                mBinding.llNoFlash.visibility = View.VISIBLE
            }
        }
    }

    private fun handleFlashDealExitTimeResult(it: Response<JsonObject>) {
        when (it) {
            is Response.Loading -> {
                Utils.showProgressDialog(activity)
            }

            is Response.Success -> {
                it.data?.let {
                    Utils.hideProgressDialog()
                    var primeStartTime: String? = ""
                    var LogoUrl: String? = ""
                    var nonPrimeStartTime: String? = ""
                    val IsPrimeCustomer: Boolean
                    val jsonObject = it
                    if (jsonObject["FlashDealStatus"].asBoolean) {
                        SectionID = jsonObject["SectionID"].asString
                        if (!jsonObject["StartTime"].isJsonNull) {
                            primeStartTime = jsonObject["StartTime"].asString
                        }
                        if (!jsonObject["StartTime"].isJsonNull) {
                            LogoUrl = jsonObject["LogoUrl"].asString
                        }
                        if (!jsonObject["NonPrimeStartTime"].isJsonNull) {
                            nonPrimeStartTime = jsonObject["NonPrimeStartTime"].asString
                        }
                        IsPrimeCustomer = jsonObject["IsPrimeCustomer"].asBoolean
                        if (!TextUtils.isNullOrEmpty(primeStartTime) && !TextUtils.isNullOrEmpty(
                                SectionID
                            )
                        ) {
                            val currMillis = System.currentTimeMillis()
                            val sdf1 = SimpleDateFormat(Utils.myFormat, Locale.getDefault())
                            sdf1.timeZone = TimeZone.getDefault()
                            val endTime = sdf1.parse(primeStartTime)
                            val endEpoch = endTime.time
                            val millse = endEpoch - currMillis
                            mCounter = MyCount(millse, 1000, IsPrimeCustomer)
                            mCounter!!.start()
                            mBinding.llFlashdealData.visibility = View.GONE
                            mBinding.llNoRemainingTime.visibility = View.VISIBLE
                            mBinding.llNoFlash.visibility = View.GONE
                            Glide.with(homeActivity!!).load(LogoUrl).into(mBinding.ivLeftTimeImage)
                        }
                        if (!TextUtils.isNullOrEmpty(nonPrimeStartTime) && !TextUtils.isNullOrEmpty(
                                SectionID
                            )
                        ) {
                            val currMillis = System.currentTimeMillis()
                            val sdf1 = SimpleDateFormat(Utils.myFormat, Locale.getDefault())
                            sdf1.timeZone = TimeZone.getDefault()
                            val endTime = sdf1.parse(nonPrimeStartTime)
                            val endEpoch = endTime.time
                            val millse = endEpoch - currMillis
                            mNonPrimeUserTimer = NonPrimeUserTimer(millse, 1000, IsPrimeCustomer)
                            mNonPrimeUserTimer!!.start()
                            mBinding.llFlashdealData.visibility = View.GONE
                            mBinding.llNoRemainingTime.visibility = View.VISIBLE
                            mBinding.llNoFlash.visibility = View.GONE
                            Glide.with(homeActivity!!).load(LogoUrl).into(mBinding.ivLeftTimeImage)
                        }
                        if (TextUtils.isNullOrEmpty(primeStartTime) && !TextUtils.isNullOrEmpty(
                                SectionID
                            )
                        ) {
                            mBinding.llFlashdealData.visibility = View.VISIBLE
                            mBinding.llNoRemainingTime.visibility = View.GONE
                            mBinding.llNoFlash.visibility = View.GONE
                            Utils.showProgressDialog(getActivity())
                            callFlashDealAPI(SectionID)
                        } else {
                            mBinding.llNoFlash.visibility = View.VISIBLE
                            mBinding.llFlashdealData.visibility = View.GONE
                            mBinding.llNoRemainingTime.visibility = View.GONE
                        }
                    } else {
                        mBinding.llNoFlash.visibility = View.VISIBLE
                        mBinding.llFlashdealData.visibility = View.GONE
                        mBinding.llNoRemainingTime.visibility = View.GONE
                    }
                }
            }

            is Response.Error -> {
                Utils.hideProgressDialog()
            }
        }
    }

}