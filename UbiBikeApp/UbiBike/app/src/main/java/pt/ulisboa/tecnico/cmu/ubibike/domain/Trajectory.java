package pt.ulisboa.tecnico.cmu.ubibike.domain;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by André on 21-03-2016.
 */
public class Trajectory implements Serializable{

    private Date date;

    private List<Coordinates> trajectory = new ArrayList<>();

    public Trajectory(Date date){
        setDate(date);
    }

    public Date getDate(){
        return date;
    }

    public void setDate(Date date){
        this.date = date;
    }

    public List<Coordinates> getTrajectory(){
        return this.trajectory;
    }

   // public void setTrajectory(List<Coordinates> list) { this.trajectory = list;}

    public boolean isEmpty(){
        return trajectory.isEmpty();
    }

    @Override
    public String toString(){
        DateFormat fm = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return fm.format(date);
    }

}

