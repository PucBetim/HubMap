package br.com.pucminas.hubmap.domain.map;

import java.awt.Color;
import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Entity
@Getter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Block implements Serializable{
	
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
	
	private Block[] linkedBlocks;

	public Block(String content, int coordX, int coordY, String image, Color color, int fontSize, String fontStyle,
			Block[] linkedBlocks) {
		this.content = content;
		this.coordX = coordX;
		this.coordY = coordY;
		this.image = image;
		this.color = color;
		this.fontSize = fontSize;
		this.fontStyle = fontStyle;
		this.linkedBlocks = linkedBlocks;
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

	public void setLinkedBlocks(Block[] linkedBlocks) {
		this.linkedBlocks = linkedBlocks;
	}
}
