package com.orphanet.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.orphanet.services.DisorderService;
import com.orphanet.services.GeneService;
import com.orphanet.services.PhenotypeService;

@Controller
public class HomeController {
	
	@Autowired
	private DisorderService disorderService;
	
	@Autowired
	private GeneService geneService;
	
	@Autowired
	private PhenotypeService phenotypeService;
	
	@GetMapping(value = {"/home", "/"})
	public String getHome() {
		return "home.html";
	}
	
	@GetMapping(value = "/searchDisorder")
	public String getSearchDisorderHome(Model model) {
		try {
			model.addAttribute("disorderNames", disorderService.findAllDisorderNamesAndSynonyms());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "searchDisorderHome.html";
	}
	
	@GetMapping(value = "/searchGene")
	public String getSearchGeneHome(Model model) {
		try {
			model.addAttribute("geneNames", geneService.findAllGeneNames());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "searchGeneHome.html";
	}
	@GetMapping(value = "/searchPhenotype")
	public String getSearchPhenotypeHome(Model model) {
		try {
			model.addAttribute("phenotypeTerms", phenotypeService.findAllPhenotypeTermsAndSynonyms());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "searchphenotypeHome.html";
	}
}
