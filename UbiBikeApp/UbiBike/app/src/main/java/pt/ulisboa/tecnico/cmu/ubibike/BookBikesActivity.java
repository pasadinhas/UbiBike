package pt.ulisboa.tecnico.cmu.ubibike;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.Map;

import pt.ulisboa.tecnico.cmu.ubibike.domain.Bike;
import pt.ulisboa.tecnico.cmu.ubibike.domain.Station;
import pt.ulisboa.tecnico.cmu.ubibike.listners.DrawerItemClickListner;
import pt.ulisboa.tecnico.cmu.ubibike.map.MapUtil;
import pt.ulisboa.tecnico.cmu.ubibike.rest.BikeServiceREST;
import pt.ulisboa.tecnico.cmu.ubibike.rest.UtilREST;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookBikesActivity extends FragmentActivity implements OnMapReadyCallback {

    private Station station;

    private Map<Marker,String> markers = new HashMap<Marker,String>();

    private Marker selectedMarker = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_bikes);
        station = (Station) getIntent().getSerializableExtra("Station");
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        //Populate UI components
        String[] drawerItems = getResources().getStringArray(R.array.drawer_items);
        ListView listView = (ListView) findViewById(R.id.left_drawer);
        listView.setAdapter(new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, drawerItems));
        listView.setOnItemClickListener(new DrawerItemClickListner(this));
    }

    @Override
    public void onMapReady(GoogleMap map) {
        LatLng latlng = null;
        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                selectedMarker = marker;
                return false;
            }
        });
        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                selectedMarker = null;
            }
        });
        MapUtil.moveToCurrentLocation(map, new LatLng(station.getPosition().getLatitude(), station.getPosition().getLongitude()));
        for(Bike bike : station.getBikes()){
            latlng = new LatLng(bike.getPosition().getLatitude(),bike.getPosition().getLongitude());
            MarkerOptions opt = new MarkerOptions().position(latlng).title("Bike Nrº: " + bike.getIdentifier())
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.marker));
            markers.put(map.addMarker(opt), bike.getIdentifier());
        }
    }

    public void clickBookBike(View v){
        if(selectedMarker == null) {
            Toast.makeText(this,R.string.select_bike, Toast.LENGTH_LONG).show();
            return;
        }
        BikeServiceREST bikeService = UtilREST.getRetrofit().create(BikeServiceREST.class);
        Call<Bike> call = bikeService.bookABike(markers.get(selectedMarker));
        call.enqueue(new Callback<Bike>() {
            @Override
            public void onResponse(Call<Bike> call, Response<Bike> response) {
                if (response.code() == 200) {
                    Toast.makeText(getBaseContext(), R.string.booking_success, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getBaseContext(), R.string.booking_failed, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Bike> call, Throwable t) {
                Toast.makeText(getBaseContext(), R.string.impossible_connect_server, Toast.LENGTH_LONG).show();
                t.printStackTrace();
            }
        });
    }

}
