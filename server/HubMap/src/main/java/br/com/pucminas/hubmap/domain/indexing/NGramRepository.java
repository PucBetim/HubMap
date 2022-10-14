package br.com.pucminas.hubmap.domain.indexing;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(exported = false)
public interface NGramRepository extends CrudRepository<NGram, Long>{
	
	@Query("SELECT ng FROM NGram ng WHERE ng.id.post.id = ?1")
	List<NGram> findByPost(Integer post);
}
