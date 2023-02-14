package com.orphanet.neo4j.data.nodes;

import java.util.List;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;
import org.springframework.data.neo4j.core.schema.GeneratedValue.UUIDGenerator;

import com.orphanet.neo4j.data.relationships.ParentRelation;

@Node
public class Root {
	
	@Id
	@GeneratedValue(generatorClass = UUIDGenerator.class)
	private Long id;
	
	private Integer orphaCode;
	
	@Relationship(type = "PARENT_RELATION")
	private List<ParentRelation> descendants;
	
	public Root(Integer orphaCode) {
		this.setOrphaCode(orphaCode);
	}

	public Integer getOrphaCode() {
		return orphaCode;
	}

	public void setOrphaCode(Integer orphaCode) {
		this.orphaCode = orphaCode;
	}
	
	public List<ParentRelation> getDescendants(){
		return descendants;
	}
	
	public void setDescendants(List<ParentRelation> descendants) {
		this.descendants = descendants;
	}
	
}
