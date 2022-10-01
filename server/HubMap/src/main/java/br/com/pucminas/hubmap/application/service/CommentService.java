package br.com.pucminas.hubmap.application.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.pucminas.hubmap.domain.comment.Comment;
import br.com.pucminas.hubmap.domain.comment.CommentRepository;

@Service
public class CommentService {
	
	@Autowired
	private CommentRepository commentRepository;

	@Transactional
	public Comment save(Comment newComment) {
			
		if(newComment.getId() != null) {
			Comment dbComment = commentRepository.findById(newComment.getId()).orElseThrow();
			
			newComment.setAuthor(dbComment.getAuthor());
			newComment.setPost(dbComment.getPost());
			newComment.setLikes(dbComment.getLikes());
			newComment.setDislikes(dbComment.getDislikes());
			newComment.setTimestampNow();
		} else {
			newComment.initializeComment();
		}
			
		return commentRepository.save(newComment);
	}

	@Transactional
	public void delete(Integer commentId) throws CommentRepliedToException {
		
		List<Comment> comments = commentRepository.findByRepliedTo(commentId);
		
		if(!comments.isEmpty()) {
			throw new CommentRepliedToException("Existem comentário(s) associados a este comentário, para deletar este comentário remova todos os comentários associados primeiramente.", comments);
		}
		
		commentRepository.deleteById(commentId);
	}
	
	@Transactional
	public void changeLike(Integer commentId, boolean positive) {
		Comment dbComment = commentRepository.findById(commentId).orElseThrow();
		dbComment.changeLikes(positive);
	}
	
	@Transactional
	public void changeDislike(Integer commentId, boolean positive) {
		Comment dbComment = commentRepository.findById(commentId).orElseThrow();
		dbComment.changeDislikes(positive);
	}
}
