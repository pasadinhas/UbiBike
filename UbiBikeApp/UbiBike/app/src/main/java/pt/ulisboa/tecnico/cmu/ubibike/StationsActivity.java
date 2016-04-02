package pt.ulisboa.tecnico.cmu.ubibike;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import pt.ulisboa.tecnico.cmu.ubibike.domain.Station;
import pt.ulisboa.tecnico.cmu.ubibike.domain.User;
import pt.ulisboa.tecnico.cmu.ubibike.listners.DrawerItemClickListner;
import pt.ulisboa.tecnico.cmu.ubibike.listners.StationItemClickListner;
import pt.ulisboa.tecnico.cmu.ubibike.rest.StationServiceREST;
import pt.ulisboa.tecnico.cmu.ubibike.rest.UserServiceREST;
import pt.ulisboa.tecnico.cmu.ubibike.rest.UtilREST;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StationsActivity extends Activity {

    private Activity currentActivity = this;

    private List<Station> stations = new ArrayList<Station>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getStations();
        setContentView(R.layout.activity_stations);
        ((ListView)findViewById(R.id.stations_listView)).setOnItemClickListener(new StationItemClickListner(this));
        //Populate UI components
        String[] drawerItems = getResources().getStringArray(R.array.drawer_items);
        ListView listView = (ListView) findViewById(R.id.left_drawer);
        listView.setAdapter(new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, drawerItems));
        listView.setOnItemClickListener(new DrawerItemClickListner(this));
    }

    // Get station information from the server.
    private void getStations(){
        StationServiceREST stationService = UtilREST.getRetrofit().create(StationServiceREST.class);
        Call<List<Station>> call = stationService.getStations();
        call.enqueue(new Callback<List<Station>>() {
            @Override
            public void onResponse(Call<List<Station>> call, Response<List<Station>> response) {
                if(response.code() == 200){
                    stations = response.body();
                    ListView view = (ListView)findViewById(R.id.stations_listView);
                    view.setAdapter(new ArrayAdapter<Station>(currentActivity,R.layout.support_simple_spinner_dropdown_item,stations));
                }
            }
            @Override
            public void onFailure(Call<List<Station>> call, Throwable t) {
                Toast.makeText(getBaseContext(),R.string.impossible_connect_server,Toast.LENGTH_LONG).show();
            }
        });
    }

}
