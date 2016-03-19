package pt.ulisboa.tecnico.cmu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import pt.ulisboa.tecnico.cmu.domain.Bike;
import pt.ulisboa.tecnico.cmu.domain.Station;
import pt.ulisboa.tecnico.cmu.domain.repositories.StationRepository;

@Component
class Initializer {

	@Autowired
	private StationRepository statRepo;
	
    @EventListener({ContextRefreshedEvent.class})
    void contextRefreshedEvent() {
        System.out.println("###### Initializing BD ######");
        Station s1 = new Station("Carnide");
        s1.getBikes().add(new Bike());
        s1.getBikes().add(new Bike());
        Station s2 = new Station("Alvalade");
        s2.getBikes().add(new Bike());
        statRepo.save(s1);
        statRepo.save(s2);
    }
}
