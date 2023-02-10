package com.orphanet.objects;

import java.util.ArrayList;
import java.util.List;

public class GeneHierarchy {

	private String symbol;
	
	private String name;
	
	private String type;
	
	private String locus;
	
	private List<DisorderWithGeneRelation> disorders;
	
	public GeneHierarchy(String symbol, String name, String type, String locus) {
		this.setSymbol(symbol);
		this.setName(name);
		this.setType(type);
		this.setLocus(locus);
		this.disorders = new ArrayList<DisorderWithGeneRelation>();
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

	public List<DisorderWithGeneRelation> getDisorders() {
		return disorders;
	}

	public void setDisorders(List<DisorderWithGeneRelation> disorders) {
		this.disorders = disorders;
	}
}
