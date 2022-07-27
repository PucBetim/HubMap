package br.com.pucminas.hubmap.application.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.pucminas.hubmap.domain.post.Post;
import br.com.pucminas.hubmap.domain.post.PostRepository;
import br.com.pucminas.hubmap.utils.StringUtils;

@Service
public class PostService {
	
	@Autowired
	private PostRepository postRepository;
	
	public Post save(Post dbPost, Post newPost) {
		dbPost = postRepository.findById(dbPost.getId()).orElseThrow();
		
		if(!StringUtils.isBlank(newPost.getDescription())){
			dbPost.setDescription(newPost.getDescription());
			dbPost.setModifiedNow();
		}
		
		if(!StringUtils.isBlank(newPost.getTitle())){
			dbPost.setTitle(newPost.getTitle());
			dbPost.setModifiedNow();
		}
		
		if(newPost.getLikes() != 0) {
			dbPost.changeLikes(newPost.getLikes() > 0);
		}
		
		if(newPost.getDislikes() != 0) {
			dbPost.changeDislikes(newPost.getDislikes() > 0);
		}
		
		if(newPost.getViews() != 0) {
			dbPost.addViews();
		}
		
		return postRepository.save(dbPost);
	}
}
