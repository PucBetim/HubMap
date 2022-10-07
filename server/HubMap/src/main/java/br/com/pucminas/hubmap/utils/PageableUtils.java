package br.com.pucminas.hubmap.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

public class PageableUtils {

	public static Pageable getPageableFromParameters(Integer page, Integer size, Boolean descending, String... sort) {
		
		Direction direction = Direction.ASC;
		
		if(page == null) {
			page = 0;
		}
		
		if(size == null) {
			size = Integer.MAX_VALUE;
		}
		
		if(descending != null) {
			if(descending) {
				direction = Direction.DESC;
			}
		}
		
		if (sort == null) {
			return PageRequest.of(page, size, Sort.unsorted());
		}

		return PageRequest.of(page, size, direction, sort);
	}
}
