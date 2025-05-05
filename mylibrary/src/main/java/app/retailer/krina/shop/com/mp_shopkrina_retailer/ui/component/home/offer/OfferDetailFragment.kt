package app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.offer

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.NestedScrollView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.api.CommonClassForAPI
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.api.observe
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.ItemListModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.offer.BillDiscountModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.subCategory.SubSubCategoriesModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.shoppingCart.CheckoutCartResponse
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.repository.AppRepository
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.response.Response
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.DialogOfferAppliedBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.FragmentOfferDetailBinding
import app.retailer.krina.shop.com.mp_shopkrina_retailer.interfaces.SubCategoryInterface
import app.retailer.krina.shop.com.mp_shopkrina_retailer.interfaces.SubSubCategoryFilterInterface
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.shoppingCart.ShoppingCartActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.adapter.BillDiscountFreeItemAdapter
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.ItemListAdapter
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.dialog.OfferInfoFragment
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.categoryBean.BaseCategoriesModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.categoryBean.SubCategoriesModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.shoppingCart.CartDealResponse
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.EndPointPref
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.SharePrefs
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.HomeActivity
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.subCategory.SubCategoryFilterAdapter
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.LocaleHelper
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.RetailerSDKApp
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import com.google.gson.JsonObject
import io.reactivex.observers.DisposableObserver
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

