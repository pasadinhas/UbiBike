package pt.ulisboa.tecnico.cmu.ubibike.domain;

import android.location.Location;
import android.support.annotation.NonNull;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import pt.ulisboa.tecnico.cmu.ubibike.remote.rest.UtilREST;

public class Trajectory implements Serializable, Comparable<Trajectory>{

    private Date date;

    private List<Coordinates> trajectory = new ArrayList<>();

    public Trajectory(Date date){
        setDate(date);
    }

    public Trajectory() { } //Needed for JSON

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

    public float calculateTotalMeters(){
        float totalMetersAcc = 0;
        if(trajectory.size() <= 1)
            return 0;
        Coordinates coord = trajectory.get(0);
        for(int i = 1; i < trajectory.size(); i++){
            Coordinates coord2 = trajectory.get(i);
            float[] res = new float[1];
                    Location.distanceBetween(coord.getLatitude(),coord.getLongitude(),
                    coord2.getLatitude(),coord2.getLongitude(),res);
            totalMetersAcc += res[0];
            coord = coord2;
        }
        return totalMetersAcc;
    }

    @Override
    public String toString(){
        DateFormat fm = new SimpleDateFormat(UtilREST.DATE_FORMAT, Locale.ENGLISH);
        return fm.format(date);
    }

    @Override
    public int compareTo(@NonNull Trajectory another) {
        int res = getDate().compareTo(another.getDate());
        if(res < 0)
            res =  1;
        else if(res > 0)
            res = -1;
        return res;
    }
}

