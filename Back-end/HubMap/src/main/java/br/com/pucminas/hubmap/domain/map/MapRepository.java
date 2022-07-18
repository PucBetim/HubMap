package br.com.pucminas.hubmap.domain.map;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MapRepository extends CrudRepository<Map, Integer>{
//TODO Check if this class will really inherit from CrudRepository	
}
