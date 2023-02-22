var disorderGraphOn = false;
var disorderGraph = null;
  		
document.getElementById("showDisorderGraph").onclick = function(){
	if(disorderGraph === null){
		$.ajax({
			type: "GET",
		  	url: "/disorderGraph",
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

autocomplete(document.getElementById("myInput"), names);

/* Funcion para autocompletar sugerencias buscador */	
function autocomplete(inp, arr) {
  	var currentFocus;
  	inp.addEventListener("input", function(e) {
    var a, b, i, val = this.value;
	closeAllLists();
	if (!val) { return false;}
		currentFocus = -1;
		a = document.createElement("DIV");
		a.setAttribute("id", this.id + "autocomplete-list");
		a.setAttribute("class", "autocomplete-items");
		this.parentNode.appendChild(a);
      	for (i = 0; i < arr.length; i++) {
        	if (arr[i].substr(0, val.length).toUpperCase() == val.toUpperCase()) {
          		b = document.createElement("DIV");
          		b.innerHTML = "<strong>" + arr[i].substr(0, val.length) + "</strong>";
          		b.innerHTML += arr[i].substr(val.length);
          		b.innerHTML += "<input type='hidden' th:name='input' value='" + arr[i] + "'>";
          		b.addEventListener("click", function(e) {
              		inp.value = this.getElementsByTagName("input")[0].value;
              		closeAllLists();
          		});
          		a.appendChild(b);
        	}
      	}
  	});
  	inp.addEventListener("keydown", function(e) {
      	var x = document.getElementById(this.id + "autocomplete-list");
      	if (x) x = x.getElementsByTagName("div");
      	if (e.keyCode == 40) {
        	currentFocus++;
        	addActive(x);
      	} else if (e.keyCode == 38) {
        	currentFocus--;
        	addActive(x);
      	} else if (e.keyCode == 13) {
        	e.preventDefault();
        	if (currentFocus > -1) {
          		if (x) x[currentFocus].click();
        	}
      	}
  	});
  	function addActive(x) {
    	if (!x) return false;
    	removeActive(x);
    	if (currentFocus >= x.length) currentFocus = 0;
    	if (currentFocus < 0) currentFocus = (x.length - 1);
    	x[currentFocus].classList.add("autocomplete-active");
  	}	
  	function removeActive(x) {
    	for (var i = 0; i < x.length; i++) {
      		x[i].classList.remove("autocomplete-active");
    	}
  	}
  	function closeAllLists(elmnt) {
    	var x = document.getElementsByClassName("autocomplete-items");
    	for (var i = 0; i < x.length; i++) {
      		if (elmnt != x[i] && elmnt != inp) {
        		x[i].parentNode.removeChild(x[i]);
      		}
    	}
  	}
  	document.addEventListener("click", function (e) {
      	closeAllLists(e.target);
  	});
}