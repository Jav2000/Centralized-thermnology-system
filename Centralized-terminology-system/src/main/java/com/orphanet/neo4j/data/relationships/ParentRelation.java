package com.orphanet.neo4j.data.relationships;

import org.springframework.data.neo4j.core.schema.RelationshipId;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;

import com.orphanet.neo4j.data.nodes.Disorder;

@RelationshipProperties
public class ParentRelation {

	@RelationshipId
	private Long id;
	
	private String type;
	
	@TargetNode
	private Disorder son;
	
	public ParentRelation(Disorder son, String type) {
		this.son = son;
		this.setType(type);
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public Disorder getSon() {
		return son;
	}
	
	public void setSon(Disorder son) {
		this.son = son;
	}
}
