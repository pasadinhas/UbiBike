package pt.ulisboa.tecnico.cmu.ubibike.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by André on 21-03-2016.
 */
public class Station implements Serializable {

    private String name;

    private List<Bike> bikes = new ArrayList<>();

    public Station(String name){
        setName(name);
    }

    public Station() { }	//Needed for JPA/Hibernate

    public List<Bike> getBikes(){
        return bikes;
    }

    public void setBikes(List<Bike> bikes) { this.bikes = bikes; }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String toString(){
        return name + " station has " + bikes.size() + " bikes available";
    }

}
