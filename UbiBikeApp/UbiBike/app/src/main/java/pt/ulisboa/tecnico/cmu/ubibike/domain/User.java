package pt.ulisboa.tecnico.cmu.ubibike.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class User implements Serializable{
    
    private String username;

    private long points;

    private Bike reservedBike;

    /**
     * If the this (replica) is synchronized or not with server.
     */
    private boolean isDirty = false;

    private List<Trajectory> trajectories = new ArrayList<>();

    private List<Trajectory> localTrajectories = new ArrayList<>();

    public User() { }    //Needed for JSON

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

    public void setReservedBike(Bike bike){
        reservedBike = bike;
    }

    public Bike getReservedBike(){
        return reservedBike;
    }

    public void addUserPoints(long points){ this.points += points; }

    /**
     * Get all trajectories EVEN the ones that aren't synchronized in server
     * @return
     */
    public List<Trajectory> getAllTrajectories(){
        List<Trajectory> allTraj = new ArrayList<>();
        allTraj.addAll(trajectories);
        allTraj.addAll(localTrajectories);
        Collections.sort(allTraj);
        return allTraj;
    }

    /**
     * Get only the trajectories that aren't synchronized with server.
     * @return
     */
    public List<Trajectory> getLocalTrajectories() {
        return this.localTrajectories;
    }

    public void replaceTrajectory(Trajectory t){
        for(Trajectory traj : trajectories){
            if(traj.equals(t)){
                traj.setTrajectory(t.getTrajectory());
            }
        }
    }

    /**
     * Done when user (local replica) is synchronized with server.
     */
    public void saveLocalTrajectories(){
        this.trajectories.addAll(localTrajectories);
        localTrajectories.clear();
        isDirty = false;
    }

    /**
     * Add a new trajectory captured in application.
     * @param trajectory
     */
    public void addLocalTrajectory(Trajectory trajectory) {
        isDirty = true;
        points += trajectory.calculateTotalMeters();
        this.localTrajectories.add(trajectory);
    }

    @Override
    public String toString() {
        return username + " " + "Points: " + points;
    }
}
