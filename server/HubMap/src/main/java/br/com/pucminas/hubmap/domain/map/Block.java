package br.com.pucminas.hubmap.domain.map;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.Size;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import br.com.pucminas.hubmap.domain.post.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@SuppressWarnings("serial")
@Entity
@JsonIgnoreProperties({ "post", "parent", "isRoot" })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Block implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "post_id", referencedColumnName = "id")
	private Post post;

	@Size(max = 300, message = "O bloco não pode conter mais que 300 caracteres.")
	private String content;

	@Embedded
	private Position position;

	@Embedded
	private BlockSize size;

	private String backgroundColor;

	private String fontColor;

	private Integer fontSize;

	private String fontStyle;

	private String fontWeight;

	private String textDecoration;

	private String textAlign;

	private String borderRadius;

	@OneToOne
	@JoinColumn(name = "BLOCK_PARENT", referencedColumnName = "id")
	private Block parent;

	private Boolean isRoot = false;

	@ManyToMany(cascade = { 
			CascadeType.DETACH, 
			CascadeType.MERGE, 
			CascadeType.PERSIST,
			CascadeType.REFRESH,
			CascadeType.REMOVE}, fetch = FetchType.EAGER)
	@JoinTable(name = "POST_BLOCK_LINKED", joinColumns = {
			@JoinColumn(name = "BLOCK_ID", referencedColumnName = "id") }, inverseJoinColumns = {
					@JoinColumn(name = "LINKED_BLOCK_ID", referencedColumnName = "id") })
	@OnDelete(action = OnDeleteAction.CASCADE)
	@ToString.Exclude
	private Set<Block> blocks = new HashSet<>();
}
