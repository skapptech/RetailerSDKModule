package app.retailer.krina.shop.com.mp_shopkrina_retailer.data.api

import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.auth.CityModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.auth.CustomerAddressModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.auth.EditProfileModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.auth.GetLokedCusResponse
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.auth.GstInfoResponse
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.auth.MyProfileResponse
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.auth.NewSignupRequest
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.auth.OTPResponse
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.auth.OtpVerifyRequest
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.auth.RegistrationResponse
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.auth.SignupResponse
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.auth.TokenResponse
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.BottomCall
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.CartAddItemModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.ItemListModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.ItemListResponse
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.MurliStoryResponse
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.RatingModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.appHome.GameBannerModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.appHome.HomeDataModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.appHome.HomeOfferFlashDealModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.category.HomeCategoryResponse
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.freebies.OfferDataModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.offer.BillDiscountListResponse
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.offer.CheckBillDiscountResponse
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.searchItem.SearchFilterModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.searchItem.SearchItemHistoryModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.searchItem.SearchItemRequestModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.store.StoreItemModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.subCategory.RelatedModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.notification.NotificationResponse
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.payment.CheckBookCreditLimitRes
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.payment.CreditLimit
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.payment.EpayLaterResponse
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.payment.OrderMaster
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.payment.OrderPlacedNewResponse
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.payment.PrepaidOrder
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.payment.ScaleUpResponse
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.shoppingCart.CartDealResponse
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.shoppingCart.CheckoutCartResponse
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.shoppingCart.CompanyWheelConfig
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.splash.AppVersionModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.splash.CompanyInfoResponse
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.wallet.MyWalletResponse
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.wallet.WalletResponse
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.PaylaterLimitsResponseModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.TargetResponseModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.categoryBean.CategoriesModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.AddCustomerModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.AddGamePointModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.AllBrandsModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.AppHomeItemGoldenDealModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.AppHomeItemModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.BucketCustomerGamesHistoryModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.BucketCustomerModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.ClearanceItemModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.ConformOrderModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.CreditPayment
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.CustomerBalance
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.CustomerHoliday
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.CustomerRes
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.DeliveryConcern
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.DocTypeModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.FaqModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.GameModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.GullakModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.HisabDetailModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.MembershipModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.MembershipPlanModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.MyAgentModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.MyDreamModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.MyExpiringWalletModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.MyFavModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.MyUdharPojo
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.NewTargetModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.OrderDetailsModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.OrderSummaryModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.ReferralConfigModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.ReferralModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.ReferredModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.ReturnOrderItemModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.ReturnOrderListModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.ReturnOrderStatusModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.subCategory.SubCatImageModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.SupplierPaymentModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.TrackOrderModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.UpdateContactModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.UploadAudioModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.YourLevelTargetModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.postModels.CatRelatedItemPostModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.postModels.ChangePasswordModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.postModels.ClearanceOfferResponse
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.postModels.ClearanceShoppingCart
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.postModels.ContactUploadModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.postModels.DeliveryFeedbackModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.postModels.DreamModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.postModels.EPayPartnerModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.postModels.EpayLaterDetail
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.postModels.FeedbackModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.postModels.GstUpdateCustomerModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.postModels.ItemIdPostModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.postModels.LatLongModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.postModels.LoginModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.postModels.OrderPlacedModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.postModels.PaymentReq
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.postModels.PaymentRequestModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.postModels.PostIssuesCategoryModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.postModels.PostReturnOrderModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.postModels.PostUPIPaymentResponse
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.postModels.PrimePaymentModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.postModels.RequestBrandModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.postModels.SearchClearanceItemDc
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.postModels.SignupModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.responseModel.BucketGameResponse
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.responseModel.ClearanceOrderResponse
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.responseModel.CommonResponse
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.responseModel.CustomerTargetResponse
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.responseModel.DialEarningResponse
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.responseModel.EtaDates
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.responseModel.FeedBackResponse
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.responseModel.GamesBannerModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.responseModel.GetOrderAtFeedbackModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.responseModel.ImageResponse
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.responseModel.IssuesCategoryModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.responseModel.LoginResponse
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.responseModel.MyIssueDetailModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.responseModel.MyIssuesResponseModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.responseModel.MyUdharResponse
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.responseModel.PrimePaymentResponse
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.responseModel.SupplierPaymentResponce
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.responseModel.TradeOfferResponse
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.saleReturn.PostKKReturnReplaceRequestDc
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.saleReturn.ReturnItemModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.saleReturn.ReturnOrderBatchItemModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.saleReturn.SaleReturnRequestResponseModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.saleReturn.SalesReturnRequestListDetailsModel
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.saleReturn.SalesReturnRequestListModel
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Streaming
import retrofit2.http.Url

/**
 * Created by User on 03-11-2018.
 */
interface APIServices {

    //Auth API
    @GET("/api/RetailerApp/Genotp")
    suspend fun getOtp(
        @Query("MobileNumber") MobileNumber: String?,
        @Query("deviceId") deviceId: String?,
        @Query("Apphash") keyHash: String?,
        @Query("mode") mode: String?
    ): Response<OTPResponse?>?

    @GET("/api/RetailerApp/ShowOtherLoginOption")
    suspend fun showOtherLoginOption(): Response<Boolean>

    @POST("/api/RetailerApp/CheckOTP")
    suspend fun callVerifyOtp(@Body verifyOtpModel: OtpVerifyRequest?): Response<Boolean>

    @GET("/api/RetailerApp/GetCustomerloged")
    suspend fun getCustVerifyInfo(
        @Query("MobileNumber") MobileNumber: String?,
        @Query("IsOTPverified") IsOTPverified: String?,
        @Query("fcmid") fcmid: String?,
        @Query("CurrentAPKversion") CurrentAPKversion: String?,
        @Query("PhoneOSversion") PhoneOSversion: String?,
        @Query("UserDeviceName") UserDeviceName: String?,
        @Query("IMEI") IMEI: String?
    ): Response<GetLokedCusResponse?>?

    @FormUrlEncoded
    @POST("/token")
    suspend fun getToken(
        @Field("grant_type") grant_type: String?,
        @Field("username") username: String?,
        @Field("password") password: String?
    ): Response<TokenResponse?>?

    @POST("/api/RetailerApp/InsertCustomer")
    suspend fun insertCustomer(@Query("MobileNumber") mobileNumber: String?): Response<RegistrationResponse?>?

    @GET
    suspend fun searchAddress(@Url url: String): Response<JsonObject>

    @PUT("/api/RetailerApp/RetailerUpdateCustomerV2")
    suspend fun editCustomerInfo(@Body model: EditProfileModel?): Response<MyProfileResponse?>?


    @PUT("/api/RetailerApp/SignupUpdate")
    suspend fun doNewSignup(@Body Model: NewSignupRequest?): Response<SignupResponse?>?

    @GET("/api/RetailerApp/GetCustomerAddress")
    suspend fun getCustAddress(
        @Query("CustomerId") customerId: Int,
    ): Response<CustomerAddressModel?>?

    @GET("/api/RetailerApp/GetCity")
    suspend fun getCity(): Response<ArrayList<CityModel>>

    @Multipart
    @POST("/api/imageupload")
    suspend fun imageUpload(@Part body: MultipartBody.Part?): Response<JsonObject>

    @GET("api/RetailerApp/CustomerGSTVerify")
    suspend fun getGstStatus(@Query("GSTNO") gst: String?): Response<GstInfoResponse>

    @GET("/api/RetailerApp/GetCustomerDocType")
    suspend fun getCustomerDocType(
        @Query("warehouseId") wId: Int, @Query("customerId") custId: Int
    ): Response<ArrayList<DocTypeModel>>

    /*-----Splash-----*/
    @GET("api/RetailerApp/RetailerAppVersion")
    suspend fun getAppVersion(): Response<ArrayList<AppVersionModel>>

    @GET("/api/RetailerApp/Customerprofile")
    suspend fun getMyProfile(
        @Query("customerid") customerId: Int, @Query("deviceId") deviceId: String?
    ): Response<MyProfileResponse>

