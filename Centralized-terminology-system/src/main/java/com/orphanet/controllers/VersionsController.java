package com.orphanet.controllers;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.orphanet.services.VersionsService;

@Controller
public class VersionsController {
	
	@Autowired
	private VersionsService versionsService;
	
	@GetMapping("/versions")
	public String getVersions() {
		try {
			versionsService.getVersions();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "versions.html";
	}
}
