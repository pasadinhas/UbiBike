package pt.ulisboa.tecnico.cmu.ubibike.location;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import pt.ulisboa.tecnico.cmu.ubibike.domain.Trajectory;
import pt.ulisboa.tecnico.cmu.ubibike.domain.User;
import pt.ulisboa.tecnico.cmu.ubibike.data.files.UtilFile;


public class GpsService extends Service {

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
        User user = (User) UtilFile.readFromFile(getBaseContext(),UtilFile.USER_FILE);
        if(user != null) user.addTrajectory(t);
        UtilFile.writeToFile(getBaseContext(),user,UtilFile.USER_FILE);
    }

}
