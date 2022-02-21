package pk.com.pieinthesky.app.utils;

import android.content.pm.PackageManager;
import android.location.Criteria;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import java.util.List;


public class GPSManager implements LocationListener {
    private Context context;

    private Location location;
    private double latitude;
    private double longitude;

    private int updateInterval = 2 * 60 * 1000; //2 min
    private int smallestDisplacement = 10; //10 meters

    private LocationManager locationManager;
    private OnLocationUpdateListener locationUpdateListener = null;
    private OnProviderActionListener providerActionListener = null;
    private static GPSManager instance = null;

    private GPSManager(Context context) {
        this.context = context;
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        location = getLastKnownLocation();
    }

    public static GPSManager createInstance(Context context) {
        if (instance == null) {
            instance = new GPSManager(context);
        }
        return instance;
    }

    public static GPSManager getInstance() {
        return instance;
    }

    public void startLocationUpdates() {
        try {
            if (locationManager != null) {

                if (Build.VERSION.SDK_INT >= 23 &&
                        ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                if (isLocationEnabled()) {
                    Criteria criteria = new Criteria();
                    criteria.setAccuracy(Criteria.ACCURACY_FINE);
                    criteria.setAltitudeRequired(false);
                    criteria.setBearingRequired(false);
                    criteria.setCostAllowed(true);
                    criteria.setPowerRequirement(Criteria.POWER_LOW);
                    String provider = locationManager.getBestProvider(criteria, true);

                    if (provider != null) {
                        locationManager.requestLocationUpdates(provider, updateInterval, smallestDisplacement, this);
                        location = getLastKnownLocation();
                    }

                } else {
                    if (providerActionListener != null)
                        providerActionListener.onProviderAction(ProviderState.PROVIDER_DISABLED);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Location getLastKnownLocation() {
        if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return null;
        }
        List<String> providers = locationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            Location lastLocation = locationManager.getLastKnownLocation(provider);
            if (lastLocation == null) {
                continue;
            }
            if (bestLocation == null || lastLocation.getAccuracy() < bestLocation.getAccuracy()) {
                bestLocation = lastLocation;
            }
        }

        if (bestLocation != null) {
            latitude = bestLocation.getLatitude();
            longitude = bestLocation.getLongitude();
        }
        return bestLocation;
    }

    public void stopLocationUpdates() {
        if (locationManager != null) {
            locationManager.removeUpdates(this);
        }
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getLatLng() {
        return latitude + "," + longitude;
    }

    public Location getLocation() {
        return location;
    }

    public boolean isLocationEnabled() {
        boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        return isGPSEnabled || isNetworkEnabled;
    }

    public int getUpdateInterval() {
        return updateInterval;
    }

    public void setUpdateInterval(int updateInterval) {
        this.updateInterval = updateInterval;
    }


    public int getSmallestDisplacement() {
        return smallestDisplacement;
    }

    public void setSmallestDisplacement(int smallestDisplacement) {
        this.smallestDisplacement = smallestDisplacement;
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            this.location = location;
            latitude = location.getLatitude();
            longitude = location.getLongitude();
        }
        if (locationUpdateListener != null)
            locationUpdateListener.onLocationChanged(location);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
        if (providerActionListener != null)
            providerActionListener.onProviderAction(ProviderState.STATUS_CHANGED);
    }

    @Override
    public void onProviderEnabled(String s) {
        if (providerActionListener != null)
            providerActionListener.onProviderAction(ProviderState.PROVIDER_ENABLED);
    }

    @Override
    public void onProviderDisabled(String s) {
        if (providerActionListener != null)
            providerActionListener.onProviderAction(ProviderState.PROVIDER_DISABLED);
    }

    public void setLocationUpdateListener(OnLocationUpdateListener locationUpdateListener) {
        this.locationUpdateListener = locationUpdateListener;
    }

    public void setLocationActionListener(OnProviderActionListener providerActionListener) {
        this.providerActionListener = providerActionListener;
    }

    public interface OnLocationUpdateListener {

        void onLocationChanged(Location location);
    }

    public interface OnProviderActionListener {

        void onProviderAction(ProviderState providerState);
    }

    public enum ProviderState {
        STATUS_CHANGED, PROVIDER_ENABLED, PROVIDER_DISABLED;
    }
}
