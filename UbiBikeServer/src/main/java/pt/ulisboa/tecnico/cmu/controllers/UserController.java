package pt.ulisboa.tecnico.cmu.controllers;

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
	
	/* ============= RESTful services =============== */
	
	@RequestMapping(value="/ubibike/user/login/{username}",method=RequestMethod.POST)
	public void login(
			@PathVariable String username,
			@RequestParam(value="password")String password) 
			throws UserDoesntExistException, InvalidLoginException{
		userServices.loginUser(username, password);
	}
	
	@RequestMapping(value = "/ubibike/user/{username}",method=RequestMethod.POST)
	public void createUser(
			@PathVariable String username,
			@RequestParam(value="password")String password) throws UserAlreadyExistException {
		userServices.createUser(username, password);
	}
	
	@RequestMapping(value = "/ubibike/user/{username}",method=RequestMethod.GET)
	public ResponseEntity<User> getUserInformation(
			@PathVariable String username) throws UserDoesntExistException{
		User user = userServices.getUserInformation(username);
		return new ResponseEntity<User>(user,HttpStatus.OK);
	}
	
	@RequestMapping(value = "/ubibike/users/{usernamePrefix}",method=RequestMethod.GET)
	public ResponseEntity<List<String>> getUsers(@PathVariable String usernamePrefix){
		List<String> users = userServices.getUsers(usernamePrefix);
		return new ResponseEntity<List<String>>(users,HttpStatus.OK);
	}
	
	@RequestMapping(value = "/ubibike/user/{username}/points/{points}",method=RequestMethod.POST)
	public void synchronizeUser(@PathVariable String username,@PathVariable long points,
			@RequestBody List<Trajectory> trajectories) throws UserDoesntExistException{
		userServices.synchronizeUser(username,points,trajectories);
	}
	
}
