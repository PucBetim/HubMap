package br.com.pucminas.hubmap.utils;

import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

public class StringUtils {
	
	public static String encrypt(String raw) {
		PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
		return encoder.encode(raw);
	}
	
	public static boolean isBlank(String str) {
		if(str == null || str.trim().length() == 0) {
			return true;
		}
		
		return false;
	}
}
