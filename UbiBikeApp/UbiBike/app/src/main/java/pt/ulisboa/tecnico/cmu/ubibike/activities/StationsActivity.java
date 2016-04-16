package pt.ulisboa.tecnico.cmu.ubibike.activities;

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
import pt.ulisboa.tecnico.cmu.ubibike.domain.Station;
import pt.ulisboa.tecnico.cmu.ubibike.remote.rest.StationServiceREST;
import pt.ulisboa.tecnico.cmu.ubibike.remote.rest.UtilREST;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StationsActivity extends BaseDrawerActivity {

    private List<Station> stations = new ArrayList<>();

    @Override
    protected int getPosition() {
        return 3;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stations);
        ((ListView)findViewById(R.id.stations_listView)).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Station station = (Station) parent.getItemAtPosition(position);
            Intent intent = new Intent(getBaseContext(), BookBikesActivity.class);
            intent.putExtra("Station", station);
            startActivity(intent);
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
        StationServiceREST stationService = UtilREST.getRetrofit().create(StationServiceREST.class);
        Call<List<Station>> call = stationService.getStations();
        call.enqueue(new Callback<List<Station>>() {
            @Override
            public void onResponse(Call<List<Station>> call, Response<List<Station>> response) {
                if(response.code() == UtilREST.HTTP_OK){
                    stations = response.body();
                    ListView view = (ListView)findViewById(R.id.stations_listView);
                    view.setAdapter(new ArrayAdapter<>(getBaseContext(),
                            R.layout.support_simple_spinner_dropdown_item,stations));
                }
            }
            @Override
            public void onFailure(Call<List<Station>> call, Throwable t) {
                ListView view = (ListView) findViewById(R.id.stations_listView);
                view.setAdapter(null);
                Toast.makeText(getBaseContext(),R.string.impossible_connect_server_toast,Toast.LENGTH_LONG).show();
            }
        });
    }

}
