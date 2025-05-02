package app.retailer.krina.shop.com.mp_shopkrina_retailer.utils;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;

import androidx.appcompat.app.AppCompatDelegate;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;
import com.mixpanel.android.mpmetrics.MixpanelAPI;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import app.retailer.krina.shop.com.mp_shopkrina_retailer.BuildConfig;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.api.CommonClassForAPI;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.db.NoteRepository;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.auth.MobileSignUpActivity;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.AnalyticPost;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.offer.BillDiscountModel;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.ItemListModel;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.auth.TokenResponse;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.EndPointPref;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.PrefManager;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.SectionPref;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.SharePrefs;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.StorePref;
import io.reactivex.observers.DisposableObserver;

/**
 * Created by user on 5/26/2017.
 */
public class RetailerSDKApp {
    private static RetailerSDKApp mInstance;
    public static Application application;
    public static Context context;

    public boolean CHECK_FROM_COME = true;
    public NoteRepository noteRepository;
    public FirebaseAnalytics mFirebaseAnalytics;
    public MixpanelAPI mixpanel;
    public Activity activity;
    public NoteRepository dbHelper;
    public PrefManager prefManager;
    //
    public boolean isReloadCart = false;
    public boolean isCommentOpen = false;

    public RetailerSDKApp() {
        onCreate();
    }

    public static synchronized RetailerSDKApp getInstance() {
        if (mInstance == null) {
            mInstance = new RetailerSDKApp();
        }
        return mInstance;
    }

    public static void initialize(Application context1) {
        application = context1;
        context = context1;
        mInstance = new RetailerSDKApp();
    }


//    @Override
//    protected void attachBaseContext(Context base) {
//        super.attachBaseContext(LocaleHelper.onAttach(base, "hi"));
    ////        MultiDex.install(context);
//    }

//    @Override
    public void onCreate() {
//        super.onCreate();
        // FirebaseApp.initializeApp(context);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        noteRepository = dbHelper = new NoteRepository(context);
        prefManager = new PrefManager(context);

        // firebase initialization
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
        mFirebaseAnalytics.setAnalyticsCollectionEnabled(true);
        if (prefManager.isLoggedIn()) {
            if (BuildConfig.DEBUG)
                mixpanel = MixpanelAPI.getInstance(context, "4c85b0cafb34fb769a7fc6c245251f89"); //4c85b0cafb34fb769a7fc6c245251f89-UAT//e854f88fd27eb00e4a22510664074b11
            else
                mixpanel = MixpanelAPI.getInstance(context, "26f73a110b4ca5288ecaad561ced74e9");
            mixpanel.identify(SharePrefs.getInstance(context).getString(SharePrefs.SK_CODE));
            mixpanel.getPeople().identify(SharePrefs.getInstance(context).getString(SharePrefs.SK_CODE));
            mixpanel.getPeople().set("name", SharePrefs.getInstance(context).getString(SharePrefs.CUSTOMER_NAME));
            mixpanel.getPeople().set("email", SharePrefs.getInstance(context).getString(SharePrefs.CUSTOMER_EMAIL));
            mixpanel.getPeople().set("city", SharePrefs.getInstance(context).getString(SharePrefs.CITY_NAME));
            // extras
            mixpanel.getPeople().set("shopName", SharePrefs.getInstance(context).getString(SharePrefs.SHOP_NAME));
            mixpanel.getPeople().set("SkCode", SharePrefs.getInstance(context).getString(SharePrefs.SK_CODE));
            mixpanel.getPeople().set("clusterId", SharePrefs.getInstance(context).getString(SharePrefs.CLUSTER_ID));
            mixpanel.getPeople().set("warehouse", SharePrefs.getInstance(context).getInt(SharePrefs.WAREHOUSE_ID));
            mixpanel.getPeople().set("isLoggedIn", true);
            // firebase
            mFirebaseAnalytics.setUserId(SharePrefs.getInstance(context).getString(SharePrefs.SK_CODE));
            mFirebaseAnalytics.setUserProperty("SkCode", SharePrefs.getInstance(context).getString(SharePrefs.SK_CODE));
            mFirebaseAnalytics.setUserProperty("city", SharePrefs.getInstance(context).getString(SharePrefs.CITY_NAME));
            mFirebaseAnalytics.setUserProperty("warehouse", "" + SharePrefs.getInstance(context).getInt(SharePrefs.WAREHOUSE_ID));
            mFirebaseAnalytics.setUserProperty("clusterId", SharePrefs.getInstance(context).getString(SharePrefs.CLUSTER_ID));
        }
        updateAnalytics("app_open");
    }


    public void startAnalyticSession() {
        if (prefManager.isLoggedIn()) {
            if (BuildConfig.DEBUG)
                mixpanel = MixpanelAPI.getInstance(context, "4c85b0cafb34fb769a7fc6c245251f89");
            else
                mixpanel = MixpanelAPI.getInstance(context, "26f73a110b4ca5288ecaad561ced74e9");
            mixpanel.identify(SharePrefs.getInstance(context).getString(SharePrefs.SK_CODE));
            mixpanel.getPeople().identify(SharePrefs.getInstance(context).getString(SharePrefs.SK_CODE));
            mixpanel.getPeople().set("name", SharePrefs.getInstance(context).getString(SharePrefs.CUSTOMER_NAME));
            mixpanel.getPeople().set("email", SharePrefs.getInstance(context).getString(SharePrefs.CUSTOMER_EMAIL));
            mixpanel.getPeople().set("city", SharePrefs.getInstance(context).getString(SharePrefs.CITY_NAME));
            // extras
            mixpanel.getPeople().set("shopName", SharePrefs.getInstance(context).getString(SharePrefs.SHOP_NAME));
            mixpanel.getPeople().set("SkCode", SharePrefs.getInstance(context).getString(SharePrefs.SK_CODE));
            mixpanel.getPeople().set("clusterId", SharePrefs.getInstance(context).getString(SharePrefs.CLUSTER_ID));
            mixpanel.getPeople().set("warehouse", SharePrefs.getInstance(context).getInt(SharePrefs.WAREHOUSE_ID));
            // firebase
            mFirebaseAnalytics.setUserId(SharePrefs.getInstance(context).getString(SharePrefs.SK_CODE));
            mFirebaseAnalytics.setUserProperty("SkCode", SharePrefs.getInstance(context).getString(SharePrefs.SK_CODE));
            mFirebaseAnalytics.setUserProperty("city", SharePrefs.getInstance(context).getString(SharePrefs.CITY_NAME));
            mFirebaseAnalytics.setUserProperty("warehouse", "" + SharePrefs.getInstance(context).getInt(SharePrefs.WAREHOUSE_ID));
            mFirebaseAnalytics.setUserProperty("clusterId", SharePrefs.getInstance(context).getString(SharePrefs.CLUSTER_ID));
        }
    }

