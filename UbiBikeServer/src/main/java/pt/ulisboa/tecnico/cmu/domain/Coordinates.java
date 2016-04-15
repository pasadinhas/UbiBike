package pt.ulisboa.tecnico.cmu.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Coordinates {

	@Id
	@GeneratedValue
	@JsonIgnore
	private long coorId;
	
	@Column
	private double latitude;
	
	@Column
	private double longitude;
	
	public Coordinates(double latitude,double longitude){
		setLatitude(latitude);
		setLongitude(longitude);
	}
	
	public Coordinates() { }	//Needed for JPA/Hibernate and JSON
	
	public void setLatitude(double latitude){
		this.latitude = latitude;
	}
	
	public double getLatitude(){
		return this.latitude;
	}
	
	public void setLongitude(double longitude){
		this.longitude = longitude;
	}
	
	public double getLongitude(){
		return this.longitude;
	}
	
	@Override
	public boolean equals(Object o){
		Coordinates c = (Coordinates) o;
		return (Double.compare(c.getLatitude(),getLatitude()) == 0) &&
			   (Double.compare(c.getLongitude(), getLongitude()) == 0);
	}
	
}