    @GET("/api/RetailerApp/GetCompanyDetailsForRetailerWithToken")
    suspend fun getCompanyDetailWithToken(@Query("customerid") customerId: Int): Response<CompanyInfoResponse>

    /*-----HomeActivity-----*/
    @GET("/api/Udhar/UdharOverDueDayRestrication")
    suspend fun getUdhaarOverDue(
        @Query("CustomerId") customerId: Int,
        @Query("AppType") appType: String,
        @Query("lang") lang: String
    ): Response<JsonObject>

    @GET("api/ShoppingCart/GetCustomerCart")
    suspend fun getCustomerCart(
        @Query("customerId") customerId: Int,
        @Query("warehouseId") warehouseId: Int,
        @Query("lang") lang: String?
    ): Response<CheckoutCartResponse>

    @GET("api/RetailerApp/GetCustomerWalletPoints")
    suspend fun getWalletPointNew(
        @Query("CustomerId") customerId: Int, @Query("page") pageNo: Int
    ): Response<MyWalletResponse>

    @GET("/api/RetailerApp/GetApphomeBottomCall")
    suspend fun getAppHomeBottomCall(@Query("customerId") customerId: Int): Response<ArrayList<BottomCall>>

    @GET("/api/DistributorApp/GetDistributorGullak")
    suspend fun getGullakBalance(@Query("customerId") custId: Int): Response<JsonObject>

    @GET("/api/Payments/GetCustomerRTGSAmount")
    suspend fun getRTGSBalance(@Query("customerId") cId: Int): Response<Double>

    @GET("/api/RetailerApp/GetVATMUrl")
    suspend fun getVATMUrl(
        @Query("CustomerId") customerId: Int, @Query("warehouseId") wId: Int
    ): Response<String>

    @GET("/api/RetailerApp/NotifyMe")
    suspend fun getNotifyItems(
        @Query("CustomerId") custId: Int,
        @Query("warehouseId") wId: Int,
        @Query("itemNumber") itemNumber: String?
    ): Response<Boolean?>?

    @GET
    suspend fun generateLead(@Url url: String?): Response<JsonObject?>?

    @GET("api/ScaleUpIntegration/LeadInitiate")
    suspend fun scaleUpLeadInitiate(
        @Query("customerId") custId: Int,
    ): Response<JsonObject>

    @GET
    suspend fun scaleUpLeadInitiateUsingUrl(@Url url: String?): Response<JsonObject>

    @POST("api/ShoppingCart/AddItem")
    suspend fun getCartResponse(@Body cartAddItemModel: CartAddItemModel?): Response<JsonObject?>?

    @GET("/api/itemMaster/GetMurliAudioForMobile")
    suspend fun getMurliAudioForMobile(
        @Query("customerId") customerId: Int, @Query("warehouseId") warehouseId: Int
    ): Response<JsonObject>

    @Streaming
    @GET
    suspend fun downloadFileWithUrl(@Url fileUrl: String?): Response<ResponseBody>

    @GET("/api/MurliStory/GetPublishedStory")
    suspend fun getMurliPublishedStory(
        @Query("customerId") customerId: Int, @Query("warehouseId") warehouseId: Int
    ): Response<MurliStoryResponse?>?

    @GET
    suspend fun getGetDboyRatingOrder(@Url url: String?): Response<ArrayList<RatingModel>>

    @GET
    suspend fun getGetDboyRatingOrderOther(@Url url: String?): Response<RatingModel>

    @POST("/api/RetailerApp/InsertRating")
    suspend fun addRating(@Body model: RatingModel?): Response<Boolean>

    /*App Home*/
    @GET("/api/RetailerApp/GetPublishedSection")
    suspend fun getAppHomeSection(
        @Query("appType") appType: String?,
        @Query("customerId") customerId: Int,
        @Query("wId") wId: Int,
        @Query("lang") lang: String?,
        @Query("lat") lat: Double,
        @Query("lg") lng: Double
    ): Response<ArrayList<HomeDataModel>>

    @GET
    fun getDynamicHtml(@Url url: String?): Observable<String?>?

    @GET("/api/RetailerApp/RetailerGetItemBySection")
    fun getItemBySection(
        @Query("customerId") custId: Int,
        @Query("Warehouseid") wId: Int,
        @Query("sectionid") sectionId: String?,
        @Query("lang") lang: String?
    ): Observable<JsonObject?>?

    @GET
    fun getOtherItemsHome(@Url url: String?): Observable<AppHomeItemGoldenDealModel?>?

    @GET
    suspend fun getOtherItemsHome1(@Url url: String?): Response<AppHomeItemGoldenDealModel?>?

    @GET("api/RetailerApp/GetAllStore")
    fun getAllStore(
        @Query("customerId") custId: Int,
        @Query("warehouseId") wId: Int,
        @Query("lang") lang: String?
    ): Observable<ArrayList<StoreItemModel>>

    @GET("api/Gamification/GameBanner")
    suspend fun getGameBanners(
        @Query("customerId") custId: Int,
    ): Response<GameBannerModel?>

    @GET("/api/RetailerApp/RatailerFlashDealoffer")
    suspend fun getFlashDealItem(
        @Query("CustomerId") custId: Int,
        @Query("Warehouseid") wId: Int,
        @Query("sectionid") sectionId: String?,
        @Query("lang") lang: String?
    ): Response<HomeOfferFlashDealModel?>?

    @GET("/api/RetailerApp/RetailerStoreFlashDealoffer")
    suspend fun getStoreFlashDeal(
        @Query("CustomerId") custId: Int,
        @Query("Warehouseid") wId: Int,
        @Query("sectionid") sectionId: String?,
        @Query("SubCategoryId") subCatId: Int,
        @Query("lang") lang: String?
    ): Response<HomeOfferFlashDealModel?>?

    @GET("/api/RetailerApp/RetailerSubCategoryOffer")
    suspend fun getSubCateOffer(
        @Query("CustomerId") customerId: Int, @Query("SubCategoryId") subCategoryId: Int
    ): Response<BillDiscountListResponse?>?

    @GET("/api/RetailerApp/GetStoreHome")
    suspend fun storeDashboard(
        @Query("customerId") custId: Int,
        @Query("wId") wId: Int,
        @Query("subCategoryId") subCatId: Int,
        @Query("lang") lang: String?
    ): Response<ArrayList<HomeDataModel>>

    @GET("api/RetailerApp/RetailerHomePageGetSubSubCategories")
    suspend fun getSubCategoryData(
        @Query("customerId") customerId: Int,
        @Query("wid") wid: Int,
        @Query("subCategoryId") subCategoryId: Int,
        @Query("lang") lang: String?
    ): Response<JsonObject>

    @GET("/api/RetailerApp/RatailerFlashExists")
    suspend fun getFlashDealExistsTime(
        @Query("CustomerId") custId: Int, @Query("Warehouseid") wId: Int
    ): Response<JsonObject>

    @GET("/api/RetailerApp/RatailerFlashBannerUrl")
    suspend fun getFlashDealBannerImage(
        @Query("CustomerId") custId: Int, @Query("Warehouseid") wId: Int
    ): Response<String>

    /*Home Category*/
    @GET("/api/RetailerApp/RetailerCategory")
    suspend fun getCategory(
        @Query("customerId") custId: Int,
        @Query("warehouseid") wId: Int,
        @Query("lang") lang: String?
    ): Response<HomeCategoryResponse>

    /*Home Sub Sub Category*/
    @GET("api/RetailerApp/GetStoreCategories")
    suspend fun getStoreCategories(
        @Query("customerId") custId: Int,
        @Query("wid") wId: Int,
        @Query("baseCategoryId") baseCatId: Int,
        @Query("subCategoryId") subCatId: Int,
        @Query("lang") lang: String?,
    ): Response<JsonObject>

    @GET("api/RetailerApp/RetailerHomePageGetCategories")
    suspend fun getCategories(
        @Query("customerId") custId: Int,
        @Query("wid") wId: Int,
        @Query("itemId") baseCatItemId: Int,
        @Query("lang") lang: String?
    ): Response<JsonObject>


    @POST("/api/RetailerApp/RetailerGetRelatedItem")
    suspend fun getRelatedItem(@Body model: CatRelatedItemPostModel?): Response<RelatedModel>

