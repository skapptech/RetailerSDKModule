package app.retailer.krina.shop.com.mp_shopkrina_retailer.data.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import app.retailer.krina.shop.com.mp_shopkrina_retailer.BuildConfig;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.EndPointPref;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.SharePrefs;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Aes256;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.MyApplication;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.TextUtils;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Singleton
public class RestClient {
    private static Retrofit retrofit = null, retrofit1 = null;
    private static Retrofit retrofit2 = null;
    private static Retrofit retrofit3 = null;
    private static Retrofit retrofit4 = null;

    private static final RestClient ourInstance = new RestClient();
    private static RestClient restClient1;
    private static final RestClient restClient2 = new RestClient(0);
    private static RestClient restClient3, restClient4;
    private static String mSectionType;
    private Request request;


    public static RestClient getInstance() {
        mSectionType = "";
        return ourInstance;
    }

    public static RestClient getInstance(String sectionType) {
        mSectionType = sectionType;
        return ourInstance;
    }

    public static RestClient getInstance1() {
        mSectionType = "";
        if (restClient1 == null) {
            restClient1 = new RestClient("epay");
        }
        return restClient1;
    }

    public static RestClient getInstance2() {
        mSectionType = "";
        return restClient2;
    }

    public static RestClient getInstance3() {
        mSectionType = "";
        if (restClient3 == null) {
            restClient3 = new RestClient("cb");
        }
        return restClient3;
    }

    public static RestClient getInstance4() {
        mSectionType = "";
        if (restClient4 == null) {
            restClient4 = new RestClient("commute");
        }
        return restClient4;
    }


