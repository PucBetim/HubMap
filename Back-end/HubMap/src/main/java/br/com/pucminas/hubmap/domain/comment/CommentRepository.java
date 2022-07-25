package br.com.pucminas.hubmap.domain.comment;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends CrudRepository<Comment, Integer>{
	
	@Query("SELECT c FROM Comment c WHERE c.post.id = ?1")
	List<Comment> findByPost(Integer postId);
}
