package br.com.pucminas.hubmap.infrastructure.web.security;

import static br.com.pucminas.hubmap.utils.LoggerUtils.getLoggerFromClass;

import java.io.IOException;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.pucminas.hubmap.domain.user.AppUser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private AuthenticationManager authenticationManager;

	public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		
		ObjectMapper mapper = new ObjectMapper();
		AppUser appUser = null;
		
		try {
			appUser = mapper.readValue(request.getInputStream(), AppUser.class);
			UsernamePasswordAuthenticationToken upat = new UsernamePasswordAuthenticationToken(appUser.getEmail(), appUser.getPassword());
			return authenticationManager.authenticate(upat);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (AuthenticationException e) {
			getLoggerFromClass(JWTAuthenticationFilter.class).warn("Authentication Failed. User E-mail: " + appUser.getEmail());
			throw new RuntimeException(e);
		}
	}
	
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		
		UserDetailsImpl userDetails = (UserDetailsImpl) authResult.getPrincipal();
		
		String jwtToken = Jwts.builder()
				.setSubject(userDetails.getUsername())
				.setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
				.claim("nick", userDetails.getNick())
				.signWith(SignatureAlgorithm.HS512, SecurityConstants.SECRET_KEY)
				.compact();
		
		response.addHeader(SecurityConstants.AUTHORIZATION_HEADER, SecurityConstants.TOKEN_PREFIX + jwtToken);
	}

}
