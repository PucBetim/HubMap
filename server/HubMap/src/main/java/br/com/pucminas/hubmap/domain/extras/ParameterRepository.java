package br.com.pucminas.hubmap.domain.extras;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(exported = false)
public interface ParameterRepository extends CrudRepository<Parameter, Long>{
	
	List<Parameter> findByTableName(String table);
}
