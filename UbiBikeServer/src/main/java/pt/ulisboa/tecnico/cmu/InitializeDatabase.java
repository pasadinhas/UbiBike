package pt.ulisboa.tecnico.cmu;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import pt.ulisboa.tecnico.cmu.domain.Bike;
import pt.ulisboa.tecnico.cmu.domain.Coordinates;
import pt.ulisboa.tecnico.cmu.domain.Station;
import pt.ulisboa.tecnico.cmu.domain.Trajectory;
import pt.ulisboa.tecnico.cmu.domain.User;
import pt.ulisboa.tecnico.cmu.domain.repositories.BikeRepository;
import pt.ulisboa.tecnico.cmu.domain.repositories.StationRepository;
import pt.ulisboa.tecnico.cmu.domain.repositories.UserRepository;

@Controller
public class InitializeDatabase {

	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private StationRepository stationRepo;
	
	@Autowired
	private BikeRepository bikeRepo;
	
	public void populateDatabase(){
		System.out.println("###### Initializing BD ######");
        Bike b1 = new Bike(new Coordinates(38.769836, -9.204510),"1");
        Bike b2 = new Bike(new Coordinates(38.769832, -9.204973),"2");
        Bike b3 = new Bike(new Coordinates(38.754212, -9.144914),"3");
        Bike b4 = new Bike(new Coordinates(38.736976, -9.139048),"4");
        Bike b5 = new Bike(new Coordinates(38.754206, -9.145207),"5");
        User u = new User("Admin","root");
        u.getTrajectories().add(new Trajectory(new Date()));
        Station s1 = new Station("Carnide",new Coordinates(38.770018, -9.204704));
        s1.addBike(b1);
        s1.addBike(b2);
        b1.setStation(s1);
        b2.setStation(s1);
        Station s2 = new Station("Alvalade",new Coordinates(38.754120, -9.144843));
        s2.addBike(b3);
        s2.addBike(b5);
        b3.setStation(s2);
        b5.setStation(s2);
        Station s3 = new Station("IST",new Coordinates(38.737045, -9.139072));
        s3.addBike(b4);
        b4.setStation(s3);
        stationRepo.save(s1);
        stationRepo.save(s2);
        stationRepo.save(s3);
        userRepo.save(u);
        System.out.println("###### BD Initialized ######");
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
