package br.com.pucminas.hubmap.application.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.pucminas.hubmap.domain.indexing.Histogram;
import br.com.pucminas.hubmap.domain.indexing.HistogramItem;
import br.com.pucminas.hubmap.domain.indexing.HistogramRepository;
import br.com.pucminas.hubmap.domain.indexing.NGram;
import br.com.pucminas.hubmap.domain.indexing.search.Search;
import br.com.pucminas.hubmap.infrastructure.web.RestResponseSearch;
import br.com.pucminas.hubmap.utils.PageableUtils;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Service
public class SearchService {

	private static final Integer PAGE_SIZE = 5;

	private HistogramRepository histogramRepository;

	private HistogramService histogramService;

	// private VocabularyService vocabularyService;

	public SearchService(HistogramRepository histogramRepository, HistogramService histogramService) {
		this.histogramRepository = histogramRepository;
		this.histogramService = histogramService;
	}

	@Transactional(readOnly = true)
	public RestResponseSearch search(Search search) throws InterruptedException, ExecutionException {

		Integer currentPage = 0;
		int resultPageSize = 5;

		if (search.getOldSearch() != null) {
			currentPage = search.getOldSearch().getPage();
			resultPageSize = search.getOldSearch().getSize();
		}

		Pageable pageable = PageableUtils.getPageableFromParameters(currentPage, PAGE_SIZE);
		List<Integer> posts = new ArrayList<>();
		List<HistogramSearch> histogramsSimilarity = new ArrayList<>();

		search = histogramService.generateSearchHistogram(search);

		Page<Histogram> histograms = histogramRepository.findByInitilized(true, pageable);

		while (true) {
			for (Histogram histogram : histograms) {
				double similarity = compareHistograms(search.getHistogram(), histogram);

				if (similarity > 0.0) {
					HistogramSearch histSearch = new HistogramSearch();
					histSearch.setPostId(histogram.getPost().getId());
					histSearch.setSimilarity(similarity);
					histogramsSimilarity.add(histSearch);
				}
			}

			if (histogramsSimilarity.size() < resultPageSize && histograms.hasNext()) {
				histograms = histogramRepository.findAll(pageable.next());
			} else {
				break;
			}
		}

		if (!histogramsSimilarity.isEmpty()) {
			Collections.sort(histogramsSimilarity, new Comparator<>() {
				@Override
				public int compare(HistogramSearch o1, HistogramSearch o2) {
					return -1 * o1.getSimilarity().compareTo(o2.getSimilarity());
				}
			});

			for (HistogramSearch histSearch : histogramsSimilarity) {
				posts.add(histSearch.getPostId());
			}
		}

		currentPage = !histograms.isLast() ? histograms.nextPageable().getPageNumber() : null;

		return RestResponseSearch.fromSearchResult(null, posts, resultPageSize, currentPage);
	}

	private double compareHistograms(Histogram hist1, Histogram hist2) throws InterruptedException, ExecutionException {

		Set<NGram> vocab = joinHistograms(hist1, hist2);
		CompletableFuture<Double[]> dissimilarityCoefficient = calculateDissimilarityCoefficient(hist1, hist2, vocab);

		return dissimilarityCoefficient.thenApply(coef -> {
			// TODO Check if I really can change the n value from size of vocabulary to
			// highest between histograms
			// int n = Math.max(hist1.getHistogram().size(), hist2.getHistogram().size());
			int n = vocab.size();
			double dc = coef[0];
			double s = coef[1];
			double t = coef[2];

			return 0.5 * ((s / n) + ((t * s) / (t * s + dc)));

		}).get();

	}

	private CompletableFuture<Double[]> calculateDissimilarityCoefficient(Histogram hist1, Histogram hist2,
			Set<NGram> vocab) {

		CompletableFuture<Double[]> dividend = calculateDissimilarityCoefficientDividendAndT(hist1, hist2, vocab);
		CompletableFuture<Double[]> divisor = calculateDissimilarityCoefficientDivisorAndS(hist1, hist2, vocab);

		return dividend.thenCompose(fd1Value -> divisor
				.thenApply(fd2Value -> new Double[] { fd1Value[0] / fd2Value[0], fd2Value[1], fd1Value[1] }));
	}

	@Async
	private CompletableFuture<Double[]> calculateDissimilarityCoefficientDivisorAndS(Histogram hist1, Histogram hist2,
			Set<NGram> vocab) {
		// Vocabulary vocab = vocabularyService.getVocabulary();

		double divisor = 0.0;
		double s = 0.0;

		for (NGram nGram : vocab) {
			HistogramItem item1 = hist1.getItemFromHistogram(nGram);
			HistogramItem item2 = hist2.getItemFromHistogram(nGram);
			double tfIdfH1 = item1 != null ? item1.getTfidf() : 0.0;
			double tfIdfH2 = item2 != null ? item2.getTfidf() : 0.0;

			divisor += tfIdfH1 + tfIdfH2;

			if (tfIdfH1 != 0.0 && tfIdfH2 != 0.0) {
				s++;
			}
		}

		return new AsyncResult<>(new Double[] { divisor * 0.5, s }).completable();
	}

	@Async
	private CompletableFuture<Double[]> calculateDissimilarityCoefficientDividendAndT(Histogram hist1, Histogram hist2,
			Set<NGram> vocab) {
		// Vocabulary vocab = vocabularyService.getVocabulary();

		double dividend = 0.0;
		double t = 0.0;

		for (NGram nGram : vocab) {
			HistogramItem item1 = hist1.getItemFromHistogram(nGram);
			HistogramItem item2 = hist2.getItemFromHistogram(nGram);
			double tfIdfH1 = item1 != null ? item1.getTfidf() : 0.0;
			double tfIdfH2 = item2 != null ? item2.getTfidf() : 0.0;

			dividend += Math.abs(tfIdfH1 - tfIdfH2);

			if (tfIdfH1 != 0.0 || tfIdfH2 != 0.0) {
				t++;
			}
		}

		return new AsyncResult<>(new Double[] { dividend, t }).completable();
	}

	private Set<NGram> joinHistograms(Histogram hist1, Histogram hist2) {
		Set<NGram> nGrams = new HashSet<>();

		hist2.getHistogram().forEach(i -> nGrams.add(i.getKey()));
		hist1.getHistogram().forEach(i -> nGrams.add(i.getKey()));

		return nGrams;
	}

	@Getter
	@Setter
	@EqualsAndHashCode(onlyExplicitlyIncluded = true)
	private class HistogramSearch {

		private Double similarity;

		@EqualsAndHashCode.Include
		private Integer postId;
	}
}
