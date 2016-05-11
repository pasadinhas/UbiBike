package pt.ulisboa.tecnico.cmu;

import java.util.Calendar;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pt.ulisboa.tecnico.cmu.domain.Bike;
import pt.ulisboa.tecnico.cmu.domain.Coordinates;
import pt.ulisboa.tecnico.cmu.domain.Station;
import pt.ulisboa.tecnico.cmu.domain.Trajectory;
import pt.ulisboa.tecnico.cmu.domain.User;
import pt.ulisboa.tecnico.cmu.persistence.repositories.BikeRepository;
import pt.ulisboa.tecnico.cmu.persistence.repositories.StationRepository;
import pt.ulisboa.tecnico.cmu.persistence.repositories.UserRepository;

@Component
public class InitializeDatabase {

	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private StationRepository stationRepo;
	
	@Autowired
	private BikeRepository bikeRepo;
	
	public void populateDatabase(){
		System.out.println("###### Initializing BD ######");
		Calendar c = Calendar.getInstance();
		c.set(Calendar.MILLISECOND,0);
		c.set(2015, 11, 6, 18, 11, 32);
		Date d = c.getTime();
		Trajectory t = new Trajectory(d);
		t.addCoordinates(new Coordinates(38.769836, -9.204510));
		t.addCoordinates(new Coordinates(38.770108, -9.204942));
		t.addCoordinates(new Coordinates(38.770362, -9.205211));
		t.addCoordinates(new Coordinates(38.770119, -9.205608));
		t.addCoordinates(new Coordinates(38.769316, -9.205436));
		t.addCoordinates(new Coordinates(38.769425, -9.205093));
		t.addCoordinates(new Coordinates(38.769642, -9.204707));
		c.set(2015,12, 2, 14, 50);
		Date d1 = c.getTime();
		Trajectory t1 = new Trajectory(d1);
		t1.addCoordinates(new Coordinates(38.754212, -9.144914));
		t1.addCoordinates(new Coordinates(38.754471, -9.144721));
		t1.addCoordinates(new Coordinates(38.755040, -9.142897));
		t1.addCoordinates(new Coordinates(38.753701, -9.142203));
		t1.addCoordinates(new Coordinates(38.753701, -9.142203));
		t1.addCoordinates(new Coordinates(38.753450, -9.142938));
		t1.addCoordinates(new Coordinates(38.754212, -9.144914));
        Bike b1 = new Bike(new Coordinates(38.769836, -9.204510),"1");
        Bike b2 = new Bike(new Coordinates(38.769832, -9.204973),"2");
        Bike b3 = new Bike(new Coordinates(38.754212, -9.144914),"3");
        Bike b4 = new Bike(new Coordinates(38.736976, -9.139048),"4");
        Bike b5 = new Bike(new Coordinates(38.754206, -9.145207),"5");
        User u = new User("Admin","root");
        u.setPoints(619);
        try{
        	u.addTrajectory(t);
            u.addTrajectory(t1);
        }catch(Exception e){
        	//DO Nothing
        }
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
        User u1 = new User("Andre","123");
        User u2 = new User("António","123");
        User u3 = new User("Alexandre","123");
        User u4 = new User("Álvaro","123");
        User u5 = new User("Bruno","123");
        userRepo.save(u);
        userRepo.save(u1);
        userRepo.save(u2);
        userRepo.save(u3);
        userRepo.save(u4);
        userRepo.save(u5);
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
