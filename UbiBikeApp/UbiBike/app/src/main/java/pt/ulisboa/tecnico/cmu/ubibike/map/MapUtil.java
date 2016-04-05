package pt.ulisboa.tecnico.cmu.ubibike.map;

import android.graphics.Color;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import pt.ulisboa.tecnico.cmu.ubibike.domain.Coordinates;
import pt.ulisboa.tecnico.cmu.ubibike.domain.Trajectory;

/**
 * Created by André on 05-04-2016.
 */
public abstract class MapUtil {

    public static void drawTrajectory(GoogleMap map,Trajectory t){
        PolylineOptions opt = new PolylineOptions();
        if(!t.isEmpty()){
            moveToCurrentLocation(map,new LatLng(t.getTrajectory().get(0).getLatitude(),
                    t.getTrajectory().get(0).getLongitude()));
        }
        for(Coordinates coord : t.getTrajectory()){
            opt.add(new LatLng(coord.getLatitude(),coord.getLongitude()));
        }
        opt.width(5);
        opt.color(Color.RED);
        map.addPolyline(opt);
    }

    public static void moveToCurrentLocation(GoogleMap googleMap,LatLng currentLocation) {
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 16));
        //googleMap.animateCamera(CameraUpdateFactory.zoomIn());
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(17), 3000, null);
    }


}
