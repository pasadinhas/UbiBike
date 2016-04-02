package pt.ulisboa.tecnico.cmu.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pt.ulisboa.tecnico.cmu.InitializeDatabase;

@RestController
public class DebugWipeoutController {
	
	@Autowired
	private InitializeDatabase init;
	
	@RequestMapping(value="/ubibike/RESET")
	public void resetDatabase(){
		init.resetDatabase();
	}
	
}
