package com.orphanet.neo4j.data.nodes;

import java.util.List;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;
import org.springframework.data.neo4j.core.schema.GeneratedValue.UUIDGenerator;
import org.springframework.data.neo4j.core.schema.Relationship.Direction;

import com.orphanet.neo4j.data.relationships.GeneDisorderRelation;

@Node
public class Gene {
	
	@Id
	@GeneratedValue(generatorClass = UUIDGenerator.class)
	private Long id;
	
	private String symbol;
	
	private String name;
	
	private String type;
	
	private String locus;
	
	private Integer OMIM;
	
	@Relationship(type = "ASSOCIATED_WITH_GENE", direction = Direction.INCOMING)
	private List<GeneDisorderRelation> disorders;
	
	public Gene(String symbol, String name, String type, String locus, Integer OMIM) {
		this.symbol = symbol;
		this.name = name;
		this.type = type;
		this.locus = locus;
		this.OMIM = OMIM;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getLocus() {
		return locus;
	}

	public void setLocus(String locus) {
		this.locus = locus;
	}

	public Integer getOMIM() {
		return OMIM;
	}

	public void setOMIM(Integer OMIM) {
		this.OMIM = OMIM;
	}
	
	public List<GeneDisorderRelation> getDisorders(){
		return disorders;
	}
	
	public void setDisorders(List<GeneDisorderRelation> disorders) {
		this.disorders = disorders;
	}
}
