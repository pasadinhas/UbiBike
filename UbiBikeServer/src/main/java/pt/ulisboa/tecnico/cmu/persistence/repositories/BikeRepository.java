package pt.ulisboa.tecnico.cmu.persistence.repositories;

import org.springframework.data.repository.CrudRepository;

import pt.ulisboa.tecnico.cmu.domain.Bike;

public interface BikeRepository extends CrudRepository<Bike, String>{

	
}
