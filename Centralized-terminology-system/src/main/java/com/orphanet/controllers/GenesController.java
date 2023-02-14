package com.orphanet.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.orphanet.neo4j.data.nodes.Gene;
import com.orphanet.neo4j.data.relationships.GeneDisorderRelation;
import com.orphanet.objects.DisorderWithGeneRelation;
import com.orphanet.objects.GeneHierarchy;
import com.orphanet.services.GeneService;

@Controller
public class GenesController {
	
	@Autowired
	private GeneService geneService;
	
	@GetMapping("/geneDisorders")
	public @ResponseBody String getGeneHierarchy(@RequestParam(value = "symbol", required = true) String symbol) {
		Gson gson = new Gson();
		return gson.toJson(convertToGeneHierarchy(geneService.findDisordersAssociatedToGene(symbol)));
	}

	private GeneHierarchy convertToGeneHierarchy(Gene gene) {
		List<GeneDisorderRelation> disordersRelations = gene.getDisorders();
		GeneHierarchy result = new GeneHierarchy(gene.getSymbol(), gene.getName(), gene.getType(), gene.getLocus()); 
		List<DisorderWithGeneRelation> geneHiearchyDisorders =  result.getDisorders();
		for(int i = 0; i < disordersRelations.size(); i++) {
			geneHiearchyDisorders.add(new DisorderWithGeneRelation(	disordersRelations.get(i).getDisorder().getOrphaCode(), disordersRelations.get(i).getDisorder().getName(), 
																	disordersRelations.get(i).getDisorder().getType(), disordersRelations.get(i).getDisorder().getGroup(), 
																	disordersRelations.get(i).getStatus(), disordersRelations.get(i).getType()));
		}
		result.setDisorders(geneHiearchyDisorders);
		return result;
	}
}
