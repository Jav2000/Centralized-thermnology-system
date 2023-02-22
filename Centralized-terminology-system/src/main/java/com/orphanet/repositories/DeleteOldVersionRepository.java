package com.orphanet.repositories;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import com.orphanet.neo4j.data.nodes.Disorder;

@Repository
public interface DeleteOldVersionRepository extends Neo4jRepository<Disorder, Long>{

	@Query("MATCH (n) DETACH DELETE n")
	void deleteAllGraph();
	
}
