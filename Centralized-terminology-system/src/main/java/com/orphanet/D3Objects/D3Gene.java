package com.orphanet.D3Objects;

public class D3Gene {

	private String symbol;
	
	private String name;
	
	private String type;
	
	private String locus;
	
	private Integer OMIM;
	
	private String disorderRelationStatus;
	
	private String disorderRelationType;
	
	public D3Gene(String symbol, String name, String type, String locus, Integer OMIM, String disorderRelationStatus, String disorderRelationType) {
		this.setSymbol(symbol);
		this.setName(name);
		this.setType(type);
		this.setLocus(locus);
		this.setOMIM(OMIM);
		this.setDisorderRelationStatus(disorderRelationStatus);
		this.setDisorderRelationType(disorderRelationType);
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

	public void setOMIM(Integer oMIM) {
		OMIM = oMIM;
	}

	public String getDisorderRelationStatus() {
		return disorderRelationStatus;
	}

	public void setDisorderRelationStatus(String disorderRelationStatus) {
		this.disorderRelationStatus = disorderRelationStatus;
	}

	public String getDisorderRelationType() {
		return disorderRelationType;
	}

	public void setDisorderRelationType(String disorderRelationType) {
		this.disorderRelationType = disorderRelationType;
	}
}
