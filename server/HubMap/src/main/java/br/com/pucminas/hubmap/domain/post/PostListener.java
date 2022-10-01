package br.com.pucminas.hubmap.domain.post;

import javax.persistence.PreRemove;

import org.springframework.stereotype.Component;

@Component
public class PostListener {
	
	@PreRemove
	public void onPreRemove(Post post) {
		post.getComments().clear();
		post.getMap().clear();
	}
}
