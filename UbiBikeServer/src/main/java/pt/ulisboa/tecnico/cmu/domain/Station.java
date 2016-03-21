package pt.ulisboa.tecnico.cmu.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Station {

	@Id
	private String name;
	
	@OneToMany(cascade = CascadeType.ALL,orphanRemoval = true)
	private List<Bike> bikes = new ArrayList<>();
	
	public Station(String name){
		setName(name);
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
	
	public boolean oneBikeAvailableAtLeast(){
		for(Bike bike : bikes){
			if(!bike.getBooked()){
				return true;
			}
		}
		return false;
	}
	
}
