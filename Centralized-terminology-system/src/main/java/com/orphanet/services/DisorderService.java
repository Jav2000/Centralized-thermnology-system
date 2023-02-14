package com.orphanet.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.orphanet.D3Objects.D3Disorder;
import com.orphanet.D3Objects.D3Gene;
import com.orphanet.D3Objects.D3Phenotype;
import com.orphanet.neo4j.data.nodes.Neo4jDisorder;
import com.orphanet.neo4j.data.relationships.DisorderGeneRelation;
import com.orphanet.neo4j.data.relationships.DisorderPhenotypeRelation;
import com.orphanet.neo4j.data.relationships.ParentRelation;
import com.orphanet.repositories.DisorderRepository;

@Service
public class DisorderService {

	@Autowired
	private DisorderRepository disorderRepository;
	
	public List<String> findAllDisorderNamesAndSynonyms(){
		List<Neo4jDisorder> disorders = disorderRepository.findAllDisorders();
		List<String> result = new ArrayList<>();
		for(int i = 0; i < disorders.size(); i++) {
			Neo4jDisorder disorder = disorders.get(i);
			result.add(disorder.getName());
			if(disorder.getSynonyms() != null && !disorder.getSynonyms().isEmpty()) {
				for(int j= 0; j < disorder.getSynonyms().size(); j++) {
					result.add(disorder.getSynonyms().get(j));
				}
			}
		}
		return result;
	}
	
	public D3Disorder findSearchInformationByOrphaCode(Integer orphaCode) {
		Neo4jDisorder disorder = disorderRepository.findDisorderGenesAndPhenotypesByOrphaCode(orphaCode);
		Neo4jDisorder classification = disorderRepository.findDisorderPreferentialClassification(orphaCode);
		Neo4jDisorder disorderHierarchy = disorderRepository.findDisorderAscendantsAndDescendants(orphaCode);
		
		D3Disorder result = convertToD3DisorderHierarchy(disorderHierarchy, disorder.getGenes(), disorder.getPhenotypes());
		if(classification != null) {
			result.setPreferentialClassification(classification.getName());
		}
		return result;
	}
	
	public D3Disorder findSearchInformationByNameOrSynonym(String nameOrSynonym) {
		Neo4jDisorder disorder = disorderRepository.findDisorderGenesAndPhenotypesByNameOrSynonym(nameOrSynonym);
		Neo4jDisorder classification = disorderRepository.findDisorderPreferentialClassification(disorder.getOrphaCode());
		Neo4jDisorder disorderHierarchy = disorderRepository.findDisorderAscendantsAndDescendants(disorder.getOrphaCode());
		D3Disorder result = convertToD3DisorderHierarchy(disorderHierarchy, disorder.getGenes(), disorder.getPhenotypes());
		if(classification != null) {
			result.setPreferentialClassification(classification.getName());
		}
		return result;
	}
	
	private D3Disorder convertToD3DisorderHierarchy(Neo4jDisorder disorder, List<DisorderGeneRelation> disorderGeneRelations, List<DisorderPhenotypeRelation> disorderPhenotypeRelations) {
		D3Disorder d3Disorder = new D3Disorder(	disorder.getOrphaCode(), disorder.getName(), 
																		disorder.getGroup(), disorder.getType(), disorder.getDescription(), null);
		D3Disorder result = convertToD3DisorderHierarchy(d3Disorder, disorder, 0);
		result.setGenes(toD3GenesList(disorderGeneRelations));
		result.setPhenotypes(toD3PhenotypesList(disorderPhenotypeRelations));
		return result;
	}
	
	private D3Disorder convertToD3DisorderHierarchy(	D3Disorder d3Disorder, Neo4jDisorder disorder, 
													Integer current) {
		current++;
		for(int i = 0; i < disorder.getDescendants().size(); i++) {
			ParentRelation disorderRelation = disorder.getDescendants().get(i);
			List<D3Disorder> descendants = d3Disorder.getDescendants();
			D3Disorder d3Disorder2 = new D3Disorder(	disorderRelation.getSon().getOrphaCode(), disorderRelation.getSon().getName(), 
														disorderRelation.getSon().getGroup(), disorderRelation.getSon().getType(), 
														disorderRelation.getSon().getDescription(), disorderRelation.getType());
			descendants.add(d3Disorder2);
			d3Disorder.setDescendants(descendants);
			convertToD3DisorderHierarchy(d3Disorder2, disorderRelation.getSon(), current);
		}
		return d3Disorder;
	}
	
	private List<D3Gene> toD3GenesList(List<DisorderGeneRelation> disorderGeneRelations){
		List<D3Gene> result = new ArrayList<D3Gene>();
		for(int i = 0; i < disorderGeneRelations.size(); i++) {
			DisorderGeneRelation dgr = disorderGeneRelations.get(i);
			result.add(new D3Gene(	dgr.getGene().getSymbol(), dgr.getGene().getName(), dgr.getGene().getType(), 
									dgr.getGene().getLocus(), dgr.getGene().getOMIM(), dgr.getStatus(), dgr.getType()));
		}
		return result;
	}
	
	private List<D3Phenotype> toD3PhenotypesList(List<DisorderPhenotypeRelation> disorderPhenotypeRelations){
		List<D3Phenotype> result = new ArrayList<D3Phenotype>();
		for(int i = 0; i < disorderPhenotypeRelations.size(); i++) {
			DisorderPhenotypeRelation dpr = disorderPhenotypeRelations.get(i);
			result.add(new D3Phenotype(dpr.getPhenotype().getHPOId(), dpr.getPhenotype().getTerm(), dpr.getCriteria(), dpr.getFrequency()));
		}
		return result;
	}
}
