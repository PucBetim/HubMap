package br.com.pucminas.hubmap.domain.comment;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import br.com.pucminas.hubmap.domain.post.Post;
import br.com.pucminas.hubmap.domain.user.AppUser;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Entity
@Getter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Comment implements Serializable{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	private int id;
	
	@Column(nullable = false)
	@NotBlank(message = "O comentário deve ser informado.")
	@Size(min = 3, max = 200, message = "O comentário deve ter entre 3 e 200 caracteres.")
	private String content;
	
	//TODO check to storage or not post_id for all comments
	@ManyToOne
	@JoinColumn(name = "POST_ID")
	private Post post;
	
	private int likes;
	
	private int dislikes;
	
	private int views;
	
	@OneToOne
	@JoinColumn(name = "REPLIED_TO", referencedColumnName = "id")
	private Comment repliedTo;
	
	@NotNull
	@ManyToOne
	@JoinColumn(name = "AUTHOR_ID", referencedColumnName = "id")
	private AppUser author;
	
	@NotNull
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
