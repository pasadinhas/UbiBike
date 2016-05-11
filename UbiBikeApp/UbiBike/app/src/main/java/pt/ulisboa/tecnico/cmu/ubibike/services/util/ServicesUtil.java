package pt.ulisboa.tecnico.cmu.ubibike.services.util;

import android.content.Context;
import android.content.Intent;

import pt.ulisboa.tecnico.cmu.ubibike.services.UserSynchronizeService;
import pt.ulisboa.tecnico.cmu.ubibike.services.WifiDirectService;
import pt.ulisboa.tecnico.cmu.ubibike.services.gps.track.GpsTrackingService;

/**
 * Created by André on 11-05-2016.
 */
public abstract class ServicesUtil {

    public static void closesAllRunningServices(Context ctx){
        if (WifiDirectService.isRunning()) {
            ctx.stopService(new Intent(ctx, WifiDirectService.class));
        }
        else if(UserSynchronizeService.isRunning()){
            ctx.stopService(new Intent(ctx, UserSynchronizeService.class));
        }
        else if(GpsTrackingService.isRunning()){
            ctx.stopService(new Intent(ctx, GpsTrackingService.class));
        }
    }

}
