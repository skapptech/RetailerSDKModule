package app.retailer.krina.shop.com.mp_shopkrina_retailer.webView;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager.widget.ViewPager;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;

import app.retailer.krina.shop.com.mp_shopkrina_retailer.R;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.api.CommonClassForAPI;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ActivityHisabCollectionBinding;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.adapter.HisabCollectionAdapter;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.adapter.VPCollectionAdapter;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.CustomerRes;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.SharePrefs;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.LocaleHelper;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.MyApplication;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils;
import io.reactivex.observers.DisposableObserver;

public class HisabCollectionActivity extends AppCompatActivity {
    private ActivityHisabCollectionBinding mBinding;
    private HisabCollectionActivity activity;
    private Utils utils;
    private HisabCollectionAdapter hisabCollectionAdapter;
    private VPCollectionAdapter vpCollectionAdapter;

    private CommonClassForAPI commonClassForAPI;
    private ArrayList<CustomerRes> customerResArrayList;
    public int VPPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_hisab_collection);
        activity = this;
        initialization();
        callAPI();

        mBinding.toolbarHisabKitab.back.setOnClickListener(v -> {
            onBackPressed();
        });
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
    }

    @Override
    public void applyOverrideConfiguration(Configuration overrideConfiguration) {
        if (overrideConfiguration != null) {
            int uiMode = overrideConfiguration.uiMode;
            overrideConfiguration.setTo(getBaseContext().getResources().getConfiguration());
            overrideConfiguration.uiMode = uiMode;
        }
        super.applyOverrideConfiguration(overrideConfiguration);
    }


    private void initialization() {
        commonClassForAPI = CommonClassForAPI.getInstance(this);
        utils = new Utils(this);

        mBinding.toolbarHisabKitab.title.setText(HisabCollectionActivity.this.getResources().getString(R.string.collection_head));
        mBinding.rvCutomerList.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
        callAPI();

        mBinding.vpCollection.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                VPPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mBinding.btWhatsapp.setOnClickListener(v -> shareWhatsapp());
    }

    public Bitmap loadBitmapFromView(View v) {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        v.measure(View.MeasureSpec.makeMeasureSpec(dm.widthPixels, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(600, View.MeasureSpec.EXACTLY));
        v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
        Bitmap returnedBitmap = Bitmap.createBitmap(v.getMeasuredWidth(), v.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(returnedBitmap);
        v.draw(c);
        return returnedBitmap;
    }

    private void shareWhatsapp() {
        try {
            CustomerRes customerRes = customerResArrayList.get(VPPosition);
            if (!customerRes.getName().equals("") && customerRes.getName() != null) {
                mBinding.shareLayout.initialsTextView.setText(Utils.wordFirstCap(customerRes.getName()).substring(0, 1));
            }
            mBinding.shareLayout.tvUserName.setText(customerRes.getName());
            mBinding.shareLayout.tvUserNumber.setText(customerRes.getMobile());

            try {
                if (Double.parseDouble(customerRes.getLastTrancAmount()) < 0) {
                    String StringAmt = String.valueOf(customerRes.getLastTrancAmount()).replace("-", "");
                    mBinding.shareLayout.tvLastTrsAmount.setText("₹ " + new DecimalFormat("##.##").format(Double.parseDouble(StringAmt)) +
                            " " + HisabCollectionActivity.this.getResources().getString(R.string.due));
                } else {
                    mBinding.shareLayout.tvLastTrsAmount.setText("₹ " + customerRes.getLastTrancAmount() + " " + HisabCollectionActivity.this.getResources().getString(R.string.advance));
                }
            } catch (Exception e) {
                e.printStackTrace();
                mBinding.shareLayout.tvLastTrsAmount.setText("₹ " + customerRes.getLastTrancAmount());
            }

            mBinding.shareLayout.tvDate.setText(Utils.getChangeDateFormatInProfile(customerRes.getLastTrancDate()));
            Bitmap bitmap = loadBitmapFromView(findViewById(R.id.ll_main_chat_view));
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, bytes);
            String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "Title", null);

            Intent sendIntent = new Intent(Intent.ACTION_SEND);
            sendIntent.setType("text/plain");
            sendIntent.putExtra(Intent.EXTRA_TEXT, "");
            sendIntent.setPackage("com.whatsapp");
            sendIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(path));
            sendIntent.setType("image/url");
            startActivity(sendIntent);
            MyApplication.getInstance().updateAnalyticShare(getClass().getSimpleName(), "Hisab Kitab Share On WhatsApp");
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "WhatsApp not installed.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }


    private void callAPI() {
        if (utils.isNetworkAvailable()) {
            if (commonClassForAPI != null) {
                Utils.showProgressDialog(this);
                commonClassForAPI.GetCustomerContact(Getcustomer, SharePrefs.getInstance(this).getString(SharePrefs.HISAB_KITAB_ID));
            }
        } else {
            Utils.setToast(getApplicationContext(), MyApplication.getInstance().dbHelper.getData("internet_connection"));
        }
    }


    private final DisposableObserver<JsonArray> Getcustomer = new DisposableObserver<JsonArray>() {

        @Override
        public void onNext(@NotNull JsonArray response) {
            Utils.hideProgressDialog();
            try {
                if (response != null) {
                    Type listType = new TypeToken<ArrayList<CustomerRes>>() {
                    }.getType();
                    customerResArrayList = new Gson().fromJson(response.toString(), listType);

                    vpCollectionAdapter = new VPCollectionAdapter(activity, customerResArrayList);
                    mBinding.vpCollection.setAdapter(vpCollectionAdapter);

                    hisabCollectionAdapter = new HisabCollectionAdapter(activity, customerResArrayList);
                    mBinding.rvCutomerList.setAdapter(hisabCollectionAdapter);

                    if (customerResArrayList.size() > 0) {
                        mBinding.llShareCollectionWhatsapp.setVisibility(View.VISIBLE);
                    } else {
                        mBinding.llShareCollectionWhatsapp.setVisibility(View.GONE);
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        @Override
        public void onError(Throwable e) {
            Utils.hideProgressDialog();
            e.printStackTrace();
        }

        @Override
        public void onComplete() {
            Utils.hideProgressDialog();
        }
    };
}
