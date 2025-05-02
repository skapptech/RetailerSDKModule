package app.retailer.krina.shop.com.mp_shopkrina_retailer.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;

import java.util.Locale;

import app.retailer.krina.shop.com.mp_shopkrina_retailer.R;

/**
 * Created by abdalla on 10/2/17.
 */
public class LocaleHelper {
    private static final String SELECTED_LANGUAGE = "Locale.Helper.Selected.Language";
    private static final String LANG_PREF = "lang_pref";

    public static Context onAttach(Context context) {
        String lang = getPersistedData(context, Locale.getDefault().getLanguage());
        return setLocale(context, lang);
    }

    public static Context onAttach(Context context, String defaultLanguage) {
        String lang = getPersistedData(context, defaultLanguage);
        return setLocale(context, lang);
    }

    public static String getLanguage(Context context) {
        //return SharePrefs.getInstance(context).getString(SharePrefs.SELECTED_LANGUAGE);
        //return getPersistedData(context, Locale.getDefault().getLanguage());
        try {
            return RetailerSDKApp.getInstance().dbHelper.getString(R.string.language_code);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "hi";
    }

    public static Context setLocale(Context context, String language) {
        SharedPreferences preferences = context.getSharedPreferences(LANG_PREF, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(SELECTED_LANGUAGE, language == null ? "en" : language);
        editor.apply();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return updateResources(context, language);
        }
        return updateResourcesLegacy(context, language);
       // return context;
    }


    private static String getPersistedData(Context context, String defaultLanguage) {
        SharedPreferences preferences = context.getSharedPreferences(LANG_PREF, 0);
        return preferences.getString(SELECTED_LANGUAGE, defaultLanguage);
    }

    @TargetApi(Build.VERSION_CODES.N)
    private static Context updateResources(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Configuration configuration = context.getResources().getConfiguration();
        configuration.setLocale(locale);
        configuration.setLayoutDirection(locale);

        return context.createConfigurationContext(configuration);
    }

    private static Context updateResourcesLegacy(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Configuration config = context.getResources().getConfiguration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            config.setLocale(locale);
        } else {
            config.locale = locale;
        }
//        config.setLayoutDirection(locale);
        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
        return context;
    }
}