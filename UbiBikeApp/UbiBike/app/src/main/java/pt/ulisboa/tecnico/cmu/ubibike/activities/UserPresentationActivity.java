package pt.ulisboa.tecnico.cmu.ubibike.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;

import pt.ulisboa.tecnico.cmu.ubibike.R;
import pt.ulisboa.tecnico.cmu.ubibike.UbiApp;
import pt.ulisboa.tecnico.cmu.ubibike.domain.Trajectory;
import pt.ulisboa.tecnico.cmu.ubibike.domain.User;
import pt.ulisboa.tecnico.cmu.ubibike.map.MapTrajectoryDrawing;
import pt.ulisboa.tecnico.cmu.ubibike.remote.rest.UserServiceREST;
import pt.ulisboa.tecnico.cmu.ubibike.remote.rest.UtilREST;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserPresentationActivity extends BaseDrawerActivity implements OnMapReadyCallback {

    protected TextView pointsTextView;
    protected TextView usernameTextView;
    protected TextView trajectoriesTextView;
    protected Spinner trajectoriesSpinner;
    protected View map;

    protected User user;

    @Override
    protected int getPosition() {
        return -1;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_presentation);
        trajectoriesSpinner = (Spinner)findViewById(R.id.spinner_trajectories);
        trajectoriesTextView = (TextView)findViewById(R.id.textView_trajectories);
        usernameTextView = (TextView)findViewById(R.id.username_textView);
        pointsTextView = (TextView)findViewById(R.id.points_textView);
        map = findViewById(R.id.home_map);

        user = (User)getIntent().getSerializableExtra("User");
        if (user == null){
            user = UbiApp.getInstance().getUser();
            if(user == null) {
                finish();
                return;
            }
        }
        usernameTextView.setText(user.getUsername());
        MapFragment mapFragment = (MapFragment)getFragmentManager().findFragmentById(R.id.home_map);
        mapFragment.getMapAsync(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        String points = getString(R.string.points_text_view).concat(""+user.getPoints());
        pointsTextView.setText(points);
        if(!user.getAllTrajectories().isEmpty()) {
            trajectoriesSpinner.setAdapter(new ArrayAdapter<>(this, R.layout.custom_row,
                    R.id.information, user.getAllTrajectories()));
            trajectoriesSpinner.setVisibility(View.VISIBLE);
            trajectoriesTextView.setVisibility(View.VISIBLE);
            map.setVisibility(View.VISIBLE);
        }
        else{
            trajectoriesSpinner.setVisibility(View.GONE);
            trajectoriesTextView.setVisibility(View.GONE);
            map.setVisibility(View.GONE);
        }
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        trajectoriesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Trajectory traj = (Trajectory) parent.getItemAtPosition(position);
                 if (traj.getTrajectory().isEmpty()) {                      //Not in memory.
                     getTrajectoryFromServer(traj.getDate(), googleMap);    //Bring from server.
                } else {
                    findViewById(R.id.home_map).setVisibility(View.VISIBLE);
                    MapTrajectoryDrawing mapDrawing = new MapTrajectoryDrawing(googleMap,traj,getBaseContext());
                    mapDrawing.clearMap();
                    mapDrawing.drawInitialMarker();
                    mapDrawing.drawTrajectory();
                    mapDrawing.drawLastMarker();
                    mapDrawing.moveToBegining();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //Nothing selected -> do nothing
            }
        });
    }

    /* Bring an user trajectory from remote server to the application. */
    private void getTrajectoryFromServer(long trajectoryDate,final GoogleMap map){
        UserServiceREST userService = UtilREST.getRetrofit().create(UserServiceREST.class);
        Call<Trajectory> call = userService.getUserTrajectory(user.getUsername(),trajectoryDate);
        call.enqueue(new Callback<Trajectory>() {
            @Override
            public void onResponse(Call<Trajectory> call, Response<Trajectory> response) {
                if (response.code() == UtilREST.HTTP_OK) {
                    findViewById(R.id.home_map).setVisibility(View.VISIBLE);
                    Trajectory t = response.body();
                    user.replaceTrajectory(t);
                    MapTrajectoryDrawing mapDrawing = new MapTrajectoryDrawing(map,t,getBaseContext());
                    mapDrawing.clearMap();
                    mapDrawing.drawInitialMarker();
                    mapDrawing.drawTrajectory();
                    mapDrawing.drawLastMarker();
                    mapDrawing.moveToBegining();
                }
            }
            @Override
            public void onFailure(Call<Trajectory> call, Throwable t) {
                findViewById(R.id.home_map).setVisibility(View.GONE);
                Toast.makeText(getBaseContext(), R.string.impossible_connect_server_toast, Toast.LENGTH_LONG).show();
            }
        });
    }

}
