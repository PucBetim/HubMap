package br.com.pucminas.hubmap.domain.post;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;

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
	
	@NotBlank(message = "Uma n-gram não pode ser vazia.")
	@Column(length = 20, nullable = false)
	private String gram;

	public NGram(String gram) {
		this.gram = gram;
	}

	public void setGram(String gram) {
		this.gram = gram;
	}
}
