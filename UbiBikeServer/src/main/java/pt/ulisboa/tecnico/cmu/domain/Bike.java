package pt.ulisboa.tecnico.cmu.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Bike {

	@Id
	@GeneratedValue
	private int id;
	
	@Column(nullable = false)
	private boolean picked;
	
	@Column(nullable = false)
	private boolean booked;
	
	public Bike(){
		setPicked(false);
		setBooked(false);
	}
	
	
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
	
}
