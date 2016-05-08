package pt.ulisboa.tecnico.cmu.ubibike.location;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.Calendar;

import pt.ulisboa.tecnico.cmu.ubibike.domain.Coordinates;
import pt.ulisboa.tecnico.cmu.ubibike.domain.Trajectory;
import pt.ulisboa.tecnico.cmu.ubibike.services.gps.GpsTrackingService;
import pt.ulisboa.tecnico.cmu.ubibike.services.gps.IGPSCallback;


public class GpsTracking  implements GoogleApiClient.ConnectionCallbacks,
                        GoogleApiClient.OnConnectionFailedListener,LocationListener{

    private final static int UPDATES_INTERVAL = 4000;

    private IGPSCallback serviceCallback;

    private Trajectory trajectory;

    private Context currentContext;

    private GoogleApiClient googleClient;

    private LocationRequest locationRequest;

    private boolean isConnected;

    public GpsTracking(Context context,IGPSCallback serviceCallback){
        Calendar c = Calendar.getInstance();
        c.set(Calendar.MILLISECOND,0);
        this.serviceCallback = serviceCallback;
        isConnected = false;
        currentContext = context;
        locationRequest = new LocationRequest();
        trajectory = new Trajectory(c.getTime());
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setFastestInterval(UPDATES_INTERVAL);
        locationRequest.setInterval(UPDATES_INTERVAL);
        googleClient = new GoogleApiClient.Builder(currentContext)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    private void startLocationUpdates() {
        int permission = ContextCompat.checkSelfPermission(currentContext,
                android.Manifest.permission.ACCESS_FINE_LOCATION);
        if(permission == PackageManager.PERMISSION_GRANTED && isConnected) {
            Log.d("GPS","Track started");
            LocationServices.FusedLocationApi.requestLocationUpdates(googleClient, locationRequest
                    , this);
        }
    }

    public Trajectory getTrajectory(){
        return trajectory;
    }

    public void stopLocationUpdates() {
        if(isConnected){
            LocationServices.FusedLocationApi.removeLocationUpdates(
                    googleClient, this);
        }
    }

    public void connect(){
        if(!isConnected){
            isConnected = true;
            googleClient.connect();
        }
    }

    public void disconnect(){
        if(isConnected) {
            isConnected = false;
            googleClient.disconnect();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        trajectory.addCoordinate(new Coordinates(location.getLatitude(), location.getLongitude()));
        serviceCallback.setLocation(location.getLatitude(), location.getLongitude());
        Log.d("GPS Current Coordinates",location.getLatitude()+ " " + location.getLongitude());
    }

    @Override
    public void onConnected(Bundle bundle) {
        int permission = ContextCompat.checkSelfPermission(currentContext,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if(permission == PackageManager.PERMISSION_GRANTED){
            startLocationUpdates();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        //DO Nothing?
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        //DO Nothing?
    }

}
