package pt.ulisboa.tecnico.cmu.ubibike.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable{
    
    private String username;

    private long points;

    private List<Trajectory> trajectories = new ArrayList<>();

    public User(String username) {
        setUsername(username);
        setPoints(0);
    }

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

    public void userFromServer(){
        for(Trajectory t : trajectories){
            t.setAtServer(true);
        }
    }

    public void addTrajectory(Trajectory trajectory){
        this.trajectories.add(trajectory);
    }
}
