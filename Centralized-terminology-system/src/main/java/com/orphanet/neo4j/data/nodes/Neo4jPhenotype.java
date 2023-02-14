package com.orphanet.neo4j.data.nodes;

import java.util.List;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;
import org.springframework.data.neo4j.core.schema.GeneratedValue.UUIDGenerator;
import org.springframework.data.neo4j.core.schema.Relationship.Direction;

import com.orphanet.neo4j.data.relationships.PhenotypeDisorderRelation;

@Node
public class Neo4jPhenotype {

	@Id
	@GeneratedValue(generatorClass = UUIDGenerator.class)
	private Long id;
	
	private String HPOId;
	
	private String term;
	
	@Relationship(type = "ASSOCIATED_WITH_PHENOTYPE", direction = Direction.INCOMING)
	private List<PhenotypeDisorderRelation> disorders;
	
	public Neo4jPhenotype(String HPOId, String term) {
		this.setHPOId(HPOId);
		this.setTerm(term);
	}

	public String getHPOId() {
		return HPOId;
	}

	public void setHPOId(String hPOId) {
		HPOId = hPOId;
	}

	public String getTerm() {
		return term;
	}

	public void setTerm(String term) {
		this.term = term;
	}
	
	public List<PhenotypeDisorderRelation> getDisorders(){
		return disorders;
	}
	
	public void setDisorders(List<PhenotypeDisorderRelation> disorders) {
		this.disorders = disorders;
	}
}