    @GET("/api/RetailerApp/RetailerGetItembycatesscatid")
    suspend fun getItemList(
        @Query("customerId") custId: Int,
        @Query("sscatid") sscatid: Int,
        @Query("scatid") scatid: Int,
        @Query("catid") categoryId: Int,
        @Query("lang") lang: String?,
        @Query("skip") skip: Int,
        @Query("take") take: Int,
        @Query("sortType") sortType: String?,
        @Query("direction") direction: String?
    ): Response<ItemListResponse?>?

    /*offer*/
    @GET("/api/RetailerApp/RetailerCommonDiscountOffer")
    suspend fun getAllBillDiscountOffer(@Query("CustomerId") customerId: Int): Response<BillDiscountListResponse?>?

    @GET("api/RetailerApp/GetOfferCompanybrandList")
    suspend fun getOfferCategory(
        @Query("customerId") custId: Int,
        @Query("offerId") offerId: Int,
        @Query("SubCategoryId") subCategoryId: Int,
        @Query("brandId") brandId: Int,
        @Query("step") step: Int,
        @Query("lang") lang: String?
    ): Response<JsonObject?>?

    @GET("/api/RetailerApp/GetOfferVisibilityItem")
    suspend fun getOfferItemList(
        @Query("customerId") customerId: Int,
        @Query("offerId") offerId: Int,
        @Query("SubCategoryId") subCatId: Int,
        @Query("brandId") brandId: Int,
        @Query("step") step: Int,
        @Query("skip") skip: Int,
        @Query("take") take: Int,
        @Query("lang") lang: String?
    ): Response<CartDealResponse?>?

    @GET("/api/RetailerApp/RemoveOffer")
    suspend fun removeAllOffer(
        @Query("customerId") customerId: Int,
        @Query("WarehouseId") offerId: Int,
        @Query("PeopleId") peopleId: Int
    ): Response<Boolean?>?

    /*Search Item*/
    @GET("/api/RetailerApp/RetailerAllCategory")
    suspend fun getAllCategories(
        @Query("customerId") custId: Int,
        @Query("warehouseid") wId: Int,
        @Query("lang") lang: String?
    ): Response<SearchFilterModel?>?

    @GET("/api/RetailerApp/RetailerGetSearchItem")
    suspend fun getSearchItemHistory(
        @Query("customerId") custId: Int,
        @Query("warehouseId") wId: Int,
        @Query("skip") skip: Int,
        @Query("take") take: Int,
        @Query("lang") lang: String?
    ): Response<SearchItemHistoryModel?>?


    @GET("/api/RetailerApp/RetailerGetCustomerSearchKeyword")
    suspend fun getSearchItemHint(
        @Query("customerId") customerId: Int,
        @Query("skip") skip: Int,
        @Query("take") take: Int,
        @Query("lang") lang: String?
    ): Response<JsonArray?>?

    @POST("/api/RetailerApp/RetailerItemSearch")
    suspend fun getSearchData(@Body model: SearchItemRequestModel?): Response<ItemListResponse?>?

    @GET("/api/itemMaster/DeleteCustomerSearchItem")
    suspend fun deleteSearchHintItem(
        @Query("customerId") customerId: Int?, @Query("keyword") keyword: String?
    ): Response<String?>?

    /*Freebies*/
    @GET("api/RetailerApp/RetailerGetOfferItem")
    suspend fun getFreebiesItem(
        @Query("CustomerId") custId: Int,
        @Query("WarehouseId") wId: Int,
        @Query("lang") lang: String?
    ): Response<OfferDataModel?>?

    @GET("api/RetailerApp/RetailerGetStoreOfferItem")
    suspend fun getStoreFreebies(
        @Query("CustomerId") custId: Int,
        @Query("WarehouseId") warehouseId: Int,
        @Query("SubCategoryId") subCatId: Int,
        @Query("lang") lang: String?
    ): Response<OfferDataModel?>?

    /*Notification*/
    @PUT("/api/Notification/NotificationClick")
    suspend fun notificationClick(
        @Query("CustomerId") customerId: Int, @Query("NotificationId") notificationId: Int
    ): Response<JsonObject?>?

    @GET("/api/RetailerApp/RetailerGetallNotification")
    suspend fun getNotification(
        @Query("customerid") customerId: Int,
        @Query("list") list: Int,
        @Query("page") page: Int
    ): Response<NotificationResponse?>?

    @GET("/api/RetailerApp/RetailerGetNotificationItems")
    suspend fun getNotificationItem(
        @Query("customerId") custId: Int,
        @Query("wareHouseId") wId: Int,
        @Query("notificationType") notificationType: String?,
        @Query("typeId") typeId: Int,
        @Query("lang") lang: String?
    ): Response<ArrayList<ItemListModel>>


    //Payment
    @GET("/api/RetailerApp/RetailerWallet")
    suspend fun getWalletPoint(@Query("CustomerId") customerId: Int): Response<WalletResponse>

    @GET("api/Udhar/GetUdharCreditLimit")
    suspend fun getUdharCreditLimit(@Query("CustomerId") customerId: Int): Response<CreditLimit>

    @GET("/marketplace/b2b/customer/{skCode}/creditLimit")
    suspend fun ePayLaterCustomerLimit(
        @Path("skCode") skCode: String?,
        @Header("Content-Type") contentType: String?,
        @Header("Authorization") authHeaderQuery: String?
    ): Response<JsonObject>

    @POST
    suspend fun checkBookCustomerLimitUrl(
        @Url Url: String?,
        @Header("API-key") apiKey: String?,
        @Body bookCreditLimitRes: CheckBookCreditLimitRes?
    ): Response<JsonObject>

    @GET("/api/OrderMastersAPI/GetCustomerCODLimit/{CustomerId}")
    suspend fun getCustomerCODLimit(@Path("CustomerId") custId: Int): Response<JsonElement>

    @GET("api/ScaleUpIntegration/GetScaleUpLimit")
    suspend fun getScaleUpLimit(
        @Query("customerId") custId: Int,
    ): Response<CreditLimit>

    @GET("/api/RetailerApp/RetailerCommonDiscountOffer")
    suspend fun getDiscountOffer(@Query("CustomerId") customerId: Int): Response<BillDiscountListResponse>

    @GET("/api/RetailerApp/GetPrepaidOrder")
    suspend fun getPrepaidOrder(@Query("WarehouseId") wId: Int): Response<PrepaidOrder>


    @GET("api/ShoppingCart/ApplyNewOffer")
    suspend fun applyOffer(
        @Query("customerId") customerId: Int,
        @Query("warehouseId") warehouseId: Int,
        @Query("offerId") offerId: Int,
        @Query("IsApply") IsApply: Boolean,
        @Query("lang") lang: String?
    ): Response<CheckoutCartResponse>


    @PUT("/api/ScratchBillDiscountOfferApp/UpdateScratchOfferById")
    suspend fun updateScratchCardStatus(
        @Query("CustomerId") CustomerId: Int,
        @Query("OfferId") OfferId: Int,
        @Query("IsScartched") IsScartched: Boolean
    ): Response<CheckBillDiscountResponse>

    @GET("/api/RetailerApp/RetailerCheckOffer")
    suspend fun checkBillDiscountOffer(
        @Query("CustomerId") customerId: Int, @Query("OfferId") offerId: String?
    ): Response<CheckBillDiscountResponse>

    @PUT("/transaction/v2/{ePayLaterId}/confirmed/{orderID}?delivered=true")
    suspend fun ePayLaterConfirmOrder(
        @Header("Content-Type") content_type: String?,
        @Header("Authorization") authHeaderQuery: String?,
        @Path("ePayLaterId") ePayLaterId: String?,
        @Path("orderID") orderID: String?
    ): Response<EpayLaterResponse>

    @GET("/api/HDFCPayment/GetRSA")
    suspend fun getHDFCRSAKey(
        @Query("hdfcOrderId") hdfcOrderId: String?,
        @Query("amount") amount: String?,
        @Query("IsCredit") isCredit: Boolean
    ): Response<String>

    @GET("api/ConsumerApp/RazorPayCreateOrder")
    suspend fun fetchRazorpayOrderId(
        @Query("orderId") orderId: String,
        @Query("Amount") amount: Double
    ): Response<String>

