package com.orphanet.neo4j.data.relationships;

import org.springframework.data.neo4j.core.schema.TargetNode;

import com.orphanet.neo4j.data.nodes.Phenotype;

public class PhenotypeRelation {

	@TargetNode
	private Phenotype phenotype;
	
	public PhenotypeRelation(Phenotype phenotype) {
		this.phenotype = phenotype;
	}
	
	public Phenotype getPhenotype() {
		return phenotype;
	}
	
	public void setPhenotype(Phenotype phenotype) {
		this.phenotype = phenotype;
	}
}
