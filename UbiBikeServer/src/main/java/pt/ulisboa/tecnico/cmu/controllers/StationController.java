package pt.ulisboa.tecnico.cmu.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import pt.ulisboa.tecnico.cmu.domain.Station;
import pt.ulisboa.tecnico.cmu.services.StationServices;

@RestController
public class StationController {

	@Autowired
	private StationServices stationsServices;
	
	@RequestMapping(value="/ubibike/station",method = RequestMethod.GET)
	public ResponseEntity<List<Station>> getStationsWithAvailableBikes(){
		List<Station> stations = stationsServices.getStationsWithAvailableBikes();
		return new ResponseEntity<List<Station>>(stations,HttpStatus.OK);
	}
	
}
