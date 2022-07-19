package br.com.pucminas.hubmap.domain.map;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
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
public class BlockPK implements Serializable {

	@NotNull
	@ManyToOne
	@JoinColumn(name = "post_id")
	private Map block;

	@NotNull
	private Integer sequence;
}