    private RestClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(5, TimeUnit.MINUTES)
                .connectTimeout(6, TimeUnit.MINUTES)
                .writeTimeout(5, TimeUnit.MINUTES)
                .retryOnConnectionFailure(false)
                .addInterceptor(chain -> {
                    Response response = null;
                    try {
                        request = chain.request();
                        response = chain.proceed(request);
                        if (response.code() == 401 && !request.url().toString().contains("api/RetailerApp/Customerprofile")) {
                            MyApplication.getInstance().token();
                        }
                        if (response.code() == 200) {
                            if (!request.url().toString().contains("/GetCompanyDetailsForRetailerWithToken") &&
                                    !request.url().toString().contains("/token") &&
                                    !request.url().toString().contains("/appVersion") &&
                                    !request.url().toString().contains("/imageupload") &&
                                    !request.url().toString().contains("/UploadSalesReturnImage") &&
                                    !request.url().toString().contains("/GenerateToken") &&
                                    !request.url().toString().contains("/UPI/InitiateDUPayInetentReq") &&
                                    !request.url().toString().contains("/place/autocomplete") &&
                                    !request.url().toString().contains("/pg/api/command")) {
                                try {
                                    JSONObject jsonObject = new JSONObject();
                                    jsonObject.put("message", new JSONObject(response.body().string()));
                                    String data = jsonObject.getJSONObject("message").getString("Data");
                                    String destr = Aes256.decrypt(data, new SimpleDateFormat("yyyyMMdd", Locale.ENGLISH).format(new Date()) + "1201");
                                    if (BuildConfig.DEBUG) {
                                        printMsg(destr);
                                    }
                                    MediaType contentType = response.body().contentType();
                                    ResponseBody responseBody = ResponseBody.create(contentType, destr);
                                    return response.newBuilder().body(responseBody).build();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    MediaType contentType = null;
                                    ResponseBody responseBody = ResponseBody.create(contentType, "destr");
                                    return response.newBuilder().body(responseBody).build();
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
//                    Response response1 = (response != null) ? response : new Response.Builder()
//                            .code(200)
//                            .request(chain.request())
//                            .protocol(Protocol.HTTP_1_0)
//                            .message("asd")
//                            .body(ResponseBody.create(MediaType.parse("text/plain"), "asd"))
//                            .build();
                    return response;
                })
                .addInterceptor(chain -> {
                    request = chain.request().newBuilder()
                            .header("username", Utils.getCustMobile(MyApplication.getInstance()))
                            .header("customerType", Utils.getCustomerType(MyApplication.getInstance()))
                            .header("activity", MyApplication.getInstance().activity == null ? "" : MyApplication.getInstance().activity.getClass().getSimpleName())
                            .header("section", TextUtils.isNullOrEmpty(mSectionType) ? "" : mSectionType)
                            .header("deviceId", MyApplication.getInstance().activity == null ? "" : Utils.getDeviceUniqueID(MyApplication.getInstance().activity))
                            .addHeader("authorization", "Bearer " + Utils.getToken(MyApplication.getInstance().activity))
//                            .addHeader("NoEncryption", "1")
                            .build();
                    return chain.proceed(request);
                })
                .addInterceptor(interceptor)
                .build();

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(EndPointPref.getInstance(MyApplication.getInstance()).getBaseUrl())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(client)
                    .build();
        }
    }

    private RestClient(String url) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(5, TimeUnit.MINUTES)
                .connectTimeout(6, TimeUnit.MINUTES)
                .writeTimeout(5, TimeUnit.MINUTES)
                .addInterceptor(interceptor)
                .retryOnConnectionFailure(false)
                .build();
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        if (url.equalsIgnoreCase("epay")) {
            retrofit1 = new Retrofit.Builder()
                    .baseUrl(EndPointPref.getInstance(MyApplication.getInstance()).getEpayEndpoint())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(client)
                    .build();
        } else if (url.equalsIgnoreCase("cb")) {
            retrofit3 = new Retrofit.Builder()
                    .baseUrl(EndPointPref.getInstance(MyApplication.getInstance()).getString(EndPointPref.CHECKBOOK_ENDPOINT))
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(client)
                    .build();
        } else {
            retrofit4 = new Retrofit.Builder()
                    .baseUrl(SharePrefs.getInstance(MyApplication.getInstance()).getString(SharePrefs.TRADE_WEB_URL))
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(client)
                    .build();
        }
    }

    private RestClient(int url) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(5, TimeUnit.MINUTES)
                .connectTimeout(6, TimeUnit.MINUTES)
                .writeTimeout(5, TimeUnit.MINUTES)
                .addInterceptor(chain -> {
                    Response response = null;
                    try {
                        request = chain.request();
                        response = chain.proceed(request);
                        if (response.code() == 200) {
                            if (!request.url().toString().contains("/token") &&
                                    !request.url().toString().contains("/appVersion") &&
                                    !request.url().toString().contains("/UploadHisabKitabImage")) {
                                try {
                                    JSONObject jsonObject = new JSONObject();
                                    jsonObject.put("message", new JSONObject(response.body().string()));
                                    String data = jsonObject.getJSONObject("message").getString("Data");
                                    String destr = Aes256.decrypt(data, new SimpleDateFormat("yyyyMMdd", Locale.ENGLISH).format(new Date()) + "1201");

                                    if (BuildConfig.DEBUG) {
                                        printMsg(destr);
                                    }

                                    MediaType contentType = response.body().contentType();
                                    ResponseBody responseBody = ResponseBody.create(contentType, data);
                                    return response.newBuilder().body(responseBody).build();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return response;
                })
                .addInterceptor(chain -> {
                    request = chain.request().newBuilder()
                            .header("HKCustomerId", Utils.getHKCustomerID(MyApplication.getInstance().activity))
                            .addHeader("authorization", "Bearer " + Utils.getToken(MyApplication.getInstance().activity))
                            .addHeader("NoEncryption", "1")
                            .build();
                    return chain.proceed(request);
                })
                .addInterceptor(interceptor)
                .retryOnConnectionFailure(false)
                .build();

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        retrofit2 = new Retrofit.Builder()
                .baseUrl(EndPointPref.getInstance(MyApplication.getInstance()).getTradeEndpoint())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build();
    }


    public APIServices getService() {
        return retrofit.create(APIServices.class);
    }

    public APIServices getService1() {
        return retrofit1.create(APIServices.class);
    }

    public APIServices getService2() {
        return retrofit2.create(APIServices.class);
    }

    public APIServices getService3() {
        return retrofit3.create(APIServices.class);
    }

    public APIServiceCom getService4() {
        return retrofit4.create(APIServiceCom.class);
    }


    private void printMsg(String msg) {
        int chunkCount = msg.length() / 4050;     // integer division
        for (int i = 0; i <= chunkCount; i++) {
            int max = 4050 * (i + 1);
            if (max >= msg.length()) {
                System.out.println(msg.substring(4050 * i));
            } else {
                System.out.println(msg.substring(4050 * i, max));
            }
        }
    }
}