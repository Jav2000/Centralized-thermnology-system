let height = 1000;
let width = 1000;
		
let svg = d3.select('svg')
        	.attr('width', width)
        	.attr('height', height);
		
let rootHierarchy = d3.hierarchy(root, d => d.descendants);
		
let diameter = height * 0.75;
let radius = diameter / 2;
let tree = d3.tree()
    			.size([2 * Math.PI, radius])
    			.separation(function(a, b) { 
        			return (a.parent == b.parent ? 1 : 2) / a.depth; 
    			});
    		
let dataTree = tree(rootHierarchy);	
let nodes = dataTree.descendants();
let links = dataTree.links();
    	
let graphGroup = svg.append('g')
    				.attr('transform', "translate("+(width/2)+","+(height/2)+")");
    						
let node = graphGroup.selectAll(".node")
        				.data(nodes)
						.join("g")
						.attr("class", "node")
						.attr("transform", function(d){
							return `rotate(${d.x * 180 / Math.PI - 90})` + `translate(${d.y}, 0)`;
						});
						        
let circulo = node.append("circle").attr("r", 2);
		
circulo.on("mouseover", function(d, i){
	d3.select(this).attr("r", 4);
	showDetails(i);
});
		
circulo.on("mouseout", function(){
	d3.select(this).attr("r", 2);
	document.getElementById("detailsDiv").style.display = "none";
})
    						
graphGroup.selectAll(".link")
    		.data(links)
    		.join("path")
    		.attr("class", "link")
    		.attr("d", d3.linkRadial()
        		.angle(d => d.x)
        		.radius(d => d.y));
        		
node.append("text")
    .attr("font-family", "sans-serif")
    .attr("font-size", 12)
    .attr("dx", function(d) { return d.x < Math.PI ? 20 : -20; })
    .attr("dy", ".31em")
    .attr("text-anchor", function(d) { 
        return d.x < Math.PI ? "start" : "end"; 
    })
    .attr("transform", function(d) { 
        return d.x < Math.PI ? null : "rotate(180)"; 
    })
    .text(function(d) {
    	if(d.data.son != undefined){
    		return d.data.son.name;
    	}else{
    		return "";
    	}
    });
		
function showDetails(node){
	var data = node.data;
	document.getElementById("detailsName").innerHTML = node.data.name;
	document.getElementById("detailsOrphaCode").innerHTML = node.data.orphaCode;
	document.getElementById("detailsType").innerHTML = node.data.type;
	document.getElementById("detailsGroup").innerHTML = node.data.group;
	
	document.getElementById("detailsDiv").style.display = "block";
}