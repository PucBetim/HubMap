package br.com.pucminas.hubmap.domain.map;

import java.io.Serializable;

import javax.persistence.Embeddable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@SuppressWarnings("serial")
@NoArgsConstructor
@Getter
@Setter
@Embeddable
public class Position implements Serializable {
	
	private Integer x;
	private Integer y;
}
