package pt.ulisboa.tecnico.cmu.ubibike.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;

import java.util.Date;

import pt.ulisboa.tecnico.cmu.ubibike.R;
import pt.ulisboa.tecnico.cmu.ubibike.data.UserLoginData;
import pt.ulisboa.tecnico.cmu.ubibike.domain.Trajectory;
import pt.ulisboa.tecnico.cmu.ubibike.domain.User;
import pt.ulisboa.tecnico.cmu.ubibike.location.UtilMap;
import pt.ulisboa.tecnico.cmu.ubibike.remote.rest.UserServiceREST;
import pt.ulisboa.tecnico.cmu.ubibike.remote.rest.UtilREST;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserPresentationActivity extends BaseDrawerActivity implements OnMapReadyCallback {

    protected User user;

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
                Trajectory traj = (Trajectory) parent.getItemAtPosition(position);
                if (traj.getTrajectory() == null) {                     //Not in memory.
                    getTrajectoryFromServer(traj.getDate(), googleMap); //Bring from server.
                } else {
                    UtilMap.drawTrajectory(googleMap, traj);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //C'est la vie
            }
        });
    }

    private void getTrajectoryFromServer(Date trajectoryDate,final GoogleMap map){
        UserServiceREST userService = UtilREST.getRetrofit().create(UserServiceREST.class);
        Call<Trajectory> call = userService.getUserTrajectory(user.getUsername(),trajectoryDate.getTime());
        call.enqueue(new Callback<Trajectory>() {
            @Override
            public void onResponse(Call<Trajectory> call, Response<Trajectory> response) {
                if (response.code() == UtilREST.HTTP_OK) {
                    Trajectory t = response.body();
                    user.replaceTrajectory(t);
                    UtilMap.drawTrajectory(map, t);
                }
            }
            @Override
            public void onFailure(Call<Trajectory> call, Throwable t) {
                Toast.makeText(getBaseContext(), R.string.impossible_connect_server_toast,
                        Toast.LENGTH_LONG).show();
            }
        });
    }

}
