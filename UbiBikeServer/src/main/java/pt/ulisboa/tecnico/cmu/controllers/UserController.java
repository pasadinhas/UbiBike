package pt.ulisboa.tecnico.cmu.controllers;

import java.util.Collection;
import java.util.Date;
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

import pt.ulisboa.tecnico.cmu.domain.Trajectory;
import pt.ulisboa.tecnico.cmu.domain.User;
import pt.ulisboa.tecnico.cmu.domain.exceptions.InvalidLoginException;
import pt.ulisboa.tecnico.cmu.domain.exceptions.TrajectoryAlreadyExistException;
import pt.ulisboa.tecnico.cmu.domain.exceptions.TrajectoryDoesntExistException;
import pt.ulisboa.tecnico.cmu.domain.exceptions.UserAlreadyExistException;
import pt.ulisboa.tecnico.cmu.domain.exceptions.UserDoesntExistException;
import pt.ulisboa.tecnico.cmu.services.UserServices;

@RestController
public class UserController {
	
	private static final String USER_DETAIL_MEDIUM = "medium";
	
	private static final String USER_DETAIL_LOW = "low";
	
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
	
	/* ============= RESTful services =============== */
	
	/**
	 * Login method for authenticate user.
	 * @param username
	 * @param password
	 * @return
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
	 * @param username
	 * @param password
	 * @throws UserAlreadyExistException
	 */
	@RequestMapping(value = "/ubibike/user/{username}",method=RequestMethod.POST)
	public void createUser(
			@PathVariable String username,
			@RequestParam(value="password")String password) throws UserAlreadyExistException {
		userServices.createUser(username, password);
	}
	
	/**
	 * Get User information according to wanted detail.
	 * @param username - User's username.
	 * @param detail - Specify the detail of the returned objects. 
	 * @return - User object
	 * @throws UserDoesntExistException
	 */
	@RequestMapping(value = "/ubibike/user/{username}",method=RequestMethod.GET)
	public ResponseEntity<User> getUserInformation(
			@PathVariable String username,
			@RequestParam(required = false) String detail) throws UserDoesntExistException{
		User user = null;
		if(detail != null && detail.equals(USER_DETAIL_MEDIUM)){
			user = userServices.getUserInformationMedium(username);
		}else if(detail != null && detail.equals(USER_DETAIL_LOW)){
			user = userServices.getUserInformationLow(username);
		}else{
			user = userServices.getUserInformation(username);
		}
		return new ResponseEntity<User>(user,HttpStatus.OK);
	}
	
	/**
	 * Get all usernames given an username prefix.
	 * @param usernamePrefix
	 * @return
	 */
	@RequestMapping(value = "/ubibike/users/{usernamePrefix}",method=RequestMethod.GET)
	public ResponseEntity<Collection<String>> getAllUsernames(@PathVariable String usernamePrefix){
		Collection<String> users = userServices.getUsers(usernamePrefix);
		return new ResponseEntity<Collection<String>>(users,HttpStatus.OK);
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
	public ResponseEntity<Trajectory> getUserTrajectory(
			@PathVariable String username,
			@PathVariable long date) throws UserDoesntExistException, TrajectoryDoesntExistException{
		Trajectory t = userServices.getUserTrajectory(username, new Date(date));
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
	
}
