package pt.ulisboa.tecnico.cmu.ubibike.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Station implements Serializable {

    private String name;

    private Coordinates position;

    private List<Bike> bikes = new ArrayList<>();

    public Station(String name,Coordinates position){
        setName(name);
        setPosition(position);
    }

    public List<Bike> getBikes(){
        return bikes;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){ this.name = name; }

    public void setPosition(Coordinates position){
        this.position = position;
    }

    public Coordinates getPosition(){
        return this.position;
    }

    public void removeBike(Bike bike){
        bikes.remove(bike);
    }

    public String toString(){
        return name + ": " + bikes.size() + " bikes available";
    }

}
