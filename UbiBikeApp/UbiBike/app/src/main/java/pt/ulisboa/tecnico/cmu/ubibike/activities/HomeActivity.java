package pt.ulisboa.tecnico.cmu.ubibike.activities;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.google.android.gms.maps.GoogleMap;

import pt.ulisboa.tecnico.cmu.ubibike.R;
import pt.ulisboa.tecnico.cmu.ubibike.domain.Trajectory;
import pt.ulisboa.tecnico.cmu.ubibike.map.MapTrajectoryDrawing;
import pt.ulisboa.tecnico.cmu.ubibike.services.util.ServicesUtil;


public class HomeActivity extends UserPresentationActivity {

    @Override
    protected int getPosition() {
        return 0;
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        Log.d("MAP", "MAP READY!!");
        trajectoriesSpinner.setAdapter(new ArrayAdapter<>(this, R.layout.custom_trajectory_row,
                R.id.information, user.getAllTrajectories()));
        trajectoriesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Trajectory traj = (Trajectory) parent.getItemAtPosition(position);
                MapTrajectoryDrawing mapDrawing = new MapTrajectoryDrawing(googleMap,traj,getBaseContext());
                mapDrawing.clearMap();
                mapDrawing.drawInitialMarker();
                mapDrawing.drawTrajectory();
                mapDrawing.drawLastMarker();
                mapDrawing.moveToBegining();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //Nothing selected -> do nothing
            }
        });
    }

    /* IMPORTANT: Before app closes this is always executed. */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        ServicesUtil.closesAllRunningServices(getBaseContext());
    }
}
