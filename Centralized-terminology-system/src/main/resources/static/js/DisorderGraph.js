var hierarchyGraphOn = false;

document.getElementById("showHierarchyGraph").onclick = function(){
	createGraph(disorderGraph);
	document.getElementById("hierarchyGraph").style.display = "block";
	hierarchyGraphOn = true;
};

document.getElementById("closeHierarchyGraph").onclick = function(){
	d3.select("#hierarchySimulationSVG").selectAll("*").remove();
	document.getElementById("hierarchyGraph").style.display = "none";
	graphOn = false;
	hierarchyGraphOn = false;
}