package br.com.pucminas.hubmap.domain.post;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import br.com.pucminas.hubmap.domain.comment.Comment;
import br.com.pucminas.hubmap.domain.map.Map;
import br.com.pucminas.hubmap.domain.user.AppUser;
import lombok.Getter;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Entity
@Getter
@NoArgsConstructor
public class Post implements Serializable{

	@Id
	@GeneratedValue
	private int id;
	
	private String title;
	
	private String description;
	
	private Map map;
	
	private int likes;
	
	private int dislikes;
	
	private int views;
	
	private boolean isPrivate;
	
	private AppUser author;
	
	private LocalDateTime timestamp;
	
	private Comment[] comments;

	public Post(String title, String description, Map map, int likes, int dislikes, int views, boolean isPrivate,
			AppUser author, LocalDateTime timestamp, Comment[] comments) {
		this.title = title;
		this.description = description;
		this.map = map;
		this.likes = likes;
		this.dislikes = dislikes;
		this.views = views;
		this.isPrivate = isPrivate;
		this.author = author;
		this.timestamp = timestamp;
		this.comments = comments;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setMap(Map map) {
		this.map = map;
	}

	public void setLikes(int likes) {
		this.likes = likes;
	}

	public void setDislikes(int dislikes) {
		this.dislikes = dislikes;
	}

	public void setViews(int views) {
		this.views = views;
	}

	public void setPrivate(boolean isPrivate) {
		this.isPrivate = isPrivate;
	}

	public void setAuthor(AppUser author) {
		this.author = author;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

	public void setComments(Comment[] comments) {
		this.comments = comments;
	}
	
}
