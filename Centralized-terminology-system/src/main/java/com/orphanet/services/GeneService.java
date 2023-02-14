package com.orphanet.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.orphanet.neo4j.data.nodes.Gene;
import com.orphanet.repositories.GeneRepository;

@Service
public class GeneService {

	@Autowired
	private GeneRepository geneRepository;
	
	public List<Gene> findAll(){
		return geneRepository.findAll();
	}
	
	public Gene findGeneBySymbol(String symbol) {
		return geneRepository.findGeneBySymbol(symbol);
	}
	
	public List<Gene> findGenesAssociatedToDisorder(Integer orphaCode){
		return geneRepository.findGenesAssociatedToDisorder(orphaCode);
	}	
	
	public Gene findDisordersAssociatedToGene(String symbol) {
		return geneRepository.findDisordersAssociatedToGene(symbol);
	}
}
