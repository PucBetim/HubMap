package br.com.pucminas.hubmap.domain.post;

import javax.persistence.PrePersist;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import br.com.pucminas.hubmap.domain.user.AppUser;
import br.com.pucminas.hubmap.domain.user.AppUserRepository;

@Component
public class PostListener {
	
	private static AppUserRepository appUserRepository;
	
	@PrePersist
	public void onPrePersistPost(Post post) {
		
		if(post.getAuthor() == null) {
			post.initializePost();
			
			String email = SecurityContextHolder.getContext().getAuthentication().getName();
			AppUser appUser = appUserRepository.findByEmail(email);
			
			if(appUser == null) {
				throw new UsernameNotFoundException("O usuário com e-mail '" + email + "' não foi encontrado.");
			}
			
			post.setAuthor(appUser);
		}
	}
	
	@Autowired
	public void init(AppUserRepository appUserRepository) {
		PostListener.appUserRepository = appUserRepository;
	}
}
