package pt.ulisboa.tecnico.cmu;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import pt.ulisboa.tecnico.cmu.domain.Bike;
import pt.ulisboa.tecnico.cmu.domain.Coordinates;
import pt.ulisboa.tecnico.cmu.domain.Station;
import pt.ulisboa.tecnico.cmu.domain.Trajectory;
import pt.ulisboa.tecnico.cmu.domain.User;
import pt.ulisboa.tecnico.cmu.domain.repositories.StationRepository;
import pt.ulisboa.tecnico.cmu.domain.repositories.UserRepository;

@Component
class Initializer {

	@Autowired
	private StationRepository statRepo;
	
	@Autowired
	private UserRepository userRepo;
	
    @EventListener({ContextRefreshedEvent.class})
    void contextRefreshedEvent() {
        System.out.println("###### Initializing BD ######");
        Bike b1 = new Bike(new Coordinates(38.769836, -9.204510));
        Bike b2 = new Bike(new Coordinates(38.769832, -9.204973));
        Bike b3 = new Bike(new Coordinates(38.754212, -9.144914));
        Bike b4 = new Bike(new Coordinates(38.736976, -9.139048));
        Bike b5 = new Bike(new Coordinates(38.754206, -9.145207));
        User u = new User("Admin","root");
        u.getTrajectories().add(new Trajectory(new Date()));
        Station s1 = new Station("Carnide",new Coordinates(38.770018, -9.204704));
        s1.addBike(b1);
        s1.addBike(b2);;
        Station s2 = new Station("Alvalade",new Coordinates(38.754120, -9.144843));
        s2.addBike(b3);
        s2.addBike(b5);
        Station s3 = new Station("IST",new Coordinates(38.737045, -9.139072));
        s3.addBike(b4);
        statRepo.save(s1);
        statRepo.save(s2);
        statRepo.save(s3);
        userRepo.save(u);
        System.out.println("###### BD Initialized ######");
    }
}
