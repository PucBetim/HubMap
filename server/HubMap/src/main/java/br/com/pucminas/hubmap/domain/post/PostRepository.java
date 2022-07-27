package br.com.pucminas.hubmap.domain.post;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends PagingAndSortingRepository<Post, Integer> {
	
	@Override
	@Query("SELECT p FROM Post p WHERE p.author.email = ?#{principal}")
	Iterable<Post> findAll(Sort sort);
	
	@Override
	@Query("SELECT p FROM Post p WHERE p.author.email = ?#{principal}")
	Page<Post> findAll(Pageable pageable);
	
	@Override
	@Query("SELECT p FROM Post p WHERE p.id = ?1 AND p.author.email = ?#{principal}")
	Optional<Post> findById(Integer id);
}

