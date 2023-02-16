package com.orphanet.repositories;

import java.util.List;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import com.orphanet.neo4j.data.nodes.Disorder;

@Repository
public interface DisorderRepository extends Neo4jRepository<Disorder, Long>{
	
	@Query("MATCH (d:Disorder) RETURN d")
	public List<Disorder> findAllDisorders();
	
	@Query("MATCH path = (p)<-[:ASSOCIATED_WITH_PHENOTYPE*0..1]-(d:Disorder {orphaCode: $orphaCode})-[:ASSOCIATED_WITH_GENE*0..1]->(g) "
			+ "RETURN d, collect(relationships(path)), collect(g), collect(p)")
	public Disorder findDisorderGenesAndPhenotypesByOrphaCode(Integer orphaCode);
	
	@Query("MATCH path = (p)<-[:ASSOCIATED_WITH_PHENOTYPE*0..1]-(d:Disorder)-[:ASSOCIATED_WITH_GENE*0..1]->(g) "
			+ "WHERE d.name = $nameOrSynonym OR $nameOrSynonym in d.synonyms RETURN d, collect(relationships(path)), collect(g), collect(p)")
	public Disorder findDisorderGenesAndPhenotypesByNameOrSynonym(String nameOrSynonym);
	
	@Query("MATCH path = (:Disorder {orphaCode: $orphaCode})<-[:PARENT_RELATION*]-(d:Disorder)<-[:PARENT_RELATION]-(:Root) "
			+ "RETURN d ORDER BY length(path) ASC LIMIT 1")
	public Disorder findDisorderPreferentialClassification(Integer orphaCode);

	@Query("MATCH path = (ascen:Disorder)-[:PARENT_RELATION*0..1]->(d:Disorder {orphaCode: $orphaCode})-[:PARENT_RELATION*0..1]->(descen:Disorder) "
			+ "RETURN d, collect(relationships(path)), collect(ascen), collect(descen)")
	public Disorder findDisorderAscendantsAndDescendants(Integer orphaCode);
	
}
