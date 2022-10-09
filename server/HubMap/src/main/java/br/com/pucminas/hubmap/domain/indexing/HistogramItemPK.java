package br.com.pucminas.hubmap.domain.indexing;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class HistogramItemPK implements Serializable{

	private Integer id;
	
	private Histogram owner; 
}
