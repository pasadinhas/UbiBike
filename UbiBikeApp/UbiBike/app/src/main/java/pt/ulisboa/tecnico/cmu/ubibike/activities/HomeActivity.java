package pt.ulisboa.tecnico.cmu.ubibike.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;

import java.util.Collections;

import pt.ulisboa.tecnico.cmu.ubibike.R;
import pt.ulisboa.tecnico.cmu.ubibike.data.UserData;
import pt.ulisboa.tecnico.cmu.ubibike.domain.Trajectory;
import pt.ulisboa.tecnico.cmu.ubibike.domain.User;
import pt.ulisboa.tecnico.cmu.ubibike.location.UtilMap;
import pt.ulisboa.tecnico.cmu.ubibike.remote.rest.UserServiceREST;
import pt.ulisboa.tecnico.cmu.ubibike.remote.rest.UtilREST;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends BaseDrawerActivity implements OnMapReadyCallback {

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        user = (User)getIntent().getSerializableExtra("User");
        if(user == null){
            user = UserData.getUserData(this);
        }
        if(user.getIsDirty()){
            Log.d("Dirty", "Dirty");
        }
        String points = getString(R.string.points ).concat(user.getPoints()+"");
        ((TextView)findViewById(R.id.username_textView)).setText(user.getUsername());
        ((TextView)findViewById(R.id.points_textView)).setText(points);
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.home_map);
        if(!user.getTrajectories().isEmpty()) {
            mapFragment.getMapAsync(this);
        }
        else{
            findViewById(R.id.spinner_trajectories).setVisibility(View.GONE);
            findViewById(R.id.send_traj_button).setVisibility(View.GONE);
            mapFragment.getView().setVisibility(View.GONE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UserData.saveUserData(this);
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        //Populate Trajectories Spinner
        Spinner trajectories = (Spinner) findViewById(R.id.spinner_trajectories);
        Collections.sort(user.getTrajectories());
        trajectories.setAdapter(new ArrayAdapter<>(this,
                R.layout.support_simple_spinner_dropdown_item, user.getTrajectories()));
        trajectories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                googleMap.clear();
                Trajectory traj = (Trajectory) parent.getItemAtPosition(position);
                Button button = (Button) findViewById(R.id.send_traj_button);
                if (!traj.getAtServer()) {
                    button.setVisibility(View.VISIBLE);
                } else {
                    button.setVisibility(View.GONE);
                }
                UtilMap.drawTrajectory(googleMap, traj);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //Do Nothing :)
            }
        });
    }

    public void sendTrajectoryCloud(View view){
        final Trajectory t = (Trajectory)((Spinner) findViewById(R.id.spinner_trajectories)).getSelectedItem();
        UserServiceREST userService = UtilREST.getRetrofit().create(UserServiceREST.class);
        Call<User> call = userService.addTrajectory(UtilREST.CONTENT_HEADER,UtilREST.CONTENT_HEADER,
                user.getUsername(),t);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.code() == UtilREST.HTTP_OK){
                    Toast.makeText(getBaseContext(),R.string.send_trajectory_success_toast,
                            Toast.LENGTH_LONG).show();
                    findViewById(R.id.send_traj_button).setVisibility(View.GONE);
                    t.setAtServer(true);
                }else if(response.code() == UtilREST.HTTP_CONFLICT){
                    Toast.makeText(getBaseContext(), R.string.send_trajectory_failed_toast,
                            Toast.LENGTH_LONG).show();
                    findViewById(R.id.send_traj_button).setVisibility(View.GONE);
                    t.setAtServer(true);
                }
                UserData.saveUserData(getBaseContext());
            }
            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(getBaseContext(),R.string.impossible_connect_server,
                        Toast.LENGTH_LONG).show();
            }
        });
    }

}
