package br.com.pucminas.hubmap.infrastructure.web;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class WebRequestExceptionHandler {
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	public RestResponse handleException(MethodArgumentNotValidException e) {
		return RestResponse.fromValidationError(e.getBindingResult());
	}
}
