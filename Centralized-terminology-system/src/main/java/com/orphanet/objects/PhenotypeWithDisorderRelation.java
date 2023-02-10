package com.orphanet.objects;

public class PhenotypeWithDisorderRelation {

	private String term;
	
	private String HPOId;
	
	private String relCriteria;
	
	private String relFrequency;
	
	public PhenotypeWithDisorderRelation(String term, String HPOId, String relCriteria, String relFrequency) {
		this.setTerm(term);
		this.setHPOId(HPOId);
		this.setRelCriteria(relCriteria);
		this.setRelFrequency(relFrequency);
	}

	public String getTerm() {
		return term;
	}

	public void setTerm(String term) {
		this.term = term;
	}

	public String getHPOId() {
		return HPOId;
	}

	public void setHPOId(String hPOId) {
		HPOId = hPOId;
	}

	public String getRelCriteria() {
		return relCriteria;
	}

	public void setRelCriteria(String relCriteria) {
		this.relCriteria = relCriteria;
	}

	public String getRelFrequency() {
		return relFrequency;
	}

	public void setRelFrequency(String relFrequency) {
		this.relFrequency = relFrequency;
	}
}
