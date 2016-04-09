package pt.ulisboa.tecnico.cmu.ubibike;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;

import pt.ulisboa.tecnico.cmu.ubibike.domain.Trajectory;
import pt.ulisboa.tecnico.cmu.ubibike.domain.User;
import pt.ulisboa.tecnico.cmu.ubibike.files.UtilFile;
import pt.ulisboa.tecnico.cmu.ubibike.listners.DrawerItemClickListner;
import pt.ulisboa.tecnico.cmu.ubibike.location.UtilMap;
import pt.ulisboa.tecnico.cmu.ubibike.remote.rest.UserServiceREST;
import pt.ulisboa.tecnico.cmu.ubibike.remote.rest.UtilREST;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends Activity implements OnMapReadyCallback {

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        user = (User)getIntent().getSerializableExtra("User");
        if(user == null){
            user = (User) UtilFile.readFromFile(this,UtilFile.USER_FILE);
        }else{
            UtilFile.writeToFile(this,user,UtilFile.USER_FILE);
        }
        String points = getString(R.string.points ).concat(user.getPoints()+"");
        ((TextView)findViewById(R.id.username_textView)).setText(user.getUsername());
        ((TextView)findViewById(R.id.points_textView)).setText(points);
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.home_map);
        mapFragment.getMapAsync(this);
        //Populate Drawer List
        String[] drawerItems = getResources().getStringArray(R.array.drawer_items);
        ListView listView = (ListView) findViewById(R.id.left_drawer);
        listView.setAdapter(new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, drawerItems));
        listView.setOnItemClickListener(new DrawerItemClickListner(this));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UtilFile.writeToFile(this,user,UtilFile.USER_FILE);
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        //Populate Trajectories Spinner
        Spinner trajectories = (Spinner) findViewById(R.id.spinner_trajectories);
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
                //C'est la vie
            }
        });
    }

    public void sendTrajectoryCloud(View view){
        final Trajectory t = (Trajectory) ((Spinner) findViewById(R.id.spinner_trajectories)).getSelectedItem();
        UserServiceREST userService = UtilREST.getRetrofit().create(UserServiceREST.class);
        Call<User> call = userService.addTrajectory(UtilREST.CONTENT_HEADER,UtilREST.CONTENT_HEADER,
                user.getUsername(),t);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.code() == UtilREST.HTTP_OK){
                    Toast.makeText(getBaseContext(),"Trajectory sent with success",
                            Toast.LENGTH_LONG).show();
                    findViewById(R.id.send_traj_button).setVisibility(View.GONE);
                    t.setAtServer(true);
                }else if(response.code() == UtilREST.HTTP_CONFLICT){
                    Toast.makeText(getBaseContext(),"Trajectory already in cloud",
                            Toast.LENGTH_LONG).show();
                    findViewById(R.id.send_traj_button).setVisibility(View.GONE);
                    t.setAtServer(true);
                }
            }
            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(getBaseContext(),R.string.impossible_connect_server,
                        Toast.LENGTH_LONG).show();
            }
        });
    }

}
