package com.orphanet.controllers;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.orphanet.services.DisorderService;

@Controller
public class DisorderController {

	@Autowired
	private DisorderService disorderService;
	
	@GetMapping("/disorderGraph")
	public @ResponseBody Map<String, List<Map<String, Object>>> getDisorderGraph(Integer orphaCode) throws IOException{
		return disorderService.findDisorderGraph(orphaCode);
	}
}
