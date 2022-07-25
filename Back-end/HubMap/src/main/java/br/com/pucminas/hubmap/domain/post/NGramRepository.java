package br.com.pucminas.hubmap.domain.post;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NGramRepository extends JpaRepository<NGram, NGramPK>{
}
