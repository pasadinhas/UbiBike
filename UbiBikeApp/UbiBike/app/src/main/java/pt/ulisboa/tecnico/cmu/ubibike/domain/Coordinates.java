package pt.ulisboa.tecnico.cmu.ubibike.domain;

import java.io.Serializable;

public class Coordinates implements Serializable{

    private double latitude;

    private double longitude;

    public Coordinates(double latitude,double longitude){
        setLatitude(latitude);
        setLongitude(longitude);
    }

    public Coordinates() { }    //Needed for JSON

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
