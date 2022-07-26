package br.com.pucminas.hubmap;

import static br.com.pucminas.hubmap.utils.LoggerUtils.getLoggerFromClass;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HubMapApplication {

	public static void main(String[] args) {
		SpringApplication.run(HubMapApplication.class, args);
		getLoggerFromClass(HubMapApplication.class).info("HubMap is ready!");
	}

}
