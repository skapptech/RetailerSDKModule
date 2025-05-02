package app.retailer.krina.shop.com.mp_shopkrina_retailer.main.activity;

import android.Manifest;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Locale;

import app.retailer.krina.shop.com.mp_shopkrina_retailer.R;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.api.CommonClassForAPI;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.databinding.ActivityMapBinding;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.main.adapter.PlaceAutocompleteAdapter;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.auth.EditProfileModel;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.models.postModels.LatLongModel;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.auth.CustomerResponse;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.data.dto.auth.MyProfileResponse;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.preference.SharePrefs;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.home.HomeActivity;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.ui.component.splash.SplashScreenActivity;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.GPSTracker;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.LocaleHelper;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.RetailerSDKApp;
import app.retailer.krina.shop.com.mp_shopkrina_retailer.utils.Utils;
import io.reactivex.observers.DisposableObserver;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener, LocationListener, GoogleApiClient.ConnectionCallbacks {
    protected GoogleApiClient mGoogleApiClient;
    private LocationRequest locationRequest;
    private Location location;
    private GoogleMap mMap;
    private PlaceAutocompleteAdapter mAdapter;
    private ActivityMapBinding mBinding;
    private Utils utils;
    private CommonClassForAPI commonClassForAPI;

    private int custId, flag;
    private String mLatitude, mLongitude, mCityName;
    private boolean locationFlag = true;
    private boolean isGPS = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityMapBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
     if (getIntent() != null) {
            flag = getIntent().getIntExtra("flag", 0);
        }
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();

        utils = new Utils(this);
        commonClassForAPI = CommonClassForAPI.getInstance(this);
        custId = SharePrefs.getInstance(this).getInt(SharePrefs.CUSTOMER_ID);
        mCityName = SharePrefs.getInstance(this).getString(SharePrefs.CITY_NAME);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mBinding.fabAdd.setOnClickListener(this);

        AutoCompleteTextView mAutocompleteView = findViewById(R.id.googleplacesearch);
        AutocompleteFilter filter = new AutocompleteFilter.Builder().setCountry("in").build();

        mAdapter = new PlaceAutocompleteAdapter(this, R.layout.google_places_search_items,
                mGoogleApiClient, null, filter, mCityName);
        mAutocompleteView.setAdapter(mAdapter);
        mAutocompleteView.setOnItemClickListener(mAutocompleteClickListener);
        mBinding.toolbarMap.back.setVisibility(View.GONE);

        mBinding.toolbarMap.title.setText(RetailerSDKApp.getInstance().dbHelper.getString(R.string.search_place));
        mBinding.tvLoc.setText(RetailerSDKApp.getInstance().dbHelper.getString(R.string.location));
        mBinding.googleplacesearch.setHint(RetailerSDKApp.getInstance().dbHelper.getString(R.string.Search_here));
        mBinding.fabAdd.setHint(RetailerSDKApp.getInstance().dbHelper.getString(R.string.Confirm_Location));

        isGPS = Utils.gpsPermission(this, "runtime");

        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setFastestInterval(100);
        locationRequest.setInterval(1000);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        // stop location updates when Activity is no longer active
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    @Override
    public void onBackPressed() {
        if (flag != 2) {
            finish();
            super.onBackPressed();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
        super.onNewIntent(intent);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fab_add) {
            String adress = mBinding.googleplacesearch.getText().toString().trim();
            if (!adress.isEmpty()) {
                isGPS = Utils.gpsPermission(this, "clicktimer");
                if (isGPS) {
                    locationCallApi();
                } else {
                    locationFlag = true;
                }
            } else {
                Toast.makeText(getApplicationContext(), RetailerSDKApp.getInstance().dbHelper.getData("please_enter_address"), Toast.LENGTH_SHORT).show();
            }
        }
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
                            locationRequest, MapsActivity.this);
                    if (location == null) {
                        location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                    }
                    break;
                case Activity.RESULT_CANCELED:
                    // The user was asked to change settings, but chose not to
                    Toast.makeText(getApplicationContext(), RetailerSDKApp.getInstance().dbHelper.getData("allow_location_access"), Toast.LENGTH_LONG).show();
                    mGoogleApiClient.disconnect();
                    break;
                default:
                    break;
            }
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


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        try {
            mMap = googleMap;
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap.setMyLocationEnabled(true);
            mMap.setOnCameraChangeListener((CameraPosition cameraPosition) -> drawMarker(cameraPosition.target));
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
                            != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MapsActivity.this,
                            Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                            locationRequest, MapsActivity.this);
                    location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                    break;
                case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                    // Location settings are not satisfied. But could be
                    // fixed by showing the user a dialog.
                    try {
                        // Show the dialog by calling startResolutionForResult(),
                        // and check the result in onActivityResult().
                        status.startResolutionForResult(MapsActivity.this, 909);
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
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        this.location = location;
        if (locationFlag) {
            try {
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 16));
                locationFlag = false;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
        }
    }

    private void launchHomeScreen() {
        try {
            if (flag == 2) {
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                finish();
                Utils.rightTransaction(this);
            } else {
                RetailerSDKApp.getInstance().clearLocalData();
                RetailerSDKApp.getInstance().clearCartData();
                RetailerSDKApp.getInstance().prefManager.setLoggedIn(true);
                startActivity(new Intent(getApplicationContext(), SplashScreenActivity.class));
                finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getAddress(double latitude, double longitude) {
        String currentLocation = "";
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.ENGLISH);
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
            String address = addresses.get(0).getAddressLine(0);
            String address1 = addresses.get(0).getAddressLine(1);
            if (!android.text.TextUtils.isEmpty(address)) {
                currentLocation = address;
                if (!android.text.TextUtils.isEmpty(address1))
                    currentLocation += "\n" + address1;
            }
            return currentLocation;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private void locationCallApi() {
        try {
            if (mMap.getCameraPosition().target.latitude != 0.0 && mMap.getCameraPosition().target.longitude != 0.0) {
                mLatitude = mMap.getCameraPosition().target.latitude + "";
                mLongitude = mMap.getCameraPosition().target.longitude + "";
            } else {
                GPSTracker gpsTracker = new GPSTracker(MapsActivity.this);
                if (gpsTracker != null) {
                    mLatitude = gpsTracker.getLatitude() + "";
                    mLongitude = gpsTracker.getLongitude() + "";
                }
            }
            String adress = mBinding.googleplacesearch.getText().toString().trim() + ", " + SharePrefs.getInstance(getApplicationContext()).getString(SharePrefs.CITY_NAME);
            String mobile = SharePrefs.getInstance(getApplicationContext()).getString(SharePrefs.MOBILE_NUMBER);
            // Toast.makeText(this, "Latitude:" + mLatitude + " Longitude:" + mLongitude, Toast.LENGTH_LONG).show();
            if (flag == 1) {
                Intent intent = new Intent();
                intent.putExtra("lat", mLatitude);
                intent.putExtra("lag", mLongitude);
                intent.putExtra("adress", adress);
                setResult(Activity.RESULT_OK, intent);
                finish();
            } else if (flag == 2) {
                if (utils.isNetworkAvailable()) {
                    if (commonClassForAPI != null) {
                        SharePrefs.getInstance(getApplicationContext()).putString(SharePrefs.SHIPPING_ADDRESS, adress);
                        Utils.showProgressDialog(this);
                        commonClassForAPI.updateLatLong(editProfile, new LatLongModel(custId, Double.parseDouble(mLatitude),
                                Double.parseDouble(mLongitude), adress, ""), "map Section");
                    }
                } else {
                    Utils.setToast(getApplicationContext(), RetailerSDKApp.getInstance().dbHelper.getData("internet_connection"));
                }
            } else {
                if (utils.isNetworkAvailable()) {
                    if (commonClassForAPI != null) {
                        SharePrefs.getInstance(getApplicationContext()).putString(SharePrefs.SHIPPING_ADDRESS, adress);
                        Utils.showProgressDialog(this);
                        commonClassForAPI.editProfile(editProfile,
                                new EditProfileModel(custId, "", mobile, "", 0, "", "", adress, "", "", "",
                                        adress, adress, "", mLatitude, mLongitude, "", "", "", "",
                                        "", "", "", "", "", "", "",
                                        "", "", "", "", 0, ""), "Address ad lat long");
                    }
                } else {
                    Utils.setToast(getApplicationContext(), RetailerSDKApp.getInstance().dbHelper.getData("internet_connection"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void drawMarker(LatLng latLng) {
        mBinding.googleplacesearch.setText(getAddress(latLng.latitude, latLng.longitude));
    }

    public void hideKeyboard() {
        // Check if no view has focus:
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }


    private final DisposableObserver<MyProfileResponse> editProfile = new DisposableObserver<MyProfileResponse>() {
        @Override
        public void onNext(@NotNull MyProfileResponse model) {
            try {
                Utils.hideProgressDialog();
                if (model.isStatus() && model.getCustomers() != null) {
                    CustomerResponse customer = model.getCustomers();
                    Utils.setToast(getApplicationContext(), RetailerSDKApp.getInstance().dbHelper.getData("succesfullSubmitted"));
                    SharePrefs.getInstance(getApplicationContext()).putInt(SharePrefs.CUSTOMER_ID, customer.customerId);
                    SharePrefs.getInstance(getApplicationContext()).putString(SharePrefs.SK_CODE, customer.getSkcode());
                    SharePrefs.getInstance(getApplicationContext()).putString(SharePrefs.MOBILE_NUMBER, customer.getMobile());
                    SharePrefs.getInstance(getApplicationContext()).putInt(SharePrefs.COMPANY_ID, customer.getCompanyId());
                    SharePrefs.getInstance(getApplicationContext()).putInt(SharePrefs.WAREHOUSE_ID, customer.getWarehouseid());
                    SharePrefs.getInstance(getApplicationContext()).putString(SharePrefs.LATITUDE, customer.lat);
                    SharePrefs.getInstance(getApplicationContext()).putString(SharePrefs.LONGITUDE, customer.lg);
                    launchHomeScreen();
                } else {
                    Utils.setToast(getApplicationContext(), model.getMessage());
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

        }
    };

    private final ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                places.release();
                return;
            }
            // Get the Place object from the buffer.
            final Place place = places.get(0);
            hideKeyboard();
            mLatitude = String.valueOf(place.getLatLng().latitude);
            mLongitude = String.valueOf(place.getLatLng().longitude);
            LatLng newLatLngTemp = new LatLng(place.getLatLng().latitude, place.getLatLng().longitude);
            if (mMap != null) {
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newLatLngTemp, 15f));
            }
        }
    };

    private final AdapterView.OnItemClickListener mAutocompleteClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            final PlaceAutocompleteAdapter.PlaceAutocomplete item = mAdapter.getItem(position);
            final String placeId = String.valueOf(item.placeId);
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi.getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
        }
    };
}