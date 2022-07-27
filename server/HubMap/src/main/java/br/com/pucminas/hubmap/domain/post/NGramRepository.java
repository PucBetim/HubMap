package br.com.pucminas.hubmap.domain.post;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface NGramRepository extends JpaRepository<NGram, NGramPK>{
	
	@Query("SELECT ng FROM NGram ng WHERE ng.id.post.id = ?1")
	List<NGram> findByPost(Integer post);
}
