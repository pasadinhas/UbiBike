package pt.ulisboa.tecnico.cmu.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import pt.ulisboa.tecnico.cmu.domain.Station;
import pt.ulisboa.tecnico.cmu.services.StationServices;

@RestController
public class StationController {

	private final static String STATION_DETAIL_LOW = "low";
	
	@Autowired
	private StationServices stationsServices;
	
	/**
	 * Get stations that have available bikes to be booked.
	 * @param detail - Specifies the detail of returned objects.
	 * @return List - List of Station Objects
	 */
	@RequestMapping(value="/ubibike/station",method = RequestMethod.GET)
	public ResponseEntity<List<Station>> getStationsWithAvailableBikes(
			@RequestParam(value="detail",required = false) String detail){
		List<Station> stations = stationsServices.getStationsWithAvailableBikes();
		if(detail != null && detail.equals(STATION_DETAIL_LOW)){
			for(Station s : stations){
				s.setStationDetailLow();
			}
		}
		return new ResponseEntity<List<Station>>(stations,HttpStatus.OK);
	}
	
}
