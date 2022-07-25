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

import br.com.pucminas.hubmap.domain.comment.Comment;
import br.com.pucminas.hubmap.domain.comment.CommentRepository;

@RestController
@RequestMapping(path = "/hubmap/comments")
public class CommentController {

	@Autowired
	private CommentRepository commentRepository;

	@GetMapping("")
	public ResponseEntity<List<Comment>> getCommentsByPost(@RequestParam(name = "post", required = true) Integer post) {
		try {
			List<Comment> comments = new ArrayList<>();

			commentRepository.findByPost(post).forEach(comments::add);

			if (comments.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			return new ResponseEntity<>(comments, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
