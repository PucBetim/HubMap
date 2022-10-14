package br.com.pucminas.hubmap.domain.user;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import br.com.pucminas.hubmap.utils.StringUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@SuppressWarnings("serial")
@Entity
@Table(name = "APP_USER")
@JsonIgnoreProperties({"profilePicture"})
@Getter
@Setter
@NoArgsConstructor
public class AppUser implements Serializable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
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
	@Email(message = "O e-mail não está em um formato válido.")
	private String email;
	
	@NotBlank(message = "Por favor, informe sua senha.")
	@Column(length = 80, nullable = false)
	@Size(max = 80, message = "A senha deve conter até 80 caracteres.")
	@JsonProperty(access = Access.WRITE_ONLY)
	private String password;

	public AppUser(String name, String nick, String profilePicture, String email, String password) {
		this.name = name;
		this.nick = nick;
		this.profilePicture = profilePicture;
		this.email = email;
		this.password = password;
	}
	
	public void createNickFromName() {
		
		StringBuilder sb = new StringBuilder();
		
		int endIndex = name.indexOf(" ") == -1 ? 0 : name.indexOf(" ");
		
		sb.append(name.substring(0, endIndex).trim());
		
		if(!(sb.length() > 0)) {
			if(name.length() <= 15) {
				sb.append(name.substring(0, name.length() - 1));
				sb.append(name.charAt(name.length() - 1));
			} else {
				sb.append(name.substring(0, 15));
			}
		}
		
		this.nick = sb.toString();
	}
		
	public void encryptPassword() {	
		this.password = StringUtils.encrypt(password);
	}
	
	public void setAuthorForPublicAccess() {
		email = null;
		name = null;
	}
}
