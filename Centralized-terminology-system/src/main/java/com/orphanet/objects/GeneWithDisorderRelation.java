package com.orphanet.objects;

public class GeneWithDisorderRelation {

	private String symbol;
	
	private String name;
	
	private String type;
	
	private String locus;
	
	private String relStatus;
	
	private String relType;
	
	public GeneWithDisorderRelation(String symbol, String name, String type, String locus, String relStatus, String relType) {
		this.setSymbol(symbol);
		this.setName(name);
		this.setType(type);
		this.setLocus(locus);
		this.setRelStatus(relStatus);
		this.setRelType(relType);
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

	public String getRelStatus() {
		return relStatus;
	}

	public void setRelStatus(String relStatus) {
		this.relStatus = relStatus;
	}

	public String getRelType() {
		return relType;
	}

	public void setRelType(String relType) {
		this.relType = relType;
	}
}
