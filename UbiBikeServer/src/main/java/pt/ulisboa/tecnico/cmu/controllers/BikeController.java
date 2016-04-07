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
import pt.ulisboa.tecnico.cmu.services.BikeServices;

@RestController
public class BikeController {
	
	@Autowired
	private BikeServices bikeServices;
	
	/* ============== Controller Exception Handling ================ */
	
	@ExceptionHandler(BikeDoesntExistException.class)
	private ResponseEntity<Error> handleBikeDoesntExist(BikeDoesntExistException ex,HttpServletRequest req){
		return new ResponseEntity<Error>(new Error(404,ex.getLocalizedMessage()),HttpStatus.NOT_FOUND);
	}
	
	/* ==================== RESTful Services ======================== */
	@RequestMapping(value="/ubibike/bike/pick/up/{id}",method=RequestMethod.PUT)
	public ResponseEntity<Bike> bikePickedUp(@PathVariable String id) throws BikeDoesntExistException{
		Bike bike = bikeServices.bikePickedUp(id);
		return new ResponseEntity<Bike>(bike,HttpStatus.OK);
	}
	
	@RequestMapping(value="/ubibike/bike/pick/off/{id}",method=RequestMethod.PUT)
	public ResponseEntity<Bike> bikePickedOff(@PathVariable String id) throws BikeDoesntExistException{
		Bike bike = bikeServices.bikePickedOff(id);
		return new ResponseEntity<Bike>(bike,HttpStatus.OK);
	}
	
	@RequestMapping(value="/ubibike/bike/book/{id}",method=RequestMethod.PUT)
	public ResponseEntity<Bike> bookABike(@PathVariable String id) throws BikeDoesntExistException{
		Bike bike = bikeServices.bookABike(id);
		return new ResponseEntity<Bike>(bike,HttpStatus.OK);
	}
	
}
