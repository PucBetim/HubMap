package br.com.pucminas.hubmap.domain.comment;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.pucminas.hubmap.domain.post.Post;

@Repository
public interface CommentRepository extends CrudRepository<Comment, Integer>{
	
	@Query("SELECT c FROM Comment c WHERE c.post.id = ?#{#post.id} AND ?#{#post.author.email} = ?#{principal}")
	List<Comment> findByPost(@Param("post") Post post);
	
	@Query("SELECT c FROM Comment c WHERE c.repliedTo.id = ?1")
	List<Comment> findByRepliedTo(@Param("rootId") Integer rootId);
}
