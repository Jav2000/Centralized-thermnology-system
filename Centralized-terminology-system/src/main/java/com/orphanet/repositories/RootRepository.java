package com.orphanet.repositories;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import com.orphanet.neo4j.data.nodes.Neo4jRoot;

@Repository
public interface RootRepository extends Neo4jRepository<Neo4jRoot, Long>{
	
	@Query("MATCH (r:Root) RETURN r")
	public Neo4jRoot findRoot();
	
	@Query("MATCH path = (r:Root)-[:PARENT_RELATION*1..1]->(d:Disorder) RETURN r, collect(relationships(path)), collect(d)")
	public Neo4jRoot findRootClassifications();
}
