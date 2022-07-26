package br.com.pucminas.hubmap.utils;

import org.slf4j.Logger;

public final class LoggerUtils {

	public static Logger getLoggerFromClass(Class<?> clazz) {
		return org.slf4j.LoggerFactory.getLogger(clazz);
	}

}
