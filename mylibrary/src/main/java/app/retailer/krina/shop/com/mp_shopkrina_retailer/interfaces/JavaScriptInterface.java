package app.retailer.krina.shop.com.mp_shopkrina_retailer.interfaces;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import org.jetbrains.annotations.NotNull;

import app.retailer.krina.shop.com.mp_shopkrina_retailer.R;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.api.CommonClassForAPI;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.target.CustomerSubCategoryTargetActivity;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.freebies.FreebiesOfferActivity;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.activity.GamesListActivity;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.activity.MembershipPlanActivity;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.order.MyOrderActivity;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.activity.MyWalletActivity;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.productDetails.ProductDetailsActivity;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.shoppingCart.ShoppingCartActivity;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.activity.WebViewActivity;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.direct.TradeActivity;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.allBrands.AllBrandFragItemList;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.allBrands.BrandOrderFragment;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.category.HomeCategoryFragment;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.fragment.NewSubCategoryWebFragment;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.subCategory.SubSubCategoryFragment;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.fragment.TradeOfferFragment;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.store.StoreHomeFragment;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.ItemListModel;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.SharePrefs;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.LocaleHelper;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.MyApplication;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils;
import io.reactivex.observers.DisposableObserver;

public class JavaScriptInterface {
    private final AppCompatActivity activity;

    public JavaScriptInterface(AppCompatActivity activity) {
        this.activity = activity;
    }


    @JavascriptInterface
    public void showToast(String toast) {
        Toast.makeText(activity, "" + toast, Toast.LENGTH_SHORT).show();
    }

    @JavascriptInterface
    public void openActivity(String type) {
        callActivities(type);
    }

    @JavascriptInterface
    public void openCategory(String baseCategoryId, int categoryId) {
        callCategory(baseCategoryId, categoryId);
    }

    @JavascriptInterface
    public void openBrand(int subCategoryName, int categoryId) {
        callBrand(subCategoryName, categoryId);
    }

    @JavascriptInterface
    public void openProduct(int itemId) {
        callProductDetailApi(itemId);
    }

    @JavascriptInterface
    public void openSubSubCategory(String itemId, int baseCategoryId, int categoryId, int subCategoryId, int subSubCategoryId) {
        openSubSubCategoryMethod(itemId, baseCategoryId, categoryId, subCategoryId, subSubCategoryId);
    }

    @JavascriptInterface
    public void openNewSubCategory(int categoryId, int subCategoryId, int subSubCategoryId, String tileImage) {
        openNewSubCategoryMethod(categoryId, subCategoryId, subSubCategoryId, tileImage);
    }

    @JavascriptInterface
    public void openNewSubSubCategory(int subCategoryId) {
        openNewSubSubCategoryMethod(subCategoryId);
    }

    @JavascriptInterface
    public void openApp(String AppName, String PackageName) {
        Open(PackageName);
    }

    @JavascriptInterface
    public void shareText(String text) {
        ShareText(text);
    }

    @JavascriptInterface
    public void openLink(String url) {
        url = url.replace("[CUSTOMERID]", "" + SharePrefs.getInstance(activity).getInt(SharePrefs.CUSTOMER_ID));
        url = url.replace("[SKCODE]", "" + SharePrefs.getInstance(activity).getString(SharePrefs.SK_CODE));
        url = url.replace("[WAREHOUSEID]", "" + SharePrefs.getInstance(activity).getInt(SharePrefs.WAREHOUSE_ID));
        url = url.replace("[LANG]", "" + LocaleHelper.getLanguage(activity));
        url = url.replace("[MOBILE]", "" + SharePrefs.getInstance(activity).getString(SharePrefs.MOBILE_NUMBER));
        activity.startActivity(new Intent(activity, WebViewActivity.class).putExtra("url", url));
    }

    @JavascriptInterface
    public void openTrade(String url) {
        url = url.replace("[CUSTOMERID]", "" + SharePrefs.getInstance(activity).getInt(SharePrefs.CUSTOMER_ID));
        url = url.replace("[SKCODE]", "" + SharePrefs.getInstance(activity).getString(SharePrefs.SK_CODE));
        url = url.replace("[WAREHOUSEID]", "" + SharePrefs.getInstance(activity).getInt(SharePrefs.WAREHOUSE_ID));
        url = url.replace("[LANG]", "" + LocaleHelper.getLanguage(activity));
        url = url.replace("[MOBILE]", "" + SharePrefs.getInstance(activity).getString(SharePrefs.MOBILE_NUMBER));
        activity.startActivity(new Intent(activity, TradeActivity.class)
                .putExtra("screen", 2)
                .putExtra("url", url));
    }

