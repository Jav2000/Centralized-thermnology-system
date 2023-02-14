package com.orphanet.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.orphanet.D3Objects.D3Disorder;
import com.orphanet.services.DisorderService;

@Controller
public class SearchController {
	
	@Autowired
	private DisorderService disorderService;
	
	@GetMapping("/search")
	public String getSearch(Model model) {
		/* Lista de nombres de entidades y sinónimos */
		model.addAttribute("entityNames", disorderService.findAllDisorderNamesAndSynonyms());
		
		return "search.html";
	}

	@GetMapping("/search/name")
	public String getEntity(@RequestParam(value =  "input") String input, Model model) {
		/* Lista de nombres de entidades y sinónimos */
		model.addAttribute("entityNames", disorderService.findAllDisorderNamesAndSynonyms());
		
		/* Detalles entidad */
		D3Disorder disorder = disorderService.findSearchInformationByNameOrSynonym(input);
		model.addAttribute("disorder", disorder);
		
		return "search.html";
	}
}
