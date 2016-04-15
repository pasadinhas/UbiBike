package pt.ulisboa.tecnico.cmu.ubibike.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import pt.ulisboa.tecnico.cmu.ubibike.data.UserLoginData;
import pt.ulisboa.tecnico.cmu.ubibike.domain.Trajectory;
import pt.ulisboa.tecnico.cmu.ubibike.domain.User;
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
        super.onDestroy();
        gpsTracking.stopLocationUpdates();
        gpsTracking.disconnect();
        saveTrajectory(gpsTracking.getTrajectory());
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    //Persist new trajectory and try synchronize with remote server.
    private void saveTrajectory(Trajectory t){
        User user = UserLoginData.getUser(getBaseContext());
        if(user != null){
            user.addLocalTrajectory(t);
            user.setIsDirty(true);
            user.addUserPoints(Math.round(t.getTotalMeters()));
            UserLoginData.setUser(getBaseContext(),user);
            Intent intent = new Intent();
            intent.setAction(UserUpdateService.SYNCHRONIZE_USER_INTENT);
            sendBroadcast(intent);
        }
    }

}
