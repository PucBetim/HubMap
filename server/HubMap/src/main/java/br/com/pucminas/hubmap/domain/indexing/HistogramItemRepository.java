package br.com.pucminas.hubmap.domain.indexing;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(exported = false)
public interface HistogramItemRepository extends CrudRepository<HistogramItem, Long>{

	List<HistogramItem> findByOwner(Histogram owner);
	
	HistogramItem findByKeyAndOwner(NGram nGram, Histogram owner);
	
	@Query("SELECT hi.tfidf FROM HistogramItem hi WHERE hi.owner.id = ?2 AND hi.key.id = ?1 ")
	Double findTfIdfByKeyAndOwner(Long nGramId, Long ownerId);
	
	@Modifying(flushAutomatically = true, clearAutomatically = true)
	@Query("DELETE FROM HistogramItem hi WHERE hi.owner.id = ?1")
	void deleteAllByOwner(Long histId);
}
