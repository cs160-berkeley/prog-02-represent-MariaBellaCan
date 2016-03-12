package com.example.mariacan.campaignupdates2016;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

/**
 * Created by mariacan on 3/10/16.
 */
public class GPSManager implements GoogleApiClient.ConnectionCallbacks{

    public static final int LOCATION_PERMISSION = 0;

    private static GoogleApiClient googleApiClient;
    static double latitude;
    static double longitude;
    static GPSCallback gpsCallback;
    static Activity activity;

    @Override
    public void onConnected(Bundle bundle) {
        Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(
                googleApiClient);

        if (lastLocation == null){
            LocationManager locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION }, LOCATION_PERMISSION);
            }
            locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    gpsCallback.OnFinished(location.getLatitude(), location.getLongitude());
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            }, null);
        } else{
            latitude = lastLocation.getLatitude();
            longitude = lastLocation.getLongitude();
            gpsCallback.OnFinished(latitude, longitude);
        }

        googleApiClient.disconnect();

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    public static void getLatAndLongforGPS(GPSCallback gpsCallback, Activity activity) {

        googleApiClient = new GoogleApiClient.Builder(activity)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(new GPSManager())
                .build();
        googleApiClient.connect();
        GPSManager.activity = activity;
        GPSManager.gpsCallback = gpsCallback;
    }
}
