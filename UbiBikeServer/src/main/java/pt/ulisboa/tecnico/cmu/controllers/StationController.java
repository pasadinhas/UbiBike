package pt.ulisboa.tecnico.cmu.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;

import pt.ulisboa.tecnico.cmu.controllers.util.JsonViews;
import pt.ulisboa.tecnico.cmu.domain.Station;
import pt.ulisboa.tecnico.cmu.services.StationServices;

@RestController
public class StationController {
	
	@Autowired
	private StationServices stationsServices;
	
	/**
	 * Get stations that have available bikes to be booked, with LOW detail.
	 * @return List - List of Station Objects
	 */
	@JsonView(JsonViews.LowDetailed.class)
	@RequestMapping(value="/ubibike/station/low",method = RequestMethod.GET)
	public ResponseEntity<List<Station>> getStationsWithAvailableBikesLowDetail(){
		return new ResponseEntity<List<Station>>(stationsServices.getStationsWithAvailableBikes(),HttpStatus.OK);
	}
	
	/**
	 * Get stations that have available bikes to be booked, with HIGH detail.
	 * @return List - List of Station Objects
	 */
	@JsonView(JsonViews.HighlyDetailed.class)
	@RequestMapping(value="/ubibike/station/high",method = RequestMethod.GET)
	public ResponseEntity<List<Station>> getStationsWithAvailableBikesHighDetail(){
		return new ResponseEntity<List<Station>>(stationsServices.getStationsWithAvailableBikes(),HttpStatus.OK);
	}
}
