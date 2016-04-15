package pt.ulisboa.tecnico.cmu.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Bike {

	@Id
	private String identifier;
	
	@JsonIgnore
	@Column(nullable = false)
	private boolean picked;
	
	@JsonIgnore
	@Column(nullable = false)
	private boolean booked;
	
	@OneToOne(cascade = {CascadeType.ALL})
	private Coordinates position;
	
	@JsonIgnore
	@ManyToOne(cascade = CascadeType.ALL)
	private Station station;
	
	public Bike(Coordinates position,String identifier){
		setIdentifier(identifier);
		setPosition(position);
		setPicked(false);
		setBooked(false);
	}
	
	public Bike() { } //Needed for JPA/Hibernate and JSON
	
	public String getIdentifier(){
		return this.identifier;
	}
	
	public void setIdentifier(String identifier){
		this.identifier = identifier;
	}
	
	public boolean getPicked(){
		return picked;
	}
	
	public void setPicked(boolean picked){
		this.picked = picked;
	}
	
	public boolean getBooked(){
		return this.booked;
	}
	
	public void setBooked(boolean booked){
		this.booked = booked;
	}
	
	public Coordinates getPosition(){
		return this.position;
	}
	
	public void setPosition(Coordinates position){
		this.position = position;
	}
	
	public void setStation(Station station){
		this.station = station;
	}
	
	public Station getStation(){
		return this.station;
	}
	
}
