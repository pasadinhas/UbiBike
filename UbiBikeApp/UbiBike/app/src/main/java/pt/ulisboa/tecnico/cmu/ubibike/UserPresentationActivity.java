package pt.ulisboa.tecnico.cmu.ubibike;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;

import pt.ulisboa.tecnico.cmu.ubibike.domain.Trajectory;
import pt.ulisboa.tecnico.cmu.ubibike.domain.User;
import pt.ulisboa.tecnico.cmu.ubibike.listners.DrawerItemClickListner;
import pt.ulisboa.tecnico.cmu.ubibike.location.UtilMap;

public class UserPresentationActivity extends Activity implements OnMapReadyCallback {

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_presentation);
        user = (User)getIntent().getSerializableExtra("User");
        if (user == null) return;

        ((TextView)findViewById(R.id.username_textView)).setText(user.getUsername());
        String points = getString(R.string.points).concat(user.getPoints() + "");
        ((TextView)findViewById(R.id.points_textView)).setText(points);
        //Populate Drawer List
        String[] drawerItems = getResources().getStringArray(R.array.drawer_items);
        ListView listView = (ListView) findViewById(R.id.left_drawer);
        listView.setAdapter(new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, drawerItems));
        listView.setOnItemClickListener(new DrawerItemClickListner(this));

        MapFragment mapFragment = (MapFragment)getFragmentManager().findFragmentById(R.id.home_map);
        if(!user.getTrajectories().isEmpty()) {
            mapFragment.getMapAsync(this);
        }
        else{
            ((Spinner) findViewById(R.id.spinner_trajectories)).setVisibility(View.GONE);
            mapFragment.getView().setVisibility(View.GONE);
        }
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
                UtilMap.drawTrajectory(googleMap, traj);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //C'est la vie
            }
        });
    }
}
