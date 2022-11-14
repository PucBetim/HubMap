package br.com.pucminas.hubmap.domain.map;

import java.io.Serializable;

import javax.persistence.Embeddable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@SuppressWarnings("serial")
@Getter
@Setter
@NoArgsConstructor
@Embeddable
public class BlockSize implements Serializable{
	
	private Integer width;
	private Integer height;
}
