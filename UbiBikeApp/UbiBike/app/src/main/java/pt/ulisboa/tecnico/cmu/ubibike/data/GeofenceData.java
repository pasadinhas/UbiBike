package pt.ulisboa.tecnico.cmu.ubibike.data;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cedac on 09/05/16.
 */
public class GeofenceData {
    private static GeofenceData instance = null;

    private List<Geofence> mGeofenceList = new ArrayList<>();

    protected GeofenceData () {}

    public static GeofenceData getInstance () {
        if (instance == null) {
            instance = new GeofenceData();
        }

        return instance;
    }

    public void setGeofenceList(List<Geofence> list) {
        this.mGeofenceList = list;
    }

    public List<Geofence> getGeofenceList() {
        return mGeofenceList;
    }

    public GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_DWELL);
        builder.addGeofences(mGeofenceList);
        return builder.build();
    }
}
