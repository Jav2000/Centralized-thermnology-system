package com.orphanet.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.orphanet.neo4j.data.nodes.Neo4jGene;
import com.orphanet.repositories.GeneRepository;

@Service
public class GeneService {

	@Autowired
	private GeneRepository geneRepository;
	
	public List<Neo4jGene> findAll(){
		return geneRepository.findAll();
	}
	
	public Neo4jGene findGeneBySymbol(String symbol) {
		return geneRepository.findGeneBySymbol(symbol);
	}
	
	public List<Neo4jGene> findGenesAssociatedToDisorder(Integer orphaCode){
		return geneRepository.findGenesAssociatedToDisorder(orphaCode);
	}	
	
	public Neo4jGene findDisordersAssociatedToGene(String symbol) {
		return geneRepository.findDisordersAssociatedToGene(symbol);
	}
}
