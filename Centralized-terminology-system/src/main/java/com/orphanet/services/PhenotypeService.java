package com.orphanet.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.neo4j.driver.exceptions.ServiceUnavailableException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.TransientDataAccessResourceException;
import org.springframework.stereotype.Service;

import com.orphanet.neo4j.data.nodes.Phenotype;
import com.orphanet.neo4j.data.relationships.PhenotypeDisorderRelation;
import com.orphanet.neo4j.data.relationships.PhenotypeRelation;
import com.orphanet.repositories.PhenotypeRepository;

@Service
public class PhenotypeService {

	@Autowired
	private PhenotypeRepository phenotypeRepository;
	
	public List<String> findAllPhenotypeTermsAndSynonyms() throws IOException {
		try {
			List<Phenotype> phenotypes;
			List<String> result = new ArrayList<>();
			phenotypes = phenotypeRepository.findAllPhenotypes();
			for(int i = 0; i < phenotypes.size(); i++) {
				Phenotype phenotype = phenotypes.get(i);
				result.add(phenotype.getTerm());
				if(phenotype.getSynonyms() != null && !phenotype.getSynonyms().isEmpty()) {
					for(int j= 0; j < phenotype.getSynonyms().size(); j++) {
						result.add(phenotype.getSynonyms().get(j));
					}
				}
			}
			return result;
		}catch (TransientDataAccessResourceException e) {
			throw new ServiceUnavailableException("Conexion con base de datos rechazada");
		}
	}
	
	public Phenotype findPhenotypeSearchInformationByTerm(String term) throws IOException {
		try {
			Phenotype phenotype = phenotypeRepository.findDisordersAssociatedToPhenotypeByTerm(term);
			Phenotype phenotypeHierarchy = phenotypeRepository.findPhenotypeAscendantsAndDescendantsByTerm(term);
			if(phenotypeHierarchy != null) {
				phenotype.setAscendants(phenotypeHierarchy.getAscendants());
				phenotype.setDescendants(phenotypeHierarchy.getDescendants());
			}
			return phenotype;
		}catch (TransientDataAccessResourceException e) {
			throw new ServiceUnavailableException("Conexion con base de datos rechazada");
		}
	}
	
	public Phenotype findPhenotypeSearchInformationByHPOId(String HPOId) throws IOException {
		try {
			Phenotype phenotype = phenotypeRepository.findDisordersAssociatedToPhenotypeByHPOId(HPOId);
			Phenotype phenotypeHierarchy = phenotypeRepository.findPhenotypeAscendantsAndDescendantsByHPOId(HPOId);
			if(phenotypeHierarchy != null) {
				phenotype.setAscendants(phenotypeHierarchy.getAscendants());
				phenotype.setDescendants(phenotypeHierarchy.getDescendants());
			}
			return phenotype;
		}catch (TransientDataAccessResourceException e) {
			throw new ServiceUnavailableException("Conexion con base de datos rechazada");
		}
	}
	
	public Map<String, List<Map<String, Object>>> findPhenotypeDisordersGraph(String HPOId) throws IOException{
		try {
			List<Map<String, Object>> nodes = new ArrayList<>();
			List<Map<String, Object>> links = new ArrayList<>();
			
			Phenotype phenotype = findPhenotypeSearchInformationByHPOId(HPOId);
			
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
		}catch (TransientDataAccessResourceException e) {
			throw new ServiceUnavailableException("Conexion con base de datos rechazada");
		}
	}
	
	public Map<String, List<Map<String, Object>>> findPhenotypeGraphByTerm(String term) throws IOException{
		try {
			List<Map<String, Object>> nodes = new ArrayList<>();
			List<Map<String, Object>> links = new ArrayList<>();
			
			Phenotype phenotype = findPhenotypeSearchInformationByTerm(term);
			
			nodes.add(Map.of(	"id", 0, "typeOfNode", "phenotype",
								"HPOId", phenotype.getHPOId(), "term", phenotype.getTerm()));

			int currentIndex = 1;
			for(int i = 0; i < phenotype.getDescendants().size(); i++) {
				PhenotypeRelation parentRel = phenotype.getDescendants().get(i);
				nodes.add(Map.of(	"id", currentIndex, "HPOId", parentRel.getPhenotype().getHPOId(), "term", parentRel.getPhenotype().getTerm()));
				links.add(Map.of("source", 0, "target", currentIndex));
				currentIndex++;
			}
			
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
		}catch (TransientDataAccessResourceException e) {
			throw new ServiceUnavailableException("Conexion con base de datos rechazada");
		}
	}

}
