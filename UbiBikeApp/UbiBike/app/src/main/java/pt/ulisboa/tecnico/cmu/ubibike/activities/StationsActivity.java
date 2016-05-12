package pt.ulisboa.tecnico.cmu.ubibike.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import pt.ulisboa.tecnico.cmu.ubibike.R;
import pt.ulisboa.tecnico.cmu.ubibike.activities.custom.StationArrayAdapter;
import pt.ulisboa.tecnico.cmu.ubibike.domain.Station;
import pt.ulisboa.tecnico.cmu.ubibike.remote.rest.StationServiceREST;
import pt.ulisboa.tecnico.cmu.ubibike.remote.rest.UtilREST;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StationsActivity extends BaseDrawerActivity {

    private Activity currentActivity;

    protected ListView stationsListView;

    private List<Station> stations = new ArrayList<>();

    @Override
    protected int getPosition() {
        return 3;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stations);
        currentActivity = this;
        stationsListView = (ListView)findViewById(R.id.stations_listView);
        stationsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position != 0) { //NOT header
                    Station station = (Station) parent.getItemAtPosition(position);
                    Intent intent = new Intent(getBaseContext(), BookBikesActivity.class);
                    intent.putExtra("Station", station);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getStations();
    }

    // Get station information from the remote server.
    private void getStations(){
        final StationServiceREST stationService = UtilREST.getRetrofit().create(StationServiceREST.class);
        Call<List<Station>> call = stationService.getStations(StationServiceREST.STATION_DETAIL_HIGH);
        call.enqueue(new Callback<List<Station>>() {
            @Override
            public void onResponse(Call<List<Station>> call, Response<List<Station>> response) {
                if(response.code() == UtilREST.HTTP_OK){
                    stations = response.body();
                    if(stationsListView.getHeaderViewsCount() == 0) {
                        stationsListView.addHeaderView(getLayoutInflater().inflate(R.layout.custom_header_station, null,false));
                    }
                    stationsListView.setAdapter(new StationArrayAdapter(currentActivity,
                            R.layout.custom_row_station,stations));
                }
            }
            @Override
            public void onFailure(Call<List<Station>> call, Throwable t) {
                stationsListView.setAdapter(null);
                Toast.makeText(getBaseContext(),R.string.impossible_connect_server_toast,Toast.LENGTH_LONG).show();
            }
        });
    }

}
