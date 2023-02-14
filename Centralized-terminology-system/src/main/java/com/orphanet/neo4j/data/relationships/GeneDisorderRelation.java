package com.orphanet.neo4j.data.relationships;

import org.springframework.data.neo4j.core.schema.RelationshipId;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;

import com.orphanet.neo4j.data.nodes.Neo4jDisorder;

@RelationshipProperties
public class GeneDisorderRelation {


	@RelationshipId
	private Long id;
	
	private String type;
	
	private String status;
	
	@TargetNode
	private Neo4jDisorder disorder;
	
	public GeneDisorderRelation(String type, String status, Neo4jDisorder disorder) {
		this.type = type;
		this.status = status;
		this.disorder = disorder;
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
	
	public Neo4jDisorder getDisorder() {
		return disorder;
	}
	
	public void setDisorder(Neo4jDisorder disorder) {
		this.disorder = disorder;
	}
}
