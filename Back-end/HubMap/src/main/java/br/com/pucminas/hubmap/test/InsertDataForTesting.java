package br.com.pucminas.hubmap.test;

import java.util.List;

import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import br.com.pucminas.hubmap.domain.comment.Comment;
import br.com.pucminas.hubmap.domain.post.Post;
import br.com.pucminas.hubmap.domain.post.PostRepository;
import br.com.pucminas.hubmap.domain.user.AppUser;
import br.com.pucminas.hubmap.domain.user.AppUserRepository;

@Component
public class InsertDataForTesting {
	
	private AppUserRepository appUserRepository;
	private PostRepository postRepository;

	public InsertDataForTesting(AppUserRepository appUserRepository, PostRepository postRepository) {
		this.appUserRepository = appUserRepository;
		this.postRepository = postRepository;
	}

	@EventListener
	public void onApplicationEvent(ContextRefreshedEvent event) {
		
		AppUser u1 = new AppUser("John D Jackson", "Jonny", "1jonny.jpg" , "john@teste.com", "abc");
		u1.encryptPassword();
		
		AppUser u2 = new AppUser("Margaret Johnson", "Meggy", "2meggy.jpg" , "margaret@teste.com", "abc");
		u2.encryptPassword();
		
		AppUser u3 = new AppUser("Giovan del Silva", null, null, "giovan@teste.com", "abc");
		u3.encryptPassword();
		u3.createNickFromName();
		
		//USER 1
		Post p1 = new Post("Arquitetura de Computadores", "Mapa mental sobre arquitetura de computadores", null, true, u1);
		Post p2 = new Post("Virtualização", "Mapa mental sobre virtualização", null, false, u1);
		Post p3 = new Post("Biologia | Plantas", "Mapa mental sobre plantas", null, false, u1);
		
		Comment c1 = new Comment(p1, "Muito bom!", u1);
		Comment c2 = new Comment(p3, "Bacana", u2);
		Comment c3 = new Comment(p2, "Você consegue fazer um pouco melhor, tente adicionar mais imagens", u3);
		Comment c4 = new Comment(p1, "Pode dar entrevista já", u3, c1);
		Comment c5 = new Comment(p1, "ótimo", u2, c4);
		
		p1.setComments(List.of(c1, c4, c5));
		p2.setComments(List.of(c3));
		p3.setComments(List.of(c2));
		
		//USER 2
		Post p4 = new Post("Geografia moderna", "Mapa mental sobre geografia moderna", null, false, u2);
		Post p5 = new Post("Geopolítica da Russia", "Mapa mental sobre geopolítica", null, false, u2);
		Post p6 = new Post("Biologia | Animais", "Mapa mental sobre animais", null, true, u2);
		
		Comment c6 = new Comment(p4, "Muito bom!", u3);
		Comment c7 = new Comment(p5, "Bacana", u1);
		Comment c8 = new Comment(p6, "Você consegue fazer um pouco melhor, tente adicionar mais imagens", u3);
		Comment c9 = new Comment(p4, "Pode dar entrevista já", u3, c6);
		Comment c10 = new Comment(p4, "ótimo", u1, c7);
		
		p4.setComments(List.of(c6, c9, c10));
		p5.setComments(List.of(c7));
		p6.setComments(List.of(c8));
		
		//USER 3
		Post p7 = new Post("Servidores físicos", "Mapa mental sobre servidores", null, true, u3);
		Post p8 = new Post("Redes de computadores", "Mapa mental sobre redes", null, true, u3);
		Post p9 = new Post("Business Inteligence", "Mapa mental sobre BI", null, false, u3);
		
		Comment c11 = new Comment(p8, "Muito bom!", u1);
		Comment c12 = new Comment(p8, "Bacana", u2, c11);
		Comment c13 = new Comment(p9, "Você consegue fazer um pouco melhor, tente adicionar mais imagens", u1);
		Comment c14 = new Comment(p8, "Pode dar entrevista já?", u3, c11);
		Comment c15 = new Comment(p8, "ótimo", u2, c14);
		
		p8.setComments(List.of(c11, c12, c14, c15));
		p9.setComments(List.of(c13));
		
		appUserRepository.saveAll(List.of(u1, u2, u3));
		postRepository.saveAll(List.of(p1, p2, p3, p4, p5, p6, p7, p8));
	}
	
}
