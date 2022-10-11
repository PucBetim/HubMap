package br.com.pucminas.hubmap.infrastructure.web.controller;

import java.util.List;
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
import br.com.pucminas.hubmap.domain.post.Post;

@RestController
@RequestMapping(path = "/hubmap/public/search")
public class SearchController {

	@Autowired
	private SearchService searchService;

	@GetMapping("")
	public ResponseEntity<List<Post>> search(@RequestBody Search search)  {
		
		try {
			List<Post> posts = searchService.search(search);
			
			if(posts.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			} else {
				return new ResponseEntity<>(posts, HttpStatus.OK);
			}
		} catch (InterruptedException ie) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (ExecutionException e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
