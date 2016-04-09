package pt.ulisboa.tecnico.cmu.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pt.ulisboa.tecnico.cmu.domain.Trajectory;
import pt.ulisboa.tecnico.cmu.domain.User;
import pt.ulisboa.tecnico.cmu.domain.exceptions.InvalidLoginException;
import pt.ulisboa.tecnico.cmu.domain.exceptions.TrajectoryAlreadyExistException;
import pt.ulisboa.tecnico.cmu.domain.exceptions.UserAlreadyExistException;
import pt.ulisboa.tecnico.cmu.domain.exceptions.UserDoesntExistException;
import pt.ulisboa.tecnico.cmu.domain.repositories.UserRepository;

@Service
public class UserServices {

	@Autowired
	private UserRepository userRepository;
	
	
	public User loginUser(String username,String password) 
			throws UserDoesntExistException, InvalidLoginException{
		User user = getUser(username);
		if(!user.getPassword().equals(password))
			throw new InvalidLoginException();
		return user;
	}
	
	public User createUser(String username,String password) throws UserAlreadyExistException{
		if(userRepository.findOne(username) != null)
			throw new UserAlreadyExistException();
		User newUser = new User(username,password);
		userRepository.save(newUser);
		return newUser;
	}
	
	public User updateUserPoints(String username,long points) throws UserDoesntExistException{
		User user = getUser(username);
		user.setPoints(points);
		userRepository.save(user);
		return user;
	}
	
	public User addTrajectory(String username,Trajectory trajectory) 
			throws UserDoesntExistException, TrajectoryAlreadyExistException{
		User user = getUser(username);
		user.addTrajectory(trajectory);
		userRepository.save(user);
		return user;
	} 
	
	public User getUserInformation(String username) throws UserDoesntExistException{
		User user = getUser(username);
		return user;
	}
	
	public List<String> getUsers(String usernamePrefix){
		List<String> users = new ArrayList<>();
		for(User user : userRepository.findAll()){
			if(user.getUsername().startsWith(usernamePrefix)){
				users.add(user.getUsername());
			}
		}
		return users;
	}
	
	private User getUser(String username) throws UserDoesntExistException{
		User user = userRepository.findOne(username);
		if(user == null)
			throw new UserDoesntExistException();
		return user;
	}
	
}
