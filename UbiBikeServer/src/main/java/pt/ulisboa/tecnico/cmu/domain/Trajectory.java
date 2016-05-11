package pt.ulisboa.tecnico.cmu.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import pt.ulisboa.tecnico.cmu.controllers.util.JsonViews;

@Entity
public class Trajectory implements Comparable<Trajectory>{
	
	@JsonIgnore
	@Id
	@GeneratedValue
	private long trajId;
	
	@JsonView(JsonViews.LowDetailed.class)
	@Column
	private long date;
	
	@JsonView(JsonViews.HighlyDetailed.class)
	@OneToMany(cascade = CascadeType.ALL,orphanRemoval = true)
	private List<Coordinates> trajectory = new ArrayList<>();
	
	
	public Trajectory(long date){
		setDate(date);
	}
	
	protected Trajectory() { }	//Needed for JPA/Hibernate and JSON
	
	public long getDate(){
		return date;
	}
	
	public void setDate(long date){
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
		Trajectory t  = (Trajectory) o;
		return this.date == t.getDate();
	}

	@Override
	public int compareTo(Trajectory another) {
		int res = 0;
        if(date < another.getDate())
            res =  1;
        else if(date > another.getDate())
            res = -1;
        return res;
	}


}