    @POST("api/Udhar/GeneratePayment")
    suspend fun creditPayment(@Body model: CreditPayment?): Response<CreditLimit>

    @GET("api/ScaleUpIntegration/ScaleUpPaymentInitiate")
    suspend fun scaleUpPaymentInitiate(
        @Query("customerId") custId: Int,
        @Query("OrderId") orderId: Int,
        @Query("TransactionAmount") amount: Double,
    ): Response<ScaleUpResponse>

    @POST("/api/OrderMastersAPI/V6")
    suspend fun getOrderPlaced(@Body orderPlacedModel: OrderPlacedModel?): Response<OrderPlacedNewResponse>

    @POST("/api/OrderMastersAPI/InsertOnlineTransactionV2")
    suspend fun getInsertOnlineTransaction(@Body paymentReq: PaymentReq?): Response<Boolean>

    @POST("/api/OrderMastersAPI/UpdateOrderForOnlinePaymentV2")
    suspend fun getUpdateOnlineTransaction(@Body paymentReq: PaymentReq?): Response<Boolean>

    @POST("/api/OrderMastersAPI/UpdateETANew")
    suspend fun updateDeliveryETA(@Body jsonObject: JsonObject?): Response<JsonObject>

    @POST
    @FormUrlEncoded
    suspend fun iciciPaymentCheck(
        @Url Url: String?,
        @Field("merchantId") merchantId: String,
        @Field("merchantTxnNo") merchantTxnNo: String,
        @Field("originalTxnNo") originalTxnNo: String,
        @Field("transactionType") transactionType: String,
        @Field("secureHash") secureHash: String,
    ): Response<JsonObject>


    //Pay now
    @GET("/api/Orders/GullakPaymentOptionEnable/{customerId}/{orderId}")
    suspend fun isGullakOptionEnable(
        @Path("customerId") customerId: Int, @Path("orderId") orderId: Int
    ): Response<Boolean>

    //Shopping cart

    @GET("api/RetailerApp/GetRetailerMinOrderAmount?")
    suspend fun getMinOrderAmount(@Query("customerId") customerId: Int): Response<Double>

    @GET("/api/RetailerApp/GetCompanyWheelConfig?")
    suspend fun getCompanyWheelConfig(@Query("WarehouseId") wid: Int): Response<CompanyWheelConfig>

    @POST("api/ShoppingCart/DeleteItem")
    suspend fun getCartDelResponse(@Body cartAddItemModel: CartAddItemModel?): Response<JsonObject>

    @GET("api/ShoppingCart/ClearCart")
    suspend fun clearCartItem(
        @Query("customerId") customerId: Int, @Query("warehouseId") warehouseId: Int
    ): Response<JsonObject>

    @GET("/api/itemMaster/GetOfferItem")
    suspend fun getOfferItem(
        @Query("customerId") customerId: Int,
        @Query("warehouseId") warehouseId: Int,
        @Query("lang") lang: String?
    ): Response<ArrayList<ItemListModel>>

    @GET("/api/RetailerApp/GetCartDealItem")
    suspend fun getCartDealItem(
        @Query("customerId") custId: Int,
        @Query("warehouseId") wId: Int,
        @Query("skip") skip: Int,
        @Query("take") take: Int,
        @Query("lang") lang: String,
    ): Response<CartDealResponse>

    //All Brands
    @GET("api/RetailerApp/RetailerGetAllBrand")
    suspend fun getAllBrands(
        @Query("customerId") custId: Int,
        @Query("WarehouseId") wId: Int,
        @Query("lang") lang: String?
    ): Response<ArrayList<AllBrandsModel>>

    @GET("api/RetailerApp/RetailerGetAllItemByBrand")
    suspend fun getBrandItem(
        @Query("customerId") custId: Int,
        @Query("warehouseid") wId: Int,
        @Query("SubsubCategoryid") subSubCatId: Int,
        @Query("lang") lang: String?
    ): Response<ItemListResponse>

    @GET("/api/RetailerApp/RetailerGetCategoryImage")
    suspend fun getImage(@Query("CategoryId") categoryId: Int): Response<ArrayList<SubCatImageModel>>
    /*
         Old APi
        --------------------------------------------
    */

    @Multipart
    @POST("/api/imageupload")
    fun imageUpload1(@Part body: MultipartBody.Part?): Observable<ImageResponse>

    @Multipart
    @POST("/api/imageupload/customerPan")
    fun uploadPanImage(@Part body: MultipartBody.Part?): Observable<ImageResponse?>?

    @Multipart
    @POST("/api/imageupload/CustomerIdProff")
    fun uploadCustomerImage(@Part body: MultipartBody.Part?): Observable<ImageResponse?>?

    @Multipart
    @POST("/api/imageupload/customerBack")
    fun uploadCustomerBackImage(@Part body: MultipartBody.Part?): Observable<ImageResponse?>?

    @Multipart
    @POST("/api/HisabKitab/UploadHisabKitabImage")
    fun uploadHisabKitabImage(@Part body: MultipartBody.Part?): Observable<String?>?

    @GET("/api/Udhar/UdharOverDueDayRestrication")
    fun getUdhaarOverDue1(
        @Query("CustomerId") customerId: Int,
        @Query("AppType") appType: String,
        @Query("lang") lang: String
    ): Observable<JsonObject>

    //@POST("/api/signup/V2")
    @POST("/api/RetailerApp/Singup")
    fun doLogin(@Body loginModel: LoginModel?): Observable<LoginResponse?>?

    @FormUrlEncoded
    @POST("/token")
    fun getToken1(
        @Field("grant_type") grant_type: String?,
        @Field("username") username: String?,
        @Field("password") password: String?
    ): Observable<TokenResponse?>?

    //@GET("/api/Customers/Forgrt/V2")
    @GET("/api/RetailerApp/ForgetPassword")
    fun postforgetPassword(@Query("Mobile") Mobile: String?): Observable<JsonObject?>?

    @PUT("/api/RetailerApp/RetailerUpdateCustomerV2")
    fun putEditProfile(@Body model: EditProfileModel?): Observable<MyProfileResponse?>?

    @GET("/api/RetailerApp/Genotp")
    fun getOtp1(
        @Query("MobileNumber") MobileNumber: String?,
        @Query("deviceId") deviceId: String?,
        @Query("Apphash") keyHash: String?,
        @Query("mode") mode: String?
    ): Observable<OTPResponse?>?

    // @GET("/api/Customers/GetLogedCust")


    /* @GET("/api/RetailerApp/GetCustomerloged")
    Observable<GetLokedCusResponse> getCustVerifyInfo(@Body JsonObject body);*/


    //Check Referral Code
    @POST("api/TradeCustomer/CheckReferralSkCode")
    fun checkReferral(@Body model: ReferralModel?): Observable<ReferralModel?>?

    //@GET("api/signup/GSTVerify")
    @GET("api/RetailerApp/CustomerGSTVerify")
    fun getGstStatus1(@Query("GSTNO") gst: String?): Observable<GstInfoResponse>

    //@GET("/api/City")
    @get:GET("/api/RetailerApp/GetCity")
    val city: Observable<ArrayList<CityModel>>

    @GET("/api/RetailerApp/GetCustomerDocType")
    fun getCustomerDocType1(
        @Query("warehouseId") wId: Int, @Query("customerId") custId: Int
    ): Observable<ArrayList<DocTypeModel?>?>?

    @POST("/api/RetailerApp/SignupUpdateBasicInfo")
    fun signupUpdateBasicInfo(@Body Model: NewSignupRequest?): Observable<SignupResponse?>?


    @PUT("/api/RetailerApp/RetailerChangePassword")
    fun postChangePassword(@Body model: ChangePasswordModel?): Observable<JsonObject?>?


    @GET("/api/RetailerApp/Customerprofile")
    fun getMyProfile1(
        @Query("customerid") customerId: Int, @Query("deviceId") deviceId: String?
    ): Observable<MyProfileResponse?>?

    @GET("/api/RetailerApp/GetCompanyDetailsForRetailerWithToken")
    fun getCompanyDetailWithToken1(@Query("customerid") customerId: Int): Observable<CompanyInfoResponse?>?

