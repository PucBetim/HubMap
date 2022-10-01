package br.com.pucminas.hubmap.infrastructure.web.controller;

import java.util.NoSuchElementException;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.RepositoryConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.pucminas.hubmap.application.service.AppUserService;
import br.com.pucminas.hubmap.application.service.DuplicatedEmailException;
import br.com.pucminas.hubmap.domain.user.AppUser;
import br.com.pucminas.hubmap.domain.user.AppUserRepository;
import br.com.pucminas.hubmap.infrastructure.web.RestResponse;
import br.com.pucminas.hubmap.utils.SecurityUtils;

@RestController
@RequestMapping(path = {"/hubmap/appUsers", "/hubmap/appusers"})
public class AppUserController {
	
	@Autowired
	private AppUserRepository appUserRepository;
	
	@Autowired
	private AppUserService appUserService;
	
	@GetMapping("")
	public ResponseEntity<AppUser> getAppUserById() {
		try {
			AppUser appUser = appUserRepository.findByEmail(SecurityUtils.getLoggedUserEmail());
			
			return new ResponseEntity<>(appUser, HttpStatus.OK);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@DeleteMapping("")
	public ResponseEntity<RestResponse> deleteAppUserById() {
		
		AppUser appUser = appUserRepository.findByEmail(SecurityUtils.getLoggedUserEmail());
		
		appUserRepository.deleteById(appUser.getId());

		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@PostMapping("")
	public ResponseEntity<RestResponse> postAppUser(@RequestBody @Valid AppUser newAppUser, Errors errors) {
		try {
			newAppUser = appUserService.save(newAppUser);
			
			RestResponse response = RestResponse.fromNormalResponse("Usuário criado com sucesso.", newAppUser.getId());
			
			return new ResponseEntity<>(response, HttpStatus.CREATED);
		} catch (DuplicatedEmailException e) {
			errors.rejectValue("email", null, e.getMessage());
			throw new RepositoryConstraintViolationException(errors);
		}
		
	}

	@PutMapping(path = "")
	public ResponseEntity<RestResponse> putAppUser(@RequestBody @Valid AppUser appUser, Errors errors) {

		try {
			
			AppUser loggedUser = appUserRepository.findByEmail(SecurityUtils.getLoggedUserEmail());
			appUser.setId(loggedUser.getId());
			
			appUser = appUserService.save(appUser);

			RestResponse response = RestResponse.fromNormalResponse("Usuário atualizado com sucesso.", appUser.getId());
			
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (DuplicatedEmailException e) {
			errors.rejectValue("email", null, e.getMessage());
			throw new RepositoryConstraintViolationException(errors);
		}

	}
	
}
