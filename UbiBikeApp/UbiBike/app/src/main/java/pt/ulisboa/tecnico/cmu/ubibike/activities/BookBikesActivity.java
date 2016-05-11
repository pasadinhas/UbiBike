package pt.ulisboa.tecnico.cmu.ubibike.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.Map;

import pt.ulisboa.tecnico.cmu.ubibike.R;
import pt.ulisboa.tecnico.cmu.ubibike.data.UserLoginData;
import pt.ulisboa.tecnico.cmu.ubibike.domain.Bike;
import pt.ulisboa.tecnico.cmu.ubibike.domain.Station;
import pt.ulisboa.tecnico.cmu.ubibike.domain.User;
import pt.ulisboa.tecnico.cmu.ubibike.location.MapTrajectoryDrawing;
import pt.ulisboa.tecnico.cmu.ubibike.remote.rest.UserServiceREST;
import pt.ulisboa.tecnico.cmu.ubibike.remote.rest.UtilREST;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookBikesActivity extends BaseDrawerActivity implements OnMapReadyCallback {

    private Station station;

    private Map<Marker,Bike> markers = new HashMap<>();

    private Marker selectedMarker = null;

    private User user;

    @Override
    protected int getPosition() {
        return -1;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_bikes);
        station = (Station) getIntent().getSerializableExtra("Station");
        user = UserLoginData.getUser(getBaseContext());
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        LatLng latlng;
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
        MapTrajectoryDrawing.moveToCurrentLocation(map, new LatLng(station.getPosition().getLatitude(),
                station.getPosition().getLongitude()));
        for(Bike bike : station.getBikes()){
            latlng = new LatLng(bike.getPosition().getLatitude(),bike.getPosition().getLongitude());
            MarkerOptions opt = new MarkerOptions().position(latlng).title("Bike: " + bike.getIdentifier())
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.marker));
            markers.put(map.addMarker(opt), bike);
        }
    }

    public void clickBookBike(View v){
        if(selectedMarker == null) {
            Toast.makeText(this,R.string.select_bike_toast, Toast.LENGTH_LONG).show();
            return;
        }
        UserServiceREST userService = UtilREST.getRetrofit().create(UserServiceREST.class);
        Call<Bike> call = userService.bookBike(user.getUsername(),markers.get(selectedMarker).getIdentifier());
        call.enqueue(new Callback<Bike>() {
            @Override
            public void onResponse(Call<Bike> call, Response<Bike> response) {
                if (response.code() == UtilREST.HTTP_OK) {
                    Toast.makeText(getBaseContext(), R.string.booking_success_toast + " " + markers.get(selectedMarker).getIdentifier(), Toast.LENGTH_LONG).show();
                    user.setReservedBike(response.body());
                    UserLoginData.setUser(getBaseContext(),user);
                    station.removeBike(markers.get(selectedMarker));
                    selectedMarker.remove();
                    markers.remove(selectedMarker);
                    selectedMarker = null;
                } else {
                    Toast.makeText(getBaseContext(), R.string.booking_failed_toast, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Bike> call, Throwable t) {
                Toast.makeText(getBaseContext(), R.string.impossible_connect_server_toast, Toast.LENGTH_LONG).show();
            }
        });
    }

}
