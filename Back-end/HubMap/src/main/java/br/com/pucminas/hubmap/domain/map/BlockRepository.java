package br.com.pucminas.hubmap.domain.map;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlockRepository extends CrudRepository<Block, Integer>{
//TODO Check if this class will really inherit from CrudRepository	
}
