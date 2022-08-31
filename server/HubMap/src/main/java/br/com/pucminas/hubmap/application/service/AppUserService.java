package br.com.pucminas.hubmap.application.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.pucminas.hubmap.domain.user.AppUser;
import br.com.pucminas.hubmap.domain.user.AppUserRepository;
import br.com.pucminas.hubmap.utils.LoggerUtils;
import br.com.pucminas.hubmap.utils.StringUtils;

@Service
public class AppUserService {

	@Autowired
	private AppUserRepository appUserRepository;

	public AppUser save(AppUser appUser) throws DuplicatedEmailException {
		
		if (!validateEmail(appUser.getEmail(), appUser.getId())) {
			throw new DuplicatedEmailException("O email informado já está sendo utilizado");
		}

		if(appUser.getId() == 0) {
			
			appUser.encryptPassword();
			
			if(StringUtils.isBlank(appUser.getNick())) {
				appUser.createNickFromName();
			}
		}
	
		appUserRepository.save(appUser);
		
		return appUser;
	}

	private boolean validateEmail(String email, Integer id) {

		AppUser appUser = appUserRepository.findByEmail(email);

		if (appUser != null) {
			if (id == null) {
				return false;
			}

			if (!id.equals(appUser.getId())) {
				return false;
			}
		}

		return true;
	}
}
