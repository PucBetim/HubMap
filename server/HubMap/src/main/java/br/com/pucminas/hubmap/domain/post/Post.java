package br.com.pucminas.hubmap.domain.post;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import br.com.pucminas.hubmap.domain.comment.Comment;
import br.com.pucminas.hubmap.domain.indexing.NGram;
import br.com.pucminas.hubmap.domain.map.Map;
import br.com.pucminas.hubmap.domain.user.AppUser;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Entity
@EntityListeners(PostListener.class)
@JsonIgnoreProperties({"author", "ngrams", "comments"})
@Getter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Post implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	private Integer id;
	
	@NotBlank(message = "Por favor, informe o título do post")
	@Size(max = 30, message = "O título é muito grande")
	@Column(length = 30, nullable = false)
	private String title;

	@Size(max = 200, message = "A descrição é muito grande")
	@Column(length = 200)
	private String description;

	private Integer likes;

	private Integer dislikes;

	private Integer views;

	private boolean isPrivate;
	
	//TODO @NotNull add this annotation 
	@OneToOne(mappedBy = "post")
	@PrimaryKeyJoinColumn
	private Map map;
	
	@ManyToOne
	@JoinColumn(name = "AUTHOR_ID", referencedColumnName = "id")
	private AppUser author;
	
	@JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
	private LocalDateTime created;
	
	@JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
	private LocalDateTime modified;
	
	@OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<Comment> comments = new HashSet<>();
	
	@OneToMany(mappedBy = "id.post", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<NGram> ngrams = new HashSet<>();

	public Post(String title, String description, Map map, boolean isPrivate, AppUser author) {
		this.title = title;
		this.description = description;
		this.map = map;
		this.isPrivate = isPrivate;
		this.author = author;
		initializePost();
	}
	
	public void initializePost() {
		likes = 0;
		dislikes = 0;
		views = 0;
		created = LocalDateTime.now();
		setModifiedNow();
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

	public void changeLikes(boolean positive) {

		if (likes > 0) {
			likes += positive ? 1 : -1;
		} else if (likes == 0 && positive) {
			likes += 1;
		}
	}

	public void changeDislikes(boolean positive) {
		
		if(dislikes > 0) {
			dislikes += positive ? 1 : -1;
		} else if(dislikes == 0 && positive){
			dislikes += 1;
		}
	}

	public void addViews() {
		views++;
	}

	public void setPrivate(boolean isPrivate) {
		this.isPrivate = isPrivate;
	}

	public void setAuthor(AppUser author) {
		this.author = author;
	}
	
	public void setModifiedNow() {
		this.modified = LocalDateTime.now();
	}
	
	public void setComments(Set<Comment> comments) {
		this.comments = comments;
	}
	
	public void setNgrams(Set<NGram> ngrams) {
		this.ngrams = ngrams;
	}
}
