package br.com.pucminas.hubmap;

import java.text.Normalizer;
import java.util.List;

import org.junit.jupiter.api.Test;

import jep.Interpreter;
import jep.SharedInterpreter;

public class PythonTests {

	@SuppressWarnings("unchecked")
	@Test
	public void testSth() {

		String sentence = "Com os nossos métodos conseguimos ensinar, sobretudo alunos iniciantes, por meio de textos práticos, que favorecem a boa leitura e consequente compreensão do que é ensinado.";
		//sentence = "Com os métodos 100% consagração, avô, avó";

		try (Interpreter interp = new SharedInterpreter()) {
			
			sentence = Normalizer.normalize(sentence, Normalizer.Form.NFKD).replaceAll("\\p{M}", "");
			
			interp.set("sentence", sentence);
			interp.runScript("D:\\Workspace\\TCC\\HubMap\\server\\HubMap\\src\\test\\resources\\spacyTest.py");
			
			List<String> tokens = interp.getValue("bagOfWords", List.class);
			
			for (int i = 0; i < tokens.size(); i++) {
				System.out.println(tokens.get(i) + " ");
			}
			
			/*
			 * interp.set("sentence", sentence); interp.runScript(
			 * "D:\\Workspace\\TCC\\HubMap\\server\\HubMap\\src\\test\\resources\\spacyTest.py"
			 * ); String[] tokens = interp.getValue("tokens", String[].class);
			 * 
			 * for(int i = 0; i < tokens.length; i++) { System.out.println(tokens[i]); }
			 * 
			 * //TODO Add java.library.path the path of jep directory (Native Library
			 * location) //TODO Try to deploy with spacy modules and portuguese trained
			 * model. assertEquals(tokens.length, 10);
			 */
		}
	}

}
