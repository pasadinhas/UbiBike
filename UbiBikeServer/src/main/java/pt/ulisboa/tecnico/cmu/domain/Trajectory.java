package pt.ulisboa.tecnico.cmu.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Trajectory {

	@Id
	@GeneratedValue
	private long id;
	
	@Column
	private Date date;
	
	
	public Trajectory(Date date){
		this.date = date;
	}
	
	public Date getDate(){
		return date;
	}
	
	public void setDate(Date date){
		this.date = date;
	}
	
}
