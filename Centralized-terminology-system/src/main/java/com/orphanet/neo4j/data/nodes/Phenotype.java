package com.orphanet.neo4j.data.nodes;

import java.util.List;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;
import org.springframework.data.neo4j.core.schema.GeneratedValue.UUIDGenerator;
import org.springframework.data.neo4j.core.schema.Relationship.Direction;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.orphanet.neo4j.data.relationships.PhenotypeDisorderRelation;
import com.orphanet.neo4j.data.relationships.PhenotypeRelation;

@Node
public class Phenotype {

	@Id
	@GeneratedValue(generatorClass = UUIDGenerator.class)
	private Long id;
	
	private String HPOId;
	
	private String term;
	
	private List<String> synonyms;
	
	private String definition;
	
	private String comments;
	
	@JsonBackReference
	@Relationship(type = "IS_A_SUBCLASS_OF", direction = Direction.INCOMING)
	private List<PhenotypeRelation> ascendants;
	
	@JsonBackReference
	@Relationship(type = "IS_A_SUBCLASS_OF")
	private List<PhenotypeRelation> descendants;
	
	@Relationship(type = "ASSOCIATED_WITH_PHENOTYPE", direction = Direction.INCOMING)
	private List<PhenotypeDisorderRelation> disorders;
	
	public Phenotype(String HPOId, String term, List<String> synonyms, String definition, String comments) {
		this.setHPOId(HPOId);
		this.setTerm(term);
		this.setSynonyms(synonyms);
		this.setDefinition(definition);
		this.setComments(comments);
	}

	public String getHPOId() {
		return HPOId;
	}

	public void setHPOId(String HPOId) {
		this.HPOId = HPOId;
	}

	public String getTerm() {
		return term;
	}

	public void setTerm(String term) {
		this.term = term;
	}
	
	public List<String> getSynonyms() {
		return synonyms;
	}

	public void setSynonyms(List<String> synonyms) {
		this.synonyms = synonyms;
	}

	public String getDefinition() {
		return definition;
	}

	public void setDefinition(String definition) {
		this.definition = definition;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}
	
	public List<PhenotypeRelation> getAscendants(){
		return ascendants;
	}
	
	public void setAscendants(List<PhenotypeRelation> ascendants) {
		this.ascendants = ascendants;
	}	
	
	public List<PhenotypeRelation> getDescendants(){
		return descendants;
	}
	
	public void setDescendants(List<PhenotypeRelation> descendants) {
		this.descendants = descendants;
	}

	public List<PhenotypeDisorderRelation> getDisorders(){
		return disorders;
	}
	
	public void setDisorders(List<PhenotypeDisorderRelation> disorders) {
		this.disorders = disorders;
	}
}