class OfferDetailFragment : Fragment(), SubSubCategoryFilterInterface, SubCategoryInterface {
    private lateinit var appCtx: RetailerSDKApp
    lateinit var binding: FragmentOfferDetailBinding
    private lateinit var activity: HomeActivity
    private lateinit var offerViewModel: OfferViewModel
    private lateinit var utils: Utils
    private var subCategoryFilterAdapter: SubCategoryFilterAdapter? = null
    private var subSubCategoryAdapter: OfferBrandAdapter? = null
    private var itemListAdapter: ItemListAdapter? = null
    private var list = ArrayList<ItemListModel>()
    private var subCategoryList = ArrayList<SubCategoriesModel>()
    private val filterSubCategoryList = ArrayList<SubCategoriesModel>()
    private var brandList = ArrayList<SubSubCategoriesModel>()
    private val filterSubSubCategoriesList = ArrayList<SubSubCategoriesModel>()
    private var mItemIdList: ArrayList<Int>? = null
    private var cartList: List<ItemListModel>? = null
    private lateinit var discountModel: BillDiscountModel
    private var lang = ""
    private var wId = 0
    private var custId = 0
    private var baseCategoryId = 0
    private var categoryId = 2
    private var subCatId = 0
    private var subSubCattId = 0
    private var skip = 0
    private var homeFlag = false
    private var isLoadDataForRelatedItem = true
    private var sortType = "M"
    private var direction = "desc"
    private var step = 1
    private var offerMaxValue = 0
    private var totalQty = 0
    private var step1D = 0
    private var step2D = 0
    private lateinit var mLinearLayoutManager: LinearLayoutManager

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as HomeActivity
        appCtx = activity.application as RetailerSDKApp

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding =
            FragmentOfferDetailBinding.inflate(inflater, container, false)
        val appRepository = AppRepository(activity.applicationContext)
        offerViewModel =
            ViewModelProvider(
                activity,
                OfferViewModelFactory(RetailerSDKApp.application, appRepository)
            )[OfferViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (arguments != null) {
            discountModel = arguments!!.getSerializable("model") as BillDiscountModel
        }

        initialization()
        categoryAPICall()
        setValues()

        RetailerSDKApp.getInstance().noteRepository.cartNonZero.observe(this) { cartList: List<ItemListModel>? ->
            this.cartList = cartList
            if (step == 1) {
                updateProgress(step)
            } else {
                updateProgress(step)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (itemListAdapter != null) itemListAdapter?.notifyDataSetChanged()
    }


    private fun initialization() {
        custId = SharePrefs.getInstance(activity).getInt(SharePrefs.CUSTOMER_ID)
        wId = SharePrefs.getInstance(activity).getInt(SharePrefs.WAREHOUSE_ID)
        utils = Utils(activity)
        lang = LocaleHelper.getLanguage(activity)

        binding.ivInfo.setOnClickListener {
            if (discountModel.offerOn.equals(
                    "ScratchBillDiscount", ignoreCase = true
                ) && !discountModel.isScratchBDCode
            ) Utils.setToast(activity, "Scratch the card first")
            else OfferInfoFragment.newInstance(0, discountModel).show(fragmentManager!!, "a")
        }
        binding.tvStep1.setOnClickListener {
            binding.rvSubCategory.visibility = View.VISIBLE
            binding.rvSubSubCategory.visibility = View.VISIBLE
            binding.tvStep1.setBackgroundResource(R.drawable.ic_step_1_bg_select)
            binding.tvStep2.setBackgroundResource(R.drawable.ic_step_2_bg_unselect)
            step = 1
            updateViews(step)
            callItemMasterAPI()
        }
        binding.tvStep2.setOnClickListener {
            binding.rvSubCategory.visibility = View.GONE
            binding.rvSubSubCategory.visibility = View.GONE
            binding.tvStep1.setBackgroundResource(R.drawable.ic_step_1_bg_unselect)
            binding.tvStep2.setBackgroundResource(R.drawable.ic_step_2_bg_select)
            step = 2
            subCatId = 0
            subSubCattId = 0
            updateViews(step)
            callItemMasterAPI()
        }
        binding.btnNext.setOnClickListener {
            binding.tvStep2.callOnClick()
        }
        binding.btnCheckout.setOnClickListener {
            CommonClassForAPI.getInstance(activity).getApplyDiscountResponse(
                applyOfferObserver, custId, wId, discountModel.offerId, true, lang, "offer"
            )
        }

        subCategoryFilterAdapter = SubCategoryFilterAdapter(activity, filterSubCategoryList, this)
        binding.rvSubCategory.adapter = subCategoryFilterAdapter
        binding.rvSubCategory.setHasFixedSize(true)
        binding.rvSubCategory.isNestedScrollingEnabled = false

        subSubCategoryAdapter = OfferBrandAdapter(activity, filterSubSubCategoriesList, this)
        binding.rvSubSubCategory.adapter = subSubCategoryAdapter
        binding.rvSubSubCategory.setHasFixedSize(true)
        binding.rvSubSubCategory.isNestedScrollingEnabled = false

        subCategoryFilterAdapter!!.basCatId = baseCategoryId

        mLinearLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        binding.rvCategoryItem.layoutManager = mLinearLayoutManager
        binding.rvCategoryItem.isNestedScrollingEnabled = false

        itemListAdapter =
            ItemListAdapter(
                activity,
                list
            )
        binding.rvCategoryItem.adapter = itemListAdapter

        binding.noItems.text = RetailerSDKApp.getInstance().dbHelper.getString(R.string.no_items_avl)

        observe(offerViewModel.categoryData, ::handleCategoryResult)
        observe(offerViewModel.itemData, ::handleItemListResult)
        observe(offerViewModel.itemData1, ::handleItemListResult1)
        observe(offerViewModel.removeOfferData, ::handleRemoveOfferResult)

        binding.nestedScroll.setOnScrollChangeListener { v: NestedScrollView, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int ->
            if (v.getChildAt(v.childCount - 1) != null) {
                if (scrollY >= v.getChildAt(v.childCount - 1).measuredHeight - v.measuredHeight && scrollY > oldScrollY) {
                    val visibleItemCount = mLinearLayoutManager.childCount
                    val totalItemCount = mLinearLayoutManager.itemCount
                    val pastVisiblesItems = mLinearLayoutManager.findFirstVisibleItemPosition()
                    if (visibleItemCount + pastVisiblesItems >= totalItemCount) {
                        Log.i("Nested", "BOTTOM SCROLL")
                        if (isLoadDataForRelatedItem) {
                            if (list.size > 2) {
                                skip += 10
                                offerViewModel.fetchItemList1(
                                    custId,
                                    discountModel.offerId!!,
                                    subCatId,
                                    subSubCattId,
                                    step,
                                    skip,
                                    10,
                                    lang
                                )
                            }
                        }
                    }
                }
            }
        }
    }


    private fun setValues() {
        binding.tvOffer.text = ""
        binding.tvOfferDes.text =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.min_ord_value) + discountModel.billAmount
        binding.tvOfferDesc.text =
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.offer_valid_on_select_products)
        binding.llMain.setBackgroundColor(Color.parseColor(discountModel.colorCode ?: "#4D9654"))
        //
        binding.rlApply.visibility = View.GONE
        binding.tvPrimeOffer.visibility = View.GONE
        binding.rlBillItem.visibility = View.GONE
        //
        if (discountModel.billDiscountOfferOn.equals("Percentage", ignoreCase = true)) {
            binding.tvOffer.text = DecimalFormat("##.##").format(
                discountModel.discountPercentage
            ) + "% " + RetailerSDKApp.getInstance().dbHelper.getString(R.string.off)
        } else if (discountModel.billDiscountOfferOn.equals("FreeItem", ignoreCase = true)) {
            binding.tvOffer.text =
                RetailerSDKApp.getInstance().dbHelper.getString(R.string.free_item_offer)
            binding.rlBillItem.visibility = View.VISIBLE
            binding.recyclerBillDiscountItem.adapter = BillDiscountFreeItemAdapter(
                activity, discountModel.retailerBillDiscountFreeItemDcs!!
            )
        } else if (discountModel.billDiscountOfferOn.equals("DynamicAmount", ignoreCase = true)) {
            binding.tvOffer.text =
                RetailerSDKApp.getInstance().dbHelper.getString(R.string.flat_rs) + DecimalFormat("##.##").format(
                    discountModel.billDiscountWallet
                ) + " " + RetailerSDKApp.getInstance().dbHelper.getString(R.string.off)
        } else {
            val msgPostBill = if (discountModel.applyOn.equals(
                    "PostOffer", ignoreCase = true
                )
            ) " PostOffer" else ""
            if (discountModel.walletType.equals("WalletPercentage", ignoreCase = true)) {
                binding.tvOffer.text =
                    DecimalFormat("##.##").format(discountModel.billDiscountWallet) + "%  " + RetailerSDKApp.getInstance().dbHelper.getString(
                        R.string.off
                    ) + msgPostBill
            } else {
                binding.tvOffer.text =
                    RetailerSDKApp.getInstance().dbHelper.getString(R.string.flat_rs) + DecimalFormat(
                        "##.##"
                    ).format(convertToAmount(discountModel.billDiscountWallet)) + " " + msgPostBill + RetailerSDKApp.getInstance().dbHelper.getString(
                        R.string.off
                    )
            }
        }

        if (discountModel.ImagePath != null) {
            var path = discountModel.ImagePath
            if (!discountModel.ImagePath!!.contains("https")) path =
                EndPointPref.getInstance(activity).baseUrl + discountModel.ImagePath
            Glide.with(this).load(path).placeholder(R.drawable.logo_grey).into(binding.ivImage)
        } else {
            binding.ivImage.setImageResource(R.drawable.logo_sk)
        }

        if (discountModel.requiredItemsList != null && discountModel.requiredItemsList!!.isNotEmpty()) {
            binding.tvStep2.visibility = View.VISIBLE
            binding.tvTotalSteps.visibility = View.VISIBLE
            binding.tvFulfillTxt.visibility = View.VISIBLE
        } else {
            binding.liStep.visibility = View.GONE
            binding.tvStep2.visibility = View.INVISIBLE
            binding.tvTotalSteps.visibility = View.INVISIBLE
            binding.tvFulfillTxt.visibility = View.GONE
        }
        if (discountModel.lineItem > 0) {
            binding.tvLineItem.visibility = View.VISIBLE
            binding.tvLineItem.text = "Add more " + discountModel.lineItem + " line item"
            offerMaxValue += discountModel.lineItem
        } else binding.tvLineItem.visibility = View.GONE
        if (discountModel.billAmount > 0) {
            binding.tvOrderValue.visibility = View.VISIBLE
            binding.tvOrderValue.text = "Min " + discountModel.billAmount + " Order value"
            offerMaxValue += (discountModel.billAmount).toInt()
        } else binding.tvOrderValue.visibility = View.GONE
        // offer max value
        binding.progressItem.max = 100

        // timer
        val timestamp = getTimeStamp(discountModel.end!!)
        val expiryTime = timestamp - Date().time
        object : CountDownTimer(expiryTime, 1000) {
            override fun onTick(millis: Long) {
                val day = TimeUnit.MILLISECONDS.toDays(millis)
                if (day > 0) {
                    binding.tvTime.text = "Expires in " + day + " days"
                } else {
                    val hour = TimeUnit.MILLISECONDS.toHours(millis)
                    if (hour > 0) {
                        binding.tvTime.text = "Expires in " + hour % 24 + " hour"
                    } else {
                        val sec = TimeUnit.MILLISECONDS.toSeconds(millis)
                        val min = TimeUnit.MILLISECONDS.toMinutes(millis)
                        binding.tvTime.text = "Expires in " + min % 60 + ":" + sec % 60
                    }
                }
            }

            override fun onFinish() {
                binding.tvTime.text = "Time Expired!"
            }
        }.start()
    }

