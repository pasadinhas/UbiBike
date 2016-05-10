package pt.ulisboa.tecnico.cmu.ubibike.services.gps;

import android.Manifest;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.List;

import pt.ulisboa.tecnico.cmu.ubibike.R;
import pt.ulisboa.tecnico.cmu.ubibike.data.GeofenceData;
import pt.ulisboa.tecnico.cmu.ubibike.data.StationsData;
import pt.ulisboa.tecnico.cmu.ubibike.domain.Station;

/**
 * Created by cedac on 09/05/16.
 */
public class GeofencingManagerService extends Service implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, ResultCallback<Status> {
    private static final String TAG = "GeofencingManagerServ";
    protected GoogleApiClient mGoogleApiClient = null;
    private PendingIntent mGeofencePendingIntent;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("UbiBike", "onStartCommand: started geofencing service");
        mGeofencePendingIntent = null;

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        populateGeofences();

        mGoogleApiClient.connect();

        return flags;
    }

    public void onDestroy() {
        mGoogleApiClient.disconnect();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void populateGeofences() {
        List<Geofence> mGeofenceList = new ArrayList<>();
        List<Station> stations = StationsData.getStations(this);

        float radius = 500;

        for (Station station : stations) {

            mGeofenceList.add(new Geofence.Builder()
                    .setRequestId(station.getName())

                    .setCircularRegion(
                            station.getPosition().getLatitude(),
                            station.getPosition().getLongitude(),
                            radius
                    )
                    .setExpirationDuration(Geofence.NEVER_EXPIRE)
                    .setLoiteringDelay(15 * 1000)
                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                                        Geofence.GEOFENCE_TRANSITION_DWELL |
                                        Geofence.GEOFENCE_TRANSITION_EXIT)
                    .build());

            Log.d(TAG, "populateGeofences: added: " + station.getName());
        }

        GeofenceData.getInstance().setGeofenceList(mGeofenceList);
    }

    private void startGeofencing() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            LocationServices.GeofencingApi.addGeofences(
                    mGoogleApiClient,
                    GeofenceData.getInstance().getGeofencingRequest(),
                    getGeofencePendingIntent()
            ).setResultCallback(this);
            Log.d(TAG, "startGeofencing: success");
        } else {
            Log.d(TAG, "startGeofencing: lacking permissions");
        }
    }

    private PendingIntent getGeofencePendingIntent() {
        // Reuse the PendingIntent if we already have it.
        if (mGeofencePendingIntent != null) {
            return mGeofencePendingIntent;
        }
        Intent intent = new Intent(this, GeofenceIntentService.class);
        return PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.d("UbiBike", "onConnected: successful connection to Location API");
        startGeofencing();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d("UbiBike", "onSuspended: suspended connection to Location API");

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d("UbiBike", "onConnFail: failed connection to Location API");

    }

    @Override
    public void onResult(Status status) {
        Log.d("UbiBike", "onResult: geofences added! " + status.getStatusMessage() + "code: " + status.getStatusCode());
    }

}
