package br.com.pucminas.hubmap.domain.comment;

import javax.persistence.PrePersist;

import org.springframework.stereotype.Component;

@Component
public class CommentListener {

	@PrePersist
	public void onPrePersistComment(Comment comment) {
		comment.initializeComment();
	}
}
