function createGraph(graph){
	var height = 600;
	var width = 600;
	var margin = {top: -5, right: -5, bottom: -5, left: -5}
	
	var svg = d3.select("#hierarchySimulationSVG")
				.attr('width', width)
			    .attr('height', height)
				.attr("viewBox", [0, 0, width, height]);
				
	/* Leyenda */
	/* 		Nodos */
	svg.append("circle").attr("cx",30).attr("cy",20).attr("r", 6).style("fill", "#df0f0f");
	svg.append("circle").attr("cx",30).attr("cy",40).attr("r", 6).style("fill", "#b150e0");
	svg.append("circle").attr("cx",30).attr("cy",60).attr("r", 6).style("fill", "#e6ff48");
	svg.append("circle").attr("cx",30).attr("cy",80).attr("r", 6).style("fill", "#ebb32b");
	svg.append("circle").attr("cx",30).attr("cy",100).attr("r", 6).style("fill", "#2ca02c");
	svg.append("circle").attr("cx",30).attr("cy",120).attr("r", 6).style("fill", "#5b9bf4");
	svg.append("text").attr("x", 50).attr("y", 20).text("Root").style("font-size", "15px").attr("alignment-baseline","middle");
	svg.append("text").attr("x", 50).attr("y", 40).text("Group").style("font-size", "15px").attr("alignment-baseline","middle");
	svg.append("text").attr("x", 50).attr("y", 60).text("Disorder").style("font-size", "15px").attr("alignment-baseline","middle");
	svg.append("text").attr("x", 50).attr("y", 80).text("Subtype").style("font-size", "15px").attr("alignment-baseline","middle");
	svg.append("text").attr("x", 50).attr("y", 100).text("Gene").style("font-size", "15px").attr("alignment-baseline","middle");
	svg.append("text").attr("x", 50).attr("y", 120).text("Phenotype").style("font-size", "15px").attr("alignment-baseline","middle");
	/* 		Links */
	svg.append("line").attr("x1",140).attr("x2",160).attr("y1",20).attr("y2",20).attr("stroke-width", 2).attr("stroke-opacity", 0.6).attr("stroke", "#ff7f0e");
	svg.append("line").attr("x1",140).attr("x2",160).attr("y1",40).attr("y2",40).attr("stroke-width", 2).attr("stroke-opacity", 0.6).attr("stroke", "#1f77b4");
	svg.append("line").attr("x1",140).attr("x2",160).attr("y1",60).attr("y2",60).attr("stroke-width", 2).attr("stroke-opacity", 0.6).attr("stroke", "#3d8c38");
	svg.append("text").attr("x", 170).attr("y", 20).text("Parent Relation").style("font-size", "15px").attr("alignment-baseline","middle");
	svg.append("text").attr("x", 170).attr("y", 40).text("Associated With Gene").style("font-size", "15px").attr("alignment-baseline","middle");
	svg.append("text").attr("x", 170).attr("y", 60).text("Associated With Phenotype").style("font-size", "15px").attr("alignment-baseline","middle");
				
	svg.call(d3.zoom()
			   	.extent([ [0, 0], [width, height] ])
				.scaleExtent([1/4, 8])
				.on("zoom", zoomed));
	
	var rootNode = disorderGraph.nodes[0];
	
	var link = svg.selectAll(".link")
					.data(graph.links)
					.enter()
					.append("g")
					.attr("class", "link")
					.style("stroke", d => colorLinks(d))
					.append("line")
					.attr("stroke-width", d => Math.sqrt(d.value));
	
	var node = svg.selectAll(".node")
					  .data(graph.nodes)
					  .enter()
					  .append("g")
					  .attr("class", "node")
					  .style("fill", d => colorNodes(d))
					  .append("circle")
					  .attr("r", function(d){
					  	if(d.id == rootNode.id){
					  		return 5;
					  	}else{
					  		return 3;
					  	}
					  });
	
	var activeNodeDetails = null;
	
	node.on("click", function(evt, d){
		if(activeNodeDetails != null){
			document.getElementById("hierarchySimulationDisorderDiv").style.display = "none";
			document.getElementById("hierarchySimulationGeneDiv").style.display = "none";
			document.getElementById("hierarchySimulationPhenotypeDiv").style.display = "none";
			if(d.id == activeNodeDetails.id){
				activeNodeDetails = null;
				node.attr("r", function(d){
					if(d.id != rootNode.id){
						return 3;
					}else{
						return 5;
					}
				});
				link.attr("display", "block");
			}else{
				activeNodeDetails = d;
				node.attr("r", function(d){
					if(d.id != rootNode.id){
						return 3;
					}else{
						return 5;
					}
				});
				link.attr("display", "none")
					.filter(l => l.target.id == d.id)
					.attr("display", "block");
				d3.select(this).attr("r", 5);
				showNodeDetails(d);
			}
		}else{
			activeNodeDetails = d;
			link.attr("display", "none")
				.filter(l => l.target.id == d.id)
				.attr("display", "block");
			d3.select(this).attr("r", 5);
			showNodeDetails(d);
		}
		event.stopPropagation();
	});
	
	node.on("mouseover", function(evt, d){
			if(activeNodeDetails != null){
			document.getElementById("hierarchySimulationDisorderDiv").style.display = "none";
			document.getElementById("hierarchySimulationGeneDiv").style.display = "none";
			document.getElementById("hierarchySimulationPhenotypeDiv").style.display = "none";
			if(d.id == activeNodeDetails.id){
				activeNodeDetails = null;
				node.attr("r", function(d){
					if(d.id != rootNode.id){
						return 3;
					}else{
						return 5;
					}
				});
				link.attr("display", "block");
			}else{
				activeNodeDetails = d;
				node.attr("r", function(d){
					if(d.id != rootNode.id){
						return 3;
					}else{
						return 5;
					}
				});
				link.attr("display", "none")
					.filter(l => l.target.id == d.id)
					.attr("display", "block");
				d3.select(this).attr("r", 5);
				showNodeDetails(d);
			}
		}else{
			activeNodeDetails = d;
			link.attr("display", "none")
				.filter(l => l.target.id == d.id)
				.attr("display", "block");
			d3.select(this).attr("r", 5);
			showNodeDetails(d);
		}
		event.stopPropagation();
	});
	
	node.on("mouseout", function(evt, d){
		if(activeNodeDetails != null){
			activeNodeDetails = null;
			document.getElementById("hierarchySimulationDisorderDiv").style.display = "none";
			document.getElementById("hierarchySimulationGeneDiv").style.display = "none";
			document.getElementById("hierarchySimulationPhenotypeDiv").style.display = "none";
			node.attr("r", function(d){
				if(d.id != rootNode.id){
					return 3;
				}else{
					return 5;
				}
			});
			link.attr("display", "block");
		}
	});
	
	svg.on("click", function(){
		if(activeNodeDetails != null){
			activeNodeDetails = null;
			document.getElementById("hierarchySimulationDisorderDiv").style.display = "none";
			document.getElementById("hierarchySimulationGeneDiv").style.display = "none";
			document.getElementById("hierarchySimulationPhenotypeDiv").style.display = "none";
			node.attr("r", function(d){
				if(d.id != rootNode.id){
					return 3;
				}else{
					return 5;
				}
			});
			link.attr("display", "block");
		}
	});
					  
	var simulation = d3.forceSimulation(graph.nodes)
					   .force("link", d3.forceLink()
						   				.id(function(d) {return d.id; })
						   				.links(graph.links)
						   	 )
					   .force("charge", d3.forceManyBody().strength(-300))         // This adds repulsion between nodes. Play with the -400 for the repulsion strength
	      			   .force("center", d3.forceCenter(width / 2, height / 2))     // This force attracts nodes to the center of the svg area
	      			   .on("tick", ticked);
	function zoomed({transform}) {
		link.attr("transform", transform);
		node.attr("transform", transform);
	}
	
	function ticked() {
		link.attr("x1", function(d) {return d.source.x; })
	    	.attr("y1", function(d) { return d.source.y; })
	        .attr("x2", function(d) { return d.target.x; })
	        .attr("y2", function(d) { return d.target.y; });
	
	    node.attr("cx", function (d) { return d.x; })
	        .attr("cy", function(d) { return d.y; });
	}
	
	function colorLinks(d){
		if(d.target.id !== undefined){
			if(rootNode.typeOfNode === "disorder"){
				var targetTypeOfNode = graph.nodes[d.target.id].typeOfNode;
								
				if(targetTypeOfNode === "disorder"){
					return "#ff7f0e";
				}else if(targetTypeOfNode === "gene"){
					return "#1f77b4";
				}else{
					return "#3d8c38";
				}
			}else if(rootNode.typeOfNode === "gene"){
				return "#1f77b4";
			}else{
				return "#3d8c38";
			}
		}else{
			if(rootNode.typeOfNode === "disorder"){
				var targetTypeOfNode = graph.nodes[d.target].typeOfNode;
								
				if(targetTypeOfNode === "disorder"){
					return "#ff7f0e";
				}else if(targetTypeOfNode === "gene"){
					return "#1f77b4";
				}else{
					return "#3d8c38";
				}
			}else if(rootNode.typeOfNode === "gene"){
				return "#1f77b4";
			}else{
				return "#3d8c38";
			}
		}
	}
	
	function colorNodes(d){
		if(d.typeOfNode === "disorder"){
			if(d.group === "Subtype of disorder"){
				return "#ebb32b";
			}else if(d.group === "Disorder"){
				return "#e6ff48";
			}else{
				return "#b150e0";
			}
		}else if(d.typeOfNode === "gene"){
			return "#2ca02c";
		}else{
			return "#5b9bf4";
		}
	}
	
	function showNodeDetails(d){
		if(d.typeOfNode === "disorder"){
			document.getElementById("disorderName").innerHTML = d.name;
			document.getElementById("disorderOrphaCode").innerHTML = d.orphaCode;
			document.getElementById("disorderType").innerHTML = d.type;
			document.getElementById("disorderGroup").innerHTML = d.group;
			document.getElementById("hierarchySimulationDisorderDiv").style.display = "block";
		}else if(d.typeOfNode === "gene"){
			document.getElementById("geneName").innerHTML = d.name;
			document.getElementById("geneSymbol").innerHTML = d.symbol;
			document.getElementById("geneType").innerHTML = d.type;
			document.getElementById("geneLocus").innerHTML = d.locus;
			document.getElementById("hierarchySimulationGeneDiv").style.display = "block";
		}else{
			document.getElementById("phenotypeTerm").innerHTML = d.term;
			document.getElementById("phenotypeHPOId").innerHTML = d.HPOId;
			document.getElementById("hierarchySimulationPhenotypeDiv").style.display = "block";
		}
	}
}


