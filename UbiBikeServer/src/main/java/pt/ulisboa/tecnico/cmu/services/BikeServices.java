package pt.ulisboa.tecnico.cmu.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pt.ulisboa.tecnico.cmu.domain.Bike;
import pt.ulisboa.tecnico.cmu.domain.Coordinates;
import pt.ulisboa.tecnico.cmu.domain.Station;
import pt.ulisboa.tecnico.cmu.domain.exceptions.BikeDoesntExistException;
import pt.ulisboa.tecnico.cmu.domain.repositories.BikeRepository;
import pt.ulisboa.tecnico.cmu.domain.repositories.StationRepository;

@Service
public class BikeServices {

	@Autowired
	private BikeRepository bikeRepository;
	
	@Autowired
	private StationRepository stationRepository;
	
	
	public void bikePickedUp(String id) throws BikeDoesntExistException{
		Bike bike = getBike(id);
		if(!bike.getPicked()){
			bike.setStation(null);
			bike.setPicked(true);
			bikeRepository.save(bike);
		}
	}
	
	@Transactional
	public void bikePickedOff(String id,String station,Coordinates coord) 
			throws BikeDoesntExistException{
		Bike bike = getBike(id);
		Station sta = stationRepository.findOne(station);
		if(sta != null && bike.getPicked()){
			sta.addBike(bike);
			bike.setStation(sta);
			bike.setPosition(coord);
			bike.setPicked(false);
			bike.setBooked(false);
			bikeRepository.save(bike);
			stationRepository.save(sta);
		}
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
