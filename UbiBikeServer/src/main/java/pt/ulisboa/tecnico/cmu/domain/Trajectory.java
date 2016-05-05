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
public class Trajectory implements Comparable<Trajectory>{
	
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
	
	protected Trajectory() { }	//Needed for JPA/Hibernate and JSON
	
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
	
	public void removeAllCoordinates(){
		this.trajectory = null;
	}
	
	@Override
	public boolean equals(Object o){
		Trajectory t = (Trajectory)o;
		return (date.compareTo(t.getDate()) == 0);
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
