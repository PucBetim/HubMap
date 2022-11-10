package br.com.pucminas.hubmap.application.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.pucminas.hubmap.domain.map.Block;
import br.com.pucminas.hubmap.domain.map.BlockRepository;
import br.com.pucminas.hubmap.domain.post.Post;
import br.com.pucminas.hubmap.domain.post.PostRepository;

@Service
public class BlockService {

	@Autowired
	private BlockRepository blockRepository;

	@Autowired
	private PostRepository postRepository;
	
	@Autowired
	private HistogramService histogramService;

	@Transactional
	public Block save(Block block, Integer postId) {

		Post dbPost = postRepository.findById(postId).orElseThrow();
		
		Block dbBlock = null;
		
		boolean isEdit = block.getId() != null;
		
		if (isEdit) {
			
			dbBlock = dbPost.getMap()
					.stream()
					.filter(b -> b.getIsRoot() == true)
					.findFirst()
					.orElseThrow();
			
			dismissParentRelation(dbBlock);
		}

		addPostToBlockChildren(block, dbPost);
		addParentToBlockChildren(block, null);
		block.setIsRoot(true);
		
		Block blockToReturn = blockRepository.save(block);
		
		if(isEdit) {
			blockRepository.deleteAllByPostWhereIsNotRoot(postId);
		}
		
		if(!dbPost.getIsPrivate()) {
			histogramService.generateHistogram(blockToReturn, dbPost, isEdit);
		}
				
		return blockToReturn;
	}

	public void dismissParentRelation(Block parent) {
		
		if (parent.getBlocks().size() > 0) {
			for (Block child : parent.getBlocks()) {
				dismissParentRelation(child);
			}
		}
		
		parent.setParent(null);
	}

	private void addParentToBlockChildren(Block block, Block parent) {
		
		if (block.getBlocks().size() > 0) {
			for (Block child : block.getBlocks()) {
				addParentToBlockChildren(child, block);
			}
		}

		if (parent != null) {
			block.setParent(parent);
		}
	}

	private void addPostToBlockChildren(Block parent, Post post) {

		if (parent.getBlocks().size() > 0) {
			for (Block child : parent.getBlocks()) {
				addPostToBlockChildren(child, post);
			}
		}

		parent.setPost(post);
	}
}
