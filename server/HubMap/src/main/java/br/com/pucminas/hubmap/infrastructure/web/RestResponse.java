package br.com.pucminas.hubmap.infrastructure.web;

import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class RestResponse {
	
	private String message;
	
	private Integer dataId;
	
	public static RestResponse fromValidationError(Errors errors, Integer id) {
		RestResponse restError = new RestResponse();
		StringBuilder sb = new StringBuilder();
		
		for (ObjectError message : errors.getAllErrors()) {
			sb.append(message.getDefaultMessage() + ". ");
		}
		
		restError.message = sb.toString();
		restError.dataId = id;
		
		return restError;
	}
	
	public static RestResponse fromValidationError(Errors errors) {
		return fromValidationError(errors, 0);
	}
	
	public static RestResponse fromNormalResponse(String message, Integer dataId) {
		RestResponse response = new RestResponse();
		response.setMessage(message);
		response.setDataId(dataId);
		
		return response;
	}
}
