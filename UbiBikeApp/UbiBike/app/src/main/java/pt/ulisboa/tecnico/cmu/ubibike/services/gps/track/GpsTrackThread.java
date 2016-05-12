package pt.ulisboa.tecnico.cmu.ubibike.services.gps.track;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

/**
 * Created by André on 12-05-2016.
 */
public class GpsTrackThread  extends Thread {

    private GpsTracking tracker;

    public Handler mHandler;

    public Context ctx;

    public IGPSCallback callback;

    public GpsTrackThread(Context ctx,IGPSCallback callback){
        this.ctx = ctx;
        this.callback = callback;
    }

    @Override
    public void run() {

        Looper.prepare();

        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                // process incoming messages here
                Log.d("WOOOOR", "WOOOOOT");
            }
        };
        tracker = new GpsTracking(ctx,callback);
        tracker.connect();
        Looper.loop();
    }
}
