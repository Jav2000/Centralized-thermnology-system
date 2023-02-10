package com.orphanet.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.orphanet.neo4j.data.nodes.Root;
import com.orphanet.repositories.RootRepository;

@Service
public class RootService {

	@Autowired
	private RootRepository rootRepository;
	
	public Root findRoot() {
		return rootRepository.findRoot();
	}
	
	public Root findRootClassifications() {
		return rootRepository.findRootClassifications();
	}
}