    @GET("/api/RetailerApp/GetFavoriteItem")
    fun getItemFav(@Query("customerId") customerId: Int): Observable<JsonArray?>?

    // @GET("/api/Customers/AddFavorite")
    @GET("/api/RetailerApp/AddCustomerFavorite")
    fun addItemFav(
        @Query("customerId") customerId: Int,
        @Query("itemId") itemId: Int,
        @Query("isLike") isLike: Boolean
    ): Observable<CommonResponse?>?

    // @POST("/api/Customers/favourite")
    @POST("/api/RetailerApp/RetailerFavourite")
    fun getFaveItemList(@Body myFavModel: MyFavModel?): Observable<AppHomeItemModel?>?

    //@GET("api/Customers/GetWalletPoints")
    @GET("api/RetailerApp/GetCustomerWalletPoints")
    fun getWalletPointNew1(
        @Query("CustomerId") CustomerId: Int, @Query("page") pageNo: Int
    ): Observable<MyWalletResponse?>?

    //@GET("/api/AppHomeSection/GetPublishedSection")


    //@GET("/api/offeritem/GetItemBySectionV2")


    //@GET("/api/offeritem/itemsgetbyofferV1")
    @GET("/api/RetailerApp/RatailerFlashDealoffer")
    fun getOfferItemByFlashDeal1(
        @Query("Warehouseid") wId: Int,
        @Query("sectionid") sectionId: String?,
        @Query("CustomerId") custId: Int,
        @Query("lang") lang: String?
    ): Observable<HomeOfferFlashDealModel?>?


    //@GET("/api/Notification/getall")


    // @GET("/api/NotificationUpdated/GetNotificationItems")

    // @GET("api/SubsubCategory/GetAllBrand/V2")


    // @GET("api/ssitem/GetAllItemByBrand/V3")


    //@GET("/api/itemMaster/GetSearchItem")


    //@GET("/api/itemMaster/GetCustomerSearchKeyword")


    //@POST("/api/itemMaster/V4")


    @GET("/api/RetailerApp/RetailerHomePageGetCategories")
    fun getSubcategory(
        @Query("itemId") itemid: String?,
        @Query("customerId") custId: Int,
        @Query("wid") wId: Int,
        @Query("lang") lang: String?
    ): Observable<JsonObject?>?


    // @GET("/api/itemMaster/MultiMoqItem")
    @GET("/api/RetailerApp/RetailerMultiMoqItem")
    fun getTreadIteamIteam(
        @Query("customerId") custId: Int,
        @Query("Warehouseid") wId: Int,
        @Query("lang") lang: String?
    ): Observable<TradeOfferResponse?>?

    //@GET("/api/ssitem/getItembycatesscatid")

    //@POST("/api/itemMaster/GetRelatedItem")

    //  @POST("api/ShoppingCart/AddItem")
    @POST("api/ShoppingCart/AddItem")
    fun getCartResponse1(@Body cartAddItemModel: CartAddItemModel?): Observable<JsonObject?>?

    //@POST("api/ShoppingCart/DeleteItem")

    // @GET("api/ShoppingCart/ClearCart")
    @GET("api/ShoppingCart/ClearCart")
    fun clearCartItem1(
        @Query("customerId") customerId: Int, @Query("warehouseId") warehouseId: Int
    ): Observable<JsonObject?>?

    // @GET("api/ShoppingCart/GetCustomerCart")
    @GET("api/ShoppingCart/GetCustomerCart")
    fun getCustomerCart1(
        @Query("customerId") customerId: Int,
        @Query("warehouseId") warehouseId: Int,
        @Query("lang") lang: String?
    ): Observable<CheckoutCartResponse?>?

    // @GET("/api/BillDiscountOfferApp/CommonDiscountOffer/V2")
    @GET("/api/RetailerApp/RetailerCommonDiscountOffer")
    fun getAllBillDiscountOffer11(@Query("CustomerId") CustomerId: Int): Observable<BillDiscountListResponse?>?

    @GET("/api/RetailerApp/RetailerSubCategoryOffer")
    fun getSubCateOffer1(
        @Query("CustomerId") CustomerId: Int, @Query("SubCategoryId") SubCategoryId: Int
    ): Observable<BillDiscountListResponse?>?

    // @GET("/api/itemMaster/GetOfferItem")


    // @GET("/api/wallet")
    @GET("/api/RetailerApp/RetailerWallet")
    fun getWalletPoint1(@Query("CustomerId") CustomerId: Int): Observable<WalletResponse?>?

    //<Test>
    // https://api2.epaylater.in:443/marketplace/b2b/customer/2539/creditLimit
    // @GET("/user/marketplaceCustomer/{skCode}/creditLimit")
    @GET("/marketplace/b2b/customer/{skCode}/creditLimit")
    fun ePayLaterCustomerTestLimit1(
        @Header("Content-Type") content_type: String?,
        @Header("Authorization") authHeaderQuery: String?,
        @Path("skCode") customerId: String?
    ): Observable<JsonObject?>?

    //<Test> api/LadgerEntryV7/PurchasePendingCustomerLedger/32
    @GET("api/LadgerEntryV7/PurchasePendingCustomerLedger/{skCode}")
    fun getPendingPayment(@Path("skCode") customerId: Int): Observable<JsonObject?>?

    @GET("/marketplace/b2b/customer/{skCode}/creditLimit")
    fun  //<Live>
            ePayLaterCustomerLimit1(
        @Header("Content-Type") content_type: String?,
        @Header("Authorization") authHeaderQuery: String?,
        @Path("skCode") customerId: String?
    ): Observable<JsonObject?>?

    @PUT("/transaction/v2/{ePayLaterId}/confirmed/{orderID}?delivered=true")
    fun ePayLaterConfirmOrder1(
        @Header("Content-Type") content_type: String?,
        @Header("Authorization") authHeaderQuery: String?,
        @Path("ePayLaterId") ePayLaterId: String?,
        @Path("orderID") orderID: String?
    ): Observable<EpayLaterResponse?>?


    @POST("/api/OrderMastersAPI/UpdateOrderForOnlinePaymentV2")
    fun getUpdateOrderPlaced1(@Body paymentReq: PaymentReq?): Observable<Boolean?>?

    @POST("/api/OrderMastersAPI/InsertOnlineTransactionV2")
    fun getInsertOnlineTransaction1(@Body paymentReq: PaymentReq?): Observable<Boolean?>?

    @POST("/api/OrderMastersAPI/V3/PostOrderDialValue")
    fun postDialEarningPoint(@Body dialEarningModel: OrderMaster?): Observable<DialEarningResponse?>?


    @GET("/api/Orders/MyOrdersWithPage")
    suspend fun getOrdersWithPages(
        @Query("customerId") customerId: Int,
        @Query("page") pageNo: Int,
        @Query("totalOrder") totalOrder: Int,
        @Query("type") type: String?
    ): Response<ArrayList<ConformOrderModel>?>?

    @get:GET("/api/RetailerApp/RetailerRewardItem")
    val myDream: Observable<ArrayList<MyDreamModel?>?>?

    @POST("/api/OrderMastersAPI/dreamitem")
    fun getBuyProduct(@Body dreamModel: DreamModel?): Observable<JsonElement?>?

    // @POST("/api/feedback")
    @POST("/api/RetailerApp/RetailerFeedback")
    fun postfeedback(@Body model: FeedbackModel?): Observable<FeedBackResponse?>?

    @POST("/api/RetailerApp/RetailerRequest")
    fun postRequestBrand(@Body model: RequestBrandModel?): Observable<JsonElement?>?

    @get:GET("/api/Companys/VideoHelper")
    val video: Observable<ArrayList<FaqModel?>?>?

    //@PUT("/api/Customers/UpdateRating")
    @PUT("/api/RetailerApp/RetailerUpdateRating")
    fun postRating(@Body model: JsonObject?): Observable<MyProfileResponse?>?

    @POST("/api/signup/GSTUpdateCustomer")
    fun requestGstUpdateApi(@Body model: GstUpdateCustomerModel?): Observable<JsonObject?>?

    @POST("/api/RetailerApp/signupCustomer")
    fun doSignup(@Body model: SignupModel?): Observable<SignupResponse?>?

