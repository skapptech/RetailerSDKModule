package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import app.retailer.krina.shop.com.mp_shopkrina_retailer.R;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.api.CommonClassForAPI;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ActivityScratchCardBinding;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.offer.BillDiscountModel;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.home.offer.CheckBillDiscountResponse;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.SharePrefs;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.MyApplication;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils;
import io.reactivex.observers.DisposableObserver;

public class ScratchCardActivity extends AppCompatActivity {
    private ActivityScratchCardBinding mBinding;
    private BillDiscountModel model = null;
    private double total = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_scratch_card);
        if (getIntent().getExtras() != null) {
            model = getIntent().getParcelableExtra("list");
            total = getIntent().getDoubleExtra("total", -1);
        }
        initializeViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Utils.rightTransaction(this);
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


    private void initializeViews() {
        mBinding.tvName.setText(MyApplication.getInstance().dbHelper.getString(R.string.scratch_card));
        mBinding.tvDetail.setText(MyApplication.getInstance().dbHelper.getString(R.string.scratch_amp_win));
        mBinding.btnApply.setText(MyApplication.getInstance().dbHelper.getString(R.string.apply_code));
        mBinding.tvTime.setText(MyApplication.getInstance().dbHelper.getString(R.string.offer_date_noffer_expires_in_2_00_hrs));

        mBinding.ivClose.setOnClickListener(v -> onBackPressed());
        mBinding.btnApply.setOnClickListener(v -> {
            if (total > model.getBillAmount()) {
                Intent intent = new Intent();
                intent.putExtra("position", getIntent().getIntExtra("position", 0));
                intent.putExtra("list", model);
                setResult(9, intent);
                onBackPressed();
            } else {
                Utils.setToast(this, getString(R.string.you_are_not_elegible_for_this_offer));
            }
        });

        mBinding.scratchCard.setOnScratchListener((scratchCard, visiblePercent) -> {
            if (visiblePercent > 0.3) {
                mBinding.scratchCard.setVisibility(View.GONE);
                mBinding.tvTime.setVisibility(View.GONE);
                mBinding.btnApply.setVisibility(View.VISIBLE);
                mBinding.tvName.setText(R.string.congratulations);
                mBinding.tvDetail.setText(model.getOfferName());
                CommonClassForAPI.getInstance(this).updateScratchCardStatus(updateScratchCardObserver,
                        SharePrefs.getInstance(this).getInt(SharePrefs.CUSTOMER_ID), model.getOfferId(), true);
            }
        });

        if (model != null) {
            mBinding.tvCoupon.setText(model.getOfferCode());
            long timestamp = getTimeStamp(model.getEnd());
            long expiryTime = timestamp - new Date().getTime();
            new CountDownTimer(expiryTime, 1000) {
                @Override
                public void onTick(long millis) {
                    long sec = TimeUnit.MILLISECONDS.toSeconds(millis);
                    long min = TimeUnit.MILLISECONDS.toMinutes(millis);
                    long hour = TimeUnit.MILLISECONDS.toHours(millis);
                    mBinding.tvTime.setText("Offer Date\nOffer Expires in " + hour % 24 + ":" + min % 60 + ":" + sec % 60 + " hrs");
                }

                @Override
                public void onFinish() {
                    mBinding.tvTime.setText("Time Expired!");
                }
            }.start();
        }
    }

    private static long getTimeStamp(String dateStr) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String all = dateStr.replaceAll("\\+0([0-9]){1}\\:00", "+0$100");
        String s = all.replaceAll("T", " ");
        long timestamp = 0;

        try {
            Date date = format.parse(s);
            timestamp = date.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return timestamp;
    }


    // Update scratch card scratch status
    private final DisposableObserver<CheckBillDiscountResponse> updateScratchCardObserver = new DisposableObserver<CheckBillDiscountResponse>() {
        @Override
        public void onNext(CheckBillDiscountResponse response) {
            Utils.hideProgressDialog();
            Intent intent = new Intent();
            intent.putExtra("list", model);
            setResult(9, intent);
        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
            updateScratchCardObserver.dispose();
            Utils.hideProgressDialog();
        }

        @Override
        public void onComplete() {

        }
    };
}