    private fun updateViews(step: Int) {
        offerMaxValue = 0
        if (step == 1) {
            if (discountModel.lineItem > 0) {
                binding.tvLineItem.visibility = View.VISIBLE
                binding.tvLineItem.text = "Add more " + discountModel.lineItem + " line item"
                offerMaxValue += discountModel.lineItem
            } else binding.tvLineItem.visibility = View.GONE
            if (discountModel.billAmount > 0) {
                binding.tvOrderValue.visibility = View.VISIBLE
                binding.tvOrderValue.text = "Min " + discountModel.billAmount + " Order value"
                offerMaxValue += (discountModel.billAmount).toInt()
            } else binding.tvOrderValue.visibility = View.GONE
            // offer max value
            binding.progressItem.max = if (discountModel.lineItem > 0) 200 else 100
        } else {
            totalQty = discountModel.requiredItemsList!!.filter { it.valueType == "Qty" }
                .sumOf { it.objectValue }.toInt()
            val totalValue = discountModel.requiredItemsList!!.filter { it.valueType == "Value" }
                .sumOf { it.objectValue }

            if (totalQty > 0) {
                binding.tvLineItem.visibility = View.VISIBLE
                binding.tvLineItem.text = "Add $totalQty more item"
                offerMaxValue += totalQty
            } else binding.tvLineItem.visibility = View.GONE
            if (totalValue > 0) {
                binding.tvOrderValue.visibility = View.VISIBLE
                binding.tvOrderValue.text = "Min $totalValue Order value"
                offerMaxValue += totalValue.toInt()
            } else binding.tvOrderValue.visibility = View.GONE
            // offer max value
            binding.progressItem.max = offerMaxValue
        }
        // update progress
        updateProgress(step)
    }

