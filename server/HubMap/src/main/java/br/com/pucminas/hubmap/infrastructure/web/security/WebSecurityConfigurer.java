package br.com.pucminas.hubmap.infrastructure.web.security;

import static br.com.pucminas.hubmap.utils.LoggerUtils.getLoggerFromClass;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import br.com.pucminas.hubmap.HubMapApplication;

@Configuration
public class WebSecurityConfigurer implements WebMvcConfigurer{
	
	
	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
		return authConfig.getAuthenticationManager();
	}
	
	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		
		AuthenticationManager authManager = authenticationManager(http.getSharedObject(AuthenticationConfiguration.class));
		
		http.csrf().disable().cors()
		.and()
			.headers().frameOptions().disable()
		.and()
			.httpBasic()
		.and()
			.authorizeRequests()
				.antMatchers("/h2-console/**").permitAll()
				.antMatchers(HttpMethod.POST, "/hubmap/appUsers").permitAll()
				.anyRequest().authenticated()
		.and()
			.addFilter(new JWTAuthenticationFilter(authManager))
			.addFilter(new JWTAuthorizationFilter(authManager))
			.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		
		getLoggerFromClass(HubMapApplication.class).info("JWT Security Settings setup... OK!");
		
		return http.build();
	}
	
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/login")
			.allowedOrigins("*")
			.allowedMethods("POST")
			.exposedHeaders(SecurityConstants.AUTHORIZATION_HEADER);

		registry.addMapping("/**")
			.allowedOrigins("*")
			.allowedMethods("GET", "POST", "PUT", "DELETE");
		
	}
}
