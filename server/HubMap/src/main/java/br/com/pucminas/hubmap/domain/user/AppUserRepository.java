package br.com.pucminas.hubmap.domain.user;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppUserRepository extends CrudRepository<AppUser, Integer> {
	
	AppUser findByEmail(String email);
}
