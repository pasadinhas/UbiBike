package pt.ulisboa.tecnico.cmu.ubibike.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import pt.ulisboa.tecnico.cmu.ubibike.R;
import pt.ulisboa.tecnico.cmu.ubibike.location.GpsService;

public class TrackTrajectoryDemo extends BaseDrawerActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_trajectory_demo);
    }

    public void startTracking(View v){
        Intent gpsOptionsIntent = new Intent(
                android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(gpsOptionsIntent);
        startService(new Intent(getBaseContext(), GpsService.class));
    }

    public void stopTracking(View v){
        stopService(new Intent(getBaseContext(),GpsService.class));
    }

}
