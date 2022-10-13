package br.com.pucminas.hubmap.application.service;

import static br.com.pucminas.hubmap.utils.LoggerUtils.getLoggerFromClass;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import javax.transaction.Transactional;

import org.springframework.beans.InvalidPropertyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import br.com.pucminas.hubmap.application.service.python.PythonService;
import br.com.pucminas.hubmap.domain.extras.Parameter;
import br.com.pucminas.hubmap.domain.extras.ParameterRepository;
import br.com.pucminas.hubmap.domain.extras.ParametersTableContants;
import br.com.pucminas.hubmap.domain.indexing.Histogram;
import br.com.pucminas.hubmap.domain.indexing.HistogramItem;
import br.com.pucminas.hubmap.domain.indexing.HistogramItemRepository;
import br.com.pucminas.hubmap.domain.indexing.HistogramRepository;
import br.com.pucminas.hubmap.domain.indexing.NGram;
import br.com.pucminas.hubmap.domain.indexing.NGramRepository;
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

	private NGramRepository nGramRepository;
	
	private ParameterRepository parameterRepository;

	public HistogramService(PythonService pythonService, HistogramItemRepository histogramItemRepository,
			HistogramRepository histogramRepository, VocabularyService vocabularyService,
			NGramRepository nGramRepository, ParameterRepository parameterRepository) {
		this.pythonService = pythonService;
		this.histogramItemRepository = histogramItemRepository;
		this.histogramRepository = histogramRepository;
		this.vocabularyService = vocabularyService;
		this.nGramRepository = nGramRepository;
		this.parameterRepository = parameterRepository;
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
		long totalElements = histogramRepository.findByInitilized(true, pageable).getTotalElements();

		Histogram hist = initializeSearchHistogram(bOW);
		hist.setInitialized(true);
		hist = calculateTfIdf(hist, totalElements);
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

		status = recalculate(bOW, post.getHistogram(), isEdit);

		Histogram hist = post.getHistogram();
		hist.setInitialized(true);

		if (status != StatusRetorno.ALREADY_IN_VOCABULARY) {
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
		Parameter newWords = parameterRepository.findByTableName(ParametersTableContants.NEW_WORDS_IN_VOCABULARY).get(0);
		boolean hasNewWords = Boolean.valueOf(newWords.getValueRegistry());
		
		if(!hasNewWords) {
			return;
		}
		
		Pageable pageable = PageableUtils.getPageableFromParameters(0, 10);
		Page<Histogram> histograms = histogramRepository.findByInitilized(true, pageable);
		
		while (true) {
			for (Histogram histogram : histograms) {
				if (histogram.getNeedRecount()) {

					Block root = histogram.getPost().getMapRoot();

					String sentence = getWordsInBlocks(root, null);

					List<String> bOW;

					try {
						bOW = pythonService.getBagOfWords(sentence);
					} catch (IOException e) {
						throw new InvalidPropertyException(PythonService.class, "hubmap.scripts.python.path",
								"Não foi possível encontrar o caminho informado");
					}

					StatusRetorno status = recalculate(bOW, histogram, true);

					if (status != StatusRetorno.ALREADY_IN_VOCABULARY) {
						histogram.setNeedRecount(true);
					} else {
						histogram.setNeedRecount(false);
					}

					getLoggerFromClass(getClass()).info("Recount all items of histogram " + histogram.getId());
				}

				Histogram dbHistogram = histogramRepository.save(histogram);
				dbHistogram = calculateTfIdf(dbHistogram,
						histogramRepository.findByInitilized(true, pageable.first()).getTotalElements());
				histogramRepository.save(dbHistogram);
			}

			if (histograms.hasNext()) {
				histograms = histogramRepository.findAll(pageable.next());
			} else {
				break;
			}
		}

		newWords.setValueRegistry("false");
		parameterRepository.save(newWords);
		
		getLoggerFromClass(getClass()).info("Histograms updated successfuly");
	}

	public Histogram calculateTfIdf(Histogram histogram, long totalElements) {
		double tf;
		double idf;
		int vocabSize = vocabularyService.getVocabulary().getOficialSize();

		for (HistogramItem item : histogram.getHistogram()) {
			tf = calculateTf(vocabSize, item.getCount());
			idf = calculateIdf(item.getKey().getId(), totalElements);
			item.setTfidf(tf * idf);
		}

		return histogram;
	}

	private double calculateTf(int vocabSize, double count) {
		return count / vocabSize;
	}

	private double calculateIdf(long nGramId, long totalElements) {
		double counter = 0.0;

		counter = histogramItemRepository.countHistogramsOfNgramId(nGramId);

		counter = counter == 0.0 ? 1.0 : counter;

		return 1.0 + Math.log(totalElements / counter);
	}

	private Histogram initializeSearchHistogram(List<String> bagOfWords) {

		Histogram hist = new Histogram();
		HistogramItem item;
		int counter;
		long i = 1L;

		for (String word : bagOfWords) {
			Optional<NGram> nGramOpt = nGramRepository.findByGramAndNewVocabulary(word, null);

			if (nGramOpt.isPresent()) {
				NGram nGram = nGramOpt.orElseThrow();
				
				if(hist.isInHistogram(nGram)) {
					continue;
				}
				counter = hist.countWords(bagOfWords, nGram.getGram());

				if (counter > 0) {
					item = buildHistogramItem(hist, counter, nGram);
					item.setId(i);
					
					hist.getHistogram().add(item);
					i++;
				}
			}
		}

		hist.setInitialized(true);
		return hist;
	}

	private HistogramItem buildHistogramItem(Histogram hist, int counter, NGram nGram) {
		HistogramItem item = new HistogramItem();
		item.setOwner(hist);
		item.setKey(nGram);
		item.setCount(counter);
		return item;
	}

	private StatusRetorno recalculate(List<String> bagOfWords, Histogram hist, boolean isEdit) {

		StatusRetorno statusReturn = StatusRetorno.ALREADY_IN_VOCABULARY;

		for (String word : bagOfWords) {
			StatusRetorno status = vocabularyService.addGram(word);

			if (status != StatusRetorno.ALREADY_IN_VOCABULARY) {
				statusReturn = status;
			}
		}

		HistogramItem item;
		int counter;

		for (String word : bagOfWords) {
			Optional<NGram> nGramOpt = nGramRepository.findByGramAndNewVocabulary(word, null);

			if (nGramOpt.isPresent()) {
				NGram nGram = nGramOpt.orElseThrow();
				counter = hist.countWords(bagOfWords, nGram.getGram());

				if (counter > 0) {
					if (isEdit && hist.isInHistogram(nGram)) {
						item = histogramItemRepository.findByKeyAndOwner(nGram, hist);
						item.setCount(counter);
					} else {
						item = buildHistogramItem(hist, counter, nGram);

						HistogramItem dbItem = histogramItemRepository.save(item);
						hist.getHistogram().add(dbItem);
					}
				}
			}
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
