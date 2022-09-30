package br.com.pucminas.hubmap.application.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.pucminas.hubmap.domain.map.Block;
import br.com.pucminas.hubmap.domain.map.BlockRepository;

@Service
public class BlockService {

	@Autowired
	private BlockRepository blockRepository;
	
	@Transactional
	public Block save(Block block) {
		return blockRepository.save(block);
	}
}
