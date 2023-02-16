package com.orphanet.neo4j.data.nodes;

import java.util.List;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.GeneratedValue.UUIDGenerator;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;
import org.springframework.data.neo4j.core.schema.Relationship.Direction;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.orphanet.neo4j.data.relationships.DisorderGeneRelation;
import com.orphanet.neo4j.data.relationships.ParentRelation;
import com.orphanet.neo4j.data.relationships.DisorderPhenotypeRelation;

@Node
public class Disorder {
	
	@Id
	@GeneratedValue(generatorClass = UUIDGenerator.class)
	private Long id;
	
	private Integer orphaCode;
	
	private String name;
	
	private String group;
	
	private String type;
	
	private String description;
	
	private List<String> synonyms;
	
	private Integer OMIM;
	
	private String averageAgeOfOnset;
	
	private String averageAgeOfDeath;
	
	private String inheritanceTypes;
	
	private String preferentialClassification;
	
	@JsonBackReference
	@Relationship(type = "PARENT_RELATION", direction = Direction.INCOMING)
	private List<ParentRelation> ascendants;
	
	@JsonBackReference
	@Relationship(type = "PARENT_RELATION")
	private List<ParentRelation> descendants;
	
	@JsonBackReference
	@Relationship(type = "ASSOCIATED_WITH_GENE")
	private List<DisorderGeneRelation> genes;
	
	@JsonBackReference
	@Relationship(type = "ASSOCIATED_WITH_PHENOTYPE")
	private List<DisorderPhenotypeRelation> phenotypes;
	
	public Disorder(Integer orphaCode, String name, String group, String type, String description,
					List<String> synonyms, Integer OMIM, String averageAgeOfOnset, String averageAgeOfDeath,
					String inheritanceTypes) {
		this.orphaCode = orphaCode;
		this.name = name;
		this.group = group;
		this.type = type;
		this.description = description;
		this.synonyms = synonyms;
		this.OMIM = OMIM;
		this.averageAgeOfOnset = averageAgeOfOnset;
		this.averageAgeOfDeath = averageAgeOfDeath;
		this.inheritanceTypes = inheritanceTypes;
	}

	public Integer getOrphaCode() {
		return orphaCode;
	}

	public void setOrphaCode(Integer orphaCode) {
		this.orphaCode = orphaCode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<String> getSynonyms() {
		return synonyms;
	}

	public void setSynonyms(List<String> synonyms) {
		this.synonyms = synonyms;
	}

	public Integer getOMIM() {
		return OMIM;
	}

	public void setOMIM(Integer OMIM) {
		this.OMIM = OMIM;
	}

	public String getAverageAgeOfOnset() {
		return averageAgeOfOnset;
	}

	public void setAverageAgeOfOnset(String averageAgeOfOnset) {
		this.averageAgeOfOnset = averageAgeOfOnset;
	}

	public String getAverageAgeOfDeath() {
		return averageAgeOfDeath;
	}

	public void setAverageAgeOfDeath(String averageAgeOfDeath) {
		this.averageAgeOfDeath = averageAgeOfDeath;
	}

	public String getInheritanceTypes() {
		return inheritanceTypes;
	}

	public void setInheritanceTypes(String inheritanceTypes) {
		this.inheritanceTypes = inheritanceTypes;
	}

	public String getPreferentialClassification() {
		return preferentialClassification;
	}

	public void setPreferentialClassification(String preferentialClassification) {
		this.preferentialClassification = preferentialClassification;
	}

	public List<ParentRelation> getAscendants(){
		return ascendants;
	}
	
	public void setAscendants(List<ParentRelation> ascendants) {
		this.ascendants = ascendants;
	}	
	
	public List<ParentRelation> getDescendants(){
		return descendants;
	}
	
	public void setDescendants(List<ParentRelation> descendants) {
		this.descendants = descendants;
	}
	
	public List<DisorderGeneRelation> getGenes(){
		return genes;
	}
	
	public void setGenes(List<DisorderGeneRelation> genes) {
		this.genes = genes;
	}
	
	public List<DisorderPhenotypeRelation> getPhenotypes(){
		return phenotypes;
	}
	
	public void setPhenotypes(List<DisorderPhenotypeRelation> phenotypes) {
		this.phenotypes = phenotypes;
	}
}
