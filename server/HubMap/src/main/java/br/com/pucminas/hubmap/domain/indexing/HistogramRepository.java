package br.com.pucminas.hubmap.domain.indexing;

import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(exported = false)
public interface HistogramRepository extends PagingAndSortingRepository<Histogram, Long>{
	
	Page<Histogram> findByNeedRecount(Boolean needRecount, Pageable pageable);
	
	@Query("SELECT h FROM Histogram h WHERE h.initialized = ?1 AND h.post.isPrivate = false")
	Page<Histogram> findByInitilized(Boolean initilized, Pageable pageable);
	
	@Query("SELECT h.id FROM Histogram h WHERE h.initialized = ?1 AND h.post.isPrivate = false")
	Page<Integer> findIdsByInitilized(Boolean initilized, Pageable pageable);
	
	@Query("SELECT count(h) FROM Histogram h WHERE h.initialized = ?1")
	Long countByInitialized(Boolean initialized);
	
	@Override
	Page<Histogram> findAll(Pageable pageable);
	
	@Override
	Set<Histogram> findAll();
	
	@Modifying(flushAutomatically = true)
	@Query("UPDATE FROM Histogram h SET h.initialized = ?2, h.needRecount = ?3 WHERE h.id = ?1")
	void updateHistogramFlags(int histId, boolean initialized, boolean needRecount);
}
