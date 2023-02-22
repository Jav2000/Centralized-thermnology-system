package com.orphanet.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.orphanet.neo4j.data.nodes.Disorder;
import com.orphanet.neo4j.data.relationships.DisorderGeneRelation;
import com.orphanet.neo4j.data.relationships.DisorderPhenotypeRelation;
import com.orphanet.neo4j.data.relationships.ParentRelation;
import com.orphanet.repositories.DisorderRepository;

@Service
public class DisorderService {

	@Autowired
	private DisorderRepository disorderRepository;
	
	public List<String> findAllDisorderNamesAndSynonyms(){
		List<Disorder> disorders = disorderRepository.findAllDisorders();
		List<String> result = new ArrayList<>();
		for(int i = 0; i < disorders.size(); i++) {
			Disorder disorder = disorders.get(i);
			result.add(disorder.getName());
			if(disorder.getSynonyms() != null && !disorder.getSynonyms().isEmpty()) {
				for(int j= 0; j < disorder.getSynonyms().size(); j++) {
					result.add(disorder.getSynonyms().get(j));
				}
			}
		}
		return result;
	}
	
	public Disorder findSearchInformationByOrphaCode(Integer orphaCode) throws IOException {
		Disorder disorder = disorderRepository.findDisorderGenesAndPhenotypesByOrphaCode(orphaCode);
		Disorder classification = disorderRepository.findDisorderPreferentialClassification(orphaCode);
		Disorder disorderHierarchy = disorderRepository.findDisorderAscendantsAndDescendants(orphaCode);
		
		disorder.setAscendants(disorderHierarchy.getAscendants());
		disorder.setDescendants(disorderHierarchy.getDescendants());
		if(classification != null) {
			disorder.setPreferentialClassification(classification.getName());
		}
		
		return disorder;
	}
	
	public Disorder findSearchInformationByNameOrSynonym(String nameOrSynonym) throws IOException {
		Disorder disorder = disorderRepository.findDisorderGenesAndPhenotypesByNameOrSynonym(nameOrSynonym);
		Disorder classification = disorderRepository.findDisorderPreferentialClassification(disorder.getOrphaCode());
		Disorder disorderHierarchy = disorderRepository.findDisorderAscendantsAndDescendants(disorder.getOrphaCode());
		
		disorder.setAscendants(disorderHierarchy.getAscendants());
		disorder.setDescendants(disorderHierarchy.getDescendants());
		if(classification != null) {
			disorder.setPreferentialClassification(classification.getName());
		}
		return disorder;
	}
	
	public Map<String, List<Map<String, Object>>> findDisorderGraph(Integer orphaCode) throws IOException{
		List<Map<String, Object>> nodes = new ArrayList<>();
		List<Map<String, Object>> links = new ArrayList<>();
		
		Disorder rootDisorder = findSearchInformationByOrphaCode(orphaCode);
		nodes.add(Map.of(	"id", 0,"typeOfNode", "disorder", "name", rootDisorder.getName(),
							"orphaCode", rootDisorder.getOrphaCode(), "type", rootDisorder.getType(),
							"group", rootDisorder.getGroup()));
		int currentIndex = 1;
		for(int i = 0; i < rootDisorder.getDescendants().size(); i++) {
			ParentRelation parentRel = rootDisorder.getDescendants().get(i);
			nodes.add(Map.of(	"id", currentIndex, "typeOfNode", "disorder", "name", parentRel.getSon().getName(),
								"orphaCode", parentRel.getSon().getOrphaCode(), "type", parentRel.getSon().getType(),
								"group", parentRel.getSon().getGroup(), "OMIM", parentRel.getSon().getOMIM()));
			links.add(Map.of("source", 0, "target", currentIndex, "type", parentRel.getType()));
			currentIndex++;
		}
		
		for(int i = 0; i < rootDisorder.getGenes().size(); i++) {
			DisorderGeneRelation geneRel = rootDisorder.getGenes().get(i);
			nodes.add(Map.of(	"id", currentIndex, "typeOfNode", "gene", "name", geneRel.getGene().getName(),
								"symbol", geneRel.getGene().getSymbol(), "type", geneRel.getGene().getType(),
								"locus", geneRel.getGene().getLocus(), "OMIM", geneRel.getGene().getOMIM()));
			links.add(Map.of("source", 0, "target", currentIndex, "status", geneRel.getStatus(), "type", geneRel.getType()));
			currentIndex++;
		}
		
		for(int i = 0; i < rootDisorder.getPhenotypes().size(); i++) {
			DisorderPhenotypeRelation phenotypeRel = rootDisorder.getPhenotypes().get(i);
			nodes.add(Map.of(	"id", currentIndex, "typeOfNode", "phenotype", "HPOId", phenotypeRel.getPhenotype().getHPOId(),
								"term", phenotypeRel.getPhenotype().getTerm()));
			links.add(Map.of("source", 0, "target", currentIndex, "criteria", phenotypeRel.getCriteria(), "frequency", phenotypeRel.getFrequency()));
			currentIndex++;
		}
		return Map.of("nodes", nodes, "links", links);
	}
}
