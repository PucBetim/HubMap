package br.com.pucminas.hubmap.domain.map;

import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@SuppressWarnings("serial")
@Entity
@Getter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Block implements Serializable {

	@EqualsAndHashCode.Include
	@EmbeddedId
	private BlockPK id;

	private String content;

	private int coordX;

	private int coordY;

	private String image;

	private Color color;

	private int fontSize;

	private String fontStyle;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "POST_BLOCK_LINKED", 
	joinColumns = {
		@JoinColumn(name = "BLOCK_ID", referencedColumnName = "post_id"),
		@JoinColumn(name = "BLOCK_SEQUENCE", referencedColumnName = "sequence")
	}, 
	inverseJoinColumns = {
		@JoinColumn(name = "LINKED_BLOCK_ID", referencedColumnName = "post_id"),
		@JoinColumn(name = "LINKED_BLOCK_SEQUENCE", referencedColumnName = "sequence")
	})
	@ToString.Exclude
	private List<Block> myBlocksLinked = new ArrayList<>();

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "POST_BLOCK_LINKED", 
		joinColumns = {
			@JoinColumn(name = "MY_LINKED_BLOCK_ID", referencedColumnName = "post_id"),
			@JoinColumn(name = "MY_LINKED_BLOCK_SEQUENCE", referencedColumnName = "sequence")
		}, 
		inverseJoinColumns = {
			@JoinColumn(name = "LINKED_BLOCK_ID", referencedColumnName = "post_id"),
			@JoinColumn(name = "LINKED_BLOCK_SEQUENCE", referencedColumnName = "sequence")
		})
	@ToString.Exclude
	private List<Block> blocksLinkedMe = new ArrayList<>();

	public Block(String content, int coordX, int coordY, String image, Color color, int fontSize, String fontStyle,
			List<Block> myBlocksLinked, List<Block> blocksLinkedMe) {
		this.content = content;
		this.coordX = coordX;
		this.coordY = coordY;
		this.image = image;
		this.color = color;
		this.fontSize = fontSize;
		this.fontStyle = fontStyle;
		this.myBlocksLinked = myBlocksLinked;
		this.blocksLinkedMe = blocksLinkedMe;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void setCoordX(int coordX) {
		this.coordX = coordX;
	}

	public void setCoordY(int coordY) {
		this.coordY = coordY;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public void setFontSize(int fontSize) {
		this.fontSize = fontSize;
	}

	public void setFontStyle(String fontStyle) {
		this.fontStyle = fontStyle;
	}

	public void setMyBlocksLinked(List<Block> myBlocksLinked) {
		this.myBlocksLinked = myBlocksLinked;
	}

	public void setBlocksLinkedMe(List<Block> blocksLinkedMe) {
		this.blocksLinkedMe = blocksLinkedMe;
	}
}
