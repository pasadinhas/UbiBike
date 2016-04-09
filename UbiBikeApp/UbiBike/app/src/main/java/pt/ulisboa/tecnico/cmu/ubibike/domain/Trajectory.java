package pt.ulisboa.tecnico.cmu.ubibike.domain;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Trajectory implements Serializable{

    private Date date;

    private List<Coordinates> trajectory = new ArrayList<>();

    private boolean atServer = true;


    public Trajectory(Date date){
        setAtServer(true);
        setDate(date);
    }

    public boolean getAtServer(){
        return atServer;
    }

    public void setAtServer(boolean atServer){
        this.atServer = atServer;
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

    public void addCoordinate(Coordinates coord){
        trajectory.add(coord);
    }

    public boolean isEmpty(){
        return trajectory.isEmpty();
    }

    @Override
    public String toString(){
        DateFormat fm = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return fm.format(date);
    }

}

