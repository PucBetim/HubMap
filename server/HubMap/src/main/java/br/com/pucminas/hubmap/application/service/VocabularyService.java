package br.com.pucminas.hubmap.application.service;

import java.time.LocalDateTime;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.pucminas.hubmap.domain.indexing.NGram;
import br.com.pucminas.hubmap.domain.indexing.Vocabulary;
import br.com.pucminas.hubmap.domain.indexing.VocabularyRepository;

@Service
public class VocabularyService {
	
	private static final Long VOCAB_ID = 1L;
	
	@Autowired
	private VocabularyRepository vocabularyRepository;
	
	@Transactional
	public void addGram(String gram) {
		Vocabulary vocab = getVocabulary();
		
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
	
	public Vocabulary getVocabulary() {
		
		Optional<Vocabulary> dbVocab = vocabularyRepository.findById(VOCAB_ID);
		
		if(dbVocab.isPresent()) {
			return dbVocab.get();	
		} else {
			return null;
		}
	}
}
