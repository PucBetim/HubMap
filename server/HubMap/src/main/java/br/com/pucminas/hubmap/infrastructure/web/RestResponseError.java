package br.com.pucminas.hubmap.infrastructure.web;

import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;

public class RestResponseError {
	
	private String error;
	
	private RestResponseError() {}
	
	public String getError() {
		return error;
	}
	
	public static RestResponseError fromValidationError(Errors errors) {
		RestResponseError restError = new RestResponseError();
		StringBuilder sb = new StringBuilder();
		
		for (ObjectError error : errors.getAllErrors()) {
			sb.append(error.getDefaultMessage() + ". ");
		}
		
		restError.error = sb.toString();
		
		return restError;
	}
}
