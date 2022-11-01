package br.com.pucminas.hubmap.domain.indexing;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;

import br.com.pucminas.hubmap.domain.post.Post;
import lombok.AccessLevel;
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

	private static final HistogramItemComparator COMPARATOR = HistogramItemComparator.getInstance();

	@Id
	@EqualsAndHashCode.Include
	private Integer id;

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "POST_ID")
	private Post post;

	// TODO Rollback this for Set and change EAGER for LAZY. Fix problems with
	// related methods
	@OneToMany(mappedBy = "owner", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@ToString.Exclude
	@OrderBy("key.id ASC")
	private List<HistogramItem> histogram = new ArrayList<>();

	private Boolean initialized;

	private Boolean needRecount;

	public Histogram(Post post) {
		this.post = post;
		initialized = false;
		id = post.getId();
	}

	public double getTfIdfFromHistogram(Long nGramId) {

		NGram nGram = new NGram();
		nGram.setId(nGramId);
		HistogramItem item = new HistogramItem();
		item.setKey(nGram);
		
		int index = Collections.binarySearch(histogram, item, COMPARATOR);

		return index >= 0 ? histogram.get(index).getTfidf() : 0;
	}

	public int isInHistogram(NGram ngram) {

		HistogramItem item = new HistogramItem();
		item.setKey(ngram);

		int index = Collections.binarySearch(histogram, item, COMPARATOR);

		return index;
	}

	public int countWords(List<String> bagOfWords, String term) {

		return Collections.frequency(bagOfWords, term);
	}

	public boolean addItem(HistogramItem item) {

		int itemIndex = isInHistogram(item.getKey());
		boolean bool;

		if (itemIndex < 0) {
			bool = histogram.add(item);
			Collections.sort(histogram, COMPARATOR);
		} else {
			bool = false;
			histogram.set(itemIndex, item);
		}

		return bool;
	}

	public boolean removeItem(HistogramItem item) {
		boolean bool = histogram.remove(item);
		Collections.sort(histogram, COMPARATOR);
		return bool;
	}

	public boolean removeAllItems(Collection<?> items) {
		boolean bool = histogram.removeAll(items);
		Collections.sort(histogram, COMPARATOR);
		return bool;
	}

	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	private static class HistogramItemComparator implements Comparator<HistogramItem> {

		private static HistogramItemComparator comparator;

		@Override
		public int compare(HistogramItem o1, HistogramItem o2) {
			return o1.getKey().getId().compareTo(o2.getKey().getId());
		}

		public static HistogramItemComparator getInstance() {
			if (comparator == null) {
				comparator = new HistogramItemComparator();
			}

			return comparator;
		}
	}
}
