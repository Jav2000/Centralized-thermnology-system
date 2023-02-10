function createGeneDisordersGraph(gene){
	var hierarchy = d3.hierarchy(gene, d => d.disorders);
					
	var height = 600;
	var width = 600;
	var margin = {top: -5, right: -5, bottom: -5, left: -5}
					
	var svg = d3.select("#geneSimulationSVG")
				.attr('width', width)
			    .attr('height', height)
				.attr("viewBox", [0, 0, width, height])
									
	svg.call(d3.zoom()
			   	.extent([ [0, 0], [width, height] ])
				.scaleExtent([1/4, 8])
				.on("zoom", zoomed));
			
	var color = d3.scaleOrdinal(d3.schemeCategory10);
			
	/* Leyenda */
	/* 		Nodos */
	svg.append("circle").attr("cx",30).attr("cy",40).attr("r", 6).style("fill", "#b150e0");
	svg.append("circle").attr("cx",30).attr("cy",60).attr("r", 6).style("fill", "#e6ff48");
	svg.append("circle").attr("cx",30).attr("cy",80).attr("r", 6).style("fill", "#ebb32b");
	svg.append("circle").attr("cx",30).attr("cy",100).attr("r", 6).style("fill", "#2ca02c");
	svg.append("text").attr("x", 50).attr("y", 40).text("Group").style("font-size", "15px").attr("alignment-baseline","middle");
	svg.append("text").attr("x", 50).attr("y", 60).text("Disorder").style("font-size", "15px").attr("alignment-baseline","middle");
	svg.append("text").attr("x", 50).attr("y", 80).text("Subtype").style("font-size", "15px").attr("alignment-baseline","middle");
	svg.append("text").attr("x", 50).attr("y", 100).text("Gene").style("font-size", "15px").attr("alignment-baseline","middle");
	/* 		Links */
	svg.append("line").attr("x1",140).attr("x2",160).attr("y1",20).attr("y2",20).attr("stroke-width", 2).attr("stroke-opacity", 0.6).attr("stroke", "#ff7f0e");
	svg.append("line").attr("x1",140).attr("x2",160).attr("y1",40).attr("y2",40).attr("stroke-width", 2).attr("stroke-opacity", 0.6).attr("stroke", "#1f77b4");
	svg.append("text").attr("x", 170).attr("y", 20).text("Parent Relation").style("font-size", "15px").attr("alignment-baseline","middle");
	svg.append("text").attr("x", 170).attr("y", 40).text("Associated With Gene").style("font-size", "15px").attr("alignment-baseline","middle");
							    	
	var link = svg.selectAll(".link")
					.data(hierarchy.links())
					.enter()
					.append("g")
					.attr("class", "link")
					.attr("stroke", function(d){
						let nodeType = typeOfNode(d.target);
						if(nodeType === 1){
							return "#ff7f0e";
						}else if(nodeType === 2){
							return "#1f77b4";
						}else{
							return "#3d8c38";
						}
					})
			    	.attr("stroke-opacity", 0.6)
					.append("line")
					.attr("stroke-width", d => Math.sqrt(d.value));
							
	var node = svg.selectAll(".node")
					.data(hierarchy.descendants())
					.enter()
					.append("g")
					.attr("class", "node")
					.style("fill", function(d){
						let nodeType = typeOfNode(d);
						if(nodeType === 1){
							if(d.parent === null){
								return "#df0f0f";
							}
							if(d.data.group === "Subtype of disorder"){
								return "#ebb32b";
							}else if(d.data.group === "Disorder"){
								return "#e6ff48";
							}else{
								return "#b150e0";
							}
							return color(d.data.group);
						}else if(nodeType === 2){
							return "#2ca02c";
						}else{
							return "#5b9bf4";
						}
					})
					.append("circle")
					.attr("r", function(d){
						if(d.parent === null){
							return 5;
						}
						return 3;
					});
							    	
	function zoomed({transform}) {
		link.attr("transform", transform);
		node.attr("transform", transform);
	}
							    	
					
	var force = d3.forceSimulation(hierarchy.descendants())
					.force('charge', d3.forceManyBody().strength(-15))
			  		.force('center', d3.forceCenter(width / 2, height / 2))
					.force('link', d3.forceLink().distance(function(d){
						let nodeType = typeOfNode(d.target);
						if(nodeType === 1){
							return 50;
						}else if(nodeType === 2){
							return 30;
						}else{
							return 40;
						}
					}).links(hierarchy.links()).id(d => d.data.orphaCode))
					.on("tick", function(){
						node.attr("cx", d => d.x)
							.attr("cy", d => d.y);
						link.attr('x1', function(d) {
							return d.source.x
						})
							.attr('y1', function(d) {
								return d.source.y
							})
							.attr('x2', function(d) {
								return d.target.x
							})
							.attr('y2', function(d) {
								return d.target.y
							});
					});
			
	var detailsOn = false;
	var activeNode = null;
	var rootNodeOn = false;
		
	node.on("click", function(evt, d){
		let nodeType = typeOfNode(d);
		if(detailsOn){
			if(nodeType === 1){
				if(d.data.orphaCode !== rootOrphaCode){
					d3.select(activeNode).attr("r", 3);		
				}
				document.getElementById("geneSimulationDisorderDiv").style.display = "none";
			}else if(nodeType === 2){
				d3.select(activeNode).attr("r", 3);	
				document.getElementById("geneSimulationGeneDiv").style.display = "none";
			}
			activeNode = null;
			rootNodeOn = null;
			detailsOn = false;
			link.attr("display", "block");
		}else{
			activeNode = this;
			if(nodeType === 1){
				if(d.data.orphaCode === rootOrphaCode){
					rootNodeOn = true;
				}
				showDisorderDetails(d);
				link.attr("display", "none")
					.filter(l => l.target.data.orphaCode === d.data.orphaCode)
					.attr("display", "block");
			}else if(nodeType === 2){
				showGeneDetails(d);
				link.attr("display", "none")
					.filter(l => l.target.data.symbol === d.data.symbol)
					.attr("display", "block");
			}
			d3.select(this).attr("r", 5);
			detailsOn = true;
		}
		event.stopPropagation();
	});
			
	svg.on("click", function(){
		if(activeNode !== null){
			if(!rootNodeOn){
				d3.select(activeNode).attr("r", 3);
			}
			detailsOn = false;
			document.getElementById("geneSimulationDisorderDiv").style.display = "none";
			document.getElementById("geneSimulationGeneDiv").style.display = "none";
			link.attr("display", "block");
		}
	});
			
	node.on("mouseover", function(evt, d){
		let nodeType = typeOfNode(d);
		if(!detailsOn){
			d3.select(this).attr("r", 5);
			if(nodeType === 1){
				showDisorderDetails(d);
				link.attr("display", "none")
					.filter(l => l.target.data.orphaCode === d.data.orphaCode)
					.attr("display", "block");
			}else if(nodeType === 2){
				showGeneDetails(d);
				link.attr("display", "none")
					.filter(l => l.target.data.symbol === d.data.symbol)
					.attr("display", "block");
			}
		}
	});
					
	node.on("mouseout", function(evt, d){
		let nodeType = typeOfNode(d);
		if(!detailsOn){
			if(nodeType === 1){
				if(d.data.orphaCode !== rootOrphaCode){
					d3.select(this).attr("r", 3);
				}
				document.getElementById("geneSimulationDisorderDiv").style.display = "none";
			}else if(nodeType === 2){
				d3.select(this).attr("r", 3);
				document.getElementById("geneSimulationGeneDiv").style.display = "none";
			}
			link.attr("display", "block");
		}
	})

	document.getElementById("genesGraph").style.display = "block";
}

