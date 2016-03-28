package pt.ulisboa.tecnico.cmu.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import pt.ulisboa.tecnico.cmu.domain.Bike;
import pt.ulisboa.tecnico.cmu.domain.exceptions.BikeDoesntExistException;
import pt.ulisboa.tecnico.cmu.domain.repositories.BikeRepository;

@RestController
public class BikeController {

	@Autowired
	private BikeRepository bikeRepository;
	
	/* ============== Controller Exception Handling ================ */
	
	@ExceptionHandler(BikeDoesntExistException.class)
	private ResponseEntity<Error> handleBikeDoesntExist(BikeDoesntExistException ex,HttpServletRequest req){
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Error(404,"Bike doesnt exist"));
	}
	
	/* ==================== RESTful Services ======================== */
	
	@RequestMapping(value="/ubibike/bike/pick/up/{id}",method=RequestMethod.PUT)
	public Bike bikePickedUp(@PathVariable int id) throws BikeDoesntExistException{
		Bike bike = bikeRepository.findOne(id);
		if(bike == null)
			throw new BikeDoesntExistException();
		bike.setPicked(true);
		bikeRepository.save(bike);
		return bike;
	}
	
	@RequestMapping(value="/ubibike/bike/pick/off/{id}",method=RequestMethod.PUT)
	public Bike bikePickedOff(@PathVariable int id) throws BikeDoesntExistException{
		Bike bike = bikeRepository.findOne(id);
		if(bike == null)
			throw new BikeDoesntExistException();
		bike.setPicked(false);
		bike.setBooked(false);
		bikeRepository.save(bike);
		return bike;
	}
	
	@RequestMapping(value="/ubibike/bike/book/{id}",method=RequestMethod.PUT)
	public Bike bookABike(@PathVariable int id) throws BikeDoesntExistException{
		Bike bike = bikeRepository.findOne(id);
		if(bike == null)
			throw new BikeDoesntExistException();
		bike.setBooked(true);
		bikeRepository.save(bike);
		return bike;
	}
	
}
