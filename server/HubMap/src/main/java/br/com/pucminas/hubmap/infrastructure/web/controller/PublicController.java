package br.com.pucminas.hubmap.infrastructure.web.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.pucminas.hubmap.domain.post.Post;
import br.com.pucminas.hubmap.domain.post.PostRepository;

@RestController
@RequestMapping(path = "/hubmap/public")
public class PublicController {

	@Autowired
	private PostRepository postRepository;
	
	@GetMapping("/posts")
	public ResponseEntity<List<Post>> getAllPublicPosts() {
		
		List<Post> posts = new ArrayList<>();
		
		postRepository.findAllPublic().forEach(posts::add);
		posts.forEach(p -> p.setMapToShow());
		
		if (posts.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}

		return new ResponseEntity<>(posts, HttpStatus.OK);
	}
	
}
