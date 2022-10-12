package br.com.pucminas.hubmap.domain.indexing;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import br.com.pucminas.hubmap.domain.post.Post;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@SuppressWarnings("serial")
@Entity
@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Histogram implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	private Long id;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "POST_ID")
	private Post post;

	@OneToMany(mappedBy = "owner", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@ToString.Exclude
	private Set<HistogramItem> histogram = new LinkedHashSet<>();

	private Boolean initialized;

	private Boolean needRecount;
	
	private Boolean upToDateWithVocabulary;

	public Histogram(Post post) {
		this.post = post;
		initialized = false;
	}

	public HistogramItem getItemFromHistogram(NGram ngram) {
		for (HistogramItem item : histogram) {
			if (item.getKey().equals(ngram)) {
				return item;
			}
		}

		return null;
	}

	public boolean isInHistogram(String term) {
		for (HistogramItem item : histogram) {
			if (item.getKey().getGram().equals(term)) {
				return true;
			}
		}

		return false;
	}

	public boolean isInHistogram(NGram nGram) {
		for (HistogramItem item : histogram) {
			if (item.getKey().equals(nGram)) {
				return true;
			}
		}

		return false;
	}

	public int countWords(List<String> bagOfWords, String term) {

		return Collections.frequency(bagOfWords, term);
	}
}
