package pt.ulisboa.tecnico.cmu.ubibike;

import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.style.ScaleXSpan;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import pt.ulisboa.tecnico.cmu.ubibike.domain.Bike;
import pt.ulisboa.tecnico.cmu.ubibike.domain.Station;

public class StationBikesActivity extends FragmentActivity implements OnMapReadyCallback{

    private GoogleMap map ;

    private Station station;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_bikes);
        station = (Station) getIntent().getSerializableExtra("Station");
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng latlng = null;
        map = googleMap;
        map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        for(Bike bike : station.getBikes()){
            latlng = new LatLng(bike.getPosition().getLatitude(),bike.getPosition().getLongitude());
            map.addMarker(new MarkerOptions()
                    .position(latlng)
                    .title("Bike Nrº: " + bike.getId()));
        }
        moveToCurrentLocation(map,latlng);
    }

    private void moveToCurrentLocation(GoogleMap googleMap,LatLng currentLocation)
    {
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));
        // Zoom in, animating the camera.
        googleMap.animateCamera(CameraUpdateFactory.zoomIn());
        // Zoom out to zoom level 10, animating with a duration of 2 seconds.
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
    }
}
