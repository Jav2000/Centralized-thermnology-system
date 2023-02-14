package com.orphanet.neo4j.data.relationships;

import org.springframework.data.neo4j.core.schema.RelationshipId;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;

import com.orphanet.neo4j.data.nodes.Neo4jGene;

@RelationshipProperties
public class DisorderGeneRelation {

	@RelationshipId
	private Long id;
	
	private String type;
	
	private String status;
	
	@TargetNode
	private Neo4jGene gene;
	
	public DisorderGeneRelation(String type, String status, Neo4jGene gene) {
		this.type = type;
		this.status = status;
		this.gene = gene;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public Neo4jGene getGene() {
		return gene;
	}
	
	public void setGene(Neo4jGene gene) {
		this.gene = gene;
	}
	
}
