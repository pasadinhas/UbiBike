package pt.ulisboa.tecnico.cmu.ubibike.domain;

import android.location.Location;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Trajectory implements Serializable, Comparable<Trajectory>{

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

    public float getTotalMeters(){
        float acc = 0;
        if(trajectory.size() <= 1)
            return 0;
        Coordinates coord = trajectory.get(0);
        for(int i = 1; i < trajectory.size(); i++){
            Coordinates coord2 = trajectory.get(i);
            float[] res = new float[1];
                    Location.distanceBetween(coord.getLatitude(),coord.getLongitude(),
                    coord2.getLatitude(),coord2.getLongitude(),res);
            acc += res[0];
            coord = coord2;
        }
        return acc;
    }

    @Override
    public String toString(){
        DateFormat fm = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return fm.format(date);
    }

    @Override
    public int compareTo(Trajectory another) {
        int res = getDate().compareTo(another.getDate());
        if(res < 0)
            res =  1;
        else if(res > 0)
            res = -1;
        return res;
    }
}