    @JavascriptInterface
    public void closeScreen() {
        activity.finish();
    }


    private void callActivities(String type) {
        Intent i;
        switch (type) {
            case "games":
                i = new Intent(activity, GamesListActivity.class);
                activity.startActivity(i);
                Utils.fadeTransaction(activity);
                break;
            case "target":
                i = new Intent(activity, CustomerSubCategoryTargetActivity.class);
                activity.startActivity(i);
                Utils.fadeTransaction(activity);
                break;
            case "prime":
                i = new Intent(activity, MembershipPlanActivity.class);
                activity.startActivity(i);
                Utils.fadeTransaction(activity);
                break;
            case "shoppingcart":
                i = new Intent(activity, ShoppingCartActivity.class);
                activity.startActivity(i);
                Utils.fadeTransaction(activity);
                break;
            case "wallet":
                i = new Intent(activity, MyWalletActivity.class);
                activity.startActivity(i);
                Utils.fadeTransaction(activity);
                break;
            case "category":
                activity.runOnUiThread(() -> pushFragments(HomeCategoryFragment.newInstance(), false, true, null));
                break;
            case "tradeoffer":
                activity.runOnUiThread(() -> pushFragments(TradeOfferFragment.newInstance(), false, true, null));
                break;
            case "allbrands":
                activity.runOnUiThread(() -> pushFragments(AllBrandFragItemList.newInstance(), false, true, null));
                break;
            case "freebies":
                i = new Intent(activity, FreebiesOfferActivity.class);
                activity.startActivity(i);
                Utils.fadeTransaction(activity);
                break;
            case "myorder":
                i = new Intent(activity, MyOrderActivity.class);
                activity.startActivity(i);
                Utils.fadeTransaction(activity);
                break;
            case "direct":
                i = new Intent(activity, TradeActivity.class);
                activity.startActivity(i);
                Utils.fadeTransaction(activity);
                break;
        }
    }

    private void callCategory(String baseCategoryId, int categoryId) {
        Bundle args = new Bundle();
        args.putString("BaseCategoryId", baseCategoryId);
        args.putInt("CATEGORY_ID", categoryId);
        args.putString("SectionType", "BottomCategory");
        activity.runOnUiThread(() -> new Handler().postDelayed(() -> {
            FragmentManager fragmentManager = activity.getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            Fragment fragment = SubSubCategoryFragment.newInstance();
            fragment.setArguments(args);
            fragmentTransaction.replace(R.id.content, fragment).addToBackStack(SubSubCategoryFragment.class.getName());
            fragmentTransaction.commit();
        }, 500));
    }

    private void callBrand(int subCatId, int categoryId) {
        Bundle args = new Bundle();
        args.putInt("subCatId", subCatId);
        args.putInt("Categoryid", categoryId);
        activity.runOnUiThread(() -> {
            pushFragments(BrandOrderFragment.newInstance(), false, true, args);
        });
    }

    private void openSubSubCategoryMethod(String itemId, int baseCategoryId, int categoryId, int subCategoryId, int subSubCategoryId) {
        activity.runOnUiThread(() -> {
            Bundle args = new Bundle();
            args.putString("ItemId", itemId);
            args.putInt("BaseCategoryId", baseCategoryId);
            args.putInt("CATEGORY_ID", categoryId);
            args.putInt("SUB_CAT_ID", subCategoryId);
            args.putInt("SUB_SUB_CAT_ID", subSubCategoryId);
            args.putBoolean("HOME_FLAG", true);
            args.putString("SectionType", "BottomCategory");
            pushFragments(SubSubCategoryFragment.newInstance(), true, true, args);
        });
    }

