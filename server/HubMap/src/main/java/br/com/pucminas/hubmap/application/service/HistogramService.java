package br.com.pucminas.hubmap.application.service;

import static br.com.pucminas.hubmap.utils.LoggerUtils.getLoggerFromClass;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import javax.transaction.Transactional;

import org.springframework.beans.InvalidPropertyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import br.com.pucminas.hubmap.application.service.python.PythonService;
import br.com.pucminas.hubmap.domain.indexing.Histogram;
import br.com.pucminas.hubmap.domain.indexing.HistogramItem;
import br.com.pucminas.hubmap.domain.indexing.HistogramItemRepository;
import br.com.pucminas.hubmap.domain.indexing.HistogramRepository;
import br.com.pucminas.hubmap.domain.indexing.NGram;
import br.com.pucminas.hubmap.domain.indexing.Vocabulary;
import br.com.pucminas.hubmap.domain.indexing.Vocabulary.StatusRetorno;
import br.com.pucminas.hubmap.domain.indexing.search.Search;
import br.com.pucminas.hubmap.domain.map.Block;
import br.com.pucminas.hubmap.domain.post.Post;
import br.com.pucminas.hubmap.utils.PageableUtils;

@Service
public class HistogramService {

	private PythonService pythonService;

	private HistogramItemRepository histogramItemRepository;

	private HistogramRepository histogramRepository;

	private VocabularyService vocabularyService;
	
	public HistogramService(PythonService pythonService, HistogramItemRepository histogramItemRepository,
			HistogramRepository histogramRepository, VocabularyService vocabularyService) {
		this.pythonService = pythonService;
		this.histogramItemRepository = histogramItemRepository;
		this.histogramRepository = histogramRepository;
		this.vocabularyService = vocabularyService;
	}

	public Search generateSearchHistogram(Search search) {
		String sentence = search.getSearch();
		
		List<String> bOW;

		try {
			bOW = pythonService.getBagOfWords(sentence);
		} catch (IOException e) {
			throw new InvalidPropertyException(PythonService.class, "hubmap.scripts.python.path",
					"Não foi possível encontrar o caminho informado");
		}

		Pageable pageable = PageableUtils.getPageableFromParameters(0, 10);
		Page<Histogram> histograms = histogramRepository.findByInitilized(true, pageable);
		
		Histogram hist = initialize(bOW);
		hist.setInitialized(true);
		hist = calculateTfIdf(hist, histograms);
		search.setHistogram(hist);
		
		return search;
	}
	
	@Transactional
	@Async
	public void generateHistogram(Block root, Post post, boolean isEdit) {

		String sentence = getWordsInBlocks(root, null);

		List<String> bOW;

		try {
			bOW = pythonService.getBagOfWords(sentence);
		} catch (IOException e) {
			throw new InvalidPropertyException(PythonService.class, "hubmap.scripts.python.path",
					"Não foi possível encontrar o caminho informado");
		}

		StatusRetorno status;
		
		if (isEdit) {
			status = recalculate(bOW, post.getHistogram());
		} else {
			status =initialize(bOW, post.getHistogram());
		}
		
		Histogram hist = post.getHistogram();
		hist.setInitialized(true);
		

		if(status != StatusRetorno.ALREADY_IN_VOCABULARY) {
			hist.setNeedRecount(true);
		} else {
			hist.setNeedRecount(false);
		}
		
		Histogram dbHistogram = histogramRepository.save(hist);
		
		CompletableFuture.completedFuture(dbHistogram);
	}

	@Transactional
	@Scheduled(initialDelay = 10 * 1000, fixedDelay = 10 * 1500)
	protected void refreshHistograms() {
		
		Pageable pageable = PageableUtils.getPageableFromParameters(0, 10);
		Page<Histogram> histograms = histogramRepository.findByInitilized(true, pageable);
		
		while (true) {
			for (Histogram histogram : histograms) {
				if(histogram.getNeedRecount()) {
					
					Block root = histogram.getPost().getMapRoot();
					
					String sentence = getWordsInBlocks(root, null);

					List<String> bOW;

					try {
						bOW = pythonService.getBagOfWords(sentence);
					} catch (IOException e) {
						throw new InvalidPropertyException(PythonService.class, "hubmap.scripts.python.path",
								"Não foi possível encontrar o caminho informado");
					}
					
					StatusRetorno status = recalculate(bOW, histogram);
					
					if(status != StatusRetorno.ALREADY_IN_VOCABULARY) {
						histogram.setNeedRecount(true);
					} else {
						histogram.setNeedRecount(false);
					}
					
					getLoggerFromClass(getClass()).info("Recount all items of histogram " + histogram.getId());
				} 
				
				if (!histogram.getUpToDateWithVocabulary()) {
					addRecentlyWords(histogram);
					histogram.setUpToDateWithVocabulary(true);
				}
				
				Histogram dbHistogram = histogramRepository.save(histogram);
				calculateTfIdf(dbHistogram, histogramRepository.findByInitilized(true, pageable.first()));
			}

			if (histograms.hasNext()) {
				histograms = histogramRepository.findAll(pageable.next());
			} else {
				break;
			}
		}
		
		getLoggerFromClass(getClass()).info("Histograms updated successfuly");
	}
	
