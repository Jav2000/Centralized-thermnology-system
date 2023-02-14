package com.orphanet.neo4j.data.relationships;

import org.springframework.data.neo4j.core.schema.RelationshipId;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;

import com.orphanet.neo4j.data.nodes.Phenotype;

@RelationshipProperties
public class DisorderPhenotypeRelation {

	@RelationshipId
	private Long id;
	
	private String frequency;
	
	private String criteria;
	
	@TargetNode
	private Phenotype phenotype;
	
	public DisorderPhenotypeRelation(String frequency, String criteria, Phenotype phenotype) {
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
	
	public Phenotype getPhenotype() {
		return phenotype;
	}
	
	public void setPhenotype(Phenotype phenotype) {
		this.phenotype = phenotype;
	}
}
