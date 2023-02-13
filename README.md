# Sistema centralizado de terminologías clínicas
Sistema centralizado de terminologías clínicas

# Autores
* Javier Rodríguez Sánchez

# Entorno
* Java 17
* Spring Boot MVN
* Neo4j

# Spring Dependencies
* Spring boot starter web
	* Dependencia para construir aplicaciones web, incluyendo RESTful, utilizando Spring MVC. Utiliza Tomcat como contenedor incrustado por defecto.
* Thymeleaf
	* Motor de plantillas Java para aplicaciones web
* DevTools
    * Auto Refresh
* Spring Starter Web
    * Para crear aplicaciones web
* Spring data Neo4j
    * Base de datos no relacional Neo4j
* Spring gson
    * Dependencia para librerias JSON

# Documentacion
## Requisitos del sistema
La web consistirá en un motor de búsqueda que permita buscar información sobre una entidad (grupo, enfermedad o subtipo).
* Sugerencias con los nombres y sinónimos de las entidades en la barra de búsqueda.
* Información sobre la entidad:
	* Nombre
	* OrphaCode
	* Grupo
	* Tipo
	* OMIM
	* Descripción
	* Sinónimos
	* Datos epidemiológicos
	* Pubmed
	* Clasificación preferente
* Información sobre las entidades padre e hijo:
* Información sobre los genes asociados:
	* Nombre
	* Símbolo
	* Locus
	* Tipo
* Información sobre las relaciones entre entidad y genes:
	* Estatus
	* Tipo
* Información sobre los fenotipos asociados:
	* HPOId
	* Término
* Información sobre las relaciones entre entidad y fenotipos:
	* Frecuencia
	* Criterio
* Listado descendente con la jerarquía
1. Nodos
  * Root:
    * Nodo raiz del grafo, sirve para obetener la clasifición de cada enfermedad.
  * Disorder:
    * Puede representar un grupo de enfermedades, una en concreto o un subtipo.
    * Campos:
      * orphaCode
      * name
      * group
      * type
      * description
      * synonyms
      * OMIM
      * averageAgeOnset
      * averageAgeOfDeath
      * inheritanceType
  * Gene:
    * Genes relacionados con las enfermedades del sistema.
    * Campos:
      * symbol
      * name
      * type
      * locus
      * OMIM
  * Phenotype:
    * Fenotipos relacionados con las enfermedades del sistema.
    * Campos:
      * HPOId
      * Term
2. Relaciones
  * ParentRelation:
    * Relacion entre enfermedades que construye su jerarquía.
    * Campos:
      * type
      * son
  * DisorderGeneRelation / GeneDisorderRelation:
    * Relaciones entre enfermedades y genes. En ambas direcciones para poder realizar diferentes tipos de búsquedas.
    * Campos:
      * type
      * status
      * gene / disorder
  * DisorderPhenotypeRelation / PhenotypeDisorderRelation:
    * Relaciones entre enfermedades y fenotipos. En ambas direcciones para poder realizar diferentes tipos de búsquedas.
    * Campos:
    	* frequency
      	* criteria
      	* phenotype / disorder
3. Repositorios
  * RootRepository:
	* Neo4jRepository<Root, Long>
    	```
    	@Query("MATCH (r:Root) RETURN r")
	public Root findRoot();
	
	@Query("MATCH path = (r:Root)-[:PARENT_RELATION*1..1]->(d:Disorder) RETURN r, collect(relationships(path)), collect(d)")
	public Root findRootClassifications();
    	```
  * DisorderRepository:
  	* Neo4jRepository<Root, Long>
	```
	@Query("MATCH (d:Disorder) RETURN d")
	public List<Disorder> findAllDisorders();
	
	@Query("MATCH path = (p)<-[:ASSOCIATED_WITH_PHENOTYPE*0..1]-(d:Disorder {orphaCode: $orphaCode})-[:ASSOCIATED_WITH_GENE*0..1]->(g) "
			+ "RETURN d, collect(relationships(path)), collect(g), collect(p)")
	public Disorder findDisorderGenesAndPhenotypesByOrphaCode(Integer orphaCode);
	
	@Query("MATCH path = (p)<-[:ASSOCIATED_WITH_PHENOTYPE*0..1]-(d:Disorder)-[:ASSOCIATED_WITH_GENE*0..1]->(g) "
			+ "WHERE d.name = $nameOrSynonym OR $nameOrSynonym in d.synonyms RETURN d, collect(relationships(path)), collect(g), collect(p)")
	public Disorder findDisorderGenesAndPhenotypesByNameOrSynonym(String nameOrSynonym);
	
	@Query("MATCH path = (:Disorder {orphaCode: $orphaCode})<-[:PARENT_RELATION*]-(d:Disorder)<-[:PARENT_RELATION]-(:Root) "
			+ "RETURN d ORDER BY length(path) ASC LIMIT 1")
	public Disorder findDisorderPreferentialClassification(Integer orphaCode);
	
	@Query("MATCH path = (ascen:Disorder)-[:PARENT_RELATION*0..]->(d:Disorder {orphaCode: $orphaCode})-[:PARENT_RELATION*0..]->(descen:Disorder) "
			+ "RETURN d, collect(relationships(path)), collect(ascen), collect(descen)")
	public Disorder findDisorderParentRelations(Integer orphaCode);
	
	@Query("MATCH path = (d {orphaCode: $orphaCode})-[:PARENT_RELATION*]->(sons:Disorder) "
			+ "RETURN d, collect(relationships(path)), collect(sons)")
	public Disorder findDisorderDescendants(Integer orphaCode);
	
	@Query("MATCH path = (d:Disorder {orphaCode: $orphaCode})<-[:PARENT_RELATION*]-(parent:Disorder) "
			+ "RETURN d, collect(relationships(path)), collect(parent)")
	public Disorder findDisorderAscendants(Integer orphaCode);
	
	@Query("MATCH path = (d:Disorder {orphaCode: $orphaCode})-[:ASSOCIATED_WITH_GENE]->(gene:Gene) "
			+ "RETURN d, collect(relationships(path)), collect(gene)")
	public Disorder findDisorderGenes(Integer orphaCode);
	
	@Query("MATCH path = (d:Disorder {orphaCode: $orphaCode})-[:ASSOCIATED_WITH_PHENOTYPE]->(phenotype:Phenotype) "
			+ "RETURN d, collect(relationships(path)), collect(phenotype)")
	public Disorder findDisorderPhenotypes(Integer orphaCode);
	
  * GeneRepository:
  	* Neo4jRepository<Gene, Long>
  ```
  	@Query("MATCH (g:Gene {symbol: $symbol}) RETURN g")
	public Gene findGeneBySymbol(String symbol);
	
	@Query("MATCH path = (g:Gene)<-[:ASSOCIATED_WITH_GENE]-(d:Disorder {orphaCode: $orphaCode}) RETURN g, collect(relationships(path)), collect(d)")
	public List<Gene> findGenesAssociatedToDisorder(Integer orphaCode);
	
	@Query("MATCH path = (g:Gene {symbol: $symbol})<-[:ASSOCIATED_WITH_GENE]-(d:Disorder) "
			+ "RETURN g, collect(relationships(path)), collect(d)")
	public Gene findDisordersAssociatedToGene(String symbol);
  ```
  * PhenotypeRepository:
  	* Neo4jRepository<Phenotype, Long>
  	```
	@Query("MATCH (p:Phenotype {HPOId: $HPOId}) RETURN p")
	public Phenotype findPhenotypeByHPOId(String HPOId);
  	```
