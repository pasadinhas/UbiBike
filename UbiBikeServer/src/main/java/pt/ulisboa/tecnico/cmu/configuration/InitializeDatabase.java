package pt.ulisboa.tecnico.cmu.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pt.ulisboa.tecnico.cmu.persistence.BikeRepository;
import pt.ulisboa.tecnico.cmu.persistence.StationRepository;
import pt.ulisboa.tecnico.cmu.persistence.UserRepository;

@Component
public class InitializeDatabase {
	
	@Autowired
	private ReadConfigurationFile config;
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private StationRepository stationRepo;
	
	@Autowired
	private BikeRepository bikeRepo;

	
	public void populateDatabase(){
		config.startConfigurationFile();
	}
	
	
	public void wipeoutDatabase(){
		stationRepo.deleteAll();
		userRepo.deleteAll();
		bikeRepo.deleteAll();
	}
	
	public void resetDatabase(){
		wipeoutDatabase();
		populateDatabase();
	}
	
}
