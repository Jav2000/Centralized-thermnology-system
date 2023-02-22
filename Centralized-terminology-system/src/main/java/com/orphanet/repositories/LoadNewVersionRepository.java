package com.orphanet.repositories;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import com.orphanet.neo4j.data.nodes.Disorder;

@Repository
public interface LoadNewVersionRepository extends Neo4jRepository<Disorder, Long>{

	@Query(	  "LOAD CSV WITH HEADERS FROM 'file:///Orphanet_disorders.csv' as row \n"
			+ "FIELDTERMINATOR '|'\n"
			+ "WITH 	toInteger(row.OrphaCode) as orphaCode, \n"
			+ "	row.Name as name,\n"
			+ "	row.Group as group,\n"
			+ "	row.Type as type,\n"
			+ " row.Description as description,\n"
			+ "	toInteger(row.OMIM) as OMIM\n"
			+ "MERGE (:Disorder {orphaCode: orphaCode, name: name, group: group, type: type, description: coalesce(description, ''), OMIM: coalesce(OMIM, 0)});")
	void loadNewDisorders();
	
	@Query(	  "LOAD CSV WITH HEADERS FROM 'file:///Orphanet_disorders_synonyms.csv' as row\n"
			+ "FIELDTERMINATOR '|'\n"
			+ "WITH	toInteger(row.OrphaCode) as orphaCode,\n"
			+ "	row.Synonym as synonym\n"
			+ "MATCH (n:Disorder {orphaCode: orphaCode})\n"
			+ "SET n.synonyms = \n"
			+ "CASE \n"
			+ "	WHEN n.synonyms IS NULL THEN [synonym] \n"
			+ "	ELSE n.synonyms + synonym\n"
			+ "END")
	void loadNewDisordersSynonyms();
	
	@Query(	  "LOAD CSV WITH HEADERS FROM 'file:///Orphanet_disorders_flags.csv' as row\n"
			+ "FIELDTERMINATOR '|'\n"
			+ "WITH toInteger(row.OrphaCode) as orphaCode,\n"
			+ "	row.Flag as flag\n"
			+ "MATCH (n:Disorder {orphaCode: orphaCode})\n"
			+ "SET n.flags = \n"
			+ "CASE \n"
			+ "	WHEN n.flags IS NULL THEN [flag] \n"
			+ "	ELSE n.flags + flag\n"
			+ "END")
	void loadNewDisordersFlags();
	
	@Query(	  "LOAD CSV WITH HEADERS FROM 'file:///Orphanet_disorders_natural_history.csv' as row \n"
			+ "FIELDTERMINATOR '|'\n"
			+ "WITH 	toInteger(row.OrphaCode) as orphaCode, \n"
			+ "	row.AverageAgeOfOnset as averageAgeOfOnset,\n"
			+ "	row.AverageAgeOfDeath as averageAgeOfDeath,\n"
			+ "	row.InheritanceTypes as inheritanceTypes\n"
			+ "MATCH (n:Disorder {orphaCode: orphaCode})\n"
			+ "SET n.averageAgeOfOnset = averageAgeOfOnset,\n"
			+ "    n.averageAgeOfDeath = averageAgeOfDeath,\n"
			+ "    n.inheritanceTypes = inheritanceTypes")
	void loadNewDisordersNaturalHistory();
	
	@Query(	  "LOAD CSV WITH HEADERS FROM 'file:///Orphanet_genes.csv' as row\n"
			+ "FIELDTERMINATOR '|'\n"
			+ "WITH	row.Name as name,\n"
			+ "	row.Symbol as symbol,\n"
			+ "	row.Type as type,\n"
			+ "	row.Locus as locus,\n"
			+ "	toInteger(row.OMIM) as OMIM\n"
			+ "MERGE (:Gene {name: name, symbol: symbol, type: type, locus: coalesce(locus, ''), OMIM: coalesce(OMIM, 0)})")
	void loadNewGenes();
	
	@Query(	  "LOAD CSV WITH HEADERS FROM 'file:///Orphanet_phenotypes.csv' as row\n"
			+ "FIELDTERMINATOR '|'\n"
			+ "WITH	row.HPOId as HPOId,\n"
			+ "	row.HPOTerm as term\n"
			+ "MERGE (:Phenotype {HPOId: HPOId, term: term})")
	void loadNewPhenotypes();
	
	@Query(	  "LOAD CSV WITH HEADERS FROM 'file:///Orphanet_parent_relations.csv' AS row\n"
			+ "FIELDTERMINATOR '|'\n"
			+ "WITH	toInteger(row.Parent_OrphaCode) as parent_orphacode,\n"
			+ "	toInteger(row.OrphaCode) as orphaCode\n"
			+ "MATCH (n1 {orphaCode: parent_orphacode})\n"
			+ "MATCH (n2:Disorder {orphaCode: orphaCode})\n"
			+ "MERGE (n1)-[:PARENT_RELATION {type: 'PARENT'}]->(n2)")
	void loadNewParentRelations();
	
	@Query(	  "LOAD CSV WITH HEADERS FROM 'file:///Orphanet_preferential_parent_relations.csv' as row\n"
			+ "FIELDTERMINATOR '|'\n"
			+ "WITH	toInteger(row.Preferential_Parent_OrphaCode) as preferential_parent_orphaCode,\n"
			+ "	toInteger(row.OrphaCode) as orphaCode\n"
			+ "MATCH (n1:Disorder {orphaCode: preferential_parent_orphaCode})\n"
			+ "MATCH (n2:Disorder {orphaCode: orphaCode})\n"
			+ "MERGE (n1)-[:PARENT_RELATION {type: 'PREFERENTIAL_PARENT'}]->(n2)")
	void loadNewPreferentialParentRelations();
	
	@Query(	  "LOAD CSV WITH HEADERS FROM 'file:///Orphanet_associated_with_gene_relations.csv' AS row\n"
			+ "FIELDTERMINATOR '|'\n"
			+ "WITH	toInteger(row.OrphaCode) AS orphaCode,\n"
			+ "	row.Gene_Symbol AS symbol,\n"
			+ "	row.Relationship_Type AS type,\n"
			+ "	row.Status AS status\n"
			+ "MATCH (n1 {orphaCode: orphaCode})\n"
			+ "MATCH (n2:Gene {symbol: symbol})\n"
			+ "MERGE (n2)-[:ASSOCIATED_WITH_GENE {type: type, status: status}]->(n1)")
	void loadNewAssociatedWithGeneRelations();
	
	@Query(	  "LOAD CSV WITH HEADERS FROM 'file:///Orphanet_associated_with_phenotype_relations.csv' as row\n"
			+ "FIELDTERMINATOR '|'\n"
			+ "WITH	toInteger(row.OrphaCode) as orphaCode,\n"
			+ "	row.HPOId as hpoId,\n"
			+ "	row.Frequency as frequency,\n"
			+ "	row.Criteria as criteria\n"
			+ "MATCH (n1:Disorder {orphaCode: orphaCode})\n"
			+ "MATCH (n2:Phenotype {id: hpoId})\n"
			+ "MERGE (n1)-[:ASSOCIATED_WITH_PHENOTYPE {frequency: frequency, criteria: coalesce(criteria, \"\")}]->(n2)")
	void loadNewAssociatedWithPhenotypeRelations();
}
