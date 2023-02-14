package com.orphanet.neo4j.data.relationships;

import org.springframework.data.neo4j.core.schema.RelationshipId;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;

import com.orphanet.neo4j.data.nodes.Neo4jPhenotype;

@RelationshipProperties
public class DisorderPhenotypeRelation {

	@RelationshipId
	private Long id;
	
	private String frequency;
	
	private String criteria;
	
	@TargetNode
	private Neo4jPhenotype phenotype;
	
	public DisorderPhenotypeRelation(String frequency, String criteria, Neo4jPhenotype phenotype) {
		this.setFrequency(frequency);
		this.setCriteria(criteria);
		this.phenotype = phenotype;
	}

	public String getFrequency() {
		return frequency;
	}

	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}

	public String getCriteria() {
		return criteria;
	}

	public void setCriteria(String criteria) {
		this.criteria = criteria;
	}
	
	public Neo4jPhenotype getPhenotype() {
		return phenotype;
	}
	
	public void setPhenotype(Neo4jPhenotype phenotype) {
		this.phenotype = phenotype;
	}
}
