package br.com.pucminas.hubmap.domain.indexing;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@SuppressWarnings("serial")
@Entity
@Table(indexes = {
		@Index(name = "Onwer_Key", columnList = "HISTOGRAM_ID, NGRAM_ID")})
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class HistogramItem implements Serializable{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "HISTOGRAM_ID")
	private Histogram owner;
	
	@ManyToOne
	@JoinColumn(name = "NGRAM_ID", referencedColumnName = "id")
	private NGram key;
	
	private Integer count;
	
	private Double tfidf = 0.0;
	
	private Boolean analyzed;
}
