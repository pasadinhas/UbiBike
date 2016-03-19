package pt.ulisboa.tecnico.cmu.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import pt.ulisboa.tecnico.cmu.domain.Bike;
import pt.ulisboa.tecnico.cmu.domain.repositories.BikeRepository;

@RestController
public class BikeController {

	@Autowired
	private BikeRepository bikeRepository;
	
	@RequestMapping(value="/ubibike/bike/pick/up/{id}",method=RequestMethod.PUT)
	public void bikePickedUp(@PathVariable int id){
		Bike bike = bikeRepository.findOne(id);
		bike.setPicked(true);
		bikeRepository.save(bike);
	}
	
	@RequestMapping(value="/ubibike/bike/pick/off/{id}",method=RequestMethod.PUT)
	public void bikePickedOff(@PathVariable int id){
		Bike bike = bikeRepository.findOne(id);
		bike.setPicked(false);
		bike.setBooked(false);
		bikeRepository.save(bike);
	}
	
	@RequestMapping(value="/ubibike/bike/book/{id}",method=RequestMethod.PUT)
	public void bookABike(@PathVariable int id){
		Bike bike = bikeRepository.findOne(id);
		bike.setBooked(true);
		bikeRepository.save(bike);
	}
	
}
