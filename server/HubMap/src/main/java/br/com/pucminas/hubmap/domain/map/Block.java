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
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import br.com.pucminas.hubmap.domain.post.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@SuppressWarnings("serial")
@Entity
@JsonIgnoreProperties("post")
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
		
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(name = "POST_BLOCK_LINKED", joinColumns = {
			@JoinColumn(name = "BLOCK_ID", referencedColumnName = "id")}, inverseJoinColumns = {
				@JoinColumn(name = "LINKED_BLOCK_ID", referencedColumnName = "id") })
	@ToString.Exclude
	private Set<Block> blocks = new HashSet<>();
}
