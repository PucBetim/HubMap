package br.com.pucminas.hubmap.domain.user;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Entity
@Table(name = "APP_USER")
@Getter
@NoArgsConstructor
public class AppUser implements Serializable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@NotBlank(message = "Por favor, informe seu nome.")
	@Column(length = 80, nullable = false)
	@Size(max = 80, message = "O nome é muito grande.")
	private String name;
	
	@Column(length = 15)
	private String nick;
	
	@Column(length = 30)
	private String profilePicture;
	
	@NotBlank(message = "Por favor, informe seu e-mail.")
	@Column(length = 100, nullable = false)
	@Size(max = 100, message = "O e-mail é muito grande.")
	private String email;
	
	@NotBlank(message = "Por favor, informe sua senha.")
	@Column(length = 80, nullable = false)
	@Size(max = 80, message = "A senha deve conter até 80 caracteres.")
	private String password;

	public AppUser(String name, String nick, String profilePicture, String email, String password) {
		this.name = name;
		this.nick = nick;
		this.profilePicture = profilePicture;
		this.email = email;
		this.password = password;
	}
	
	public AppUser(String name, String email, String password) {
		
		StringBuilder sb = new StringBuilder();
		sb.append(name.substring(0, name.indexOf(" ")).trim());
		
		if(!(sb.toString().length() > 0)) {
			if(name.length() <= 15) {
				sb.append(name.substring(0, name.length() - 1));
			} else {
				sb.append(name.substring(0, 15));
			}
		}
		
		nick = sb.toString();
		this.name = name;
		this.email = email;
		//TODO Encrypt password
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
	
	public void setName(String name) {
		this.name = name;
	}
}
