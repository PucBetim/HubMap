package br.com.pucminas.hubmap.domain.indexing;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@SuppressWarnings("serial")
@Entity
@Table(name = "VOCABULARY")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Vocabulary implements Serializable {

	public enum StatusRetorno {
		ALREADY_IN_VOCABULARY, NOT_IN_VOCABULARY, NOT_IN_NEW_VOCABULARY
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	private Long id;

	@Column(name = "WHEN_UPDATED")
	private LocalDateTime whenUpdated;

	@Column(name = "HAS_NEW_WORDS")
	private Boolean hasNewWords;

	@OneToMany(mappedBy = "vocabulary", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@ToString.Exclude
	private Set<NGram> ngrams = new HashSet<>();

	@OneToMany(mappedBy = "newVocabulary", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@ToString.Exclude
	private Set<NGram> newNGrams = new HashSet<>();

	public void updateWhenUpdated() {
		whenUpdated = LocalDateTime.now();
	}

	public int getOficialSize() {
		return ngrams.size();
	}
}
