package br.com.pucminas.hubmap.domain.comment;

import javax.persistence.PrePersist;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import br.com.pucminas.hubmap.domain.user.AppUser;
import br.com.pucminas.hubmap.domain.user.AppUserRepository;
import br.com.pucminas.hubmap.utils.SecurityUtils;

@Component
public class CommentListener {

	
	private static AppUserRepository appUserRepository;
	
	@PrePersist
	public void onPrePersistPost(Comment comment) {
		
		if(comment.getAuthor() == null) {
			comment.initializeComment();
			
			String email = SecurityUtils.getLoggedUserEmail();
			
			AppUser appUser = appUserRepository.findByEmail(email);
			
			if(appUser == null) {
				throw new UsernameNotFoundException("O usuário com e-mail '" + email + "' não foi encontrado.");
			}
			
			comment.setAuthor(appUser);
		}
	}
	
	@Autowired
	public void init(AppUserRepository appUserRepository) {
		CommentListener.appUserRepository = appUserRepository;
	}
}
