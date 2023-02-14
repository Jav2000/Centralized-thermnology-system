package com.orphanet.repositories;

import java.util.List;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import com.orphanet.neo4j.data.nodes.Neo4jGene;

@Repository
public interface GeneRepository extends Neo4jRepository<Neo4jGene, Long>{
	
	@Query("MATCH (g:Gene {symbol: $symbol}) RETURN g")
	public Neo4jGene findGeneBySymbol(String symbol);
	
	@Query("MATCH path = (g:Gene)<-[:ASSOCIATED_WITH_GENE]-(d:Disorder {orphaCode: $orphaCode}) RETURN g, collect(relationships(path)), collect(d)")
	public List<Neo4jGene> findGenesAssociatedToDisorder(Integer orphaCode);
	
	@Query("MATCH path = (g:Gene {symbol: $symbol})<-[:ASSOCIATED_WITH_GENE]-(d:Disorder) "
			+ "RETURN g, collect(relationships(path)), collect(d)")
	public Neo4jGene findDisordersAssociatedToGene(String symbol);
	
}
