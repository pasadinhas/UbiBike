package pt.ulisboa.tecnico.cmu.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import pt.ulisboa.tecnico.cmu.domain.Return;

@RestController
public class ReturnController {

	private int counter = 0;
	
	
	@RequestMapping("/hey")
	public Return hey(@RequestParam(value="name",defaultValue="Hello") String name){

        Return r = new Return(name, counter++);

        
		return r;
	}
	
}
