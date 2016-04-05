package pt.ulisboa.tecnico.cmu.ubibike;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.Date;

import pt.ulisboa.tecnico.cmu.ubibike.domain.Coordinates;
import pt.ulisboa.tecnico.cmu.ubibike.domain.Trajectory;
import pt.ulisboa.tecnico.cmu.ubibike.domain.User;
import pt.ulisboa.tecnico.cmu.ubibike.listners.DrawerItemClickListner;
import pt.ulisboa.tecnico.cmu.ubibike.map.MapUtil;

public class HomeActivity extends Activity implements OnMapReadyCallback {

    private GoogleMap map;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        user = (User)getIntent().getSerializableExtra("User");
        ((TextView)findViewById(R.id.username_textView)).setText(user.getUsername());
        ((TextView)findViewById(R.id.points_textView)).setText("Points: " + user.getPoints());
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.home_map);
        mapFragment.getMapAsync(this);
        //Populate Drawer List
        String[] drawerItems = getResources().getStringArray(R.array.drawer_items);
        ListView listView = (ListView) findViewById(R.id.left_drawer);
        listView.setAdapter(new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, drawerItems));
        listView.setOnItemClickListener(new DrawerItemClickListner(this));
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        //Populate Trajectories Spinner
        Spinner trajectories = (Spinner) findViewById(R.id.spinner_trajectories);
        trajectories.setAdapter(new ArrayAdapter<Trajectory>(this,R.layout.support_simple_spinner_dropdown_item,user.getTrajectories()));
        trajectories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                googleMap.clear();
                Trajectory traj = (Trajectory) parent.getItemAtPosition(position);
                MapUtil.drawTrajectory(googleMap, traj);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //Do Nothing ?
            }
        });
    }
}
