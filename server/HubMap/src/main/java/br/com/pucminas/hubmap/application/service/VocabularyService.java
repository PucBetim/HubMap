package br.com.pucminas.hubmap.application.service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.pucminas.hubmap.domain.indexing.NGram;
import br.com.pucminas.hubmap.domain.indexing.NGramRepository;
import br.com.pucminas.hubmap.domain.indexing.Vocabulary;
import br.com.pucminas.hubmap.domain.indexing.VocabularyRepository;

@Service
public class VocabularyService {
	
	private static final Long VOCAB_ID = 1L;
	
	@Autowired
	private VocabularyRepository vocabularyRepository;
	
	@Autowired
	private NGramRepository nGramRepository;
	
	@Transactional
	public void addGram(String gram) {
		Vocabulary vocab = getVocabulary(true);
		
		if(!isInVocabulary(gram, vocab)) {
			NGram item = new NGram(gram);
			item.setVocabulary(vocab);
			
			vocab.setHasNewWords(false);
			vocab.setWhenUpdated(LocalDateTime.now());
			vocab.getNgrams().add(item);
			
			vocabularyRepository.save(vocab);
		}		
	}
	
	private boolean isInVocabulary(String gram, Vocabulary vocab) {

		for (NGram nGram : vocab.getNgrams()) {
			if(nGram.getGram().equalsIgnoreCase(gram)) {
				return true;
			}
		}
		
		return false;
	}
	
	private Vocabulary getVocabulary(boolean isIntern) {
		
		Optional<Vocabulary> dbVocab = vocabularyRepository.findById(VOCAB_ID);
		
		if(dbVocab.isPresent() && isIntern) {
			return dbVocab.get();	
		} else {
			return null;
		}
	}
	
	public Vocabulary getVocabulary() {
				
		Optional<Vocabulary> dbVocab = vocabularyRepository.findById(VOCAB_ID);
		
		if(dbVocab.isPresent()) {
			Vocabulary vocabToReturn = dbVocab.get();
			
			Set<NGram> nGrams = nGramRepository.findByVocabularyOrderById(vocabToReturn);
			
			vocabToReturn.setNgrams(nGrams);
			return vocabToReturn;
		} else {
			return null;
		}
	}
}
