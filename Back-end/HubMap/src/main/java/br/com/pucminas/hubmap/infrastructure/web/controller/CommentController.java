package br.com.pucminas.hubmap.infrastructure.web.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
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

import br.com.pucminas.hubmap.application.Service.CommentRepliedToException;
import br.com.pucminas.hubmap.application.Service.CommentService;
import br.com.pucminas.hubmap.domain.comment.Comment;
import br.com.pucminas.hubmap.domain.comment.CommentRepository;
import br.com.pucminas.hubmap.domain.post.Post;
import br.com.pucminas.hubmap.domain.post.PostRepository;

@RestController
@RequestMapping(path = "/hubmap/comments")
public class CommentController {

	@Autowired
	private CommentRepository commentRepository;
	
	@Autowired
	private CommentService commentService;
	
	@Autowired
	private PostRepository postRepository;

	@GetMapping("")
	public ResponseEntity<List<Comment>> getCommentsByPost(@RequestParam(name = "post", required = true) Integer postId) {
		try {
			List<Comment> comments = new ArrayList<>();
			
			Post post = postRepository.findById(postId).orElseThrow();
			
			commentRepository.findByPost(post).forEach(comments::add);

			if (comments.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			return new ResponseEntity<>(comments, HttpStatus.OK);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping(path = "/{id}")
	public ResponseEntity<Comment> getCommentById(@PathVariable Integer id){
		try {
			Comment comment = commentRepository.findById(id).orElseThrow();
			
			return new ResponseEntity<>(comment, HttpStatus.OK);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<List<Comment>> deleteById(@PathVariable Integer id) {
		try {
			commentService.delete(id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (CommentRepliedToException e) {
			return new ResponseEntity<>(e.getAssociatedComments(), HttpStatus.CONFLICT);
		}
	}
	
	@PostMapping(path = "")
	public ResponseEntity<Comment> postComment(
			@RequestParam(name = "post", required = true) Integer postId,
			@RequestBody @Valid Comment comment) {
		
		try {
			Post post = postRepository.findById(postId).orElseThrow();
			
			comment.setPost(post);
			comment = commentService.save(comment);
			
			return new ResponseEntity<>(comment, HttpStatus.CREATED);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@PutMapping(path = "/{id}")
	public ResponseEntity<Comment> putComment(
			@PathVariable Integer id,
			@RequestBody Comment newComment) {
		try {
			Comment oldComment = commentRepository.findById(id).orElseThrow();
			
			newComment = commentService.save(oldComment, newComment);
			
			return new ResponseEntity<>(newComment, HttpStatus.OK);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
}
