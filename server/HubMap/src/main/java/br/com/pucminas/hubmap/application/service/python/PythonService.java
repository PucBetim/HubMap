package br.com.pucminas.hubmap.application.service.python;

import java.io.File;
import java.io.IOException;
import java.text.Normalizer;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jep.Interpreter;
import jep.SharedInterpreter;

@Service
public class PythonService {

	@Value("${hubmap.scripts.python.path}")
	private String scritpsPythonPath;
	
	@Value("${hubmap.scripts.python.file-name}")
	private String scritpPythonFileName;
	
	@SuppressWarnings("unchecked")
	public List<String> getBagOfWords(String sentence) throws IOException {
		
		String canonicalPath =  new File(".").getCanonicalPath();
		String fullPath = canonicalPath + scritpsPythonPath + scritpPythonFileName;
		
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
