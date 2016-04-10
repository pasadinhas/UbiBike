package pt.ulisboa.tecnico.cmu.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Trajectory{
	
	@Id
	@GeneratedValue
	@JsonIgnore
	private long trajId;
	
	@Column
	@JsonFormat(pattern="dd-MM-yyyy HH:mm:ss")
	private Date date;
	
	@OneToMany(cascade = CascadeType.ALL,orphanRemoval = true)
	private List<Coordinates> trajectory = new ArrayList<>();
	
	
	public Trajectory(Date date){
		setDate(date);
	}
	
	protected Trajectory() { }	//Needed for JPA/Hibernate
	
	public Date getDate(){
		return date;
	}
	
	public void setDate(Date date){
		this.date = date;
	}
	
	public void addCoordinates(Coordinates coordinates){
		trajectory.add(coordinates);
	}
	
	public List<Coordinates> getTrajectory(){
		return this.trajectory;
	}
	
	@Override
	public boolean equals(Object o){
		Trajectory t = (Trajectory)o;
		return (date.compareTo(t.getDate()) == 0);
	}


}
