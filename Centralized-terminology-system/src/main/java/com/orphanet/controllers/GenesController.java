package com.orphanet.controllers;


import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.neo4j.driver.exceptions.ServiceUnavailableException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.orphanet.neo4j.data.nodes.Gene;
import com.orphanet.services.GeneService;

@Controller
public class GenesController {
	
	@Autowired
	private GeneService geneService;
	
	@GetMapping("/searchGene/name")
	public String getGene(@RequestParam(value =  "input") String input, Model model) {
		try {
			if(input == null) {
				model.addAttribute("entityNames", geneService.findAllGeneNames());
				model.addAttribute("error", "Orphacode no puede ser null.");
				return "searchDisorder.html";
			}else {
				model.addAttribute("disorder", geneService.findGeneSearchInformationByName(input));
				return "searchDisorder.html";
			}
		} catch (ServiceUnavailableException e) {
			model.addAttribute("error", e.getMessage());
			return "errors/errorConexionBBDD.html";
		} catch (IOException e) {
			model.addAttribute("error", e.getMessage());
			return "errors/errorConexionBBDD.html";
		}
	}
	
	@GetMapping("/geneDisorders")
	public @ResponseBody Gene findGeneDisorders(String symbol) throws IOException {
		return geneService.findGeneSearchInformationBySymbol(symbol);
	}
	
	@GetMapping("/geneGraph")
	public @ResponseBody Map<String, List<Map<String, Object>>> getGeneGraph(String symbol){
		return geneService.findGeneGraphBySymbol(symbol);
	}
}
