package pt.ulisboa.tecnico.cmu.services;

import java.util.Collection;
import java.util.List;
import java.util.TreeSet;

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
	
	public void createUser(String username,String password) throws UserAlreadyExistException{
		if(userRepository.findOne(username) != null)
			throw new UserAlreadyExistException();
		User newUser = new User(username,password);
		userRepository.save(newUser);
	}
	
	
	public User getUserInformation(String username) throws UserDoesntExistException{
		User user = getUser(username);
		return user;
	}
	
	public Collection<String> getUsers(String usernamePrefix){
		TreeSet<String> users = new TreeSet<>();
		for(User user : userRepository.findAll()){
			if(user.getUsername().startsWith(usernamePrefix)){
				users.add(user.getUsername());
			}
		}
		return users;
	}
	
	public void synchronizeUser(String username,long points,List<Trajectory> trajectories)
		throws UserDoesntExistException{
		User user = getUser(username);
		user.setPoints(points);
		for(Trajectory newTraj : trajectories){
			try{
				user.addTrajectory(newTraj);
			}catch(TrajectoryAlreadyExistException ex){
				continue;
			}
		}
		userRepository.save(user);
	}
	
	private User getUser(String username) throws UserDoesntExistException{
		User user = userRepository.findOne(username);
		if(user == null)
			throw new UserDoesntExistException();
		return user;
	}
	
	
}
