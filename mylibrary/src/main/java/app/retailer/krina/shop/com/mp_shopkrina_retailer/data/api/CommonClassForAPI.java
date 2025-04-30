package app.retailer.krina.shop.com.mp_shopkrina_retailer.data.api;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import app.retailer.krina.shop.com.mp_shopkrina_retailer.BuildConfig;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.store.StoreItemModel;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.splash.CompanyInfoResponse;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.TargetResponseModel;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.categoryBean.CategoriesModel;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.AddCustomerModel;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.AddGamePointModel;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.AppHomeItemGoldenDealModel;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.AppHomeItemModel;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.BucketCustomerGamesHistoryModel;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.BucketCustomerModel;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.payment.CheckBookCreditLimitRes;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.auth.CityModel;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.ClearanceItemModel;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.payment.CreditLimit;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.CreditPayment;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.CustomerBalance;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.CustomerHoliday;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.CustomerRes;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.DeliveryConcern;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.DocTypeModel;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.FaqModel;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.GameModel;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.GullakModel;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.HisabDetailModel;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.appHome.HomeOfferFlashDealModel;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.ItemListModel;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.MembershipModel;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.MembershipPlanModel;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.MyDreamModel;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.MyExpiringWalletModel;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.MyFavModel;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.MyUdharPojo;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.NewTargetModel;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.OrderDetailsModel;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.OrderSummaryModel;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.ReferralConfigModel;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.ReferralModel;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.ReferredModel;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.ReturnOrderItemModel;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.ReturnOrderListModel;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.ReturnOrderStatusModel;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.SupplierPaymentModel;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.auth.TokenResponse;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.TrackOrderModel;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.UpdateContactModel;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.UploadAudioModel;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.YourLevelTargetModel;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.CartAddItemModel;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.postModels.ChangePasswordModel;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.postModels.ClearanceOfferResponse;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.postModels.ClearanceShoppingCart;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.postModels.ContactUploadModel;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.postModels.DeliveryFeedbackModel;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.postModels.DreamModel;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.postModels.EPayPartnerModel;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.auth.EditProfileModel;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.postModels.EpayLaterDetail;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.postModels.FeedbackModel;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.postModels.GstUpdateCustomerModel;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.postModels.ItemIdPostModel;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.postModels.LatLongModel;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.postModels.LoginModel;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.auth.NewSignupRequest;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.postModels.PaymentReq;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.postModels.PaymentRequestModel;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.postModels.PostIssuesCategoryModel;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.postModels.PostReturnOrderModel;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.postModels.PostUPIPaymentResponse;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.postModels.PrimePaymentModel;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.RatingModel;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.postModels.RequestBrandModel;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.postModels.SearchClearanceItemDc;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.postModels.SignupModel;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.offer.BillDiscountListResponse;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.responseModel.BucketGameResponse;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.offer.CheckBillDiscountResponse;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.shoppingCart.CheckoutCartResponse;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.responseModel.ClearanceOrderResponse;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.responseModel.CommonResponse;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.responseModel.CustomerTargetResponse;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.responseModel.DialEarningResponse;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.payment.EpayLaterResponse;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.responseModel.EtaDates;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.responseModel.FeedBackResponse;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.responseModel.GamesBannerModel;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.responseModel.GetOrderAtFeedbackModel;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.auth.GstInfoResponse;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.responseModel.ImageResponse;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.responseModel.IssuesCategoryModel;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.ItemListResponse;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.responseModel.LoginResponse;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.responseModel.MyIssueDetailModel;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.responseModel.MyIssuesResponseModel;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.auth.MyProfileResponse;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.responseModel.MyUdharResponse;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.wallet.MyWalletResponse;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.auth.OTPResponse;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.payment.OrderMaster;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.payment.PrepaidOrder;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.responseModel.PrimePaymentResponse;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.payment.ScaleUpResponse;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.auth.SignupResponse;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.responseModel.SupplierPaymentResponce;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.responseModel.TradeOfferResponse;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.wallet.WalletResponse;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.MyApplication;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;

public class CommonClassForAPI {
    private static CommonClassForAPI commonClassForAPI;


    public static CommonClassForAPI getInstance(Activity activity) {
        MyApplication.getInstance().activity = activity;
        if (commonClassForAPI == null) {
            commonClassForAPI = new CommonClassForAPI(activity);
        }
        return commonClassForAPI;
    }

    private CommonClassForAPI() {
    }

    private CommonClassForAPI(Activity activity) {
        MyApplication.getInstance().activity = activity;
    }


