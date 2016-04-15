package pt.ulisboa.tecnico.cmu.ubibike.location;

import android.graphics.Color;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.List;

import pt.ulisboa.tecnico.cmu.ubibike.R;
import pt.ulisboa.tecnico.cmu.ubibike.domain.Coordinates;
import pt.ulisboa.tecnico.cmu.ubibike.domain.Trajectory;

/**
 * Created by André on 05-04-2016.
 */
public abstract class UtilMap {

    private final static int TRAJECTORY_COLOR = Color.GREEN;

    private final static int TRAJECTORY_WIDTH = 10;

    public static void drawTrajectory(GoogleMap map,Trajectory t){
        PolylineOptions opt = new PolylineOptions();
        if(!t.isEmpty()){
            moveToCurrentLocation(map,new LatLng(t.getTrajectory().get(0).getLatitude(),
                    t.getTrajectory().get(0).getLongitude()));
        }
        List<Coordinates> coords = t.getTrajectory();
        for(int i = 0; i < coords.size(); i++ ){
            LatLng latLng = new LatLng(coords.get(i).getLatitude(),coords.get(i).getLongitude());
            if(i == 0){
                MarkerOptions marker = new MarkerOptions().position(latLng).title("Start")
                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.start));
                map.addMarker(marker);
            }
            else if(i == coords.size()-1){
                MarkerOptions marker = new MarkerOptions().position(latLng).title("Finish")
                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.finish));
                map.addMarker(marker);
            }
            opt.add(latLng);
        }
        opt.width(TRAJECTORY_WIDTH);
        opt.color(TRAJECTORY_COLOR);
        map.addPolyline(opt);
    }

    public static void moveToCurrentLocation(GoogleMap googleMap,LatLng currentLocation) {
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 17));
    }


}
