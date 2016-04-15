package pt.ulisboa.tecnico.cmu.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pt.ulisboa.tecnico.cmu.domain.Bike;
import pt.ulisboa.tecnico.cmu.domain.Station;
import pt.ulisboa.tecnico.cmu.domain.exceptions.BikeDoesntExistException;
import pt.ulisboa.tecnico.cmu.domain.repositories.BikeRepository;

@Service
public class BikeServices {

	@Autowired
	private BikeRepository bikeRepository;
	
	
	public void bikePickedUp(String id) throws BikeDoesntExistException{
		Bike bike = getBike(id);
		Station s = bike.getStation();
		if(s == null){
			System.out.println("Bike: " + id + " out of station");
			return ;
		}
		bike.setStation(null);
		bike.setPicked(true);
		bikeRepository.save(bike);
	}
	
	public void bikePickedOff(String id) throws BikeDoesntExistException{
		Bike bike = getBike(id);
		bike.setPicked(false);
		bike.setBooked(false);
		bikeRepository.save(bike);
	}
	
	public void bookABike(String id) throws BikeDoesntExistException{
		Bike bike = getBike(id);
		bike.setBooked(true);
		bikeRepository.save(bike);
	}
	
	private Bike getBike(String id) throws BikeDoesntExistException{
		Bike bike = bikeRepository.findOne(id);
		if(bike == null)
			throw new BikeDoesntExistException();
		return bike;
	}
	
}
