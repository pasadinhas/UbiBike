package pt.ulisboa.tecnico.cmu.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class User {
	
	@Id
	private String username;
	
	@Column(nullable = false)
	private String password;
	
	@Column(nullable = false)
	private long points;
	
	@OneToMany(cascade = CascadeType.ALL,orphanRemoval = true)
	private List<Trajectory> trajectories = new ArrayList<>();
	
	
	public User(String username,String password) {
		setUsername(username);
		setPassword(password);
		setPoints(0);
	}
	
	protected User() { }
	
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
	
	public List<Trajectory> getTrajectories(){
		return this.trajectories;
	}
}