function showDisorderDetails(node){
	var data = node.data;
	document.getElementById("geneDisorderName").innerHTML = data.name;
	document.getElementById("geneDisorderOrphaCode").innerHTML = data.orphaCode;
	document.getElementById("geneDisorderType").innerHTML = data.type;
	document.getElementById("geneDisorderGroup").innerHTML = data.group;
	document.getElementById("geneDisorderRelStatus").innerHTML = data.relStatus;
	document.getElementById("geneDisorderRelType").innerHTML = data.relType;
	document.getElementById("geneSimulationDisorderDiv").style.display = "block";
}
	
function showGeneDetails(gene){
	var data = gene.data;
	document.getElementById("geneGeneName").innerHTML = data.name;
	document.getElementById("geneGeneSymbol").innerHTML = data.symbol;
	document.getElementById("geneGeneType").innerHTML = data.type;
	document.getElementById("geneGeneGroup").innerHTML = data.locus;
	document.getElementById("geneSimulationGeneDiv").style.display = "block";
}
			
function typeOfNode(node){
	if(node.data.orphaCode !== undefined){
		return 1;
	}else if(node.data.symbol !== undefined){
		return 2;
	}
	return 3;
}

document.getElementById("closeGenesGraph").onclick = function(){
	document.getElementById("genesGraph").style.display = "none";
	graphOn = false;
}