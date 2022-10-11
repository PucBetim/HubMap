package br.com.pucminas.hubmap.domain.post;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends PagingAndSortingRepository<Post, Integer> {
	
	@Query("SELECT p FROM Post p WHERE p.author.email = ?#{principal}")
	Page<Post> findAllFromAuthor(Pageable pageable);
	
	@Query("SELECT p FROM Post p WHERE p.isPrivate = false")
	Page<Post> findAllPublic(Pageable pageable);
	
	@Query("SELECT p FROM Post p WHERE p.id = ?1 AND p.author.email = ?#{principal}")
	Optional<Post> findByIdFromLoggedAuthor(Integer id);
	
	@Query("SELECT p FROM Post p WHERE p.id = ?1 AND p.isPrivate = false")
	Optional<Post> findByIdIfPublic(Integer id);
	
	@Query("SELECT p FROM Post p WHERE p.id = ?1 AND (p.isPrivate = false OR p.author.email = ?#{principal})")
	Optional<Post> findByIdIfPublicOrFromAuthor(Integer id);
	
	@Query("SELECT p FROM Post p WHERE p.id = ?1 AND p.isPrivate = false")
	Optional<Post> findByIdWhereIsPrivateFalse(Integer id);
}