    @GET("api/DeliveryTask/GetOrderDeliveryDetail")
    fun getOrderDetails(@Query("OrderId") orderId: String?): Observable<GetOrderAtFeedbackModel?>?

    @GET("/api/TargetModule/GetCustomerTarget")
    fun getCustomerTarget(
        @Query("WarehouseId") warehouseid: Int,
        @Query("SkCode") skCode: String?,
        @Query("customerid") customerid: Int
    ): Observable<CustomerTargetResponse?>?

    @GET("/api/TargetModule/GetCustomerCompanyTarget")
    fun getCustomerSubCategoryTarget(
        @Query("warehouseId") warehouseid: Int, @Query("customerid") customerid: Int
    ): Observable<List<NewTargetModel?>?>?

    @POST("/api/itemMaster/ProductDetails")
    fun getProductDetails(@Body itemIdModel: ItemIdPostModel?): Observable<ItemListModel?>?

    @GET("/api/Customers/CustomerFeedbackQuestion")
    fun getCustomerFeedbackQuestions(@Query("WarehouseId") warehouseid: Int): Observable<JsonObject?>?

    @POST("/api/Customers/CustomerOrderFeedback")
    fun postCustomerFeedback(@Body itemIdModel: DeliveryFeedbackModel?): Observable<DeliveryFeedbackModel?>?

    @GET("/api/ssitem/getbysscatid/V3")
    fun getBrandInfo(
        @Query("customerId") customerId: Int,
        @Query("sscatid") sscatid: Int,
        @Query("lang") lang: String?
    ): Observable<ItemListResponse?>?

    @GET("/api/ssitem/V2")
    fun getSpecialCategory(
        @Query("customerId") customerId: Int,
        @Query("catid") catid: Int,
        @Query("lang") lang: String?
    ): Observable<ItemListResponse?>?

    @PUT("/api/signup/Customeraddupdate")
    fun updateLatLong(@Body latModel: LatLongModel?): Observable<MyProfileResponse?>?

    @GET("api/target/Report?")
    fun getordersummary(
        @Query("day") day: Int, @Query("skcode") skcode: String?
    ): Observable<List<OrderSummaryModel?>?>?

    @POST("/api/Myudhar")
    fun setMyUdharData(@Body model: MyUdharPojo?): Observable<MyUdharResponse?>?

    @GET("/api/ssitem/getbysscatid")
    fun getBannerItems(
        @Query("customerId") customerId: Int, @Query("sscatid") sscatid: Int
    ): Observable<ItemListResponse?>?

    @GET("/api/itemMaster/ItemDetail")
    fun getBannerItemDetail(
        @Query("customerId") customerId: Int, @Query("itemId") sscatid: Int
    ): Observable<ItemListResponse?>?

    @GET("/api/offer/GetOfferItemForMobileByOfferId")
    fun getBannerOffers(
        @Query("customerId") customerId: Int, @Query("offerId") offerId: Int
    ): Observable<ItemListResponse?>?

    @PUT("/api/ScratchBillDiscountOfferApp/UpdateScratchOfferById")
    fun updateScratchCardStatus1(
        @Query("CustomerId") CustomerId: Int,
        @Query("OfferId") OfferId: Int,
        @Query("IsScartched") IsScartched: Boolean
    ): Observable<CheckBillDiscountResponse?>?

    @GET("/api/HDFCPayment/GetRSA")
    fun getRSAKey1(
        @Query("hdfcOrderId") hdfcOrderId: String?,
        @Query("amount") amount: String?,
        @Query("IsCredit") isCredit: Boolean
    ): Observable<String?>?

    @POST("/api/Customers/GenPwdotp")
    fun getOTP(@Query("mobileNumber") mobileNumber: String?): Observable<JsonObject?>?

    @GET("/api/OrderMastersAPI/GetepayCust")
    fun getEPayLaterData(@Query("customerId") customerId: Int): Observable<JsonObject?>?

    @POST("/api/OrderMastersAPI/AddEpaycust")
    fun postEPayLaterData(@Body detail: EpayLaterDetail?): Observable<JsonObject?>?

    @POST("/api/OrderMastersAPI/AddEpayPartner")
    fun postEPayPartnerData(@Body model: EPayPartnerModel?): Observable<JsonObject?>?

    @PUT("/api/Notification/NotificationClick")
    fun putNotificationView1(
        @Query("CustomerId") CustomerId: Int, @Query("NotificationId") NotificationId: Int
    ): Observable<JsonObject?>?

    @GET("/api/TargetModule/ClaimCustomerReward")
    fun claimCustomerTarget(
        @Query("WarehouseId") warehouseId: Int,
        @Query("SKCode") skCode: String?,
        @Query("CustomerId") customerId: Int
    ): Observable<CustomerTargetResponse?>?

    @GET("/api/TargetModule/ClaimCustomerCompanyTarget")
    fun claimCustomerCompanyTarget(
        @Query("customerId") customerId: Int, @Query("targetDetailId") targetDetailId: Int
    ): Observable<CustomerTargetResponse?>?

    @POST("/api/LadgerEntryV7/CustomerLedgerForRetailerApp")
    fun getCustomerLedger(@Body model: SupplierPaymentModel?): Observable<SupplierPaymentResponce?>?

    @POST("/api/CustomerLedger/GenerateCustomerReport")
    fun getCustomerLedgerPDF(@Body model: SupplierPaymentModel?): Observable<JsonObject?>?

    @GET("/api/itemMaster/GetMurliAudioForMobile")
    fun getMurliAudioForMobile1(
        @Query("customerId") customerId: Int, @Query("warehouseId") warehouseId: Int
    ): Observable<JsonObject?>?

    @Streaming
    @GET
    fun downloadFileWithUrl1(@Url fileUrl: String?): Observable<ResponseBody?>?


    @GET("/api/itemMaster/MurliImage")
    fun getMurliImage(
        @Query("customerId") customerId: Int, @Query("WarehouseId") warehouseId: Int
    ): Observable<JsonObject?>?

    @POST("/api/HisabKitab/AddContact")
    fun getAddContact(@Body addCustomerModel: AddCustomerModel?): Observable<JsonObject?>?

    @GET("/api/HisabKitab/IsCustomerExists")
    fun IsCustomerExists(
        @Query("mobile") MobileNumber: String?, @Query("fCMId") fCMIdId: String?
    ): Observable<JsonObject?>?

    @GET("/api/HisabKitab/GetCustomerContact")
    fun CustomerContact(@Query("Id") customerId: String?): Observable<JsonArray?>?

    @GET("/api/HisabKitab/GetMyHisabKitab")
    fun myHisabKitab(
        @Query("crCustomerId") crCustomerId: String?,
        @Query("drCustomerId") drCustomerId: String?,
        @Query("Type") Type: String?
    ): Observable<JsonObject?>?

    @GET("api/ShoppingCart/ApplyNewOffer")
    fun getApplyDiscountResponse1(
        @Query("customerId") customerId: Int,
        @Query("warehouseId") warehouseId: Int,
        @Query("offerId") offerId: Int,
        @Query("IsApply") IsApply: Boolean,
        @Query("lang") lang: String?
    ): Observable<CheckoutCartResponse?>?

    @GET("api/KKReturnReplace/GetLastSevenDaysOrderList")
    fun getLast7DayOrders(@Query("CustomerId") customerId: Int): Observable<ArrayList<Int?>?>?

    @GET("api/KKReturnReplace")
    fun getOrderById(
        @Query("CustomerId") customerId: Int, @Query("OrderId") orderId: Int
    ): Observable<ArrayList<ReturnOrderItemModel?>?>?

    @POST("api/KKReturnReplace/PostData")
    fun postReturnOrder(@Body model: PostReturnOrderModel?): Observable<Boolean?>?

    @GET("api/KKReturnReplace/GetReturnReplaceOrderList")
    fun getReturnReplaceOrders(@Query("CustomerId") customerId: Int): Observable<ArrayList<ReturnOrderListModel?>?>?

    @GET("api/KKReturnReplaceHistory/GetReturnReplaceHistoryList")
    fun getReturnReplaceStatusList(@Query("KKRequestId") KKRequestId: Int): Observable<ArrayList<ReturnOrderStatusModel?>?>?

