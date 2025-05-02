package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.gson.JsonObject;

import java.util.Calendar;

import app.retailer.krina.shop.com.mp_shopkrina_retailer.R;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.api.CommonClassForAPI;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ActivityEpayPartnerBinding;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.postModels.EPayPartnerModel;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.SharePrefs;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.RetailerSDKApp;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.TextUtils;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils;
import io.reactivex.observers.DisposableObserver;

@SuppressWarnings("ConstantConditions")
public class EPayPartnerActivity extends AppCompatActivity implements View.OnClickListener {
    private ActivityEpayPartnerBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityEpayPartnerBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot())   ;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mBinding.etcountry.setText("India");
        mBinding.etcity.setText(SharePrefs.getInstance(this).getString(SharePrefs.CITY_NAME));

        mBinding.etDob.setOnClickListener(this);
        mBinding.btnSubmit.setOnClickListener(this);

        mBinding.proprietorFN.setHint(RetailerSDKApp.getInstance().dbHelper.getString(R.string.proprietor_first_name));
        mBinding.proprietorLN.setHint(RetailerSDKApp.getInstance().dbHelper.getString(R.string.proprietor_last_name));
        mBinding.mobNo.setHint(RetailerSDKApp.getInstance().dbHelper.getString(R.string.mobile_number));
        mBinding.whatsAppNo.setHint(RetailerSDKApp.getInstance().dbHelper.getString(R.string.txt_cust_whatsApp_number));
        mBinding.email.setHint(RetailerSDKApp.getInstance().dbHelper.getString(R.string.email));
        mBinding.dob.setHint(RetailerSDKApp.getInstance().dbHelper.getString(R.string.dob));
        mBinding.panNo.setHint(RetailerSDKApp.getInstance().dbHelper.getString(R.string.pannumber));
        mBinding.country.setHint(RetailerSDKApp.getInstance().dbHelper.getString(R.string.country));
        mBinding.state.setHint(RetailerSDKApp.getInstance().dbHelper.getString(R.string.state));
        mBinding.city.setHint(RetailerSDKApp.getInstance().dbHelper.getString(R.string.city));
        mBinding.zipCode.setHint(RetailerSDKApp.getInstance().dbHelper.getString(R.string.pincode));
        mBinding.tvAllMandatory.setHint(RetailerSDKApp.getInstance().dbHelper.getString(R.string.all_partner_details_are_mandatory));

        mBinding.btnSubmit.setHint(RetailerSDKApp.getInstance().dbHelper.getString(R.string.submit));
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.et_dob) {
            clickDOB();
        } else if (id == R.id.btn_partner1) {
            startActivity(new Intent(getApplicationContext(), EPayPartnerActivity.class));
        } else if (id == R.id.btn_partner1) {
            startActivity(new Intent(getApplicationContext(), EPayPartnerActivity.class));
        } else if (id == R.id.btn_partner2) {
            startActivity(new Intent(getApplicationContext(), EPayPartnerActivity.class));
        } else if (id == R.id.btn_partner3) {
            startActivity(new Intent(getApplicationContext(), EPayPartnerActivity.class));
        } else if (id == R.id.btn_partner4) {
            startActivity(new Intent(getApplicationContext(), EPayPartnerActivity.class));
        } else if (id == R.id.btn_submit) {
            checkFormData();
        }
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


    private void checkFormData() {
        mBinding.proprietorFN.setError(null);
        mBinding.proprietorLN.setError(null);
        mBinding.mobNo.setError(null);
        mBinding.whatsAppNo.setError(null);
        mBinding.email.setError(null);
        mBinding.dob.setError(null);
        mBinding.panNo.setError(null);
        mBinding.country.setError(null);
        mBinding.state.setError(null);
        mBinding.city.setError(null);
        mBinding.zipCode.setError(null);

        String proprietorFN = mBinding.etproprietorFN.getText().toString();
        String proprietorLN = mBinding.etproprietorLN.getText().toString();
        String mobNo = mBinding.etmobNo.getText().toString();
        String whatsAppNo = mBinding.etWhatsAppNo.getText().toString();
        String email = mBinding.etemail.getText().toString();
        String dob = mBinding.etDob.getText().toString();
        String panNo = mBinding.etpanNo.getText().toString();
        String country = mBinding.etcountry.getText().toString();
        String state = mBinding.etstate.getText().toString();
        String city = mBinding.etcity.getText().toString();
        String zipCode = mBinding.etzipCode.getText().toString();

        if (TextUtils.isNullOrEmpty(proprietorFN)) {
            mBinding.proprietorFN.setError("Enter proprietor First Name");
            mBinding.proprietorFN.requestFocus();
        } else if (TextUtils.isNullOrEmpty(proprietorLN)) {
            mBinding.proprietorLN.setError("Enter proprietor Last Name");
            mBinding.proprietorLN.requestFocus();
        } else if (TextUtils.isNullOrEmpty(mobNo)) {
            mBinding.mobNo.setError("Enter Mobile");
            mBinding.mobNo.requestFocus();
        } else if (!TextUtils.isValidMobileNo(mobNo)) {
            mBinding.mobNo.setError("Enter Valid Mobile Number");
            mBinding.mobNo.requestFocus();
        } else if (TextUtils.isNullOrEmpty(whatsAppNo)) {
            mBinding.whatsAppNo.setError("Enter WhatsApp Number");
            mBinding.whatsAppNo.requestFocus();
        } else if (!TextUtils.isValidMobileNo(whatsAppNo)) {
            mBinding.whatsAppNo.setError("Enter Valid WhatsApp Number");
            mBinding.whatsAppNo.requestFocus();
        } else if (TextUtils.isNullOrEmpty(email)) {
            mBinding.email.setError("Enter Email");
            mBinding.email.requestFocus();
        } else if (!TextUtils.isValidEmail(email)) {
            mBinding.email.setError("Enter Valid Email Address");
            mBinding.email.requestFocus();
        } else if (TextUtils.isNullOrEmpty(dob)) {
            mBinding.dob.setError("Enter DOB");
            mBinding.dob.requestFocus();
        } else if (TextUtils.isNullOrEmpty(panNo)) {
            mBinding.panNo.setError("Enter PAN Number");
            mBinding.panNo.requestFocus();
        } else if (TextUtils.isNullOrEmpty(country)) {
            mBinding.country.setError("Enter Country");
            mBinding.country.requestFocus();
        } else if (TextUtils.isNullOrEmpty(state)) {
            mBinding.state.setError("Enter State");
            mBinding.state.requestFocus();
        } else if (TextUtils.isNullOrEmpty(city)) {
            mBinding.city.setError("Enter City");
            mBinding.city.requestFocus();
        } else if (TextUtils.isNullOrEmpty(zipCode)) {
            mBinding.zipCode.setError("Enter Postel Code");
            mBinding.zipCode.requestFocus();
        } else {
            EPayPartnerModel model = new EPayPartnerModel(SharePrefs.getInstance(this).getInt(SharePrefs.CUSTOMER_ID),
                    proprietorFN, proprietorLN, mBinding.spGender.getSelectedItem().toString(), mobNo, whatsAppNo, email, dob, panNo, country, state, city, zipCode);
            Utils.showProgressDialog(this);
            CommonClassForAPI.getInstance(this).addEPayaterPartnerInfo(ePayPartnerObserver, model);
        }
    }

    private void clickDOB() {
        try {
            Calendar c = Calendar.getInstance();
            int mYear = c.get(Calendar.YEAR);
            int mMonth = c.get(Calendar.MONTH);
            int mDay = c.get(Calendar.DAY_OF_MONTH);
            System.out.println("the selected" + mDay);
            DatePickerDialog dialog = new DatePickerDialog(this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, new mDateSetListenerForDob(), mYear, mMonth, mDay);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setButton(DialogInterface.BUTTON_NEGATIVE, RetailerSDKApp.getInstance().dbHelper.getString(R.string.cancel), (dialog1, which) -> {
                mBinding.etDob.setText("");
            });
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    class mDateSetListenerForDob implements DatePickerDialog.OnDateSetListener {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {
            try {
                String date = String.valueOf(new StringBuilder()
                        .append(day).append("/").append(month + 1).append("/")
                        .append(year));
                mBinding.etDob.setText(year + "-" + Utils.getDateDashFormat(date));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    DisposableObserver<JsonObject> ePayFormObserver = new DisposableObserver<JsonObject>() {
        @Override
        public void onNext(JsonObject object) {
            Utils.hideProgressDialog();
            try {
                if (object.get("Status").getAsBoolean()) {
                    onBackPressed();
                }
                Utils.setToast(getApplicationContext(), object.get("Message").getAsString());
            } catch (Exception e) {
                e.printStackTrace();

            }
        }

        @Override
        public void onError(Throwable e) {
            Utils.hideProgressDialog();
            e.printStackTrace();
            dispose();
        }

        @Override
        public void onComplete() {
            Utils.hideProgressDialog();
        }
    };

    DisposableObserver<JsonObject> ePayPartnerObserver = new DisposableObserver<JsonObject>() {
        @Override
        public void onNext(JsonObject object) {
            try {
                if (object.get("Status").getAsBoolean()) {
                    Intent intent = new Intent();
                    setResult(Activity.RESULT_OK, intent);
                    onBackPressed();
                }
                Utils.setToast(getApplicationContext(), object.get("Message").getAsString());
            } catch (Exception e) {
                e.printStackTrace();

            }
        }

        @Override
        public void onError(Throwable e) {
            Utils.hideProgressDialog();
            e.printStackTrace();
            if (ePayPartnerObserver != null) {
                ePayPartnerObserver.dispose();
            }
        }

        @Override
        public void onComplete() {
            Utils.hideProgressDialog();
        }
    };
}