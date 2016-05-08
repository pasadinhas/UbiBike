package pt.ulisboa.tecnico.cmu.ubibike.services.gps;

import android.app.Activity;

import pt.ulisboa.tecnico.cmu.ubibike.domain.Trajectory;

/**
 * Created by André on 07-05-2016.
 */
public interface IGPSTrackingService {

    void registerActivity(Activity activity, IGPSCallback callback);

    void unregisterActivity(Activity activity);

    Trajectory getTrajectory();

}
