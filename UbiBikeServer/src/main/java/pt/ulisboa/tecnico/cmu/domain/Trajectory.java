package pt.ulisboa.tecnico.cmu.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Trajectory {

	@Id
	@GeneratedValue
	@JsonIgnore
	private long id;
	
	@Column
	@JsonFormat(pattern="dd-MM-yyyy")
	private Date date;
	
	public Trajectory(Date date){
		this.date = date;
	}
	
	protected Trajectory() { }	//Needed for JPA/Hibernate
	
	public Date getDate(){
		return date;
	}
	
	public void setDate(Date date){
		this.date = date;
	}
	
}
