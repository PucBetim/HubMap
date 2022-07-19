package br.com.pucminas.hubmap.domain.post;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Embeddable
public class NGramPK implements Serializable{
	
	@NotNull
	@ManyToOne
	private Post post;
	
	@NotNull
	private Integer sequence;
}
