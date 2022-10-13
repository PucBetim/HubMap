package br.com.pucminas.hubmap.domain.indexing.search;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import br.com.pucminas.hubmap.domain.indexing.Histogram;
import br.com.pucminas.hubmap.infrastructure.web.RestResponseSearch;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonIgnoreProperties({"histogram"})
@Getter
@Setter
@NoArgsConstructor
public class Search {

	private Histogram histogram;
	
	private String search;
	
	private RestResponseSearch oldSearch;
}
