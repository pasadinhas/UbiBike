package pt.ulisboa.tecnico.cmu.persistence;

import org.springframework.data.repository.CrudRepository;

import pt.ulisboa.tecnico.cmu.domain.Station;

public interface StationRepository extends CrudRepository<Station,String>{

}
