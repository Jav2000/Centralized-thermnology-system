package com.orphanet.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.orphanet.D3Objects.D3Disorder;
import com.orphanet.D3Objects.D3Gene;
import com.orphanet.D3Objects.D3Phenotype;
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
	
	public D3Disorder findSearchInformationByOrphaCode(Integer orphaCode) {
		Disorder disorder = disorderRepository.findDisorderGenesAndPhenotypesByOrphaCode(orphaCode);
		Disorder classification = disorderRepository.findDisorderPreferentialClassification(orphaCode);
		Disorder disorderHierarchy = disorderRepository.findDisorderAscendantsAndDescendants(orphaCode);
		
		D3Disorder result = convertToD3DisorderHierarchy(disorderHierarchy, disorder.getGenes(), disorder.getPhenotypes());
		if(classification != null) {
			result.setPreferentialClassification(classification.getName());
		}
		return result;
	}
	
	public D3Disorder findSearchInformationByNameOrSynonym(String nameOrSynonym) {
		Disorder disorder = disorderRepository.findDisorderGenesAndPhenotypesByNameOrSynonym(nameOrSynonym);
		Disorder classification = disorderRepository.findDisorderPreferentialClassification(disorder.getOrphaCode());
		Disorder disorderHierarchy = disorderRepository.findDisorderAscendantsAndDescendants(disorder.getOrphaCode());
		D3Disorder result = convertToD3DisorderHierarchy(disorderHierarchy, disorder.getGenes(), disorder.getPhenotypes());
		if(classification != null) {
			result.setPreferentialClassification(classification.getName());
		}
		return result;
	}
	
	private D3Disorder convertToD3DisorderHierarchy(Disorder disorder, List<DisorderGeneRelation> disorderGeneRelations, List<DisorderPhenotypeRelation> disorderPhenotypeRelations) {
		D3Disorder d3Disorder = new D3Disorder(	disorder.getOrphaCode(), disorder.getName(), 
												disorder.getGroup(), disorder.getType(), disorder.getDescription(), 
												disorder.getSynonyms(), disorder.getOMIM(), disorder.getAverageAgeOfOnset(), 
												disorder.getAverageAgeOfDeath(), disorder.getInheritanceTypes(), null);
		
		List<D3Disorder> ascendants = new ArrayList<D3Disorder>();
		for(int i = 0; i < disorder.getAscendants().size(); i++) {
			ParentRelation disorderRelation = disorder.getAscendants().get(i);
			D3Disorder asc = new D3Disorder(	disorderRelation.getSon().getOrphaCode(), disorderRelation.getSon().getName(), 
												disorderRelation.getSon().getGroup(), disorderRelation.getSon().getType(), 
												disorderRelation.getSon().getDescription(), disorderRelation.getSon().getSynonyms(),
												disorderRelation.getSon().getOMIM(), disorderRelation.getSon().getAverageAgeOfOnset(),
												disorderRelation.getSon().getAverageAgeOfDeath(), disorderRelation.getSon().getInheritanceTypes(),
												disorderRelation.getType());
			ascendants.add(asc);
		}
		d3Disorder.setAscendants(ascendants);
		
		List<D3Disorder> descendants  = new ArrayList<D3Disorder>();
		for(int i = 0; i < disorder.getDescendants().size(); i++) {
			ParentRelation disorderRelation = disorder.getDescendants().get(i);
			D3Disorder desc = new D3Disorder(	disorderRelation.getSon().getOrphaCode(), disorderRelation.getSon().getName(), 
												disorderRelation.getSon().getGroup(), disorderRelation.getSon().getType(), 
												disorderRelation.getSon().getDescription(), disorderRelation.getSon().getSynonyms(),
												disorderRelation.getSon().getOMIM(), disorderRelation.getSon().getAverageAgeOfOnset(),
												disorderRelation.getSon().getAverageAgeOfDeath(), disorderRelation.getSon().getInheritanceTypes(),
												disorderRelation.getType());
			descendants.add(desc);
		}
		d3Disorder.setDescendants(descendants);
		
		d3Disorder.setGenes(toD3GenesList(disorderGeneRelations));
		d3Disorder.setPhenotypes(toD3PhenotypesList(disorderPhenotypeRelations));
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
