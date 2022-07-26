package br.com.pucminas.hubmap.infrastructure.web.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import br.com.pucminas.hubmap.domain.user.AppUser;

@SuppressWarnings("serial")
public class UserDetailsImpl implements UserDetails{

	private String username;
	private String password;
	private String nick;
	
	public UserDetailsImpl(AppUser appUser) {
		username = appUser.getEmail();
		password = appUser.getPassword();
		nick = appUser.getNick();
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return AuthorityUtils.NO_AUTHORITIES;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
	
	public String getNick() {
		return nick;
	}
}
