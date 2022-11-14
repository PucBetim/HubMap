package br.com.pucminas.hubmap;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.Test;

import jep.Interpreter;
import jep.SharedInterpreter;

public class PythonTests {

	@SuppressWarnings("unchecked")
	@Test
	public void testSth() throws IOException {

		String sentence = "Com os nossos métodos conseguimos ensinar, sobretudo alunos iniciantes, por meio de textos práticos, que favorecem a boa leitura e consequente compreensão do que é ensinado para as crianças.";

		try (Interpreter interp = new SharedInterpreter()) {
			
			interp.set("sentence", sentence);
			interp.runScript("D:\\Workspace\\TCC\\HubMap\\server\\HubMap\\src\\test\\resources\\spacyTest.py");
			
			List<String> tokens = interp.getValue("bagOfWords", List.class);
			String tokensStr = interp.getValue("bagOfWordsString", String.class);
			
			System.out.println("=== Palavras na lista ===");
			for (int i = 0; i < tokens.size(); i++) {
				System.out.println(tokens.get(i) + " ");
			}
			
			System.out.println("=== Palavras na string ===\n\n" + tokensStr);
			
			FileOutputStream outputStream = new FileOutputStream("D:\\tmp\\java_out.txt");
		    byte[] strToBytes = tokensStr.getBytes();
		    outputStream.write(strToBytes);

		    outputStream.close();
		}
	}

}
