package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.adapter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import app.retailer.krina.shop.com.mp_shopkrina_retailer.R;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.community.FeedActivity;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.appHome.HomeDataModel;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.Grid3Binding;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.HorizontalTypeBinding;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.OtherTypeOneBinding;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.TileTypeBinding;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.freebies.FreebiesOfferActivity;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.activity.GamesListActivity;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.activity.MembershipPlanActivity;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.HomeActivity;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.appHome.FlashDealOfferFragment;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.order.MyOrderActivity;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.activity.MyWalletActivity;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.shoppingCart.ShoppingCartActivity;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.activity.WebViewActivity;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.clearance.ClearanceActivity;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.direct.TradeActivity;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.allBrands.AllBrandFragItemList;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.category.HomeCategoryFragment;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.fragment.HomeSubCategoryFragment;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.fragment.ShopbyBrandFragment;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.fragment.SubCategoryFragment;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.subCategory.SubSubCategoryFragment;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.fragment.TradeOfferFragment;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.target.CustomerSubCategoryTargetActivity;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.AnalyticPost;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.EndPointPref;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.SharePrefs;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.LocaleHelper;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.MyApplication;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.TextUtils;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils;

public class HomeSectionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public final int SCROLLABLE_BANNER = 0;
    public final int SCROLLABLE_TILE = 1;
    public final int OTHER_TYPE_2 = 2;

    private final HomeActivity activity;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private final HomeDataModel dataDetailsList;
    private final int listSizeItem;
    private final String mSectionSubType;


    public HomeSectionAdapter(HomeActivity activity, HomeDataModel homeDataModel, int listSizeItem, String sectionSubType) {
        this.dataDetailsList = homeDataModel;
        this.activity = activity;
        this.listSizeItem = listSizeItem;
        this.mSectionSubType = sectionSubType;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        switch (viewType) {
            case SCROLLABLE_BANNER:
                return new RecyclerViewHorizontalHolder(DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()),
                        R.layout.horizontal_type, viewGroup, false));
            // horizontal
            case SCROLLABLE_TILE:
                return new RecyclerViewTileTypeHolder(DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()),
                        R.layout.tile_type, viewGroup, false));
            // grid of 3
            case OTHER_TYPE_2:
                return new RecyclerView3gridTypeOneHolder(DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()),
                        R.layout.grid_3, viewGroup, false));
            default:
                return new RecyclerViewOtherTypeOneHolder(DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()),
                        R.layout.other_type_one, viewGroup, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder vh, int i) {
        HomeDataModel.AppItemsList homeDataDetailsModel = dataDetailsList.getAppItemsList().get(i);
        // Banner Image done by harshita
        if (vh.getItemViewType() == SCROLLABLE_BANNER) {
            if (!TextUtils.isNullOrEmpty(dataDetailsList.getAppItemsList().get(i).getBannerImage())) {
                Glide.with(activity).load(dataDetailsList.getAppItemsList().get(i).getBannerImage().trim())
                        .placeholder(R.drawable.logo_grey_wide)
                        .into(((RecyclerViewHorizontalHolder) vh).mBinding.ivBanner);
            } else {
                ((RecyclerViewHorizontalHolder) vh).mBinding.ivBanner.setImageResource(R.drawable.logo_grey_wide);
            }
            ((RecyclerViewHorizontalHolder) vh).mBinding.catLinearLayout.setOnClickListener(v -> {
                clickActionPerform(((RecyclerViewHorizontalHolder) vh).mBinding.catLinearLayout, dataDetailsList.getSectionSubType(), homeDataDetailsModel);
            });

        } else if (vh.getItemViewType() == SCROLLABLE_TILE) {
            ((RecyclerViewTileTypeHolder) vh).mBinding.tvTitle.setText(homeDataDetailsModel.getTileName());
            if (!TextUtils.isNullOrEmpty(homeDataDetailsModel.getTileImage())) {
                Glide.with(activity)
                        .load(homeDataDetailsModel.getTileImage().trim())
                        .placeholder(R.drawable.logo_grey)
                        .into(((RecyclerViewTileTypeHolder) vh).mBinding.ivTile);
            } else {
                ((RecyclerViewTileTypeHolder) vh).mBinding.ivTile.setImageResource(R.drawable.logo_grey);
            }
            ((RecyclerViewTileTypeHolder) vh).mBinding.liTile.setOnClickListener(v -> {
                clickActionPerform(((RecyclerViewTileTypeHolder) vh).mBinding.liTile, dataDetailsList.getSectionSubType(), homeDataDetailsModel);
            });
            setScrollableBackGroundImage((RecyclerViewTileTypeHolder) vh, homeDataDetailsModel);
        } else if (vh.getItemViewType() == OTHER_TYPE_2) {
            setBackGroundImage3Gird((RecyclerView3gridTypeOneHolder) vh, homeDataDetailsModel);
            ((RecyclerView3gridTypeOneHolder) vh).mBinding.tvTextTitle.setText(homeDataDetailsModel.getTileName());

            if (!TextUtils.isNullOrEmpty(homeDataDetailsModel.getTileImage())) {
                Glide.with(activity).load(homeDataDetailsModel.getTileImage().trim())
                        .placeholder(R.drawable.logo_grey)
                        .into(((RecyclerView3gridTypeOneHolder) vh).mBinding.ivItemImage);
            } else {
                ((RecyclerView3gridTypeOneHolder) vh).mBinding.ivItemImage.setImageResource(R.drawable.logo_grey);
            }
            ((RecyclerView3gridTypeOneHolder) vh).mBinding.liTile.setOnClickListener(v -> {
                clickActionPerform(((RecyclerView3gridTypeOneHolder) vh).mBinding.liTile, dataDetailsList.getSectionSubType(), homeDataDetailsModel);
            });
        } else {
            setBackGroundImage((RecyclerViewOtherTypeOneHolder) vh, homeDataDetailsModel);
            ((RecyclerViewOtherTypeOneHolder) vh).mBinding.tvTitle.setText(homeDataDetailsModel.getTileName());

            if (!TextUtils.isNullOrEmpty(homeDataDetailsModel.getTileImage())) {
                Glide.with(activity)
                        .load(homeDataDetailsModel.getTileImage().trim())
                        .placeholder(R.drawable.logo_grey)
                        .into(((RecyclerViewOtherTypeOneHolder) vh).mBinding.ivItemImage);
            } else {
                ((RecyclerViewOtherTypeOneHolder) vh).mBinding.ivItemImage.setImageResource(R.drawable.logo_grey);
            }
            ((RecyclerViewOtherTypeOneHolder) vh).mBinding.catLinearLayout.setOnClickListener(v -> {
                clickActionPerform(((RecyclerViewOtherTypeOneHolder) vh).mBinding.catLinearLayout, dataDetailsList.getSectionSubType(), homeDataDetailsModel);
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        boolean horizontal = dataDetailsList.getRowCount() == 1 && dataDetailsList.getColumnCount() > 2;
        if (horizontal && !dataDetailsList.isTile()) {
            return SCROLLABLE_BANNER;
        } else if (horizontal && dataDetailsList.isTile()) {
            return SCROLLABLE_TILE;
        } else if (dataDetailsList.getRowCount() > 1 && dataDetailsList.getColumnCount() > 2) {
            return OTHER_TYPE_2;
        } else {
            return 9;
        }
    }

    @Override
    public int getItemCount() {
        return dataDetailsList == null ? 0 : listSizeItem;
    }


    private void setScrollableBackGroundImage(RecyclerViewTileTypeHolder vh, HomeDataModel.AppItemsList homeDataDetailsModel) {
        if (homeDataDetailsModel.getTileSectionBackgroundImage() != null && !homeDataDetailsModel.getTileSectionBackgroundImage().equals("") && !homeDataDetailsModel.getTileSectionBackgroundImage().isEmpty()) {
            Picasso.get().load(homeDataDetailsModel.getTileSectionBackgroundImage().trim()).into(new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    vh.mBinding.liTile.setBackground(new BitmapDrawable(bitmap));
                }

                @Override
                public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            });
        } else {
            vh.mBinding.liTile.setBackgroundResource(R.drawable.rectangle_transperent_outline);
        }
    }

    private void setBackGroundImage(@NonNull RecyclerViewOtherTypeOneHolder vh, HomeDataModel.AppItemsList homeDataDetailsModel) {
        if (dataDetailsList.getSectionBackgroundImage() != null && !dataDetailsList.getSectionBackgroundImage().isEmpty()) {
            Picasso.get().load(dataDetailsList.getSectionBackgroundImage().trim()).into(new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    vh.mBinding.catLinearLayout.setBackground(new BitmapDrawable(bitmap));
                }

                @Override
                public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            });
        } else if (homeDataDetailsModel.getTileSectionBackgroundImage() != null && !homeDataDetailsModel.getTileSectionBackgroundImage().isEmpty()) {
            Picasso.get().load(homeDataDetailsModel.getTileSectionBackgroundImage().trim())
                    .into(new Target() {
                        @Override
                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                            vh.mBinding.catLinearLayout.setBackground(new BitmapDrawable(bitmap));
                        }

                        @Override
                        public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                        }

                        @Override
                        public void onPrepareLoad(Drawable placeHolderDrawable) {

                        }
                    });
        } else {
            vh.mBinding.catLinearLayout.setBackgroundResource(R.drawable.rectangle_transperent_outline);
        }
    }

    private void setBackGroundImage3Gird(@NonNull RecyclerView3gridTypeOneHolder vh, HomeDataModel.AppItemsList homeDataDetailsModel) {
        if (dataDetailsList.getSectionBackgroundImage() != null && !dataDetailsList.getSectionBackgroundImage().isEmpty()) {
            Picasso.get()
                    .load(dataDetailsList.getSectionBackgroundImage().replaceAll(" ", "%20"))
                    .into(new Target() {
                        @Override
                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                            vh.mBinding.liTile.setBackground(new BitmapDrawable(bitmap));
                        }

                        @Override
                        public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                        }

                        @Override
                        public void onPrepareLoad(Drawable placeHolderDrawable) {

                        }
                    });
        } else if (homeDataDetailsModel.getTileSectionBackgroundImage() != null && !homeDataDetailsModel.getTileSectionBackgroundImage().isEmpty()) {
            Picasso.get()
                    .load(homeDataDetailsModel.getTileSectionBackgroundImage().replaceAll(" ", "%20"))
                    .into(new Target() {
                        @Override
                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                            vh.mBinding.liTile.setBackground(new BitmapDrawable(bitmap));
                        }

                        @Override
                        public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                        }

                        @Override
                        public void onPrepareLoad(Drawable placeHolderDrawable) {

                        }
                    });
        } else {
            vh.mBinding.liTile.setBackgroundResource(R.drawable.rectangle_transperent_outline);
        }
    }

    private void clickActionPerform(LinearLayout vh, String actionType, HomeDataModel.AppItemsList appItemModel) {
        try {
            vh.setEnabled(false);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Do something after 100ms
                    vh.setEnabled(true);
                    handler.postDelayed(this, 500);
                    handler.removeCallbacks(this);
                    Bundle args = new Bundle();
                    args.putString("ItemId", String.valueOf(appItemModel.getRedirectionID()));
                    args.putInt("BaseCategoryId", appItemModel.getBaseCategoryId());
                    args.putString("SectionType", mSectionSubType);
                    args.putInt("CATEGORY_ID", appItemModel.getCategoryId());
                    args.putInt("SUB_CAT_ID", appItemModel.getSubCategoryId());
                    args.putInt("SUB_SUB_CAT_ID", appItemModel.getSubsubCategoryId());
                    args.putBoolean("HOME_FLAG", true);
                    // analytics data
                    AnalyticPost analyticPost = new AnalyticPost();
                    analyticPost.sectionId = dataDetailsList.getSectionID();
                    analyticPost.sectionSubType = dataDetailsList.getSectionSubType();
                    analyticPost.sectionName = dataDetailsList.getSectionName();
                    analyticPost.baseCatId = String.valueOf(appItemModel.getBaseCategoryId());
                    analyticPost.categoryId = appItemModel.getCategoryId();
                    analyticPost.subCatId = appItemModel.getSubCategoryId();
                    analyticPost.subSubCatId = appItemModel.getSubsubCategoryId();
                    String url = dataDetailsList.getWebViewUrl();
                    if (url != null && url.length() > 0) {
                        url = url.replace("[CUSTOMERID]", "" + SharePrefs.getInstance(activity).getInt(SharePrefs.CUSTOMER_ID));
                        url = url.replace("[SKCODE]", "" + SharePrefs.getInstance(activity).getString(SharePrefs.SK_CODE));
                        url = url.replace("[WAREHOUSEID]", "" + SharePrefs.getInstance(activity).getInt(SharePrefs.WAREHOUSE_ID));
                        url = url.replace("[LANG]", "" + LocaleHelper.getLanguage(activity));
                        url = url.replace("[MOBILE]", "" + SharePrefs.getInstance(activity).getString(SharePrefs.MOBILE_NUMBER));
                        dataDetailsList.setWebViewUrl(url);
                    }

                    switch (actionType) {
                        case "Base Category":
                            if (dataDetailsList.getViewType() != null && !dataDetailsList.getViewType().isEmpty() && dataDetailsList.getViewType().equalsIgnoreCase("webView")) {
                                if (dataDetailsList.getWebViewUrl().startsWith("https://trade.er15.xyz:4436")) {
                                    if (EndPointPref.getInstance(activity).getBoolean(EndPointPref.showNewSocial))
                                        activity.startActivity(new Intent(activity, FeedActivity.class));
                                    else
                                        activity.startActivity(new Intent(activity, TradeActivity.class));
                                    Utils.leftTransaction(activity);
                                } else if(dataDetailsList.getWebViewUrl().contains("ScaleUpIntegration/LeadInitiate")){
                                    activity.callScaleUpApiUsingUrl(url);
                                }  else {
                                    Bundle bundle = new Bundle();
                                    bundle.putString("url", dataDetailsList.getWebViewUrl());
                                    activity.startActivity(new Intent(activity, WebViewActivity.class).putExtras(bundle));
                                }
                            } else {
                                args.putString("cetegoryTittle", appItemModel.getTileName());
                                activity.pushFragments(SubCategoryFragment.newInstance(), false, true, args);
                            }
                            // update analytics
                            MyApplication.getInstance().updateAnalytics("appHome_basCat_click", analyticPost);
                            break;
                        case "Brand":
                            if (dataDetailsList.getViewType() != null && !dataDetailsList.getViewType().isEmpty() && dataDetailsList.getViewType().equalsIgnoreCase("webView")) {
                                if (dataDetailsList.getWebViewUrl().startsWith("https://trade.er15.xyz:4436")) {
                                    if (EndPointPref.getInstance(activity).getBoolean(EndPointPref.showNewSocial))
                                        activity.startActivity(new Intent(activity, FeedActivity.class));
                                    else
                                        activity.startActivity(new Intent(activity, TradeActivity.class));
                                    Utils.leftTransaction(activity);
                                } else if (dataDetailsList.getWebViewUrl().startsWith("vAtm") || dataDetailsList.getWebViewUrl().startsWith("vatm")) {
                                    activity.callVAtmApi();
                                } else if (dataDetailsList.getWebViewUrl().contains("Udhar/GenerateLead")) {
                                    activity.callLeadApi(dataDetailsList.getWebViewUrl());
                                }  else if(dataDetailsList.getWebViewUrl().contains("ScaleUpIntegration/LeadInitiate")){
                                    activity.callScaleUpApiUsingUrl(url);
                                } else {
                                    Bundle bundle = new Bundle();
                                    bundle.putString("url", dataDetailsList.getWebViewUrl());
                                    activity.startActivity(new Intent(activity, WebViewActivity.class).putExtras(bundle));
                                }
                            } else {
                                if (!String.valueOf(appItemModel.getRedirectionID()).equalsIgnoreCase("0") && appItemModel.getRedirectionID() != 0) {
//                                    args.putBoolean("isStore", true);
                                    activity.pushFragments(SubSubCategoryFragment.newInstance(), false, true, args);
                                } else {
                                    args.putString("BRAND_NAME", "Brand");
                                    activity.pushFragments(ShopbyBrandFragment.newInstance(), false, true, args);
                                }
                            }
                            // update analytics
                            MyApplication.getInstance().updateAnalytics("appHome_brand_click", analyticPost);
                            break;
                        case "Category":
                            args.putBoolean("HOME_FLAG", false);
                            if (dataDetailsList.getViewType() != null && !dataDetailsList.getViewType().isEmpty() && dataDetailsList.getViewType().equalsIgnoreCase("webView")) {
                                if (dataDetailsList.getWebViewUrl().startsWith("https://trade.er15.xyz:4436")) {
                                    if (EndPointPref.getInstance(activity).getBoolean(EndPointPref.showNewSocial))
                                        activity.startActivity(new Intent(activity, FeedActivity.class));
                                    else
                                        activity.startActivity(new Intent(activity, TradeActivity.class));
                                    Utils.leftTransaction(activity);
                                }  else if(dataDetailsList.getWebViewUrl().contains("ScaleUpIntegration/LeadInitiate")){
                                    activity.callScaleUpApiUsingUrl(url);
                                } else {
                                    Bundle bundle = new Bundle();
                                    bundle.putString("url", dataDetailsList.getWebViewUrl());
                                    activity.startActivity(new Intent(activity, WebViewActivity.class).putExtras(bundle));
                                }
                            } else {
                                if (appItemModel.getRedirectionID() != 0) {
                                    activity.pushFragments(SubSubCategoryFragment.newInstance(), false, true, args);
                                } else {
                                    args.putString("BRAND_NAME", "Brand");
                                    activity.pushFragments(ShopbyBrandFragment.newInstance(), false, true, args);
                                }
                            }
                            // update analytics
                            MyApplication.getInstance().updateAnalytics("appHome_category_click", analyticPost);
                            break;
                        case "SubCategory":
                            if (dataDetailsList.getViewType() != null && !dataDetailsList.getViewType().isEmpty() && dataDetailsList.getViewType().equalsIgnoreCase("webView")) {
                                if (dataDetailsList.getWebViewUrl().startsWith("https://trade.er15.xyz:4436")) {
                                    if (EndPointPref.getInstance(activity).getBoolean(EndPointPref.showNewSocial))
                                        activity.startActivity(new Intent(activity, FeedActivity.class));
                                    else
                                        activity.startActivity(new Intent(activity, TradeActivity.class));
                                    Utils.leftTransaction(activity);
                                } else if (dataDetailsList.getWebViewUrl().startsWith("ShowStoreFlashDeal")) {
                                    int id = dataDetailsList.getAppItemsList().get(0).getRedirectionID();
                                    args = new Bundle();
                                    args.putString("SECTION_ID", "-1");
                                    args.putInt("subCategoryId", id);
                                    args.putBoolean("isStore", true);
                                    activity.pushFragments(FlashDealOfferFragment.newInstance(), true, true, args);
                                } else if(dataDetailsList.getWebViewUrl().contains("ScaleUpIntegration/LeadInitiate")){
                                    activity.callScaleUpApiUsingUrl(url);
                                }  else {
                                    Bundle bundle = new Bundle();
                                    bundle.putString("url", dataDetailsList.getWebViewUrl());
                                    activity.startActivity(new Intent(activity, WebViewActivity.class).putExtras(bundle));
                                }
                            } else {
                                if (!String.valueOf(appItemModel.getRedirectionID()).equalsIgnoreCase("0") && appItemModel.getRedirectionID() != 0) {
                                    args.putString("ITEM_IMAGE", appItemModel.getTileImage());
                                    activity.pushFragments(HomeSubCategoryFragment.newInstance(), false, true, args);
                                } else {
                                    args.putString("BRAND_NAME", "Brand");
                                    activity.pushFragments(ShopbyBrandFragment.newInstance(), false, true, args);
                                }
                            }
                            // update analytics
                            MyApplication.getInstance().updateAnalytics("appHome_subCat_click", analyticPost);
                            break;
                        case "Other":
                            if (dataDetailsList.getViewType() != null && !dataDetailsList.getViewType().isEmpty() && dataDetailsList.getViewType().equalsIgnoreCase("webView")) {
                                if (dataDetailsList.getWebViewUrl().startsWith("https://trade.er15.xyz:4436")) {
                                    if (EndPointPref.getInstance(activity).getBoolean(EndPointPref.showNewSocial))
                                        activity.startActivity(new Intent(activity, FeedActivity.class));
                                    else
                                        activity.startActivity(new Intent(activity, TradeActivity.class));
                                    Utils.leftTransaction(activity);
                                }  else if(dataDetailsList.getWebViewUrl().contains("ScaleUpIntegration/LeadInitiate")){
                                    activity.callScaleUpApiUsingUrl(url);
                                } else {
                                    Bundle bundle = new Bundle();
                                    bundle.putString("url", dataDetailsList.getWebViewUrl());
                                    activity.startActivity(new Intent(activity, WebViewActivity.class).putExtras(bundle));
                                }
                            } else {
                                callActivities(appItemModel.getBannerActivity(), appItemModel.getRedirectionUrl());
                            }
                        default:
                            if (dataDetailsList.getViewType() != null && !dataDetailsList.getViewType().isEmpty() && dataDetailsList.getViewType().equalsIgnoreCase("webView")) {
                                if (dataDetailsList.getWebViewUrl().startsWith("https://trade.er15.xyz:4436")) {
                                    if (EndPointPref.getInstance(activity).getBoolean(EndPointPref.showNewSocial))
                                        activity.startActivity(new Intent(activity, FeedActivity.class));
                                    else
                                        activity.startActivity(new Intent(activity, TradeActivity.class));
                                    Utils.leftTransaction(activity);
                                }  else if(dataDetailsList.getWebViewUrl().contains("ScaleUpIntegration/LeadInitiate")){
                                    activity.callScaleUpApiUsingUrl(url);
                                } else {
                                    Bundle bundle = new Bundle();
                                    bundle.putString("url", dataDetailsList.getWebViewUrl());
                                    activity.startActivity(new Intent(activity, WebViewActivity.class).putExtras(bundle));
                                }
                            } else {
                                if (!String.valueOf(appItemModel.getRedirectionID()).equalsIgnoreCase("0") && appItemModel.getRedirectionID() != 0) {
                                    // args.putString("BRAND_NAME", "Banner Brand");
                                    activity.pushFragments(SubSubCategoryFragment.newInstance(), false, true, args);
                                } else {
                                    args.putString("BRAND_NAME", "Brand");
                                    activity.pushFragments(ShopbyBrandFragment.newInstance(), false, true, args);
                                }
                            }
                            // update analytics
                            MyApplication.getInstance().updateAnalytics("appHome_default_click", analyticPost);
                            break;
                    }
                }
            }, 300);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void callActivities(String type, String url) {
        Intent i = null;
        if (type.equalsIgnoreCase("games")) {
            activity.startActivity(new Intent(activity, GamesListActivity.class));
            Utils.fadeTransaction(activity);
        } else if (type.equalsIgnoreCase("target")) {
            activity.startActivity(new Intent(activity, CustomerSubCategoryTargetActivity.class));
            Utils.fadeTransaction(activity);
        } else if (type.equalsIgnoreCase("prime")) {
            activity.startActivity(new Intent(activity, MembershipPlanActivity.class));
            Utils.fadeTransaction(activity);
        } else if (type.equalsIgnoreCase("shoppingcart")) {
            activity.startActivity(new Intent(activity, ShoppingCartActivity.class));
            Utils.fadeTransaction(activity);
        } else if (type.equalsIgnoreCase("wallet")) {
            i = new Intent(activity, MyWalletActivity.class);
            activity.startActivity(i);
            Utils.fadeTransaction(activity);
        } else if (type.equalsIgnoreCase("category")) {
            activity.pushFragments(
                    HomeCategoryFragment.newInstance(),
                    false,
                    true,
                    null
            );
        } else if (type.equalsIgnoreCase("tradeoffer")) {
            activity.pushFragments(
                    TradeOfferFragment.newInstance(),
                    false,
                    true,
                    null
            );
        } else if (type.equalsIgnoreCase("allbrands")) {
            activity.pushFragments(
                    AllBrandFragItemList.newInstance(),
                    false,
                    true,
                    null
            );
        } else if (type.equalsIgnoreCase("freebies")) {
            activity.startActivity(new Intent(activity, FreebiesOfferActivity.class));
            Utils.fadeTransaction(activity);
        } else if (type.equalsIgnoreCase("myorder")) {
            activity.startActivity(new Intent(activity, MyOrderActivity.class));
            Utils.fadeTransaction(activity);
        } else if (type.equalsIgnoreCase("direct")) {
            if (EndPointPref.getInstance(activity).getBoolean(EndPointPref.showNewSocial))
                activity.startActivity(new Intent(activity, FeedActivity.class));
            else
                activity.startActivity(new Intent(activity, TradeActivity.class));
            Utils.fadeTransaction(activity);
        } else if (type.equalsIgnoreCase("Clearance")) {
            activity.startActivity(new Intent(activity, ClearanceActivity.class));
            Utils.fadeTransaction(activity);
        } else if (type.equalsIgnoreCase("ExternalURL")) {
            try {
                Uri uri = Uri.parse(url); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                activity.startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Utils.fadeTransaction(activity);
        }
    }


    public class RecyclerViewHorizontalHolder extends RecyclerView.ViewHolder {
        HorizontalTypeBinding mBinding;

        RecyclerViewHorizontalHolder(HorizontalTypeBinding mBinding) {
            super(mBinding.getRoot());
            this.mBinding = mBinding;
        }
    }

    public class RecyclerViewTileTypeHolder extends RecyclerView.ViewHolder {
        TileTypeBinding mBinding;

        RecyclerViewTileTypeHolder(TileTypeBinding mBinding) {
            super(mBinding.getRoot());
            this.mBinding = mBinding;
        }
    }

    public class RecyclerViewOtherTypeOneHolder extends RecyclerView.ViewHolder {
        OtherTypeOneBinding mBinding;

        RecyclerViewOtherTypeOneHolder(OtherTypeOneBinding mBinding) {
            super(mBinding.getRoot());
            this.mBinding = mBinding;
        }
    }

    public class RecyclerView3gridTypeOneHolder extends RecyclerView.ViewHolder {
        Grid3Binding mBinding;

        RecyclerView3gridTypeOneHolder(Grid3Binding mBinding) {
            super(mBinding.getRoot());
            this.mBinding = mBinding;
        }
    }
}