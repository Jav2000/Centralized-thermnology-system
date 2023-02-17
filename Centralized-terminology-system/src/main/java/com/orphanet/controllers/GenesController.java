package com.orphanet.controllers;


import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.orphanet.neo4j.data.nodes.Gene;
import com.orphanet.services.GeneService;

@Controller
public class GenesController {
	
	@Autowired
	private GeneService geneService;
	
	@GetMapping("/geneDisorders")
	public @ResponseBody Gene findGeneDisorders(String symbol) {
		return geneService.findDisordersAssociatedToGene(symbol);
	}
	
	@GetMapping("/geneGraph")
	public @ResponseBody Map<String, List<Map<String, Object>>> findGeneGraph(String symbol){
		return geneService.getGeneGraph(symbol);
	}
}