    private void openNewSubCategoryMethod(int categoryId, int subCategoryId, int subSubCategoryId, String tileImage) {
        activity.runOnUiThread(() -> {
            Bundle args = new Bundle();
            args.putInt("CATEGORY_ID", categoryId);
            args.putInt("SUB_CAT_ID", subCategoryId);
            args.putInt("SUB_SUB_CAT_ID", subSubCategoryId);
            args.putBoolean("HOME_FLAG", true);
            args.putString("ITEM_IMAGE", tileImage);
            args.putString("SectionType", "BottomCategory");
            pushFragments(StoreHomeFragment.newInstance(), true, true, args);
        });
    }

    private void openNewSubSubCategoryMethod(int subCategoryId) {
        activity.runOnUiThread(() -> {
            Bundle args = new Bundle();
            args.putInt("SUB_CAT_ID", subCategoryId);
            pushFragments(NewSubCategoryWebFragment.newInstance(), true, true, args);
        });
    }

    private void Open(String PackageName) {
        Intent intent = activity.getPackageManager().getLaunchIntentForPackage(PackageName);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (intent == null) {
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=" + PackageName));
        }
        activity.startActivity(intent);
    }

    public void ShareText(String text) {
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.putExtra(Intent.EXTRA_TEXT, text);
        activity.startActivity(Intent.createChooser(share, "Share"));
    }

    private void callProductDetailApi(int itemId) {
        Utils.showProgressDialog(activity);
        CommonClassForAPI.getInstance(MyApplication.getInstance().activity).fetchProductDetails(productDetailObserver, itemId,
                SharePrefs.getInstance(activity).getInt(SharePrefs.CUSTOMER_ID),
                SharePrefs.getInstance(activity).getInt(SharePrefs.WAREHOUSE_ID),
                LocaleHelper.getLanguage(activity));
    }


    // manage back stake
    public void pushFragments(Fragment fragment, boolean shouldAnimate, boolean shouldAdd, Bundle args) {
        try {
            if (fragment != null) {
                FragmentManager manager = activity.getSupportFragmentManager();
                boolean fragmentPopped = manager.popBackStackImmediate(fragment.getClass().getSimpleName(), 0);
                if (!fragmentPopped) { //fragment not in back stack, create it.
                    FragmentTransaction ft = manager.beginTransaction();
                    ft.setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out);
                    if (args != null) {
                        fragment.setArguments(args);
                    }
                    if (shouldAdd)
                        ft.addToBackStack(fragment.getClass().getSimpleName());
                    if (shouldAnimate) {
                        ft.setCustomAnimations(R.anim.fade_in, R.anim.fade_out, R.anim.fade_in, R.anim.fade_out);
                    }
                    ft.replace(R.id.content, fragment);
                    ft.commit();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private final DisposableObserver<ItemListModel> productDetailObserver = new DisposableObserver<ItemListModel>() {
        @Override
        public void onNext(@NotNull ItemListModel response) {
            Utils.hideProgressDialog();
            if (response != null) {
                Bundle args = new Bundle();
                Intent intent = new Intent(activity, ProductDetailsActivity.class);
                intent.putExtra("PRODUCT_IMAGE", response.getLogoUrl());
                intent.putExtra("PRODUCT_NAME", response.itemname);
                intent.putExtra("PRODUCT_PRICE", response.getUnitPrice());
                intent.putExtra("PRODUCT_MOQ", response.getMinOrderQty());
                intent.putExtra("PRODUCT_ITEM_ID", response.getItemId());
                intent.putExtra("PRODUCT_DP", response.dreamPoint);
                intent.putExtra("WAREHOUSE_ID", response.getWarehouseId());
                intent.putExtra("COMPANY_ID", response.getCompanyId());
                intent.putExtra("PRICE", response.price);
                intent.putExtra("MARGIN_POINT", response.marginPoint);
                intent.putExtra("NUMBER", response.getItemNumber());
                intent.putExtra("ItemMultiMRPId", response.getItemMultiMRPId());
                args.putSerializable("ITEM_LIST", response);
                ItemListModel model = MyApplication.getInstance().noteRepository.getCartItem1(response.getItemId());
                if (response.isItemLimit && model != null) {
                    intent.putExtra("remainingqty", model.getItemLimitQty() - model.qty);
                }
                intent.putExtras(args);
                activity.startActivity(intent);
                Utils.fadeTransaction(activity);
            }
        }

        @Override
        public void onError(Throwable e) {
            Utils.hideProgressDialog();
            e.printStackTrace();
        }

        @Override
        public void onComplete() {

        }
    };
}