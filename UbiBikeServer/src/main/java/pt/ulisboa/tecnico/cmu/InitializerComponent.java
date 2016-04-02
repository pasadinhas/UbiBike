package pt.ulisboa.tecnico.cmu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
class InitializerComponent {
	
	@Autowired
	private InitializeDatabase init;
	
    @EventListener({ContextRefreshedEvent.class})
    void contextRefreshedEvent() {
        init.populateDatabase();
    }
}
