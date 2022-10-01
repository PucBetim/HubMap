package br.com.pucminas.hubmap.domain.comment;

import javax.persistence.PreRemove;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.pucminas.hubmap.domain.post.Post;
import br.com.pucminas.hubmap.domain.post.PostRepository;

@Component
public class CommentListener {

	
	private static PostRepository postRepository;
		
	@PreRemove
	public void onPreRemove(Comment comment) {
		Post post = postRepository.findById(comment.getPost().getId()).orElseThrow();
		post.getComments().remove(comment);
	}
	
	@Autowired
	public void init(PostRepository postRepository) {
		CommentListener.postRepository = postRepository;
	}
}
