/* Ventana del grafo */
var height = 600;
var width = 600;
var margin = {top: -5, right: -5, bottom: -5, left: -5}
				
var svg = d3.select("#D3Graph")
			.attr('width', width)
		    .attr('height', height)
			.attr("viewBox", [0, 0, width, height])

/* Zoom del grafo */								
svg.call(d3.zoom()
		   	.extent([ [0, 0], [width, height] ])
			.scaleExtent([1/4, 8])
			.on("zoom", zoomed));
		
var color = d3.scaleOrdinal(d3.schemeCategory10);
		
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




