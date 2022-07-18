package br.com.pucminas.hubmap.domain.comment;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import br.com.pucminas.hubmap.domain.user.AppUser;
import lombok.Getter;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Entity
@Getter
@NoArgsConstructor
public class Comment implements Serializable{
	
	@Id
	@GeneratedValue
	private int id;
	
	private String content;
	
	private int likes;
	
	private int dislikes;
	
	private int views;
	
	private Comment repliedTo;
	
	private AppUser author;
	
	private LocalDateTime timestamp;

	public Comment(String content, int likes, int dislikes, Comment repliedTo, AppUser author,
			LocalDateTime timestamp) {
		this.content = content;
		this.likes = likes;
		this.dislikes = dislikes;
		this.repliedTo = repliedTo;
		this.author = author;
		this.timestamp = timestamp;
	}

	public void setContent(String content) {
		this.content = content;
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

	public void setRepliedTo(Comment repliedTo) {
		this.repliedTo = repliedTo;
	}

	public void setAuthor(AppUser author) {
		this.author = author;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}
}