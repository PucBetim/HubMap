package br.com.pucminas.hubmap.application.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.pucminas.hubmap.domain.comment.Comment;
import br.com.pucminas.hubmap.domain.comment.CommentRepository;
import br.com.pucminas.hubmap.utils.StringUtils;

@Service
public class CommentService {
	
	@Autowired
	private CommentRepository commentRepository;
	
	@Transactional
	public Comment save(Comment comment) {
		return commentRepository.save(comment);
	}
	
	@Transactional
	public Comment save(Comment dbComment, Comment newComment) {
		
		dbComment = commentRepository.findById(dbComment.getId()).orElseThrow();
		
		if(!StringUtils.isBlank(newComment.getContent())){
			
			dbComment.setContent(newComment.getContent());
			dbComment.setTimestampNow();
		}
		
		if(newComment.getLikes() != 0) {
			dbComment.changeLikes(newComment.getLikes() > 0);
		}
		
		if(newComment.getDislikes() != 0) {
			dbComment.changeDislikes(newComment.getDislikes() > 0);
		}
		
		if(newComment.getViews() != 0) {
			dbComment.addViews();
		}
		
		return commentRepository.save(dbComment);
	}

	public void delete(Integer commentId) throws CommentRepliedToException {
		
		List<Comment> comments = commentRepository.findByRepliedTo(commentId);
		
		if(!comments.isEmpty()) {
			throw new CommentRepliedToException("Existem comentário(s) associados a este comentário, para deletar este comentário remova todos os comentários associados primeiramente.", comments);
		}
		
		commentRepository.deleteById(commentId);
	}
}
