package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.auth;

import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AddressComponent;
import com.google.android.libraries.places.api.model.Place;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import app.retailer.krina.shop.com.mp_shopkrina_retailer.R;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.api.CommonClassForAPI;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ActivityPlacesSearchBinding;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.HomeActivity;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.model.AddressModel;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.auth.EditProfileModel;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.postModels.LatLongModel;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.auth.CustomerResponse;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.auth.MyProfileResponse;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.SharePrefs;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.auth.CustomSearchPlaceActivity;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Constant;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.MyApplication;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.TextUtils;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils;
import io.reactivex.observers.DisposableObserver;

public class PlacesSearchActivity extends AppCompatActivity {
    private final int REQUEST_FOR_ADDRESS = 1001;

    private ActivityPlacesSearchBinding mBinding;

    private Place place;
    private Geocoder mGeocoder;
    private Utils utils;
    private CommonClassForAPI commonClassForAPI;

    private String address = "", areaName = "", city = "";
    private String intentCityName = "", zipcode = "", flateOrFloorNumber = "", landmark = "";
    private final int selectCityId = 0;
    private int REDIRECT_FLAG;
    private int custId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_places_search);

        mBinding.etSearchKeyword.setText(MyApplication.getInstance().dbHelper.getString(R.string.title_serach_address));
        mBinding.address.setHint(MyApplication.getInstance().dbHelper.getString(R.string.search_delivery_address));
        mBinding.city.setHint(MyApplication.getInstance().dbHelper.getString(R.string.city));
        mBinding.state.setHint(MyApplication.getInstance().dbHelper.getString(R.string.state_astrick));
        mBinding.pincode.setHint(MyApplication.getInstance().dbHelper.getString(R.string.pin_code));
        mBinding.flateOrFloorNumber.setHint(MyApplication.getInstance().dbHelper.getString(R.string.address_field_number));
        mBinding.landmark.setHint(MyApplication.getInstance().dbHelper.getString(R.string.landmark_optional));
        mBinding.btnSave.setHint(MyApplication.getInstance().dbHelper.getString(R.string.save));

        mGeocoder = new Geocoder(getApplicationContext(), Locale.ENGLISH);
        if (getIntent() != null) {
            REDIRECT_FLAG = getIntent().getIntExtra("REDIRECT_FLAG", 0);
            intentCityName = getIntent().getStringExtra("cityName");
            if (REDIRECT_FLAG != 1) {
                EditProfileModel editProfileModel = (EditProfileModel) getIntent().getSerializableExtra("CUSTOMER_DETAILS");
                address = editProfileModel.getShippingAddress();
//                IsVerified = editProfileModel.getCustomerVerify();
                String pinCode = editProfileModel.getZipCode();
                flateOrFloorNumber = editProfileModel.getShippingAddress1();
                landmark = editProfileModel.getLandMark();
                if (!TextUtils.isNullOrEmpty(address)) {
                    mBinding.etAddress.setText(address);
                }
                if (!TextUtils.isNullOrEmpty(pinCode)) {
                    mBinding.etPincode.setText(pinCode);
                }
                if (!TextUtils.isNullOrEmpty(flateOrFloorNumber)) {
                    mBinding.etFlateOrFloorNumber.setText(flateOrFloorNumber);
                }
                if (!TextUtils.isNullOrEmpty(landmark)) {
                    mBinding.etLandmark.setText(landmark);
                }
                String isVerified = SharePrefs.getInstance(getApplicationContext()).getString(SharePrefs.CUSTOMER_VERIFY);
                if (!TextUtils.isNullOrEmpty(isVerified)) {
                    if (isVerified.equalsIgnoreCase("Full Verified")) {
                        mBinding.etAddress.setEnabled(false);
                        mBinding.etCity.setEnabled(false);
                        mBinding.etFlateOrFloorNumber.setEnabled(false);
                        mBinding.etPincode.setEnabled(false);
                        mBinding.etLandmark.setEnabled(false);
                    }
                }
            }
            mBinding.etCity.setText(intentCityName);
        }

        init();
        //get city
        if (REDIRECT_FLAG != 1) {
            mBinding.lerCitySelection.setVisibility(View.GONE);
        }

        mBinding.btnSave.setOnClickListener(view -> {
            try {
                address = mBinding.etAddress.getText().toString();
                city = mBinding.etCity.getText().toString();
                String state = mBinding.etState.getText().toString();
                flateOrFloorNumber = mBinding.etFlateOrFloorNumber.getText().toString();
                landmark = mBinding.etLandmark.getText().toString();
                if (!TextUtils.isNullOrEmpty(mBinding.etPincode.getText().toString())) {
                    zipcode = mBinding.etPincode.getText().toString();
                }
                if (TextUtils.isNullOrEmpty(address)) {
                    Utils.setToast(getApplicationContext(), MyApplication.getInstance().dbHelper.getString(R.string.enter_delivery_address));
                } else if (TextUtils.isNullOrEmpty(city) && REDIRECT_FLAG == 1) {
                    Utils.setToast(getApplicationContext(), MyApplication.getInstance().dbHelper.getString(R.string.select_city));
                } else if (TextUtils.isNullOrEmpty(zipcode)) {
                    Utils.setToast(getApplicationContext(), MyApplication.getInstance().dbHelper.getString(R.string.valid_pincode_number));
                } else if (zipcode.length() < 6) {
                    Utils.setToast(getApplicationContext(), MyApplication.getInstance().dbHelper.getString(R.string.valid_pincode_number));
                } else {
                    if (REDIRECT_FLAG == 3) {
                        if (utils.isNetworkAvailable()) {
                            if (commonClassForAPI != null) {
                                SharePrefs.getInstance(getApplicationContext()).putString(SharePrefs.SHIPPING_ADDRESS, address);
                                Utils.showProgressDialog(this);
                                commonClassForAPI.updateLatLong(updateLatLnDes, new LatLongModel(custId, place.getLatLng().latitude,
                                        place.getLatLng().longitude, address, flateOrFloorNumber), "Place API");
                            }
                        } else {
                            Utils.setToast(getApplicationContext(), MyApplication.getInstance().dbHelper.getString(R.string.internet_connection));
                        }
                    } else {
//                        validatePincode(intentCityName, zipcode);
                        AddressModel model = new AddressModel(selectCityId, areaName, city, landmark, address, place.getLatLng().latitude,
                                place.getLatLng().longitude, zipcode, flateOrFloorNumber, "", "", "", "");
                        Intent intent = new Intent();
                        intent.putExtra(Constant.CUSTOMER_ADDRESS, model);
                        setResult(Activity.RESULT_OK, intent);
                        Utils.rightTransaction(PlacesSearchActivity.this);
                        finish();
                    }
                }
            } catch (Exception e) {
                Utils.setToast(getApplicationContext(), MyApplication.getInstance().dbHelper.getString(R.string.not_getting_proper_address));
                e.printStackTrace();
            }
        });
        mBinding.back.setOnClickListener(view -> onBackPressed());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Utils.rightTransaction(this);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_FOR_ADDRESS & resultCode == RESULT_OK) {
            place = data.getParcelableExtra("PlaceResult");
            address = data.getStringExtra("Address");
            mBinding.etAddress.setText(address);
            try {
                if (place.getAddressComponents() != null)
                    for (AddressComponent component : place.getAddressComponents().asList()) {
                        if (component.getTypes().get(0).equals("postal_code")) {
                            mBinding.etPincode.setText(component.getShortName());
                            break;
                        }
                    }
                if (TextUtils.isNullOrEmpty(mBinding.etPincode.getText().toString())) {
                    mBinding.etPincode.setFocusable(true);
                    mBinding.etPincode.setFocusableInTouchMode(true);
                    getAddressDetails();
                } else {
                    mBinding.etPincode.setFocusable(false);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private void init() {
        utils = new Utils(this);
        commonClassForAPI = CommonClassForAPI.getInstance(this);
        custId = SharePrefs.getInstance(getApplicationContext()).getInt(SharePrefs.CUSTOMER_ID);
        mBinding.etAddress.setOnClickListener(view -> findPlace());
    }

    private void findPlace() {
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), getResources().getString(R.string.google_maps_key), Locale.ENGLISH);
        }
        startActivityForResult(new Intent(getApplicationContext(), CustomSearchPlaceActivity.class)
                .putExtra("cityname", intentCityName)
                .putExtra("searchCity", false), REQUEST_FOR_ADDRESS);
        Utils.leftTransaction(this);
    }

    private void getAddressDetails() {
        try {
            LatLng latLng = place.getLatLng();
            List<Address> addresses = mGeocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            if (addresses.get(0).getLocality() != null) {
                String state = addresses.get(0).getAdminArea();
                String pin = addresses.get(0).getPostalCode();
                String tempCity = addresses.get(0).getLocality();
                areaName = addresses.get(0).getSubLocality();
                mBinding.etPincode.setText(pin);
                mBinding.etCity.setText(intentCityName);
                mBinding.etState.setText(state);
                if (TextUtils.isNullOrEmpty(mBinding.etPincode.getText().toString())) {
                    mBinding.etPincode.setFocusable(true);
                    mBinding.etPincode.setFocusableInTouchMode(true);
                } else {
                    mBinding.etPincode.setFocusable(false);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private DisposableObserver<MyProfileResponse> updateLatLnDes = new DisposableObserver<MyProfileResponse>() {
        @Override
        public void onNext(@NotNull MyProfileResponse response) {
            try {
                Utils.hideProgressDialog();
                if (!response.isStatus()) {
                    Utils.setToast(getApplicationContext(), response.getMessage());
                } else {
                    if (response.getCustomers() != null) {
                        CustomerResponse customer = response.getCustomers();
                        Utils.setToast(getApplicationContext(), MyApplication.getInstance().dbHelper.getString(R.string.succesfullSubmitted));
                        SharePrefs.getInstance(getApplicationContext()).putInt(SharePrefs.CUSTOMER_ID, customer.customerId);
                        SharePrefs.getInstance(getApplicationContext()).putString(SharePrefs.SK_CODE, customer.getSkcode());
                        SharePrefs.getInstance(getApplicationContext()).putString(SharePrefs.MOBILE_NUMBER, customer.getMobile());
                        SharePrefs.getInstance(getApplicationContext()).putInt(SharePrefs.COMPANY_ID, customer.getCompanyId());
                        SharePrefs.getInstance(getApplicationContext()).putInt(SharePrefs.WAREHOUSE_ID, customer.getWarehouseid());
                        SharePrefs.getInstance(getApplicationContext()).putString(SharePrefs.LATITUDE, customer.lat);
                        SharePrefs.getInstance(getApplicationContext()).putString(SharePrefs.LONGITUDE, customer.lg);
                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        finish();
                        Utils.rightTransaction(PlacesSearchActivity.this);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
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