package app.retailer.krina.shop.com.mp_shopkrina_retailer.preference;

import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Singleton;

@Singleton
public class SectionPref {
    // Murli story read page count
    public static final String APP_HOME = "app_home_data";
    public static final String PRODUCT_LIST = "product_list";
    public static String CATEGORY_BY_ID = "category_by_id";

    private static SectionPref sectionPref;
    private final SharedPreferences pref;


    public static SectionPref getInstance(Context context) {
        if (sectionPref == null) {
            sectionPref = new SectionPref(context);
        }
        return sectionPref;
    }

    public SectionPref(Context context) {
        pref = context.getSharedPreferences("productPref", 0);
    }

    public void putString(String key, String val) {
        pref.edit().putString(key, val).apply();
    }

    public String getString(String key) {
        return pref.getString(key, null);
    }


    public void clear() {
        pref.edit().clear().apply();
    }
}