package pt.ulisboa.tecnico.cmu.controllers;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;

import pt.ulisboa.tecnico.cmu.controllers.util.Error;
import pt.ulisboa.tecnico.cmu.controllers.util.JsonViews;
import pt.ulisboa.tecnico.cmu.domain.Bike;
import pt.ulisboa.tecnico.cmu.domain.Coordinates;
import pt.ulisboa.tecnico.cmu.domain.Trajectory;
import pt.ulisboa.tecnico.cmu.domain.User;
import pt.ulisboa.tecnico.cmu.domain.exceptions.BikeAlreadyBookedException;
import pt.ulisboa.tecnico.cmu.domain.exceptions.BikeDoesntExistException;
import pt.ulisboa.tecnico.cmu.domain.exceptions.CantBookMoreBikesException;
import pt.ulisboa.tecnico.cmu.domain.exceptions.InvalidLoginException;
import pt.ulisboa.tecnico.cmu.domain.exceptions.StationDoesntExistException;
import pt.ulisboa.tecnico.cmu.domain.exceptions.TrajectoryAlreadyExistException;
import pt.ulisboa.tecnico.cmu.domain.exceptions.TrajectoryDoesntExistException;
import pt.ulisboa.tecnico.cmu.domain.exceptions.UserAlreadyExistException;
import pt.ulisboa.tecnico.cmu.domain.exceptions.UserDoesntExistException;
import pt.ulisboa.tecnico.cmu.services.UserServices;

@RestController
public class UserController {
	
	@Autowired
	private UserServices userServices;
	
	/* ============== Controller Exception Handling ================ */
	
	@ExceptionHandler(UserAlreadyExistException.class)
	private ResponseEntity<Error> handleUserAlreadyExistException(UserAlreadyExistException ex,
			HttpServletRequest request) {
		return new ResponseEntity<Error>(new Error(409,ex.getLocalizedMessage()),HttpStatus.CONFLICT);
	}
	
