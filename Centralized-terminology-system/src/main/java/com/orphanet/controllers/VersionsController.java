package com.orphanet.controllers;

import org.springframework.web.bind.annotation.GetMapping;

public class VersionsController {
	
	@GetMapping("/versions")
	public String getVersions() {
		return "versions.html";
	}
}
