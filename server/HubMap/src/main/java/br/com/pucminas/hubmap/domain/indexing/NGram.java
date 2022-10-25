package br.com.pucminas.hubmap.domain.indexing;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@SuppressWarnings("serial")
@Entity
@Table(indexes = {
		@Index(name = "Gram", columnList = "GRAM")})
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class NGram implements Serializable, Comparable<NGram> {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	private Long id;
	
	@NotBlank(message = "Uma n-gram não pode ser vazia.")
	@Column(length = 20, nullable = false)
	@EqualsAndHashCode.Include
	private String gram;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "VOCABULARY_ID", referencedColumnName = "id")
	private Vocabulary vocabulary;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "NEW_VOCABULARY_ID", referencedColumnName = "id")
	private Vocabulary newVocabulary;
	
	public NGram(String gram) {
		this.gram = gram;
	}

	@Override
	public int compareTo(NGram o) {		
		return gram.compareTo(o.getGram());
	}
	
	@Override
	public String toString() {
		return id + " - " + gram;
	}
}
