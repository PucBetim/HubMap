package br.com.pucminas.hubmap.domain.comment;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends PagingAndSortingRepository<Comment, Integer>{
	
	@Query("SELECT c FROM Comment c WHERE c.post.id = ?1 AND c.post.author.email = ?#{principal}")
	Page<Comment> findByPost(Integer postId, Pageable pageable);
	
	@Query("SELECT c FROM Comment c WHERE c.repliedTo.id = ?1")
	List<Comment> findByRepliedTo(@Param("rootId") Integer rootId);
	
	@Query("SELECT c FROM Comment c WHERE c.post.id = ?1 AND c.post.isPrivate = false")
	Page<Comment> findByPostIfPublic(Integer postId, Pageable pageable);
	
	@Query("SELECT c FROM Comment c WHERE c.id = ?1 AND c.post.isPrivate = false")
	Optional<Comment> findByIdIfPostPublic(Integer id);
}
