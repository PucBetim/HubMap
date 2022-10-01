package br.com.pucminas.hubmap.infrastructure.web.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.pucminas.hubmap.application.service.BlockService;
import br.com.pucminas.hubmap.domain.map.Block;
import br.com.pucminas.hubmap.domain.map.BlockRepository;
import br.com.pucminas.hubmap.domain.post.Post;
import br.com.pucminas.hubmap.domain.post.PostRepository;
import br.com.pucminas.hubmap.infrastructure.web.RestResponse;

@RestController
@RequestMapping(path = { "/hubmap/blocks" })
public class BlockController {
	
	@Autowired
	private BlockService blockService;
	
	@Autowired
	private BlockRepository blockRepository;
	
	@Autowired
	private PostRepository postRepository;
	
	@GetMapping("")
	public ResponseEntity<List<Block>> getBlocksByPost(
			@RequestParam(name = "post", required = true) Integer postId) {
		
		try {
			List<Block> blocks = new ArrayList<>();
			
			Post post = postRepository.findById(postId).orElseThrow();
			
			blockRepository.findByPost(post).forEach(blocks::add);

			if (blocks.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			return new ResponseEntity<>(blocks, HttpStatus.OK);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping("")
	public ResponseEntity<RestResponse> postBlock(
			@RequestParam(name = "post", required = true) Integer postId,
			@RequestBody @Valid Block block) {
		
		try {
			Post post = postRepository.findById(postId).orElseThrow();
			
			block.setPost(post);
			block = blockService.save(block);
			
			RestResponse response = RestResponse.fromNormalResponse("Bloco criado com sucesso.", block.getId());
			
			return new ResponseEntity<>(response, HttpStatus.CREATED);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@PutMapping()
	public ResponseEntity<RestResponse> putComment(
			@RequestParam(name = "post", required = true) Integer postId,
			@RequestBody @Valid Block newBlock) {
		
		try {
			Post post = postRepository.findById(postId).orElseThrow();
			
			newBlock.setPost(post);
			newBlock = blockService.save(newBlock);
			
			RestResponse response = RestResponse.fromNormalResponse("Bloco atualizado com sucesso.", newBlock.getId());
			
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping(path = "/{id}")
	public ResponseEntity<Block> getCommentById(@PathVariable Integer id){
		try {
			Block block = blockRepository.findById(id).orElseThrow();
			
			return new ResponseEntity<>(block, HttpStatus.OK);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	/*
	@DeleteMapping("/{id}")
	public ResponseEntity<List<Comment>> deleteById(@PathVariable Integer id) {
		try {
			blockService.delete(id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		}
	}*/
}
