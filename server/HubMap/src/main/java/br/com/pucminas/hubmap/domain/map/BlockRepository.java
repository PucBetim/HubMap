package br.com.pucminas.hubmap.domain.map;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.pucminas.hubmap.domain.post.Post;

@Repository
public interface BlockRepository extends CrudRepository<Block, Integer>{

	@Query("SELECT b FROM Block b WHERE b.post.id = ?#{#post.id}")
	List<Block> findByPost(@Param("post") Post post);
}
