package pt.ulisboa.tecnico.cmu.ubibike.activities;

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

public class UserPresentationActivity extends BaseDrawerActivity implements OnMapReadyCallback {

    private User user;

    @Override
    protected int getPosition() {
        return -1;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_presentation);
    }

    @Override
    protected void onResume() {
        super.onResume();
        user = (User)getIntent().getSerializableExtra("User");
        if (user == null){
            user = UserLoginData.getUser(getBaseContext());
            if(user == null) {
                finish();
                return;
            }
        }
        ((TextView)findViewById(R.id.username_textView)).setText(user.getUsername());
        String points = getString(R.string.points_text_view).concat(user.getPoints() + "");
        ((TextView)findViewById(R.id.points_textView)).setText(points);
        if(!user.getAllTrajectories().isEmpty()) {
            MapFragment mapFragment = (MapFragment)getFragmentManager().findFragmentById(R.id.home_map);
            mapFragment.getMapAsync(this);
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
        trajectories.setAdapter(new ArrayAdapter<>(this,R.layout.custom_row,R.id.information,
                user.getAllTrajectories()));
        trajectories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                googleMap.clear();
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
