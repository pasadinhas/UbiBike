package pt.ulisboa.tecnico.cmu.ubibike.services.gps.track;

/**
 * Created by André on 07-05-2016.
 */
public interface IGPSCallback {

    void setLocation(double lat, double lon);

}
