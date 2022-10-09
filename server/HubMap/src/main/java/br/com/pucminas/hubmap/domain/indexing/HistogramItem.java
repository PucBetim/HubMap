package br.com.pucminas.hubmap.domain.indexing;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@SuppressWarnings("serial")
@Entity
@IdClass(HistogramItemPK.class)
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class HistogramItem implements Serializable{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	private Integer id;
	
	@Id
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "HISTOGRAM_ID", referencedColumnName = "id")
	@EqualsAndHashCode.Include
	private Histogram owner;
	
	@ManyToOne
	@JoinColumns({
			@JoinColumn(name = "NGRAM_ID_POST", referencedColumnName = "POST_ID"),
			@JoinColumn(name = "NGRAM_ID_SEQUENCE", referencedColumnName = "sequence")})
	private NGram key;
	
	private int count;
	
	private double tfidf;
}
