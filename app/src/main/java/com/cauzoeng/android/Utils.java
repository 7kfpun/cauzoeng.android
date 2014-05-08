package com.cauzoeng.android;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by kf on 5/8/14.
 */
public class Utils {
    public static Double[] getGpsLocation (Object service) {

        LocationManager locationManager = (LocationManager) service;
        // service is getSystemService(LOCATION_SERVICE)
        Criteria criteria = new Criteria();
        String bestProvider = locationManager.getBestProvider(criteria, false);
        Location location = locationManager.getLastKnownLocation(bestProvider);
        LocationListener loc_listener = new LocationListener() {

            public void onLocationChanged(Location l) {}

            public void onProviderEnabled(String p) {}

            public void onProviderDisabled(String p) {}

            public void onStatusChanged(String p, int status, Bundle extras) {}
        };
        locationManager
                .requestLocationUpdates(bestProvider, 0, 0, loc_listener);
        location = locationManager.getLastKnownLocation(bestProvider);
        Double lat, lon;
        try {
            lat = location.getLatitude();
            lon = location.getLongitude();
        } catch (NullPointerException e) {
            lat = -1.0;
            lon = -1.0;
            Log.e(Constants.GPS_TAG, e.toString());
        }
        // locationManager.removeUpdates(loc_listener);

        return new Double[] {lat, lon};
    }

    public static String getMacAddress(Object service) {

        WifiManager wm = (WifiManager) service;
        // service is getSystemService(Context.WIFI_SERVICE)
        String macAddress = wm.getConnectionInfo().getMacAddress();
        if ( macAddress == null || macAddress.isEmpty() ) {
            return "FAKE_USER";
        } else {
            return macAddress;
        }
    }
}