    private fun updateProgress(step: Int) {
        if (step == 1) {
            step1D = 0
            val cartTotal = cartList?.sumOf { it.unitPrice * it.qty }

            val offerCheck = OfferCheck(discountModel, cartList!!, cartTotal!!)
            val isApplicable = offerCheck.checkCoupon()
            val cartLineItem = offerCheck.orderLineItems
            val itemTotal = offerCheck.itemTotal

            var progress = 0
            if (itemTotal >= discountModel.billAmount && cartLineItem >= discountModel.lineItem) progress =
                200
            else if (discountModel.lineItem > 0 && cartLineItem > 0) {
                progress += (cartLineItem * 100) / discountModel.lineItem
            } else {
                progress += ((itemTotal * 100) / discountModel.billAmount).toInt()
            }
            binding.progressItem.progress = progress / 2
            if (progress >= 100) {
                step1D = 1
                binding.tvStep1.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.ic_stepper_checked, 0, 0, 0
                )
                binding.tvTotalSteps.text = "1/2 Done"
            } else {
                binding.tvStep1.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.ic_stepper_wrong, 0, 0, 0
                )
                binding.tvTotalSteps.text = "0/2 Done"
            }
            // line item
            if (discountModel.lineItem > 0 && cartLineItem >= discountModel.lineItem) {
                binding.tvLineItem.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.right_tick_offer, 0, 0, 0
                )
                binding.tvLineItem.text = "" + discountModel.lineItem + " line items added"
            } else {
                binding.tvLineItem.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.offer_perc, 0, 0, 0
                )
                binding.tvLineItem.text =
                    "Add more " + (discountModel.lineItem - cartLineItem) + " line item"
            }
            // bill amount
            if (step1D + step2D == 2) binding.tvTotalSteps.text = "2/2 Done"
            else if (step1D + step2D == 1) binding.tvTotalSteps.text = "1/2 Done"
            else binding.tvTotalSteps.text = "0/2 Done"
            if (discountModel.billAmount > 0 && itemTotal <= 1) {
                binding.tvOrderValue.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.offer_perc, 0, 0, 0
                )
                binding.tvOrderValue.text = "Min " + discountModel.billAmount + " Order value"
            } else if (discountModel.billAmount > 0 && itemTotal >= discountModel.billAmount) {
                binding.tvOrderValue.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.right_tick_offer, 0, 0, 0
                )
                binding.tvOrderValue.text = "Rs " + discountModel.billAmount + " Added"
                binding.tvTotalSteps.text = "1/2 Done"
            } else {
                binding.tvOrderValue.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.offer_perc, 0, 0, 0
                )
                val left = DecimalFormat("##.##").format(discountModel.billAmount - itemTotal)
                binding.tvOrderValue.text = "Add Rs $left more"
            }

            if (isApplicable) {
                step1D = 1
                step2D = 1
                binding.tvTotalSteps.text = "2/2 Done"
                binding.liCheckout.visibility = View.VISIBLE
                binding.liNext.visibility = View.GONE
                binding.liProgressView.visibility = View.GONE
            } else {
                binding.liProgressView.visibility = View.VISIBLE
                binding.liNext.visibility = View.GONE
                binding.liCheckout.visibility = View.GONE
                if (binding.progressItem.progress >= 100) {
                    binding.liProgressView.visibility = View.GONE
                    binding.liNext.visibility = View.VISIBLE
                }
            }
        } else if (step == 2) {
            step2D = 0
            binding.liNext.visibility = View.GONE
            if (discountModel.requiredItemsList != null && discountModel.requiredItemsList!!.isNotEmpty()) {
                var isRequiredItemExists = true
                var totalItem = 0
                var currentItem = 0
                var totalValue = 0
                var currentValue = 0
                for (reqItem in discountModel.requiredItemsList!!) {
                    var totalT = 0.0
                    if (reqItem.objectType.equals("Item", ignoreCase = true)) {
                        if (reqItem.valueType.equals("Qty", ignoreCase = true)) {
                            totalT += cartList!!.sumOf { if (reqItem.objectId!!.contains(it.itemMultiMRPId.toString()) && !(it.isOffer && it.offerType == "FlashDeal")) it.qty else 0 }
                            totalItem = reqItem.objectValue.toInt()
                            currentItem = totalT.toInt()
                        } else if (reqItem.valueType.equals("Value", ignoreCase = true)) {
                            totalT += cartList!!.sumOf { if (reqItem.objectId!!.contains(it.itemMultiMRPId.toString()) && !(it.isOffer && it.offerType == "FlashDeal")) (it.unitPrice * it.qty) else 0.0 }
                            totalValue = reqItem.objectValue.toInt()
                            currentValue = totalT.toInt()
                        }
                        if (reqItem.objectValue > totalT) {
                            isRequiredItemExists = false
                        }
                    } else if (reqItem.objectType.equals("brand", ignoreCase = true)) {
                        if (reqItem.valueType.equals("Qty", ignoreCase = true)) {
                            val ids = reqItem.objectId!!.replace(",", " ").split(" ")
                            totalT += cartList!!.sumOf {
                                if (ids.contains(it.subsubCategoryid.toString()) && !(it.isOffer && it.offerType == "FlashDeal")) it.qty else 0
                            }
                            totalItem = reqItem.objectValue.toInt()
                            currentItem = totalT.toInt()
                        } else if (reqItem.valueType.equals("Value", ignoreCase = true)) {
                            val ids = reqItem.objectId!!.replace(",", " ").split(" ")
                            totalT += cartList!!.sumOf {
                                if (ids.contains(it.subsubCategoryid.toString()) && !(it.isOffer && it.offerType == "FlashDeal")) it.unitPrice * it.qty else 0.0
                            }
                            totalValue = reqItem.objectValue.toInt()
                            currentValue = totalT.toInt()
                        }
                        if (reqItem.objectValue > totalT) {
                            isRequiredItemExists = false
                        }
                    }
                }
                if (totalQty > 0) {
                    if (currentItem >= totalItem) {
                        binding.tvLineItem.setCompoundDrawablesWithIntrinsicBounds(
                            R.drawable.right_tick_offer, 0, 0, 0
                        )
                        binding.tvLineItem.text = "$totalItem Items Added"
                    } else {
                        binding.tvLineItem.setCompoundDrawablesWithIntrinsicBounds(
                            R.drawable.offer_perc, 0, 0, 0
                        )
                        binding.tvLineItem.text = "Add " + (totalItem - currentItem) + " more item"
                    }
                }
                if (currentValue >= totalValue) {
                    binding.tvOrderValue.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.right_tick_offer, 0, 0, 0
                    )
                    binding.tvOrderValue.text = "Rs $totalValue Added"
                } else {
                    binding.tvOrderValue.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.offer_perc, 0, 0, 0
                    )
                    binding.tvOrderValue.text = "Add Rs " + (totalValue - currentValue) + " more"
                }
                if ((currentValue + currentItem) >= (totalValue + totalItem)) {
                    binding.tvStep2.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.ic_stepper_checked, 0, 0, 0
                    )
                    binding.tvTotalSteps.text = "1/2 Done"
                } else {
                    binding.tvStep2.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.ic_stepper_wrong, 0, 0, 0
                    )
                    binding.tvTotalSteps.text = "0/2 Done"
                }
                if (isRequiredItemExists) {
                    binding.tvTotalSteps.text = "1/2 Done"
                    val cartTotal = cartList?.sumOf { it.unitPrice * it.qty }
                    val offerCheck = OfferCheck(discountModel, cartList!!, cartTotal!!)
                    val isApplicable = offerCheck.checkCoupon()
                    val requiredItems = offerCheck.requiredItems
                    //
                    if (isApplicable) {
                        binding.tvTotalSteps.text = "2/2 Done"
                        binding.liCheckout.visibility = View.VISIBLE
                        binding.liProgressView.visibility = View.GONE
                    } else {
                        binding.liCheckout.visibility = View.GONE
                        binding.liProgressView.visibility = View.VISIBLE
                    }
                }

                // progress
                var progress = 0
                if (totalQty > 0) binding.progressItem.max = 200
                else binding.progressItem.max = 100

                if (totalValue > 0) progress += (currentValue * 100) / totalValue
                if (totalItem > 0) progress += (currentItem * 100) / totalItem

                binding.progressItem.progress = progress
                if (progress >= binding.progressItem.max) {
                    step2D = 1
                }
                if (step1D + step2D == 2) binding.tvTotalSteps.text = "2/2 Done"
                else if (step1D + step2D == 1) binding.tvTotalSteps.text = "1/2 Done"
                else binding.tvTotalSteps.text = "0/2 Done"
                if ((currentValue + currentItem) < (totalValue + totalItem)) {
                    binding.liCheckout.visibility = View.GONE
                    binding.liProgressView.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun convertToAmount(amount: Double): Double {
        return amount / 10
    }

    private fun getTimeStamp(dateStr: String): Long {
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val all = dateStr.replace("\\+0([0-9]){1}\\:00".toRegex(), "+0$100")
        val s = all.replace("T".toRegex(), " ")
        var timestamp: Long = 0
        try {
            val date = format.parse(s)
            timestamp = date.time
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return timestamp
    }

    private fun showOfferAppliedDialog(response: CheckoutCartResponse) {
        val dialog = BottomSheetDialog(activity)
        val dialogBinding: DialogOfferAppliedBinding = DataBindingUtil.inflate(
            layoutInflater, R.layout.dialog_offer_applied, null, false
        )
        dialog.setContentView(dialogBinding.root)
        dialog.window!!.findViewById<View>(R.id.design_bottom_sheet)
            .setBackgroundColor(Color.TRANSPARENT)
        dialog.setCancelable(true)

        if (discountModel.billDiscountOfferOn.equals(
                "Percentage", ignoreCase = true
            )
        ) dialogBinding.tvOffer.text = "" + discountModel.discountPercentage + "% Offer Applied"
        else if (discountModel.billDiscountOfferOn.equals(
                "FreeItem", ignoreCase = true
            )
        ) dialogBinding.tvOffer.text = "Free Item Offer Applied"
        else {
            val msgPostBill = if (discountModel.applyOn.equals("PostOffer")) "Post Offer " else ""
            if (discountModel.walletType.equals(
                    "WalletPercentage", ignoreCase = true
                )
            ) dialogBinding.tvOffer.text =
                "" + msgPostBill + "" + discountModel.billDiscountWallet + "% Offer Applied"
            else dialogBinding.tvOffer.text =
                "" + msgPostBill + "" + discountModel.billDiscountWallet + "OFF Offer Applied"
        }

        if (dialogBinding.tvOffer.text == "Free Item Offer Applied") dialogBinding.tvSaved.text =
            "You will get Free Item"
        else dialogBinding.tvSaved.text =
            "You saves Rs - " + DecimalFormat("##.##").format(response.shoppingCartItemDetailsResponse?.totalDiscountAmt)

        if (response.shoppingCartItemDetailsResponse?.discountDetails != null && response.shoppingCartItemDetailsResponse?.discountDetails!!.size > 1) {
            dialogBinding.tvTotal.visibility = View.VISIBLE
            val discountAmt =
                response.shoppingCartItemDetailsResponse?.discountDetails?.find { it.offerId == discountModel.offerId }?.discountAmount?.toInt()

            dialogBinding.tvSaved.text =
                "You saves Rs - " + DecimalFormat("##.##").format(discountAmt)
            dialogBinding.tvTotal.text =
                "Total Saving " + DecimalFormat("##.##").format(response.shoppingCartItemDetailsResponse?.totalDiscountAmt)
        }

        dialogBinding.btnCheckout.setOnClickListener {
            dialog.dismiss()
            startActivity(Intent(activity, ShoppingCartActivity::class.java))
        }
        dialogBinding.btnContinue.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun showRemoveOfferAlert() {
        val dialog = AlertDialog.Builder(activity)
        dialog.setMessage(RetailerSDKApp.getInstance().dbHelper.getString(R.string.this_offer_can_not_be_clubbed))
        dialog.setPositiveButton(
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.replace_offer)
        ) { dialog: DialogInterface, i: Int ->
            dialog.dismiss()
            offerViewModel.removeAllOffer(custId, wId)
        }
        dialog.setNegativeButton(
            RetailerSDKApp.getInstance().dbHelper.getString(R.string.cancel)
        ) { dialog: DialogInterface, i: Int ->
            dialog.dismiss()
        }

        dialog.show()
    }


    private fun categoryAPICall() {
        if (utils.isNetworkAvailable) {
            offerViewModel.getOfferCategory(
                custId, discountModel.offerId, 0, 0, 1, lang
            )
        } else {
            Utils.setToast(
                activity,
                RetailerSDKApp.getInstance().dbHelper.getString(R.string.internet_connection)
            )
        }
    }

    // Call itemMater API
    private fun callItemMasterAPI() {
        if (utils.isNetworkAvailable) {
            sortType = "M"
            direction = "desc"
            skip = 0
            isLoadDataForRelatedItem = true
            list.clear()
            itemListAdapter!!.notifyDataSetChanged()
            binding.proRelatedItem.visibility = View.VISIBLE

            offerViewModel.fetchItemList(
                custId, discountModel.offerId, subCatId, subSubCattId, step, skip, 10, lang
            )
        } else {
            Utils.setToast(
                activity,
                RetailerSDKApp.getInstance().dbHelper.getString(R.string.internet_connection)
            )
        }
    }

    // filter data
    private fun layoutHideUnHide(value: Boolean) {
        if (value) {
            binding.noItems.visibility = View.GONE
            binding.rvCategoryItem.visibility = View.VISIBLE
        } else {
            binding.filterTitle.text =
                "0 " + RetailerSDKApp.getInstance().dbHelper.getString(R.string.Items)
            binding.noItems.visibility = View.VISIBLE
            binding.rvCategoryItem.visibility = View.GONE
        }
    }

    // filter sub category
    private fun setSubCategory(categoryId: Int) {
        Log.e("TAG", "setSubCategory: $categoryId")
        var subCategoryId = 0
        try {
            filterSubCategoryList.clear()
            for (i in subCategoryList.indices) {
                if (subCategoryList[i].categoryid == categoryId) {
                    if (subCategoryList[i].itemcount != 0) {
                        filterSubCategoryList.add(subCategoryList[i])
                    }
                }
            }
            if (filterSubCategoryList.size > 1 || categoryId == 0) {
                filterSubCategoryList.add(
                    0, SubCategoriesModel(
                        false,
                        0,
                        categoryId,
                        RetailerSDKApp.getInstance().dbHelper.getString(R.string.all),
                        "",
                        10
                    )
                )
            }
            if (filterSubCategoryList.size > 0) {
                if (homeFlag) {
                    subCategoryId = subCatId
                    for (i in filterSubCategoryList.indices) {
                        if (subCategoryId == filterSubCategoryList[i].subcategoryid) {
                            binding.rvSubCategory.scrollToPosition(i)
                        }
                    }
                } else {
                    subCategoryId = filterSubCategoryList[0].subcategoryid
                }
                subCategoryFilterAdapter!!.setSubcategoryOrderList(
                    filterSubCategoryList, subCategoryId
                )
                val finalSubCategoryId = subCategoryId
                // check if id exist
                val checkList =
                    filterSubCategoryList.filter { s: SubCategoriesModel -> s.subcategoryid == finalSubCategoryId }
                if (checkList == null || checkList.size == 0) {
                    binding.noItems.visibility = View.VISIBLE
                }
            } else {
                layoutHideUnHide(false)
                Utils.setToast(
                    activity,
                    RetailerSDKApp.getInstance().dbHelper.getString(R.string.no_data_available)
                )
                subCategoryFilterAdapter!!.setSubcategoryOrderList(
                    filterSubCategoryList, subCategoryId
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // filter sub sub category
    private fun setSubSubCategory(subCategoryId: Int, categoryId: Int) {
        try {
            filterSubSubCategoriesList.clear()
            filterSubSubCategoriesList.add(
                0,
                SubSubCategoriesModel(
                    RetailerSDKApp.getInstance().dbHelper.getString(R.string.all),
                    baseCategoryId,
                    categoryId,
                    subCategoryId,
                    0
                )
            )
            binding.nestedScroll.fullScroll(NestedScrollView.FOCUS_UP)
            for (ii in brandList.indices) {
                if (brandList[ii].subcategoryid == subCategoryId && brandList[ii].categoryid == categoryId) {
                    if (brandList[ii].itemcount != 0) {
                        filterSubSubCategoriesList.add(brandList[ii])
                    }
                }
            }
            if (filterSubSubCategoriesList.size == 2) {
                filterSubSubCategoriesList.removeAt(0)
            }
            if (filterSubSubCategoriesList.size != 0) {
                var subsubCatId = 0
                if (homeFlag) {
                    homeFlag = false
                    subsubCatId = subSubCattId
                } else {
                    subsubCatId = filterSubSubCategoriesList[0].subsubcategoryid
                }
                filterSubSubCategoriesList[0].isChecked = true
                subSubCategoryAdapter!!.setCategoryOrderFilterList(
                    filterSubSubCategoriesList, subsubCatId
                )
                val finalSubsubCatId = subsubCatId
                val checkList =
                    filterSubSubCategoriesList.filter { s: SubSubCategoriesModel -> s.subsubcategoryid == finalSubsubCatId }
                if (checkList == null || checkList.size == 0) {
                    binding.noItems.visibility = View.VISIBLE
                }
                for (i in filterSubSubCategoriesList.indices) {
                    if (filterSubSubCategoriesList[i].subsubcategoryid == subsubCatId) {
                        binding.rvSubSubCategory.scrollToPosition(i)
                        break
                    }
                }
            } else {
                subSubCategoryAdapter!!.setCategoryOrderFilterList(filterSubSubCategoriesList, 0)
                layoutHideUnHide(false)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    private fun handleCategoryResult(it: Response<JsonObject>) {
        when (it) {
            is Response.Loading -> {
                binding.progressCategory.visibility = View.VISIBLE
            }

            is Response.Success -> {
                it.data?.let {
                    binding.progressCategory.visibility = View.GONE
                    try {
                        val baseCategoryModel = Gson().fromJson(it, BaseCategoriesModel::class.java)
                        subCategoryList.clear()
                        brandList.clear()
                        subCategoryList = baseCategoryModel.subCategoryDC
                        brandList = baseCategoryModel.subsubCategoryDc

                        Log.e("TAG", "success onNext subCategoryItemList: $subCategoryList")
                        Log.e("TAG", "success onNext: $brandList")

                        //set category
                        setSubCategory(0)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }

            is Response.Error -> {
                binding.progressCategory.visibility = View.GONE
                Toast.makeText(
                    activity, it.errorMesssage.toString(), Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun handleItemListResult(it: Response<CartDealResponse>) {
        when (it) {
            is Response.Loading -> {
                binding.proRelatedItem.visibility = View.VISIBLE
            }

            is Response.Success -> {
                it.data?.let {
                    binding.proRelatedItem.visibility = View.GONE
                    homeFlag = false
                    mItemIdList = ArrayList()
                    try {
                        if (it.itemDataDCs != null && it.itemDataDCs.size > 0) {
                            layoutHideUnHide(true)
                            list = it.itemDataDCs
                            if (list.size != 0) {
                                binding.filterTitle.text =
                                    it.totalItem.toString() + " " + RetailerSDKApp.getInstance().dbHelper.getString(
                                        R.string.Items
                                    )
                                itemListAdapter =
                                    ItemListAdapter(
                                        activity,
                                        list
                                    )
                                binding.rvCategoryItem.adapter = itemListAdapter

                                binding.nestedScroll.fullScroll(NestedScrollView.FOCUS_UP)
                                // update analytic
                                RetailerSDKApp.getInstance().updateAnalyticVIL("categoryItems", list)
                            }
                        } else {
                            layoutHideUnHide(false)
                            itemListAdapter!!.setItemListCategory(list)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }

            is Response.Error -> {
                binding.proRelatedItem.visibility = View.GONE
                Toast.makeText(
                    activity, it.errorMesssage.toString(), Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun handleItemListResult1(it: Response<CartDealResponse>) {
        when (it) {
            is Response.Loading -> {
                binding.progressCategory.visibility = View.VISIBLE
            }

            is Response.Success -> {
                binding.progressCategory.visibility = View.GONE
                it.data?.let {
                    try {
                        if (it.itemDataDCs != null && it.itemDataDCs.size > 0) {
                            list.addAll(it.itemDataDCs)
                            if (list.size != 0) {
                                itemListAdapter!!.notifyDataSetChanged()
                                // update analytic
                                RetailerSDKApp.getInstance().updateAnalyticVIL("categoryItems", list)
                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }

            is Response.Error -> {
                binding.progressCategory.visibility = View.GONE
                Toast.makeText(
                    activity, it.errorMesssage.toString(), Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun handleRemoveOfferResult(it: Response<Boolean>) {
        when (it) {
            is Response.Loading -> {
                Utils.showProgressDialog(activity)
            }

            is Response.Success -> {
                Utils.hideProgressDialog()
                it.data?.let {
                    try {
                        if (it != null && it) {
                            CommonClassForAPI.getInstance(activity).getApplyDiscountResponse(
                                applyOfferObserver,
                                custId,
                                wId,
                                discountModel.offerId,
                                true,
                                lang,
                                "offer"
                            )
                        } else {
                            Utils.setToast(
                                activity,
                                RetailerSDKApp.getInstance().noteRepository.getString(R.string.server_error)
                            )
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }

            is Response.Error -> {
                Utils.hideProgressDialog()
                Utils.setToast(activity, it.errorMesssage.toString())
            }
        }
    }


    // sub category select
    override fun SubCategoryClicked(subCategoryId: Int, categoryId: Int) {
        setSubSubCategory(subCategoryId, categoryId)
        binding.nestedScroll.fullScroll(NestedScrollView.FOCUS_UP)
    }

    // sub sub category select
    override fun SubSubCategoryFilterClicked(
        pos: Int, subsubCatId: Int, scateId: Int, categoryId: Int
    ) {
        try {
            subSubCattId = subsubCatId
            subCatId = scateId
            this.categoryId = categoryId
            callItemMasterAPI()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private val applyOfferObserver: DisposableObserver<CheckoutCartResponse> =
        object : DisposableObserver<CheckoutCartResponse>() {
            override fun onNext(response: CheckoutCartResponse) {
                Utils.hideProgressDialog()
                if (response.status) {
                    showOfferAppliedDialog(response)
                } else {
                    showRemoveOfferAlert()
                }
            }

            override fun onError(e: Throwable) {
                Utils.hideProgressDialog()
            }

            override fun onComplete() {}
        }
}