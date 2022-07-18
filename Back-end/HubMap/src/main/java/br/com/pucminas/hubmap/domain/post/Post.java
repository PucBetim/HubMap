package br.com.pucminas.hubmap.domain.post;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;

import br.com.pucminas.hubmap.domain.comment.Comment;
import br.com.pucminas.hubmap.domain.map.Map;
import br.com.pucminas.hubmap.domain.user.AppUser;
import lombok.Getter;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Entity
@Getter
@NoArgsConstructor
public class Post implements Serializable {

	@Id
	@GeneratedValue
	private int id;

	private String title;

	private String description;

	@OneToOne(mappedBy = "post")
	@PrimaryKeyJoinColumn
	private Map map;

	private int likes;

	private int dislikes;

	private int views;

	private boolean isPrivate;

	@ManyToOne
	@JoinColumn(name = "AUTHOR_ID")
	private AppUser author;

	private LocalDateTime timestamp;

	@OneToMany(mappedBy = "post")
	private List<Comment> comments = new ArrayList<>();

	@OneToMany(mappedBy = "id.post")
	private List<NGram> ngrams = new ArrayList<>();

	public Post(String title, String description, Map map, int likes, int dislikes, int views, boolean isPrivate,
			AppUser author, LocalDateTime timestamp, List<Comment> comments, List<NGram> ngrams) {
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
		this.ngrams = ngrams;
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

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}
	
	public void setNgrams(List<NGram> ngrams) {
		this.ngrams = ngrams;
	}
}
