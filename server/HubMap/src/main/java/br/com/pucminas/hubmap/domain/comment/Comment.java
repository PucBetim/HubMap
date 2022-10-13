package br.com.pucminas.hubmap.domain.comment;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import br.com.pucminas.hubmap.domain.post.Post;
import br.com.pucminas.hubmap.domain.user.AppUser;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@SuppressWarnings("serial")
@Entity
@EntityListeners(CommentListener.class)
@JsonIgnoreProperties({ "post" })
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Comment implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	private Integer id;

	@Column(nullable = false)
	@NotBlank(message = "O comentário deve ser informado")
	@Size(min = 3, max = 200, message = "O comentário deve ter entre 3 e 200 caracteres")
	private String content;

	@ManyToOne
	@JoinColumn(name = "POST_ID", referencedColumnName = "id")
	private Post post;

	private Integer likes;

	private Integer dislikes;

	@OneToOne
	@JoinColumn(name = "REPLIED_TO", referencedColumnName = "id")
	private Comment repliedTo;

	@ManyToOne
	@JoinColumn(name = "AUTHOR_ID", referencedColumnName = "id")
	private AppUser author;

	@JsonFormat(pattern = "MM-dd-yyyy HH:mm:ss")
	private LocalDateTime timestamp;

	//TODO Remove all constructors
	public Comment(Post post, String content, AppUser author, Comment repliedTo) {
		this.post = post;
		this.content = content;
		this.repliedTo = repliedTo;
		this.author = author;
		initializeComment();
	}

	public Comment(Post post, String content, AppUser author) {
		this(post, content, author, null);
	}

	public void initializeComment() {
		likes = 0;
		dislikes = 0;
		setTimestampNow();
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
	
	public void setTimestampNow() {
		timestamp = LocalDateTime.now();
	}
}
