package app.retailer.krina.shop.com.mp_shopkrina_retailer.preference;

import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Singleton;

import app.retailer.krina.shop.com.mp_shopkrina_retailer.BuildConfig;

@Singleton
public class EndPointPref {
    private final Context context;
    private static EndPointPref instance;
    private final SharedPreferences pref;

    public static final String API_ENDPOINT = "retailer_base_url";
    public static final String TRADE_ENDPOINT = "trade_base_url";
    public static final String EPAY_ENDPOINT = "ePay_base_url";
    public static final String CHECKBOOK_ENDPOINT = "checkbook_base_url";
    public static final String CART_POSITION = "cart_position";
    public static final String SEARCH_POSITION = "search_position";
    public static final String IS_ANALYTIC = "isAnalytics";
    public static final String IS_MIXPANEL = "isMixpanel";
    public static final String IS_VAN_RTGS = "isVanRtgs";
    public static final String AREA_RADIUS = "areaRadius";
    public static final String ADDRESS_RADIUS = "addressRadius";
    public static final String Show_Chat_Bot = "showChatBot";
    public static final String CLEARANCE_MIN_ORDER = "clearanceMinOrder";
    public static final String IS_SHOW_SELLER = "isShowSeller";
    public static final String IS_CL_SHOW_COD = "isClShowCOD";
    public static final String FCM_TOKEN = "fcm_token";

    public static final String IMAGE_UPLOAD_QTY = "imageUploadQty";
    public static final String logOutDays = "logOutDays";
    public static final String showNewSocial = "show_New_Social";

    public static final String showOfferBtn = "ShowOfferButton";
    public static final String IS_SCALEUP = "is_scale_up";

    public static final String IS_RAZORPAY_PAYMENT = "IsRazorpayPayment";
    public static final String IS_ICICI_PAYMENT = "IsICICIPayment";
    public static final String IS_SCALE_UP_SDK = "isScaleUpSdk";
    public static final String HDFC_NEW_UI = "hdfcNewUI";

    public EndPointPref(Context context) {
        this.context = context;
        pref = context.getSharedPreferences("ApiEndpointsPref", 0);
    }

    public static EndPointPref getInstance(Context context) {
        if (instance == null) {
            instance = new EndPointPref(context);
        }
        return instance;
    }


    public void putString(String key, String val) {
        pref.edit().putString(key, val).apply();
    }

    public String getString(String key) {
        return pref.getString(key, "https://internal.er15.xyz");
    }


    public void putInt(String key, int val) {
        pref.edit().putInt(key, val).apply();
    }

    public void putBoolean(String key, boolean val) {
        pref.edit().putBoolean(key, val).apply();
    }

    public boolean getBoolean(String key) {
        return pref.getBoolean(key, true);
    }


    public void putLong(String key, long val) {
        pref.edit().putLong(key, val).apply();
    }

    public long getLong(String key) {
        return pref.getLong(key, 0);
    }


    public String getBaseUrl() {
        if (BuildConfig.DEBUG)
            return pref.getString(API_ENDPOINT, "https://uat.shopkirana.in");
        else
            return pref.getString(API_ENDPOINT, "https://internal.er15.xyz");
    }

    public String getEpayEndpoint() {
        if (BuildConfig.DEBUG)
            return pref.getString(EPAY_ENDPOINT, "https://api-blackbox.epaylater.in");
        else
            return pref.getString(EPAY_ENDPOINT, "https://api2.epaylater.in:443");
    }

    public String getTradeEndpoint() {
        if (BuildConfig.DEBUG)
            return pref.getString(TRADE_ENDPOINT, "https://treaduatservice.shopkirana.in");
        else
            return pref.getString(TRADE_ENDPOINT, "https://tradeservice.er15.xyz:4436");
    }

    public int getCartPosition() {
        return pref.getInt(CART_POSITION, 2);
    }

    public int getSearchPosition() {
        return pref.getInt(SEARCH_POSITION, 2);
    }

    public int getAreaRadius() {
        return pref.getInt(AREA_RADIUS, 50000);
    }

    public int getAddressRadius() {
        return pref.getInt(ADDRESS_RADIUS, 2000);
    }

    public String getFcmToken(String key) {
        return pref.getString(key, "");
    }


    public void clear() {
        pref.edit().clear().apply();
    }
}
