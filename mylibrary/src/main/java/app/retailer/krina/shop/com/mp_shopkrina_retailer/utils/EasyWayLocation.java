package app.retailer.krina.shop.com.mp_shopkrina_retailer.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Looper;
import android.provider.Settings;
import android.text.TextUtils;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.Random;

import app.retailer.krina.shop.com.mp_shopkrina_retailer.interfaces.Listener;

/**
 * Utility class for easy access to the device location on Android
 */
public class EasyWayLocation {
    public static final int LOCATION_SETTING_REQUEST_CODE = 5;
    public Listener getmListener;
    private static final String PROVIDER_COARSE = LocationManager.NETWORK_PROVIDER;
    private static final String PROVIDER_FINE = LocationManager.GPS_PROVIDER;
    private static final String PROVIDER_FINE_PASSIVE = LocationManager.PASSIVE_PROVIDER;
    private static final long INTERVAL_DEFAULT = 10 * 60 * 1000;
    private static final float KILOMETER_TO_METER = 1000.0f;
    private static final float LATITUDE_TO_KILOMETER = 111.133f;
    private static final float LONGITUDE_TO_KILOMETER_AT_ZERO_LATITUDE = 111.320f;
    private static final int REQUEST_CHECK_SETTINGS = 11;
    private static final Random mRandom = new Random();
    private static final double SQUARE_ROOT_TWO = Math.sqrt(2);
    private static Location mCachedPosition;
    //private final LocationManager mLocationManager;
    //private final boolean mRequireFine;
    private boolean mPassive;
    private long mInterval;
    private boolean mRequireLastLocation;
    private FusedLocationProviderClient fusedLocationClient;
    boolean gps_enabled = false;
    boolean network_enabled = false;
    private Boolean locationReturn = true;
    private Context activity;
    private Context context;
    private LocationCallback locationCallback;
    private int mBlurRadius;
    private LocationListener mLocationListener;
    private Location mPosition;
    private Listener mListener;
    private LocationRequest locationRequest;

    public EasyWayLocation(final Context context, final boolean requireLastLocation, Boolean isDebuggable, final Listener listener) {
        this(context, null, isDebuggable, requireLastLocation, listener);
    }


    public EasyWayLocation(Context context, final LocationRequest locationRequest, final boolean requireLastLocation, Boolean isDebuggable, final Listener listener) {
        // mLocationManager = (LocationManager) context.getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        this.context = context;
        this.mListener = listener;
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
        if (locationRequest != null) {
            this.locationRequest = locationRequest;
        } else {
            this.locationRequest = LocationRequest.create();
            this.locationRequest.setInterval(5000);
            // locationRequest.setSmallestDisplacement(10F);
            this.locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        }
        this.mRequireLastLocation = requireLastLocation;
        if (mRequireLastLocation) {
            getCachedPosition();
        }
    }

    private static int calculateRandomOffset(final int radius) {
        return mRandom.nextInt((radius + 1) * 2) - radius;
    }

    public static void openSettings(final Context context) {
        context.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
    }

    public static double latitudeToKilometer(double latitude) {
        return latitude * LATITUDE_TO_KILOMETER;
    }

    public static double kilometerToLatitude(double kilometer) {
        return kilometer / latitudeToKilometer(1.0f);
    }

    public static double latitudeToMeter(double latitude) {
        return latitudeToKilometer(latitude) * KILOMETER_TO_METER;
    }

    public static double meterToLatitude(double meter) {
        return meter / latitudeToMeter(1.0f);
    }

    public static double longitudeToKilometer(double longitude, double latitude) {
        return longitude * LONGITUDE_TO_KILOMETER_AT_ZERO_LATITUDE * Math.cos(Math.toRadians(latitude));
    }

    public static double kilometerToLongitude(double kilometer, double latitude) {
        return kilometer / longitudeToKilometer(1.0f, latitude);
    }

    public static double longitudeToMeter(double longitude, double latitude) {
        return longitudeToKilometer(longitude, latitude) * KILOMETER_TO_METER;
    }

    public static double meterToLongitude(double meter, double latitude) {
        return meter / longitudeToMeter(1.0f, latitude);
    }

    public static double calculateDistance(Point start, Point end) {
        return calculateDistance(start.latitude, start.longitude, end.latitude, end.longitude);
    }

    public static double calculateDistance(double startLatitude, double startLongitude, double endLatitude, double endLongitude) {
        float[] results = new float[3];
        Location.distanceBetween(startLatitude, startLongitude, endLatitude, endLongitude, results);
        return results[0];
    }

    public void setListener(final Listener listener) {
        mListener = listener;
    }

