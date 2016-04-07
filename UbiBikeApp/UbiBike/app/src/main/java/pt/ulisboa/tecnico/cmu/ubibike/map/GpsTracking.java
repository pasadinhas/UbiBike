package pt.ulisboa.tecnico.cmu.ubibike.map;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.security.Permission;
import java.util.jar.Manifest;

/**
 * Created by André on 07-04-2016.
 */
public class GpsTracking implements GoogleApiClient.ConnectionCallbacks,
                        GoogleApiClient.OnConnectionFailedListener, LocationListener{

    private final static int UPDATES_INTERVAL = 5000;

    private Activity currentActivity;

    private GoogleApiClient googleClient;

    private Location mLastLocation;

    private LocationRequest locationRequest;

    private boolean isConnected;

    private boolean isReady;

    public GpsTracking(Activity activity){
        isConnected = isReady = false;
        currentActivity = activity;
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setFastestInterval(UPDATES_INTERVAL);
        locationRequest.setInterval(UPDATES_INTERVAL);
        googleClient = new GoogleApiClient.Builder(currentActivity)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    public void startLocationUpdates() {
        int permission = ContextCompat.checkSelfPermission(currentActivity,
                android.Manifest.permission.ACCESS_FINE_LOCATION);
        if(permission == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(googleClient, locationRequest
                    , this);
        }
    }

    public void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                googleClient,this);
    }

    public void connect(){
        googleClient.connect();
    }

    public void disconnect(){
        googleClient.disconnect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        try {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(googleClient);
            startLocationUpdates();
        }catch (SecurityException ex){
            //TODO
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d("WOW",location.getLatitude() + " " + location.getLongitude());
    }

}
