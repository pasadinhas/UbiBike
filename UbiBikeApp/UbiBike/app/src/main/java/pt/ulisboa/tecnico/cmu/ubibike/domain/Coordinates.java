package pt.ulisboa.tecnico.cmu.ubibike.domain;

import java.io.Serializable;

/**
 * Created by André on 28-03-2016.
 */
public class Coordinates implements Serializable{

    private double latitude;

    private double longitude;

    public Coordinates(double latitude,double longitude){
        setLatitude(latitude);
        setLongitude(longitude);
    }

    public void setLatitude(double latitude){
        this.latitude = latitude;
    }

    public double getLatitude(){
        return this.latitude;
    }

    public void setLongitude(double longitude){
        this.longitude = longitude;
    }

    public double getLongitude(){
        return this.longitude;
    }

}
