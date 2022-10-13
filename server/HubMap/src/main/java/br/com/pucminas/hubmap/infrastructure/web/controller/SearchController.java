package br.com.pucminas.hubmap.infrastructure.web.controller;

import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.pucminas.hubmap.application.service.SearchService;
import br.com.pucminas.hubmap.domain.indexing.search.Search;
import br.com.pucminas.hubmap.infrastructure.web.RestResponse;
import br.com.pucminas.hubmap.utils.StringUtils;

@RestController
@RequestMapping(path = "/hubmap/public/search")
public class SearchController {

	@Autowired
	private SearchService searchService;

	@GetMapping("")
	public ResponseEntity<RestResponse> search(@RequestBody Search search)  {

		HttpStatus status;
		String msg;
		RestResponse response;
		
		try {			
			response = searchService.search(search);
			
			response.setMessage("Pesquisa realizada com sucesso.");
			
			if(StringUtils.isBlank(response.getDataId())) {
				status = HttpStatus.NO_CONTENT;
			} else {
				status = HttpStatus.OK;
			}
		} catch (InterruptedException ie) {
			msg = "Ocorreu um erro interno durante o cálculo de similaridade.";
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			response = RestResponse.fromNormalResponse(msg, null);
		} catch (ExecutionException e) {
			msg = "Ocorreu um erro interno durante o cálculo de similaridade.";
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			response = RestResponse.fromNormalResponse(msg, null);
		}
		
		return new ResponseEntity<>(response, status);
	}
}
