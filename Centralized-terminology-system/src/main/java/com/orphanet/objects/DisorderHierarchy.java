package com.orphanet.objects;

import java.util.ArrayList;
import java.util.List;

public class DisorderHierarchy {

	private Integer orphaCode;
	
	private String name;
	
	private String group;
	
	private String type;
	
	private List<DisorderHierarchy> descendants;
	
	public DisorderHierarchy(Integer orphaCode, String name, String group, String type) {
		this.orphaCode = orphaCode;
		this.name = name;
		this.group = group;
		this.type = type;
		this.descendants = new ArrayList<DisorderHierarchy>();
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
	
	public List<DisorderHierarchy> getDescendants(){
		return descendants;
	}
	
	public void setDescendants(List<DisorderHierarchy> descendants) {
		this.descendants = descendants;
	}
}
