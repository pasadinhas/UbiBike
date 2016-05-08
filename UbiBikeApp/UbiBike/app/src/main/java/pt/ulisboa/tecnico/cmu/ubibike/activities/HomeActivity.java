package pt.ulisboa.tecnico.cmu.ubibike.activities;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.android.gms.maps.GoogleMap;

import pt.ulisboa.tecnico.cmu.ubibike.R;
import pt.ulisboa.tecnico.cmu.ubibike.domain.Coordinates;
import pt.ulisboa.tecnico.cmu.ubibike.domain.Trajectory;
import pt.ulisboa.tecnico.cmu.ubibike.location.MapTrajectoryDrawing;


public class HomeActivity extends UserPresentationActivity {

    @Override
    protected int getPosition() {
        return 0;
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        //Populate Trajectories Spinner
        Spinner trajectories = (Spinner) findViewById(R.id.spinner_trajectories);
        trajectories.setAdapter(new ArrayAdapter<>(this,R.layout.custom_row,R.id.information,
                user.getAllTrajectories()));
        trajectories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
                //C'est la vie
            }
        });
    }

}
