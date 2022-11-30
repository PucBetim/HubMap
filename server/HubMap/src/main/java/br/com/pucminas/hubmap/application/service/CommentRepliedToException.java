package br.com.pucminas.hubmap.application.service;

import java.util.List;

import br.com.pucminas.hubmap.domain.comment.Comment;

@SuppressWarnings("serial")
public class CommentRepliedToException extends Exception{

	private List<Comment> associatedComments;
	
	public CommentRepliedToException(List<Comment> associatedComments) {
		this(null, null, associatedComments);
	}

	public CommentRepliedToException(String message, Throwable cause, List<Comment> associatedComments) {
		super(message, cause);
		this.associatedComments = associatedComments;
	}

	public CommentRepliedToException(String message, List<Comment> associatedComments) {
		this(message, null, associatedComments);
	}

	public CommentRepliedToException(Throwable cause, List<Comment> associatedComments) {
		this(null, cause, associatedComments);
	}
	
	public List<Comment> getAssociatedComments() {
		return associatedComments;
	}
}
