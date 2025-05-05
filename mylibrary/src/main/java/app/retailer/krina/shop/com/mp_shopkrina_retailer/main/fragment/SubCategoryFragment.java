package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import app.retailer.krina.shop.com.mp_shopkrina_retailer.R;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.api.CommonClassForAPI;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.FragmentSubCategoryBinding;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.HomeActivity;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.adapter.SubCategoryAdapter;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.adapter.ViewPagerAdapter;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.CategoryDetailsModel;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.SliderModel;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.subCategory.SubCatImageModel;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.SubCategoryModel;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.SectionPref;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.SharePrefs;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.AutoScrollViewPager;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.LocaleHelper;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.RetailerSDKApp;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils;
import io.reactivex.observers.DisposableObserver;

public class SubCategoryFragment extends Fragment {
    private FragmentSubCategoryBinding mBinding;
    private SwipeRefreshLayout refresh_layout;

    private HomeActivity activity;
    private Utils utils;
    private CommonClassForAPI commonClassForAPI;
    private AutoScrollViewPager viewPager;
    private SubCategoryAdapter adapterCategorie;

    private final List<CategoryDetailsModel> basecatlist = new ArrayList<>();
    private final ArrayList<SliderModel> sliderList = new ArrayList<>();
    private String ItemId;
    private int warehouseId, baseCatId = 1;
    private String categoryTittle, lang = "", mSectionType = "";


    public SubCategoryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (HomeActivity) context;
    }

    public static SubCategoryFragment newInstance() {
        return new SubCategoryFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = FragmentSubCategoryBinding.inflate(inflater, container, false);
        if (getArguments() != null) {
            ItemId = getArguments().getString("ItemId");
            baseCatId = getArguments().getInt("BaseCategoryId");
            categoryTittle = getArguments().getString("cetegoryTittle");
            mSectionType = getArguments().getString("SectionType");
        }
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lang = LocaleHelper.getLanguage(activity);
        activity.searchText.setVisibility(View.VISIBLE);
        activity.rightSideIcon.setVisibility(View.VISIBLE);
        // init view
        initialization();
        // cat API call
        categoryAPICall();
        refresh_layout.setOnRefreshListener(() -> {
            if (commonClassForAPI != null) {
                mBinding.progressSub.setVisibility(View.VISIBLE);
                commonClassForAPI.fetchSubcategory(getSubCategory, ItemId,
                        activity.custId, warehouseId, lang);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        RetailerSDKApp.getInstance().mFirebaseAnalytics.setCurrentScreen(activity,
                this.getClass().getSimpleName(), null);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getSubCategory.dispose();
        getimage.dispose();
    }


    public void initialization() {
        warehouseId = SharePrefs.getInstance(activity).getInt(SharePrefs.WAREHOUSE_ID);
        utils = new Utils(activity);
        commonClassForAPI = CommonClassForAPI.getInstance(activity);
        viewPager = mBinding.pager;
        viewPager.setCycle(true);
        viewPager.setStopScrollWhenTouch(true);
        activity.bottomNavigationView.setVisibility(View.VISIBLE);
        mBinding.gridview.setLayoutManager(new GridLayoutManager(activity, 2));
        adapterCategorie = new SubCategoryAdapter(activity, basecatlist, baseCatId, mSectionType);
        mBinding.gridview.setAdapter(adapterCategorie);
        adapterCategorie.notifyDataSetChanged();
        refresh_layout = mBinding.swipeContainer;
        refresh_layout.setColorSchemeResources(R.color.colorAccent);
        mBinding.title.setText(categoryTittle);
        mBinding.title.setText(RetailerSDKApp.getInstance().dbHelper.getString(R.string.shop_by_categories));
        mBinding.noItems.setText(RetailerSDKApp.getInstance().dbHelper.getString(R.string.no_items_available));
    }

    public void setupViewpager(ArrayList<SliderModel> list) {
        if (sliderList.size() != 0) {
            ViewPagerAdapter adapter = new ViewPagerAdapter(activity, list);
            viewPager.setAdapter(adapter);
            final float density = activity.getResources().getDisplayMetrics().density;
           // circlePageIndicator.setRadius(3 * density);
           // circlePageIndicator.setFillColor(0x99FF4500);
           // mBinding.indicator.setViewPager(viewPager);
        }
    }

    private void categoryAPICall() {
        String dataSaved = SectionPref.getInstance(activity).getString(SectionPref.CATEGORY_BY_ID + ItemId);
        if (dataSaved != null && !dataSaved.isEmpty()) {
            try {
                basecatlist.clear();
                SubCategoryModel subCategoryResponses = new Gson().fromJson(dataSaved, SubCategoryModel.class);
                basecatlist.addAll(subCategoryResponses.getCategories());
                if (basecatlist.size() != 0) {
                    adapterCategorie.setSubCategory(basecatlist, baseCatId);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            if (utils.isNetworkAvailable()) {
                mBinding.progressSub.setVisibility(View.VISIBLE);
                commonClassForAPI.fetchSubcategory(getSubCategory, ItemId, activity.custId, warehouseId, lang);
            } else {
                Utils.setToast(activity, RetailerSDKApp.getInstance().dbHelper.getString(R.string.internet_connection));
            }
        }
    }


    // get category result
    private final DisposableObserver<JsonObject> getSubCategory = new DisposableObserver<JsonObject>() {
        @Override
        public void onNext(@NotNull JsonObject jsonObject) {
            mBinding.progressSub.setVisibility(View.GONE);
            mBinding.noItems.setVisibility(View.GONE);
            basecatlist.clear();
            SubCategoryModel model = new Gson().fromJson(jsonObject, SubCategoryModel.class);
            if (model != null && model.getCategories() != null && model.getCategories().size() > 0) {
                basecatlist.addAll(model.getCategories());
                SectionPref.getInstance(activity).putString(SectionPref.CATEGORY_BY_ID + ItemId, jsonObject.toString());
                if (basecatlist.size() != 0) {
                    adapterCategorie.setSubCategory(basecatlist, baseCatId);
                }
                refresh_layout.setRefreshing(false);
            } else {
                mBinding.noItems.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
            mBinding.noItems.setVisibility(View.VISIBLE);
            mBinding.progressSub.setVisibility(View.GONE);
        }

        @Override
        public void onComplete() {
        }
    };

    // get image
    private final DisposableObserver<List<SubCatImageModel>> getimage = new DisposableObserver<List<SubCatImageModel>>() {
        @Override
        public void onNext(@NotNull List<SubCatImageModel> subCatImageResponses) {
            sliderList.clear();
            for (int i = 0; i < subCatImageResponses.size(); i++) {
                sliderList.add(new SliderModel(subCatImageResponses.get(i).categoryimageid, subCatImageResponses.get(i).categoryimg));
            }
            setupViewpager(sliderList);
            refresh_layout.setRefreshing(false);
        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
        }

        @Override
        public void onComplete() {
        }
    };
}