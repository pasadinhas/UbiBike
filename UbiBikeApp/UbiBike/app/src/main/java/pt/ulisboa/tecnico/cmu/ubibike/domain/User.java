package pt.ulisboa.tecnico.cmu.ubibike.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class User implements Serializable{
    
    private String username;

    private long points;

    private boolean isDirty;

    private List<Trajectory> trajectories = new ArrayList<>();

    private List<Trajectory> localTrajectories;

    /* Constructor for GSON */
    protected User(){
        localTrajectories = new ArrayList<>();
        isDirty = false;
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

    public boolean getIsDirty() { return isDirty; }

    public void setIsDirty(boolean isDirty) { this.isDirty = isDirty; }

    public List<Trajectory> getAllTrajectories(){
        List<Trajectory> allTraj = new ArrayList<>();
        allTraj.addAll(trajectories);
        allTraj.addAll(localTrajectories);
        Collections.sort(allTraj);
        return allTraj;
    }

    public List<Trajectory> getLocalTrajectories() { return this.localTrajectories; }

    public void addUserPoints(long points){
        this.points += points;
    }

    public void saveLocalTrajectories(){
        this.trajectories.addAll(localTrajectories);
        localTrajectories.clear();
    }

    public void addLocalTrajectory(Trajectory trajectory) { this.localTrajectories.add(trajectory); }

}
