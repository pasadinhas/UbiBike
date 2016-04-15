package pt.ulisboa.tecnico.cmu.ubibike.domain;

import java.io.Serializable;


public class Bike implements Serializable{

    private String identifier;

    private Coordinates position;

    public Bike(){ }    //Needed for JSON

    public void setIdentifier(String id){ this.identifier = id; }

    public String getIdentifier() { return this.identifier; }

    public Coordinates getPosition(){
        return this.position;
    }

    public void setPosition(Coordinates position){
        this.position = position;
    }

}
