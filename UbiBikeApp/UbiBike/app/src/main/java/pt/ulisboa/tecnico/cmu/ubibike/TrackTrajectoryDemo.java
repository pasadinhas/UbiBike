package pt.ulisboa.tecnico.cmu.ubibike;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import pt.ulisboa.tecnico.cmu.ubibike.location.GpsService;

public class TrackTrajectoryDemo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_trajectory_demo);
    }

    public void startTracking(View v){
        startService(new Intent(getBaseContext(), GpsService.class));
    }

    public void stopTracking(View v){
        stopService(new Intent(getBaseContext(),GpsService.class));
    }

}
