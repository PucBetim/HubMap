package br.com.pucminas.hubmap.domain.user;

import javax.persistence.PrePersist;

import org.springframework.stereotype.Component;

@Component
public class AppUserListener {
	
	@PrePersist
	public void onPrePersistAppUser(AppUser appUser) {
		
		if(appUser.getNick() == null) {
			appUser.createNickFromName();
		}
		
		//TODO appUser.encryptPassword();
	}
}
