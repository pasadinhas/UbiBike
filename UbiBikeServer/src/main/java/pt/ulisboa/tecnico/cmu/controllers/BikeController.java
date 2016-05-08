package pt.ulisboa.tecnico.cmu.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import pt.ulisboa.tecnico.cmu.controllers.util.Error;
import pt.ulisboa.tecnico.cmu.domain.Coordinates;
import pt.ulisboa.tecnico.cmu.domain.exceptions.BikeDoesntExistException;
import pt.ulisboa.tecnico.cmu.domain.exceptions.StationDoesntExistException;
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
	
	@ExceptionHandler(StationDoesntExistException.class)
	private ResponseEntity<Error> handleStationDoesntExist(StationDoesntExistException ex,HttpServletRequest req){
		return new ResponseEntity<Error>(new Error(404,ex.getLocalizedMessage()),HttpStatus.NOT_FOUND);
	}
	
	/* ==================== RESTful Services ======================== */
	
	/**
	 * Alert when a bike has been picked by a user.
	 * @param id - Bike unique identifier.
	 * @throws BikeDoesntExistException
	 */
	@RequestMapping(value="/ubibike/bike/pick/up/{id}",method=RequestMethod.PUT)
	public void bikePickedUp(@PathVariable String id) throws BikeDoesntExistException{
		bikeServices.bikePickedUp(id);
	}
	
	/**
	 * Alert when a bike has been dropped in a station by user.
	 * @param id - Bike identifier.
	 * @param station - Station name.
	 * @param position - Bike position (coordinates).
	 * @throws BikeDoesntExistException
	 * @throws StationDoesntExistException 
	 */
	@RequestMapping(value="/ubibike/bike/pick/off/{id}/station/{station}",method=RequestMethod.PUT)
	public void bikePickedOff(@PathVariable String id,
			@PathVariable String station,
			@RequestBody Coordinates position) throws BikeDoesntExistException, StationDoesntExistException{
		bikeServices.bikePickedOff(id,station,position);
	}
	
	/**
	 * Call when a user book a specific bike.
	 * @param id - Bike identifier.
	 * @throws BikeDoesntExistException
	 */
	@RequestMapping(value="/ubibike/bike/book/{id}",method=RequestMethod.PUT)
	public void bookABike(@PathVariable String id) throws BikeDoesntExistException{
		bikeServices.bookABike(id);
	}
	
}
