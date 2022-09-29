package br.com.pucminas.hubmap.domain.indexing;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import br.com.pucminas.hubmap.domain.post.Post;
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
	@ManyToOne(cascade = CascadeType.ALL)
	private Post post;
	
	@NotNull
	private Integer sequence;
}
