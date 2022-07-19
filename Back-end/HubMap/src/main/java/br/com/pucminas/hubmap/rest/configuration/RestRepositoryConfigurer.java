package br.com.pucminas.hubmap.rest.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import br.com.pucminas.hubmap.domain.comment.Comment;
import br.com.pucminas.hubmap.domain.post.NGram;
import br.com.pucminas.hubmap.domain.post.Post;

@Configuration
public class RestRepositoryConfigurer implements RepositoryRestConfigurer{
	
	@Override
	public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {
		config.exposeIdsFor(Comment.class, Post.class, NGram.class);
	}
}
