package pt.ulisboa.tecnico.cmu.ubibike.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by André on 21-03-2016.
 */
public class User {
    
    private String username;

    private long points;

    private List<Trajectory> trajectories = new ArrayList<>();

    public User(String username) {
        setUsername(username);
        setPoints(0);
    }

    protected User() { }	//Needed for JPA/Hibernate

    public String getUsername(){
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public long getPoints(){
        return points;
    }

    public void setPoints(long points){
        this.points = points;
    }

    public List<Trajectory> getTrajectories(){
        return this.trajectories;
    }

    public void setTrajectories(List<Trajectory> trajectories){this.trajectories = trajectories;}
}
