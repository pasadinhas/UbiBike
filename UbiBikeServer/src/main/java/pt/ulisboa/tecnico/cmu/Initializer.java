package pt.ulisboa.tecnico.cmu;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import pt.ulisboa.tecnico.cmu.domain.Bike;
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
        User u = new User("Admin","root");
        u.getTrajectories().add(new Trajectory(new Date()));
        Station s1 = new Station("Carnide");
        s1.getBikes().add(new Bike());
        s1.getBikes().add(new Bike());
        Station s2 = new Station("Alvalade");
        s2.getBikes().add(new Bike());
        statRepo.save(s1);
        statRepo.save(s2);
        userRepo.save(u);
        System.out.println("###### BD Initialized ######");
    }
}
