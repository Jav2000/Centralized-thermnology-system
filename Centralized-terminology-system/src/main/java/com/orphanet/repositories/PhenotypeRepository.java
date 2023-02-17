package com.orphanet.repositories;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import com.orphanet.neo4j.data.nodes.Phenotype;

@Repository
public interface PhenotypeRepository extends Neo4jRepository<Phenotype, Long>{

	@Query("MATCH (p:Phenotype {HPOId: $HPOId}) RETURN p")
	public Phenotype findPhenotypeByHPOId(String HPOId);
	
	@Query("MATCH path = (p:Phenotype {HPOId: $HPOId})<-[:ASSOCIATED_WITH_PHENOTYPE]-(d:Disorder) "
			+ "RETURN p, collect(relationships(path)), collect(d)")
	public Phenotype findDisordersAssociatedToPhenotype(String HPOId);
	
}
