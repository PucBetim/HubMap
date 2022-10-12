package br.com.pucminas.hubmap.infrastructure.web.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
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

import br.com.pucminas.hubmap.application.service.CommentService;
import br.com.pucminas.hubmap.domain.comment.Comment;
import br.com.pucminas.hubmap.domain.comment.CommentRepository;
import br.com.pucminas.hubmap.domain.post.Post;
import br.com.pucminas.hubmap.domain.post.PostRepository;
import br.com.pucminas.hubmap.domain.user.AppUser;
import br.com.pucminas.hubmap.domain.user.AppUserRepository;
import br.com.pucminas.hubmap.infrastructure.web.RestResponse;
import br.com.pucminas.hubmap.utils.PageableUtils;
import br.com.pucminas.hubmap.utils.SecurityUtils;

@RestController
@RequestMapping(path = "/hubmap/comments")
public class CommentController {

	@Autowired
	private CommentRepository commentRepository;
	
	@Autowired
	private CommentService commentService;
	
	@Autowired
	private PostRepository postRepository;
	
	@Autowired
	private AppUserRepository appUserRepository;

	@GetMapping("")
	public ResponseEntity<List<Comment>> getCommentsByPost(
			@RequestParam(name = "post", required = true) Integer postId,
			@RequestParam(required = false) Integer page,
			@RequestParam(required = false) Integer size, 
			@RequestParam(required = false) Boolean descending,
			@RequestParam(required = false) String... sort) {
		
		if(size == null) {
			size = 10;
		}
		
		if(sort == null) {
			sort = new String[]{"timestamp"};
		}
		
		try {
			List<Comment> comments = new ArrayList<>();
			
			Pageable pageable = PageableUtils.getPageableFromParameters(page, size, descending, sort);
			commentRepository.findByPost(postId, pageable).forEach(comments::add);
			comments.forEach(c -> c.getAuthor().setAuthorForPublicAccess());
			
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
	public ResponseEntity<RestResponse> deleteById(@PathVariable Integer id) {
		boolean deleted = commentService.delete(id);
		HttpStatus status = HttpStatus.NO_CONTENT;
		RestResponse response = null;
		
		if(!deleted) {
			status = HttpStatus.BAD_REQUEST;
			response = RestResponse.fromNormalResponse("O comentário não foi encontrado ou você não possui permissão para deletá-lo.", id.toString());
		}
		
		return new ResponseEntity<>(response, status);
	}
	
	@PostMapping(path = "")
	public ResponseEntity<RestResponse> postComment(
			@RequestParam(name = "post", required = true) Integer postId,
			@RequestBody @Valid Comment comment) {
		
		try {
			Post post = postRepository.findByIdIfPublicOrFromAuthor(postId).orElseThrow();
			AppUser loggedUser = appUserRepository.findByEmail(SecurityUtils.getLoggedUserEmail());
			
			comment.setPost(post);
			comment.setAuthor(loggedUser);
			comment = commentService.save(comment);
			
			RestResponse response = RestResponse.fromNormalResponse("Comentário criado com sucesso.", comment.getId().toString());
			
			return new ResponseEntity<>(response, HttpStatus.CREATED);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@PutMapping(path = "/{id}")
	public ResponseEntity<RestResponse> putComment(
			@PathVariable Integer id,
			@RequestBody @Valid Comment newComment) {
		
		HttpStatus status = HttpStatus.OK;
		String msg = null;
		RestResponse response = null;
		
		try {
			newComment.setId(id);
			newComment = commentService.save(newComment);
			
			if(newComment == null) {
				status = HttpStatus.BAD_REQUEST;
				msg = "O comentário não foi encontrado ou você não possui permissão para editá-lo.";
			} else {
				msg = "Comentário atualizado com sucesso.";	
			}
			
		} catch (NoSuchElementException e) {
			status = HttpStatus.BAD_REQUEST;
			msg = "O comentário não foi encontrado ou você não possui permissão para editá-lo.";
		}
		
		response = RestResponse.fromNormalResponse(msg, id.toString());
		
		return new ResponseEntity<>(response, status);
	}
	
	@PostMapping("/{id}/likes")
	public ResponseEntity<RestResponse> changeLike(
			@PathVariable("id") Integer id,
			@RequestParam(name = "add", required = true) Boolean positive) {
		
		try {
			commentService.changeLike(id, positive);
			RestResponse response = RestResponse.fromNormalResponse("Likes alterados com sucesso.", id.toString());
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
			commentService.changeDislike(id, positive);
			RestResponse response = RestResponse.fromNormalResponse("Dislikes alterados com sucesso.", id.toString());
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
}
