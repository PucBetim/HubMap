package br.com.pucminas.hubmap.application.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.pucminas.hubmap.domain.indexing.Histogram;
import br.com.pucminas.hubmap.domain.indexing.HistogramRepository;
import br.com.pucminas.hubmap.domain.indexing.NGram;
import br.com.pucminas.hubmap.domain.indexing.Vocabulary;
import br.com.pucminas.hubmap.domain.indexing.search.Search;
import br.com.pucminas.hubmap.domain.post.Post;
import br.com.pucminas.hubmap.domain.post.PostRepository;
import br.com.pucminas.hubmap.utils.PageableUtils;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Service
public class SearchService {

	@Autowired
	private HistogramRepository histogramRepository;
	
	@Autowired
	private HistogramService histogramService;

	@Autowired
	private VocabularyService vocabularyService;
	
	@Autowired
	private PostRepository postRepository;
	
	@Transactional(readOnly = true)
	public List<Post> search(Search search) throws InterruptedException, ExecutionException {

		PostRepository p2 = postRepository;
		
		Pageable pageable = PageableUtils.getPageableFromParameters(0, 10);

		Page<Histogram> histograms = histogramRepository.findByInitilized(true, pageable);
		List<Post> posts = new ArrayList<>();
		SortedSet<HistogramSearch> histogramsSimilarity = new TreeSet<>(new Comparator<>() {

			@Override
			public int compare(HistogramSearch o1, HistogramSearch o2) {
				return -1 * o1.getSimilarity().compareTo(o2.getSimilarity());
			}

		});

		search = histogramService.generateSearchHistogram(search);
		
		List<Histogram> hists = histograms.getContent();
		
		while (true) {

			for (Histogram histogram : hists) {
				double similarity = compareHistograms(search.getHistogram(), histogram);
				HistogramSearch histSearch = new HistogramSearch();
				histSearch.setPostId(histogram.getPost().getId());
				histSearch.setSimilarity(similarity);
				histogramsSimilarity.add(histSearch);
			}

			if (histograms.hasNext()) {
				histograms = histogramRepository.findAll(pageable.next());
			} else {
				break;
			}
		}

		
		
		if (!histogramsSimilarity.isEmpty()) {
			
			for (HistogramSearch histSearch : histogramsSimilarity) {
				
				Post post = p2.findById(histSearch.getPostId()).orElseThrow();
				
				posts.add(post);
			}
		}

		return posts;
	}

	private double compareHistograms(Histogram hist1, Histogram hist2) throws InterruptedException, ExecutionException {

		CompletableFuture<Double[]> dissimilarityCoefficient = calculateDissimilarityCoefficient(hist1, hist2);
		
		return dissimilarityCoefficient.thenApply(coef ->  {
			int n = hist1.getHistogram().size();
			double dc = coef[0];
			double s = coef[1];
			double t = coef[2];

			return 0.5 * ((s / n) + ((t * s) / (t * s + dc)));

		}).get();

	}

	private CompletableFuture<Double[]> calculateDissimilarityCoefficient(Histogram hist1, Histogram hist2) {

		CompletableFuture<Double[]> dividend = calculateDissimilarityCoefficientDividendAndT(hist1, hist2);
		CompletableFuture<Double[]> divisor = calculateDissimilarityCoefficientDivisorAndS(hist1, hist2);

		return dividend.thenCompose(fd1Value -> divisor
				.thenApply(fd2Value -> new Double[] { fd1Value[0] / fd2Value[0], fd2Value[1], fd1Value[1] }));
	}

	@Async
	private CompletableFuture<Double[]> calculateDissimilarityCoefficientDivisorAndS(Histogram hist1, Histogram hist2) {
		Vocabulary vocab = vocabularyService.getVocabulary();

		double divisor = 0.0;
		double s = 0.0;

		for (NGram nGram : vocab.getNgrams()) {
			double tfIdfH1 = hist1.getItemFromHistogram(nGram).getTfidf();
			double tfIdfH2 = hist2.getItemFromHistogram(nGram).getTfidf();

			divisor += Math.abs(tfIdfH1 + tfIdfH2);

			if (tfIdfH1 != 0 && tfIdfH2 != 0) {
				s++;
			}
		}

		return new AsyncResult<>(new Double[] { divisor * 0.5, s }).completable();
	}

	@Async
	private CompletableFuture<Double[]> calculateDissimilarityCoefficientDividendAndT(Histogram hist1, Histogram hist2) {
		Vocabulary vocab = vocabularyService.getVocabulary();
		double dividend = 0.0;
		double t = 0.0;

		for (NGram nGram : vocab.getNgrams()) {
			double tfIdfH1 = hist1.getItemFromHistogram(nGram).getTfidf();
			double tfIdfH2 = hist2.getItemFromHistogram(nGram).getTfidf();
			
			dividend += Math.abs(tfIdfH1 - tfIdfH2);

			if (tfIdfH1 != 0 || tfIdfH2 != 0) {
				t++;
			}
		}

		return new AsyncResult<>(new Double[] { dividend, t }).completable();
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