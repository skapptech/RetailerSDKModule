package app.retailer.krina.shop.com.mp_shopkrina_retailer.interfaces;

import android.location.Location;

public interface Listener {
    public String TAG = "Location_Sample_Logs";
    void locationOn();

    void currentLocation(Location location);

    void locationCancelled();
}


