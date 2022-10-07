package br.com.pucminas.hubmap.infrastructure.web.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.pucminas.hubmap.domain.post.Post;
import br.com.pucminas.hubmap.domain.post.PostRepository;

@RestController
@RequestMapping(path = "/hubmap/public")
public class PublicController {

	@Autowired
	private PostRepository postRepository;
	
	@GetMapping(path = "/posts")
	public ResponseEntity<List<Post>> getAllPublicPosts() {
		
		List<Post> posts = new ArrayList<>();
		
		postRepository.findAllPublic().forEach(posts::add);
		posts.forEach(p -> {
			p.setMapToShow();
			p.setAuthorForPublicAccess();
		});
		
		if (posts.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}

		return new ResponseEntity<>(posts, HttpStatus.OK);
	}
	
	@GetMapping("/posts/{id}")
	public ResponseEntity<Post> getPostsById(@PathVariable Integer id) {
		try {
			Post post = postRepository.findByIdWhereIsPrivateFalse(id).orElseThrow();
			post.setMapToShow();
			post.setAuthorForPublicAccess();

			return new ResponseEntity<>(post, HttpStatus.OK);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
}
