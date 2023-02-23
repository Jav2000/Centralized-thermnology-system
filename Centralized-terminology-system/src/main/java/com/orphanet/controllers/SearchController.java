package com.orphanet.controllers;

import java.io.IOException;

import org.neo4j.driver.exceptions.ServiceUnavailableException;
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
		try {
			model.addAttribute("entityNames", disorderService.findAllDisorderNamesAndSynonyms());
			return "searchHome.html";
		} catch(ServiceUnavailableException e) {
			model.addAttribute("error", e.getMessage());
			return "errors/errorConexionBBDD.html";
		}
	}

	@GetMapping("/search/name")
	public String getEntity(@RequestParam(value =  "input") String input, Model model) throws IOException {
		try {
			if(input == null) {
				model.addAttribute("entityNames", disorderService.findAllDisorderNamesAndSynonyms());
				model.addAttribute("error", "Orphacode no puede ser null.");
				return "search.html";
			}else {
				model.addAttribute("disorder", disorderService.findSearchInformationByNameOrSynonym(input));
				return "search.html";
			}
		} catch (ServiceUnavailableException e) {
			model.addAttribute("error", e.getMessage());
			return "errors/errorConexionBBDD.html";
		}
	}
}
