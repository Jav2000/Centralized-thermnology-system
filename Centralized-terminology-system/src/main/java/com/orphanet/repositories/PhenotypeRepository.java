package com.orphanet.repositories;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import com.orphanet.neo4j.data.nodes.Neo4jPhenotype;

@Repository
public interface PhenotypeRepository extends Neo4jRepository<Neo4jPhenotype, Long>{

	@Query("MATCH (p:Phenotype {HPOId: $HPOId}) RETURN p")
	public Neo4jPhenotype findPhenotypeByHPOId(String HPOId);
	
}
