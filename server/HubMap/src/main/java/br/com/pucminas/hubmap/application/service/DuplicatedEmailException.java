package br.com.pucminas.hubmap.application.service;

@SuppressWarnings("serial")
public class DuplicatedEmailException extends Exception {

	public DuplicatedEmailException() {
		super();
	}

	public DuplicatedEmailException(String message, Throwable cause) {
		super(message, cause);
	}

	public DuplicatedEmailException(String message) {
		super(message);
	}

	public DuplicatedEmailException(Throwable cause) {
		super(cause);
	}
}
