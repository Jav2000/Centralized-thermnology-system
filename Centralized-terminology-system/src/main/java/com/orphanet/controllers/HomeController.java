package com.orphanet.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.orphanet.services.DisorderService;

@Controller
public class HomeController {
	
	@Autowired
	private DisorderService disorderService;
	
	@GetMapping(value = {"/home", "/"})
	public String getHome(Model model) {
		try {
			model.addAttribute("entityNames", disorderService.findAllDisorderNamesAndSynonyms());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "home.html";
	}
}
