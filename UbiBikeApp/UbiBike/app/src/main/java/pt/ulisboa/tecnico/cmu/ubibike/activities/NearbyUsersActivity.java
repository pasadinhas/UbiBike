package pt.ulisboa.tecnico.cmu.ubibike.activities;

import android.os.Bundle;

import pt.ulisboa.tecnico.cmu.ubibike.R;

public class NearbyUsersActivity extends BaseDrawerActivity {

    @Override
    protected int getPosition() {
        return 1;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby_users);
    }
}
