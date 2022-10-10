package br.com.pucminas.hubmap.domain.indexing;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(exported = false)
public interface VocabularyRepository extends CrudRepository<Vocabulary, Long> {

}