    public void updateAnalyticAuth(String eventName, String method, String number) {
        if (EndPointPref.getInstance(context).getBoolean(EndPointPref.IS_ANALYTIC)) {
            Bundle bundle = new Bundle();
            bundle.putString(FirebaseAnalytics.Param.METHOD, method);
            bundle.putString("number", number);
            mFirebaseAnalytics.logEvent(eventName, bundle);
        }
        if (EndPointPref.getInstance(context).getBoolean(EndPointPref.IS_MIXPANEL)) {
            try {
                JSONObject props = new JSONObject();
                props.put("number", number);
                if (mixpanel != null)
                    mixpanel.track(eventName, props);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void updateAnalytics(String eventName) {
        eventName = eventName.replace(" ", "");
        eventName = eventName.replace("&", "_");
        eventName = eventName.replace(",", "_");
        if (EndPointPref.getInstance(context).getBoolean(EndPointPref.IS_ANALYTIC)) {
            Bundle bundle = new Bundle();
            bundle.putString("SkCode", SharePrefs.getInstance(context).getString(SharePrefs.SK_CODE));
            bundle.putString("shop_name", SharePrefs.getInstance(context).getString(SharePrefs.SHOP_NAME));
            bundle.putString("city", SharePrefs.getInstance(context).getString(SharePrefs.CITY_NAME));
            bundle.putInt("warehouse", SharePrefs.getInstance(context).getInt(SharePrefs.WAREHOUSE_ID));
            bundle.putString("clusterId", SharePrefs.getInstance(context).getString(SharePrefs.CLUSTER_ID));
            mFirebaseAnalytics.logEvent(eventName, bundle);
        }
        if (EndPointPref.getInstance(context).getBoolean(EndPointPref.IS_MIXPANEL)) {
            try {
                JSONObject props = new JSONObject();
                props.put("customer_name", SharePrefs.getInstance(context).getString(SharePrefs.CUSTOMER_NAME));
                props.put("shop_name", SharePrefs.getInstance(context).getString(SharePrefs.SHOP_NAME));
                props.put("SkCode", SharePrefs.getInstance(context).getString(SharePrefs.SK_CODE));
                props.put("city", SharePrefs.getInstance(context).getString(SharePrefs.CITY_NAME));
                props.put("warehouse", SharePrefs.getInstance(context).getInt(SharePrefs.WAREHOUSE_ID));
                props.put("clusterId", SharePrefs.getInstance(context).getString(SharePrefs.CLUSTER_ID));
                if (mixpanel != null)
                    mixpanel.track(eventName, props);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void updateAnalytics(String eventName, AnalyticPost analyticPost) {
        eventName = eventName.replace(" ", "_");
        if (EndPointPref.getInstance(context).getBoolean(EndPointPref.IS_ANALYTIC)) {
            Bundle bundle = new Bundle();
            bundle.putInt("sectionId", analyticPost.sectionId);
            bundle.putString("sectionSubType", analyticPost.sectionSubType);
            bundle.putString("sectionName", analyticPost.sectionName);
            bundle.putString("url", analyticPost.url);
            bundle.putString("baseCatId", analyticPost.baseCatId);
            bundle.putInt("categoryId", analyticPost.categoryId);
            bundle.putInt("subCatId", analyticPost.subCatId);
            bundle.putInt("subSubCatId", analyticPost.subSubCatId);
            bundle.putString("categoryName", analyticPost.categoryName);
            bundle.putString("source", analyticPost.source);
            // user data
            bundle.putString("SkCode", SharePrefs.getInstance(context).getString(SharePrefs.SK_CODE));
            bundle.putString("shop_name", SharePrefs.getInstance(context).getString(SharePrefs.SHOP_NAME));
            bundle.putString("city", SharePrefs.getInstance(context).getString(SharePrefs.CITY_NAME));
            bundle.putInt("warehouse", SharePrefs.getInstance(context).getInt(SharePrefs.WAREHOUSE_ID));
            bundle.putString("clusterId", SharePrefs.getInstance(context).getString(SharePrefs.CLUSTER_ID));
            // event
            mFirebaseAnalytics.logEvent(eventName, bundle);
        }
        if (EndPointPref.getInstance(context).getBoolean(EndPointPref.IS_MIXPANEL)) {
            try {
                JSONObject props = new JSONObject();
                props.put("sectionId", analyticPost.sectionId);
                props.put("sectionSubType", analyticPost.sectionSubType);
                props.put("sectionName", analyticPost.sectionName);
                props.put("url", analyticPost.url);
                props.put("baseCatId", analyticPost.baseCatId);
                props.put("categoryId", analyticPost.categoryId);
                props.put("subCatId", analyticPost.subCatId);
                props.put("subSubCatId", analyticPost.subSubCatId);
                props.put("categoryName", analyticPost.categoryName);
                props.put("source", analyticPost.source);

                props.put("SkCode", SharePrefs.getInstance(context).getString(SharePrefs.SK_CODE));
                props.put("clusterId", SharePrefs.getInstance(context).getString(SharePrefs.CLUSTER_ID));
                props.put("city", SharePrefs.getInstance(context).getString(SharePrefs.CITY_NAME));
                props.put("warehouse", "" + SharePrefs.getInstance(context).getInt(SharePrefs.WAREHOUSE_ID));
                mixpanel.track(eventName, props);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public void updateAnalyticShareProd(ItemListModel model) {
        if (EndPointPref.getInstance(context).getBoolean(EndPointPref.IS_ANALYTIC)) {
            Bundle bundle = new Bundle();
            bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, model.getItemNumber());
            bundle.putInt(FirebaseAnalytics.Param.ITEM_ID, model.getItemId());
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, model.itemname);
            bundle.putInt(FirebaseAnalytics.Param.ITEM_VARIANT, model.getMinOrderQty());
            bundle.putInt(FirebaseAnalytics.Param.LOCATION_ID, model.getWarehouseId());
            // user data
            bundle.putString("SkCode", SharePrefs.getInstance(context).getString(SharePrefs.SK_CODE));

            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SHARE, bundle);
        }
        if (EndPointPref.getInstance(context).getBoolean(EndPointPref.IS_MIXPANEL)) {
            try {
                JSONObject props = new JSONObject();
                props.put(FirebaseAnalytics.Param.CONTENT_TYPE, model.getItemNumber());
                props.put(FirebaseAnalytics.Param.ITEM_ID, model.getItemId());
                props.put(FirebaseAnalytics.Param.ITEM_NAME, model.itemname);
                props.put(FirebaseAnalytics.Param.ITEM_VARIANT, model.getMinOrderQty());
                props.put(FirebaseAnalytics.Param.LOCATION_ID, model.getWarehouseId());
                // user data
                props.put("SkCode", SharePrefs.getInstance(context).getString(SharePrefs.SK_CODE));
                props.put("city", SharePrefs.getInstance(context).getString(SharePrefs.CITY_NAME));
                props.put("warehouse", "" + SharePrefs.getInstance(context).getInt(SharePrefs.WAREHOUSE_ID));
                props.put("clusterId", SharePrefs.getInstance(context).getString(SharePrefs.CLUSTER_ID));
                // event
                mixpanel.track(FirebaseAnalytics.Event.SHARE, props);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void updateAnalyticShare(String screen, String content) {
        if (EndPointPref.getInstance(context).getBoolean(EndPointPref.IS_ANALYTIC)) {
            Bundle bundle = new Bundle();
            bundle.putString("screen", screen);
            bundle.putString("content", content);
            // user data
            bundle.putString("SkCode", SharePrefs.getInstance(context).getString(SharePrefs.SK_CODE));

            mFirebaseAnalytics.logEvent("share_app", bundle);
        }
        if (EndPointPref.getInstance(context).getBoolean(EndPointPref.IS_MIXPANEL)) {
            try {
                JSONObject props = new JSONObject();
                props.put("screen", screen);
                props.put("content", content);
                // user data
                props.put("SkCode", SharePrefs.getInstance(context).getString(SharePrefs.SK_CODE));
                props.put("city", SharePrefs.getInstance(context).getString(SharePrefs.CITY_NAME));
                props.put("warehouse", "" + SharePrefs.getInstance(context).getInt(SharePrefs.WAREHOUSE_ID));
                props.put("clusterId", SharePrefs.getInstance(context).getString(SharePrefs.CLUSTER_ID));
                // event
                mixpanel.track("share_app", props);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public void updateAnalyticSearch(String searchTerm) {
        if (EndPointPref.getInstance(context).getBoolean(EndPointPref.IS_ANALYTIC)) {
            Bundle bundle = new Bundle();
            bundle.putString(FirebaseAnalytics.Param.SEARCH_TERM, searchTerm);
            RetailerSDKApp.getInstance().mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SEARCH, bundle);
        }
        if (EndPointPref.getInstance(context).getBoolean(EndPointPref.IS_MIXPANEL)) {
            try {
                JSONObject props = new JSONObject();
                props.put(FirebaseAnalytics.Param.SEARCH_TERM, searchTerm);
                // user data
                props.put("SkCode", SharePrefs.getInstance(context).getString(SharePrefs.SK_CODE));
                props.put("city", SharePrefs.getInstance(context).getString(SharePrefs.CITY_NAME));
                props.put("warehouse", "" + SharePrefs.getInstance(context).getInt(SharePrefs.WAREHOUSE_ID));
                props.put("clusterId", SharePrefs.getInstance(context).getString(SharePrefs.CLUSTER_ID));
                // event
                mixpanel.track(FirebaseAnalytics.Event.SEARCH, props);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void updateAnalyticVSR(ArrayList<ItemListModel> list) {
        if (EndPointPref.getInstance(context).getBoolean(EndPointPref.IS_ANALYTIC)) {
            Parcelable[] parcelables = new Parcelable[list.size()];
            for (int i = 0; i < list.size(); i++) {
                Bundle itemJeggings = new Bundle();
                itemJeggings.putInt(FirebaseAnalytics.Param.ITEM_ID, list.get(i).getItemId());
                itemJeggings.putString(FirebaseAnalytics.Param.ITEM_NAME, list.get(i).itemname);
                itemJeggings.putInt(FirebaseAnalytics.Param.ITEM_CATEGORY, list.get(i).getCategoryid());
                itemJeggings.putInt(FirebaseAnalytics.Param.ITEM_BRAND, list.get(i).getSubsubCategoryid());
                itemJeggings.putDouble(FirebaseAnalytics.Param.PRICE, list.get(i).getUnitPrice());
                itemJeggings.putLong(FirebaseAnalytics.Param.QUANTITY, list.get(i).qty);
                itemJeggings.putInt(FirebaseAnalytics.Param.ITEM_VARIANT, list.get(i).getMinOrderQty());
                parcelables[i] = itemJeggings;
            }

            Bundle bundle = new Bundle();
            bundle.putInt(FirebaseAnalytics.Param.ITEM_LIST_ID, list.get(0).getStoreId());
            bundle.putString(FirebaseAnalytics.Param.ITEM_LIST_NAME, list.get(0).getStoreName());
            bundle.putParcelableArray(FirebaseAnalytics.Param.ITEMS, parcelables);
            // user data
            bundle.putString("SkCode", SharePrefs.getInstance(context).getString(SharePrefs.SK_CODE));
            bundle.putString("city", SharePrefs.getInstance(context).getString(SharePrefs.CITY_NAME));
            bundle.putString("warehouse", "" + SharePrefs.getInstance(context).getInt(SharePrefs.WAREHOUSE_ID));
            bundle.putString("clusterId", SharePrefs.getInstance(context).getString(SharePrefs.CLUSTER_ID));

            RetailerSDKApp.getInstance().mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_SEARCH_RESULTS, bundle);
        }
        if (EndPointPref.getInstance(context).getBoolean(EndPointPref.IS_MIXPANEL)) {
            try {
                JSONObject props = new JSONObject();
                props.put(FirebaseAnalytics.Param.ITEM_LIST_ID, list.get(0).getStoreId());
                props.put(FirebaseAnalytics.Param.ITEM_LIST_NAME, list.get(0).getStoreName());
                // user data
                props.put("SkCode", SharePrefs.getInstance(context).getString(SharePrefs.SK_CODE));
                props.put("city", SharePrefs.getInstance(context).getString(SharePrefs.CITY_NAME));
                props.put("warehouse", "" + SharePrefs.getInstance(context).getInt(SharePrefs.WAREHOUSE_ID));
                props.put("clusterId", SharePrefs.getInstance(context).getString(SharePrefs.CLUSTER_ID));
                // event
                mixpanel.track(FirebaseAnalytics.Event.VIEW_SEARCH_RESULTS, props);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public void updateAnalyticVIL(String listName, ArrayList<ItemListModel> list) {
        Parcelable[] parcelables = new Parcelable[list.size()];
        for (int i = 0; i < list.size(); i++) {
            Bundle itemJeggings = new Bundle();
            itemJeggings.putInt(FirebaseAnalytics.Param.ITEM_ID, list.get(i).getItemId());
            itemJeggings.putString(FirebaseAnalytics.Param.ITEM_NAME, list.get(i).itemname);
            itemJeggings.putInt(FirebaseAnalytics.Param.ITEM_CATEGORY, list.get(i).getCategoryid());
            itemJeggings.putInt(FirebaseAnalytics.Param.ITEM_BRAND, list.get(i).getSubsubCategoryid());
            itemJeggings.putDouble(FirebaseAnalytics.Param.PRICE, list.get(i).getUnitPrice());
            itemJeggings.putLong(FirebaseAnalytics.Param.QUANTITY, list.get(i).qty);
            itemJeggings.putInt(FirebaseAnalytics.Param.ITEM_VARIANT, list.get(i).getMinOrderQty());
            itemJeggings.putInt(FirebaseAnalytics.Param.LOCATION_ID, list.get(i).getWarehouseId());
            parcelables[i] = itemJeggings;
        }
        if (EndPointPref.getInstance(context).getBoolean(EndPointPref.IS_ANALYTIC)) {
            Bundle bundle = new Bundle();
            bundle.putInt(FirebaseAnalytics.Param.ITEM_LIST_ID, list.get(0).getStoreId());
            bundle.putString(FirebaseAnalytics.Param.ITEM_LIST_NAME, list.get(0).getStoreName() != null ? list.get(0).getStoreName() : "" + listName);
            bundle.putParcelableArray(FirebaseAnalytics.Param.ITEMS, parcelables);
            // user data
            bundle.putString("SkCode", SharePrefs.getInstance(context).getString(SharePrefs.SK_CODE));
            bundle.putString("city", SharePrefs.getInstance(context).getString(SharePrefs.CITY_NAME));
            bundle.putString("warehouse", "" + SharePrefs.getInstance(context).getInt(SharePrefs.WAREHOUSE_ID));
            bundle.putString("clusterId", SharePrefs.getInstance(context).getString(SharePrefs.CLUSTER_ID));

            RetailerSDKApp.getInstance().mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM_LIST, bundle);
        }
        if (EndPointPref.getInstance(context).getBoolean(EndPointPref.IS_MIXPANEL)) {

        }
    }

    public void updateAnalyticsVItem(ItemListModel model) {
        if (EndPointPref.getInstance(context).getBoolean(EndPointPref.IS_ANALYTIC)) {
            Bundle bundle = new Bundle();
            bundle.putInt(FirebaseAnalytics.Param.ITEM_ID, model.getItemId());
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, model.itemname == null ? model.getItemNumber() : model.itemname);
            bundle.putDouble(FirebaseAnalytics.Param.VALUE, model.getUnitPrice());
            bundle.putInt(FirebaseAnalytics.Param.ITEM_CATEGORY, model.getCategoryid());
            bundle.putInt(FirebaseAnalytics.Param.ITEM_BRAND, model.getSubsubCategoryid());
            bundle.putInt(FirebaseAnalytics.Param.ITEM_VARIANT, model.getMinOrderQty());
            bundle.putInt(FirebaseAnalytics.Param.LOCATION_ID, model.getWarehouseId());
            // user data
            bundle.putString("SkCode", SharePrefs.getInstance(context).getString(SharePrefs.SK_CODE));
            bundle.putString("city", SharePrefs.getInstance(context).getString(SharePrefs.CITY_NAME));
            bundle.putString("warehouse", "" + SharePrefs.getInstance(context).getInt(SharePrefs.WAREHOUSE_ID));
            bundle.putString("clusterId", SharePrefs.getInstance(context).getString(SharePrefs.CLUSTER_ID));

            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM, bundle);
        }
        if (EndPointPref.getInstance(context).getBoolean(EndPointPref.IS_MIXPANEL)) {
            try {
                JSONObject props = new JSONObject();
                props.put(FirebaseAnalytics.Param.ITEM_ID, model.getItemId());
                props.put(FirebaseAnalytics.Param.ITEM_NAME, model.itemname == null ? model.getItemNumber() : model.itemname);
                props.put(FirebaseAnalytics.Param.ITEM_CATEGORY, model.getCategoryid());
                props.put(FirebaseAnalytics.Param.VALUE, model.getUnitPrice());
                props.put(FirebaseAnalytics.Param.PRICE, model.price);
                props.put(FirebaseAnalytics.Param.ITEM_BRAND, model.getSubsubCategoryid());
                props.put(FirebaseAnalytics.Param.ITEM_VARIANT, model.getMinOrderQty());
                props.put("MOQ", model.getMinOrderQty());
                // user data
                props.put("SkCode", SharePrefs.getInstance(context).getString(SharePrefs.SK_CODE));
                props.put("city", SharePrefs.getInstance(context).getString(SharePrefs.CITY_NAME));
                props.put("warehouse", "" + SharePrefs.getInstance(context).getInt(SharePrefs.WAREHOUSE_ID));
                props.put("clusterId", SharePrefs.getInstance(context).getString(SharePrefs.CLUSTER_ID));
                // event
                mixpanel.track(FirebaseAnalytics.Event.VIEW_ITEM, props);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void analyticAddWishList(ItemListModel model) {
        if (EndPointPref.getInstance(context).getBoolean(EndPointPref.IS_ANALYTIC)) {
            Bundle bundle = new Bundle();
            bundle.putInt(FirebaseAnalytics.Param.ITEM_ID, model.getItemId());
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, model.itemname);
            bundle.putInt(FirebaseAnalytics.Param.ITEM_CATEGORY, model.getCategoryid());
            bundle.putDouble(FirebaseAnalytics.Param.PRICE, model.price);
            bundle.putInt(FirebaseAnalytics.Param.ITEM_BRAND, model.getSubsubCategoryid());
            bundle.putInt(FirebaseAnalytics.Param.LOCATION_ID, model.getWarehouseId());
            bundle.putInt(FirebaseAnalytics.Param.ITEM_VARIANT, model.getMinOrderQty());
            // user data
            bundle.putString("SkCode", SharePrefs.getInstance(context).getString(SharePrefs.SK_CODE));
            bundle.putString("city", SharePrefs.getInstance(context).getString(SharePrefs.CITY_NAME));
            bundle.putString("warehouse", "" + SharePrefs.getInstance(context).getInt(SharePrefs.WAREHOUSE_ID));
            bundle.putString("clusterId", SharePrefs.getInstance(context).getString(SharePrefs.CLUSTER_ID));

            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.ADD_TO_WISHLIST, bundle);
        }
        if (EndPointPref.getInstance(context).getBoolean(EndPointPref.IS_MIXPANEL)) {
            try {
                JSONObject props = new JSONObject();
                props.put(FirebaseAnalytics.Param.ITEM_ID, model.getItemId());
                props.put(FirebaseAnalytics.Param.ITEM_NAME, model.itemname == null ? model.getItemNumber() : model.itemname);
                props.put(FirebaseAnalytics.Param.ITEM_CATEGORY, model.getCategoryid());
                props.put(FirebaseAnalytics.Param.PRICE, model.price);
                props.put(FirebaseAnalytics.Param.ITEM_BRAND, model.getSubsubCategoryid());
                props.put(FirebaseAnalytics.Param.ITEM_VARIANT, model.getMinOrderQty());
                props.put("MOQ", model.getMinOrderQty());
                // user data
                props.put("SkCode", SharePrefs.getInstance(context).getString(SharePrefs.SK_CODE));
                props.put("city", SharePrefs.getInstance(context).getString(SharePrefs.CITY_NAME));
                props.put("warehouse", "" + SharePrefs.getInstance(context).getInt(SharePrefs.WAREHOUSE_ID));
                props.put("clusterId", SharePrefs.getInstance(context).getString(SharePrefs.CLUSTER_ID));
                // event
                mixpanel.track(FirebaseAnalytics.Event.ADD_TO_WISHLIST, props);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public void updateAnalyticsCart(String event, ItemListModel model) {
        if (EndPointPref.getInstance(context).getBoolean(EndPointPref.IS_ANALYTIC)) {
            Bundle bundle = new Bundle();
            bundle.putInt(FirebaseAnalytics.Param.ITEM_ID, model.getItemId());
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, model.itemname == null ? model.getItemNumber() : model.itemname);
            bundle.putInt(FirebaseAnalytics.Param.ITEM_CATEGORY, model.getCategoryid());
            bundle.putDouble(FirebaseAnalytics.Param.PRICE, model.price);
            bundle.putInt(FirebaseAnalytics.Param.ITEM_VARIANT, model.getMinOrderQty());
            bundle.putInt(FirebaseAnalytics.Param.ITEM_BRAND, model.getSubsubCategoryid());
            bundle.putInt("MOQ", model.getMinOrderQty());
            // user data
            bundle.putString("SkCode", SharePrefs.getInstance(context).getString(SharePrefs.SK_CODE));
            bundle.putString("city", SharePrefs.getInstance(context).getString(SharePrefs.CITY_NAME));
            bundle.putString("warehouse", "" + SharePrefs.getInstance(context).getInt(SharePrefs.WAREHOUSE_ID));
            bundle.putString("clusterId", SharePrefs.getInstance(context).getString(SharePrefs.CLUSTER_ID));
            // event
            mFirebaseAnalytics.logEvent(event, bundle);
        }
        if (EndPointPref.getInstance(context).getBoolean(EndPointPref.IS_MIXPANEL)) {
            try {
                JSONObject props = new JSONObject();
                props.put(FirebaseAnalytics.Param.ITEM_ID, model.getItemId());
                props.put(FirebaseAnalytics.Param.ITEM_NAME, model.itemname == null ? model.getItemNumber() : model.itemname);
                props.put(FirebaseAnalytics.Param.ITEM_CATEGORY, model.getCategoryid());
                props.put(FirebaseAnalytics.Param.PRICE, model.price);
                props.put(FirebaseAnalytics.Param.ITEM_VARIANT, model.getMinOrderQty());
                props.put(FirebaseAnalytics.Param.ITEM_BRAND, model.getSubsubCategoryid());
                props.put("MOQ", model.getMinOrderQty());
                // user data
                props.put("SkCode", SharePrefs.getInstance(context).getString(SharePrefs.SK_CODE));
                props.put("city", SharePrefs.getInstance(context).getString(SharePrefs.CITY_NAME));
                props.put("warehouse", "" + SharePrefs.getInstance(context).getInt(SharePrefs.WAREHOUSE_ID));
                props.put("clusterId", SharePrefs.getInstance(context).getString(SharePrefs.CLUSTER_ID));
                // event
                mixpanel.track(event, props);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void updateAnalyticRCart(String event, int itemId, String wId) {
        if (EndPointPref.getInstance(context).getBoolean(EndPointPref.IS_ANALYTIC)) {
            Bundle bundle = new Bundle();
            bundle.putInt(FirebaseAnalytics.Param.ITEM_ID, itemId);
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "");
            bundle.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, "");
            bundle.putString(FirebaseAnalytics.Param.PRICE, "0");
            bundle.putString(FirebaseAnalytics.Param.ITEM_BRAND, "");
            bundle.putString("warehouse", wId);
            // user data
            bundle.putString("SkCode", SharePrefs.getInstance(context).getString(SharePrefs.SK_CODE));
            bundle.putString("city", SharePrefs.getInstance(context).getString(SharePrefs.CITY_NAME));
            bundle.putString("warehouse", "" + SharePrefs.getInstance(context).getInt(SharePrefs.WAREHOUSE_ID));
            bundle.putString("clusterId", SharePrefs.getInstance(context).getString(SharePrefs.CLUSTER_ID));

            mFirebaseAnalytics.logEvent(event, bundle);
        }
        if (EndPointPref.getInstance(context).getBoolean(EndPointPref.IS_MIXPANEL)) {
            try {
                JSONObject props = new JSONObject();
                props.put(FirebaseAnalytics.Param.PROMOTION_ID, itemId);
                props.put("warehouse", wId);
                // user data
                props.put("SkCode", SharePrefs.getInstance(context).getString(SharePrefs.SK_CODE));
                props.put("city", SharePrefs.getInstance(context).getString(SharePrefs.CITY_NAME));
                props.put("warehouse", "" + SharePrefs.getInstance(context).getInt(SharePrefs.WAREHOUSE_ID));
                props.put("clusterId", SharePrefs.getInstance(context).getString(SharePrefs.CLUSTER_ID));
                // event
                mixpanel.track(event, props);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void updateAnalyticVC(double total, ArrayList<ItemListModel> list) {
        if (EndPointPref.getInstance(context).getBoolean(EndPointPref.IS_ANALYTIC)) {
            Parcelable[] cartItems = new Parcelable[list.size()];
            for (int i = 0; i < list.size(); i++) {
                Bundle itemJeggings = new Bundle();
                itemJeggings.putInt(FirebaseAnalytics.Param.ITEM_ID, list.get(i).getItemId());
                itemJeggings.putString(FirebaseAnalytics.Param.ITEM_NAME, list.get(i).itemname);
                itemJeggings.putInt(FirebaseAnalytics.Param.ITEM_CATEGORY, list.get(i).getCategoryid());
                itemJeggings.putInt(FirebaseAnalytics.Param.ITEM_VARIANT, list.get(i).getMinOrderQty());
                itemJeggings.putInt(FirebaseAnalytics.Param.ITEM_BRAND, list.get(i).getSubsubCategoryid());
                itemJeggings.putDouble(FirebaseAnalytics.Param.PRICE, list.get(i).getUnitPrice());
                itemJeggings.putLong(FirebaseAnalytics.Param.QUANTITY, list.get(i).qty);
                cartItems[i] = itemJeggings;
            }

            Bundle viewCartParams = new Bundle();
            viewCartParams.putString(FirebaseAnalytics.Param.CURRENCY, "INR");
            viewCartParams.putDouble(FirebaseAnalytics.Param.VALUE, total);
            viewCartParams.putParcelableArray(FirebaseAnalytics.Param.ITEMS, cartItems);
            // user data
            viewCartParams.putString("SkCode", SharePrefs.getInstance(context).getString(SharePrefs.SK_CODE));
            viewCartParams.putString("city", SharePrefs.getInstance(context).getString(SharePrefs.CITY_NAME));
            viewCartParams.putString("warehouse", "" + SharePrefs.getInstance(context).getInt(SharePrefs.WAREHOUSE_ID));
            viewCartParams.putString("clusterId", SharePrefs.getInstance(context).getString(SharePrefs.CLUSTER_ID));

            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_CART, viewCartParams);
        }
        if (EndPointPref.getInstance(context).getBoolean(EndPointPref.IS_MIXPANEL)) {
            try {
                JSONObject props = new JSONObject();
                props.put(FirebaseAnalytics.Param.VALUE, total);
                props.put(FirebaseAnalytics.Param.ITEMS, new Gson().toJson(list));
                // user data
                props.put("SkCode", SharePrefs.getInstance(context).getString(SharePrefs.SK_CODE));
                props.put("city", SharePrefs.getInstance(context).getString(SharePrefs.CITY_NAME));
                props.put("warehouse", "" + SharePrefs.getInstance(context).getInt(SharePrefs.WAREHOUSE_ID));
                props.put("clusterId", SharePrefs.getInstance(context).getString(SharePrefs.CLUSTER_ID));
                // event
                mixpanel.track(FirebaseAnalytics.Event.VIEW_CART, props);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public void updateAnalyticPromotion(BillDiscountModel model) {
        if (EndPointPref.getInstance(context).getBoolean(EndPointPref.IS_ANALYTIC)) {
            Bundle promoParams = new Bundle();
            promoParams.putString(FirebaseAnalytics.Param.PROMOTION_ID, "" + model.getOfferId());
            promoParams.putString(FirebaseAnalytics.Param.PROMOTION_NAME, model.getOfferName());
            promoParams.putString(FirebaseAnalytics.Param.CREATIVE_NAME, model.getOfferCode());
            promoParams.putString(FirebaseAnalytics.Param.CREATIVE_SLOT, model.getApplyOn());
            promoParams.putString(FirebaseAnalytics.Param.LOCATION_ID, "" + SharePrefs.getInstance(context).getInt(SharePrefs.WAREHOUSE_ID));
            // user data
            promoParams.putString("SkCode", SharePrefs.getInstance(context).getString(SharePrefs.SK_CODE));
            promoParams.putString("city", SharePrefs.getInstance(context).getString(SharePrefs.CITY_NAME));
            promoParams.putString("warehouse", "" + SharePrefs.getInstance(context).getInt(SharePrefs.WAREHOUSE_ID));
            promoParams.putString("clusterId", SharePrefs.getInstance(context).getString(SharePrefs.CLUSTER_ID));
            // Promotion displayed
            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_PROMOTION, promoParams);
            // Promotion selected
            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_PROMOTION, promoParams);
        }
        if (EndPointPref.getInstance(context).getBoolean(EndPointPref.IS_MIXPANEL)) {
            try {
                JSONObject props = new JSONObject();
                props.put(FirebaseAnalytics.Param.PROMOTION_ID, "" + model.getOfferId());
                props.put(FirebaseAnalytics.Param.PROMOTION_NAME, model.getOfferName());
                props.put(FirebaseAnalytics.Param.CREATIVE_NAME, model.getOfferCode());
                props.put(FirebaseAnalytics.Param.CREATIVE_SLOT, model.getApplyOn());
                props.put(FirebaseAnalytics.Param.LOCATION_ID, "" + SharePrefs.getInstance(context).getInt(SharePrefs.WAREHOUSE_ID));
                // user data
                props.put("SkCode", SharePrefs.getInstance(context).getString(SharePrefs.SK_CODE));
                props.put("city", SharePrefs.getInstance(context).getString(SharePrefs.CITY_NAME));
                props.put("warehouse", "" + SharePrefs.getInstance(context).getInt(SharePrefs.WAREHOUSE_ID));
                props.put("clusterId", SharePrefs.getInstance(context).getString(SharePrefs.CLUSTER_ID));
                // event
                mixpanel.track(FirebaseAnalytics.Event.VIEW_PROMOTION, props);
                mixpanel.track(FirebaseAnalytics.Event.SELECT_PROMOTION, props);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void updateAnalyticBC(double total, String coupon, ArrayList<ItemListModel> list) {
        Parcelable[] cartItems = new Parcelable[list.size()];
        for (int i = 0; i < list.size(); i++) {
            Bundle itemJeggings = new Bundle();
            itemJeggings.putInt(FirebaseAnalytics.Param.ITEM_ID, list.get(i).getItemId());
            itemJeggings.putString(FirebaseAnalytics.Param.ITEM_NAME, list.get(i).itemname);
            itemJeggings.putInt(FirebaseAnalytics.Param.ITEM_CATEGORY, list.get(i).getCategoryid());
            itemJeggings.putInt(FirebaseAnalytics.Param.ITEM_VARIANT, list.get(i).getMinOrderQty());
            itemJeggings.putInt(FirebaseAnalytics.Param.ITEM_BRAND, list.get(i).getSubsubCategoryid());
            itemJeggings.putDouble(FirebaseAnalytics.Param.PRICE, list.get(i).getUnitPrice());
            itemJeggings.putLong(FirebaseAnalytics.Param.QUANTITY, list.get(i).qty);
            cartItems[i] = itemJeggings;
        }
        if (EndPointPref.getInstance(context).getBoolean(EndPointPref.IS_ANALYTIC)) {
            Bundle beginCheckoutParams = new Bundle();
            beginCheckoutParams.putString(FirebaseAnalytics.Param.CURRENCY, "INR");
            beginCheckoutParams.putDouble(FirebaseAnalytics.Param.VALUE, total);
            beginCheckoutParams.putString(FirebaseAnalytics.Param.COUPON, coupon);
            beginCheckoutParams.putParcelableArray(FirebaseAnalytics.Param.ITEMS, cartItems);
            // user data
            beginCheckoutParams.putString("SkCode", SharePrefs.getInstance(context).getString(SharePrefs.SK_CODE));
            beginCheckoutParams.putString("city", SharePrefs.getInstance(context).getString(SharePrefs.CITY_NAME));
            beginCheckoutParams.putString("warehouse", "" + SharePrefs.getInstance(context).getInt(SharePrefs.WAREHOUSE_ID));
            beginCheckoutParams.putString("clusterId", SharePrefs.getInstance(context).getString(SharePrefs.CLUSTER_ID));

            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.BEGIN_CHECKOUT, beginCheckoutParams);
        }
        if (EndPointPref.getInstance(context).getBoolean(EndPointPref.IS_MIXPANEL)) {
            try {
                JSONObject props = new JSONObject();
                props.put(FirebaseAnalytics.Param.VALUE, total);
                props.put(FirebaseAnalytics.Param.COUPON, coupon);
                props.put(FirebaseAnalytics.Param.ITEMS, new Gson().toJson(list));
                // user data
                props.put("SkCode", SharePrefs.getInstance(context).getString(SharePrefs.SK_CODE));
                props.put("city", SharePrefs.getInstance(context).getString(SharePrefs.CITY_NAME));
                props.put("warehouse", "" + SharePrefs.getInstance(context).getInt(SharePrefs.WAREHOUSE_ID));
                props.put("clusterId", SharePrefs.getInstance(context).getString(SharePrefs.CLUSTER_ID));
                // event
                mixpanel.track(FirebaseAnalytics.Event.BEGIN_CHECKOUT, props);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void updateAnalyticPurchase(String tId, double total, double shipping, String coupon, String orderId, int wheel, String pay_by, ArrayList<ItemListModel> list) {
        Parcelable[] cartItems = new Parcelable[list.size()];
        if (list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                Bundle itemJeggings = new Bundle();
                itemJeggings.putInt(FirebaseAnalytics.Param.ITEM_ID, list.get(i).getItemId());
                itemJeggings.putString(FirebaseAnalytics.Param.ITEM_NAME, list.get(i).itemname);
                itemJeggings.putInt(FirebaseAnalytics.Param.ITEM_CATEGORY, list.get(i).getCategoryid());
                itemJeggings.putInt(FirebaseAnalytics.Param.ITEM_VARIANT, list.get(i).getMinOrderQty());
                itemJeggings.putInt(FirebaseAnalytics.Param.ITEM_BRAND, list.get(i).getSubsubCategoryid());
                itemJeggings.putDouble(FirebaseAnalytics.Param.PRICE, list.get(i).getUnitPrice());
                itemJeggings.putLong(FirebaseAnalytics.Param.QUANTITY, list.get(i).qty);
                cartItems[i] = itemJeggings;
            }
        }
        if (EndPointPref.getInstance(context).getBoolean(EndPointPref.IS_ANALYTIC)) {
            Bundle purchaseParams = new Bundle();
            purchaseParams.putString(FirebaseAnalytics.Param.TRANSACTION_ID, tId);
            purchaseParams.putString(FirebaseAnalytics.Param.AFFILIATION, "Retailer");
            purchaseParams.putString(FirebaseAnalytics.Param.CURRENCY, "INR");
            purchaseParams.putDouble(FirebaseAnalytics.Param.VALUE, total);
            purchaseParams.putDouble(FirebaseAnalytics.Param.SHIPPING, shipping);
            purchaseParams.putString(FirebaseAnalytics.Param.COUPON, coupon);
            purchaseParams.putString("order_id", "" + orderId);
            purchaseParams.putString("pay_by", "" + pay_by);
            purchaseParams.putInt("wheel_count", wheel);
            purchaseParams.putInt("wheel_count", wheel);
            purchaseParams.putParcelableArray(FirebaseAnalytics.Param.ITEMS, cartItems);
            // user data
            purchaseParams.putString("SkCode", SharePrefs.getInstance(context).getString(SharePrefs.SK_CODE));
            purchaseParams.putString("city", SharePrefs.getInstance(context).getString(SharePrefs.CITY_NAME));
            purchaseParams.putString("warehouse", "" + SharePrefs.getInstance(context).getInt(SharePrefs.WAREHOUSE_ID));
            purchaseParams.putString("clusterId", SharePrefs.getInstance(context).getString(SharePrefs.CLUSTER_ID));

            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.PURCHASE, purchaseParams);
        }
        if (EndPointPref.getInstance(context).getBoolean(EndPointPref.IS_MIXPANEL)) {
            try {
                JSONObject props = new JSONObject();
                props.put(FirebaseAnalytics.Param.TRANSACTION_ID, tId);
                props.put(FirebaseAnalytics.Param.AFFILIATION, "Retailer");
                props.put(FirebaseAnalytics.Param.CURRENCY, "INR");
                props.put(FirebaseAnalytics.Param.VALUE, total);
                props.put(FirebaseAnalytics.Param.SHIPPING, shipping);
                props.put(FirebaseAnalytics.Param.COUPON, coupon);
                props.put("order_id", "" + orderId);
                props.put("pay_by", "" + pay_by);
                props.put("wheel_count", wheel);
                props.put(FirebaseAnalytics.Param.ITEMS, new Gson().toJson(list));
                // user data
                props.put("SkCode", SharePrefs.getInstance(context).getString(SharePrefs.SK_CODE));
                props.put("city", SharePrefs.getInstance(context).getString(SharePrefs.CITY_NAME));
                props.put("warehouse", "" + SharePrefs.getInstance(context).getInt(SharePrefs.WAREHOUSE_ID));
                props.put("clusterId", SharePrefs.getInstance(context).getString(SharePrefs.CLUSTER_ID));

                mixpanel.track(FirebaseAnalytics.Event.PURCHASE, props);
                mixpanel.getPeople().trackCharge(total, props);
                mixpanel.getPeople().increment("totalOrder", 1);
                mixpanel.getPeople().increment("orderAmount", total);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void updateAnalyticWheel(String eventName, int wheelCount, int wheelPlay, int point) {
        if (EndPointPref.getInstance(context).getBoolean(EndPointPref.IS_ANALYTIC)) {
            Bundle bundle = new Bundle();
            bundle.putString("customer_name", SharePrefs.getInstance(context).getString(SharePrefs.CUSTOMER_NAME));
            bundle.putString("SkCode", SharePrefs.getInstance(context).getString(SharePrefs.SK_CODE));
            bundle.putDouble(FirebaseAnalytics.Param.VALUE, point);
            bundle.putInt("wheel_count", wheelCount);
            bundle.putInt("wheel_play", wheelPlay);

            mFirebaseAnalytics.logEvent(eventName, bundle);
        }
        if (EndPointPref.getInstance(context).getBoolean(EndPointPref.IS_MIXPANEL)) {
            try {
                JSONObject props = new JSONObject();
                props.put("SkCode", SharePrefs.getInstance(context).getString(SharePrefs.SK_CODE));
                props.put("city", SharePrefs.getInstance(context).getString(SharePrefs.CITY_NAME));
                props.put("warehouse", "" + SharePrefs.getInstance(context).getInt(SharePrefs.WAREHOUSE_ID));
                props.put("clusterId", SharePrefs.getInstance(context).getString(SharePrefs.CLUSTER_ID));
                props.put(FirebaseAnalytics.Param.VALUE, point);
                props.put("wheel_count", wheelCount);
                props.put("wheel_play", wheelPlay);
                mixpanel.track(eventName, props);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public void updateAnalyticGame(String eventName, int score) {
        eventName = eventName.replace(" ", "_");
        if (EndPointPref.getInstance(context).getBoolean(EndPointPref.IS_ANALYTIC)) {
            Bundle bundle = new Bundle();
            bundle.putString("SkCode", SharePrefs.getInstance(context).getString(SharePrefs.SK_CODE));
            bundle.putInt("score", score);
            mFirebaseAnalytics.logEvent(eventName, bundle);
        }
        if (EndPointPref.getInstance(context).getBoolean(EndPointPref.IS_MIXPANEL)) {
            try {
                JSONObject props = new JSONObject();
                props.put("SkCode", SharePrefs.getInstance(context).getString(SharePrefs.SK_CODE));
                props.put("city", SharePrefs.getInstance(context).getString(SharePrefs.CITY_NAME));
                props.put("warehouse", "" + SharePrefs.getInstance(context).getInt(SharePrefs.WAREHOUSE_ID));
                props.put("clusterId", SharePrefs.getInstance(context).getString(SharePrefs.CLUSTER_ID));
                props.put("score", score);
                mixpanel.track(eventName, props);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void updateLogOut() {
        Bundle bundle = new Bundle();
        bundle.putString("SkCode", SharePrefs.getInstance(context).getString(SharePrefs.SK_CODE));
        bundle.putString("city", SharePrefs.getInstance(context).getString(SharePrefs.CITY_NAME));
        bundle.putInt("warehouse", SharePrefs.getInstance(context).getInt(SharePrefs.WAREHOUSE_ID));
        bundle.putString("clusterId", SharePrefs.getInstance(context).getString(SharePrefs.CLUSTER_ID));

        mFirebaseAnalytics.logEvent("LogOut", bundle);
        try {
            JSONObject props = new JSONObject();
            props.put("SkCode", SharePrefs.getInstance(context).getString(SharePrefs.SK_CODE));
            props.put("city", SharePrefs.getInstance(context).getString(SharePrefs.CITY_NAME));
            props.put("warehouse", "" + SharePrefs.getInstance(context).getInt(SharePrefs.WAREHOUSE_ID));
            props.put("clusterId", SharePrefs.getInstance(context).getString(SharePrefs.CLUSTER_ID));

            mixpanel.track("LogOut", props);
            mixpanel.reset();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void logScreenAnalytics(Class aClass) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, aClass.getSimpleName());
        bundle.putString(FirebaseAnalytics.Param.SCREEN_CLASS, aClass.getSimpleName());

        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle);
    }

    // Community event
    public void updateAnalytics(@NotNull AnalyticPost analyticPost) {
        if (EndPointPref.getInstance(context).getBoolean(EndPointPref.IS_ANALYTIC)) {
            Bundle bundle = new Bundle();
            bundle.putInt("sectionId", analyticPost.sectionId);
            bundle.putString("sectionSubType", analyticPost.sectionSubType);
            bundle.putString("sectionName", analyticPost.sectionName);
            bundle.putString("url", analyticPost.url);
            bundle.putString("baseCatId", analyticPost.baseCatId);
            bundle.putInt("categoryId", analyticPost.categoryId);
            bundle.putInt("subCatId", analyticPost.subCatId);
            bundle.putInt("subSubCatId", analyticPost.subSubCatId);
            bundle.putString("categoryName", analyticPost.categoryName);
            // user data
            bundle.putString("SkCode", SharePrefs.getInstance(context).getString(SharePrefs.SK_CODE));
            bundle.putString("shop_name", SharePrefs.getInstance(context).getString(SharePrefs.SHOP_NAME));
            bundle.putString("city", SharePrefs.getInstance(context).getString(SharePrefs.CITY_NAME));
            bundle.putInt("warehouse", SharePrefs.getInstance(context).getInt(SharePrefs.WAREHOUSE_ID));
            bundle.putString("clusterId", SharePrefs.getInstance(context).getString(SharePrefs.CLUSTER_ID));
            // event
            mFirebaseAnalytics.logEvent(analyticPost.eventName, bundle);
        }
        if (EndPointPref.getInstance(context).getBoolean(EndPointPref.IS_MIXPANEL)) {
            try {
                JSONObject props = new JSONObject();
                props.put("sectionId", analyticPost.sectionId);
                props.put("sectionSubType", analyticPost.sectionSubType);
                props.put("sectionName", analyticPost.sectionName);
                props.put("url", analyticPost.url);
                props.put("baseCatId", analyticPost.baseCatId);
                props.put("categoryId", analyticPost.categoryId);
                props.put("subCatId", analyticPost.subCatId);
                props.put("subSubCatId", analyticPost.subSubCatId);
                props.put("categoryName", analyticPost.categoryName);
                props.put("source", analyticPost.source);
                props.put("Section", analyticPost.section);
                props.put("postTypeClick", analyticPost.postTypeClick);
                props.put("postId", analyticPost.postId);
                props.put("postType", analyticPost.postType);
                props.put("commentCount", analyticPost.commentCount);
                props.put("likeCount", analyticPost.likeCount);
                props.put("postTypeClick", analyticPost.postTypeClick);

                props.put("SkCode", SharePrefs.getInstance(context).getString(SharePrefs.SK_CODE));
                props.put("clusterId", SharePrefs.getInstance(context).getString(SharePrefs.CLUSTER_ID));
                props.put("city", SharePrefs.getInstance(context).getString(SharePrefs.CITY_NAME));
                props.put("warehouse", "" + SharePrefs.getInstance(context).getInt(SharePrefs.WAREHOUSE_ID));
                mixpanel.track(analyticPost.eventName, props);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void token() {
        CommonClassForAPI.getInstance(activity).getToken(callTokenDes, "password",
                SharePrefs.getInstance(context).getString(SharePrefs.TOKEN_NAME),
                SharePrefs.getInstance(context).getString(SharePrefs.TOKEN_PASSWORD));

    }

    public void notificationView(int notificationId) {
        CommonClassForAPI.getInstance(activity).getNotificationView(null,
                SharePrefs.getInstance(context).getInt(SharePrefs.CUSTOMER_ID), notificationId);
    }

    public void checkLastLogin() {
        try {
            if (TextUtils.isNullOrEmpty(SharePrefs.getInstance(context).getString(SharePrefs.LAST_LOGIN_DATE)))
                SharePrefs.getInstance(context).putString(SharePrefs.LAST_LOGIN_DATE, new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
            //
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
            Date lastLogin = simpleDateFormat.parse(SharePrefs.getInstance(context).getString(SharePrefs.LAST_LOGIN_DATE));
            String dateString = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
            Date currentDate = new SimpleDateFormat("dd/MM/yyyy").parse(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
            System.out.println(lastLogin + " fuk " + currentDate);

            long difference = currentDate.getTime() - lastLogin.getTime();
            long differenceDates = difference / (24 * 60 * 60 * 1000);
            if (differenceDates > EndPointPref.getInstance(context).getLong(EndPointPref.logOutDays)) {
                clearLocalData();
                clearCartData();
                prefManager.setLoggedIn(false);
                context.startActivity(new Intent(context, MobileSignUpActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void logout(Activity activity) {
        clearLocalData();
        clearCartData();
        noteRepository.truncateCart();
        noteRepository.truncateSearch();
        noteRepository.deleteNotifyTask();
        prefManager.setLoggedIn(false);
        SharePrefs.getInstance(activity).clear();
        updateLogOut();
        // clear app data
        activity.getCacheDir().delete();
        Intent intent = new Intent(activity, MobileSignUpActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
//        LocaleHelper.setLocale(activity, "hi");
        Utils.fadeTransaction(activity);
    }


    // clear local data
    public void clearLangData() {
        try {
            SharePrefs.getInstance(context).putString(SharePrefs.CATEGORY_BY_ALL, "");
            SharePrefs.getInstance(context).putString(SharePrefs.ALL_CATEGORY_SERACH, "");
            SharePrefs.getInstance(context).putString(SharePrefs.SEARCH_HINT_DATA, "");
            SectionPref.getInstance(context).clear();
            StorePref.getInstance(context).clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clearLocalData() {
        try {
            SharePrefs.getInstance(context).putString(SharePrefs.CATEGORY_BY_ALL, "");
            SharePrefs.getInstance(context).putString(SharePrefs.ALL_CATEGORY_SERACH, "");
            SharePrefs.getInstance(context).putString(SharePrefs.SEARCH_HINT_DATA, "");
            SharePrefs.getInstance(context).putString(SharePrefs.MY_UDHAR_GET_DATA, "");
            SharePrefs.getInstance(context).putString(SharePrefs.DILIVERY_CHARGE, "");
            SharePrefs.getInstance(context).putBoolean(SharePrefs.IS_GULLAK_BAL, false);
            SharePrefs.getInstance(context).putBoolean(SharePrefs.MURLI_API_CALL, true);
            SectionPref.getInstance(context).clear();
            StorePref.getInstance(context).clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clearCartData() {
        noteRepository.truncateCart();
    }

    // getting token response
    private final DisposableObserver<TokenResponse> callTokenDes = new DisposableObserver<TokenResponse>() {
        @Override
        public void onNext(@NotNull TokenResponse response) {
            try {
                if (response != null) {
                    SharePrefs.getInstance(context).putString(SharePrefs.TOKEN, "");
                    SharePrefs.getInstance(context).putString(SharePrefs.TOKEN, response.access_token);
                    SharePrefs.getInstance(context).putString(SharePrefs.TOKEN_DATE, new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH).format(new Date()));
                    if (activity != null)
                        activity.recreate();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
        }

        @Override
        public void onComplete() {
        }
    };


//    public class MyClickListener implements FirebaseInAppMessagingClickListener {
//
//        @Override
//        public void messageClicked(InAppMessage inAppMessage, Action action) {
//            // Determine which URL the user clicked
//            String url = action.getActionUrl();
//            // Get general information about the campaign
//            CampaignMetadata metadata = inAppMessage.getCampaignMetadata();
//            //
//            System.out.println(url + " " + metadata);
//            if (url != null) {
//                if (url.contains("trade")) {
//                    activity.startActivity(new Intent(activity, TradeActivity.class));
//                    Utils.leftTransaction(activity);
//                } else if (url.contains("skDirect")) {
//                    activity.startActivity(new Intent(activity, TradeActivity.class));
//                    Utils.leftTransaction(activity);
//                } else {
//                    Bundle bundle = new Bundle();
//                    bundle.putString("url", url);
//                    activity.startActivity(new Intent(activity, WebViewActivity.class).putExtras(bundle));
//                }
//            }
//        }
//    }
}