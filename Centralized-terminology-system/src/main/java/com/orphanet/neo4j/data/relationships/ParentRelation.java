package com.orphanet.neo4j.data.relationships;

import org.springframework.data.neo4j.core.schema.RelationshipId;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;

import com.orphanet.neo4j.data.nodes.Neo4jDisorder;

@RelationshipProperties
public class ParentRelation {

	@RelationshipId
	private Long id;
	
	private String type;
	
	@TargetNode
	private Neo4jDisorder son;
	
	public ParentRelation(Neo4jDisorder son, String type) {
		this.son = son;
		this.setType(type);
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public Neo4jDisorder getSon() {
		return son;
	}
	
	public void setSon(Neo4jDisorder son) {
		this.son = son;
	}
}
