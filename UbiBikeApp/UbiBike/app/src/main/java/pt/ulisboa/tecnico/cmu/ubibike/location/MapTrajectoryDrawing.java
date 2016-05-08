package pt.ulisboa.tecnico.cmu.ubibike.location;

import android.content.Context;
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
public class MapTrajectoryDrawing {

    private static final float DEFAULT_ZOOM = 17;
    private final int TRAJECTORY_COLOR = Color.GREEN;
    private final int TRAJECTORY_WIDTH = 10;

    private Context ctx;

    private GoogleMap map;

    private Trajectory trajectory;

    private PolylineOptions opt = new PolylineOptions();

    public MapTrajectoryDrawing(GoogleMap map, Trajectory t, Context ctx){
        this.ctx = ctx;
        this.map = map;
        this.trajectory = t;
        opt.width(TRAJECTORY_WIDTH);
        opt.color(TRAJECTORY_COLOR);
    }

    public static void moveToCurrentLocation(GoogleMap googleMap,LatLng currentLocation) {
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, DEFAULT_ZOOM));
    }

    public void moveToBegining(){
        Coordinates initial = trajectory.getInitialPosition();
        if(initial != null){
            moveToCurrentLocation(map,new LatLng(initial.getLatitude(),initial.getLongitude()));
        }
    }

    public void addAndDrawPosition(double lat, double lon){
        LatLng ll = new LatLng(lat,lon);
        if(trajectory.getTrajectory().size() == 1){
            drawInitialMarker();
        }
        opt.add(ll);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(ll, map.getCameraPosition().zoom));
        map.addPolyline(opt);
    }

    public void drawTrajectory(){
        List<Coordinates> coords = trajectory.getTrajectory();
        for(int i = 0; i < coords.size(); i++ ){
            LatLng latLng = new LatLng(coords.get(i).getLatitude(),coords.get(i).getLongitude());
            opt.add(latLng);
        }
        map.addPolyline(opt);
    }

    public void drawInitialMarker(){
        Coordinates initial = trajectory.getInitialPosition();
        if(initial != null){
            String markerTitle = ctx.getResources().getString(R.string.initial_marker_title);
            LatLng latLng = new LatLng(initial.getLatitude(),initial.getLongitude());
            MarkerOptions marker = new MarkerOptions().position(latLng).title(markerTitle)
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.start));
            map.addMarker(marker);
        }
    }

    public void clearMap(){
        map.clear();
    }

    public void drawLastMarker(){
        Coordinates last = trajectory.getLastPosition();
        if(last != null){
            String markerTitle = ctx.getResources().getString(R.string.last_marker_title);
            LatLng latLng = new LatLng(last.getLatitude(),last.getLongitude());
            MarkerOptions marker = new MarkerOptions().position(latLng).title(markerTitle)
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.finish));
            map.addMarker(marker);
        }
    }

}
