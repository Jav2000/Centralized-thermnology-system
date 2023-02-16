package com.orphanet.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.orphanet.services.GeneService;

@Controller
public class GenesController {
	
	@Autowired
	private GeneService geneService;
}
