package app.retailer.krina.shop.com.mp_shopkrina_retailer.data.repository

import android.content.Context
import app.retailer.krina.shop.com.mp_shopkrina_retailer.BuildConfig
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.api.RestClient
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.auth.EditProfileModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.auth.NewSignupRequest
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.auth.OtpVerifyRequest
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.CartAddItemModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.RatingModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.searchItem.SearchItemRequestModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.payment.CheckBookCreditLimitRes
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.CreditPayment
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.postModels.CatRelatedItemPostModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.postModels.OrderPlacedModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.postModels.PaymentReq
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.saleReturn.PostKKReturnReplaceRequestDc
import com.google.gson.JsonObject
import okhttp3.MultipartBody

class AppRepository(
    applicationContext: Context
) {

    var apiService = RestClient.getInstance().service

    suspend fun getSalesReturnList(customerId: Int) = apiService!!.getSalesReturnList(customerId)
    suspend fun getSalesReturnDetailsList(requestId: Int) =
        apiService!!.getSalesReturnDetailsList(requestId)

    suspend fun getReturnItem(customerId: Int, wId: Int, keyValue: String) =
        apiService!!.getReturnItem(customerId, wId, keyValue)

    suspend fun getSaleReturnOrderBatchItem(customerId: Int, itemMultiMrpId: Int) =
        apiService!!.getSaleReturnOrderBatchItem(customerId, itemMultiMrpId)

    suspend fun uploadKKReturnReplaceImages(body: MultipartBody.Part) =
        apiService!!.uploadKKReturnReplaceImages(body)

    suspend fun postSalesReturnRequest(model: PostKKReturnReplaceRequestDc) =
        apiService!!.postSalesReturnRequest(model)

    //Auth
    suspend fun getOtp(mobileNumber: String, deviceId: String, keyHas: String) =
        RestClient.getInstance("Generate OTP").service!!.getOtp(
            mobileNumber,
            deviceId,
            keyHas,
            BuildConfig.BUILD_TYPE
        )

    suspend fun showOtherLoginOption() = apiService!!.showOtherLoginOption()

    suspend fun callVerifyOtp(verifyOtpModel: OtpVerifyRequest?) =
        RestClient.getInstance("OTP verify").service!!.callVerifyOtp(verifyOtpModel)

    suspend fun getCustVerifyInfo(
        mobileNumber: String,
        verify: String,
        fcmtoken: String,
        currentAPKversion: String,
        phoneOSversion: String,
        userDeviceName: String,
        IMEI: String
    ) =
        RestClient.getInstance("Check Customer Already register or not").service!!.getCustVerifyInfo(
            mobileNumber,
            verify,
            fcmtoken,
            currentAPKversion,
            phoneOSversion,
            userDeviceName,
            IMEI
        )

    suspend fun getToken(grantType: String?, username: String, password: String?) =
        apiService!!.getToken(grantType, username, password)

    suspend fun insertCustomer(mobileNumber: String?) =
        RestClient.getInstance("Customer register").service!!.insertCustomer(mobileNumber)

    suspend fun searchAddress(url: String) = apiService!!.searchAddress(url)

    suspend fun editCustomerInfo(model: EditProfileModel, mSectionType: String) =
        RestClient.getInstance(mSectionType).service!!.editCustomerInfo(model)

    suspend fun doNewSignup(signupModel: NewSignupRequest) =
        RestClient.getInstance("Update customer information").service!!.doNewSignup(signupModel)

    suspend fun getCustAddress(customerId: Int) = apiService!!.getCustAddress(customerId)

    suspend fun getCity() = apiService!!.getCity()

    suspend fun uploadImage(body: MultipartBody.Part) = apiService!!.imageUpload(body)
    suspend fun getGstStatus(gst: String, mSectionType: String) =
        RestClient.getInstance(mSectionType).service!!.getGstStatus(gst)

    suspend fun getCustomerDocType(wId: Int, custId: Int) =
        apiService.getCustomerDocType(wId, custId)

    //Splash
    suspend fun getAppVersion() = apiService!!.getAppVersion()
    suspend fun getMyProfile(customerId: Int, deviceId: String) =
        apiService!!.getMyProfile(customerId, deviceId)


    suspend fun getCompanyDetailWithToken(customerId: Int, mSectionType: String) =
        RestClient.getInstance(mSectionType).service!!.getCompanyDetailWithToken(customerId)

    //Home
    suspend fun getUdhaarOverDue(customerId: Int, lang: String) =
        apiService.getUdhaarOverDue(customerId, "Retailer", lang)

    suspend fun getCustomerCart(
        customerId: Int,
        warehouseId: Int,
        lang: String,
        mSectionType: String
    ) = RestClient.getInstance(mSectionType).service!!.getCustomerCart(
        customerId,
        warehouseId,
        lang
    )

    suspend fun getWalletPointNew(customerId: Int, pageNo: Int, mSectionType: String) =
        RestClient.getInstance(mSectionType).service!!.getWalletPointNew(customerId, pageNo)

    suspend fun getAppHomeBottomCall(customerId: Int) =
        RestClient.getInstance("HomePage").service!!.getAppHomeBottomCall(customerId)

    suspend fun getGullakBalance(customerId: Int) = apiService.getGullakBalance(customerId)
    suspend fun getRTGSBalance(customerId: Int) = apiService.getRTGSBalance(customerId)
    suspend fun getVATMUrl(customerId: Int, warehouseId: Int) =
        apiService.getVATMUrl(customerId, warehouseId)

    suspend fun getNotifyItems(customerId: Int, warehouseId: Int, itemNumber: String) =
        apiService.getNotifyItems(customerId, warehouseId, itemNumber)

    suspend fun generateLead(url: String?) = apiService.generateLead(url)
    suspend fun scaleUpLeadInitiate(customerId: Int) =
        RestClient.getInstance("Home").service!!.scaleUpLeadInitiate(customerId)

    suspend fun scaleUpLeadInitiateUsingUrl(url: String?) =
        RestClient.getInstance("Home").service!!.scaleUpLeadInitiateUsingUrl(url)

    suspend fun getCartResponse(cartAddItemModel: CartAddItemModel?, mSectionType: String) =
        RestClient.getInstance(mSectionType).service!!.getCartResponse(cartAddItemModel)

    suspend fun getMurliAudioForMobile(customerId: Int, warehouseId: Int, mSectionType: String) =
        RestClient.getInstance(mSectionType).service!!.getMurliAudioForMobile(
            customerId,
            warehouseId
        )

    suspend fun downloadFileWithUrl(fileUrl: String) = apiService!!.downloadFileWithUrl(fileUrl)
    suspend fun getMurliPublishedStory(customerId: Int, warehouseId: Int) =
        apiService!!.getMurliPublishedStory(customerId, warehouseId)

    suspend fun getGetDboyRatingOrder(url: String) = apiService!!.getGetDboyRatingOrder(url)
    suspend fun getGetDboyRatingOrderOther(url: String) =
        apiService!!.getGetDboyRatingOrderOther(url)

    suspend fun addRating(model: RatingModel?) = apiService!!.addRating(model)


    //App home
    suspend fun getAppHomeSection(
        appType: String?,
        customerId: Int,
        wId: Int,
        lang: String?,
        lat: Double,
        lng: Double
    ) = RestClient.getInstance("App home").service!!.getAppHomeSection(
        appType,
        customerId,
        wId,
        lang,
        lat,
        lng
    )

    suspend fun getDynamicHtml(url: String?) = apiService!!.getDynamicHtml(url)
    suspend fun getItemBySection(
        customerId: Int,
        warehouseId: Int,
        sectionId: String,
        lang: String
    ) = RestClient.getInstance("item").service!!.getItemBySection(
        customerId,
        warehouseId,
        sectionId,
        lang
    )

    suspend fun getOtherItemsHome(url: String?) =
        RestClient.getInstance("OtherItem").service!!.getOtherItemsHome1(url)

    suspend fun getAllStore(customerId: Int, warehouseId: Int, lang: String) =
        RestClient.getInstance("App home").service!!.getAllStore(customerId, warehouseId, lang)

    suspend fun getGameBanners(customerId: Int) =
        RestClient.getInstance("App home").service!!.getGameBanners(customerId)

    suspend fun getFlashDealItem(
        customerId: Int,
        wId: Int,
        sectionId: String?,
        lang: String?, mSectionType: String
    ) = RestClient.getInstance(mSectionType).service!!.getFlashDealItem(
        customerId,
        wId,
        sectionId,
        lang
    )

    suspend fun getStoreFlashDeal(
        customerId: Int,
        wId: Int,
        sectionId: String?,
        subCategoryId: Int,
        lang: String?, mSectionType: String
    ) = RestClient.getInstance(mSectionType).service!!.getStoreFlashDeal(
        customerId,
        wId,
        sectionId,
        subCategoryId,
        lang
    )

    suspend fun getSubCateOffer(
        customerId: Int,
        subCategoryId: Int
    ) = apiService!!.getSubCateOffer(
        customerId,
        subCategoryId
    )

    suspend fun storeDashboard(
        custId: Int,
        wId: Int,
        subCatId: Int,
        lang: String?
    ) = RestClient.getInstance("App home").service!!.storeDashboard(
        custId,
        wId,
        subCatId,
        lang
    )

    suspend fun getSubCategoryData(
        custId: Int,
        wId: Int,
        subCatId: Int,
        lang: String?
    ) = RestClient.getInstance("App home").service!!.getSubCategoryData(
        custId,
        wId,
        subCatId,
        lang
    )

    suspend fun getFlashDealExistsTime(
        custId: Int,
        wId: Int,
        mSectionType: String?
    ) = RestClient.getInstance(mSectionType).service!!.getFlashDealExistsTime(
        custId,
        wId
    )

    suspend fun getFlashDealBannerImage(
        custId: Int,
        wId: Int
    ) = apiService!!.getFlashDealBannerImage(
        custId,
        wId
    )

    /*Home Category*/
    suspend fun getCategory(
        custId: Int,
        wId: Int,
        lang: String
    ) = apiService!!.getCategory(
        custId,
        wId,
        lang
    )
    suspend fun getStoreCategories(
       custId: Int,
       wId: Int,
       baseCatId: Int,
       subCatId: Int,
       lang: String?,
    ) = RestClient.getInstance("Category Item Header").service!!.getStoreCategories(
        custId,
        wId,
        baseCatId,
        subCatId,
        lang
    )

    suspend fun getCategories(
        custId: Int,
        wId: Int,
        baseCatId: Int,
        lang: String?,
    ) = RestClient.getInstance("Category Item Header").service!!.getCategories(
        custId,
        wId,
        baseCatId,
        lang
    )

    suspend fun getRelatedItem(
        model: CatRelatedItemPostModel
    ) = apiService!!.getRelatedItem(
        model
    )

    suspend fun getItemList(
        custId: Int,
        sscatid: Int,
        scatid: Int,
        categoryId: Int,
        lang: String?,
        skip: Int,
        take: Int,
        sortType: String?,
        direction: String?
    ) = apiService!!.getItemList(
        custId,sscatid,scatid,categoryId,lang,skip,take,sortType,direction
    )

    /*Offer*/
    suspend fun getAllBillDiscountOffer(customerId: Int,sectionType:String) =
        RestClient.getInstance(sectionType).service!!.getAllBillDiscountOffer(customerId)

    suspend fun getOfferCategory(custId: Int, offerId: Int, ssCatId: Int, brandId: Int, step: Int, lang: String) =
        apiService.getOfferCategory(custId, offerId, ssCatId, brandId, step, lang)

    suspend fun getOfferItemList(custId: Int, offerId: Int, ssCatId: Int, brandId: Int, step: Int, skip: Int, take: Int, lang: String) =
        apiService.getOfferItemList(custId, offerId, ssCatId, brandId, step, skip, take, lang)

    suspend fun removeAllOffer(custId: Int, wId: Int,peopleId:Int) =
        apiService.removeAllOffer(custId, wId, peopleId)
    // My Order
    suspend fun getOrdersWithPages(customerId: Int, page: Int, total: Int, type: String) =
        apiService!!.getOrdersWithPages(customerId, page, total, type)

    suspend fun getPayLaterLimits(customerId: Int) = apiService!!.getPaylaterLimits(customerId)

    suspend fun getCustomerSalesPersons(customerId: Int) = apiService!!.getCustomerSalesPersons(customerId)

    /*Search Item*/
    suspend fun getAllCategories(customerId: Int,wId:Int,lang:String) = RestClient.getInstance("Search").service!!.getAllCategories(customerId,wId,lang)
    suspend fun getSearchItemHistory(custId: Int,wId: Int,skip: Int,take: Int,lang: String?) = RestClient.getInstance("Search").service!!.getSearchItemHistory(custId,wId,skip,take,lang)
    suspend fun getSearchItemHint(custId: Int,skip: Int,take: Int,lang: String?) = RestClient.getInstance("Search").service!!.getSearchItemHint(custId,skip,take,lang)
    suspend fun getSearchData(model: SearchItemRequestModel?) = apiService.getSearchData(model)
    suspend fun deleteSearchHintItem(customerId: Int?,keyWord:String) = apiService.deleteSearchHintItem(customerId,keyWord)

    /*Freebies*/
    suspend fun getFreebiesItem(customerId: Int,wId:Int,lang:String) = apiService.getFreebiesItem(customerId,wId,lang)
    suspend fun getStoreFreebies(customerId: Int,wId:Int,subCatId: Int,lang:String) = apiService.getStoreFreebies(customerId,wId,subCatId,lang)
    suspend fun notificationClick(customerId: Int,notificationId:Int) = apiService.notificationClick(customerId,notificationId)
    suspend fun getNotification(customerId: Int,list: Int, page: Int) = RestClient.getInstance("Notification Screen").service!!.getNotification(customerId,list,page)
    suspend fun getNotificationItem(custId: Int, wId: Int, notificationType: String?, typeId: Int, lang: String?) = apiService.getNotificationItem(custId,wId,notificationType,typeId,lang)

    /*Payment*/
    suspend fun getWalletPoint(customerId: Int, mSectionType:String) = RestClient.getInstance(mSectionType).service!!.getWalletPoint(customerId)
    suspend fun getUdharCreditLimit(customerId: Int) = apiService.getUdharCreditLimit(customerId)
    suspend fun ePayLaterCustomerLimit(skCode: String?,authHeaderQuery: String?) = RestClient.getInstance1().service1.ePayLaterCustomerLimit(skCode,"application/json",authHeaderQuery)
    suspend fun checkBookCustomerLimit(url: String?,apiKey: String?,bookCreditLimitRes: CheckBookCreditLimitRes) =  RestClient.getInstance3().service3.checkBookCustomerLimitUrl(url,apiKey,bookCreditLimitRes)
    suspend fun getCustomerCODLimit(customerId: Int) = apiService.getCustomerCODLimit(customerId)
    suspend fun getScaleUpLimit(customerId: Int) = apiService.getScaleUpLimit(customerId)
    suspend fun getDiscountOffer(customerId: Int,mSectionType:String) = RestClient.getInstance(mSectionType).service!!.getDiscountOffer(customerId)
    suspend fun getPrepaidOrder(warehouseId: Int,mSectionType:String) = RestClient.getInstance(mSectionType).service!!.getPrepaidOrder(warehouseId)
    suspend fun applyOffer(customerId: Int,warehouseId: Int,offerId: Int,isApply: Boolean,lang: String?,mSectionType:String) = RestClient.getInstance(mSectionType).service!!.applyOffer(customerId,warehouseId,offerId,isApply,lang)
    suspend fun updateScratchCardStatus(customerId: Int,offerId: Int,isScartched: Boolean) = apiService.updateScratchCardStatus(customerId,offerId,isScartched)
    suspend fun checkBillDiscountOffer(customerId: Int,offerId: String) = apiService.checkBillDiscountOffer(customerId,offerId)
    suspend fun ePayLaterConfirmOrder(authHeaderQuery: String,ePayLaterId: String,orderID:String) = RestClient.getInstance1().service1.ePayLaterConfirmOrder("application/json",authHeaderQuery,ePayLaterId,orderID)
    suspend fun getHDFCRSAKey(hdfcOrderId: String?,amount: String?,isCredit: Boolean,mSectionType:String) = RestClient.getInstance(mSectionType).service!!.getHDFCRSAKey(hdfcOrderId,amount,isCredit)
    suspend fun fetchRazorpayOrderId(orderId: String, amount: Double) = apiService.fetchRazorpayOrderId(orderId,amount)
    suspend fun creditPayment(model: CreditPayment) = apiService.creditPayment(model)
    suspend fun scaleUpPaymentInitiate(custId: Int,orderId: Int, amount: Double) = apiService.scaleUpPaymentInitiate(custId,orderId,amount)
    suspend fun getOrderPlaced(orderPlacedModel: OrderPlacedModel) = RestClient.getInstance("Retailer place order button").service!!.getOrderPlaced(orderPlacedModel)
    suspend fun getInsertOnlineTransaction(paymentReq:PaymentReq?, mSectionType:String) = RestClient.getInstance(mSectionType).service!!.getInsertOnlineTransaction(paymentReq)
    suspend fun getUpdateOnlineTransaction(paymentReq: PaymentReq?,mSectionType:String) = RestClient.getInstance(mSectionType).service!!.getUpdateOnlineTransaction(paymentReq)
    suspend fun iciciPaymentCheck(url: String,merchantId:String,merchantTxnNo:String,originalTxnNo:String,transactionType:String,secureHash:String) = apiService.iciciPaymentCheck(url,merchantId, merchantTxnNo, originalTxnNo, transactionType, secureHash)
    suspend fun updateDeliveryETA(jsonObject: JsonObject, mSectionType:String) = RestClient.getInstance(mSectionType).service!!.updateDeliveryETA(jsonObject)

    //Pay now
    suspend fun isGullakOptionEnable(custId: Int,orderId: Int) = apiService.isGullakOptionEnable(custId,orderId)

    //Shopping cart
    suspend fun getMinOrderAmount(custId: Int) = apiService.getMinOrderAmount(custId)
    suspend fun getCompanyWheelConfig(wid: Int) = apiService.getCompanyWheelConfig(wid)
    suspend fun getCartDelResponse(cartAddItemModel: CartAddItemModel) = apiService.getCartDelResponse(cartAddItemModel)
    suspend fun clearCartItem(customerId: Int, warehouseId: Int) = apiService.clearCartItem(customerId,warehouseId)
    suspend fun getOfferItem(customerId: Int, warehouseId: Int,lang: String) = apiService.getOfferItem(customerId,warehouseId,lang)
    suspend fun getCartDealItem(customerId: Int, warehouseId: Int, skip: Int, take: Int, lang: String) = apiService.getCartDealItem(customerId,warehouseId,skip,take,lang)

    //All brands
    suspend fun getAllBrands(customerId: Int, warehouseId: Int, lang: String) = apiService.getAllBrands(customerId,warehouseId,lang)
    suspend fun getBrandItem(customerId: Int, warehouseId: Int,subSubCatId: Int, lang: String) = apiService.getBrandItem(customerId,warehouseId,subSubCatId,lang)
    suspend fun getImage(categoryId: Int) = apiService.getImage(categoryId)



}