	@ExceptionHandler(UserDoesntExistException.class)
	private ResponseEntity<Error> handleUserAlreadyExistException(UserDoesntExistException ex,
			HttpServletRequest request) {
		return new ResponseEntity<Error>(new Error(404,ex.getLocalizedMessage()),HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(InvalidLoginException.class)
	public ResponseEntity<Error> handleInvalidLogin(InvalidLoginException ex,HttpServletRequest request){
		return new ResponseEntity<Error>(new Error(409,ex.getLocalizedMessage()),HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(TrajectoryAlreadyExistException.class)
	public ResponseEntity<Error> handleTrajectoryAlreadyExistException(TrajectoryAlreadyExistException ex,
			HttpServletRequest request){
		return new ResponseEntity<Error>(new Error(409,ex.getLocalizedMessage()),HttpStatus.CONFLICT);
	}
	
	@ExceptionHandler(TrajectoryDoesntExistException.class)
	public ResponseEntity<Error> handleTrajectoryDoesntExistException(TrajectoryDoesntExistException ex,
			HttpServletRequest request){
		return new ResponseEntity<Error>(new Error(404,ex.getLocalizedMessage()),HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(BikeDoesntExistException.class)
	private ResponseEntity<Error> handleBikeDoesntExist(BikeDoesntExistException ex,HttpServletRequest req){
		return new ResponseEntity<Error>(new Error(404,ex.getLocalizedMessage()),HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(StationDoesntExistException.class)
	private ResponseEntity<Error> handleStationDoesntExist(StationDoesntExistException ex,HttpServletRequest req){
		return new ResponseEntity<Error>(new Error(404,ex.getLocalizedMessage()),HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(BikeAlreadyBookedException.class)
	private ResponseEntity<Error> handleBikeAlreadyBooked(BikeAlreadyBookedException ex, HttpServletRequest req){
		return new ResponseEntity<Error>(new Error(409,ex.getLocalizedMessage()),HttpStatus.CONFLICT);
	}
	
	@ExceptionHandler(CantBookMoreBikesException.class)
	private ResponseEntity<Error> handleCantBookMoreBikes(CantBookMoreBikesException ex, HttpServletRequest req){
		return new ResponseEntity<Error>(new Error(409,ex.getLocalizedMessage()),HttpStatus.CONFLICT);
	}
	
	/* ================================= RESTful services ====================================== */
	
	/**
	 * Login method for authenticate user.
	 * @param username - User's username
	 * @param password - User's password
	 * @return User object
	 * @throws UserDoesntExistException
	 * @throws InvalidLoginException
	 */
	@RequestMapping(value="/ubibike/user/login/{username}",method=RequestMethod.POST)
	public ResponseEntity<User> login(
			@PathVariable String username,
			@RequestParam(value="password")String password) 
			throws UserDoesntExistException, InvalidLoginException{
		User user = userServices.loginUser(username, password);
		return new ResponseEntity<User>(user,HttpStatus.OK);
	}
	
	/**
	 * Create a user in the system.
	 * @param username - User's username
	 * @param password - User's password
	 * @throws UserAlreadyExistException
	 */
	@RequestMapping(value = "/ubibike/user/{username}",method=RequestMethod.POST)
	public void createUser(
			@PathVariable String username,
			@RequestParam(value="password")String password) throws UserAlreadyExistException {
		userServices.createUser(username, password);
	}
	
	/**
	 * Get User information according (with all the information).
	 * @param username - User's username.
	 * @return - User object
	 * @throws UserDoesntExistException
	 */
	@RequestMapping(value = "/ubibike/user/{username}",method=RequestMethod.GET)
	public ResponseEntity<User> getUserInformation(
			@PathVariable String username) throws UserDoesntExistException{
		return new ResponseEntity<User>(userServices.getUser(username),HttpStatus.OK);
	}
	
	/**
	 * Get User information according (with partial information).
	 * @param username
	 * @return User object with MEDIUM level of detail
	 * @throws UserDoesntExistException
	 */
	@JsonView(JsonViews.MediumDetailed.class)
	@RequestMapping(value = "/ubibike/user/{username}/medium",method=RequestMethod.GET)
	public ResponseEntity<User> getUserInformationMediumDetail(
			@PathVariable String username) throws UserDoesntExistException{
		return new ResponseEntity<User>(userServices.getUser(username),HttpStatus.OK);
	}
	
	/**
	 * Get User information according (with partial information).
	 * @param username
	 * @return
	 * @throws UserDoesntExistException
	 */
	@JsonView(JsonViews.LowDetailed.class)
	@RequestMapping(value = "/ubibike/user/{username}/low",method=RequestMethod.GET)
	public ResponseEntity<User> getUserInformationLowDetail(
			@PathVariable String username) throws UserDoesntExistException{
		return new ResponseEntity<User>(userServices.getUser(username),HttpStatus.OK);
	}
	
	
	/**
	 * Get all Users given an username prefix.
	 * @param usernamePrefix - Username's prefix we want to search.
	 * @return User object with LOW level of detailed
	 */
	@JsonView(JsonViews.LowDetailed.class)
	@RequestMapping(value = "/ubibike/users/{usernamePrefix}",method=RequestMethod.GET)
	public ResponseEntity<Collection<User>> getAllUsers(@PathVariable String usernamePrefix){
		Collection<User> users = userServices.getUsersByUsernamePrefix(usernamePrefix);
		Collections.sort((List<User>) users);
		return new ResponseEntity<Collection<User>>(users,HttpStatus.OK);
	}
	
	/**
	 * Get a user trajectory.
	 * @param username
	 * @param date
	 * @return Trajectory
	 * @throws UserDoesntExistException
	 * @throws TrajectoryDoesntExistException
	 */
	@RequestMapping(value = "/ubibike/user/{username}/trajectory/{date}", method = RequestMethod.GET)
	public ResponseEntity<Trajectory> getUserTrajectory(@PathVariable String username, @PathVariable long date) 
			throws UserDoesntExistException, TrajectoryDoesntExistException{
		Trajectory t = userServices.getUserTrajectory(username, date);
		return new ResponseEntity<Trajectory>(t,HttpStatus.OK);
	}
	
	/**
	 * Used to synchronize information from mobile with server.
	 * @param username
	 * @param points
	 * @param trajectories
	 * @throws UserDoesntExistException
	 */
	@RequestMapping(value = "/ubibike/user/{username}/points/{points}",method=RequestMethod.POST)
	public void synchronizeUser(@PathVariable String username,@PathVariable long points,
			@RequestBody List<Trajectory> trajectories) throws UserDoesntExistException{
		userServices.synchronizeUser(username,points,trajectories);
	}
	
	// ############################# Bike Mapper ####################################
	
	/**
	 * Alert when a bike has been picked by a user.
	 * @param id - Bike unique identifier.
	 * @throws BikeDoesntExistException
	 */
	@RequestMapping(value="/ubibike/user/{username}/pick/bike/{id}",method=RequestMethod.PUT)
	public void pickBike(@PathVariable String id,
			@PathVariable String username) throws BikeDoesntExistException{
		userServices.bikePicked(username,id);
	}
	
	/**
	 * Alert when a bike has been dropped in a station by user.
	 * @param id - Bike identifier.
	 * @param station - Station name.
	 * @param position - Bike position (coordinates).
	 * @throws BikeDoesntExistException
	 * @throws StationDoesntExistException 
	 */
	@RequestMapping(value="/ubibike/user/drop/bike/{id}/station/{station}",method=RequestMethod.PUT)
	public void bikeDropped(@PathVariable String id, @PathVariable String station,
			@RequestBody Coordinates position) throws BikeDoesntExistException, StationDoesntExistException{
		userServices.bikeDropped(id,station,position);
	}
	
	/**
	 * Call when a user book a specific bike.
	 * @param id - Bike identifier.
	 * @throws BikeDoesntExistException
	 * @throws BikeAlreadyBookedException 
	 * @throws CantBookMoreBikesException 
	 */
	@RequestMapping(value="/ubibike/user/{username}/book/bike/{id}",method=RequestMethod.PUT)
	public ResponseEntity<Bike> bookABike(@PathVariable String id,@PathVariable String username) 
			throws BikeDoesntExistException, BikeAlreadyBookedException, CantBookMoreBikesException{
		Bike bike = userServices.bookABike(username,id);
		return new ResponseEntity<Bike>(bike,HttpStatus.OK);
	}
	
}
