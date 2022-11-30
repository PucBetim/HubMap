package br.com.pucminas.hubmap.infrastructure.web.controller;

import java.util.List;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.pucminas.hubmap.application.service.SearchService;
import br.com.pucminas.hubmap.domain.indexing.search.Search;
import br.com.pucminas.hubmap.infrastructure.web.RestResponse;

@RestController
@RequestMapping(path = "/hubmap/public/search")
public class SearchController {

	@Autowired
	private SearchService searchService;

	@PostMapping("")
	public ResponseEntity<RestResponse> search(@RequestBody Search search)  {

		HttpStatus status;
		String msg;
		RestResponse response;
		
		try {			
			List<Integer> posts = searchService.search(search);
			
			if(!posts.isEmpty()) {
				status = HttpStatus.OK;
				msg = "Pesquisa realizada com sucesso.";
				response = RestResponse.fromSearchResult(msg, posts);
			} else {
				response = null;
				status = HttpStatus.NO_CONTENT;
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
