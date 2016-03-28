package pt.ulisboa.tecnico.cmu.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class Bike {

	@Id
	@GeneratedValue
	private int id;
	
	@Column(nullable = false)
	private boolean picked;
	
	@Column(nullable = false)
	private boolean booked;
	
	@OneToOne(cascade = {CascadeType.ALL})
	private Coordinates position;
	
	public Bike(Coordinates position){
		setPosition(position);
		setPicked(false);
		setBooked(false);
	}
	
	public Bike() { } //Needed for JPA/Hibernate
	
	public void setId(int id){
		this.id = id;
	}
	
	public int getId(){
		return id;
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
	
}
