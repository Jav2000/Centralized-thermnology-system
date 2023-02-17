package com.orphanet.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.orphanet.neo4j.data.nodes.Phenotype;
import com.orphanet.neo4j.data.relationships.PhenotypeDisorderRelation;
import com.orphanet.repositories.PhenotypeRepository;

@Service
public class PhenotypeService {

	@Autowired
	private PhenotypeRepository phenotypeRepository;
	
	public Phenotype findPhenotypeByHPOId(String HPOId) {
		return phenotypeRepository.findPhenotypeByHPOId(HPOId);
	}
	
	public Phenotype findDisordersAssociatedToPhenotype(String HPOId) {
		return phenotypeRepository.findDisordersAssociatedToPhenotype(HPOId);
	}
	
	public Map<String, List<Map<String, Object>>> findPhenotypeGraph(String HPOId){
		List<Map<String, Object>> nodes = new ArrayList<>();
		List<Map<String, Object>> links = new ArrayList<>();
		
		Phenotype phenotype = phenotypeRepository.findDisordersAssociatedToPhenotype(HPOId);
		
		nodes.add(Map.of(	"id", 0, "typeOfNode", "phenotype",
							"HPOId", phenotype.getHPOId(), "term", phenotype.getTerm()));

		int currentIndex = 1;
		
		for(int i = 0; i < phenotype.getDisorders().size(); i++) {
			PhenotypeDisorderRelation disorderRel = phenotype.getDisorders().get(i);
			nodes.add(Map.of(	"id", currentIndex, "typeOfNode", "disorder", 
								"name", disorderRel.getDisorder().getName(), "orphaCode", disorderRel.getDisorder().getOrphaCode(),
								"group", disorderRel.getDisorder().getGroup(), "type", disorderRel.getDisorder().getType(),
								"OMIM", disorderRel.getDisorder().getOMIM()));
			links.add(Map.of("source", 0, "target", currentIndex, "criteria", disorderRel.getCriteria(), "frequency", disorderRel.getFrequency()));
			currentIndex++;
		}
		return Map.of("nodes", nodes, "links", links);
	}

}
