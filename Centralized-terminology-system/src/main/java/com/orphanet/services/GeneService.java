package com.orphanet.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.orphanet.neo4j.data.nodes.Gene;
import com.orphanet.neo4j.data.relationships.GeneDisorderRelation;
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
	
	public Gene findDisordersAssociatedToGene(String symbol) {
		return geneRepository.findDisordersAssociatedToGene(symbol);
	}
	
	public Map<String, List<Map<String, Object>>> getGeneGraph(String symbol){
		Gene gene = geneRepository.findDisordersAssociatedToGene(symbol);
		
		List<Map<String, Object>> nodes = new ArrayList<>();
		List<Map<String, Object>> links = new ArrayList<>();
		
		nodes.add(Map.of(	"id", 0, "typeOfNode", "gene",
							"name", gene.getName(), "type", gene.getType(),
							"locus", gene.getLocus()));
		
		int currentIndex = 1;
		
		for(int i = 0; i < gene.getDisorders().size(); i++) {
			GeneDisorderRelation disorderRel = gene.getDisorders().get(i);
			nodes.add(Map.of(	"id", currentIndex, "typeOfNode", "disorder", 
								"name", disorderRel.getDisorder().getName(), "orphaCode", disorderRel.getDisorder().getOrphaCode(),
								"group", disorderRel.getDisorder().getGroup(), "type", disorderRel.getDisorder().getType(),
								"OMIM", disorderRel.getDisorder().getOMIM()));
			links.add(Map.of("source", 0, "target", currentIndex, "status", disorderRel.getStatus(), "type", disorderRel.getType()));
			currentIndex++;
		}
		return Map.of("nodes", nodes, "links", links);
	}
}
