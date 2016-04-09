package pt.ulisboa.tecnico.cmu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import pt.ulisboa.tecnico.cmu.domain.exceptions.TrajectoryAlreadyExistException;

@Component
class InitializerComponent {
	
	@Autowired
	private InitializeDatabase init;
	
    @EventListener({ContextRefreshedEvent.class})
    void contextRefreshedEvent() throws TrajectoryAlreadyExistException {
        init.populateDatabase();
    }
}
