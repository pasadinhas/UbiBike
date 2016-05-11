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

    private long date;

    private List<Coordinates> trajectory = new ArrayList<>();


    public Trajectory(long date){
        setDate(date);
    }

    public Trajectory() { } //Needed for JSON

    public long getDate(){
        return date;
    }

    public void setDate(long date){
        this.date = date;
    }

    public List<Coordinates> getTrajectory(){
        return this.trajectory;
    }

    public void setTrajectory(List<Coordinates> t) { this.trajectory = t; }

    public void addCoordinate(Coordinates coord){
        trajectory.add(coord);
    }

    public boolean isEmpty(){
        return trajectory.isEmpty();
    }

    public Coordinates getInitialPosition(){
        if(trajectory != null && !trajectory.isEmpty()){
            return trajectory.get(0);
        }
        return null;
    }

    public Coordinates getLastPosition(){
        if(trajectory != null && !trajectory.isEmpty()){
            return trajectory.get(trajectory.size() - 1);
        }
        return null;
    }

    /**
     * Return trajectory distance in meters.
     * @return
     */
    public long calculateTotalMeters(){
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
        return Math.round(totalMetersAcc);
    }

    public int calculateTotalKm(){
        return Math.round(calculateTotalMeters()/1000);
    }

    @Override
    public boolean equals(Object o) {
        Trajectory t  = (Trajectory) o;
        return this.date == t.getDate();
    }

    @Override
    public int compareTo(@NonNull Trajectory another) {
        int res = 0;
        if(date < another.getDate())
            res =  1;
        else if(date > another.getDate())
            res = -1;
        return res;
    }

    @Override
    public String toString(){
        DateFormat fm = new SimpleDateFormat(UtilREST.DATE_FORMAT, Locale.ENGLISH);
        return fm.format(new Date(date)) + " " + calculateTotalKm() + "km";
    }
}

