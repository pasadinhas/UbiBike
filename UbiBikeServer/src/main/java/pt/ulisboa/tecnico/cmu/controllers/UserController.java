package pt.ulisboa.tecnico.cmu.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import pt.ulisboa.tecnico.cmu.domain.User;
import pt.ulisboa.tecnico.cmu.domain.exceptions.UbiBikeServerException;
import pt.ulisboa.tecnico.cmu.domain.exceptions.UserAlreadyExistException;
import pt.ulisboa.tecnico.cmu.domain.exceptions.UserDoesntExistException;
import pt.ulisboa.tecnico.cmu.domain.repositories.UserRepository;

@RestController
public class UserController {
	
	@Autowired
	private UserRepository userRepository;
	
	/* ============== Error Handling ================ */
	
	@ExceptionHandler(UserAlreadyExistException.class)
	public Error handleUserAlreadyExistException(UserAlreadyExistException ex,
			HttpServletRequest request) {
		return new Error(409,"User already exists");
	}
	
	@ExceptionHandler(UserDoesntExistException.class)
	public Error handleUserAlreadyExistException(UserDoesntExistException ex,
			HttpServletRequest request) {
		return new Error(404,"User doesnt exists");
	}
	
	/* ============================================== */
	
	@RequestMapping(value = "/ubibike/user/{username}",method=RequestMethod.POST)
	public ResponseEntity<Error> createUser(
			@PathVariable String username,
			@RequestParam(value="password")String password) throws UbiBikeServerException{
		if(userRepository.findOne(username) != null)
			return ResponseEntity.status(HttpStatus.valueOf(404)).body(new Error(404,"User already exists"));
		userRepository.save(new User(username,password));
		return ResponseEntity.ok(null);
	}
	
	@RequestMapping(value = "/ubibike/user/{username}/points/{points}",method=RequestMethod.PUT)
	public void updateUserPoints(
			@PathVariable String username,
			@PathVariable(value="points")long points) throws UserDoesntExistException{
		User user = userRepository.findOne(username); 
		if(user == null)
			throw new UserDoesntExistException();
		user.setPoints(points);
		userRepository.save(user);
	}
	
	@RequestMapping(value= "/ubibike/user/{username}/trajectory",method=RequestMethod.PUT)
	public void addTrajectory(@PathVariable String username){
		//TODO
	}
	
	@RequestMapping(value = "/ubibike/user/{username}",method=RequestMethod.GET)
	public User getUserInformation(
			@PathVariable String username) throws UserDoesntExistException{
		User user = userRepository.findOne(username); 
		if(user == null)
			throw new UserDoesntExistException();
		return user;
	}
	
}
