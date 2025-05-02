package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.auth;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputLayout;
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;

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
import java.util.Timer;
import java.util.TimerTask;

import app.retailer.krina.shop.com.mp_shopkrina_retailer.R;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.api.CommonClassForAPI;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ActivityUpdateAddressBinding;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.auth.CityModel;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.auth.TokenResponse;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.auth.NewSignupRequest;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.auth.CustomerResponse;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.auth.SignupResponse;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.auth.UserAuth;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.EndPointPref;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.SharePrefs;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.auth.CustomSearchPlaceActivity;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.auth.NewSignupActivity;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Constant;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Logger;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.MarshmallowPermissions;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.RetailerSDKApp;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.TextUtils;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils;
import io.reactivex.observers.DisposableObserver;

public class UpdateAddressActivity extends AppCompatActivity implements LocationListener,
        GoogleApiClient.ConnectionCallbacks, OnMapReadyCallback {

    private final int REQUEST_LOCATION = 49, REQUEST_FOR_CITY = 1002;
    private ActivityUpdateAddressBinding mBinding;

    protected GoogleApiClient mGoogleApiClient;
    private LocationRequest locationRequest;
    private Location location;
    private GoogleMap mMap;
    private Geocoder mGeocoder;

    private CommonClassForAPI commonClassForAPI;

    private ArrayList<CityModel> cityList;
    private String address, address1, shopName, areaName, landmark, city = "", state, zipCode, houseNo = "";
    private int selectCityId = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_update_address);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setFastestInterval(100);
        locationRequest.setInterval(1000);
        mGeocoder = new Geocoder(getApplicationContext(), Locale.ENGLISH);

        commonClassForAPI = CommonClassForAPI.getInstance(this);

        mBinding.btnBack.setOnClickListener(v -> {
            onBackPressed();
        });
        mBinding.btnChange.setOnClickListener(v -> {
            startActivityForResult(new Intent(getApplicationContext(), CustomSearchPlaceActivity.class)
                    .putExtra("cityname", "")
                    .putExtra("searchCity", false), REQUEST_FOR_CITY);
        });
        mBinding.btnConfirm.setOnClickListener(v -> {
            if (MarshmallowPermissions.isPermissionAllowed(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                showAddressDetailDialog();
            } else {
                String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
                Permissions.check(this, permissions, null, null, new PermissionHandler() {
                    @Override
                    public void onGranted() {
                    }

                    @Override
                    public void onDenied(Context context, ArrayList<String> deniedPermissions) {
                        super.onDenied(context, deniedPermissions);
                    }
                });
            }
        });

        Utils.showProgressDialog(this);
        commonClassForAPI.fetchCity(getCityDes);

        MarshmallowPermissions.requestPermission(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_LOCATION);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected())
            mGoogleApiClient.disconnect();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 909) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    // All required changes were successfully made
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(),
                            Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                            locationRequest, UpdateAddressActivity.this);
                    if (location == null) {
                        location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                    }
                    if (location != null) {
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(),
                                location.getLongitude()), 18));
                        generateLocationAddress();
                    } else {
                        getPreciseLocation();
                    }
                    break;
                case Activity.RESULT_CANCELED:
                    // The user was asked to change settings, but chose not to
                    Toast.makeText(getApplicationContext(), RetailerSDKApp.getInstance().dbHelper.getString(R.string.allow_location_access), Toast.LENGTH_LONG).show();
                    mGoogleApiClient.disconnect();
                    break;
                default:
                    break;
            }
        } else if (requestCode == REQUEST_FOR_CITY && resultCode == RESULT_OK) {
            if (data != null) {
                Place place = data.getParcelableExtra("PlaceResult");
                getPlaceInfo(place.getLatLng().latitude, place.getLatLng().longitude);
                Logger.logD("", "AddressData::" + place.getAddress());
                mBinding.tvAddress.setText(place.getAddress());
                address = place.getAddress();
                if (location == null) {
                    location = new Location("map");
                }
                location.setLatitude(place.getLatLng().latitude);
                location.setLongitude(place.getLatLng().longitude);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(),
                        location.getLongitude()), 18));
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
                mMap.setOnCameraIdleListener(() -> {
                    if (location == null) {
                        location = new Location("map");
                    }
                    location.setLatitude(mMap.getCameraPosition().target.latitude);
                    location.setLongitude(mMap.getCameraPosition().target.longitude);
                    generateLocationAddress();
                    mMap.setOnCameraMoveStartedListener(i -> mBinding.liDrag.setVisibility(View.INVISIBLE));
                });
                mMap.setMyLocationEnabled(true);
                requestLocation();
            } else {
                Utils.setToast(getApplicationContext(), "Location permission is required!");
            }
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        try {
            mMap = googleMap;
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap.setMyLocationEnabled(true);
            mMap.setOnCameraIdleListener(() -> {
                if (location == null) {
                    location = new Location("map");
                }
                location.setLatitude(mMap.getCameraPosition().target.latitude);
                location.setLongitude(mMap.getCameraPosition().target.longitude);
                generateLocationAddress();
                mMap.setOnCameraMoveStartedListener(i -> mBinding.liDrag.setVisibility(View.INVISIBLE));
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true); // this is the key ingredient

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi
                .checkLocationSettings(mGoogleApiClient, builder.build());
        result.setResultCallback(result1 -> {
            Status status = result1.getStatus();
            switch (status.getStatusCode()) {
                case LocationSettingsStatusCodes.SUCCESS:
                    // All location settings are satisfied. The client can
                    // initialize location requests here.
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(UpdateAddressActivity.this,
                            Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                            locationRequest, UpdateAddressActivity.this);
                    location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                    if (location != null) {
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(),
                                location.getLongitude()), 18));
                        generateLocationAddress();
                    } else {
                        getPreciseLocation();
                    }
                    break;
                case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                    // Location settings are not satisfied. But could be
                    // fixed by showing the user a dialog.
                    try {
                        // Show the dialog by calling startResolutionForResult(),
                        // and check the result in onActivityResult().
                        status.startResolutionForResult(UpdateAddressActivity.this, 909);
                    } catch (IntentSender.SendIntentException e) {
                        e.printStackTrace();
                    }
                    break;
                case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                    // Location settings are not satisfied. However, we have
                    // no way to fix the settings so we won't show the dialog.
                    break;
            }
        });
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
//        this.location = location;
//        mMap.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude())));
    }


    private void requestLocation() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true); // this is the key ingredient

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi
                .checkLocationSettings(mGoogleApiClient, builder.build());
        result.setResultCallback(result1 -> {
            Status status = result1.getStatus();
            switch (status.getStatusCode()) {
                case LocationSettingsStatusCodes.SUCCESS:
                    // All location settings are satisfied. The client can
                    // initialize location requests here.
                    if (ActivityCompat.checkSelfPermission(UpdateAddressActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(UpdateAddressActivity.this,
                            Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                            locationRequest, UpdateAddressActivity.this);
                    location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                    if (location != null) {
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(),
                                location.getLongitude()), 18));
                        generateLocationAddress();
                    } else {
                        getPreciseLocation();
                    }
                    break;
                case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                    // Location settings are not satisfied. But could be fixed by showing the user a dialog.
                    try {
                        // Show the dialog by calling startResolutionForResult(),
                        // and check the result in onActivityResult().
                        status.startResolutionForResult(UpdateAddressActivity.this, REQUEST_LOCATION);
                    } catch (IntentSender.SendIntentException e) {
                        e.printStackTrace();
                    }
                    break;
                case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                    // Location settings are not satisfied. However, we have
                    // no way to fix the settings so we won't show the dialog.
                    break;
            }
        });
    }

    private void generateLocationAddress() {

        class GenerateAddress extends AsyncTask<Void, Void, Void> {
            private List<Address> addresses = null;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                mBinding.btnConfirm.setEnabled(false);
            }

            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    if (location != null) {
                        addresses = mGeocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 5);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                try {
                    String area = "", address = "", address_new = "";
                    // Handle case where no address was found.
                    if (addresses == null || addresses.size() == 0) {
                        address = "";
                    } else {
                        Address address1 = addresses.get(0);
                        // Fetch the address lines using getAddressLine,
                        // join them, and send them to the thread.
                        System.out.println(address1.getSubAdminArea());
                        System.out.println(address1.getSubLocality());
                        System.out.println(address1.getFeatureName());
                        System.out.println(address1.getSubThoroughfare());
                        System.out.println(address1.getThoroughfare());
                        System.out.println(address1.getPremises());
                        if (address1.getSubLocality() != null) {
                            area = address1.getSubLocality();
                            address_new = address1.getFeatureName() + ", " + address1.getSubLocality();
                        } else {
                            area = address1.getLocality();
                            address_new = address1.getFeatureName() + ", " + address1.getLocality();
                        }

                        Log.d("CompleteAddress", "" + address_new);

                        for (int i = 0; i <= address1.getMaxAddressLineIndex(); i++) {
                            if (i == 0)
                                address = address1.getAddressLine(i);
                        }
                    }
                    Address address1 = null;
                    try {
                        assert addresses != null;
                        address1 = addresses.get(0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    System.out.println(address + "+ " + address1);
                    areaName = addresses.get(0).getSubLocality();
                    city = addresses.get(0).getLocality();
                    state = addresses.get(0).getAdminArea();
                    String pin = addresses.get(0).getPostalCode();
                    if (city == null) {
                        city = addresses.get(0).getSubAdminArea();
                    }
                    if (city == null) {
                        city = "";
                    }
                    mBinding.tvArea.setText("" + city);
                    mBinding.tvAddress.setText("" + address);
                    mBinding.tvDelivery.setVisibility(View.VISIBLE);
                    mBinding.tvDelivery.setText("Your Order will be delivered here");
                    UpdateAddressActivity.this.address = address;

                } catch (Exception e) {
                    e.printStackTrace();
                }
                mBinding.btnConfirm.setEnabled(true);
            }
        }
        new GenerateAddress().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void getPreciseLocation() {
        final Timer timer = new Timer();

        TimerTask timerTask1 = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> {
                    if (location != null) {
                        timer.cancel();
                        if (Utils.isNetworkAvailable(getApplicationContext())) {
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(),
                                    location.getLongitude()), 18));
                        }
                    }
                });
            }
        };
        timer.scheduleAtFixedRate(timerTask1, 500, 5000);
    }

    private void getPlaceInfo(double lat, double lon) {
        try {
            List<Address> addresses = mGeocoder.getFromLocation(lat, lon, 1);
            if (addresses.get(0).getLocality() != null) {
                String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                zipCode = addresses.get(0).getPostalCode();
                setUpCity(city, state);
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

    private void setUpCity(String city, String state) {
        boolean isCity = false;
        if (cityList != null && cityList.size() != 0) {
            for (CityModel model : cityList) {
                if (city.equalsIgnoreCase(model.getCityName())) {
                    selectCityId = model.getCityid();
                    this.city = city;
                    this.state = state;
                    isCity = true;
                    break;
                }
            }
        }
        if (!isCity) {
            this.city = city;
            this.state = state;
        }
        mBinding.tvArea.setText(city);
    }

    private void showAddressDetailDialog() {
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.setContentView(R.layout.dialog_address_detail);
        dialog.getWindow().findViewById(R.id.design_bottom_sheet).setBackgroundColor(Color.TRANSPARENT);

        TextView tvAddH = dialog.findViewById(R.id.tvAddH);
        TextView tvLocation = dialog.findViewById(R.id.tvLocation);
        ImageView ivClose = dialog.findViewById(R.id.ivClose);
        Button btnSave = dialog.findViewById(R.id.btnSave);
        TextInputLayout tiShopName = dialog.findViewById(R.id.tiShopName);
        TextInputLayout tiHouseNo = dialog.findViewById(R.id.tiHouseNo);
        EditText etShopName = dialog.findViewById(R.id.etShopName);
        EditText etHouseNo = dialog.findViewById(R.id.etHouseNo);
        EditText etFloor = dialog.findViewById(R.id.etFloor);
        EditText etHTR = dialog.findViewById(R.id.etHTR);

        tvLocation.setText(address);

        etHouseNo.setOnFocusChangeListener((view, hasFocus) -> {
            if (hasFocus) {
                etHouseNo.setHint("House no. / Floor / Building");
            } else {
                etHouseNo.setHint("");
            }
        });
        etFloor.setOnFocusChangeListener((view, hasFocus) -> {
            if (hasFocus) {
                etFloor.setHint("e.g. Ground Floor (Optional)");
            } else {
                etFloor.setHint("");
            }
        });
        etHTR.setOnFocusChangeListener((view, hasFocus) -> {
            if (hasFocus) {
                etHTR.setHint("Land mark / Entry gate / Street");
            } else {
                etHTR.setHint("");
            }
        });

        btnSave.setOnClickListener(v -> {
            shopName = etShopName.getText().toString();
            houseNo = etHouseNo.getText().toString();
            landmark = etHTR.getText().toString();
            address1 = etHouseNo.getText().toString() + " " + etFloor.getText().toString() + " " + etHTR.getText().toString();
            if (TextUtils.isNullOrEmpty(shopName)) {
                tiShopName.setError(getString(R.string.enter_shop_Name));
//            } else if (TextUtils.isNullOrEmpty(houseNo)) {
//                tiHouseNo.setError(getString(R.string.please_enter_address));
            } else {
                NewSignupRequest signupModel = new NewSignupRequest(SharePrefs.getInstance(getApplicationContext()).getInt(SharePrefs.CUSTOMER_ID),
                        "", "", SharePrefs.getInstance(getApplicationContext()).getString(SharePrefs.MOBILE_NUMBER),
                        "", address, areaName, "", landmark, zipCode, "", selectCityId, city,
                        SharePrefs.getInstance(getApplicationContext()).getString(SharePrefs.SK_CODE),
                        "", "", shopName, "", Constant.VERSION_NAME,
                        Build.VERSION.RELEASE, Build.MODEL, Utils.getDeviceUniqueID(this),
                        Utils.getDeviceUniqueID(this), location.getLatitude(), location.getLongitude(),
                        "", "",
                        "", "", "", "", false,
                        EndPointPref.getInstance(getApplicationContext()).getFcmToken(EndPointPref.FCM_TOKEN), "", "", ""
                        , address, "", "", "", 0, 0, 0, "", null,"Retailer");

                Utils.showProgressDialog(this);
                commonClassForAPI.signupUpdateBasicInfo(callSignUpDes, signupModel);
                dialog.dismiss();
            }
        });
        ivClose.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }


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
                    setUpCity(city, jsonObject.getString("state"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    // Forth city getting
    private final DisposableObserver<ArrayList<CityModel>> getCityDes = new DisposableObserver<ArrayList<CityModel>>() {
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

    private final DisposableObserver<SignupResponse> callSignUpDes = new DisposableObserver<SignupResponse>() {
        @Override
        public void onNext(@NotNull SignupResponse model) {
            Utils.hideProgressDialog();
            if (model.isStatus()) {
                CustomerResponse customer = model.customers;
                if (customer != null) {
                    Utils.setToast(getApplicationContext(), RetailerSDKApp.getInstance().dbHelper.getString(R.string.signup_successfully_done));
                    RetailerSDKApp.getInstance().prefManager.setLoggedIn(true);
                    // SharePrefs.getInstance(getApplicationContext()).putInt(SharePrefs.CUSTOMER_ID, Integer.parseInt(customer.getCustomerId()));
                    SharePrefs.getInstance(getApplicationContext()).putString(SharePrefs.SK_CODE, customer.getSkcode());
                    SharePrefs.getInstance(getApplicationContext()).putString(SharePrefs.SHOP_NAME, customer.getShopName());
                    SharePrefs.getInstance(getApplicationContext()).putString(SharePrefs.MOBILE_NUMBER, customer.getMobile());
                    SharePrefs.getInstance(getApplicationContext()).putInt(SharePrefs.COMPANY_ID, customer.getCompanyId());
                    SharePrefs.getInstance(getApplicationContext()).putInt(SharePrefs.WAREHOUSE_ID, customer.getWarehouseid());
                    SharePrefs.getInstance(getApplicationContext()).putString(SharePrefs.LATITUDE, customer.lat);
                    SharePrefs.getInstance(getApplicationContext()).putString(SharePrefs.LONGITUDE, customer.lg);
                    SharePrefs.getInstance(getApplicationContext()).putString(SharePrefs.CLUSTER_ID, customer.getClusterId());
                    UserAuth regApk = customer.registeredApk;
                    if (regApk != null) {
                        String Password = regApk.getPassword();
                        String username = regApk.getUserName();
                        SharePrefs.getInstance(getApplicationContext()).putString(SharePrefs.TOKEN_PASSWORD, Password);
                        SharePrefs.getInstance(getApplicationContext()).putString(SharePrefs.TOKEN_NAME, username);
                        Utils.showProgressDialog(UpdateAddressActivity.this);
                        commonClassForAPI.getToken(callTokenDes, "password", username, Password);
                    }
                }
            } else {
                Utils.setToast(getApplicationContext(), model.getMessage() + "");
            }
        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
            Utils.hideProgressDialog();
        }

        @Override
        public void onComplete() {

        }
    };

    // Third(A) getting token and go to home
    private final DisposableObserver<TokenResponse> callTokenDes = new DisposableObserver<TokenResponse>() {
        @Override
        public void onNext(@NotNull TokenResponse model) {
            try {
                Utils.hideProgressDialog();
                if (model != null) {
                    SharePrefs.getInstance(getApplicationContext()).putString(SharePrefs.TOKEN, model.access_token);
                    startActivity(new Intent(getApplicationContext(), NewSignupActivity.class)
                            .putExtra("address", address)
                            .putExtra("address1", address1)
                            .putExtra("shopName", shopName)
                            .putExtra("landmark", landmark)
                            .putExtra("city", city)
                            .putExtra("state", state)
                            .putExtra("zipCode", zipCode)
                            .putExtra("lat", location.getLatitude())
                            .putExtra("lng", location.getLongitude())
                            .putExtra("houseNo", houseNo)
                            .putExtra("selectCityId", selectCityId));
                    Utils.leftTransaction(UpdateAddressActivity.this);
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