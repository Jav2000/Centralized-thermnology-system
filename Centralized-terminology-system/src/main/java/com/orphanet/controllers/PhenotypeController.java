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

import com.orphanet.services.PhenotypeService;

@Controller
public class PhenotypeController {

	@Autowired
	private PhenotypeService phenotypeService;
	
	@GetMapping("/searchPhenotype/term")
	public String getPhenotype(@RequestParam(value =  "input") String input, Model model) {
		try {
			if(input == null) {
				model.addAttribute("entityNames", phenotypeService.findAllPhenotypeTermsAndSynonyms());
				model.addAttribute("error", "Orphacode no puede ser null.");
				return "searchDisorder.html";
			}else {
				model.addAttribute("disorder", phenotypeService.findPhenotypeSearchInformationByTerm(input));
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
	
	@GetMapping("/phenotypeGraph")
	public @ResponseBody Map<String, List<Map<String, Object>>> getPhenotypeGraph(String HPOId) throws IOException{
		return phenotypeService.findPhenotypeDisordersGraph(HPOId);
	}
}
