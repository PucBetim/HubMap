package br.com.pucminas.hubmap.application.service;

import static br.com.pucminas.hubmap.utils.LoggerUtils.getLoggerFromClass;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
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
import br.com.pucminas.hubmap.domain.map.BlockRepository;
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

	private BlockRepository blockRepository;

	public HistogramService(PythonService pythonService, HistogramItemRepository histogramItemRepository,
			HistogramRepository histogramRepository, VocabularyService vocabularyService,
			NGramRepository nGramRepository, ParameterRepository parameterRepository, BlockRepository blockRepository) {
		this.pythonService = pythonService;
		this.histogramItemRepository = histogramItemRepository;
		this.histogramRepository = histogramRepository;
		this.vocabularyService = vocabularyService;
		this.nGramRepository = nGramRepository;
		this.parameterRepository = parameterRepository;
		this.blockRepository = blockRepository;
	}

	public Histogram generateSearchHistogram(Search search) {
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

		return hist;
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

		status = recalculate(bOW, post.getHistogram(), isEdit, false);

		Histogram hist = post.getHistogram();
		hist.setInitialized(true);

		if (status != StatusRetorno.ALREADY_IN_VOCABULARY) {
			hist.setNeedRecount(true);
		} else {
			hist.setNeedRecount(false);
		}

		Histogram dbHistogram = histogramRepository.save(hist);
		calculateTfIdf();

		CompletableFuture.completedFuture(dbHistogram);
	}

	@Transactional
	@Scheduled(initialDelay = 30 * 1000, fixedDelay = 20 * 60000)
	protected void refreshHistograms() {
		Parameter newWords = parameterRepository.findByTableName(ParametersTableContants.NEW_WORDS_IN_VOCABULARY)
				.get(0);
		boolean hasNewWords = Boolean.valueOf(newWords.getValueRegistry());

		if (!hasNewWords) {
			getLoggerFromClass(getClass()).debug("Histograms is already up-to-date");
			return;
		}

		Pageable pageable = PageableUtils.getPageableFromParameters(0, 10);
		Page<Histogram> histograms = histogramRepository.findByInitilized(true, pageable);

		while (true) {
			for (Histogram histogram : histograms) {
				if (histogram.getNeedRecount()) {

					Block root = blockRepository.findByPost(histogram.getId());

					String sentence = getWordsInBlocks(root, null);

					List<String> bOW;

					try {
						bOW = pythonService.getBagOfWords(sentence);
					} catch (IOException e) {
						throw new InvalidPropertyException(PythonService.class, "hubmap.scripts.python.path",
								"Não foi possível encontrar o caminho informado");
					}

					StatusRetorno status = recalculate(bOW, histogram, true, true);

					if (status != StatusRetorno.ALREADY_IN_VOCABULARY) {
						histogram.setNeedRecount(true);
					} else {
						histogram.setNeedRecount(false);
					}

					getLoggerFromClass(getClass()).debug("Recount all items of histogram " + histogram.getId());
				}

				histogramRepository.save(histogram);
			}

			if (histograms.hasNext()) {
				histograms = histogramRepository.findAll(pageable.next());
			} else {
				break;
			}
		}

		newWords.setValueRegistry("false");
		parameterRepository.save(newWords);

		calculateTfIdf();
		getLoggerFromClass(getClass()).info("Histograms updated successfuly");
	}

	private Histogram calculateTfIdf(Histogram histogram, long totalElements) {
		histogram.getHistogram().forEach(item -> {
			double tf = calculateTf(item.getCount());
			double idf = calculateIdf(item.getKey().getId(), totalElements);
			item.setTfidf(tf * idf);
		});

		return histogram;
	}

	private void calculateTfIdf() {

		Pageable pageable = PageableUtils.getPageableFromParameters(0, 10);
		Page<Histogram> histograms = histogramRepository.findByInitilized(true, pageable);
		final long totalElements = histograms.getTotalElements();

		while (true) {
			histograms.stream().parallel().forEach(hist -> {
				hist.getHistogram().forEach(item -> {
					double tf = calculateTf(item.getCount());
					double idf = calculateIdf(item.getKey().getId(), totalElements);
					item.setTfidf(tf * idf);
				});
				histogramRepository.save(hist);
			});

			if (histograms.hasNext()) {
				histograms = histogramRepository.findAll(pageable.next());
			} else {
				break;
			}
		}
	}

	private double calculateTf(int count) {
		return 1 + Math.log(count);
	}

	private double calculateIdf(long nGramId, long totalElements) {
		int counter = 0;

		counter = histogramItemRepository.countHistogramsOfNgramId(nGramId);

		counter = counter == 0 ? 1 : counter;

		return Math.log(1.0 * totalElements / counter);
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

				if (hist.isInHistogram(nGram) >= 0) {
					continue;
				}
				counter = hist.countWords(bagOfWords, nGram.getGram());

				if (counter > 0) {
					item = buildHistogramItem(hist, counter, nGram);
					item.setId(i);

					hist.addItem(item);
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

	private StatusRetorno recalculate(List<String> bagOfWords, Histogram hist, boolean isEdit, boolean isSchedule) {

		StatusRetorno statusReturn = StatusRetorno.ALREADY_IN_VOCABULARY;

		for (String word : bagOfWords) {
			StatusRetorno status = vocabularyService.addGram(word);

			if (status != StatusRetorno.ALREADY_IN_VOCABULARY) {
				statusReturn = status;
			}
		}

		HistogramItem item;
		int counter;
		Set<String> words = new HashSet<>(bagOfWords);

		if (isEdit && !isSchedule) {
			histogramItemRepository.updateAnalyzed(hist.getId(), false);
		}

		for (String word : words) {
			Optional<NGram> nGramOpt = nGramRepository.findByGramAndNewVocabulary(word, null);

			if (nGramOpt.isPresent()) {
				NGram nGram = nGramOpt.orElseThrow();
				counter = hist.countWords(bagOfWords, nGram.getGram());

				if (counter > 0) {
					if (isEdit && hist.isInHistogram(nGram) >= 0) {
						item = histogramItemRepository.findByKeyAndOwner(nGram, hist).orElseThrow();
						hist.removeItem(item);
						item.setCount(counter);
						item.setAnalyzed(true);
					} else {
						item = buildHistogramItem(hist, counter, nGram);
					}

					item = histogramItemRepository.save(item);
					hist.addItem(item);
				}
			}
		}
		List<HistogramItem> oldItems = null;

		if (isEdit) {
			oldItems = histogramItemRepository.findByOwnerAndAnalyzed(hist, false);
			hist.removeAllItems(oldItems);
			histogramItemRepository.deleteAll(oldItems);
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
