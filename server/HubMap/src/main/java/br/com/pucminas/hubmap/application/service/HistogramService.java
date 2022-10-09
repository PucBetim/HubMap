package br.com.pucminas.hubmap.application.service;

import java.io.IOException;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.InvalidPropertyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.pucminas.hubmap.application.service.python.PythonService;
import br.com.pucminas.hubmap.domain.indexing.Histogram;
import br.com.pucminas.hubmap.domain.indexing.HistogramItem;
import br.com.pucminas.hubmap.domain.indexing.HistogramItemRepository;
import br.com.pucminas.hubmap.domain.indexing.NGram;
import br.com.pucminas.hubmap.domain.map.Block;
import br.com.pucminas.hubmap.domain.post.Post;

@Service
public class HistogramService {

	@Autowired
	private PythonService pythonService;
	
	@Autowired
	private HistogramItemRepository histogramItemRepository;

	@Transactional
	public void generateHistogram(Block root, Post post) {

		String sentence = getWordsInBlocks(root, null);

		List<String> bOW;
		
		try {
			bOW = pythonService.getBagOfWords(sentence);
		} catch (IOException e) {
			throw new InvalidPropertyException(PythonService.class, "hubmap.scripts.python.path",
					"Não foi possível encontrar o caminho informado");
		}

		initialize(bOW, post.getHistogram());
	}
	
	private void initialize(List<String> bagOfWords, Histogram hist) {
		HistogramItem item;
		int counter;

		for (String word : bagOfWords) {
			if (!hist.isInHistogram(word)) {
				counter = hist.countWords(bagOfWords, word);
				
				item = new HistogramItem();
				item.setOwner(hist);
				item.setKey(new NGram(word));
				item.setCount(counter);
				
				HistogramItem dbItem = histogramItemRepository.save(item);
				
				hist.getHistogram().add(dbItem);
			}
		}
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
