package br.com.pucminas.hubmap.application.service;

import static br.com.pucminas.hubmap.utils.LoggerUtils.getLoggerFromClass;

import java.util.Optional;
import java.util.Set;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import br.com.pucminas.hubmap.domain.indexing.HistogramRepository;
import br.com.pucminas.hubmap.domain.indexing.NGram;
import br.com.pucminas.hubmap.domain.indexing.NGramRepository;
import br.com.pucminas.hubmap.domain.indexing.Vocabulary;
import br.com.pucminas.hubmap.domain.indexing.VocabularyRepository;

@Service
public class VocabularyService {
		
	private static final Long VOCAB_ID = 1L;
	
	private VocabularyRepository vocabularyRepository;

	private NGramRepository nGramRepository;
	
	private HistogramRepository histogramRepository;
	
	private TransactionTemplate transactionTemplate;
	
	public VocabularyService(VocabularyRepository vocabularyRepository, NGramRepository nGramRepository,
			HistogramRepository histogramRepository, TransactionTemplate transactionTemplate) {
		this.vocabularyRepository = vocabularyRepository;
		this.nGramRepository = nGramRepository;
		this.histogramRepository = histogramRepository;
		this.transactionTemplate = transactionTemplate;
	}

	@Transactional
	public Vocabulary.StatusRetorno addGram(String gram) {
			
		Vocabulary vocab = getVocabulary();
		
		Vocabulary.StatusRetorno result = vocab.addNGrams(gram);
		vocabularyRepository.save(vocab);
		
		return result;
	}
	
	@Transactional
	@Scheduled(initialDelay = 10 * 1000, fixedDelay = 10 * 1000)
	protected void updateVocabulary() {
		
		Set<NGram> nGrams = nGramRepository.findByNewVocabulary(VOCAB_ID);
		Vocabulary vocab = getVocabulary();
		
		if(vocab.getHasNewWords()) {
			for (NGram nGram : nGrams) {
				if(!vocab.hasInVocabulary(nGram.getGram())) {
					nGram.setNewVocabulary(null);
					nGram.setVocabulary(vocab);
					vocab.getNgrams().add(nGram);
				} else {
					nGram.setNewVocabulary(null);
					nGramRepository.delete(nGram);
				}
			}
			
			vocab.updateWhenUpdated();
			transactionTemplate.execute(t -> {
				histogramRepository.updateUpToDateWithVocabulary(false);
				t.flush();
				return null;
			});
			
			
			getLoggerFromClass(getClass()).info("Vocabulary updated succefuly");
		} else {
			getLoggerFromClass(getClass()).info("Vocabulary is already up-to-date");
		}
		
		vocab.setHasNewWords(false);
		
		vocabularyRepository.save(vocab);
	}
	
	public Vocabulary getVocabulary() {
		
		Optional<Vocabulary> dbVocab = vocabularyRepository.findById(VOCAB_ID);
		
		if(dbVocab.isPresent()) {
			return dbVocab.get();	
		} else {
			return null;
		}
	}
}
