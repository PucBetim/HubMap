package br.com.pucminas.hubmap.application.service;

import static br.com.pucminas.hubmap.utils.LoggerUtils.getLoggerFromClass;

import java.util.Optional;
import java.util.Set;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.pucminas.hubmap.domain.extras.Parameter;
import br.com.pucminas.hubmap.domain.extras.ParameterRepository;
import br.com.pucminas.hubmap.domain.extras.ParametersTableContants;
import br.com.pucminas.hubmap.domain.indexing.NGram;
import br.com.pucminas.hubmap.domain.indexing.NGramRepository;
import br.com.pucminas.hubmap.domain.indexing.Vocabulary;
import br.com.pucminas.hubmap.domain.indexing.Vocabulary.StatusRetorno;
import br.com.pucminas.hubmap.domain.indexing.VocabularyRepository;

@Service
public class VocabularyService {

	private static final Long VOCAB_ID = 1L;

	private VocabularyRepository vocabularyRepository;

	private NGramRepository nGramRepository;

	private ParameterRepository parameterRepository;

	public VocabularyService(VocabularyRepository vocabularyRepository, NGramRepository nGramRepository,
			ParameterRepository parameterRepository) {
		this.vocabularyRepository = vocabularyRepository;
		this.nGramRepository = nGramRepository;
		this.parameterRepository = parameterRepository;
	}

	@Transactional
	@Scheduled(initialDelay = 25 * 1000, fixedDelay = 15 * 60000)
	protected void updateVocabulary() {

		Set<NGram> nGrams = nGramRepository.findByNewVocabulary(VOCAB_ID);
		Vocabulary vocab = getVocabulary();

		if (vocab.getHasNewWords()) {
			for (NGram nGram : nGrams) {
				if (!hasInVocabulary(nGram.getGram())) {
					nGram.setNewVocabulary(null);
					nGram.setVocabulary(vocab);
					nGramRepository.save(nGram);
				} else {
					nGram.setNewVocabulary(null);
					nGramRepository.delete(nGram);
				}
			}

			vocab.updateWhenUpdated();
			Parameter newWords = parameterRepository.findByTableName(ParametersTableContants.NEW_WORDS_IN_VOCABULARY)
					.get(0);
			newWords.setValueRegistry("true");
			parameterRepository.save(newWords);

			vocab.setHasNewWords(false);

			vocabularyRepository.save(vocab);

			getLoggerFromClass(getClass()).info("Vocabulary updated successfuly");
			return;
		}

		getLoggerFromClass(getClass()).debug("Vocabulary is already up-to-date");
	}

	@Transactional
	public StatusRetorno addGram(String gram) {
		StatusRetorno result = StatusRetorno.ALREADY_IN_VOCABULARY;

		if (!hasInVocabulary(gram)) {
			result = StatusRetorno.NOT_IN_VOCABULARY;
		}

		if (!hasInNewVocabulary(gram) && result == StatusRetorno.NOT_IN_VOCABULARY) {
			result = StatusRetorno.NOT_IN_NEW_VOCABULARY;
		}

		if (result == StatusRetorno.NOT_IN_NEW_VOCABULARY) {
			Vocabulary vocab = getVocabulary();
			NGram item = new NGram(gram);
			item.setNewVocabulary(vocab);

			vocab.setHasNewWords(true);
			vocab.getNewNGrams().add(item);

			vocabularyRepository.save(vocab);
		}

		return result;
	}

	public boolean hasInNewVocabulary(String gram) {

		Optional<NGram> optNGram = nGramRepository.findByGramAndNewVocabulary(gram, getVocabulary());

		return optNGram.isPresent();
	}

	public boolean hasInVocabulary(String gram) {
		Optional<NGram> optNGram = nGramRepository.findByGramAndVocabulary(gram, getVocabulary());

		return optNGram.isPresent();
	}

	public Vocabulary getVocabulary() {

		Optional<Vocabulary> dbVocab = vocabularyRepository.findById(VOCAB_ID);

		if (dbVocab.isPresent()) {
			return dbVocab.get();
		} else {
			return null;
		}
	}
	
	public Integer getOfficialSize() {
		return nGramRepository.countOfficialVocabularySize(VOCAB_ID);
	}
}
