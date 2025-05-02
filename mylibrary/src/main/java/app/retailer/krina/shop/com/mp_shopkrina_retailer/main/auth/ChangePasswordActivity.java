package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.auth;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.gson.JsonObject;

import app.retailer.krina.shop.com.mp_shopkrina_retailer.R;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.api.CommonClassForAPI;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ActivityChangepassordBinding;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.HomeActivity;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.splash.SplashScreenActivity;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.postModels.ChangePasswordModel;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.SharePrefs;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.LocaleHelper;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.RetailerSDKApp;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.TextUtils;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils;
import io.reactivex.observers.DisposableObserver;

public class ChangePasswordActivity extends AppCompatActivity implements View.OnClickListener {
    private ActivityChangepassordBinding mBinding;
    private Utils utils;
    private CommonClassForAPI commonClassForAPI;
    private EditText oldPassword;
    private Button changePasswordBtn;
    private String SoldPassword, SconfirmPassword;
    private String CurrentPassowrd;
    private int custId;
    private int flag;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_changepassord);
        mBinding.toolbarCp.title.setText(RetailerSDKApp.getInstance().dbHelper.getString(R.string.changepassword));
        SharePrefs.getInstance(ChangePasswordActivity.this).putString(SharePrefs.IsSignup, "true");

        Intent intent = getIntent();
        if (intent != null) {
            flag = intent.getIntExtra("FLAG", 0);
        }
        if (flag == 0) {
            mBinding.textInputLayout.setHint(RetailerSDKApp.getInstance().dbHelper.getString(R.string.enter_sms_password));
            mBinding.llMeassage.setVisibility(View.VISIBLE);
        } else {
            mBinding.textInputLayout.setHint(RetailerSDKApp.getInstance().dbHelper.getString(R.string.oldpass));
        }
        //init view
        initialization();
        // changed pass Btn
        changePasswordBtn.setOnClickListener(v -> {
            validateFields();
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.back) {
            onBackPressed();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (flag == 0) {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            finish();
        }
        finish();
        Utils.fadeTransaction(this);
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


    public void initialization() {
        mBinding.tvNewPassword.setHint(RetailerSDKApp.getInstance().dbHelper.getString(R.string.newpass));
        mBinding.tvConfirmPassword.setHint(RetailerSDKApp.getInstance().dbHelper.getString(R.string.conformpass));
        mBinding.changePasswordBtn.setText(RetailerSDKApp.getInstance().dbHelper.getString(R.string.txt_cust_Save));

        CurrentPassowrd = SharePrefs.getInstance(this).getString(SharePrefs.PASSWORD);
        custId = SharePrefs.getInstance(this).getInt(SharePrefs.CUSTOMER_ID);
        utils = new Utils(this);
        commonClassForAPI = CommonClassForAPI.getInstance(this);

        mBinding.toolbarCp.back.setOnClickListener(this);
        oldPassword = mBinding.oldpassword;
        changePasswordBtn = mBinding.changePasswordBtn;

        mBinding.etConfirmPwd.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_NULL) {
                validateFields();
                return true;
            }
            return false;
        });
    }

    private void validateFields() {
        if (!(mBinding.oldpassword.getText().toString().equalsIgnoreCase(CurrentPassowrd))) {
            mBinding.oldpassword.setError(RetailerSDKApp.getInstance().dbHelper.getString(R.string.validation_old_password_incorrect));
            mBinding.oldpassword.requestFocus();
        } else if (TextUtils.isNullOrEmpty(mBinding.newpassword.getText().toString().trim())) {
            mBinding.newpassword.setError(RetailerSDKApp.getInstance().dbHelper.getString(R.string.validation_enter_new_pass));
            mBinding.newpassword.requestFocus();
        } else if (CurrentPassowrd.equalsIgnoreCase(mBinding.newpassword.getText().toString().trim())) {
            mBinding.newpassword.setError(RetailerSDKApp.getInstance().dbHelper.getString(R.string.validation_both_pass_not_same));
            mBinding.newpassword.requestFocus();
        } else if (mBinding.newpassword.length() <= 5) {
            mBinding.newpassword.setError(RetailerSDKApp.getInstance().dbHelper.getString(R.string.special_validation));
            mBinding.newpassword.requestFocus();
        } else if (TextUtils.isNullOrEmpty(mBinding.oldpassword.getText().toString().trim())) {
            mBinding.oldpassword.setError(RetailerSDKApp.getInstance().dbHelper.getString(R.string.validation_enter_old_pass));
            mBinding.oldpassword.requestFocus();
        } else if (TextUtils.isNullOrEmpty(mBinding.etConfirmPwd.getText().toString().trim())) {
            mBinding.etConfirmPwd.setError(RetailerSDKApp.getInstance().dbHelper.getString(R.string.validation_enter_confrm_pass));
            mBinding.etConfirmPwd.requestFocus();
        } else if (!mBinding.newpassword.getText().toString().trim().equalsIgnoreCase(mBinding.etConfirmPwd.getText().toString().trim())) {
            mBinding.etConfirmPwd.setError(RetailerSDKApp.getInstance().dbHelper.getString(R.string.validation_doesnotmatch));
            mBinding.etConfirmPwd.requestFocus();
        } else if (utils.isNetworkAvailable()) {
            if (commonClassForAPI != null) {
                SoldPassword = oldPassword.getText().toString();
                SconfirmPassword = mBinding.etConfirmPwd.getText().toString();
                Utils.showProgressDialog(this);
                commonClassForAPI.changePassword(changedPassDes,
                        new ChangePasswordModel(SconfirmPassword, SoldPassword, custId));
            }
        } else {
            Utils.setToast(getApplicationContext(), RetailerSDKApp.getInstance().dbHelper.getString(R.string.internet_connection));
        }
    }


    // changed pass result
    private final DisposableObserver<JsonObject> changedPassDes = new DisposableObserver<JsonObject>() {
        @Override
        public void onNext(@NonNull JsonObject jsonObject) {
            try {
                if (jsonObject.get("Status").getAsBoolean()) {
                    Utils.hideProgressDialog();
                    Utils.setToast(getApplicationContext(), RetailerSDKApp.getInstance().dbHelper.getString(R.string.msg_passwordchanged));
                    mBinding.oldpassword.setText("");
                    mBinding.etConfirmPwd.setText("");
                    mBinding.newpassword.setText("");
                    SharePrefs.getInstance(getApplicationContext()).putString(SharePrefs.PASSWORD, jsonObject.get("Password").getAsString());
                    if (flag == 0) {
                        startActivity(new Intent(getApplicationContext(), SplashScreenActivity.class));
                        Utils.leftTransaction(ChangePasswordActivity.this);
                        finish();
                        RetailerSDKApp.getInstance().prefManager.setLoggedIn(false);
                        RetailerSDKApp.getInstance().prefManager.setShowcaseFirstTimeLaunch(true);
                    } else if (flag == 1) {
                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        Utils.rightTransaction(ChangePasswordActivity.this);
                        finish();
                    } else {
                        Utils.rightTransaction(ChangePasswordActivity.this);
                        finish();
                    }
                } else {
                    Utils.setToast(getApplicationContext(), RetailerSDKApp.getInstance().dbHelper.getString(R.string.msg_passwordntchanged));
                    Utils.hideProgressDialog();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
            Utils.hideProgressDialog();
        }

        @Override
        public void onComplete() {
            Utils.hideProgressDialog();
        }
    };
}