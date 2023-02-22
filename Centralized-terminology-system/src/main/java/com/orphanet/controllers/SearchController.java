package com.orphanet.controllers;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
	public String getEntity(@RequestParam(value =  "input") String input, Model model) throws IOException {
		model.addAttribute("entityNames", disorderService.findAllDisorderNamesAndSynonyms());
		model.addAttribute("disorder", disorderService.findSearchInformationByNameOrSynonym(input));
		return "search.html";
	}
}
