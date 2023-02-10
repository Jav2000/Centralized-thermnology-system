package com.orphanet.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.gson.Gson;
import com.orphanet.neo4j.data.nodes.Disorder;
import com.orphanet.neo4j.data.relationships.DisorderGeneRelation;
import com.orphanet.neo4j.data.relationships.DisorderPhenotypeRelation;
import com.orphanet.objects.GeneWithDisorderRelation;
import com.orphanet.objects.PhenotypeWithDisorderRelation;
import com.orphanet.objects.DisorderHierarchy;
import com.orphanet.services.DisorderService;

@Controller
public class SearchController {
	
	@Autowired
	private DisorderService disorderService;
	
	@GetMapping("/search")
	public String getSearch(Model model) {
		model.addAttribute("entityNames", disorderService.findAllDisorderNamesAndSynonyms());
		return "search.html";
	}

	@GetMapping("/search/name")
	public String getEntity(@RequestParam(value =  "input") String input, Model model) {
		model.addAttribute("entityNames", disorderService.findAllDisorderNamesAndSynonyms());
		
		/* Disorder details */
		Disorder disorder = disorderService.findDisorderGenesAndPhenotypesByNameOrSynonym(input);
		model.addAttribute("disorder", disorder);
		model.addAttribute("preferentialClassification", disorderService.findDisorderPreferentialClassification(disorder.getOrphaCode()));
		Disorder disorderParentRelations = disorderService.findDisorderParentRelations(disorder.getOrphaCode());
		model.addAttribute("parentRel", disorderParentRelations);
		
		/* D3 objects */
		Gson gson = new Gson();
		model.addAttribute("disorderHierarchy", gson.toJson(convertToDisorderHierarchy(disorderParentRelations, 3)));
		model.addAttribute("disorderGenesHierarchy", gson.toJson(convertToD3Genes(disorder.getGenes())));
		model.addAttribute("disorderPhenotypesHierarchy", gson.toJson(convertToD3Phenotypes(disorder.getPhenotypes())));

		return "search.html";
	}
	
	private DisorderHierarchy convertToDisorderHierarchy(Disorder disorder, Integer max) {
		DisorderHierarchy disorderHierarchy = new DisorderHierarchy(	disorder.getOrphaCode(), disorder.getName(), 
																		disorder.getGroup(), disorder.getType());
		return convertToDisorderHierarchy(disorderHierarchy, disorder, 0, max);
	}
	
	private DisorderHierarchy convertToDisorderHierarchy(	DisorderHierarchy disorderHierarchy, Disorder disorder, 
															Integer current, Integer max) {
		current++;
		if(current != max) {
			for(int i = 0; i < disorder.getDescendants().size(); i++) {
				Disorder disorder2 = disorder.getDescendants().get(i).getSon();
				List<DisorderHierarchy> descendants = disorderHierarchy.getDescendants();
				DisorderHierarchy disorderHierarchy2 = new DisorderHierarchy(	disorder2.getOrphaCode(), disorder2.getName(), 
																				disorder2.getGroup(), disorder2.getType());
				descendants.add(disorderHierarchy2);
				disorderHierarchy.setDescendants(descendants);
				convertToDisorderHierarchy(disorderHierarchy2, disorder2, current, max);
			}
		}
		return disorderHierarchy;
	}
	
	private List<GeneWithDisorderRelation> convertToD3Genes(List<DisorderGeneRelation> disorderGeneRelations){
		List<GeneWithDisorderRelation> result = new ArrayList<GeneWithDisorderRelation>();
		for(int i = 0; i < disorderGeneRelations.size(); i++) {
			result.add(new GeneWithDisorderRelation(disorderGeneRelations.get(i).getGene().getSymbol(), disorderGeneRelations.get(i).getGene().getName(), disorderGeneRelations.get(i).getGene().getType(), disorderGeneRelations.get(i).getGene().getLocus(), disorderGeneRelations.get(i).getStatus(), disorderGeneRelations.get(i).getType()));
		}
		return result;
	}
	
	private List<PhenotypeWithDisorderRelation> convertToD3Phenotypes(List<DisorderPhenotypeRelation> disorderPhenotypeRelations){
		List<PhenotypeWithDisorderRelation> result = new ArrayList<PhenotypeWithDisorderRelation>();
		for(int i = 0; i < disorderPhenotypeRelations.size(); i++) {
			result.add(new PhenotypeWithDisorderRelation(disorderPhenotypeRelations.get(i).getPhenotype().getTerm(), disorderPhenotypeRelations.get(i).getPhenotype().getHPOId(), disorderPhenotypeRelations.get(i).getCriteria(), disorderPhenotypeRelations.get(i).getFrequency()));
		}
		return result;
	}
}
