package com.orphanet.repositories;

import java.util.List;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import com.orphanet.neo4j.data.nodes.Gene;

@Repository
public interface GeneRepository extends Neo4jRepository<Gene, Long>{
	
	@Query("MATCH (g:Gene) RETURN g")
	public List<Gene> findAllGenes();
	
	@Query("MATCH (g:Gene {symbol: $symbol}) RETURN g")
	public Gene findGeneBySymbol(String symbol);
	
	@Query("MATCH (g:Gene {name: $name}) RETURN g")
	public Gene findGeneByName(String name);
	
	@Query("MATCH path = (g:Gene {name: $name})<-[:ASSOCIATED_WITH_GENE]-(d:Disorder) "
			+ "RETURN g, collect(relationships(path)), collect(d)")
	public Gene findDisordersAssociatedToGeneByName(String name);
	
	@Query("MATCH path = (g:Gene {symbol: $symbol})<-[:ASSOCIATED_WITH_GENE]-(d:Disorder) "
			+ "RETURN g, collect(relationships(path)), collect(d)")
	public Gene findDisordersAssociatedToGeneBySymbol(String symbol);
	
}
