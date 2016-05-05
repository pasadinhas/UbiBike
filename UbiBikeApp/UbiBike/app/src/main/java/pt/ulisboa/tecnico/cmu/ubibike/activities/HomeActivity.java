package pt.ulisboa.tecnico.cmu.ubibike.activities;


import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;

import java.util.Date;

import pt.ulisboa.tecnico.cmu.ubibike.R;
import pt.ulisboa.tecnico.cmu.ubibike.domain.Trajectory;
import pt.ulisboa.tecnico.cmu.ubibike.domain.User;
import pt.ulisboa.tecnico.cmu.ubibike.location.UtilMap;
import pt.ulisboa.tecnico.cmu.ubibike.remote.rest.UserServiceREST;
import pt.ulisboa.tecnico.cmu.ubibike.remote.rest.UtilREST;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
                UtilMap.drawTrajectory(googleMap, traj);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //C'est la vie
            }
        });
    }

}
