package com.orphanet.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.orphanet.neo4j.data.nodes.Neo4jRoot;
import com.orphanet.repositories.RootRepository;

@Service
public class RootService {

	@Autowired
	private RootRepository rootRepository;
	
	public Neo4jRoot findRoot() {
		return rootRepository.findRoot();
	}
	
	public Neo4jRoot findRootClassifications() {
		return rootRepository.findRootClassifications();
	}
}
