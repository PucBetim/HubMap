package br.com.pucminas.hubmap.infrastructure.web.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.pucminas.hubmap.application.Service.PostService;
import br.com.pucminas.hubmap.domain.post.Post;
import br.com.pucminas.hubmap.domain.post.PostRepository;

@RestController
@RequestMapping(path = { "/hubmap/posts" })
public class PostController {

	@Autowired
	private PostRepository postRepository;

	@Autowired
	private PostService postService;

	@GetMapping("")
	public ResponseEntity<List<Post>> getPosts() {
		try {

			List<Post> posts = new ArrayList<>();

			postRepository.findAll(Sort.by("title")).forEach(posts::add);

			return new ResponseEntity<>(posts, HttpStatus.OK);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/{id}")
	public ResponseEntity<Post> getPostsById(@PathVariable Integer id) {
		try {
			Post post = postRepository.findById(id).orElseThrow();

			return new ResponseEntity<>(post, HttpStatus.OK);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Post> deletePostsById(@PathVariable Integer id) {
		postRepository.deleteById(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@PostMapping("")
	public ResponseEntity<Post> postPosts(@RequestBody @Valid Post newPost) {
		newPost = postRepository.save(newPost);
		return new ResponseEntity<>(newPost, HttpStatus.CREATED);
	}

	@PutMapping(path = "/{id}")
	public ResponseEntity<Post> putPost(
			@PathVariable("id") Integer id, 
			@RequestBody Post newPost) {

		try {
			Post oldPost = postRepository.findById(id).orElseThrow();

			newPost = postService.save(oldPost, newPost);

			return new ResponseEntity<>(newPost, HttpStatus.OK);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

	}
}
