package com.orphanet.repositories;

import java.util.List;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import com.orphanet.neo4j.data.nodes.Phenotype;

@Repository
public interface PhenotypeRepository extends Neo4jRepository<Phenotype, Long>{
	
	@Query("MATCH (p:Phenotype) RETURN p")
	public List<Phenotype> findAllPhenotypes();

	@Query("MATCH (p:Phenotype {HPOId: $HPOId}) RETURN p")
	public Phenotype findPhenotypeByHPOId(String HPOId);
	
	@Query("MATCH (p:Phenotype {term: $term}) RETURN p")
	public Phenotype findPhenotypeByTerm(String term);
	
	@Query("MATCH path = (ascen:Phenotype)-[:IS_A_SUBCLASS_OF*0..1]->(p:Phenotype {term: $term})-[:IS_A_SUBCLASS_OF*0..1]->(descen:Phenotype) "
			+ "RETURN p, collect(relationships(path)), collect(ascen), collect(descen)")
	public Phenotype findPhenotypeAscendantsAndDescendantsByTerm(String term);
	
	@Query("MATCH path = (ascen:Phenotype)-[:IS_A_SUBCLASS_OF*0..1]->(p:Phenotype {HPOId: $HPOId})-[:IS_A_SUBCLASS_OF*0..1]->(descen:Phenotype) "
			+ "RETURN p, collect(relationships(path)), collect(ascen), collect(descen)")
	public Phenotype findPhenotypeAscendantsAndDescendantsByHPOId(String HPOId);
	
	@Query("MATCH path = (p:Phenotype {term: $term})<-[:ASSOCIATED_WITH_PHENOTYPE]-(d:Disorder) "
			+ "RETURN p, collect(relationships(path)), collect(d)")
	public Phenotype findDisordersAssociatedToPhenotypeByTerm(String term);
	
	@Query("MATCH path = (p:Phenotype {HPOId: $HPOId})<-[:ASSOCIATED_WITH_PHENOTYPE]-(d:Disorder) "
			+ "RETURN p, collect(relationships(path)), collect(d)")
	public Phenotype findDisordersAssociatedToPhenotypeByHPOId(String HPOId);
	
}
