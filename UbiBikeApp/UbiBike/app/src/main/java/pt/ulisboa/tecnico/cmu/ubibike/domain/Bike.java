package pt.ulisboa.tecnico.cmu.ubibike.domain;

/**
 * Created by André on 21-03-2016.
 */
public class Bike {

    private int id;

    private boolean picked;

    private boolean booked;

    public Bike(){
        setPicked(false);
        setBooked(false);
    }

    public void setId(int id){
        this.id = id;
    }

    public int getId(){
        return id;
    }

    public boolean getPicked(){
        return picked;
    }

    public void setPicked(boolean picked){
        this.picked = picked;
    }

    public boolean getBooked(){
        return this.booked;
    }

    public void setBooked(boolean booked){
        this.booked = booked;
    }

}
