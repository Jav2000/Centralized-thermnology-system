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

import com.orphanet.services.DisorderService;

@Controller
public class DisorderController {

	@Autowired
	private DisorderService disorderService;
	
	@GetMapping("/searchDisorder/name")
	public String getDisorder(@RequestParam(value =  "input") String input, Model model) throws Exception {
		try {
			model.addAttribute("disorderNames", disorderService.findAllDisorderNamesAndSynonyms());
			if(input == null) {
				model.addAttribute("error", "Orphacode no puede ser null.");
				return "searchDisorder.html";
			}else {
				model.addAttribute("disorder", disorderService.findDisorderSearchInformationByNameOrSynonym(input));
				return "searchDisorder.html";
			}
		} catch (ServiceUnavailableException e) {
			model.addAttribute("error", e.getMessage());
			return "errors/errorConexionBBDD.html";
		}
	}
	
	@GetMapping("/searchDisorder/graph")
	public @ResponseBody Map<String, List<Map<String, Object>>> getDisorderGraph(Integer orphaCode) throws IOException{
		return disorderService.findOrphanetDisorderGraph(orphaCode);
	}
}
