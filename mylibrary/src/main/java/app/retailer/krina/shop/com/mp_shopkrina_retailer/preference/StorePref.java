package app.retailer.krina.shop.com.mp_shopkrina_retailer.preference;

import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Singleton;

@Singleton
public class StorePref {
    // Murli story read page count
    public static final String PRODUCT_LIST = "product_list";

    private static StorePref sectionPref;
    private final SharedPreferences pref;


    public static StorePref getInstance(Context context) {
        if (sectionPref == null) {
            sectionPref = new StorePref(context);
        }
        return sectionPref;
    }

    public StorePref(Context context) {
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