package br.com.pucminas.hubmap.utils;

import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {
	
	public static String getLoggedUserEmail() {
		return SecurityContextHolder.getContext().getAuthentication().getName();
	}
}
