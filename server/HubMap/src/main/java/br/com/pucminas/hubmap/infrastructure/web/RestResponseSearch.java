package br.com.pucminas.hubmap.infrastructure.web;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class RestResponseSearch extends RestResponse {

	private Integer size;
	
	private Integer page;
	
	protected RestResponseSearch(String message, String dataId) {
		super(message, dataId);
	}
	
	public static RestResponseSearch fromSearchResult(String msg, List<Integer> ids, Integer size, Integer page) {
		
		String dataId = ids.toString().replace("[", "").replace("]", "");
		
		RestResponseSearch response = new RestResponseSearch(msg, dataId);
		response.setPage(page);
		response.setSize(size);
		
		return response;
	}
}
