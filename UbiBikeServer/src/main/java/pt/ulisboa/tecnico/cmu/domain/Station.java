package pt.ulisboa.tecnico.cmu.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
public class Station {

	@Id
	private String name;
	
	@OneToOne(cascade = {CascadeType.ALL})
	private Coordinates position;
	
	@OneToMany(cascade = CascadeType.ALL,orphanRemoval = true)
	private List<Bike> bikes = new ArrayList<>();
	
	
	public Station(String name,Coordinates position){
		setName(name);
		setPosition(position);
	}
	
	public Station() { }	//Needed for JPA/Hibernate
	
	public List<Bike> getBikes(){
		return bikes;
	}
	
	public String getName(){
		return name;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public void setPosition(Coordinates position){
		this.position = position;
	}
	
	public Coordinates getPosition(){
		return this.position;
	}
	
	public void addBike(Bike bike){
		bikes.add(bike);
	}
	
	public void removeBookedBikes(){
		List<Bike> bikes = new ArrayList<Bike>();
		for(Bike bike : this.bikes){
			if(!bike.getBooked()){
				bikes.add(bike);
			}
		}
		this.bikes = bikes;
	}
	
	public boolean oneBikeAvailableAtLeast(){
		for(Bike bike : bikes){
			if(!bike.getBooked()){
				return true;
			}
		}
		return false;
	}
	
}
