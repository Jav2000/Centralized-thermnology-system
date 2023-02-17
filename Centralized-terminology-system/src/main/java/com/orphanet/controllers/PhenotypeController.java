package com.orphanet.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.orphanet.services.PhenotypeService;

@Controller
public class PhenotypeController {

	@Autowired
	private PhenotypeService phenotypeService;
	
	@GetMapping("/phenotypeGraph")
	public @ResponseBody Map<String, List<Map<String, Object>>> getPhenotypeGraph(String HPOId){
		return phenotypeService.findPhenotypeGraph(HPOId);
	}
}
