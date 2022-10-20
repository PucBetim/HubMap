package br.com.pucminas.hubmap.application.service;

import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.pucminas.hubmap.domain.comment.Comment;
import br.com.pucminas.hubmap.domain.comment.CommentRepository;
import br.com.pucminas.hubmap.domain.user.AppUser;
import br.com.pucminas.hubmap.domain.user.AppUserRepository;
import br.com.pucminas.hubmap.utils.SecurityUtils;

@Service
public class CommentService {

	@Autowired
	private CommentRepository commentRepository;

	@Autowired
	private AppUserRepository appUserRepository;

	@Transactional
	public Comment save(Comment newComment) {

		if (newComment.getId() != null) {
			Comment dbComment = commentRepository.findById(newComment.getId()).orElseThrow();
			AppUser loggedUser = appUserRepository.findByEmail(SecurityUtils.getLoggedUserEmail());
			
			if(!canEdit(dbComment, loggedUser)) {
				return null;
			}
			
			newComment.setAuthor(dbComment.getAuthor());
			newComment.setPost(dbComment.getPost());
			newComment.setLikes(dbComment.getLikes());
			newComment.setDislikes(dbComment.getDislikes());
			newComment.setEdited(true);
		} else {
			newComment.initializeComment();
		}

		return commentRepository.save(newComment);
	}
	
	private boolean canEdit(Comment comment, AppUser author) {
		if(comment == null || author == null) {
			return false;
		}
		
		return comment.getAuthor() == author;
	}

	private boolean canDelete(Comment comment, AppUser author) {
		if(comment == null || author == null) {
			return false;
		}
		
		return comment.getAuthor() == author || comment.getPost().getAuthor() == author;
	}
	
	@Transactional
	public boolean delete(Integer commentId) {
		Optional<Comment> optComment = commentRepository.findById(commentId);

		if (optComment.isEmpty()) {
			return false;
		}

		AppUser loggedUser = appUserRepository.findByEmail(SecurityUtils.getLoggedUserEmail());

		Comment dbComment = optComment.get();

		if (!canDelete(dbComment, loggedUser)) {
			return false;
		}

		Set<Integer> comments = getAllChildren(commentId, null);
		
		commentRepository.deleteAllById(comments);
		commentRepository.delete(dbComment);

		return true;
	}
	
	private Set<Integer> getAllChildren(Integer parentId, Set<Integer> newIds) {
		
		Set<Integer> comments = commentRepository.findByRepliedTo(parentId);
		
		if(comments.isEmpty() && newIds != null) {
			
			if(!newIds.isEmpty() ) {
				comments.addAll(newIds);
			}
			
			return comments;
		}
		
		for(Integer id : comments) {
			comments.addAll(getAllChildren(id, comments));
		}
		
		return comments;
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
