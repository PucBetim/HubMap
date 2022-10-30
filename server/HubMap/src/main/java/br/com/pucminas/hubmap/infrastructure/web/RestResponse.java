package br.com.pucminas.hubmap.infrastructure.web;

import java.util.List;

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

	private String dataId;

	public RestResponse(String message, String dataId) {
		this.message = message;
		this.dataId = dataId;
	}

	public static RestResponse fromValidationError(Errors errors, Integer id) {

		StringBuilder sb = new StringBuilder();

		for (ObjectError message : errors.getAllErrors()) {
			sb.append(message.getDefaultMessage() + ". ");
		}

		return new RestResponse(sb.toString(), id.toString());
	}

	public static RestResponse fromValidationError(Errors errors) {
		return fromValidationError(errors, 0);
	}

	public static RestResponse fromNormalResponse(String message, String dataId) {
		return new RestResponse(message, dataId);
	}

	public static RestResponse fromSearchResult(String msg, List<Integer> ids) {

		String dataId = ids.toString().replace("[", "")
					.replace("]", "")
					.replace(" ", "");

		RestResponse response = new RestResponse(msg, dataId);

		return response;
	}
}
