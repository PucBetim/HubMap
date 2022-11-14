package br.com.pucminas.hubmap.domain.post;

import java.util.Optional;

import javax.persistence.PreRemove;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.pucminas.hubmap.application.service.BlockService;
import br.com.pucminas.hubmap.domain.map.Block;

@Component
public class PostListener {

	private static BlockService blockService;

	@PreRemove
	public void onPreRemove(Post post) {

		Optional<Block> blockOpt = post.getMap()
				.stream()
				.filter(b -> b.getIsRoot() == true)
				.findFirst();

		post.getComments().clear();

		if (blockOpt.isPresent()) {
			Block block = blockOpt.get();
			blockService.dismissParentRelation(block);
			post.getMap().remove(block);
		}
	}

	@Autowired
	public void init(BlockService blockService) {
		PostListener.blockService = blockService;
	}
}
