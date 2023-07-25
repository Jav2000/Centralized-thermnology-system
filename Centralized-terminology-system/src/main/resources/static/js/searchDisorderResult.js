var disorderGraphOn = false;
var disorderGraph = null;
  		
document.getElementById("showDisorderGraph").onclick = function(){
	if(disorderGraph === null){
		$.ajax({
			type: "GET",
		  	url: "/searchDisorder/graph",
		  	data: {"orphaCode": disorder.orphaCode},
		  	success: function(result){
		  		disorderGraph = result;
		  		createGraph(disorderGraph);
				document.getElementById("fullGraph").style.display = "block";
				disorderGraphOn = true;
		  	}
		  });
  	}else{
  		createGraph(disorderGraph);
		document.getElementById("fullGraph").style.display = "block";
		disorderGraphOn = true;
  	}
};
		
var geneGraphOn = false;
var geneGraph = null;
		
for(var i = 0; i < genes.length; i++){
	document.getElementById(genes[i].gene.symbol).onclick = function(){
		if(geneGraph === null){
			$.ajax({
				type: "GET",
				url: "/geneGraph",
				data: {"symbol": this.id},
				success: function(result){
					geneGraph = result;
					createGraph(geneGraph);
					document.getElementById("fullGraph").style.display = "block";
					geneGraphOn = true;
				}
			});
		}else{
			createGraph(geneGraph);
			document.getElementById("fullGraph").style.display = "block";
			geneGraphOn = true;
		}
	}
}

var phenotypeGraphOn = false;
var phenotypeGraph = null;
var phenotypesGraphLoad = new Map();

for(var i = 0; i < phenotypes.length; i++){
	document.getElementById(phenotypes[i].phenotype.hpoid).onclick = function(){
		if(phenotypesGraphLoad.get(this.id) === undefined){
			$.ajax({
				type: "GET",
				url: "/phenotypeGraph",
				data: {"HPOId": this.id},
				success: function(result){
					phenotypesGraphLoad.set(result.nodes[0].HPOId, result);
					createGraph(result);
					document.getElementById("fullGraph").style.display = "block";
					phenotypeGraphOn = true;
				}
			});
		}else{
			createGraph(phenotypesGraphLoad.get(this.id));
			document.getElementById("fullGraph").style.display = "block";
			phenotypeGraphOn = true;
		}
	}
}
		
document.getElementById("closeGraph").onclick = function(){
	d3.select("#graphSVG").selectAll("*").remove();
	document.getElementById("fullGraph").style.display = "none";
	disorderGraphOn = false;
	genesGraphOn = false;
	phenotypeGraphOn = false;
}