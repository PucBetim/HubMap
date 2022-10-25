package br.com.pucminas.hubmap.domain.indexing;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(exported = false)
public interface NGramRepository extends CrudRepository<NGram, Long>{
	
	Set<NGram> findByVocabularyOrderById(Vocabulary vocabulary);
	
	@Query("SELECT n FROM NGram n WHERE n.newVocabulary.id = ?1")
	Set<NGram> findByNewVocabulary(Long vocab);
	
	Optional<NGram> findByGramAndNewVocabulary(String gram, Vocabulary newVocab);
	
	Optional<NGram> findByGramAndVocabulary(String gram, Vocabulary vocab);
}
