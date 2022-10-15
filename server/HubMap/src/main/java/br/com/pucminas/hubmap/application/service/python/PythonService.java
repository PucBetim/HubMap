package br.com.pucminas.hubmap.application.service.python;

import java.io.File;
import java.io.IOException;
import java.text.Normalizer;
import java.util.List;

import org.springframework.stereotype.Service;

import jep.Interpreter;
import jep.SharedInterpreter;

@Service
public class PythonService {
	
	@SuppressWarnings("unchecked")
	public List<String> getBagOfWords(String sentence) throws IOException {
		
		File file = new File("resources/python/spacy.py");
		String fullPath = file.getAbsolutePath();
		
		List<String> tokens = null;
		
		try (Interpreter interp = new SharedInterpreter()) {

			sentence = Normalizer.normalize(sentence, Normalizer.Form.NFKD).replaceAll("\\p{M}", "");

			interp.set("sentence", sentence);
			interp.runScript(fullPath);

			tokens = interp.getValue("bagOfWords", List.class);
		}
		
		return tokens;
	}
}
