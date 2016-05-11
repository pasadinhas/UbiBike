package pt.ulisboa.tecnico.cmu.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pt.ulisboa.tecnico.cmu.domain.Bike;
import pt.ulisboa.tecnico.cmu.domain.Coordinates;
import pt.ulisboa.tecnico.cmu.domain.Station;
import pt.ulisboa.tecnico.cmu.domain.Trajectory;
import pt.ulisboa.tecnico.cmu.domain.User;
import pt.ulisboa.tecnico.cmu.domain.exceptions.BikeDoesntExistException;
import pt.ulisboa.tecnico.cmu.domain.exceptions.InvalidLoginException;
import pt.ulisboa.tecnico.cmu.domain.exceptions.StationDoesntExistException;
import pt.ulisboa.tecnico.cmu.domain.exceptions.TrajectoryAlreadyExistException;
import pt.ulisboa.tecnico.cmu.domain.exceptions.TrajectoryDoesntExistException;
import pt.ulisboa.tecnico.cmu.domain.exceptions.UserAlreadyExistException;
import pt.ulisboa.tecnico.cmu.domain.exceptions.UserDoesntExistException;
import pt.ulisboa.tecnico.cmu.persistence.repositories.BikeRepository;
import pt.ulisboa.tecnico.cmu.persistence.repositories.StationRepository;
import pt.ulisboa.tecnico.cmu.persistence.repositories.UserRepository;

@Service
public class UserServices {

	@Autowired
	private BikeRepository bikeRepository;
	
	@Autowired
	private StationRepository stationRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	
	public User loginUser(String username,String password) 
			throws UserDoesntExistException, InvalidLoginException{
		User user = getUserFromRepository(username);
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
	
	
	public Trajectory getUserTrajectory(String username,Date date) 
			throws UserDoesntExistException, TrajectoryDoesntExistException{
		System.out.println("[GET DATE AFTER]"+date.getTime());
		User user = getUserFromRepository(username);
		return user.getTrajectory(date);
	}
	
	public Collection<User> getUsersByUsernamePrefix(String usernamePrefix){
		List<User> users = new ArrayList<>();
		Iterable<User> storedUsers = userRepository.findAll();
		for(User user : storedUsers){
			if(user.getUsername().startsWith(usernamePrefix)){
				users.add(user);
			}
		}
		return users;
	}
	
	@Transactional
	public void synchronizeUser(String username,long points,List<Trajectory> trajectories)
		throws UserDoesntExistException{
		User user = getUserFromRepository(username);
		user.setPoints(points);
		for(Trajectory newTraj : trajectories){
			try{
				System.out.println("[NEWDATE SYNC]"+newTraj.getDate().getTime());
				user.addTrajectory(newTraj);
			}catch(TrajectoryAlreadyExistException ex){
				continue;
			}
		}
		userRepository.save(user);
	}
	
	
	public User getUser(String username) throws UserDoesntExistException{
		User user = getUserFromRepository(username);
		return user;
	}

	//############################## Bike Methods ####################################
	
	public void bikePicked(String user,String bikeId) throws BikeDoesntExistException{
		Bike bike = getBikeFromRepository(bikeId);
		if(bike.getBooked() && !bike.getPicked() && user.equals(bike.getUser().getUsername())){
			bike.setStation(null);
			bike.setPicked(true);
			bikeRepository.save(bike);
		}
	}
	
	@Transactional
	public void bikeDropped(String bikeId,String stationName,Coordinates bikePos) 
			throws BikeDoesntExistException, StationDoesntExistException{
		Bike bike = getBikeFromRepository(bikeId);
		Station sta = getStationFromRepository(stationName);
		if(bike.getPicked()){
			sta.addBike(bike);
			bike.setStation(sta);
			bike.setPosition(bikePos);
			bike.setPicked(false);
			bike.setBooked(false);
			User user = bike.getUser();
			user.setReservedBike(null);
			bike.setUser(null);
			bikeRepository.save(bike);
			stationRepository.save(sta);
			userRepository.save(user);
		}
	}
	
	public Bike bookABike(String username,String bikeId) throws BikeDoesntExistException{
		Bike bike = getBikeFromRepository(bikeId);
		User user = userRepository.findOne(username);
		user.setReservedBike(bike);
		bike.setBooked(true);
		bike.setUser(user);
		bikeRepository.save(bike);
		userRepository.save(user);
		return bike;
	}
	
	/*========================= Repository Access Methods =========================== */
	
	private User getUserFromRepository(String username) throws UserDoesntExistException{
		User user = userRepository.findOne(username);
		if(user == null)
			throw new UserDoesntExistException();
		return user;
	}
	
	private Station getStationFromRepository(String stationName) throws StationDoesntExistException{
		Station station = stationRepository.findOne(stationName);
		if(station == null)
			throw new StationDoesntExistException();
		return station;
	}
	
	private Bike getBikeFromRepository(String id) throws BikeDoesntExistException{
		Bike bike = bikeRepository.findOne(id);
		if(bike == null)
			throw new BikeDoesntExistException();
		return bike;
	}
	
}
