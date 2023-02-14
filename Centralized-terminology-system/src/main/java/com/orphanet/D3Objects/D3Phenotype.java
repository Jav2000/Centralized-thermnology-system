package com.orphanet.D3Objects;

public class D3Phenotype {

	private String HPOId;
	
	private String term;
	
	private String disorderRelationCriteria;
	
	private String disorderRelationFrequency;
	
	public D3Phenotype(String HPOId, String term, String disorderRelationCriteria, String disorderRelationFrequency) {
		this.setHPOId(HPOId);
		this.setTerm(term);
		this.setDisorderRelationCriteria(disorderRelationCriteria);
		this.setDisorderRelationFrequency(disorderRelationFrequency);
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

	public String getDisorderRelationCriteria() {
		return disorderRelationCriteria;
	}

	public void setDisorderRelationCriteria(String disorderRelationCriteria) {
		this.disorderRelationCriteria = disorderRelationCriteria;
	}

	public String getDisorderRelationFrequency() {
		return disorderRelationFrequency;
	}

	public void setDisorderRelationFrequency(String disorderRelationFrequency) {
		this.disorderRelationFrequency = disorderRelationFrequency;
	}
}
