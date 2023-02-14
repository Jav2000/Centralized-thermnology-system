package com.orphanet.neo4j.data.relationships;

import org.springframework.data.neo4j.core.schema.RelationshipId;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;

import com.orphanet.neo4j.data.nodes.Neo4jDisorder;

@RelationshipProperties
public class PhenotypeDisorderRelation {

	@RelationshipId
	private Long id;
	
	private String frequency;
	
	private String criteria;
	
	@TargetNode
	private Neo4jDisorder disorder;
	
	public PhenotypeDisorderRelation(String frequency, String criteria, Neo4jDisorder disorder) {
		this.setFrequency(frequency);
		this.setCriteria(criteria);
		this.disorder = disorder;
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
	
	public Neo4jDisorder getDisorder() {
		return disorder;
	}
	
	public void setDisorder(Neo4jDisorder disorder) {
		this.disorder = disorder;
	}
}
