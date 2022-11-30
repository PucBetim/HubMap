package br.com.pucminas.hubmap.domain.map;

import java.util.Set;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BlockRepository extends CrudRepository<Block, Integer>{

	@Query("SELECT b FROM Block b WHERE b.post.id = ?1 AND b.isRoot = true")
	Block findByPost(@Param("post") Integer postId);
	
	@Query("SELECT b FROM Block b WHERE b.post.id = ?1 AND b.parent = null AND b.isRoot = false")
	Set<Block> findAllByPostWhereIsNotRoot(@Param("post") Integer postId);
	
	@Modifying(flushAutomatically = true, clearAutomatically = true)
	@Query("DELETE FROM Block b WHERE b.post.id = ?1 AND b.parent = null AND b.isRoot = false")
	void deleteAllByPostWhereIsNotRoot(@Param("post") Integer postId);
}