    public boolean hasLocationEnabled() {
        try {
            int locationMode = 0;
            String locationProviders;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                try {
                    locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);

                } catch (Settings.SettingNotFoundException e) {
                    e.printStackTrace();
                    return false;
                }
                return locationMode != Settings.Secure.LOCATION_MODE_OFF;
            } else {
                locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
                return !TextUtils.isEmpty(locationProviders);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void startLocation() {
        //checkLocationSetting();
        if (mRequireLastLocation) {
            beginUpdates();
            endUpdates();
        } else {
            beginUpdates();
        }

    }

    @SuppressLint("MissingPermission")
    private void beginUpdates() {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    mListener.locationCancelled();
                } else {
                    for (Location location : locationResult.getLocations()) {
                        mListener.currentLocation(location);
                    }
                }

            }
        };

        fusedLocationClient.requestLocationUpdates(locationRequest,
                locationCallback,
                Looper.getMainLooper());
    }

    @SuppressLint("MissingPermission")
    public void endUpdates() {
        if (locationCallback != null) {
            fusedLocationClient.removeLocationUpdates(locationCallback);
        }
    }

    private Location blurWithRadius(final Location originalLocation) {
        if (mBlurRadius <= 0) {
            return originalLocation;
        } else {
            Location newLocation = new Location(originalLocation);

            double blurMeterLong = calculateRandomOffset(mBlurRadius) / SQUARE_ROOT_TWO;
            double blurMeterLat = calculateRandomOffset(mBlurRadius) / SQUARE_ROOT_TWO;

            newLocation.setLongitude(newLocation.getLongitude() + meterToLongitude(blurMeterLong, newLocation.getLatitude()));
            newLocation.setLatitude(newLocation.getLatitude() + meterToLatitude(blurMeterLat));

            return newLocation;
        }
    }

    public Point getPosition() {
        if (mPosition == null) {
            return null;
        } else {
            Location position = blurWithRadius(mPosition);
            return new Point(position.getLatitude(), position.getLongitude());
        }
    }

    public double getLatitude() {
        if (mPosition == null) {
            return 0.0f;
        } else {
            Location position = blurWithRadius(mPosition);
            return position.getLatitude();
        }
    }

    public double getLongitude() {
        if (mPosition == null) {
            return 0.0f;
        } else {
            Location position = blurWithRadius(mPosition);
            return position.getLongitude();
        }
    }

    public float getSpeed() {
        if (mPosition == null) {
            return 0.0f;
        } else {
            return mPosition.getSpeed();
        }
    }

    public double getAltitude() {
        if (mPosition == null) {
            return 0.0f;
        } else {
            return mPosition.getAltitude();
        }
    }

    public void setBlurRadius(final int blurRadius) {
        mBlurRadius = blurRadius;
    }


    @SuppressLint("MissingPermission")
    private void getCachedPosition() {
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            mListener.currentLocation(location);
                        } else {
                            //checkLocationSetting();
                            beginUpdates();
                            endUpdates();
                        }
                    }
                });
    }

    /**
     * Caches the current position
     */
    @Deprecated
    private void cachePosition() {
        if (mPosition != null) {
            mCachedPosition = mPosition;
        }
    }

    /**
     * Wrapper for two coordinates (latitude and longitude)
     */
    public static class Point implements android.os.Parcelable {

        public static final Creator<Point> CREATOR = new Creator<Point>() {

            @Override
            public Point createFromParcel(android.os.Parcel in) {
                return new Point(in);
            }

            @Override
            public Point[] newArray(int size) {
                return new Point[size];
            }

        };
        /**
         * The latitude of the point
         */
        public final double latitude;
        /**
         * The longitude of the point
         */
        public final double longitude;

        /**
         * Constructs a new point from the given coordinates
         *
         * @param lat the latitude
         * @param lon the longitude
         */
        public Point(double lat, double lon) {
            latitude = lat;
            longitude = lon;
        }

        private Point(android.os.Parcel in) {
            latitude = in.readDouble();
            longitude = in.readDouble();
        }

        @Override
        public String toString() {
            return "(" + latitude + ", " + longitude + ")";
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(android.os.Parcel out, int flags) {
            out.writeDouble(latitude);
            out.writeDouble(longitude);
        }

    }

    public void onActivityResult(int result) {

        if (result == Activity.RESULT_OK) {
            mListener.locationOn();
            //beginUpdates();
        } else if (result == Activity.RESULT_CANCELED) {
            mListener.locationCancelled();
        }
    }

    public void showAlertDialog(String title, String message, android.graphics.drawable.Drawable drawable) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(title);
        if (drawable != null) {
            alertDialog.setIcon(drawable);
        }
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    public static String getAddress(Context context, Double latitude, Double longitude, boolean country, boolean fullAddress) {
        String add = "";
        Geocoder geoCoder = new Geocoder(((Activity) context).getBaseContext(), java.util.Locale.getDefault());
        try {
            java.util.List<Address> addresses = geoCoder.getFromLocation(latitude, longitude, 1);

            if (addresses.size() > 0) {
                if (country) {
                    add = addresses.get(0).getCountryName();
                } else if (fullAddress) {
                    add = addresses.get(0).getFeatureName() + "," + addresses.get(0).getSubLocality() + "," + addresses.get(0).getSubAdminArea() + "," + addresses.get(0).getPostalCode() + "," + addresses.get(0).getCountryName();
                } else {
                    add = addresses.get(0).getLocality();
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
        return add.replaceAll(",null", "");
    }

   /* private void checkLocationSetting(){
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.setAlwaysShow(true);
        builder.addLocationRequest(locationRequest);
        SettingsClient client = LocationServices.getSettingsClient(context);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());

        task.addOnSuccessListener(locationSettingsResponse -> {
            if (mRequireLastLocation){
                beginUpdates();
                endUpdates();
            }else {
                beginUpdates();
            }

        });

        task.addOnFailureListener(e -> {
            if (e instanceof ResolvableApiException) {
                // Location settings are not satisfied, but this can be fixed
                // by showing the user a dialog.
                try {
                    // Show the dialog by calling startResolutionForResult(),
                    // and check the result in onActivityResult().
                    ResolvableApiException resolvable = (ResolvableApiException) e;
                    resolvable.startResolutionForResult((Activity)context,
                            LOCATION_SETTING_REQUEST_CODE);
                } catch (IntentSender.SendIntentException sendEx) {
                        sendEx.printStackTrace();
                }
            }
        });
    }*/
}
