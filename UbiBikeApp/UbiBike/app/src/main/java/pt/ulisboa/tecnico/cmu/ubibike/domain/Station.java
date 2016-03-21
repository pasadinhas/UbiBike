package pt.ulisboa.tecnico.cmu.ubibike.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by André on 21-03-2016.
 */
public class Station {

    private String name;

    private List<Bike> bikes = new ArrayList<>();

    public Station(String name){
        setName(name);
    }

    public Station() { }	//Needed for JPA/Hibernate

    public List<Bike> getBikes(){
        return bikes;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

}
