package br.com.pucminas.hubmap.application.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.pucminas.hubmap.domain.post.Post;
import br.com.pucminas.hubmap.domain.post.PostRepository;

@Service
public class PostService {
	
	@Autowired
	private PostRepository postRepository;
	
	@Transactional
	public Post save(Post newPost) {
		
		if(newPost.getId() != null) {
			
			Post dbPost = postRepository.findById(newPost.getId()).orElseThrow();
			
			newPost.setAuthor(dbPost.getAuthor());
			newPost.setLikes(dbPost.getLikes());
			newPost.setDislikes(dbPost.getDislikes());
			newPost.setViews(dbPost.getViews());
			newPost.setCreated(dbPost.getCreated());
			newPost.setModifiedNow();
		} else {
			newPost.initializePost();
		}
		
		return postRepository.save(newPost);
	}
	
	@Transactional
	public void delete(Integer postId) {			
		postRepository.deleteById(postId);
	}
	
	@Transactional
	public void changeLike(Integer postId, boolean positive) {
		Post dbPost = postRepository.findByIdWhereIsPrivateFalse(postId).orElseThrow();
		dbPost.changeLikes(positive);
	}
	
	@Transactional
	public void changeDislike(Integer postId, boolean positive) {
		Post dbPost = postRepository.findByIdWhereIsPrivateFalse(postId).orElseThrow();
		dbPost.changeDislikes(positive);
	}

	@Transactional
	public void addView(Integer postId) {
		Post dbPost = postRepository.findByIdWhereIsPrivateFalse(postId).orElseThrow();
		dbPost.addViews();
	}
}
