package pt.ulisboa.tecnico.cmu.ubibike.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;

import pt.ulisboa.tecnico.cmu.ubibike.R;
import pt.ulisboa.tecnico.cmu.ubibike.data.UserLoginData;

public abstract class BaseDrawerActivity extends Activity{

    protected DrawerLayout fullLayout;

    protected FrameLayout subLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView(int layoutResID) {
        fullLayout = (DrawerLayout) getLayoutInflater().inflate(R.layout.activity_base_drawer,null);
        subLayout = (FrameLayout) fullLayout.findViewById(R.id.content_frame);
        getLayoutInflater().inflate(layoutResID, subLayout, true);
        super.setContentView(fullLayout);
        //Populate Drawer List
        String[] drawerItems = getResources().getStringArray(R.array.drawer_items);
        ListView listView = (ListView) findViewById(R.id.left_drawer);
        listView.setAdapter(new ArrayAdapter<>(this,
                R.layout.support_simple_spinner_dropdown_item, drawerItems));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = null;
            Intent intentHome = new Intent(getBaseContext(), HomeActivity.class);
            intentHome.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION |
                Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_SINGLE_TOP);
            if (!this.getClass().equals(HomeActivity.class) && position == 0) {
                intent = new Intent(getBaseContext(), HomeActivity.class);
            } else if (!this.getClass().equals(NearbyUsersActivity.class) && position == 1) {
                intent = new Intent(getBaseContext(), NearbyUsersActivity.class);
            } else if (!this.getClass().equals(SearchUserActivity.class) && position == 2) {
                intent = new Intent(getBaseContext(), SearchUserActivity.class);
            } else if (!this.getClass().equals(StationsActivity.class) && position == 3) {
                intent = new Intent(getBaseContext(), StationsActivity.class);
            } else if (position == 4) {
                intent = new Intent(getBaseContext(), LoginActivity.class);
                UserLoginData.clearUserLoggedIn(getBaseContext());
            } else if (!this.getClass().equals(TrackTrajectoryDemo.class) && position == 5) {
                intent = new Intent(getBaseContext(), TrackTrajectoryDemo.class);
            }
            if (intent != null) {
                startActivity(intentHome);
                if (position != 0) {
                    ((DrawerLayout)findViewById(R.id.drawer_layout)).closeDrawers();
                    startActivity(intent);
                }
            }
            }
        });
    }

}