    // upload profile image
    public void uploadImage(DisposableObserver<ImageResponse> observer, MultipartBody.Part body) {
        RestClient.getInstance().getService().imageUpload1(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ImageResponse>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull ImageResponse s) {
                        observer.onNext(s);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        observer.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        observer.onComplete();
                    }
                });
    }

    // upload pan image
    public void uploadPanImage(DisposableObserver<ImageResponse> observer, MultipartBody.Part body) {
        RestClient.getInstance().getService().uploadPanImage(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ImageResponse>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull ImageResponse s) {
                        observer.onNext(s);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        observer.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        observer.onComplete();
                    }
                });
    }

    // upload pan image
    public void uploadCustomerImage(DisposableObserver<ImageResponse> observer, MultipartBody.Part body) {
        RestClient.getInstance().getService().uploadCustomerImage(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ImageResponse>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull ImageResponse s) {
                        observer.onNext(s);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        observer.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        observer.onComplete();
                    }
                });
    }

    // upload pan image
    public void uploadCustomerBackImage(DisposableObserver<ImageResponse> observer, MultipartBody.Part body) {
        RestClient.getInstance().getService().uploadCustomerBackImage(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ImageResponse>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull ImageResponse s) {
                        observer.onNext(s);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        observer.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        observer.onComplete();
                    }
                });
    }

    // upload pan image
    public void uploadHisabKitabImage(DisposableObserver<String> observer, MultipartBody.Part body) {
        RestClient.getInstance2().getService2().uploadHisabKitabImage(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull String s) {
                        observer.onNext(s);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        observer.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        observer.onComplete();
                    }
                });
    }


    public void fetchLoginData(DisposableObserver<LoginResponse> fetchLoginDes, LoginModel loginModel) {
        RestClient.getInstance().getService().doLogin(loginModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<LoginResponse>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull LoginResponse userModel) {
                        fetchLoginDes.onNext(userModel);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        fetchLoginDes.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        fetchLoginDes.onComplete();
                    }
                });
    }

    public void requestGstUpdate(DisposableObserver<JsonObject> fetchLoginDes, GstUpdateCustomerModel signupModel) {
        RestClient.getInstance().getService().requestGstUpdateApi(signupModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<JsonObject>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onNext(@NonNull JsonObject object) {
                        //  customDialog.dismiss();
                        fetchLoginDes.onNext(object);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        // customDialog.dismiss();
                        fetchLoginDes.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        //customDialog.dismiss();
                        fetchLoginDes.onComplete();
                    }
                });
    }

    public void fetchSignupData(DisposableObserver<SignupResponse> fetchLoginDes, SignupModel signupModel) {
        RestClient.getInstance().getService().doSignup(signupModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SignupResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(SignupResponse userModel) {
                        fetchLoginDes.onNext(userModel);
                    }

                    @Override
                    public void onError(Throwable e) {
                        fetchLoginDes.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        //customDialog.dismiss();
                        fetchLoginDes.onComplete();
                    }
                });
    }

    public void fetchCustomerTarget(DisposableObserver<CustomerTargetResponse> custTargetDes, int warehouseid, String skCode, int customerid) {
        RestClient.getInstance().getService().getCustomerTarget(warehouseid, skCode, customerid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CustomerTargetResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(CustomerTargetResponse o) {
                        custTargetDes.onNext(o);
                    }

                    @Override
                    public void onError(Throwable e) {
                        custTargetDes.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        custTargetDes.onComplete();
                    }
                });
    }

    public void fetchCustomerSubCategoryTarget(DisposableObserver<List<NewTargetModel>> custTargetDes, int warehouseid, int customerid) {
        RestClient.getInstance().getService().getCustomerSubCategoryTarget(warehouseid, customerid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<NewTargetModel>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(List<NewTargetModel> o) {
                        custTargetDes.onNext(o);
                    }

                    @Override
                    public void onError(Throwable e) {
                        custTargetDes.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        custTargetDes.onComplete();
                    }
                });
    }

    public void fetchProductDetails(DisposableObserver<ItemListModel> productDetailsDes, ItemIdPostModel model) {
        RestClient.getInstance("Product Share").getService().getProductDetails(model)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ItemListModel>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(ItemListModel o) {
                        productDetailsDes.onNext(o);
                    }

                    @Override
                    public void onError(Throwable e) {
                        productDetailsDes.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        productDetailsDes.onComplete();
                    }
                });
    }

    public void getToken(DisposableObserver<TokenResponse> fetchTokenDes, String password, String username, String Password) {
        RestClient.getInstance().getService().getToken1(password, username, Password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<TokenResponse>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull TokenResponse object) {
                        fetchTokenDes.onNext(object);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        fetchTokenDes.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        fetchTokenDes.onComplete();
                    }
                });
    }

    public void fetchCity(final DisposableObserver<ArrayList<CityModel>> fetchCityDes) {
        RestClient.getInstance().getService().getCity()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ArrayList<CityModel>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(ArrayList<CityModel> userModel) {
                        fetchCityDes.onNext(userModel);
                    }

                    @Override
                    public void onError(Throwable e) {
                        fetchCityDes.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        fetchCityDes.onComplete();
                    }
                });
    }

    public void getCustomerDocType(DisposableObserver<ArrayList<DocTypeModel>> fetchCityDes, int wId, int custId) {
        RestClient.getInstance().getService().getCustomerDocType1(wId, custId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ArrayList<DocTypeModel>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(ArrayList<DocTypeModel> list) {
                        fetchCityDes.onNext(list);
                    }

                    @Override
                    public void onError(Throwable e) {
                        fetchCityDes.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        fetchCityDes.onComplete();
                    }
                });
    }

    public void fetchCustomerFeedbackQuestions(final DisposableObserver<JsonObject> observer, int warehouseId) {
        RestClient.getInstance().getService().getCustomerFeedbackQuestions(warehouseId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<JsonObject>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(JsonObject object) {
                        observer.onNext(object);
                    }

                    @Override
                    public void onError(Throwable e) {
                        observer.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        observer.onComplete();
                    }
                });
    }

    public void postCustomerFeedbackData(final DisposableObserver<DeliveryFeedbackModel> observer, DeliveryFeedbackModel model) {
        RestClient.getInstance().getService().postCustomerFeedback(model)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DeliveryFeedbackModel>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(DeliveryFeedbackModel model) {
                        observer.onNext(model);
                    }

                    @Override
                    public void onError(Throwable e) {
                        observer.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        observer.onComplete();
                    }
                });
    }

    // OTP get API
    public void fetchOTP(final DisposableObserver<OTPResponse> fetchOTPDes, String MobileNumber, String deviceId, String keyHas) {
        RestClient.getInstance("Generate OTP").getService().getOtp1(MobileNumber, deviceId, keyHas, BuildConfig.BUILD_TYPE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<OTPResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(OTPResponse otp) {
                        fetchOTPDes.onNext(otp);
                    }

                    @Override
                    public void onError(Throwable e) {
                        fetchOTPDes.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        fetchOTPDes.onComplete();
                    }
                });
    }


    //for sub category item
    public void fetchSubcategory(final DisposableObserver<JsonObject> observer, String itemid, int customerId, int warehouseid, String lang) {
        RestClient.getInstance("Sub Category Fragment").getService().getSubcategory(itemid, customerId, warehouseid, lang)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<JsonObject>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(@NonNull JsonObject model) {
                        observer.onNext(model);
                    }


                    @Override
                    public void onError(@NonNull Throwable e) {
                        observer.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        observer.onComplete();
                    }
                });
    }

    //Add feedback
    public void addfeedback(DisposableObserver<FeedBackResponse> fetchChatDes, FeedbackModel feedbackModel) {
        RestClient.getInstance().getService().postfeedback(feedbackModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<FeedBackResponse>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull FeedBackResponse object) {
                        fetchChatDes.onNext(object);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        fetchChatDes.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        fetchChatDes.onComplete();
                    }
                });
    }

    //Change Password
    public void changePassword(DisposableObserver<JsonObject> fetchChatDes, ChangePasswordModel model) {
        RestClient.getInstance("Generate new password").getService().postChangePassword(model)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<JsonObject>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull JsonObject o) {
                        fetchChatDes.onNext(o);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        fetchChatDes.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        fetchChatDes.onComplete();
                    }
                });
    }

    public void forgetPassword(final DisposableObserver<JsonObject> forgetPasswordres, String Mobile) {
        RestClient.getInstance().getService().postforgetPassword(Mobile)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<JsonObject>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onNext(@NonNull JsonObject o) {
                        forgetPasswordres.onNext(o);
                    }


                    @Override
                    public void onError(@NonNull Throwable e) {
                        forgetPasswordres.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        forgetPasswordres.onComplete();
                    }
                });
    }

    //edit Profile
    public void editProfile(DisposableObserver<MyProfileResponse> fetchChatDes, EditProfileModel model, String mSectionType) {
        RestClient.getInstance(mSectionType).getService().putEditProfile(model)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MyProfileResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(MyProfileResponse object) {
                        fetchChatDes.onNext(object);
                    }

                    @Override
                    public void onError(Throwable e) {
                        fetchChatDes.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        fetchChatDes.onComplete();
                    }
                });
    }


    //Add request Brand
    public void addRequestBrand(DisposableObserver<JsonElement> fetchChatDes, RequestBrandModel requestBrandModel) {
        RestClient.getInstance().getService().postRequestBrand(requestBrandModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<JsonElement>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(JsonElement object) {
                        fetchChatDes.onNext(object);
                    }

                    @Override
                    public void onError(Throwable e) {
                        fetchChatDes.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        fetchChatDes.onComplete();
                    }
                });
    }




    public void fetchWalletPoint(final DisposableObserver<WalletResponse> walletDes, int CustomerId, String mSectionType) {
        RestClient.getInstance(mSectionType).getService().getWalletPoint1(CustomerId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<WalletResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(WalletResponse o) {
                        walletDes.onNext(o);
                    }

                    @Override
                    public void onError(Throwable e) {
                        // customDialog.dismiss();
                        walletDes.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        //customDialog.dismiss();
                        walletDes.onComplete();
                    }
                });
    }

    public void fetchWalletPointNew(DisposableObserver<MyWalletResponse> walletDes, int CustomerId, int pageNo, String mSectionType) {
        RestClient.getInstance(mSectionType).getService().getWalletPointNew1(CustomerId, pageNo)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MyWalletResponse>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull MyWalletResponse o) {
                        walletDes.onNext(o);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        walletDes.onError(e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    public void getVideoList(final DisposableObserver<ArrayList<FaqModel>> video) {
        RestClient.getInstance().getService().getVideo()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ArrayList<FaqModel>>() {

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ArrayList<FaqModel> o) {
                        video.onNext(o);
                    }

                    @Override
                    public void onError(Throwable e) {
                        video.onError(e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    //ItemListModel API
    public void fetchItemList(final DisposableObserver<ItemListResponse> baseCatitem, int CustomerId, int sscatid,
                              int scateId, int categoryId, String lang, String mSectionType, int skip, int take, String sortType, String direction) {
//        RestClient.getInstance(mSectionType).getService().getItemList(CustomerId, sscatid, scateId, categoryId, lang, skip, take, sortType, direction)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<ItemListResponse>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//                    }
//
//                    @Override
//                    public void onNext(ItemListResponse o) {
//                        baseCatitem.onNext(o);
//                    }
//
//
//                    @Override
//                    public void onError(Throwable e) {
//                        baseCatitem.onError(e);
//                    }
//
//                    @Override
//                    public void onComplete() {
//                        baseCatitem.onComplete();
//                    }
//                });
    }

    //Favorite Item API
    public void fetchFaveItemList(final DisposableObserver<AppHomeItemModel> faveItem, MyFavModel myFavModel) {
        RestClient.getInstance("favorite Items").getService().getFaveItemList(myFavModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<AppHomeItemModel>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onNext(@NonNull AppHomeItemModel o) {
                        faveItem.onNext(o);
                    }


                    @Override
                    public void onError(@NonNull Throwable e) {
                        faveItem.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        faveItem.onComplete();
                    }
                });
    }

    //Update order placed API
    public void getUpdateOrderPlaced(final DisposableObserver<Boolean> itemObs, PaymentReq paymentReq, String mSectionType) {
        RestClient.getInstance(mSectionType).getService().getUpdateOrderPlaced1(paymentReq)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Boolean>() {

                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onNext(@NonNull Boolean o) {
                        itemObs.onNext(o);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        itemObs.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        itemObs.onComplete();
                    }
                });
    }

    //Update order placed API
    public void insertOnlineTransaction(final DisposableObserver<Boolean> itemObs, PaymentReq paymentReq, String mSectionType) {
        RestClient.getInstance(mSectionType).getService().getInsertOnlineTransaction1(paymentReq)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Boolean>() {

                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onNext(@NonNull Boolean o) {
                        itemObs.onNext(o);
                    }


                    @Override
                    public void onError(@NonNull Throwable e) {
                        itemObs.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        itemObs.onComplete();
                    }
                });
    }


    //PostOrderDialValue
    public void postOrderDialValue(final DisposableObserver<DialEarningResponse> itemObs, OrderMaster dialEarningModel, String mSectionType) {
        RestClient.getInstance(mSectionType).getService().postDialEarningPoint(dialEarningModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DialEarningResponse>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onNext(@NonNull DialEarningResponse o) {
                        itemObs.onNext(o);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        itemObs.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        itemObs.onComplete();
                    }
                });
    }
    public void FetchShopByBrand(final DisposableObserver<ItemListResponse> brandDes, int customerId, int sscatid, String lang, String mSectionType) {
        RestClient.getInstance(mSectionType).getService().getBrandInfo(customerId, sscatid, lang)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ItemListResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(ItemListResponse o) {
                        brandDes.onNext(o);
                    }

                    @Override
                    public void onError(Throwable e) {
                        brandDes.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        brandDes.onComplete();
                    }
                });
    }

    public void fetchBannerItems(final DisposableObserver<ItemListResponse> brandDes, int customerId, int sscatid, String mSectionType) {
        RestClient.getInstance(mSectionType).getService().getBannerItems(customerId, sscatid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ItemListResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(ItemListResponse o) {
                        brandDes.onNext(o);
                    }

                    @Override
                    public void onError(Throwable e) {
                        brandDes.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        brandDes.onComplete();
                    }
                });
    }

    public void fetchBannerItemDetail(final DisposableObserver<ItemListResponse> brandDes, int customerId, int sscatid, String mSectionType) {
        RestClient.getInstance(mSectionType).getService().getBannerItemDetail(customerId, sscatid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ItemListResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(ItemListResponse o) {
                        brandDes.onNext(o);
                    }

                    @Override
                    public void onError(Throwable e) {
                        // customDialog.dismiss();
                        brandDes.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        //customDialog.dismiss();
                        brandDes.onComplete();
                    }
                });
    }

    public void fetchBannerOffers(final DisposableObserver<ItemListResponse> brandDes, int customerId, int offerId, String mSectionType) {
        RestClient.getInstance(mSectionType).getService().getBannerOffers(customerId, offerId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ItemListResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(ItemListResponse o) {
                        brandDes.onNext(o);
                    }

                    @Override
                    public void onError(Throwable e) {
                        // customDialog.dismiss();
                        brandDes.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        brandDes.onComplete();
                    }
                });
    }

    public void FetchSpecialCategory(final DisposableObserver<ItemListResponse> brandDes, int customerId, int sscatid, String lang, String mSectionType) {
        RestClient.getInstance(mSectionType).getService().getSpecialCategory(customerId, sscatid, lang)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ItemListResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(ItemListResponse o) {

                        brandDes.onNext(o);
                    }

                    @Override
                    public void onError(Throwable e) {
                        // customDialog.dismiss();
                        brandDes.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        //customDialog.dismiss();

                        brandDes.onComplete();
                    }
                });
    }


    //My profile
    public void fetchProfileData(final DisposableObserver<MyProfileResponse> SearchItemDes, int custId, String deviceId) {
        RestClient.getInstance().getService().getMyProfile1(custId, deviceId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MyProfileResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(MyProfileResponse myProfileResponse) {
                        SearchItemDes.onNext(myProfileResponse);
                    }

                    @Override
                    public void onError(Throwable e) {
                        // customDialog.dismiss();
                        SearchItemDes.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        //customDialog.dismiss();
                        SearchItemDes.onComplete();
                    }
                });
    }

    //MyDream
    public void fetchMyDream(final DisposableObserver<ArrayList<MyDreamModel>> SearchItemDes) {
        RestClient.getInstance().getService().getMyDream()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ArrayList<MyDreamModel>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(ArrayList<MyDreamModel> mydreamResponses) {
                        SearchItemDes.onNext(mydreamResponses);
                    }

                    @Override
                    public void onError(Throwable e) {
                        // customDialog.dismiss();
                        SearchItemDes.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        //customDialog.dismiss();
                        SearchItemDes.onComplete();
                    }
                });
    }

    //buy product
    public void buyProduct(DisposableObserver<JsonElement> fetchProductDes, DreamModel model) {
        RestClient.getInstance().getService().getBuyProduct(model)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<JsonElement>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(JsonElement object) {
                        fetchProductDes.onNext(object);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("TAG", "Error:" + e.toString());
                        fetchProductDes.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        fetchProductDes.onComplete();
                    }
                });
    }


    public void Fetchordersummary(final DisposableObserver<List<OrderSummaryModel>> brandItem, int day, String skcode) {
        RestClient.getInstance().getService().getordersummary(day, skcode)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<OrderSummaryModel>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onNext(@NonNull List<OrderSummaryModel> o) {
                        brandItem.onNext(o);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        brandItem.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        //customDialog.dismiss();
                        brandItem.onComplete();
                    }
                });
    }

    public void postUdharData(final DisposableObserver<MyUdharResponse> itemObs, MyUdharPojo myUdharPojo) {
        RestClient.getInstance("Document Upload").getService().setMyUdharData(myUdharPojo)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MyUdharResponse>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onNext(@NonNull MyUdharResponse o) {
                        itemObs.onNext(o);
                    }


                    @Override
                    public void onError(@NonNull Throwable e) {
                        itemObs.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        itemObs.onComplete();
                    }
                });
    }

    public void getTreadOffersFromApi(DisposableObserver<TradeOfferResponse> observer, int cId, int wId, String lang) {
        RestClient.getInstance("Trade Offer Fragment").getService().getTreadIteamIteam(cId, wId, lang)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<TradeOfferResponse>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onNext(@NonNull TradeOfferResponse o) {
                        observer.onNext(o);
                    }


                    @Override
                    public void onError(@NonNull Throwable e) {
                        observer.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        observer.onComplete();
                    }
                });
    }

    public void getOffersFlashDeal(DisposableObserver<HomeOfferFlashDealModel> objDesObserver, int wId, String SectionID, int custId, String lang, String mSectionType) {
        RestClient.getInstance(mSectionType).getService().getOfferItemByFlashDeal1(wId, SectionID, custId, lang)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HomeOfferFlashDealModel>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onNext(@NonNull HomeOfferFlashDealModel o) {
                        objDesObserver.onNext(o);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        objDesObserver.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        objDesObserver.onComplete();
                    }
                });
    }
    public void fetchDynamicHtml(final DisposableObserver<String> observer, String Url) {
        RestClient.getInstance().getService().getDynamicHtml(Url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(String o) {
                        observer.onNext(o);
                    }

                    @Override
                    public void onError(Throwable e) {
                        observer.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        observer.onComplete();
                    }
                });
    }
    public void getOtherItemsHome(DisposableObserver<AppHomeItemGoldenDealModel> observer, String url) {
        RestClient.getInstance("Other").getService().getOtherItemsHome(url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<AppHomeItemGoldenDealModel>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(AppHomeItemGoldenDealModel o) {
                        observer.onNext(o);
                    }

                    @Override
                    public void onError(Throwable e) {
                        observer.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        observer.onComplete();
                    }
                });
    }

    public void getItemBySection(DisposableObserver<JsonObject> objDesObserver,int custId, int wId, String sectionID, String lang) {
        RestClient.getInstance("Item").getService().getItemBySection(custId,wId, sectionID, lang)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<JsonObject>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(JsonObject o) {
                        objDesObserver.onNext(o);
                    }

                    @Override
                    public void onError(Throwable e) {
                        objDesObserver.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        objDesObserver.onComplete();
                    }
                });
    }
    public void getAllStore(final DisposableObserver<ArrayList<StoreItemModel>> baseCatitem, int custId, int wId, String lang) {
        RestClient.getInstance("App home").getService().getAllStore(custId, wId, lang)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ArrayList<StoreItemModel>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onNext(@NonNull ArrayList<StoreItemModel> o) {
                        baseCatitem.onNext(o);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        baseCatitem.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        baseCatitem.onComplete();
                    }
                });
    }

        // bill discount offer list API
    public void getBillDiscountOfferList(final DisposableObserver<BillDiscountListResponse> objDesObserver, int CustomerId, String mSectionType) {
        RestClient.getInstance(mSectionType).getService().getAllBillDiscountOffer11(CustomerId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BillDiscountListResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(BillDiscountListResponse o) {
                        objDesObserver.onNext(o);
                    }

                    @Override
                    public void onError(Throwable e) {
                        objDesObserver.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        objDesObserver.onComplete();
                    }
                });
    }

    //Subcategory Offer
    public void getSubCatOfferList(final DisposableObserver<BillDiscountListResponse> objDesObserver, int CustomerId, int SubCategoryId) {
        RestClient.getInstance().getService().getSubCateOffer1(CustomerId, SubCategoryId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BillDiscountListResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(BillDiscountListResponse o) {
                        objDesObserver.onNext(o);
                    }

                    @Override
                    public void onError(Throwable e) {
                        objDesObserver.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        objDesObserver.onComplete();
                    }
                });
    }

    // Update Scratch card open status
    public void updateScratchCardStatus(final DisposableObserver<CheckBillDiscountResponse> objDesObserver, int CustomerId, int OfferId, boolean IsScartched) {
        RestClient.getInstance().getService().updateScratchCardStatus1(CustomerId, OfferId, IsScartched)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CheckBillDiscountResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(CheckBillDiscountResponse o) {
                        objDesObserver.onNext(o);
                    }

                    @Override
                    public void onError(Throwable e) {
                        objDesObserver.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        objDesObserver.onComplete();
                    }
                });
    }

    //Add feedback
    public void addRating(DisposableObserver<MyProfileResponse> fetchChatDes, JsonObject feedbackModel) {
        RestClient.getInstance().getService().postRating(feedbackModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MyProfileResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(MyProfileResponse object) {
                        fetchChatDes.onNext(object);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("TAG", "Error:" + e.toString());
                        fetchChatDes.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        fetchChatDes.onComplete();
                    }
                });
    }

    // get customer ePayLater limit
    public void ePayLaterCustomerLimit(final DisposableObserver<JsonObject> observer, String token, String skCode) {
        if (BuildConfig.DEBUG) {
            RestClient.getInstance1().getService1().ePayLaterCustomerTestLimit1("application/json", token, skCode)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<JsonObject>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                        }

                        @Override
                        public void onNext(JsonObject o) {
                            observer.onNext(o);
                        }

                        @Override
                        public void onError(Throwable e) {
                            observer.onError(e);
                        }

                        @Override
                        public void onComplete() {
                            observer.onComplete();
                        }
                    });
        } else {
            RestClient.getInstance1().getService1().ePayLaterCustomerLimit1("application/json", token, skCode)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<JsonObject>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                        }

                        @Override
                        public void onNext(JsonObject o) {
                            observer.onNext(o);
                        }

                        @Override
                        public void onError(Throwable e) {
                            observer.onError(e);
                        }

                        @Override
                        public void onComplete() {
                            observer.onComplete();
                        }
                    });
        }
    }

    // get customer CheckBook limit
    public void checkBookLimit(final DisposableObserver<JsonObject> observer, String apiKey, CheckBookCreditLimitRes creditLimitRes) {
        if (BuildConfig.DEBUG) {
            RestClient.getInstance3().getService3().checkBookCustomerLimit(apiKey, creditLimitRes)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<JsonObject>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                        }

                        @Override
                        public void onNext(JsonObject o) {
                            observer.onNext(o);
                        }

                        @Override
                        public void onError(Throwable e) {
                            observer.onError(e);
                        }

                        @Override
                        public void onComplete() {
                            observer.onComplete();
                        }
                    });
        } else {
            RestClient.getInstance3().getService3().checkBookCustomerLimit(apiKey, creditLimitRes)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<JsonObject>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                        }

                        @Override
                        public void onNext(JsonObject o) {
                            observer.onNext(o);
                        }

                        @Override
                        public void onError(Throwable e) {
                            observer.onError(e);
                        }

                        @Override
                        public void onComplete() {
                            observer.onComplete();
                        }
                    });
        }
    }


    // update to ePayLater order is confirmed
    public void ePayLaterOrderConfirmed(final DisposableObserver<EpayLaterResponse> itemObs, String ePayLaterId, String token, String orderID) {
        RestClient.getInstance1().getService1().ePayLaterConfirmOrder1("application/json", token, ePayLaterId, orderID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<EpayLaterResponse>() {

                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(EpayLaterResponse o) {
                        itemObs.onNext(o);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        itemObs.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        itemObs.onComplete();
                    }
                });
    }


    public void GetOTP(final DisposableObserver<JsonObject> Otpdes, String mobileNumber) {
        RestClient.getInstance().getService().getOTP(mobileNumber)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<JsonObject>() {
                    public void onSubscribe(Disposable d) {
                    }

                    public void onNext(JsonObject o) {
                        Otpdes.onNext(o);
                    }

                    @Override
                    public void onError(Throwable e) {
                        // customDialog.dismiss();
                        Otpdes.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        Otpdes.onComplete();
                    }
                });
    }


    public void getCompanyInfo(DisposableObserver<CompanyInfoResponse> brandItem, int cusId, String mSectionType) {
        RestClient.getInstance(mSectionType).getService().getCompanyDetailWithToken1(cusId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CompanyInfoResponse>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onNext(@NonNull CompanyInfoResponse o) {
                        brandItem.onNext(o);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        brandItem.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        brandItem.onComplete();
                    }
                });
    }


    // post ePaylater response data
    public void getEPayaterCustInfo(DisposableObserver<JsonObject> fetchChatDes, int object) {
        RestClient.getInstance().getService().getEPayLaterData(object)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<JsonObject>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(JsonObject object) {
                        fetchChatDes.onNext(object);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("TAG", "Error:" + e.toString());
                        fetchChatDes.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        fetchChatDes.onComplete();
                    }
                });
    }

    // post ePaylater response data
    public void addEPayaterCustInfo(DisposableObserver<JsonObject> fetchChatDes, EpayLaterDetail detail) {
        RestClient.getInstance().getService().postEPayLaterData(detail)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<JsonObject>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(JsonObject object) {
                        fetchChatDes.onNext(object);
                    }

                    @Override
                    public void onError(Throwable e) {
                        fetchChatDes.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        fetchChatDes.onComplete();
                    }
                });
    }

    // post ePaylater response data
    public void addEPayaterPartnerInfo(DisposableObserver<JsonObject> fetchChatDes, EPayPartnerModel model) {
        RestClient.getInstance().getService().postEPayPartnerData(model)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<JsonObject>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(JsonObject object) {
                        fetchChatDes.onNext(object);
                    }

                    @Override
                    public void onError(Throwable e) {
                        fetchChatDes.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        fetchChatDes.onComplete();
                    }
                });
    }

    // generate RSA key for HDFC
    public void fetchRSAKey(DisposableObserver<String> observer, String orderId, String amount, boolean isCredit, String mSectionType) {
        RestClient.getInstance(mSectionType).getService().getRSAKey1(orderId, amount, isCredit)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(String object) {
                        observer.onNext(object);
                    }

                    @Override
                    public void onError(Throwable e) {
                        observer.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        observer.onComplete();
                    }
                });
    }

    //Update order placed API
    public void getNotificationView(DisposableObserver<JsonObject> itemObs, int cust, int notiId) {
        RestClient.getInstance().getService().putNotificationView1(cust, notiId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<JsonObject>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onNext(@NonNull JsonObject o) {
                        if (itemObs != null)
                            itemObs.onNext(o);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        if (itemObs != null)
                            itemObs.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        if (itemObs != null)
                            itemObs.onComplete();
                    }
                });
    }

    public void claimCustomerTarget(final DisposableObserver<CustomerTargetResponse> observer, int warehouseId, String skCode, int customerId) {
        RestClient.getInstance("Claim Button").getService().claimCustomerTarget(warehouseId, skCode, customerId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CustomerTargetResponse>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onNext(@NonNull CustomerTargetResponse o) {
                        observer.onNext(o);
                    }

                    @Override
                    public void onError(Throwable e) {
                        observer.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        observer.onComplete();
                    }
                });
    }

    public void claimCustomerCompanyTarget(final DisposableObserver<CustomerTargetResponse> observer, int custId, int targetDetailId) {
        RestClient.getInstance("Claim Button").getService().claimCustomerCompanyTarget(custId, targetDetailId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CustomerTargetResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(CustomerTargetResponse o) {
                        observer.onNext(o);
                    }

                    @Override
                    public void onError(Throwable e) {
                        observer.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        observer.onComplete();
                    }
                });
    }

    //Update order placed API
    public void CustomerLedgerForRetailerApp(final DisposableObserver<SupplierPaymentResponce> itemObs, SupplierPaymentModel supplierPaymentModel) {
        RestClient.getInstance().getService().getCustomerLedger(supplierPaymentModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SupplierPaymentResponce>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onNext(SupplierPaymentResponce o) {
                        itemObs.onNext(o);
                    }

                    @Override
                    public void onError(Throwable e) {
                        itemObs.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        itemObs.onComplete();
                    }
                });
    }


    public void CustomerPendingPayment(final DisposableObserver<JsonObject> itemObs, int cust_id) {
        RestClient.getInstance().getService().getPendingPayment(cust_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<JsonObject>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(JsonObject o) {
                        itemObs.onNext(o);
                    }

                    @Override
                    public void onError(Throwable e) {
                        // customDialog.dismiss();
                        itemObs.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        //customDialog.dismiss();
                        itemObs.onComplete();
                    }
                });

    }

    public void CustomerLedgerPDF(final DisposableObserver<JsonObject> itemObs, SupplierPaymentModel supplierPaymentModel) {
        RestClient.getInstance().getService().getCustomerLedgerPDF(supplierPaymentModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<JsonObject>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(JsonObject o) {
                        itemObs.onNext(o);
                    }

                    @Override
                    public void onError(Throwable e) {
                        itemObs.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        itemObs.onComplete();
                    }
                });

    }


    public void addItemFav(final DisposableObserver<CommonResponse> observer, int customerId, int itemId, boolean isLike) {
        RestClient.getInstance().getService().addItemFav(customerId, itemId, isLike)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CommonResponse>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onNext(@NonNull CommonResponse o) {
                        observer.onNext(o);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        observer.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        observer.onComplete();
                    }
                });
    }

    public void getFavItem(final DisposableObserver<JsonArray> objDesObserver, int customerId) {
        RestClient.getInstance().getService().getItemFav(customerId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<JsonArray>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onNext(@NonNull JsonArray o) {
                        objDesObserver.onNext(o);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        objDesObserver.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        objDesObserver.onComplete();
                    }
                });
    }

    //edit Profile
    public void updateLatLong(DisposableObserver<MyProfileResponse> fetchChatDes, LatLongModel latModel, String mSectionType) {
        RestClient.getInstance(mSectionType).getService().updateLatLong(latModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MyProfileResponse>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(MyProfileResponse response) {
                        fetchChatDes.onNext(response);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        fetchChatDes.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        fetchChatDes.onComplete();
                    }
                });
    }

    public void getMurliAudioForMobile(DisposableObserver<JsonObject> objDesObserver, int custId, int warehouseId, String mSection) {
        RestClient.getInstance(mSection).getService().getMurliAudioForMobile1(custId, warehouseId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<JsonObject>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(JsonObject o) {
                        objDesObserver.onNext(o);
                    }

                    @Override
                    public void onError(Throwable e) {

                        objDesObserver.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        objDesObserver.onComplete();
                    }
                });
    }

    public void getMurliImage(final DisposableObserver<JsonObject> objDesObserver, int custId, int warehouseId) {
        RestClient.getInstance().getService().getMurliImage(custId, warehouseId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<JsonObject>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(JsonObject o) {
                        objDesObserver.onNext(o);
                    }

                    @Override
                    public void onError(Throwable e) {
                        objDesObserver.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        objDesObserver.onComplete();
                    }
                });
    }

    public void downloadFileWithUrl(DisposableObserver<ResponseBody> objDesObserver, String url) {
        RestClient.getInstance().getService().downloadFileWithUrl1(url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(ResponseBody o) {
                        objDesObserver.onNext(o);
                    }

                    @Override
                    public void onError(Throwable e) {
                        objDesObserver.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        objDesObserver.onComplete();
                    }
                });
    }

    public void signupUpdateBasicInfo(final DisposableObserver<SignupResponse> observer, NewSignupRequest signupModel) {
        RestClient.getInstance("Update customer city").getService().signupUpdateBasicInfo(signupModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SignupResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(@NonNull SignupResponse userModel) {
                        observer.onNext(userModel);
                    }

                    @Override
                    public void onError(Throwable e) {
                        observer.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        observer.onComplete();
                    }
                });
    }

    public void getGstCustInfo(DisposableObserver<GstInfoResponse> fetchLoginDes, String gstNo, String mSectionType) {
        RestClient.getInstance(mSectionType).getService().getGstStatus1(gstNo)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<GstInfoResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(GstInfoResponse userModel) {
                        fetchLoginDes.onNext(userModel);
                    }

                    @Override
                    public void onError(Throwable e) {
                        fetchLoginDes.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        fetchLoginDes.onComplete();
                    }
                });
    }

    public void getOrderDetails(DisposableObserver orderDetaildis, String orderId) {
        RestClient.getInstance().getService().getOrderDetails(orderId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<GetOrderAtFeedbackModel>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(GetOrderAtFeedbackModel object) {
                        orderDetaildis.onNext(object);
                    }

                    @Override
                    public void onError(Throwable e) {
                        orderDetaildis.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        orderDetaildis.onComplete();
                    }
                });
    }

    public void AddContact(final DisposableObserver<JsonObject> itemObs, AddCustomerModel addCustomerModel) {
        RestClient.getInstance2().getService2().getAddContact(addCustomerModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<JsonObject>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(JsonObject o) {
                        itemObs.onNext(o);
                    }


                    @Override
                    public void onError(Throwable e) {
                        itemObs.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        itemObs.onComplete();
                    }
                });
    }

    public void postAddCartItem(final DisposableObserver<JsonObject> objDesObserver, CartAddItemModel model, String mSectionType) {
        RestClient.getInstance(mSectionType).getService().getCartResponse1(model)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<JsonObject>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(JsonObject o) {
                        objDesObserver.onNext(o);
                    }

                    @Override
                    public void onError(Throwable e) {
                        objDesObserver.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        objDesObserver.onComplete();
                    }
                });
    }

    public void customerExists(final DisposableObserver<JsonObject> walletDes, String MobileNumber, String fcmtoken) {
        RestClient.getInstance2().getService2().IsCustomerExists(MobileNumber, fcmtoken)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<JsonObject>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(JsonObject o) {
                        walletDes.onNext(o);
                    }

                    @Override
                    public void onError(Throwable e) {
                        walletDes.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        walletDes.onComplete();
                    }
                });
    }

    public void GetCustomerContact(final DisposableObserver<JsonArray> walletDes, String customerID) {
        RestClient.getInstance2().getService2().CustomerContact(customerID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<JsonArray>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(JsonArray o) {
                        walletDes.onNext(o);
                    }

                    @Override
                    public void onError(Throwable e) {
                        // customDialog.dismiss();
                        walletDes.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        //customDialog.dismiss();
                        walletDes.onComplete();
                    }
                });
    }

    public void getCustomerShoppingCart(final DisposableObserver<CheckoutCartResponse> objDesObserver, int custId, int wId, String lang, String sectionType) {
        RestClient.getInstance(sectionType).getService().getCustomerCart1(custId, wId, lang)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CheckoutCartResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(CheckoutCartResponse o) {
                        objDesObserver.onNext(o);
                    }

                    @Override
                    public void onError(Throwable e) {
                        objDesObserver.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        objDesObserver.onComplete();
                    }
                });
    }

    public void getApplyDiscountResponse(final DisposableObserver<CheckoutCartResponse> observer, int custId, int wId, int OfferId, boolean isApplied, String lang, String mSectionType) {
        RestClient.getInstance(mSectionType).getService().getApplyDiscountResponse1(custId, wId, OfferId, isApplied, lang)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CheckoutCartResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(CheckoutCartResponse o) {
                        observer.onNext(o);
                    }

                    @Override
                    public void onError(Throwable e) {
                        observer.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        observer.onComplete();
                    }
                });
    }

    public void clearAllCartItem(final DisposableObserver<JsonObject> objDesObserver, int custId, int wId) {
        RestClient.getInstance("Clear Cart Button").getService().clearCartItem1(custId, wId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<JsonObject>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(JsonObject o) {
                        objDesObserver.onNext(o);
                    }

                    @Override
                    public void onError(Throwable e) {
                        objDesObserver.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        objDesObserver.onComplete();
                    }
                });
    }

    public void GetGetMyHisabKitab(final DisposableObserver<JsonObject> walletDes, String crCustomerID, String drCustomerID, String Type) {
        RestClient.getInstance2().getService2().myHisabKitab(crCustomerID, drCustomerID, Type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<JsonObject>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(JsonObject o) {

                        walletDes.onNext(o);
                    }

                    @Override
                    public void onError(Throwable e) {
                        // customDialog.dismiss();
                        walletDes.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        //customDialog.dismiss();
                        walletDes.onComplete();
                    }
                });
    }

    public void AddHisabKitab(final DisposableObserver<HisabDetailModel> itemObs, HisabDetailModel hisabDetailModel) {
        RestClient.getInstance2().getService2().addMyHisabKitab(hisabDetailModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HisabDetailModel>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(HisabDetailModel o) {
                        itemObs.onNext(o);
                    }


                    @Override
                    public void onError(Throwable e) {
                        itemObs.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        //customDialog.dismiss();
                        itemObs.onComplete();
                    }
                });
    }

    public void GetCustomerDetailsbyID(final DisposableObserver<CustomerRes> itemObs, String loginCustID, String custId) {
        RestClient.getInstance2().getService2().getCustomerDetails(loginCustID, custId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CustomerRes>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(CustomerRes o) {
                        itemObs.onNext(o);
                    }


                    @Override
                    public void onError(Throwable e) {
                        itemObs.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        //customDialog.dismiss();
                        itemObs.onComplete();
                    }
                });
    }

    public void matchedContactApi(final DisposableObserver<JsonArray> itemObs, UpdateContactModel updateContactModel) {
        RestClient.getInstance2().getService2().getMatchedContact(updateContactModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<JsonArray>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(JsonArray o) {
                        itemObs.onNext(o);
                    }


                    @Override
                    public void onError(Throwable e) {
                        itemObs.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        //customDialog.dismiss();
                        itemObs.onComplete();
                    }
                });
    }

    public void GetHisabKitabInvoice(final DisposableObserver<String> itemObs, String custId, String ContactId, String Type) {
        RestClient.getInstance2().getService2().getHisabInvoice(custId, ContactId, Type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(String o) {
                        itemObs.onNext(o);
                    }


                    @Override
                    public void onError(Throwable e) {
                        itemObs.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        //customDialog.dismiss();
                        itemObs.onComplete();
                    }
                });
    }

    //CheckReferral
    public void checkReferralSkCode(final DisposableObserver<ReferralModel> observer, ReferralModel model) {
        RestClient.getInstance2().getService2().checkReferral(model)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ReferralModel>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onNext(@NonNull ReferralModel object) {
                        observer.onNext(object);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        observer.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        observer.onComplete();
                    }
                });
    }

    /*
     * Order return replace API's
     */
    public void getLast7DayOrders(final DisposableObserver<ArrayList<Integer>> observer, int custId) {
        RestClient.getInstance("KKReturn").getService().getLast7DayOrders(custId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ArrayList<Integer>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(ArrayList<Integer> o) {
                        observer.onNext(o);
                    }

                    @Override
                    public void onError(Throwable e) {
                        observer.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        observer.onComplete();
                    }
                });
    }

    public void getOrderById(final DisposableObserver<ArrayList<ReturnOrderItemModel>> observer, int custId, int orderId) {
        RestClient.getInstance("KKReturn").getService().getOrderById(custId, orderId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ArrayList<ReturnOrderItemModel>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(ArrayList<ReturnOrderItemModel> o) {
                        observer.onNext(o);
                    }

                    @Override
                    public void onError(Throwable e) {
                        observer.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        observer.onComplete();
                    }
                });
    }

    public void postReturnOrder(final DisposableObserver<Boolean> observer, PostReturnOrderModel model) {
        RestClient.getInstance("KKReturn").getService().postReturnOrder(model)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(Boolean o) {
                        observer.onNext(o);
                    }

                    @Override
                    public void onError(Throwable e) {
                        observer.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        observer.onComplete();
                    }
                });
    }

    public void getReturnReplaceOrders(final DisposableObserver<ArrayList<ReturnOrderListModel>> observer, int customerId) {
        RestClient.getInstance("KKReturn").getService().getReturnReplaceOrders(customerId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ArrayList<ReturnOrderListModel>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(ArrayList<ReturnOrderListModel> o) {
                        observer.onNext(o);
                    }

                    @Override
                    public void onError(Throwable e) {
                        observer.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        observer.onComplete();
                    }
                });
    }

    public void getReturnReplaceStatusList(final DisposableObserver observer, int KKRequestId) {
        RestClient.getInstance("KKReturn").getService().getReturnReplaceStatusList(KKRequestId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ArrayList<ReturnOrderStatusModel>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(ArrayList<ReturnOrderStatusModel> o) {
                        observer.onNext(o);
                    }

                    @Override
                    public void onError(Throwable e) {
                        observer.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        observer.onComplete();
                    }
                });
    }

    public void getReturnReplaceItemList(final DisposableObserver observer, int KKRequestId) {
        RestClient.getInstance("KKReturn").getService().getReturnReplaceItemList(KKRequestId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ArrayList<ReturnOrderItemModel>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(ArrayList<ReturnOrderItemModel> o) {
                        observer.onNext(o);
                    }

                    @Override
                    public void onError(Throwable e) {
                        observer.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        observer.onComplete();
                    }
                });
    }

    public void updateReturnRequestStatus(final DisposableObserver<Boolean> observer, int KKRequestId, String status, int dBoyId) {
        RestClient.getInstance("KKReturn").getService().updateReturnRequestStatus(KKRequestId, status, dBoyId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(Boolean o) {
                        observer.onNext(o);
                    }

                    @Override
                    public void onError(Throwable e) {
                        observer.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        observer.onComplete();
                    }
                });
    }

    public void GetMyBalanceHisab(final DisposableObserver<CustomerBalance> observer) {
        RestClient.getInstance2().getService2().myBalance()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CustomerBalance>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(CustomerBalance o) {
                        observer.onNext(o);
                    }

                    @Override
                    public void onError(Throwable e) {
                        // customDialog.dismiss();
                        observer.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        observer.onComplete();
                    }
                });
    }

    public void fetchIssueTickets(final DisposableObserver<ArrayList<MyIssuesResponseModel>> observer, int custId, int skip, int take) {
        RestClient.getInstance2().getService().getIssueTickets(custId, skip, take)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ArrayList<MyIssuesResponseModel>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(ArrayList<MyIssuesResponseModel> o) {
                        observer.onNext(o);
                    }

                    @Override
                    public void onError(Throwable e) {
                        // customDialog.dismiss();
                        observer.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        observer.onComplete();
                    }
                });
    }

    public void fetchIssueDetail(final DisposableObserver<MyIssueDetailModel> observer, int ticketID) {
        RestClient.getInstance2().getService().getIssueDetail(ticketID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MyIssueDetailModel>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(MyIssueDetailModel o) {
                        observer.onNext(o);
                    }

                    @Override
                    public void onError(Throwable e) {
                        observer.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        observer.onComplete();
                    }
                });
    }

    public void getIssueTopics(final DisposableObserver<IssuesCategoryModel> observer, PostIssuesCategoryModel postIssuesCategoryModel) {
        RestClient.getInstance2().getService().getIssueTopicsApi(postIssuesCategoryModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<IssuesCategoryModel>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(IssuesCategoryModel o) {
                        observer.onNext(o);
                    }

                    @Override
                    public void onError(Throwable e) {
                        observer.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        observer.onComplete();
                    }
                });
    }

    // Update Scratch card open status
    public void prepaidOrder(DisposableObserver<PrepaidOrder> objDesObserver, int wId, String mSectionType) {
        RestClient.getInstance(mSectionType).getService().getPrepaidOrderAPI1(wId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<PrepaidOrder>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(PrepaidOrder o) {
                        objDesObserver.onNext(o);
                    }

                    @Override
                    public void onError(Throwable e) {
                        objDesObserver.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        objDesObserver.onComplete();
                    }
                });
    }

    //Sub Category  API
    public void fetchSubCategory(final DisposableObserver<JsonObject> CatItem, int subCategoryId, int custId, int wId, String lang, String mSectionType) {
        RestClient.getInstance(mSectionType).getService().getSubCategoryData1(subCategoryId, custId, wId, lang)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<JsonObject>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(JsonObject o) {
                        CatItem.onNext(o);
                    }


                    @Override
                    public void onError(Throwable e) {
                        CatItem.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        CatItem.onComplete();
                    }
                });
    }

    //ItemListModel API
    public void fetchItemBrandByList(final DisposableObserver<ItemListResponse> observer, int custId, int sscatid, int scateId, String lang, String mSectionType) {
        RestClient.getInstance(mSectionType).getService().getItemByBrandList(custId, sscatid, scateId, lang)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ItemListResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(ItemListResponse o) {
                        observer.onNext(o);
                    }


                    @Override
                    public void onError(Throwable e) {
                        observer.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        observer.onComplete();
                    }
                });
    }

    // Update Scratch card open status

    // game API's
    public void addGamePoint(DisposableObserver<String> objDesObserver, AddGamePointModel addGamePointModel) {
        RestClient.getInstance("Game").getService().addWalletPoint(addGamePointModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(String o) {
                        objDesObserver.onNext(o);
                    }

                    @Override
                    public void onError(Throwable e) {
                        objDesObserver.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        objDesObserver.onComplete();
                    }
                });
    }

    public void getRetailAppGame(DisposableObserver<ArrayList<GameModel>> observer, int customerId, int warehouseId) {
        RestClient.getInstance("Game").getService().getRetailAppGame(customerId, warehouseId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ArrayList<GameModel>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(ArrayList<GameModel> o) {
                        observer.onNext(o);
                    }

                    @Override
                    public void onError(Throwable e) {
                        observer.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        observer.onComplete();
                    }
                });
    }

    public void getRetailAppGameBanner(DisposableObserver<ArrayList<GamesBannerModel>> observer, int customerId, int warehouseId) {
        RestClient.getInstance("Game").getService().getRetailAppGameBanner(customerId, warehouseId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ArrayList<GamesBannerModel>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(ArrayList<GamesBannerModel> o) {
                        observer.onNext(o);
                    }

                    @Override
                    public void onError(Throwable e) {
                        observer.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        observer.onComplete();
                    }
                });
    }


    // prime request's
    public void getAllMemberShip(DisposableObserver<ArrayList<MembershipPlanModel>> observer, int cusId, String lang) {
        RestClient.getInstance("Prime").getService().getAllMemberShip(cusId, lang)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ArrayList<MembershipPlanModel>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(ArrayList<MembershipPlanModel> o) {
                        observer.onNext(o);
                    }

                    @Override
                    public void onError(Throwable e) {
                        observer.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        observer.onComplete();
                    }
                });
    }

    public void primePaymentRequest(DisposableObserver<PrimePaymentResponse> observer, PrimePaymentModel model) {
        RestClient.getInstance("Prime").getService().primePaymentRequest(model)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<PrimePaymentResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(PrimePaymentResponse o) {
                        observer.onNext(o);
                    }

                    @Override
                    public void onError(Throwable e) {
                        observer.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        observer.onComplete();
                    }
                });
    }

    public void primePaymentResponse(DisposableObserver<Boolean> observer, PrimePaymentModel model) {
        RestClient.getInstance("Prime").getService().primePaymentResponse(model)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(Boolean o) {
                        observer.onNext(o);
                    }

                    @Override
                    public void onError(Throwable e) {
                        observer.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        observer.onComplete();
                    }
                });
    }

    public void membershipDetail(DisposableObserver<MembershipModel> observer, int cusId, int wId, String lang) {
        RestClient.getInstance("Prime").getService().membershipDetail(cusId, wId, lang)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MembershipModel>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(MembershipModel o) {
                        observer.onNext(o);
                    }

                    @Override
                    public void onError(Throwable e) {
                        observer.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        observer.onComplete();
                    }
                });
    }

    public void fetchYourLevelData(DisposableObserver<ArrayList<YourLevelTargetModel>> observer, int customerid, String skCode) {
        RestClient.getInstance().getService().getYourLevelData(customerid, skCode)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ArrayList<YourLevelTargetModel>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(ArrayList<YourLevelTargetModel> o) {
                        observer.onNext(o);
                    }

                    @Override
                    public void onError(Throwable e) {
                        observer.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        observer.onComplete();
                    }
                });
    }


    public void fetchExpiringPointsList(final DisposableObserver<ArrayList<MyExpiringWalletModel>> observer, int customerId) {
        RestClient.getInstance().getService().getExpiringWalletPoints(customerId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ArrayList<MyExpiringWalletModel>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(ArrayList<MyExpiringWalletModel> o) {
                        observer.onNext(o);
                    }

                    @Override
                    public void onError(Throwable e) {
                        observer.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        observer.onComplete();
                    }
                });
    }

    public void fetchProductDetails(final DisposableObserver<ItemListModel> observer, int itemId, int customerId, int warehouseId, String lang) {
        RestClient.getInstance().getService().getProductDetails(itemId, customerId, warehouseId, lang)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ItemListModel>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(ItemListModel o) {
                        observer.onNext(o);
                    }

                    @Override
                    public void onError(Throwable e) {
                        observer.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        observer.onComplete();
                    }
                });
    }


    public void checkBookLimitDy(final DisposableObserver<JsonObject> observer, String Url, String apiKey, CheckBookCreditLimitRes creditLimitRes) {
        RestClient.getInstance3().getService3().checkBookCustomerLimitDy(Url, apiKey, creditLimitRes)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<JsonObject>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(JsonObject o) {
                        observer.onNext(o);
                    }

                    @Override
                    public void onError(Throwable e) {
                        observer.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        observer.onComplete();
                    }
                });

    }

    public void uploadContacts(DisposableObserver<JsonElement> observer, ArrayList<ContactUploadModel> contacts) {
        RestClient.getInstance().getService().uploadContacts(contacts)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<JsonElement>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onNext(@NonNull JsonElement o) {
                        observer.onNext(o);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        observer.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        observer.onComplete();
                    }
                });
    }

    public void paymentRequest(DisposableObserver<Long> observer, PaymentRequestModel model) {
        RestClient.getInstance().getService().paymentRequest(model)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onNext(@NonNull Long o) {
                        observer.onNext(o);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        observer.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        observer.onComplete();
                    }
                });
    }

    public void postPaymentResponse(DisposableObserver<Boolean> observer, PostUPIPaymentResponse model) {
        RestClient.getInstance().getService().postPaymentResponse(model)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onNext(@NonNull Boolean o) {
                        observer.onNext(o);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        observer.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        observer.onComplete();
                    }
                });
    }

    public void fetchGullakDataList(DisposableObserver<ArrayList<GullakModel>> observer, int custId, int page, int totalOrder) {
        RestClient.getInstance().getService().getGullakData(custId, page, totalOrder)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ArrayList<GullakModel>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onNext(@NonNull ArrayList<GullakModel> o) {
                        observer.onNext(o);
                    }


                    @Override
                    public void onError(@NonNull Throwable e) {
                        observer.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        observer.onComplete();
                    }
                });
    }

    public void getGullakBalance(DisposableObserver<JsonObject> observer, int customerId) {
        RestClient.getInstance().getService().getGullakBalance1(customerId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<JsonObject>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onNext(@NonNull JsonObject o) {
                        observer.onNext(o);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        observer.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        observer.onComplete();
                    }
                });
    }

    public void getNotfayItems(final DisposableObserver<Boolean> objDesObserver, int customerId, int warehouseId, String itemNumber) {
        RestClient.getInstance().getService().getNotfayItems1(customerId, warehouseId, itemNumber)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onNext(@NonNull Boolean o) {
                        objDesObserver.onNext(o);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        objDesObserver.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        objDesObserver.onComplete();
                    }
                });
    }

    public void notificationReceived(final DisposableObserver<JsonElement> observer, int notificationId, int customerId) {
        RestClient.getInstance().getService().notificationReceived(notificationId, customerId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<JsonElement>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull JsonElement o) {
                        observer.onNext(o);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        observer.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        observer.onComplete();
                    }
                });
    }


    public void generateLead(final DisposableObserver<JsonObject> observer, String Url) {
        RestClient.getInstance().getService().generateLead1(Url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<JsonObject>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(JsonObject o) {
                        observer.onNext(o);
                    }

                    @Override
                    public void onError(Throwable e) {
                        observer.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        observer.onComplete();
                    }
                });
    }


    // sk credit limit
    public void getCreditLimit(final DisposableObserver<CreditLimit> observer, int custId) {
        RestClient.getInstance("Payment").getService().getCreditLimit1(custId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CreditLimit>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onNext(@NonNull CreditLimit o) {
                        observer.onNext(o);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        observer.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        observer.onComplete();
                    }
                });
    }

    public void creditPayment(final DisposableObserver<CreditLimit> observer, CreditPayment model) {
        RestClient.getInstance("Payment").getService().creditPayment1(model)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CreditLimit>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onNext(@NonNull CreditLimit o) {
                        observer.onNext(o);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        observer.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        observer.onComplete();
                    }
                });
    }

    // delivery slots
    public void updateDeliveryETA(final DisposableObserver<JsonObject> walletDes, JsonObject jsonObject, String mSectionType) {
        RestClient.getInstance(mSectionType).getService().updateDeliveryETA1(jsonObject)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<JsonObject>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(JsonObject o) {
                        walletDes.onNext(o);
                    }

                    @Override
                    public void onError(Throwable e) {
                        // customDialog.dismiss();
                        walletDes.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        //customDialog.dismiss();
                        walletDes.onComplete();
                    }
                });
    }


    public void getAppHomeBottomData(final DisposableObserver<ArrayList<RatingModel>> observer, String url) {
        RestClient.getInstance("").getService().getAppHomeBottomData1(url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ArrayList<RatingModel>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(ArrayList<RatingModel> o) {
                        observer.onNext(o);
                    }

                    @Override
                    public void onError(Throwable e) {
                        observer.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        observer.onComplete();
                    }
                });
    }

    public void getOrderDetail(final DisposableObserver<List<OrderDetailsModel>> observer, int orderId) {
        RestClient.getInstance("").getService().getOrderDetail(orderId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<OrderDetailsModel>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(List<OrderDetailsModel> o) {
                        observer.onNext(o);
                    }

                    @Override
                    public void onError(Throwable e) {
                        observer.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        observer.onComplete();
                    }
                });
    }

    public void getTripDetails(final DisposableObserver<TrackOrderModel> observer, int tripId, int customerId) {
        RestClient.getInstance("").getService().getTripDetails(tripId, customerId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<TrackOrderModel>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(TrackOrderModel o) {
                        observer.onNext(o);
                    }

                    @Override
                    public void onError(Throwable e) {
                        observer.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        observer.onComplete();
                    }
                });
    }

    public void getOrderETADate(final DisposableObserver<EtaDates> observer, int orderId) {
        RestClient.getInstance("").getService().getOrderETADate(orderId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<EtaDates>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(EtaDates o) {
                        observer.onNext(o);
                    }

                    @Override
                    public void onError(Throwable e) {
                        observer.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        observer.onComplete();
                    }
                });
    }

    public void getOrderStatus(final DisposableObserver<ArrayList<ReturnOrderStatusModel>> observer, int orderId) {
        RestClient.getInstance("").getService().getOrderStatus(orderId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ArrayList<ReturnOrderStatusModel>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onNext(@NonNull ArrayList<ReturnOrderStatusModel> o) {
                        observer.onNext(o);
                    }

                    @Override
                    public void onError(Throwable e) {
                        observer.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        observer.onComplete();
                    }
                });
    }

    public void uploadAudioFileApi(final DisposableObserver<Boolean> observer, UploadAudioModel uploadAudioModel) {
        RestClient.getInstance("").getService().uploadAudioFile(uploadAudioModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onNext(@NonNull Boolean o) {
                        observer.onNext(o);
                    }

                    @Override
                    public void onError(Throwable e) {
                        observer.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        observer.onComplete();
                    }
                });
    }

    public void getReferralConfig(final DisposableObserver<ArrayList<ReferralConfigModel>> observer, int cityId) {
        RestClient.getInstance("").getService().getReferralConfig(cityId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ArrayList<ReferralConfigModel>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onNext(@NonNull ArrayList<ReferralConfigModel> o) {
                        observer.onNext(o);
                    }

                    @Override
                    public void onError(Throwable e) {
                        observer.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        observer.onComplete();
                    }
                });
    }

    public void getReferredList(final DisposableObserver<ArrayList<ReferredModel>> observer, int customerId) {
        RestClient.getInstance("").getService().getReferredList(customerId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ArrayList<ReferredModel>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onNext(@NonNull ArrayList<ReferredModel> o) {
                        observer.onNext(o);
                    }

                    @Override
                    public void onError(Throwable e) {
                        observer.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        observer.onComplete();
                    }
                });
    }


    public void fetchRTGSDataList(DisposableObserver<ArrayList<GullakModel>> observer, int custId, int page, int totalOrder) {
        RestClient.getInstance().getService().fetchRTGSDataList(custId, page, totalOrder)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ArrayList<GullakModel>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onNext(@NonNull ArrayList<GullakModel> o) {
                        observer.onNext(o);
                    }


                    @Override
                    public void onError(@NonNull Throwable e) {
                        observer.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        observer.onComplete();
                    }
                });
    }

    public void getRTGSBalance(DisposableObserver<Double> observer, int customerId) {
        RestClient.getInstance().getService().getRTGSBalance1(customerId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Double>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onNext(@NonNull Double o) {
                        observer.onNext(o);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        observer.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        observer.onComplete();
                    }
                });
    }

    public void getOrderConcernByOrderId(DisposableObserver<DeliveryConcern> observer, int orderId) {
        RestClient.getInstance().getService().getOrderConcernByOrderId(orderId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DeliveryConcern>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onNext(@NonNull DeliveryConcern o) {
                        observer.onNext(o);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        observer.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        observer.onComplete();
                    }
                });
    }

    public void postOrderConcern(DisposableObserver<Boolean> observer, JsonObject jsonObject) {
        RestClient.getInstance().getService().postOrderConcern(jsonObject)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onNext(@NonNull Boolean o) {
                        observer.onNext(o);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        observer.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        observer.onComplete();
                    }
                });
    }


    // Clearance category API
    public void getClearanceItemCategory(final DisposableObserver<ArrayList<CategoriesModel>> CatItem, int wId, int customerID, String lang) {
        RestClient.getInstance("Category Item Header").getService().getClearanceItemCategory(wId, customerID, lang)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ArrayList<CategoriesModel>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(ArrayList<CategoriesModel> o) {
                        CatItem.onNext(o);
                    }

                    @Override
                    public void onError(Throwable e) {
                        CatItem.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        CatItem.onComplete();
                    }
                });
    }

    public void getClearanceItem(final DisposableObserver<ArrayList<ClearanceItemModel>> CatItem, SearchClearanceItemDc itemDc) {
        RestClient.getInstance("Category Item Header").getService().getClearanceItem(itemDc)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ArrayList<ClearanceItemModel>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(ArrayList<ClearanceItemModel> o) {
                        CatItem.onNext(o);
                    }

                    @Override
                    public void onError(Throwable e) {
                        CatItem.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        CatItem.onComplete();
                    }
                });
    }

    public void placeClearanceOrder(final DisposableObserver<ClearanceOrderResponse> observer, ClearanceShoppingCart model) {
        RestClient.getInstance("Place Order Button").getService().placeClearanceOrder(model)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ClearanceOrderResponse>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onNext(@NonNull ClearanceOrderResponse o) {
                        observer.onNext(o);
                    }


                    @Override
                    public void onError(@NonNull Throwable e) {
                        observer.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        observer.onComplete();
                    }
                });
    }

    public void getTcsPercent(final DisposableObserver<Double> observer, int customerId) {
        RestClient.getInstance("").getService().getTcsPercent(customerId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Double>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(Double o) {
                        observer.onNext(o);
                    }

                    @Override
                    public void onError(Throwable e) {
                        observer.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        observer.onComplete();
                    }
                });
    }

    public void applyClearanceOffer(DisposableObserver<ClearanceOfferResponse> observer, Boolean isApply, ClearanceShoppingCart cart) {
        RestClient.getInstance("").getService().applyClearanceOffer(isApply, cart)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ClearanceOfferResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(ClearanceOfferResponse o) {
                        observer.onNext(o);
                    }

                    @Override
                    public void onError(Throwable e) {
                        observer.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        observer.onComplete();
                    }
                });
    }

    public void clearanceOrderValidPayment(DisposableObserver<Boolean> observer, int isApply) {
        RestClient.getInstance("").getService().clearanceOrderValidPayment(isApply)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(Boolean o) {
                        observer.onNext(o);
                    }

                    @Override
                    public void onError(Throwable e) {
                        observer.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        observer.onComplete();
                    }
                });
    }

    public void getTargetItems(DisposableObserver<TargetResponseModel> observer, int companyId, int storeId, int custId, int wareHouseId, int peopleId, int skip, int take, String lang, String itemname) {
        RestClient.getInstance().getService().getTargetItems(companyId,
                        storeId,
                        custId,
                        wareHouseId,
                        peopleId,
                        skip,
                        take,
                        lang,
                        itemname)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<TargetResponseModel>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onNext(@NonNull TargetResponseModel o) {
                        observer.onNext(o);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        observer.onComplete();
                    }
                });
    }

    public void getAlreadyBoughtTargetItem(DisposableObserver<TargetResponseModel> observer, int companyId, int storeId, int custId, int wareHouseId, int peopleId, int skip, int take, String lang, String itemname) {
        Objects.requireNonNull(RestClient.getInstance().getService().getAlreadyBoughtTargetItem(companyId,
                        storeId,
                        custId,
                        wareHouseId,
                        peopleId,
                        skip,
                        take,
                        lang,
                        itemname))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<TargetResponseModel>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onNext(@NonNull TargetResponseModel o) {
                        observer.onNext(o);
                    }


                    @Override
                    public void onError(@NonNull Throwable e) {
                        observer.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        observer.onComplete();
                    }
                });
    }


    public void updateCustomerHoliday(DisposableObserver<CommonResponse> observer, CustomerHoliday model) {
        RestClient.getInstance().getService().updateCustomerHoliday(model)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CommonResponse>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onNext(@NonNull CommonResponse o) {
                        observer.onNext(o);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        observer.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        observer.onComplete();
                    }
                });
    }

    public void getUdhaarOverDue(DisposableObserver<JsonObject> observer, int custId, String lang) {
        RestClient.getInstance("").getService().getUdhaarOverDue1(custId, "Retailer", lang)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<JsonObject>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(JsonObject o) {
                        observer.onNext(o);
                    }

                    @Override
                    public void onError(Throwable e) {
                        observer.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        observer.onComplete();
                    }
                });
    }


    public void getCustomerBucketGames(DisposableObserver<BucketGameResponse> observer, int custId, int wareHouseId, int skip, int take, String lang) {
        RestClient.getInstance().getService().getCustomerBucketGames(custId, wareHouseId, skip, take, lang)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BucketGameResponse>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onNext(@NonNull BucketGameResponse o) {
                        observer.onNext(o);
                    }


                    @Override
                    public void onError(@NonNull Throwable e) {
                        observer.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        observer.onComplete();
                    }
                });
    }

    public void getCustomerGameStreak(DisposableObserver<BucketGameResponse> observer, int custId, String lang) {
        RestClient.getInstance().getService().getCustomerGameStreak(custId, lang)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BucketGameResponse>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onNext(@NonNull BucketGameResponse o) {
                        observer.onNext(o);
                    }


                    @Override
                    public void onError(@NonNull Throwable e) {
                        observer.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        observer.onComplete();
                    }
                });
    }

    public void getCustomerAchieveLevel(DisposableObserver<ArrayList<BucketCustomerModel>> observer, int custId, int skip, int take, String lang) {
        RestClient.getInstance().getService().getCustomerAchieveLevel(custId, skip, take, lang)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ArrayList<BucketCustomerModel>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onNext(@NonNull ArrayList<BucketCustomerModel> o) {
                        observer.onNext(o);
                    }


                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void getBucketEarningUserList(DisposableObserver<BucketCustomerGamesHistoryModel> observer, int custId) {
        RestClient.getInstance().getService().getBucketEarningUserList(custId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BucketCustomerGamesHistoryModel>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onNext(@NonNull BucketCustomerGamesHistoryModel o) {
                        observer.onNext(o);
                    }


                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    // scaleUp credit limit
    public void getScaleUpLimit(final DisposableObserver<CreditLimit> observer, int custId) {
        RestClient.getInstance("Payment").getService().getScaleUpLimit1(custId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CreditLimit>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onNext(@NonNull CreditLimit o) {
                        observer.onNext(o);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        observer.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        observer.onComplete();
                    }
                });
    }

    public void scaleUpPaymentInitiate(final DisposableObserver<ScaleUpResponse> observer, int custId, int orderId, double scaleUpAmt) {
        RestClient.getInstance("Payment").getService().scaleUpPaymentInitiate1(custId, orderId, scaleUpAmt)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ScaleUpResponse>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onNext(@NonNull ScaleUpResponse o) {
                        observer.onNext(o);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        observer.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        observer.onComplete();
                    }
                });
    }

    public void fetchRazorpayOrderId(DisposableObserver<String> observer, String orderId,double razorpayAmount ,String section) {
        RestClient.getInstance(section).getService().fetchRazorpayOrderId1(orderId,razorpayAmount)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(String object) {
                        observer.onNext(object);
                    }

                    @Override
                    public void onError(Throwable e) {
                        observer.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        observer.onComplete();
                    }
                });
    }

}