package com.orphanet.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.orphanet.neo4j.data.nodes.Phenotype;
import com.orphanet.repositories.PhenotypeRepository;

@Service
public class PhenotypeService {

	@Autowired
	private PhenotypeRepository phenotypeRepository;
	
	public Phenotype findPhenotypeByHPOId(String HPOId) {
		return phenotypeRepository.findPhenotypeByHPOId(HPOId);
	}

}
