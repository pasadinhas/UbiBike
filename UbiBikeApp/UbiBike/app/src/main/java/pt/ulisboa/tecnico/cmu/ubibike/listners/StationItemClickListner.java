package pt.ulisboa.tecnico.cmu.ubibike.listners;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import pt.ulisboa.tecnico.cmu.ubibike.GetUserInformationActivity;
import pt.ulisboa.tecnico.cmu.ubibike.HomeActivity;
import pt.ulisboa.tecnico.cmu.ubibike.StationBikesActivity;
import pt.ulisboa.tecnico.cmu.ubibike.StationsActivity;
import pt.ulisboa.tecnico.cmu.ubibike.domain.Station;

/**
 * Created by André on 26-03-2016.
 */
public class StationItemClickListner implements ListView.OnItemClickListener {

    private Activity current;

    public StationItemClickListner(Activity current){
        this.current = current;
    }

    @Override
    public void onItemClick(AdapterView parent, View view, int position, long id) {
        Station station = (Station) parent.getItemAtPosition(position);
        Intent intent = new Intent(current, StationBikesActivity.class);
        intent.putExtra("Station",station);
        current.startActivity(intent);
    }

}
