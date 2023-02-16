package com.orphanet.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.orphanet.neo4j.data.nodes.Disorder;
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
		Disorder disorder = disorderService.findSearchInformationByNameOrSynonym(input);
		model.addAttribute("disorder", disorder);
		Map<String, List<Map<String, Object>>> disorderGraph = disorderService.getDisorderGraph(disorder.getOrphaCode());
		model.addAttribute("disorderGraph", disorderGraph);
		return "search.html";
	}
}
