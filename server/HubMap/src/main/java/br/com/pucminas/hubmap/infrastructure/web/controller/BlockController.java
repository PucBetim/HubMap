package br.com.pucminas.hubmap.infrastructure.web.controller;

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
import br.com.pucminas.hubmap.infrastructure.web.RestResponse;

@RestController
@RequestMapping(path = { "/hubmap/blocks" })
public class BlockController {
	
	@Autowired
	private BlockService blockService;
	
	@Autowired
	private BlockRepository blockRepository;
	
	@GetMapping("")
	public ResponseEntity<Block> getBlocksByPost(
			@RequestParam(name = "post", required = true) Integer postId) {
		
		try {		
			Block block = blockRepository.findByPost(postId);

			if (block == null) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			return new ResponseEntity<>(block, HttpStatus.OK);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping("")
	public ResponseEntity<RestResponse> postBlock(
			@RequestParam(name = "post", required = true) Integer postId,
			@RequestBody @Valid Block block) {
		
		try {
			block = blockService.save(block, postId);
			
			RestResponse response = RestResponse.fromNormalResponse("Bloco criado com sucesso.", block.getId().toString());
			
			return new ResponseEntity<>(response, HttpStatus.CREATED);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@PutMapping("")
	public ResponseEntity<RestResponse> putBlock (
			@RequestParam(name = "post", required = true) Integer postId,
			@RequestBody @Valid Block newBlock) {
		
		try {
			newBlock = blockService.save(newBlock, postId);
			
			RestResponse response = RestResponse.fromNormalResponse("Bloco atualizado com sucesso.", newBlock.getId().toString());
			
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping(path = "/{id}")
	public ResponseEntity<Block> getBlockById(@PathVariable Integer id){
		try {
			Block block = blockRepository.findById(id).orElseThrow();
			
			return new ResponseEntity<>(block, HttpStatus.OK);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
}