    @GET("api/KKReturnReplaceHistory/GetReturnReplaceItemList")
    fun getReturnReplaceItemList(@Query("KKRequestId") KKRequestId: Int): Observable<ArrayList<ReturnOrderItemModel?>?>?

    @GET("api/KKReturnReplace/ChangeStatus")
    fun updateReturnRequestStatus(
        @Query("KKReturnReplaceId") KKRequestId: Int,
        @Query("Status") status: String?,
        @Query("dboyId") dBoyId: Int
    ): Observable<Boolean?>?

    @POST("/api/HisabKitab/AddMyHisabKitab")
    fun addMyHisabKitab(@Body hisabDetailModel: HisabDetailModel?): Observable<HisabDetailModel?>?

    @GET("/api/HisabKitab/GetCustomerContactDetail?")
    fun getCustomerDetails(
        @Query("logincustId") logincustId: String?, @Query("custId") custId: String?
    ): Observable<CustomerRes?>?

    @POST("/api/HisabKitab/UpdateCustomerContact?")
    fun getMatchedContact(@Body updateContactModel: UpdateContactModel?): Observable<JsonArray?>?

    @GET("/api/HisabKitab/HisabKitabInvoice?")
    fun getHisabInvoice(
        @Query("CustomerId") CustomerId: String?,
        @Query("ContactId") ContactId: String?,
        @Query("Type") Type: String?
    ): Observable<String?>?

    //GetMyBalance
    @GET("/api/HisabKitab/MyBalance")
    fun myBalance(): Observable<CustomerBalance?>?

    //Ticket API
    @GET("/api/Ticket/GetCustomerTicket")
    fun getIssueTickets(
        @Query("customerId") customerId: Int, @Query("skip") skip: Int, @Query("take") take: Int
    ): Observable<ArrayList<MyIssuesResponseModel?>?>?

    @GET("/api/Ticket/GetTicketDetail")
    fun getIssueDetail(@Query("ticketId") ticketId: Int): Observable<MyIssueDetailModel?>?

    @POST("/api/Ticket/GetCategoryWithResponse")
    fun getIssueTopicsApi(@Body postIssuesCategoryModel: PostIssuesCategoryModel?): Observable<IssuesCategoryModel?>?

    @GET("/api/RetailerApp/GetPrepaidOrder")
    fun getPrepaidOrderAPI1(@Query("WarehouseId") wId: Int): Observable<PrepaidOrder?>?

    @GET("api/RetailerApp/RetailerHomePageGetSubSubCategories")
    fun getSubCategoryData1(
        @Query("subCategoryId") subCategoryId: Int,
        @Query("customerId") customerId: Int,
        @Query("wid") wid: Int,
        @Query("lang") lang: String?
    ): Observable<JsonObject?>?

    @GET("/api/RetailerApp/RetailerGetItembySubCatAndBrand")
    fun getItemByBrandList(
        @Query("customerId") CustomerId: Int,
        @Query("sscatid") sscatid: Int,
        @Query("scatid") scatid: Int,
        @Query("lang") lang: String?
    ): Observable<ItemListResponse?>?


    // game API's
    @POST("/api/wallet/AddWalletPointToCustomer")
    fun addWalletPoint(@Body addGamePointModel: AddGamePointModel?): Observable<String?>?

    @GET("/api/wallet/GetRetailAppGame")
    fun getRetailAppGame(
        @Query("customerId") customerId: Int, @Query("warehouseId") warehouseId: Int
    ): Observable<ArrayList<GameModel?>?>?

    @GET("/api/wallet/GetRetailAppGameBanner")
    fun getRetailAppGameBanner(
        @Query("customerId") customerId: Int, @Query("warehouseId") warehouseId: Int
    ): Observable<ArrayList<GamesBannerModel?>?>?


    @GET("api/RetailerApp/GetAllMemberShip")
    fun getAllMemberShip(
        @Query("customerId") cusId: Int, @Query("lang") lang: String?
    ): Observable<ArrayList<MembershipPlanModel?>?>?

    @POST("/api/RetailerApp/PrimePaymentRequest")
    fun primePaymentRequest(@Body model: PrimePaymentModel?): Observable<PrimePaymentResponse?>?

    @POST("/api/RetailerApp/PrimePaymentResponse")
    fun primePaymentResponse(@Body model: PrimePaymentModel?): Observable<Boolean?>?

    @GET("/api/RetailerApp/CustomerMemberShipDetail")
    fun membershipDetail(
        @Query("customerId") custId: Int,
        @Query("warehouseId") wId: Int,
        @Query("lang") lang: String?
    ): Observable<MembershipModel?>?

    @GET("/api/TargetModule/GetLevelData")
    fun getYourLevelData(
        @Query("customerId") cusID: Int, @Query("SkCode") skCode: String?
    ): Observable<ArrayList<YourLevelTargetModel?>?>?


    @GET("api/RetailerApp/ExpiringGamePoint")
    fun getExpiringWalletPoints(@Query("CustomerId") CustomerId: Int): Observable<ArrayList<MyExpiringWalletModel?>?>?

    @GET("api/RetailerApp/RetailerItemDetail")
    fun getProductDetails(
        @Query("itemId") itemId: Int,
        @Query("customerId") custId: Int,
        @Query("wareHouseId") wId: Int,
        @Query("lang") lang: String?
    ): Observable<ItemListModel?>?

    @POST("/api/cl/pg/account")
    fun checkBookCustomerLimit(
        @Header("API-key") content_type: String?, @Body bookCreditLimitRes: CheckBookCreditLimitRes?
    ): Observable<JsonObject?>?

    @POST
    fun checkBookCustomerLimitDy(
        @Url Url: String?,
        @Header("API-key") content_type: String?,
        @Body bookCreditLimitRes: CheckBookCreditLimitRes?
    ): Observable<JsonObject?>?


    @POST("/api/RetailerApp/InsertCustomerContacts")
    fun uploadContacts(@Body contacts: ArrayList<ContactUploadModel?>?): Observable<JsonElement?>?

    @POST("/api/DistributorApp/MobilePaymentRequest")
    fun paymentRequest(@Body model: PaymentRequestModel?): Observable<Long?>?

    @POST("/api/DistributorApp/MobilePaymentResponse")
    fun postPaymentResponse(@Body model: PostUPIPaymentResponse?): Observable<Boolean?>?


    @GET("/api/DistributorApp/MyGullakTransWithPage")
    fun getGullakData(
        @Query("customerId") custId: Int,
        @Query("page") page: Int,
        @Query("totalOrder") totalOrder: Int
    ): Observable<ArrayList<GullakModel?>?>?

    @GET("/api/DistributorApp/GetDistributorGullak")
    fun getGullakBalance1(@Query("customerId") custId: Int): Observable<JsonObject?>?

    @GET("/api/RetailerApp/NotifyMe")
    fun getNotfayItems1(
        @Query("CustomerId") custId: Int,
        @Query("warehouseId") wId: Int,
        @Query("itemNumber") itemNumber: String?
    ): Observable<Boolean?>?


    @GET("/api/Notification/NotificationReceived")
    fun notificationReceived(
        @Query("NotificationId") notificationId: Int, @Query("customerid") custId: Int
    ): Observable<JsonElement?>?

    // Store API's


    @GET
    fun generateLead1(@Url url: String?): Observable<JsonObject?>?

    // sk credit limit
    @GET("api/Udhar/GetUdharCreditLimit")
    fun getCreditLimit1(@Query("CustomerId") custId: Int): Observable<CreditLimit?>?

    @POST("api/Udhar/GeneratePayment")
    fun creditPayment1(@Body model: CreditPayment?): Observable<CreditLimit?>?

    @POST("/api/OrderMastersAPI/UpdateETANew")
    fun updateDeliveryETA1(@Body jsonObject: JsonObject?): Observable<JsonObject>?


    @GET
    fun getAppHomeBottomData1(@Url url: String?): Observable<ArrayList<RatingModel?>?>?


    @GET("api/orderprocess/GetOrderDetail/{OrderId}")
    fun getOrderDetail(@Path("OrderId") OrderId: Int): Observable<List<OrderDetailsModel?>?>?

