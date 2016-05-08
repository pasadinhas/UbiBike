package pt.ulisboa.tecnico.cmu.ubibike.services.gps;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import pt.ulisboa.tecnico.cmu.ubibike.data.UserLoginData;
import pt.ulisboa.tecnico.cmu.ubibike.domain.Trajectory;
import pt.ulisboa.tecnico.cmu.ubibike.domain.User;
import pt.ulisboa.tecnico.cmu.ubibike.location.GpsTracking;
import pt.ulisboa.tecnico.cmu.ubibike.services.UserUpdateService;


public class GpsTrackingService extends Service implements IGPSCallback {

    private GpsTracking gpsTracking;

    private final GpsTrackingBinder localBinder = new GpsTrackingBinder();

    public static boolean RUNNING = false;

    /* Save all interested activities and the respective callbacks */
    private Map<Activity,IGPSCallback> activityClients = new ConcurrentHashMap<>();

    /* Return Binder (interface) to be used by clients to commnicate with running service. */
    public class GpsTrackingBinder extends Binder implements IGPSTrackingService {
        @Override
        public void registerActivity(Activity activity, IGPSCallback callback) {
            activityClients.put(activity,callback);
        }

        @Override
        public void unregisterActivity(Activity activity) {
            activityClients.remove(activity);
        }

        @Override
        public Trajectory getTrajectory() {
            return gpsTracking.getTrajectory();
        }
    }

    /*Called by GPSTracking when a new location is obtained. */
    @Override
    public void setLocation(double lat, double lon) {
        for (Activity client : activityClients.keySet()) {
            IGPSCallback callback = activityClients.get(client);
            callback.setLocation(lat, lon);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        gpsTracking = new GpsTracking(getBaseContext(),this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        gpsTracking.connect();
        RUNNING = true;
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RUNNING = false;
        gpsTracking.stopLocationUpdates();
        gpsTracking.disconnect();
        saveTrajectory(gpsTracking.getTrajectory());
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return localBinder;
    }

    //Persist new trajectory and try synchronize with remote server.
    private void saveTrajectory(Trajectory t){
        User user = UserLoginData.getUser(getBaseContext());
        if(user != null){
            user.addLocalTrajectory(t);
            UserLoginData.setUser(getBaseContext(),user);
            Intent intent = new Intent();
            intent.setAction(UserUpdateService.SYNCHRONIZE_USER_INTENT);
            sendBroadcast(intent);
        }
    }

}
