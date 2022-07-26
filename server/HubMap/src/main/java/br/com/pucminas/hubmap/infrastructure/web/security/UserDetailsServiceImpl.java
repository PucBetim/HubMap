package br.com.pucminas.hubmap.infrastructure.web.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.pucminas.hubmap.domain.user.AppUser;
import br.com.pucminas.hubmap.domain.user.AppUserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService{

	private AppUserRepository appUserRepository;
	
	public UserDetailsServiceImpl(AppUserRepository appUserRepository) {
		this.appUserRepository = appUserRepository;
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		AppUser appUser = appUserRepository.findByEmail(username);
		
		if(appUser == null) {
			throw new UsernameNotFoundException("O usuário não foi encontrado no sistema");
		}
		
		return new UserDetailsImpl(appUser);
	}

}
