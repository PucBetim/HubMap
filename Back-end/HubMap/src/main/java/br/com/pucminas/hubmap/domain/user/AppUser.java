package br.com.pucminas.hubmap.domain.user;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Entity
@Table(name = "USER")
@Getter
@NoArgsConstructor
public class AppUser implements Serializable {
	
	@Id
	@GeneratedValue
	private int id;
	
	private String nick;
	
	private String profilePicture;
	
	private String email;
	
	private String password;

	public AppUser(String nick, String profilePicture, String email, String password) {
		this.nick = nick;
		this.profilePicture = profilePicture;
		this.email = email;
		this.password = password;
	}
	
	public void setNick(String nick) {
		this.nick = nick;
	}

	public void setProfilePicture(String profilePicture) {
		this.profilePicture = profilePicture;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
}
