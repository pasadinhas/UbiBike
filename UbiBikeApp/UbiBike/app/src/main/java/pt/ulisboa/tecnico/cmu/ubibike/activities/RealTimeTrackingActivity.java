package pt.ulisboa.tecnico.cmu.ubibike.activities;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;

import pt.ulisboa.tecnico.cmu.ubibike.R;
import pt.ulisboa.tecnico.cmu.ubibike.location.MapTrajectoryDrawing;
import pt.ulisboa.tecnico.cmu.ubibike.services.gps.track.GpsTrackingService;
import pt.ulisboa.tecnico.cmu.ubibike.services.gps.track.IGPSCallback;
import pt.ulisboa.tecnico.cmu.ubibike.services.gps.track.IGPSTrackingService;

public class RealTimeTrackingActivity extends BaseDrawerActivity implements IGPSCallback, OnMapReadyCallback {

    private TextView notCyclingTextView;
    private View mapFragment;

    private IGPSTrackingService serviceS;

    private GoogleMap map;

    private MapTrajectoryDrawing mapDrawing;

    private boolean isRegistered = false;

    @Override
    protected int getPosition() {
        return 4;
    }

    /** Defines callbacks for service binding, passed to bindService() */
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            serviceS = (IGPSTrackingService) service;
            serviceS.registerActivity(RealTimeTrackingActivity.this, RealTimeTrackingActivity.this);
            ((MapFragment)getFragmentManager().findFragmentById(R.id.real_time_map)).getMapAsync(RealTimeTrackingActivity.this);
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mapDrawing.clearMap();
            serviceS = null;
            mapDrawing = null;
            notCyclingTextView.setVisibility(View.VISIBLE);
            mapFragment.setVisibility(View.GONE);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_real_time_tracking);
        notCyclingTextView = (TextView)findViewById(R.id.textView_NotCycling);
        mapFragment = findViewById(R.id.real_time_map);
        //Bind activity with GpsTracking Service if its running
        if(GpsTrackingService.isRunning()){
            isRegistered = true;
            bindService(new Intent(this, GpsTrackingService.class), mConnection, 0);
            notCyclingTextView.setVisibility(View.GONE);
        }
        else{
            mapFragment.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(isRegistered) {
            serviceS.unregisterActivity(this);
            unbindService(mConnection);
        }
    }

    @Override
    public void setLocation(double lat, double lon) {
        if(map != null){
            mapDrawing.addAndDrawPosition(lat,lon);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        mapDrawing = new MapTrajectoryDrawing(map,serviceS.getTrajectory(),getBaseContext());
        mapDrawing.drawInitialMarker();
        mapDrawing.drawTrajectory();
    }
}
