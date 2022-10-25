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
	
	@Query("SELECT count(hi) FROM HistogramItem hi WHERE hi.key.id = ?1")
	Integer countHistogramsOfNgramId(Long id);
	
	@Modifying(flushAutomatically = true)
	@Query("UPDATE FROM HistogramItem hi SET hi.analyzed = ?2 WHERE hi.owner.id = ?1 ")
	void updateAnalyzed(Integer ownerId, Boolean analyzed);
	
	List<HistogramItem> findByOwnerAndAnalyzed(Histogram owner, Boolean analyzed);
}
