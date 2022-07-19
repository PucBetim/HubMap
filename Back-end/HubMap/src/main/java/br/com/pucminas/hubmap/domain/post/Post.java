package br.com.pucminas.hubmap.domain.post;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import br.com.pucminas.hubmap.domain.comment.Comment;
import br.com.pucminas.hubmap.domain.map.Map;
import br.com.pucminas.hubmap.domain.user.AppUser;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Entity
@Getter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Post implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	private int id;
	
	@NotBlank(message = "Por favor, informe o título do post.")
	@Size(max = 30, message = "O título é muito grande.")
	@Column(length = 30, nullable = false)
	private String title;

	@Size(max = 200, message = "A descrição é muito grande.")
	@Column(length = 200)
	private String description;

	@NotNull
	@OneToOne(mappedBy = "post")
	@PrimaryKeyJoinColumn
	private Map map;

	private int likes;

	private int dislikes;

	private int views;

	private boolean isPrivate;
	
	@NotNull(message = "O autor deve ser informado.")
	@ManyToOne
	@JoinColumn(name = "AUTHOR_ID")
	private AppUser author;
	
	@NotNull(message = "A data deve ser informada.")
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
