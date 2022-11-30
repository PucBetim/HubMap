package br.com.pucminas.hubmap.application.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.pucminas.hubmap.domain.indexing.Histogram;
import br.com.pucminas.hubmap.domain.post.Post;
import br.com.pucminas.hubmap.domain.post.PostRepository;

@Service
public class PostService {
	
	@Autowired
	private PostRepository postRepository;
	
	@Transactional
	public Post save(Post newPost) {
		
		Post dbNewPost;
		
		if(newPost.getId() != null) {
			
			Post dbPost = postRepository.findByIdFromLoggedAuthor(newPost.getId()).orElseThrow();
			
			newPost.setAuthor(dbPost.getAuthor());
			newPost.setLikes(dbPost.getLikes());
			newPost.setDislikes(dbPost.getDislikes());
			newPost.setViews(dbPost.getViews());
			newPost.setCreated(dbPost.getCreated());
			newPost.setModifiedNow();
			dbNewPost = postRepository.save(newPost);
		} else {
			newPost.initializePost();
			dbNewPost = postRepository.save(newPost);
			dbNewPost.setHistogram(new Histogram(dbNewPost));
		}
		
		return dbNewPost;
	}
	
	@Transactional
	public boolean delete(Integer postId) {
		boolean isPresent = postRepository.findByIdFromLoggedAuthor(postId).isPresent();
		
		if(isPresent) {
			postRepository.deleteById(postId);
			return true;
		}
		return false;
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
