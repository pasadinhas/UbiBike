package pt.ulisboa.tecnico.cmu.domain.repositories;

import org.springframework.data.repository.CrudRepository;

import pt.ulisboa.tecnico.cmu.domain.User;

public interface UserRepository extends CrudRepository<User,String> {

}
