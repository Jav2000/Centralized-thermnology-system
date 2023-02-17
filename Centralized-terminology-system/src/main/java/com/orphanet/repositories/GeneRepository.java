package com.orphanet.repositories;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import com.orphanet.neo4j.data.nodes.Gene;

@Repository
public interface GeneRepository extends Neo4jRepository<Gene, Long>{
	
	@Query("MATCH (g:Gene {symbol: $symbol}) RETURN g")
	public Gene findGeneBySymbol(String symbol);
	
	@Query("MATCH path = (g:Gene {symbol: $symbol})<-[:ASSOCIATED_WITH_GENE]-(d:Disorder) "
			+ "RETURN g, collect(relationships(path)), collect(d)")
	public Gene findDisordersAssociatedToGene(String symbol);
	
}
