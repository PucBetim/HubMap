package br.com.pucminas.hubmap.infrastructure.web.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.pucminas.hubmap.domain.post.NGram;
import br.com.pucminas.hubmap.domain.post.NGramRepository;

@RestController
@RequestMapping(path = "/hubmap/ngrams")
public class NGramController {
	
	@Autowired
	NGramRepository nGramRepository;
	
	@GetMapping("")
	public ResponseEntity<List<NGram>> getCommentsByPost(@RequestParam(name = "post", required = true) Integer post) {
		try {
			List<NGram> ngrams = new ArrayList<>();

			nGramRepository.findByPost(post).forEach(ngrams::add);

			if (ngrams.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			return new ResponseEntity<>(ngrams, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
