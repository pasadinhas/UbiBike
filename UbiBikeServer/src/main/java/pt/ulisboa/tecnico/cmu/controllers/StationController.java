package pt.ulisboa.tecnico.cmu.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import pt.ulisboa.tecnico.cmu.domain.Station;
import pt.ulisboa.tecnico.cmu.domain.repositories.StationRepository;

@RestController
public class StationController {

	@Autowired
	private StationRepository stationRepository;
	
	@RequestMapping(value="/ubibike/station",method = RequestMethod.GET)
	public List<Station> getStationsWithAvailableBikes(){
		List<Station> stations = new ArrayList<>();
		for(Station s : stationRepository.findAll()){
			s.removeBookedBikes();
			if(s.oneBikeAvailableAtLeast())
				stations.add(s);
		}
		return stations;
	}
	
}
