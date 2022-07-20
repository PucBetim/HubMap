package br.com.pucminas.hubmap.domain.post;

import javax.persistence.PrePersist;

import org.springframework.stereotype.Component;

@Component
public class PostListener {

	@PrePersist
	public void onPrePersistPost(Post post) {
		post.initializePost();
	}
}
