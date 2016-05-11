package pt.ulisboa.tecnico.cmu.ubibike.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import pt.ulisboa.tecnico.cmu.ubibike.R;
import pt.ulisboa.tecnico.cmu.ubibike.data.BikeStatusData;
import pt.ulisboa.tecnico.cmu.ubibike.data.UserLoginData;
import pt.ulisboa.tecnico.cmu.ubibike.domain.User;

public class MyBikeActiviy extends BaseDrawerActivity {

    @Override
    protected int getPosition() {
        return 7;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_bike_activiy);

        User user = UserLoginData.getUser(this);
        if (user.hasBike()) {
            TextView bikeNameView = (TextView) findViewById(R.id.textViewBikeName);
            bikeNameView.setText("Bike " + user.getReservedBike().getIdentifier());
            TextView bikeDescription = (TextView) findViewById(R.id.textViewBookHelp);
            bikeDescription.setText("Awesome Bike");
            ImageView image = (ImageView) findViewById(R.id.imageViewBike);
            image.setVisibility(View.VISIBLE);
            TextView isNear = (TextView) findViewById(R.id.textViewNearBike);
            String isNearText = BikeStatusData.getIsNear(this) ? "On Bike" : "Off Bike";
            isNear.setText(isNearText);
        } else {
            ImageView image = (ImageView) findViewById(R.id.imageViewBike);
            image.setVisibility(View.INVISIBLE);
            TextView isNear = (TextView) findViewById(R.id.textViewNearBike);
            isNear.setVisibility(View.INVISIBLE);
        }
    }
}
