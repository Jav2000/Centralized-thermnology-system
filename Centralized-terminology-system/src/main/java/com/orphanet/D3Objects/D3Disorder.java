package com.orphanet.D3Objects;

import java.util.List;

public class D3Disorder {

	private Integer orphaCode;
	
	private String name;
	
	private String group;
	
	private String type;
	
	private List<String> synonyms;
	
	private Integer OMIM;
	
	private String description;
	
	private String averageAgeOfOnset;
	
	private String averageAgeOfDeath;
	
	private String inheritanceTypes;
	
	private String preferentialClassification;
	
	private List<D3Disorder> ascendants;
	
	private List<D3Disorder> descendants;
	
	private String parentRelationType;
	
	private List<D3Gene> genes;
	
	private List<D3Phenotype> phenotypes;
	
	public D3Disorder(	Integer orphaCode, String name, String group, String type, String description,
						List<String> synonyms, Integer OMIM, String averageAgeOfOnset, String averageAgeOfDeath,
						String inheritanceTypes, String parentRelationType) {
		this.setOrphaCode(orphaCode);
		this.setName(name);
		this.setGroup(group);
		this.setType(type);
		this.setDescription(description);
		this.setSynonyms(synonyms);
		this.setOMIM(OMIM);
		this.setAverageAgeOfOnset(averageAgeOfOnset);
		this.setAverageAgeOfDeath(averageAgeOfDeath);
		this.setInheritanceTypes(inheritanceTypes);
		this.setParentRelationType(parentRelationType);
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

	public List<String> getSynonyms() {
		return synonyms;
	}

	public void setSynonyms(List<String> synonyms) {
		this.synonyms = synonyms;
	}

	public Integer getOMIM() {
		return OMIM;
	}

	public void setOMIM(Integer oMIM) {
		OMIM = oMIM;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public String getPreferentialClassification() {
		return preferentialClassification;
	}

	public void setPreferentialClassification(String preferentialClassification) {
		this.preferentialClassification = preferentialClassification;
	}

	public String getInheritanceTypes() {
		return inheritanceTypes;
	}

	public void setInheritanceTypes(String inheritanceTypes) {
		this.inheritanceTypes = inheritanceTypes;
	}

	public List<D3Disorder> getAscendants() {
		return ascendants;
	}

	public void setAscendants(List<D3Disorder> ascendants) {
		this.ascendants = ascendants;
	}

	public List<D3Disorder> getDescendants() {
		return descendants;
	}

	public void setDescendants(List<D3Disorder> descendants) {
		this.descendants = descendants;
	}

	public List<D3Gene> getGenes() {
		return genes;
	}

	public void setGenes(List<D3Gene> genes) {
		this.genes = genes;
	}

	public List<D3Phenotype> getPhenotypes() {
		return phenotypes;
	}

	public void setPhenotypes(List<D3Phenotype> phenotypes) {
		this.phenotypes = phenotypes;
	}

	public String getParentRelationType() {
		return parentRelationType;
	}

	public void setParentRelationType(String parentRelationType) {
		this.parentRelationType = parentRelationType;
	}
}
