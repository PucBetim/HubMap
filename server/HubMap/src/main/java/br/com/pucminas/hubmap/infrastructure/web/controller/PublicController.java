package br.com.pucminas.hubmap.infrastructure.web.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.pucminas.hubmap.domain.comment.Comment;
import br.com.pucminas.hubmap.domain.comment.CommentRepository;
import br.com.pucminas.hubmap.domain.post.Post;
import br.com.pucminas.hubmap.domain.post.PostRepository;
import br.com.pucminas.hubmap.utils.PageableUtils;

@RestController
@RequestMapping(path = "/hubmap/public")
public class PublicController {

	@Autowired
	private PostRepository postRepository;
	
	@Autowired
	private CommentRepository commetRepository;
	
	@GetMapping(path = "/posts")
	public ResponseEntity<List<Post>> getAllPublicPosts(
			@RequestParam(required = false) Integer page,
			@RequestParam(required = false) Integer size,
			@RequestParam(required = false) Boolean descending,
			@RequestParam(required = false) String... sort) {
		
		List<Post> posts = new ArrayList<>();
		
		if(size == null) {
			size = 10;
		}
		
		if(sort == null) {
			sort = new String[]{"title", "id"};
		}
		Pageable pageable = PageableUtils.getPageableFromParameters(page, size, descending, sort);
		
		postRepository.findAllPublic(pageable).forEach(posts::add);
		posts.forEach(p -> {
			p.setMapToShow();
			p.getAuthor().setAuthorForPublicAccess();
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
			post.getAuthor().setAuthorForPublicAccess();

			return new ResponseEntity<>(post, HttpStatus.OK);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@GetMapping(path = "/comments")
	public ResponseEntity<List<Comment>> getAllPublicComments(
			@RequestParam(required = true, name = "post") Integer postId,
			@RequestParam(required = false) Integer page,
			@RequestParam(required = false) Integer size,
			@RequestParam(required = false) Boolean descending,
			@RequestParam(required = false) String... sort) {
		
		List<Comment> comments = new ArrayList<>();
		
		if(size == null) {
			size = 10;
		}
		
		if(sort == null) {
			sort = new String[]{"timestamp"};
		}
		Pageable pageable = PageableUtils.getPageableFromParameters(page, size, descending, sort);
		
		commetRepository.findByPostIfPublic(postId, pageable).forEach(comments::add);
		comments.forEach(c -> c.getAuthor().setAuthorForPublicAccess());
		
		if (comments.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}

		return new ResponseEntity<>(comments, HttpStatus.OK);
	}
	
	@GetMapping("/comments/{id}")
	public ResponseEntity<Comment> getCommentsById(@PathVariable Integer id) {
		try {
			Comment comment = commetRepository.findByIdIfPostPublic(id).orElseThrow();
			comment.getAuthor().setAuthorForPublicAccess();
			
			return new ResponseEntity<>(comment, HttpStatus.OK);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
}
