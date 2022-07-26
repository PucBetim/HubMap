package br.com.pucminas.hubmap.test;

import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import br.com.pucminas.hubmap.domain.comment.Comment;
import br.com.pucminas.hubmap.domain.comment.CommentRepository;
import br.com.pucminas.hubmap.domain.post.Post;
import br.com.pucminas.hubmap.domain.post.PostRepository;
import br.com.pucminas.hubmap.domain.user.AppUser;
import br.com.pucminas.hubmap.domain.user.AppUserRepository;

@Component
public class InsertDataForTesting {
	
	private AppUserRepository appUserRepository;
	private PostRepository postRepository;
	private CommentRepository commentRepository;

	public InsertDataForTesting(AppUserRepository appUserRepository, PostRepository postRepository, CommentRepository commentRepository) {
		this.appUserRepository = appUserRepository;
		this.postRepository = postRepository;
		this.commentRepository = commentRepository;
	}

	@EventListener
	public void onApplicationEvent(ContextRefreshedEvent event) {
		
		AppUser u1 = new AppUser("John D Jackson", "Jonny", "1jonny.jpg" , "john@teste.com", "abc");
		AppUser u2 = new AppUser("Margaret Johnson", "Meggy", "2meggy.jpg" , "margaret@teste.com", "abc");
		AppUser u3 = new AppUser("Giovan del Silva", null, null, "giovan@teste.com", "abc");
		
		appUserRepository.save(u1);
		appUserRepository.save(u2);
		appUserRepository.save(u3);
		
		//USER 1
		Post p1 = new Post("Arquitetura de Computadores", "Mapa mental sobre arquitetura de computadores", null, true, u1);
		Post p2 = new Post("Virtualização", "Mapa mental sobre virtualização", null, false, u1);
		Post p3 = new Post("Biologia | Plantas", "Mapa mental sobre plantas", null, false, u1);
		
		postRepository.save(p1);
		postRepository.save(p2);
		postRepository.save(p3);
				
		Comment c1 = new Comment(p1, "Muito bom!", u1);
		Comment c2 = new Comment(p3, "Bacana", u2);
		Comment c3 = new Comment(p2, "Você consegue fazer um pouco melhor, tente adicionar mais imagens", u3);
		Comment c4 = new Comment(p1, "Pode dar entrevista já", u3, c1);
		Comment c5 = new Comment(p1, "ótimo", u2, c4);
		
		commentRepository.save(c1);
		commentRepository.save(c2);
		commentRepository.save(c3);
		commentRepository.save(c4);
		commentRepository.save(c5);
		
		//USER 2
		Post p4 = new Post("Geografia moderna", "Mapa mental sobre geografia moderna", null, false, u2);
		Post p5 = new Post("Geopolítica da Russia", "Mapa mental sobre geopolítica", null, false, u2);
		Post p6 = new Post("Biologia | Animais", "Mapa mental sobre animais", null, true, u2);
		
		postRepository.save(p4);
		postRepository.save(p5);
		postRepository.save(p6);
		
		Comment c6 = new Comment(p4, "Muito bom!", u3);
		Comment c7 = new Comment(p5, "Bacana", u1);
		Comment c8 = new Comment(p6, "Você consegue fazer um pouco melhor, tente adicionar mais imagens", u3);
		Comment c9 = new Comment(p4, "Pode dar entrevista já", u3, c6);
		Comment c10 = new Comment(p4, "ótimo", u1, c7);

		commentRepository.save(c6);
		commentRepository.save(c7);
		commentRepository.save(c8);
		commentRepository.save(c9);
		commentRepository.save(c10);
		
		//USER 3
		Post p7 = new Post("Servidores físicos", "Mapa mental sobre servidores", null, true, u3);
		Post p8 = new Post("Redes de computadores", "Mapa mental sobre redes", null, true, u3);
		Post p9 = new Post("Business Inteligence", "Mapa mental sobre BI", null, false, u3);
		
		postRepository.save(p7);
		postRepository.save(p8);
		postRepository.save(p9);
		
		Comment c11 = new Comment(p8, "Muito bom!", u1);
		Comment c12 = new Comment(p8, "Bacana", u2, c11);
		Comment c13 = new Comment(p9, "Você consegue fazer um pouco melhor, tente adicionar mais imagens", u1);
		Comment c14 = new Comment(p8, "Pode dar entrevista já?", u3, c11);
		Comment c15 = new Comment(p8, "ótimo", u2, c14);
		
		commentRepository.save(c11);
		commentRepository.save(c12);
		commentRepository.save(c13);
		commentRepository.save(c14);
		commentRepository.save(c15);
	}
	
}
