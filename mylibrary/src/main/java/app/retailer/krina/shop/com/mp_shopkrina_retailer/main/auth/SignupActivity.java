package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.auth;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.android.libraries.places.api.model.Place;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import app.retailer.krina.shop.com.mp_shopkrina_retailer.BuildConfig;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.R;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.api.CommonClassForAPI;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ActivitySignupBinding;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.HomeActivity;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.shoppingCart.ShoppingCartActivity;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.settings.TermOfServicesActivity;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.auth.CustomerAddressModel;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.AddressModel;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.auth.CityModel;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.DocTypeModel;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.postModels.SignupModel;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.auth.CustomerResponse;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.auth.SignupResponse;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.EndPointPref;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.SharePrefs;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Constant;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.LocaleHelper;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Logger;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.MyApplication;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.TextUtils;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils;
import io.reactivex.observers.DisposableObserver;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener {
    private final int REQUST_FOR_ADDRESS = 1001;
    private final int REQUST_FOR_CITY = 1002;

    private ActivitySignupBinding mBinding;

    private Utils utils;
    private CommonClassForAPI commonClassForAPI;
    private Geocoder mGeocoder;
    private AddressModel addressModel;
    private CustomerAddressModel customerAddressModel;

    private ArrayList<CityModel> cityList;
    private String SIGNUPLOC = "", mobile = "", skcode;
    private int cityId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_signup);

        if (getIntent().getExtras() != null) {
            customerAddressModel = (CustomerAddressModel) getIntent().getSerializableExtra("model");
        }
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mGeocoder = new Geocoder(getApplicationContext(), Locale.ENGLISH);

        initialization();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.et_city) {
            onBackPressed();
        } else if (id == R.id.et_address) {
            onBackPressed();
        }else{
            validateFields();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (callSignUpDes != null) {
            callSignUpDes.dispose();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == REQUST_FOR_ADDRESS && resultCode == RESULT_OK) {
                if (data != null) {
                    addressModel = data.getParcelableExtra(Constant.CUSTOMER_ADDRESS);
                    mBinding.etAddress.setText(addressModel.getAddress());
                    Logger.logD("", "AddressData::" + addressModel.getAddress());
                }
            } else if (requestCode == REQUST_FOR_CITY && resultCode == RESULT_OK) {
                if (data != null) {
                    Place place = data.getParcelableExtra("PlaceResult");
                    getPlaceInfo(place.getLatLng().latitude, place.getLatLng().longitude);
                    Logger.logD("", "AddressData::" + place.getAddress());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        utils = new Utils(this);
        commonClassForAPI = CommonClassForAPI.getInstance(this);
        customTextView(mBinding.tvTerms);
        mBinding.btnSignup.setOnClickListener(this);

        mBinding.etFirstNameSignUp.setHint(MyApplication.getInstance().dbHelper.getString(R.string.hint_name));
        mBinding.etShopNameSignUp.setHint(MyApplication.getInstance().dbHelper.getString(R.string.hint_shopname));
        mBinding.etCity.setHint(MyApplication.getInstance().dbHelper.getString(R.string.select_city));
        mBinding.etAddress.setHint(MyApplication.getInstance().dbHelper.getString(R.string.hint_shipping_address));
        mBinding.etGSTSignup.setHint(MyApplication.getInstance().dbHelper.getString(R.string.hint_GSt));
        mBinding.etMobileNo.setHint(MyApplication.getInstance().dbHelper.getString(R.string.hint_mobilenumber));
        mBinding.btnSignup.setHint(MyApplication.getInstance().dbHelper.getString(R.string.hint_signup));
        mBinding.tvTerms.setHint(MyApplication.getInstance().dbHelper.getString(R.string.terms_of_use));

        SIGNUPLOC = SharePrefs.getInstance(getApplicationContext()).getString(SharePrefs.SIGNUPLOC);
        mobile = SharePrefs.getInstance(getApplicationContext()).getString(SharePrefs.MOBILE_NUMBER);
        cityId = SharePrefs.getInstance(getApplicationContext()).getInt(SharePrefs.CITY_ID);
        String cityName = SharePrefs.getInstance(getApplicationContext()).getString(SharePrefs.CITY_NAME);
        skcode = SharePrefs.getInstance(getApplicationContext()).getString(SharePrefs.SK_CODE);
        String shopName = SharePrefs.getInstance(getApplicationContext()).getString(SharePrefs.SHOP_NAME);
        String custName = SharePrefs.getInstance(getApplicationContext()).getString(SharePrefs.CUSTOMER_NAME);

        mBinding.etMobileNo.setText(mobile);
        mBinding.etAddress.setText(customerAddressModel.getAddressText());
        mBinding.etCity.setOnClickListener(this);
        mBinding.etAddress.setOnClickListener(this);
        if (!TextUtils.isNullOrEmpty(cityName)) {
            mBinding.etCity.setText(cityName);
        }
        if (!TextUtils.isNullOrEmpty(custName)) {
            mBinding.etFirstNameSignUp.setText(custName);
        }
        if (!TextUtils.isNullOrEmpty(shopName)) {
            mBinding.etShopNameSignUp.setText(shopName);
        }

        Utils.showProgressDialog(this);
        commonClassForAPI.fetchCity(getCityDes);

        addressModel = new AddressModel(cityId, cityName, customerAddressModel.getAreaText(), "",
                customerAddressModel.getAddressText(),
                customerAddressModel.getAddressLat(),
                customerAddressModel.getAddressLng(),
                customerAddressModel.getZipCode(),
                "",
                customerAddressModel.getAddressText(),
                cityName,
                customerAddressModel.getState(),
                customerAddressModel.getZipCode());
    }

    private void customTextView(TextView view) {
        SpannableString SpanString = new SpannableString(
                "Click here to accept Terms and conditions of Shopkirana Term of services");

        ClickableSpan teremsAndCondition = new ClickableSpan() {
            @Override
            public void onClick(@NotNull View textView) {
                Intent mIntent = new Intent(getApplicationContext(), TermOfServicesActivity.class);
                mIntent.putExtra("isTermsAndCondition", "isTermsAndCondition");
                startActivity(mIntent);
            }
        };

        // Character starting from 32 - 45 is Terms and condition.
        // Character starting from 49 - 63 is privacy policy.
        ClickableSpan privacy = new ClickableSpan() {
            @Override
            public void onClick(@NotNull View textView) {
                Intent mIntent = new Intent(getApplicationContext(), TermOfServicesActivity.class);
                mIntent.putExtra("isTermsAndCondition", "isPrivacyPolicy");
                startActivity(mIntent);
            }
        };

        SpanString.setSpan(teremsAndCondition, 21, 41, 0);
        SpanString.setSpan(privacy, 56, 72, 0);
        SpanString.setSpan(new ForegroundColorSpan(Color.BLUE), 21, 41, 0);
        SpanString.setSpan(new ForegroundColorSpan(Color.BLUE), 56, 72, 0);
        SpanString.setSpan(new UnderlineSpan(), 21, 41, 0);
        SpanString.setSpan(new UnderlineSpan(), 56, 72, 0);

        view.setMovementMethod(LinkMovementMethod.getInstance());
        view.setText(SpanString, TextView.BufferType.SPANNABLE);
        view.setSelected(true);
    }

    private void getPlaceInfo(double lat, double lon) {
        try {
            List<Address> addresses = mGeocoder.getFromLocation(lat, lon, 1);
            if (addresses.get(0).getLocality() != null) {
                String city = addresses.get(0).getLocality();
                mBinding.etCity.setText(city);
                setUpCity(city);
            } else {
                Utils.showProgressDialog(this);
                String url = String.format("https://nominatim.openstreetmap.org/reverse?format=json&lat=%f&lon=%f&zoom=18&addressdetails=1", lat, lon);
                new FetchCityUsingAPI().execute(url);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Utils.showProgressDialog(this);
            String url = String.format("https://nominatim.openstreetmap.org/reverse?format=json&lat=%f&lon=%f&zoom=18&addressdetails=1", lat, lon);
            new FetchCityUsingAPI().execute(url);
        }
    }

    private void setUpCity(String city) {
        if (cityList != null && cityList.size() != 0) {
            for (CityModel model : cityList) {
                if (city.equalsIgnoreCase(model.getCityName())) {
                    cityId = model.getCityid();
                    break;
                }
            }
        }
    }


    private void validateFields() {
        String name = mBinding.etFirstNameSignUp.getText().toString().trim();
        String shopName = mBinding.etShopNameSignUp.getText().toString().trim();
        String shippingAddress = mBinding.etAddress.getText().toString().trim();
        String city = mBinding.etCity.getText().toString().trim();

        if (TextUtils.isNullOrEmpty(mBinding.etFirstNameSignUp.getText().toString().trim())) {
            Utils.setToast(getApplicationContext(), getString(R.string.entername));
        } else if (TextUtils.isNullOrEmpty(mBinding.etShopNameSignUp.getText().toString().trim())) {
            Utils.setToast(getApplicationContext(), getString(R.string.enter_shop_Name));
        } else if (TextUtils.isNullOrEmpty(mBinding.etCity.getText().toString().trim())) {
            Utils.setToast(getApplicationContext(), getString(R.string.select_city_validation));
        } else if (TextUtils.isNullOrEmpty(mBinding.etAddress.getText().toString().trim())) {
            Utils.setToast(getApplicationContext(), getString(R.string.enter_shipping_address));
        } else if (addressModel == null && addressModel.getCityId() == 0) {
            mBinding.etCity.callOnClick();
        } else if (!mBinding.checkbox.isChecked()) {
            Utils.setToast(getApplicationContext(), MyApplication.getInstance().dbHelper.getString(R.string.check_terms_and_condition));
        } else if (utils.isNetworkAvailable()) {
            // API call SignUp
            SignupModel signupModel = new SignupModel(SharePrefs.getInstance(getApplicationContext()).getInt(SharePrefs.CUSTOMER_ID),
                    mobile, name, shopName, shippingAddress,
                    addressModel == null ? "" : addressModel.getAreaName(),
                    "123456", cityId, skcode, Constant.VERSION_NAME, Build.VERSION.RELEASE,
                    Build.MODEL, Utils.getDeviceUniqueID(this),
                    Utils.getDeviceUniqueID(this), city, addressModel == null ? 0 : addressModel.getLatitude(),
                    addressModel == null ? 0 : addressModel.getLongitude(), EndPointPref.getInstance(getApplicationContext()).getFcmToken(EndPointPref.FCM_TOKEN),
                    addressModel == null ? "" : addressModel.getLandmark(), addressModel == null ? "" : addressModel.getPincode(),
                    customerAddressModel);
            Utils.showProgressDialog(this);
            commonClassForAPI.fetchSignupData(callSignUpDes, signupModel);
        } else {
            Utils.setToast(getApplicationContext(), MyApplication.getInstance().dbHelper.getString(R.string.internet_connection));
        }
    }


    @SuppressWarnings("deprecation")
    @SuppressLint("StaticFieldLeak")
    private class FetchCityUsingAPI extends AsyncTask<String, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(String... strings) {
            JSONObject resultJson = null;
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream stream = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));
                StringBuilder builder = new StringBuilder();

                String inputString;
                while ((inputString = bufferedReader.readLine()) != null) {
                    builder.append(inputString);
                }
                JSONObject topLevel = new JSONObject(builder.toString());
                resultJson = topLevel.getJSONObject("address");

                urlConnection.disconnect();
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return resultJson;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            try {
                Utils.hideProgressDialog();
                if (jsonObject.has("city")) {
                    String city = jsonObject.getString("city");
                    mBinding.etCity.setText(city);
                    setUpCity(city);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    // get Customer Doc Type
    DisposableObserver<ArrayList<DocTypeModel>> docTypeObserver = new DisposableObserver<ArrayList<DocTypeModel>>() {
        @Override
        public void onNext(@NotNull ArrayList<DocTypeModel> list) {
//            docTypeList = list;
//            docTypeList.add(0, new DocTypeModel(0, "Select Document Type"));
//            docTypeAdapter = new DocTypeAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, docTypeList);
//            mBinding.spDocType.setAdapter(docTypeAdapter);
//            for (int i = 0; i < docTypeList.size(); i++) {
//                if (docTypeList.get(i).docType.equalsIgnoreCase("gst")) {
//                    mBinding.spDocType.setSelection(i);
//                    break;
//                }
//            }
        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
        }

        @Override
        public void onComplete() {

        }
    };

    // Forth city getting
    DisposableObserver<ArrayList<CityModel>> getCityDes = new DisposableObserver<ArrayList<CityModel>>() {
        @Override
        public void onNext(@NotNull ArrayList<CityModel> list) {
            Utils.hideProgressDialog();
            cityList = list;
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

    // Response sign up
    DisposableObserver<SignupResponse> callSignUpDes = new DisposableObserver<SignupResponse>() {
        @Override
        public void onNext(@NotNull SignupResponse model) {
            Utils.hideProgressDialog();
            if (model.isStatus()) {
                Utils.setToast(getApplicationContext(), getResources().getString(R.string.signup_successfully_done));
                CustomerResponse customer = model.customers;
                SharePrefs.getInstance(getApplicationContext()).putInt(SharePrefs.CUSTOMER_ID, customer.customerId);
                SharePrefs.getInstance(getApplicationContext()).putString(SharePrefs.SK_CODE, customer.getSkcode());
                SharePrefs.getInstance(getApplicationContext()).putString(SharePrefs.CUSTOMER_NAME, customer.getName());
                SharePrefs.getInstance(getApplicationContext()).putString(SharePrefs.CUSTOMER_EMAIL, customer.getEmailid());
                SharePrefs.getInstance(getApplicationContext()).putString(SharePrefs.MOBILE_NUMBER, customer.getMobile());
                SharePrefs.getInstance(getApplicationContext()).putString(SharePrefs.SHOP_NAME, customer.getShopName());
                SharePrefs.getInstance(getApplicationContext()).putInt(SharePrefs.COMPANY_ID, customer.getCompanyId());
                SharePrefs.getInstance(getApplicationContext()).putString(SharePrefs.CUSTOMER_TYPE, "");
                SharePrefs.getInstance(getApplicationContext()).putString(SharePrefs.SHIPPING_ADDRESS, customer.getShippingAddress());
                SharePrefs.getInstance(getApplicationContext()).putInt(SharePrefs.WAREHOUSE_ID, customer.getWarehouseid());
                SharePrefs.getInstance(getApplicationContext()).putBoolean(SharePrefs.CUST_ACTIVE, Boolean.valueOf(customer.getActive()));
                SharePrefs.getInstance(getApplicationContext()).putInt(SharePrefs.CITY_ID, customer.getCityid());
                SharePrefs.getInstance(getApplicationContext()).putString(SharePrefs.CITY_NAME, customer.getCity());
                SharePrefs.getInstance(getApplicationContext()).putString(SharePrefs.USER_PROFILE_IMAGE, customer.getUploadProfilePichure());
                SharePrefs.getInstance(getApplicationContext()).putBoolean(SharePrefs.IS_SIGN_UP, customer.isSignup());
                SharePrefs.getInstance(getApplicationContext()).putString(SharePrefs.PASSWORD, customer.getPassword());
                SharePrefs.getInstance(getApplicationContext()).putString(SharePrefs.LICENSE_NO, customer.getLicenseNumber());
                SharePrefs.getInstance(getApplicationContext()).putString(SharePrefs.GST_NO, customer.getRefNo());
                SharePrefs.getInstance(getApplicationContext()).putString(SharePrefs.LATITUDE, customer.lat);
                SharePrefs.getInstance(getApplicationContext()).putString(SharePrefs.LONGITUDE, customer.lg);
                SharePrefs.getInstance(getApplicationContext()).putString(SharePrefs.CLUSTER_ID, customer.getClusterId());
                SharePrefs.getInstance(getApplicationContext()).putString(SharePrefs.CUSTOMER_VERIFY, customer.getCustomerVerify());
                SharePrefs.getInstance(getApplicationContext()).putBoolean(SharePrefs.IS_PRIME_MEMBER, customer.isPrimeCustomer);
                SharePrefs.getInstance(getApplicationContext()).putString(SharePrefs.PRIME_EXPIRY, customer.primeEndDate);
                SharePrefs.getInstance(getApplicationContext()).putBoolean(SharePrefs.IsKPP, customer.isKPP());
                SharePrefs.getInstance(getApplicationContext()).putBoolean(SharePrefs.IS_COMPANY_API_CALL, true);
                SharePrefs.getInstance(getApplicationContext()).putBoolean(SharePrefs.IS_FAV_API_CALL, true);
                SharePrefs.getInstance(getApplication()).putBoolean(SharePrefs.CART_AMOUNT_API_CALL, false);
                Intent intent;
                if (SIGNUPLOC.equalsIgnoreCase("HOME")) {
                    intent = new Intent(getApplicationContext(), HomeActivity.class);
                } else if (SIGNUPLOC.equalsIgnoreCase("PAYMENT")) {
                    intent = new Intent(getApplicationContext(), ShoppingCartActivity.class);
                } else {
                    intent = new Intent(getApplicationContext(), HomeActivity.class);
                }
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            } else {
                Utils.setToast(getApplicationContext(), model.getMessage());
                finish();
            }

        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
            callSignUpDes.dispose();
            Utils.hideProgressDialog();
        }

        @Override
        public void onComplete() {
            Utils.hideProgressDialog();
        }
    };
}