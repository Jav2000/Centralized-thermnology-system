package com.orphanet.objects;

public class DisorderWithDisorderRelation {

	private Integer orphaCode;
	
	private String name;
	
	private String type;
	
	private String group;
	
	private String relType;
	
	public DisorderWithDisorderRelation(Integer orphaCode, String name, String type, String group, String relType) {
		this.setOrphaCode(orphaCode);
		this.setName(name);
		this.setType(type);
		this.setGroup(group);
		this.setRelType(relType);
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getRelType() {
		return relType;
	}

	public void setRelType(String relType) {
		this.relType = relType;
	}
	
	
}
