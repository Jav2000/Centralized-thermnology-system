package com.orphanet.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.orphanet.neo4j.data.nodes.Disorder;
import com.orphanet.repositories.DisorderRepository;

@Service
public class DisorderService {

	@Autowired
	private DisorderRepository disorderRepository;
	
	public List<Disorder> findAllDisorders(){
		return disorderRepository.findAllDisorders();
	}
	
	public Disorder findDisorderGenesAndPhenotypesByOrphaCode(Integer orphaCode) {
		return disorderRepository.findDisorderGenesAndPhenotypesByOrphaCode(orphaCode);
	}
	
	public Disorder findDisorderGenesAndPhenotypesByNameOrSynonym(String nameOrSynonym) {
		return disorderRepository.findDisorderGenesAndPhenotypesByNameOrSynonym(nameOrSynonym);
	}
	
	public Disorder findDisorderPreferentialClassification(Integer orphaCode) {
		return disorderRepository.findDisorderPreferentialClassification(orphaCode);
	}
	
	public Disorder findDisorderParentRelations(Integer orphaCode) {
		return disorderRepository.findDisorderParentRelations(orphaCode);
	}
	
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
}
