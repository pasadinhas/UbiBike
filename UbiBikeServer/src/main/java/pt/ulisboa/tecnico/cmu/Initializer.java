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
        Bike b3 = new Bike(new Coordinates(38.769839, -9.204999));
        User u = new User("Admin","root");
        u.getTrajectories().add(new Trajectory(new Date()));
        Station s1 = new Station("Carnide");
        s1.getBikes().add(b1);
        s1.getBikes().add(b2);
        Station s2 = new Station("Alvalade");
        s2.getBikes().add(b3);
        statRepo.save(s1);
        statRepo.save(s2);
        userRepo.save(u);
        System.out.println("###### BD Initialized ######");
    }
}
