package br.com.pucminas.hubmap.domain.post;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Entity
@Getter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class NGram implements Serializable {
	
	@EqualsAndHashCode.Include
	@EmbeddedId
	private NGramPK id;
	
	private String gram;

	public NGram(String gram) {
		this.gram = gram;
	}

	public void setGram(String gram) {
		this.gram = gram;
	}
}
