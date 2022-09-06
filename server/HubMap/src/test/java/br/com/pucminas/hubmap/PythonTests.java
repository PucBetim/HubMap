package br.com.pucminas.hubmap;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import jep.Interpreter;
import jep.SharedInterpreter;

public class PythonTests {
	
	@Test
	public void testSth() {
		
		String sentence = "O rato roeu a roupa do rei de roma.";
		
		try (Interpreter interp = new SharedInterpreter()) {
			
		    interp.set("sentence", sentence);
		    interp.runScript("D:\\Workspace\\TCC\\HubMap\\server\\HubMap\\src\\test\\resources\\spacyTest.py");
		    String[] tokens = interp.getValue("tokens", String[].class);
		   
		    for(int i = 0; i < tokens.length; i++) {
		    	System.out.println(tokens[i]);
		    }
		    
		    //TODO Add java.library.path the path of jep directory (Native Library location)
		    //TODO Try to deploy with spacy modules and portuguese trained model.
		    assertEquals(tokens.length, 10);
		}
	}
	
}
