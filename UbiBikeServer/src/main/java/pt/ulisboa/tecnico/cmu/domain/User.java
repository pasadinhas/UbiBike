package pt.ulisboa.tecnico.cmu.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

import com.fasterxml.jackson.annotation.JsonIgnore;

import pt.ulisboa.tecnico.cmu.domain.exceptions.TrajectoryAlreadyExistException;
import pt.ulisboa.tecnico.cmu.domain.exceptions.TrajectoryDoesntExistException;

@Entity
public class User {
	
	@Id
	private String username;
	
	@Column(nullable = false)
	@JsonIgnore
	private String password;
	
	@Column(nullable = false)
	private long points;
	
	@OneToMany(cascade = CascadeType.ALL,orphanRemoval = true)
	@OrderBy("date DESC")
	private List<Trajectory> trajectories = new ArrayList<>();
	
	
	public User(String username,String password) {
		setUsername(username);
		setPassword(password);
		setPoints(0);
	}
	
	protected User() { }	//Needed for JPA/Hibernate and JSON
	
	public String getUsername(){
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPassword(){
		return password;
	}
	
	public void setPassword(String password){
		this.password = password;
	}
	
	public long getPoints(){
		return points;
	}
	
	public void setPoints(long points){
		this.points = points;
	}
	
	public void addPoints(long points){
		this.points += points;
	}
	
	public Collection<Trajectory> getTrajectories(){
		return this.trajectories;
	}
	
	public Trajectory getTrajectory(Date date) throws TrajectoryDoesntExistException{
		for(Trajectory t : this.trajectories){
			if(t.getDate().compareTo(date) == 0){
				return t;
			}
		}
		throw new TrajectoryDoesntExistException();
	}
	
	public void addTrajectory(Trajectory t) throws TrajectoryAlreadyExistException{
		for(Trajectory traj : this.trajectories){
			if(t.equals(traj)){
				throw new TrajectoryAlreadyExistException();
			}
		}
		trajectories.add(t);
	}
	
	/* =========== Object Detail Control Methods ======= */
	
	public void setUserDetailMedium(){
		for(Trajectory t : this.trajectories){
			t.removeAllCoordinates();
		}
	}
	
	public void setUserDetailLow(){
		this.trajectories = null;
	}
	
}
