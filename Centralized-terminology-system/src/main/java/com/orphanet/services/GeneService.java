package com.orphanet.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.neo4j.driver.exceptions.ServiceUnavailableException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.TransientDataAccessResourceException;
import org.springframework.stereotype.Service;

import com.orphanet.neo4j.data.nodes.Gene;
import com.orphanet.neo4j.data.relationships.GeneDisorderRelation;
import com.orphanet.repositories.GeneRepository;

@Service
public class GeneService {

	@Autowired
	private GeneRepository geneRepository;
	
	public List<String> findAllGeneNames() throws IOException {
		try {
			List<Gene> genes;
			List<String> result = new ArrayList<>();
			genes = geneRepository.findAllGenes();
			for(int i = 0; i < genes.size(); i++) {
				Gene gene = genes.get(i);
				result.add(gene.getName());
			}
			return result;
		}catch (TransientDataAccessResourceException e) {
			throw new ServiceUnavailableException("Conexion con base de datos rechazada");
		}
	}
	
	public Gene findGeneSearchInformationByName(String name) throws IOException {
		try {
			return geneRepository.findDisordersAssociatedToGeneByName(name);
		}catch (TransientDataAccessResourceException e) {
			throw new ServiceUnavailableException("Conexion con base de datos rechazada");
		}
	}
	
	public Gene findGeneSearchInformationBySymbol(String symbol) throws IOException {
		try {
			return geneRepository.findDisordersAssociatedToGeneBySymbol(symbol);
		}catch (TransientDataAccessResourceException e) {
			throw new ServiceUnavailableException("Conexion con base de datos rechazada");
		}
	}
	
	public Map<String, List<Map<String, Object>>> findGeneGraphBySymbol(String symbol){
		try {
			Gene gene = geneRepository.findDisordersAssociatedToGeneBySymbol(symbol);
			
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
		}catch (TransientDataAccessResourceException e) {
			throw new ServiceUnavailableException("Conexion con base de datos rechazada");
		}
	}
}