    @GET("api/orderprocess/GetCustomerTrip/{Id}/{CustomerId}")
    fun getTripDetails(
        @Path("Id") tripId: Int, @Path("CustomerId") customerId: Int
    ): Observable<TrackOrderModel?>?

    @GET("api/orderprocess/GetOrderETADate/{OrderId}")
    fun getOrderETADate(@Path("OrderId") orderId: Int): Observable<EtaDates?>?

    @GET("api/Orders/GetOrderStatusDetailByOrderId")
    fun getOrderStatus(@Query("OrderId") orderId: Int): Observable<ArrayList<ReturnOrderStatusModel?>?>?

    @POST("api/TripCustVoiceRecord/InsertTripCustomerVoiceRecord")
    fun uploadAudioFile(@Body uploadAudioModel: UploadAudioModel?): Observable<Boolean?>?

    @GET("api/RetailerApp/GetCustomerSalesPersons/{customerId}")
    suspend fun getCustomerSalesPersons(@Path("customerId") custId: Int): Response<ArrayList<MyAgentModel>>?

    @GET("api/RetailerApp/GetCustReferralConfigurations")
    fun getReferralConfig(@Query("CityId") cityId: Int): Observable<ArrayList<ReferralConfigModel?>?>?

    @GET("api/RetailerApp/GetCustomerReferralOrderList")
    fun getReferredList(@Query("customerId") customerId: Int): Observable<ArrayList<ReferredModel?>?>?

    @GET("/api/Payments/GetTopRTGSTrans")
    fun fetchRTGSDataList(
        @Query("customerId") cId: Int,
        @Query("skip") page: Int,
        @Query("totalRecord") totalOrder: Int
    ): Observable<ArrayList<GullakModel?>?>?

    @GET("/api/Payments/GetCustomerRTGSAmount")
    fun getRTGSBalance1(@Query("customerId") cId: Int): Observable<Double?>?


    @GET("/api/Orders/GetOrderConcernDataByOrderId")
    fun getOrderConcernByOrderId(@Query("OrderId") orderId: Int): Observable<DeliveryConcern?>?

    @POST("/api/Orders/PostOrderConcern")
    fun postOrderConcern(@Body jsonObject: JsonObject): Observable<Boolean>


    @GET("/api/Clearance/GetClearanceLiveItemCategory/{wId}/{CustomerId}/{lang}")
    fun getClearanceItemCategory(
        @Path("wId") wId: Int, @Path("CustomerId") custId: Int, @Path("lang") lang: String?
    ): Observable<ArrayList<CategoriesModel>>

    @POST("/api/Clearance/ClearanceLiveItemToSale")
    fun getClearanceItem(@Body itemDc: SearchClearanceItemDc): Observable<ArrayList<ClearanceItemModel>>

    @POST("/api/Clearance/GenerateClearanceOrder")
    fun placeClearanceOrder(@Body model: ClearanceShoppingCart?): Observable<ClearanceOrderResponse?>?

    @GET("/api/Clearance/GetTCSpercent/{CustomerId}")
    fun getTcsPercent(@Path("CustomerId") CustomerId: Int): Observable<Double?>?

    @POST("/api/Clearance/ApplyOffer")
    fun applyClearanceOffer(
        @Query("IsApply") isApply: Boolean, @Body cart: ClearanceShoppingCart
    ): Observable<ClearanceOfferResponse?>?

    @GET("/api/Clearance/OrderValidateForPayment/{orderId}")
    fun clearanceOrderValidPayment(@Path("orderId") orderId: Int): Observable<Boolean?>?


    @GET("/api/TargetModule/GetTargetItem")
    fun getTargetItems(
        @Query("companyId") companyId: Int,
        @Query("storeId") storeId: Int,
        @Query("customerId") custId: Int,
        @Query("warehouseId") wareHouseId: Int,
        @Query("peopleId") peopleId: Int,
        @Query("skip") skip: Int?,
        @Query("take") take: Int?,
        @Query("lang") lang: String,
        @Query("itemname") itemname: String
    ): Observable<TargetResponseModel?>?

    @GET("/api/TargetModule/GetAlreadyBoughtTargetItem")
    fun getAlreadyBoughtTargetItem(
        @Query("companyId") companyId: Int,
        @Query("storeId") storeId: Int,
        @Query("customerId") custId: Int,
        @Query("warehouseId") wareHouseId: Int,
        @Query("peopleId") peopleId: Int,
        @Query("skip") skip: Int?,
        @Query("take") take: Int?,
        @Query("lang") lang: String,
        @Query("itemname") itemname: String
    ): Observable<TargetResponseModel?>?

    @POST("/api/ClusterHoliday/UpdateCustomerHoliday")
    fun updateCustomerHoliday(@Body model: CustomerHoliday): Observable<CommonResponse?>?


    @GET("api/Gamification/GetCustomerBucketGames")
    fun getCustomerBucketGames(
        @Query("CustomerId") custId: Int,
        @Query("warehouseId") wId: Int,
        @Query("skip") skip: Int,
        @Query("take") take: Int,
        @Query("lang") lang: String
    ): Observable<BucketGameResponse?>?

    @GET("api/Gamification/GetCustomerGameStreak")
    fun getCustomerGameStreak(
        @Query("CustomerId") custId: Int, @Query("lang") lang: String
    ): Observable<BucketGameResponse?>?

    @GET("api/Gamification/GetCustomerAchiveLevel")
    fun getCustomerAchieveLevel(
        @Query("CustomerId") custId: Int,
        @Query("skip") skip: Int,
        @Query("take") take: Int,
        @Query("lang") lang: String
    ): Observable<ArrayList<BucketCustomerModel>?>?

    @GET("api/Gamification/GetCustomerGamesHistory")
    fun getBucketEarningUserList(
        @Query("CustomerId") custId: Int,
    ): Observable<BucketCustomerGamesHistoryModel>?


    @GET("api/KKReturnReplace/SalesReturnList")
    suspend fun getSalesReturnList(@Query("CustomerId") customerId: Int): Response<ArrayList<SalesReturnRequestListModel>>

    @GET("api/KKReturnReplace/SalesReturnDetailList")
    suspend fun getSalesReturnDetailsList(@Query("RequestId") requestId: Int): Response<ArrayList<SalesReturnRequestListDetailsModel>>

    @GET("api/KKReturnReplace/ItemSearch")
    suspend fun getReturnItem(
        @Query("CustomerId") customerId: Int,
        @Query("warehouseid") warehouseid: Int,
        @Query("KeyValue") keyValue: String
    ): Response<ArrayList<ReturnItemModel>>

    @GET("api/KKReturnReplace/SaleReturnOrderBatchItem")
    suspend fun getSaleReturnOrderBatchItem(
        @Query("CustomerId") customerId: Int,
        @Query("ItemMultiMrpId") itemMultiMrpId: Int
    ): Response<ArrayList<ReturnOrderBatchItemModel>>

    @Multipart
    @POST("api/itemimageupload/UploadSalesReturnImage")
    suspend fun uploadKKReturnReplaceImages(@Part body: MultipartBody.Part): Response<JsonObject>

    @POST("api/KKReturnReplace/PostSalesReturnRequest")
    suspend fun postSalesReturnRequest(@Body model: PostKKReturnReplaceRequestDc): Response<SaleReturnRequestResponseModel>

    // offer visibility API's


    @GET("api/ScaleUpIntegration/GetScaleUpLimit")
    fun getScaleUpLimit1(
        @Query("customerId") custId: Int,
    ): Observable<CreditLimit?>?

    @GET("api/ScaleUpIntegration/ScaleUpPaymentInitiate")
    fun scaleUpPaymentInitiate1(
        @Query("customerId") custId: Int,
        @Query("OrderId") orderId: Int,
        @Query("TransactionAmount") amount: Double,
    ): Observable<ScaleUpResponse?>?


    @GET("api/TestCashCollection/GetPayLaterCollectionLimit")
    suspend fun getPaylaterLimits(
        @Query("Customerid") customerId: Int,
    ): Response<PaylaterLimitsResponseModel?>?


    @GET("api/ConsumerApp/RazorPayCreateOrder")
    fun fetchRazorpayOrderId1(
        @Query("orderId") orderId: String,
        @Query("Amount") amount: Double
    ): Observable<String>


}