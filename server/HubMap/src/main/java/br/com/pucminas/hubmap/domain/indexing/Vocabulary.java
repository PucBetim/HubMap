package br.com.pucminas.hubmap.domain.indexing;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

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
public class Vocabulary implements Serializable{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	private Long id;
	
	@Column(name = "WHEN_UPDATED")
	private LocalDateTime whenUpdated;
	
	@Column(name = "HAS_NEW_WORDS")
	private boolean hasNewWords;
	
	@OneToMany(mappedBy = "vocabulary", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@ToString.Exclude
	private Set<NGram> ngrams = new TreeSet<>(new Comparator<NGram>() {

		@Override
		public int compare(NGram o1, NGram o2) {
			return o1.getId().compareTo(o2.getId());
		}
	});
	
}
