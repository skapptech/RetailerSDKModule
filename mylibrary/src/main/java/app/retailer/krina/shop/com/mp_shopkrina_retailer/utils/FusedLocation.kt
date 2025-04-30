package app.retailer.krina.shop.com.mp_shopkrina_retailer.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationTokenSource
import com.nabinbhandari.android.permissions.PermissionHandler
import com.nabinbhandari.android.permissions.Permissions
import java.util.Locale

class FusedLocation(private val mContext: Context) {

    private var cancellationTokenSource: CancellationTokenSource? = null
    private var fusedLocationClient: FusedLocationProviderClient? = null

    @kotlin.jvm.JvmField
    val locationLiveData = SingleLiveEvent<Location?>()

    fun getLocation() {
        var mlocation: Location?
        try {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(mContext)
            cancellationTokenSource = CancellationTokenSource()
            if (askPermission()) {
                if (ActivityCompat.checkSelfPermission(
                        mContext,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    fusedLocationClient?.getCurrentLocation(
                        LocationRequest.PRIORITY_HIGH_ACCURACY,
                        cancellationTokenSource!!.getToken()
                    )
                        ?.addOnSuccessListener { location: Location? ->
                            if (location != null) {
                                mlocation = location
                            } else {
                                val gpsTracker = GPSTrackerMain(mContext)
                                mlocation = gpsTracker.getLocation()
                            }
                            if (mlocation != null) {
                                locationLiveData.postValue(mlocation)
                            }
                            println("Location::${mlocation?.latitude},${mlocation?.longitude}")
                            return@addOnSuccessListener
                        }
                }
            }
        } catch (e: Exception) {
            // e.printStackTrace();
            Log.e(
                "Error : Location",
                "Impossible to connect to LocationManager", e
            )
        }
    }

    private fun askPermission(): Boolean {
        var flag = false
        val permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_MEDIA_IMAGES)
        } else {
            arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        Permissions.check(
            mContext,
            permissions,
            null,
            null,
            object : PermissionHandler() {
                override fun onGranted() {
                    flag = true
                }

                override fun onDenied(context: Context?, deniedPermissions: ArrayList<String>) {
                    //permissionAskDialog()
                    flag = false
                    askPermission()
                }

            })
        return flag
    }

    fun onStop() {
        if (cancellationTokenSource != null) {
            cancellationTokenSource!!.cancel()
        }
    }


    companion object {
        fun getAddress(
            mContext: Context,
            latitude: Double,
            longitude: Double,
            isAddress: Boolean,
            isFullAddress: Boolean,
            isCity: Boolean
        ): String {
            var add = ""
            var addresses: List<Address>? = null
            val mGeocoder = Geocoder(mContext, Locale.ENGLISH)
            try {
                addresses = mGeocoder.getFromLocation(latitude, longitude, 1)
                if (addresses!=null && addresses.size!=0) {
                    if (isFullAddress) {
                        var fullAddress = ""
                        val address = "" + addresses[0].getAddressLine(0)
                        val city = "" + addresses[0].locality
                        val state = "" + addresses[0].adminArea
                        val country = "" + addresses[0].countryName
                        val postalCode = "" + addresses[0].postalCode
                        val knownName = "" + addresses[0].featureName
                        fullAddress = "$address,$city,$postalCode,$state,$knownName,$country"
                        if (!fullAddress.isNullOrEmpty()) {
                            add = fullAddress
                        }
                    } else if (isAddress) {
                        if (!addresses[0].getAddressLine(0).isNullOrEmpty()) {
                            add = addresses[0].getAddressLine(0)
                        }
                    } else if (isCity) {
                        if (!addresses[0].locality.isNullOrEmpty()) {
                            add = addresses[0].locality
                        }
                    } else {
                        if (!addresses[0].subLocality.isNullOrEmpty()) {
                            add = addresses[0].subLocality
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return add.replace(",null".toRegex(), "")
        }
    }
}