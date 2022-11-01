package br.com.pucminas.hubmap.domain.indexing;

import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(exported = false)
public interface HistogramRepository extends PagingAndSortingRepository<Histogram, Long>{
	
	@Query("SELECT h FROM Histogram h WHERE h.post.isPrivate = false AND h.initialized = true")
	Page<Histogram> findByInitilized(Boolean initilized, Pageable pageable);
	
	@Query("SELECT h.id FROM Histogram h WHERE h.post.isPrivate = false AND h.initialized = ?1")
	Page<Integer> findIdsByInitilized(Boolean initilized, Pageable pageable);
	
	@Query("SELECT count(h) FROM Histogram h WHERE h.initialized = ?1")
	Long countByInitialized(Boolean initialized);
	
	@Override
	Page<Histogram> findAll(Pageable pageable);
	
	@Override
	Set<Histogram> findAll();
}
