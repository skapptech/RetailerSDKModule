package app.retailer.krina.shop.com.mp_shopkrina_retailer.preference;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Lincoln on 05/05/16.
 */
public class PrefManager {
    private final SharedPreferences pref;
    private final SharedPreferences.Editor editor;
    // Shared preferences file name
    private static final String PREF_NAME = "retailerPref";
    private static final String IS_LOGGED_IN = "isLoggedIn";
    private static final String IS_FIRST_TIME_LAUNCH_SHOWCASE = "IsFirstTimeLaunchSHOWCASE";


    public PrefManager(Context context) {
        pref = context.getSharedPreferences(PREF_NAME, 0);
        editor = pref.edit();
    }

    public void setLoggedIn(boolean isFirstTime) {
        editor.putBoolean(IS_LOGGED_IN, isFirstTime);
        editor.commit();
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGGED_IN, false);
    }

    public void setShowcaseFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH_SHOWCASE, isFirstTime);
        editor.commit();
    }
}