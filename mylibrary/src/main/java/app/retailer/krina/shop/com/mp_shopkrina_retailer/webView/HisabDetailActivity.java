package app.retailer.krina.shop.com.mp_shopkrina_retailer.webView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;

import app.retailer.krina.shop.com.mp_shopkrina_retailer.R;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.api.CommonClassForAPI;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ActivityHisabDeatilBinding;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.interfaces.OnChatClick;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.adapter.HisabDetailAdapter;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.CustomerRes;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.HisabDetailModel;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.SharePrefs;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.LocaleHelper;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.MyApplication;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils;
import io.reactivex.observers.DisposableObserver;

public class HisabDetailActivity extends AppCompatActivity implements View.OnClickListener, OnChatClick {
    private ActivityHisabDeatilBinding mBinding;
    private CustomerRes customersContactModel;
    private static final int MY_PERMISSION_REQUEST_CODE = 123;
    private Utils utils;
    private CommonClassForAPI commonClassForAPI;
    private ArrayList<HisabDetailModel> hisabDetailList;
    int selectedPosition = 0;
    double TotalBalanceAmount;
    String NotificationId = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_hisab_deatil);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle extras = getIntent().getExtras();
        try {
            if (extras != null) {
                NotificationId = getIntent().getStringExtra("id");
                customersContactModel = (CustomerRes) getIntent().getSerializableExtra("list");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // init view
        initialization();
        getCustomerDetails();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void callAPI() {
        if (utils.isNetworkAvailable()) {
            if (commonClassForAPI != null) {
                Utils.showProgressDialog(this);
                commonClassForAPI.GetGetMyHisabKitab(GetDetailUser, SharePrefs.getInstance(this).getString(SharePrefs.HISAB_KITAB_ID), customersContactModel.getId(), "");
            }
        } else {
            Utils.setToast(this, MyApplication.getInstance().dbHelper.getString(R.string.internet_connection));
        }
    }

    private void getCustomerDetails() {
        if (utils.isNetworkAvailable()) {
            if (commonClassForAPI != null) {
                Utils.showProgressDialog(this);
                commonClassForAPI.GetCustomerDetailsbyID(CustomerDetail, SharePrefs.getInstance(this).getString(SharePrefs.HISAB_KITAB_ID), NotificationId);
            }
        } else {
            Utils.setToast(this, MyApplication.getInstance().dbHelper.getString(R.string.internet_connection));
        }
    }

    private void initialization() {
        commonClassForAPI = CommonClassForAPI.getInstance(this);
        utils = new Utils(this);


        mBinding.llSend.setOnClickListener(this);
        mBinding.llGiveCreadit.setOnClickListener(this);
        mBinding.llAcceptPayment.setOnClickListener(this);
        mBinding.llCalling.setOnClickListener(this);

        mBinding.recyeler.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_give_creadit:
                givePayment("Give");
                break;
            case R.id.ll_accept_payment:
                givePayment("Accept");
                break;
            case R.id.ll_send:
                shareOption();
                break;
            case R.id.ll_calling:
                callingOption();
                break;
        }
    }


    DisposableObserver<JsonObject> GetDetailUser = new DisposableObserver<JsonObject>() {
        @Override
        public void onNext(JsonObject response) {
            Utils.hideProgressDialog();
            try {
                if (response != null) {
                    JsonArray jsonArray = response.get("myHisabKitabDc").getAsJsonArray();
                    TotalBalanceAmount = response.get("TotalBalanceAmount").getAsDouble();
                    TotalBalanceAmount = Double.parseDouble(new DecimalFormat("##.##").format(TotalBalanceAmount));
                    if (TotalBalanceAmount < 0) {
                        String TotalBal = String.valueOf(TotalBalanceAmount);
                        TotalBal = TotalBal.replace("-", "");
                        mBinding.tvBalanceDue.setText(getString(R.string.balance) + " ₹ " + TotalBal + " " + getString(R.string.due));
                        mBinding.tvBalanceDue.setTextColor(getResources().getColor(R.color.colorAccent));
                    } else {
                        if (TotalBalanceAmount == 0) {
                            mBinding.tvBalanceDue.setText(getString(R.string.no_dues));
                        } else {
                            mBinding.tvBalanceDue.setText(getString(R.string.balance) + " ₹ " + TotalBalanceAmount + " " + getString(R.string.advance));
                        }
                        mBinding.tvBalanceDue.setTextColor(getResources().getColor(R.color.green_50));
                    }
                    Type listType = new TypeToken<ArrayList<HisabDetailModel>>() {
                    }.getType();
                    hisabDetailList = new Gson().fromJson(jsonArray, listType);

                    HisabDetailAdapter hisabDetailAdapter = new HisabDetailAdapter(HisabDetailActivity.this, hisabDetailList, HisabDetailActivity.this);
                    mBinding.recyeler.setAdapter(hisabDetailAdapter);
                    mBinding.recyeler.scrollToPosition(hisabDetailList.size() - 1);
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

    DisposableObserver<CustomerRes> CustomerDetail = new DisposableObserver<CustomerRes>() {

        @Override
        public void onNext(CustomerRes response) {
            Utils.hideProgressDialog();
            try {
                if (response != null) {

                    customersContactModel = response;
                    getSupportActionBar().setTitle(customersContactModel.getName());
                    getSupportActionBar().setSubtitle(customersContactModel.getMobile());
                    callAPI();
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


    private void callingOption() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkPermission();
        } else {
            CallContact();
        }
    }

    private void shareOption() {
        startActivity(new Intent(this, ShareOptionActivity.class).putExtra("customerContectID", customersContactModel).putExtra("balance", TotalBalanceAmount));
        Utils.leftTransaction(this);
    }

    private void givePayment(String GiveAccept) {
        Intent inNext = new Intent(this, AddCreditActivity.class);
        inNext.putExtra("list", customersContactModel);
        inNext.putExtra("GiveAccept", GiveAccept);
        startActivityForResult(inNext, 100);
        Utils.leftTransaction(this);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("onResult-ActivityMain", String.valueOf(requestCode));
        if (requestCode == 100 && data != null && resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            callAPI();

        }
    }

    protected void checkPermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.CALL_PHONE)) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSION_REQUEST_CODE);
                } else {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSION_REQUEST_CODE);
                }
            } else {
                CallContact();
            }
        }
    }

    @SuppressLint("MissingPermission")
    private void CallContact() {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + customersContactModel.getMobile()));
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NotNull String[] permissions, @NotNull int[] grantResults) {
        if (requestCode == MY_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
                CallContact();
            } else {
                // Permission denied
            }
        }
    }

    @Override
    public void onSelectClick(int position, String giveAccept) {
        selectedPosition = position;
        Intent inNext = new Intent(this, TransactionDetailActivity.class);
        inNext.putExtra("list", customersContactModel);
        inNext.putExtra("msgList", hisabDetailList.get(position));
        inNext.putExtra("giveAccept", giveAccept);
        startActivityForResult(inNext, 100);
        Utils.leftTransaction(this);
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
}
