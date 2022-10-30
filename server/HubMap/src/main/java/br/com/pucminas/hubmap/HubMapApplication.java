package br.com.pucminas.hubmap;

import static br.com.pucminas.hubmap.utils.LoggerUtils.getLoggerFromClass;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class HubMapApplication extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		getLoggerFromClass(HubMapApplication.class).info("HubMap is ready!");
		return application.sources(HubMapApplication.class);
	}
	 
	public static void main(String[] args) {
		SpringApplication.run(HubMapApplication.class, args);
		getLoggerFromClass(HubMapApplication.class).info("HubMap is ready!");
	}

}
