package pt.ulisboa.tecnico.cmu.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pt.ulisboa.tecnico.cmu.domain.Station;
import pt.ulisboa.tecnico.cmu.persistence.StationRepository;

@Service
public class StationServices {

	@Autowired
	private StationRepository stationRepository;
	
	public Collection<Station> getStationsWithAvailableBikes(){
		List<Station> stations = new ArrayList<>();
		for(Station s : stationRepository.findAll()){
			s.removeBookedBikes();
			if(s.oneBikeAvailableAtLeast())
				stations.add(s);
		}
		return stations;
	}
	
}
