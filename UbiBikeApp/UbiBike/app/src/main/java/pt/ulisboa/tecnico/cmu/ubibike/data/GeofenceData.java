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

    private String currentStation = null;

    private boolean possibleDropOffWhenLeave = false;

    protected GeofenceData () {}

    public static GeofenceData getInstance () {
        if (instance == null) {
            instance = new GeofenceData();
        }

        return instance;
    }

    public boolean isInStation() {
        return currentStation != null;
    }

    public void enterStation(String name) {
        currentStation = name;
    }

    public void leaveStation() {
        currentStation = null;
    }

    public String getCurrentStation() {
        return currentStation;
    }

    public void setGeofenceList(List<Geofence> list) {
        this.mGeofenceList = list;
    }

    public List<Geofence> getGeofenceList() {
        return mGeofenceList;
    }

    public GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofences(mGeofenceList);
        return builder.build();
    }

    public void setPossibleDropOffWhenLeave(boolean status) {
        possibleDropOffWhenLeave = status;
    }

    public boolean getPossibleDropOffWhenLeave() {
        return possibleDropOffWhenLeave;
    }
}
