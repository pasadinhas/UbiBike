package pt.ulisboa.tecnico.cmu.ubibike.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;

import pt.ulisboa.tecnico.cmu.ubibike.R;
import pt.ulisboa.tecnico.cmu.ubibike.data.UserLoginData;
import pt.ulisboa.tecnico.cmu.ubibike.domain.Trajectory;
import pt.ulisboa.tecnico.cmu.ubibike.domain.User;
import pt.ulisboa.tecnico.cmu.ubibike.location.UtilMap;

public class HomeActivity extends BaseDrawerActivity implements OnMapReadyCallback {

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    @Override
    protected void onResume() {
        super.onResume();
        user = UserLoginData.getUser(getBaseContext());
        if(user == null){
            finish();
            startActivity(new Intent(getBaseContext(), LoginActivity.class));
            return;
        }
        String points = getString(R.string.points).concat(user.getPoints()+"");
        ((TextView)findViewById(R.id.username_textView)).setText(user.getUsername());
        ((TextView)findViewById(R.id.points_textView)).setText(points);
        if(!user.getAllTrajectories().isEmpty()) {
            MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.home_map);
            mapFragment.getMapAsync(this);
            findViewById(R.id.spinner_trajectories).setVisibility(View.VISIBLE);
            findViewById(R.id.textView_trajectories).setVisibility(View.VISIBLE);
            findViewById(R.id.home_map).setVisibility(View.VISIBLE);
        }
        else{
            findViewById(R.id.spinner_trajectories).setVisibility(View.GONE);
            findViewById(R.id.textView_trajectories).setVisibility(View.GONE);
            findViewById(R.id.home_map).setVisibility(View.GONE);
        }
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        //Populate Trajectories Spinner
        Spinner trajectories = (Spinner) findViewById(R.id.spinner_trajectories);
        trajectories.setAdapter(new ArrayAdapter<>(this,R.layout.custom_row,R.id.information
                , user.getAllTrajectories()));
        trajectories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                googleMap.clear();
                Trajectory traj = (Trajectory) parent.getItemAtPosition(position);
                UtilMap.drawTrajectory(googleMap, traj);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //Do Nothing :)
            }
        });
    }


}
