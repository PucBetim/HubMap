package br.com.pucminas.hubmap.domain.map;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import br.com.pucminas.hubmap.domain.post.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Entity
@Getter
@NoArgsConstructor
//TODO Check if this class will have just one attribute, if the case, remove and use just Post
public class Map implements Serializable{
	
	@Id
	@Column(name = "POST_ID")
	private int id;
	
	@OneToOne
	@MapsId
	@JoinColumn(name = "POST_ID")
	private Post post;
	
	@OneToMany(mappedBy = "id.block")
	private List<Block> roots = new ArrayList<>();

	public Map(List<Block> roots) {
		this.roots = roots;
	}

	public void setRoots(List<Block> roots) {
		this.roots = roots;
	}
	
}
