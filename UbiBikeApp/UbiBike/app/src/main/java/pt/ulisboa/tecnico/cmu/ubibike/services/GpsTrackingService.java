package pt.ulisboa.tecnico.cmu.ubibike.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import pt.ulisboa.tecnico.cmu.ubibike.data.UserData;
import pt.ulisboa.tecnico.cmu.ubibike.domain.Trajectory;
import pt.ulisboa.tecnico.cmu.ubibike.domain.User;
import pt.ulisboa.tecnico.cmu.ubibike.data.files.UtilFile;
import pt.ulisboa.tecnico.cmu.ubibike.location.GpsTracking;


public class GpsTrackingService extends Service {

    private GpsTracking gpsTracking;

    @Override
    public void onCreate() {
        super.onCreate();
        gpsTracking = new GpsTracking(getBaseContext());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        gpsTracking.connect();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        gpsTracking.stopLocationUpdates();
        gpsTracking.disconnect();
        saveTrajectory(gpsTracking.getTrajectory());
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void saveTrajectory(Trajectory t){
        User user = UserData.getUserData(this);
        user.addTrajectory(t);
        user.setIsDirty(true);
        user.updateUserPoints(Math.round(t.getTotalMeters()));
        UserData.saveUserData(this);
    }

}
