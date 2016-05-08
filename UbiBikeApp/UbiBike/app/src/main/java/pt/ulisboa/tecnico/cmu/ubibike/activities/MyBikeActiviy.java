package pt.ulisboa.tecnico.cmu.ubibike.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import pt.ulisboa.tecnico.cmu.ubibike.R;

public class MyBikeActiviy extends BaseDrawerActivity {

    @Override
    protected int getPosition() {
        return 6;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_bike_activiy);
    }
}
