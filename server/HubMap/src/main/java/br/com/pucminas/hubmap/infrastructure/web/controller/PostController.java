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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.pucminas.hubmap.application.service.PostService;
import br.com.pucminas.hubmap.domain.post.Post;
import br.com.pucminas.hubmap.domain.post.PostRepository;
import br.com.pucminas.hubmap.domain.user.AppUser;
import br.com.pucminas.hubmap.domain.user.AppUserRepository;
import br.com.pucminas.hubmap.infrastructure.web.RestResponse;
import br.com.pucminas.hubmap.utils.SecurityUtils;

@RestController
@RequestMapping(path = { "/hubmap/posts" })
public class PostController {

	@Autowired
	private PostRepository postRepository;

	@Autowired
	private PostService postService;
	
	@Autowired
	private AppUserRepository appUserRepository;

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
	public ResponseEntity<RestResponse> postPosts(@RequestBody @Valid Post newPost) {
		
		try {
			AppUser loggedUser = appUserRepository.findByEmail(SecurityUtils.getLoggedUserEmail());
			
			newPost.setAuthor(loggedUser);
			newPost = postService.save(newPost);
			
			RestResponse response = RestResponse.fromNormalResponse("Post criado com sucesso.", newPost.getId());
			
			return new ResponseEntity<>(response, HttpStatus.CREATED);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
	}

	@PutMapping(path = "/{id}")
	public ResponseEntity<RestResponse> putPost(
			@PathVariable("id") Integer id, 
			@RequestBody @Valid Post newPost) {

		try {
			
			newPost.setId(id);
			newPost = postService.save(newPost);
			
			RestResponse response = RestResponse.fromNormalResponse("Post atualizado com sucesso.", newPost.getId());
			
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping("/{id}/likes")
	public ResponseEntity<RestResponse> changeLike(
			@PathVariable("id") Integer id,
			@RequestParam(name = "add", required = true) Boolean positive) {
		
		try {
			postService.changeLike(id, positive);
			RestResponse response = RestResponse.fromNormalResponse("Likes alterados com sucesso.", id);
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping("/{id}/dislikes")
	public ResponseEntity<RestResponse> changeDislike(
			@PathVariable("id") Integer id,
			@RequestParam(name = "add", required = true) Boolean positive) {
		
		try {
			postService.changeDislike(id, positive);
			RestResponse response = RestResponse.fromNormalResponse("Dislikes alterados com sucesso.", id);
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping("/{id}/views")
	public ResponseEntity<RestResponse> addViews(@PathVariable("id") Integer id) {
		
		try {
			postService.addView(id);
			RestResponse response = RestResponse.fromNormalResponse("Views incrementadas com sucesso.", id);
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
}
