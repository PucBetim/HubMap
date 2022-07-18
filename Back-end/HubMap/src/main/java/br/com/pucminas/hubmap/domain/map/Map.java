package br.com.pucminas.hubmap.domain.map;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Getter;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Entity
@Getter
@NoArgsConstructor
public class Map implements Serializable{
	
	@Id
	@GeneratedValue
	private int id;
	
	private Block[] roots;

	public Map(Block[] roots) {
		this.roots = roots;
	}

	public void setRoots(Block[] roots) {
		this.roots = roots;
	}
	
}
