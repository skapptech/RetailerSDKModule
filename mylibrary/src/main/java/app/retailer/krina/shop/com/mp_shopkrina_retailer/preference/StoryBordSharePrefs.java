package app.retailer.krina.shop.com.mp_shopkrina_retailer.preference;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by User on 13-11-2018.
 */
public class StoryBordSharePrefs {
    public static String SHARED_PREF = "StoryBordPrefs";
    public static String KEY_ACCESS_TOKEN = "token";
    public static String HOMEPAGE = "home";
    public static String RETURNORDER = "returnorder";
    public static String RETURNREPLACE = "returnorder";
    public static String SHOPPINGCART = "shoppingcart";
    public static String PAYMENTOPTION = "paymentoption";
    public static String SUBSUBCATEGORY = "subsubcat";
    public static String MYORDER = "myorder";
    public static String MYWALLET = "mywallet";
    public static String CUSTOMERlIST = "customerList";
    public static String HISABKITAB_DETAILS = "hisab_kitab_details";
    public static String ADDD_CREDIT = "addcredit";
    public static String WUDU_INTRO_SCREEN = "wudu_intro_screen";
    public static String PREFERENCEES = "WUDU";
    public static String PREFERENCEAPPVER = "preference_app_ver";
    private final Context ctx;
    private final SharedPreferences sharedPreferences;
    private static StoryBordSharePrefs instance;


    public StoryBordSharePrefs(Context context) {
        ctx = context;
        sharedPreferences = context.getSharedPreferences(SHARED_PREF, 0);
    }

    public static StoryBordSharePrefs getInstance(Context ctx) {
        if (instance == null) {
            instance = new StoryBordSharePrefs(ctx);
        }
        return instance;
    }

    public void putString(String key, String val) {
        sharedPreferences.edit().putString(key, val).apply();
    }

    public String getString(String key) {
        return sharedPreferences.getString(key, "");
    }

    public void putBoolean(String key, Boolean val) {
        sharedPreferences.edit().putBoolean(key, val).apply();
    }

    public Boolean getBoolean(String key) {
        return sharedPreferences.getBoolean(key, false);
    }
}