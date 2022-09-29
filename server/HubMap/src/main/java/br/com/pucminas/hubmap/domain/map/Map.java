package br.com.pucminas.hubmap.domain.map;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import br.com.pucminas.hubmap.domain.post.Post;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Entity
@Getter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
//TODO Check if this class will have just one attribute, if the case, remove and use just Post
public class Map implements Serializable{
	
	@Id
	@Column(name = "POST_ID")
	@EqualsAndHashCode.Include
	private Integer id;
	
	@OneToOne(cascade = CascadeType.ALL)
	@MapsId
	@JoinColumn(name = "POST_ID")
	private Post post;
	
	@OneToMany(mappedBy = "id.block", cascade = CascadeType.ALL)
	private List<Block> roots = new ArrayList<>();

	public Map(List<Block> roots) {
		this.roots = roots;
	}

	public void setRoots(List<Block> roots) {
		this.roots = roots;
	}
	
}