	public Histogram calculateTfIdf(Histogram histogram, Page<Histogram> histograms) {
		double tf;
		double idf;

		for (HistogramItem item : histogram.getHistogram()) {
			tf = calculateTf(histogram, item.getCount());
			idf = calculateIdf(item.getKey().getGram(), histograms);
			item.setTfidf(tf * idf);
		}
		
		return histogram;
	}

	private double calculateTf(Histogram histogram, double count) {
		return count / histogram.getHistogram().size();
	}

	private double calculateIdf(String term, Page<Histogram> histograms) {
		double counter = 0.0;
		boolean appear;
		
		while (true) {
			for (Histogram hist : histograms) {
				//counter = 0.0;
				appear = false;

				for (HistogramItem item : hist.getHistogram()) {
					if (item.getKey().getGram().equals(term)) {
						appear = true;
						break;
					}
				}

				if (appear) {
					counter++;
				}
			}

			if (histograms.hasNext()) {
				histograms = histogramRepository.findAll(histograms.nextPageable());
			} else {
				break;
			}
		}

		counter = counter == 0.0 ? 1.0 : counter;

		return 1.0 + Math.log(histograms.getTotalElements() / counter);
	}
	
	private void addRecentlyWords(Histogram hist) {
		Vocabulary vocab = vocabularyService.getVocabulary();
		HistogramItem item;

		for (NGram nGram : vocab.getNgrams()) {
			if (!hist.isInHistogram(nGram)) {
				item = new HistogramItem();
				item.setOwner(hist);
				item.setKey(nGram);
				item.setCount(0);

				HistogramItem dbItem = histogramItemRepository.save(item);
				hist.getHistogram().add(dbItem);
			}
		}
	}

	private StatusRetorno recalculate(List<String> bagOfWords, Histogram hist) {
		
		StatusRetorno statusReturn = StatusRetorno.ALREADY_IN_VOCABULARY;
		
		for (String word : bagOfWords) {
			StatusRetorno status = vocabularyService.addGram(word);
			
			if(status != StatusRetorno.ALREADY_IN_VOCABULARY) {
				statusReturn = status;
			}
		}

		Vocabulary vocab = vocabularyService.getVocabulary();
		HistogramItem item;
		int counter;

		for (NGram nGram : vocab.getNgrams()) {
			counter = hist.countWords(bagOfWords, nGram.getGram());

			if (hist.isInHistogram(nGram)) {
				item = histogramItemRepository.findByKeyAndOwner(nGram, hist);
				item.setCount(counter);
			} else {
				item = new HistogramItem();
				item.setOwner(hist);
				item.setKey(nGram);
				item.setCount(counter);

				HistogramItem dbItem = histogramItemRepository.save(item);
				hist.getHistogram().add(dbItem);
			}
		}
		
		hist.setUpToDateWithVocabulary(true);
		
		return statusReturn;
	}

	private Histogram initialize(List<String> bagOfWords) {
		
		Vocabulary vocab = vocabularyService.getVocabulary();
		Histogram hist = new Histogram();
		HistogramItem item;
		int counter;
		long i = 1L;
		
		for (NGram nGram : vocab.getNgrams()) {
			counter = hist.countWords(bagOfWords, nGram.getGram());

			item = new HistogramItem();
			item.setId(i);
			item.setOwner(hist);
			item.setKey(nGram);
			item.setCount(counter);

			hist.getHistogram().add(item);
			i++;
		}
		
		hist.setInitialized(true);
		return hist;
	}
	
	private StatusRetorno initialize(List<String> bagOfWords, Histogram hist) {

		StatusRetorno statusReturn = StatusRetorno.ALREADY_IN_VOCABULARY;
		
		for (String word : bagOfWords) {
			StatusRetorno status = vocabularyService.addGram(word);
			
			if(status != StatusRetorno.ALREADY_IN_VOCABULARY) {
				statusReturn = status;
			}
		}

		Vocabulary vocab = vocabularyService.getVocabulary();
		HistogramItem item;
		int counter;

		for (NGram nGram : vocab.getNgrams()) {
			counter = hist.countWords(bagOfWords, nGram.getGram());

			item = new HistogramItem();
			item.setOwner(hist);
			item.setKey(nGram);
			item.setCount(counter);

			HistogramItem dbItem = histogramItemRepository.save(item);

			hist.getHistogram().add(dbItem);
		}
		
		return statusReturn;
	}

	private String getWordsInBlocks(Block root, StringBuilder sb) {

		if (sb == null) {
			sb = new StringBuilder();
			sb.append(root.getContent() + " | ");
		}

		if (root.getBlocks().size() > 0) {
			for (Block child : root.getBlocks()) {
				sb.append(child.getContent() + " | ");
				getWordsInBlocks(child, sb);
			}
		}

		return sb.toString();
	}
}
