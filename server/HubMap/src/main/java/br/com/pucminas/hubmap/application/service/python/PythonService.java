package br.com.pucminas.hubmap.application.service.python;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.text.Normalizer;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import jep.Interpreter;
import jep.SharedInterpreter;

public class PythonService implements Runnable {

	private static PythonService obj;

	private ReentrantLock lock = new ReentrantLock();
	private Condition condition = lock.newCondition();

	private Interpreter interp;
	private final String fullPath;
	private String sentence;
	private List<String> tokens;
	private volatile boolean setted;
	private volatile boolean finished;
	private volatile boolean calculated;
	private volatile boolean threadUp;

	private PythonService() {
		File file;

		URL res = getClass().getClassLoader().getResource("python/spacy.py");
		try {
			file = Paths.get(res.toURI()).toFile();
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}

		setted = false;
		finished = true;
		calculated = false;
		threadUp = true;
		fullPath = file.getAbsolutePath();
	}

	public void setSentence(String sentence) {
		if (sentence == null || sentence.isBlank())
			return;

		sentence = Normalizer
					.normalize(sentence, Normalizer.Form.NFKD)
					.replaceAll("\\p{M}", "")
					.toLowerCase();

		lock.lock();
		try {
			while (setted || !finished)
				condition.await();

			this.sentence = sentence;
			setted = true;
			finished = false;
			calculated = false;
			condition.signalAll();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
	}

	public List<String> getTokens() {
		lock.lock();
		try {
			while (!calculated)
				condition.await();

			finished = true;
			return tokens;
		} catch (InterruptedException e) {
			e.printStackTrace();
			return null;
		} finally {
			lock.unlock();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void run() {
		interp = new SharedInterpreter();

		while (true) {
			if (!threadUp)
				break;

			lock.lock();
			try {
				if (!threadUp)
					break;

				if (!setted)
					condition.await();

				interp.set("sentence", sentence);
				interp.runScript(fullPath);

				tokens = interp.getValue("bagOfWords", List.class);
				setted = false;
				calculated = true;
				condition.signalAll();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} finally {
				lock.unlock();
			}
		}
	}

	public void destroy() {
		threadUp = false;
		interp.close();
		condition.signalAll();
	}

	public static PythonService getInstance() {
		if (obj == null)
			obj = new PythonService();

		return obj;
	}
}
