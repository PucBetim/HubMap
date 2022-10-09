package br.com.pucminas.hubmap.domain.post;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

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
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import br.com.pucminas.hubmap.domain.comment.Comment;
import br.com.pucminas.hubmap.domain.indexing.Histogram;
import br.com.pucminas.hubmap.domain.indexing.NGram;
import br.com.pucminas.hubmap.domain.map.Block;
import br.com.pucminas.hubmap.domain.user.AppUser;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@SuppressWarnings("serial")
@Entity
@EntityListeners(PostListener.class)
@JsonIgnoreProperties({ "histogram", "comments" })
@Getter
@Setter
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

	@JsonProperty("private")
	private Boolean isPrivate;

	@JsonFormat(pattern = "MM-dd-yyyy HH:mm:ss")
	private LocalDateTime created;

	@JsonFormat(pattern = "MM-dd-yyyy HH:mm:ss")
	private LocalDateTime modified;

	@ManyToOne
	@JoinColumn(name = "AUTHOR_ID", referencedColumnName = "id")
	@JsonProperty(access = Access.READ_ONLY)
	private AppUser author;

	@OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JsonProperty(access = Access.READ_ONLY)
	private Set<Block> map = new HashSet<>();

	@OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JsonProperty(access = Access.READ_ONLY)
	private Set<Comment> comments = new HashSet<>();

	@OneToMany(mappedBy = "id.post", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<NGram> ngrams = new HashSet<>();

	@OneToOne(mappedBy = "post", cascade = CascadeType.ALL) 
	private Histogram histogram;

	public Post(String title, String description, boolean isPrivate, AppUser author) {
		this.title = title;
		this.description = description;
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
		histogram = new Histogram();
	}

	public void changeLikes(boolean positive) {

		if (likes > 0) {
			likes += positive ? 1 : -1;
		} else if (likes == 0 && positive) {
			likes += 1;
		}
	}

	public void changeDislikes(boolean positive) {

		if (dislikes > 0) {
			dislikes += positive ? 1 : -1;
		} else if (dislikes == 0 && positive) {
			dislikes += 1;
		}
	}

	public void addViews() {
		views++;
	}

	public void setModifiedNow() {
		this.modified = LocalDateTime.now();
	}
	
	@JsonIgnore
	public void setAuthor(AppUser author) {
		this.author = author;
	}
	
	@JsonIgnore
	public Block getMapRoot() {
		return map.stream()
				.filter(b -> b.getIsRoot())
				.findFirst()
				.orElseThrow();
	}
	
	public void setMapToShow() {
		map = map.stream()
				.filter(b -> b.getIsRoot())
				.collect(Collectors.toSet());
	}
	
	public void setAuthorForPublicAccess() {
		author.setEmail(null);
		author.setName(null);
	}
}
