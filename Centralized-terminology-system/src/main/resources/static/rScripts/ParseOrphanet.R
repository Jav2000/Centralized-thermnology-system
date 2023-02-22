library(xml2)
library(dplyr)
library(purrr)

# Main
load("src/main/resources/static/rScripts/Versiones/LastVersion.RData")
check_new_versions()

check_new_versions <- function(){
  if(as.Date(actual_date) < as.Date(xml_attr(read_xml("https://www.orphadata.com/data/xml/en_product1.xml"), "date"))){
    actual_date <<- as.character(as.Date(xml_attr(read_xml("https://www.orphadata.com/data/xml/en_product1.xml"), "date")))
    parse_orphanet()
    print(paste0("Version nueva disponible en la carpeta ", actual_date))
    return(1)
  }
  print("No hay versiones nuevas disponibles")
  return(0)
}

parse_orphanet <- function(){
  dir.create(paste0("src/main/resources/static/rScripts/Versiones/", actual_date))
  actual_disorders <<- parse_disorders()
  actual_disorders_synonyms <<- parse_disorders_synonyms()
  actual_disorders_flags <<- parse_disorders_flags()
  actual_disorders_natural_history <<- parse_disorders_natural_history()
  actual_parent_relations <<- parse_parent_relations()
  actual_preferential_parent_relations <<- parse_preferential_parent_relations()
  actual_genes <<- parse_genes()
  actual_associated_with_gene_relations <<- parse_genes_relations()
  actual_phenotypes <<- parse_phenotypes()
  actual_associated_with_phenotype_relations <<- parse_phenotypes_relations()
  save.image("src/main/resources/static/rScripts/Versiones/LastVersion.RData")
  
  database_import_folder <- "/Users/javierrodriguezsanchez/Library/Application Support/Neo4j Desktop/Application/relate-data/dbmss/dbms-77bfd498-475f-4b44-852e-c3f26f82a067/import/"
  write.table(actual_disorders, paste0(database_import_folder, "Orphanet_disorders.csv"), row.names = F, sep = "|", na = "")
  write.table(actual_disorders_synonyms, paste0(database_import_folder, "Orphanet_disorders_synonyms.csv"), row.names = F, sep = "|", na = "")
  write.table(actual_disorders_flags, paste0(database_import_folder, "Orphanet_disorders_flags.csv"), row.names = F, sep = "|", na = "")
  write.table(actual_disorders_natural_history, paste0(database_import_folder, "Orphanet_disorders_natural_history.csv"), row.names = F, sep = "|", na = "")
  write.table(actual_parent_relations, paste0(database_import_folder, "Orphanet_parent_relations.csv"), row.names = F, sep = "|", na = "")
  write.table(actual_preferential_parent_relations, paste0(database_import_folder, "Orphanet_preferential_parent_relations.csv"), row.names = F, sep = "|", na = "")
  write.table(actual_genes, paste0(database_import_folder, "Orphanet_genes.csv"), row.names = F, sep = "|", na = "")
  write.table(actual_associated_with_gene_relations, paste0(database_import_folder, "Orphanet_associated_with_gene_relations.csv"), row.names = F, sep = "|", na = "")
  write.table(actual_phenotypes, paste0(database_import_folder, "Orphanet_phenotype.csv"), row.names = F, sep = "|", na = "")
  write.table(actual_associated_with_phenotype_relations, paste0(database_import_folder, "Orphanet_associated_with_phenotype_relations.csv"), row.names = F, sep = "|", na = "")
}

parse_disorders <- function(){
  print("Parse entities")
  xml <- read_xml("https://www.orphadata.com/data/xml/en_product1.xml")
  
  disorders_list <- xml_child(xml, "DisorderList")
  number_of_disorders <- xml_attr(disorders_list, "count")
  pb <- txtProgressBar(min = 0,      # Valor minimo de la barra de progreso
                       max = as.integer(number_of_disorders),       # Valor maximo de la barra de progreso
                       style = 3,    # Estilo de la barra (tambien style = 1 y style = 2)
                       width = 50,   # Ancho de la barra. Por defecto: getOption("width")
                       char = "=")   # Caracter usado para crear la barra
  progress <- 1
  disorders <- tibble( OrphaCode = numeric(),
                       Name = character(),
                       Group = character(),
                       Type = character(),
                       Description = character(),
                       OMIM = numeric())
  for(i in 1:number_of_disorders){
    disorder <- xml_child(disorders_list, i)
    flag_list <- xml_integer(xml_find_all(disorder, "DisorderFlagList/DisorderFlag/Value"))
    # Se descartan las enfermedades raras que esten obsoletas, inactivas o no reconocidas en europa.
    # if(length(intersect(c(16, 32, 256, 1024, 8192), flag_list)) == 0){
    external_references <- xml_child(disorder, "ExternalReferenceList")
    if(xml_attr(external_references, "count") != 0){
      omim <- NA
      for(j in 1:xml_attr(external_references, "count")){
        external_reference <- xml_child(external_references, j)
        if(xml_text(xml_child(external_reference, "Source")) == "OMIM"){
          omim <- xml_integer(xml_child(external_reference, "Reference"))
        }
      }
    }
    summaries <- xml_child(disorder, "SummaryInformationList")
    number_of_summaries <- xml_attr(summaries, "count")
    if(number_of_summaries != 0){
      desc <- xml_text(xml_child(xml_child(summaries, 1), "TextSectionList/TextSection/Contents"))
    }else{
      desc <- NA
    }
    tibble <- tibble(OrphaCode = xml_integer(xml_child(disorder, "OrphaCode")),
                     Name = xml_text(xml_child(disorder, "Name")),
                     Group = xml_text(xml_child(disorder, "DisorderGroup/Name")),
                     Type = xml_text(xml_child(disorder, "DisorderType/Name")),
                     description = desc,
                     OMIM = omim)
    disorders <- rbind(disorders, tibble)
    # }
    progress <- progress + 1
    setTxtProgressBar(pb, progress)
  }
  close(pb)
  write.table(disorders, paste0("src/main/resources/static/rScripts/Versiones/", actual_date, "Orphanet_active_entities.csv"), row.names = F, sep = "|", na = "")
  return(disorders)
}

parse_disorders_synonyms <- function(){
  print("Parse entities synonyms")
  xml <- read_xml("https://www.orphadata.com/data/xml/en_product1.xml")
  disorders_list <- xml_child(xml, "DisorderList")
  number_of_disorders <- xml_attr(disorders_list, "count")
  pb <- txtProgressBar(min = 0,      # Valor minimo de la barra de progreso
                       max = as.integer(number_of_disorders),       # Valor maximo de la barra de progreso
                       style = 3,    # Estilo de la barra (tambien style = 1 y style = 2)
                       width = 50,   # Ancho de la barra. Por defecto: getOption("width")
                       char = "=")   # Caracter usado para crear la barra
  progress <- 1
  synonyms_list <- tibble(OrphaCode = numeric(),
                          Synonym = character())
  for(i in 1:number_of_disorders){
    disorder <- xml_child(disorders_list, i)
    orphaCode <- xml_integer(xml_child(disorder, "OrphaCode"))
    synonyms <- xml_child(disorder, "SynonymList")
    number_of_synonyms <- xml_attr(synonyms, "count")
    if(number_of_synonyms > 0){
      for(j in 1:number_of_synonyms){
        synonyms_list <- rbind(synonyms_list, tibble(OrphaCode = orphaCode,
                                                     Synonym = xml_text(xml_child(synonyms, j))))
      }
    }
    progress <- progress + 1
    setTxtProgressBar(pb, progress)
  }
  close(pb)
  write.table(synonyms_list, paste0("src/main/resources/static/rScripts/Versiones/", actual_date, "Orphanet_entities_synonyms.csv"), row.names = F, sep = "|", na = "")
  return(synonyms_list)
}

parse_disorders_flags <- function(){
  print("Parse entities flags")
  xml <- read_xml("https://www.orphadata.com/data/xml/en_product1.xml")
  
  disorders_list <- xml_child(xml, "DisorderList")
  number_of_disorders <- xml_attr(disorders_list, "count")
  pb <- txtProgressBar(min = 0,      # Valor minimo de la barra de progreso
                       max = as.integer(number_of_disorders),       # Valor maximo de la barra de progreso
                       style = 3,    # Estilo de la barra (tambien style = 1 y style = 2)
                       width = 50,   # Ancho de la barra. Por defecto: getOption("width")
                       char = "=")   # Caracter usado para crear la barra
  progress <- 1
  disorders <- tibble( OrphaCode = numeric(),
                       Flag = numeric())
  for(i in 1:number_of_disorders){
    disorder <- xml_child(disorders_list, i)
    flags <- xml_child(disorder, "DisorderFlagList")
    number_of_flags <- xml_attr(flags, "count")
    for(j in 1: number_of_flags){
      disorders <- rbind(disorders, tibble(OrphaCode = xml_integer(xml_child(disorder, "OrphaCode")),
                                           Flag = xml_integer(xml_child(xml_child(flags, j), "Value"))))
    }
    progress <- progress + 1
    setTxtProgressBar(pb, progress)
  }
  close(pb)
  write.table(disorders, paste0("src/main/resources/static/rScripts/Versiones/", actual_date, "Orphanet_entities_flags.csv"), row.names = F, sep = "|", na = "")
  return(disorders)
}

parse_disorders_natural_history <- function(){
  print("Parse entities natural history")
  xml <- read_xml("https://www.orphadata.com/data/xml/en_product9_ages.xml")
  disorder_list <- xml_child(xml, "DisorderList")
  number_of_disorders <- xml_attr(disorder_list, "count")
  pb <- txtProgressBar(min = 0,      # Valor minimo de la barra de progreso
                       max = as.integer(number_of_disorders),       # Valor maximo de la barra de progreso
                       style = 3,    # Estilo de la barra (tambien style = 1 y style = 2)
                       width = 50,   # Ancho de la barra. Por defecto: getOption("width")
                       char = "=")   # Caracter usado para crear la barra
  progress <- 1
  disorders_natural_history <- tibble(OrphaCode = numeric(),
                                      AverageAgeOfOnset = character(),
                                      AverageAgeOfDeath = character(),
                                      InheritanceTypes = character())
  for(i in 1:number_of_disorders){
    disorder <- xml_child(disorder_list, i)
    average_age_of_onset <- paste(xml_text(xml_find_all(disorder, "AverageAgeOfOnsetList/AverageAgeOfOnset/Name")), collapse = ", ")
    average_age_of_death <- paste(xml_text(xml_find_all(disorder, "AverageAgeOfDeathList/AverageAgeOfDeath/Name")), collapse = ", ")
    inheritance_types <- paste(xml_text(xml_find_all(disorder, "TypeOfInheritanceList/TypeOfInheritance/Name")), collapse = ", ")
    disorders_natural_history <- rbind(disorders_natural_history, tibble(OrphaCode = xml_integer(xml_child(disorder, "OrphaCode")),
                                                                         AverageAgeOfOnset = average_age_of_onset,
                                                                         AverageAgeOfDeath = average_age_of_death,
                                                                         InheritanceTypes = inheritance_types))
    progress <- progress + 1
    setTxtProgressBar(pb, progress)
  }
  close(pb)
  write.table(disorders_natural_history, paste0("src/main/resources/static/rScripts/Versiones/", actual_date, "Orphanet_entities_natural_history.csv"), row.names = F, sep = "|", na = "")
  return(disorders_natural_history)
}

parse_classification <- function(xml, parent){
  if(parent == 0){
    if(xml_integer(xml_child(xml, "ClassificationList/Classification/OrphaNumber")) == 616871){
      xml <- xml_child(xml, "ClassificationList/Classification/ClassificationNodeRootList/ClassificationNode/ClassificationNodeChildList")
      return(tibble(Parent_OrphaCode = parent,
                    OrphaCode = xml_integer(xml_child(xml_child(xml, 1), "Disorder/OrphaCode"))))
    }else{
      xml <- xml_child(xml, "ClassificationList/Classification/ClassificationNodeRootList/ClassificationNode")
    }
  }
  disorder <- tibble( Parent_OrphaCode = parent,
                      OrphaCode = xml_integer(xml_child(xml, "Disorder/OrphaCode")))
  parent <- xml_integer(xml_child(xml, "Disorder/OrphaCode"))
  childs <- xml_child(xml, "ClassificationNodeChildList")
  number_child_nodes <- as.integer(xml_attr(childs, "count"))
  if(number_child_nodes > 0){
    disorders <- tibble(Parent_OrphaCode = numeric(),
                        OrphaCode = numeric())
    for(i in 1:number_child_nodes){
      disorders_loop <- parse_classification(xml_child(childs, i), parent)
      disorders <- rbind(disorders_loop, disorders)
    }
    disorders <- rbind(disorder, disorders)
  }else{
    disorders <- disorder
  }
  return(disorders)
}

parse_parent_relations <- function(){
  print("Parse parent relations")
  files_vector <- c("en_product3_182", "en_product3_212", "en_product3_199", "en_product3_146", "en_product3_148",
                    "en_product3_198", "en_product3_147", "en_product3_204", "en_product3_235", "en_product3_193", 
                    "en_product3_152", "en_product3_156", "en_product3_205", "en_product3_194", "en_product3_183", 
                    "en_product3_195", "en_product3_150", "en_product3_203", "en_product3_201", "en_product3_202", 
                    "en_product3_181", "en_product3_197", "en_product3_189", "en_product3_200", "en_product3_188", 
                    "en_product3_184", "en_product3_187", "en_product3_209", "en_product3_186", "en_product3_231", 
                    "en_product3_196", "en_product3_216", "en_product3_233", "en_product3_185")
  classifications <- tibble(Parent_OrphaCode = numeric(),
                            OrphaCode = numeric())
  pb <- txtProgressBar(min = 0,      # Valor minimo de la barra de progreso
                       max = length(files_vector),       # Valor maximo de la barra de progreso
                       style = 3,    # Estilo de la barra (tambien style = 1 y style = 2)
                       width = 50,   # Ancho de la barra. Por defecto: getOption("width")
                       char = "=")   # Caracter usado para crear la barra
  progress <- 1
  for(i in 1:length(files_vector)){
    classifications <- rbind(classifications, parse_classification(read_xml(paste("https://www.orphadata.com/data/xml/", files_vector[i], ".xml", sep = "")), 0) |> unique())
    progress <- progress + 1
    setTxtProgressBar(pb, progress)
  }
  close(pb)
  classifications <- classifications |> distinct(`OrphaCode`, `Parent_OrphaCode`, .keep_all = T)
  write.table(classifications, paste0("src/main/resources/static/rScripts/Versiones/", actual_date, "Orphanet_parent_relations.csv"), row.names = F, sep = "|")
  return(classifications)
}

parse_preferential_parent_relations <- function(){
  print("Parse preferential parent relations")
  xml <- read_xml("https://www.orphadata.com/data/xml/en_product7.xml")
  linearizations <- xml_child(xml, "DisorderList")
  number_of_linearizations <- xml_attr(linearizations, "count")
  pb <- txtProgressBar(min = 0,      # Valor minimo de la barra de progreso
                       max = as.integer(number_of_linearizations),       # Valor maximo de la barra de progreso
                       style = 3,    # Estilo de la barra (tambien style = 1 y style = 2)
                       width = 50,   # Ancho de la barra. Por defecto: getOption("width")
                       char = "=")   # Caracter usado para crear la barra
  progress <- 1
  linearization_rules <- tibble( OrphaCode = numeric(),
                                 Preferential_Parent_OrphaCode = numeric())
  for(i in 1:number_of_linearizations){
    disorder <- xml_child(linearizations, i)
    linearization_rules <- rbind(linearization_rules, tibble( OrphaCode = xml_integer(xml_child(disorder, "OrphaCode")),
                                                              Preferential_Parent_OrphaCode = xml_integer(xml_child(disorder, "DisorderDisorderAssociationList/DisorderDisorderAssociation/TargetDisorder/OrphaCode"))))
    progress <- progress + 1
    setTxtProgressBar(pb, progress)
  }
  close(pb)
  linearization_rules <- linearization_rules |> filter(!is.na(`Preferential_Parent_OrphaCode`))
  write.table(linearization_rules, paste0("src/main/resources/static/rScripts/Versiones/", actual_date, "Orphanet_preferential_parent_relations.csv"), row.names = F, sep = "|")
  return(linearization_rules)
}

parse_genes <- function(){
  print("Parse genes")
  xml <- read_xml("https://www.orphadata.com/data/xml/en_product6.xml")
  disorders <- xml_child(xml, "DisorderList")
  number_of_disorders <- xml_attr(disorders, "count")
  pb <- txtProgressBar(min = 0,      # Valor minimo de la barra de progreso
                       max = as.integer(number_of_disorders),       # Valor maximo de la barra de progreso
                       style = 3,    # Estilo de la barra (tambien style = 1 y style = 2)
                       width = 50,   # Ancho de la barra. Por defecto: getOption("width")
                       char = "=")   # Caracter usado para crear la barra
  progress <- 1
  genes_table <- tibble(Name = character(),
                        Symbol = character(),
                        Type = character(),
                        Locus = character(),
                        OMIM = numeric())
  for(i in 1:number_of_disorders){
    disorder <- xml_child(disorders, i)
    genes <- xml_child(disorder, "DisorderGeneAssociationList")
    number_of_genes <- xml_attr(genes, "count")
    for(j in 1:number_of_genes){
      association <- xml_child(genes, j)
      gene <- xml_child(association, "Gene")
      external_references <- xml_child(gene, "ExternalReferenceList")
      number_external_references <- xml_attr(external_references, "count")
      has_omim <- F
      if(number_external_references != 0){
        for(k in 1:number_external_references){
          external_reference <- xml_child(external_references, k)
          if(xml_text(xml_child(external_reference, "Source")) == "OMIM"){
            has_omim <- T
            gene_tibble <- tibble(Name = xml_text(xml_child(gene, "Name")),
                                  Symbol = xml_text(xml_child(gene, "Symbol")),
                                  Type = xml_text(xml_child(gene, "GeneType/Name")),
                                  Locus = xml_text(xml_child(gene, "LocusList/Locus/GeneLocus")),
                                  OMIM = xml_integer(xml_child(external_reference, "Reference")))
          }
        }
      }
      if(!has_omim){
        gene_tibble <- tibble(Name = xml_text(xml_child(gene, "Name")),
                              Symbol = xml_text(xml_child(gene, "Symbol")),
                              Type = xml_text(xml_child(gene, "GeneType/Name")),
                              Locus = xml_text(xml_child(gene, "LocusList/Locus/GeneLocus")),
                              OMIM = NA)
      }
      genes_table <- rbind(genes_table, gene_tibble)
    }
    progress <- progress + 1
    setTxtProgressBar(pb, progress)
  }
  close(pb)
  genes_table <- genes_table |> unique()
  write.table(genes_table, paste0("src/main/resources/static/rScripts/Versiones/", actual_date, "Orphanet_genes.csv"), row.names = F, sep = "|", na = "")
  return(genes_table)
}

parse_genes_relations <- function(){
  print("Parse associated with gene relations")
  xml <- read_xml("https://www.orphadata.com/data/xml/en_product6.xml")
  disorders <- xml_child(xml, "DisorderList")
  number_of_disorders <- xml_attr(disorders, "count")
  pb <- txtProgressBar(min = 0,      # Valor minimo de la barra de progreso
                       max = as.integer(number_of_disorders),       # Valor maximo de la barra de progreso
                       style = 3,    # Estilo de la barra (tambien style = 1 y style = 2)
                       width = 50,   # Ancho de la barra. Por defecto: getOption("width")
                       char = "=")   # Caracter usado para crear la barra
  progress <- 1
  genes_disorders_association <- tibble(OrphaCode = numeric(), 
                                        Gene_Symbol = character(),
                                        Relationship_Type = character(),
                                        Status = character())
  for(i in 1:number_of_disorders){
    disorder <- xml_child(disorders, i)
    genes <- xml_child(disorder, "DisorderGeneAssociationList")
    number_of_genes <- xml_attr(genes, "count")
    for(j in 1:number_of_genes){
      association <- xml_child(genes, j)
      gene <- xml_child(association, "Gene")
      genes_disorders_association <- rbind(genes_disorders_association, tibble(OrphaCode = xml_integer(xml_child(disorder, "OrphaCode")),
                                                                               Gene_Symbol = xml_text(xml_child(gene, "Symbol")),
                                                                               Relationship_Type = xml_text(xml_child(association, "DisorderGeneAssociationType/Name")),
                                                                               Status = xml_text(xml_child(association, "DisorderGeneAssociationStatus/Name"))))
    }
    progress <- progress + 1
    setTxtProgressBar(pb, progress)
  }
  close(pb)
  write.table(genes_disorders_association, paste0("src/main/resources/static/rScripts/Versiones/", actual_date, "Orphanet_associated_with_gene_relations.csv"), row.names = F, sep = "|")
  return(genes_disorders_association)
}

parse_phenotypes <- function(){
  print("Parse phenotypes")
  xml <- read_xml("https://www.orphadata.com/data/xml/en_product4.xml")
  hpo_disorder_status_list <- xml_child(xml, "HPODisorderSetStatusList")
  hpos_id <- xml_text(xml_find_all(hpo_disorder_status_list, "HPODisorderSetStatus/Disorder/HPODisorderAssociationList/HPODisorderAssociation/HPO/HPOId"))
  hpos_term <- xml_text(xml_find_all(hpo_disorder_status_list, "HPODisorderSetStatus/Disorder/HPODisorderAssociationList/HPODisorderAssociation/HPO/HPOTerm"))
  hpos <- tibble(HPOId = hpos_id, HPOTerm = hpos_term) |> unique()
  write.table(hpos, paste0("./Versiones/", actual_date, "Orphanet_phenotypes.csv"), row.names = F, sep = "|", na = "")
  return(hpos)
}

parse_phenotypes_relations <- function(){
  print("Parse associated with phenotype relations")
  xml <- read_xml("https://www.orphadata.com/data/xml/en_product4.xml")
  hpo_disorder_status_list <- xml_child(xml, "HPODisorderSetStatusList")
  number_of_hpo_disorder_associations <- xml_attr(hpo_disorder_status_list, "count")
  pb <- txtProgressBar(min = 0,      # Valor minimo de la barra de progreso
                       max = as.integer(number_of_hpo_disorder_associations),       # Valor maximo de la barra de progreso
                       style = 3,    # Estilo de la barra (tambien style = 1 y style = 2)
                       width = 50,   # Ancho de la barra. Por defecto: getOption("width")
                       char = "=")   # Caracter usado para crear la barra
  progress <- 1
  hpo_disorder_association_list <- tibble(OrphaCode = numeric(),
                                          HPOId = character(),
                                          Frequency = character(),
                                          Criteria = character())
  for(i in 1:number_of_hpo_disorder_associations){
    disorder <- xml_child(xml_child(hpo_disorder_status_list, i), "Disorder")
    orphaCode <- xml_integer(xml_child(disorder, "OrphaCode"))
    hpos <- xml_child(disorder, "HPODisorderAssociationList")
    number_of_hpos <- xml_attr(hpos, "count")
    if(number_of_hpos > 0){
      for(j in 1:number_of_hpos){
        hpo_disorder_association <- xml_child(hpos, j)
        hpoId <- xml_text(xml_child(hpo_disorder_association, "HPO/HPOId"))
        hpoFrecuency <- xml_text(xml_child(hpo_disorder_association, "HPOFrequency/Name"))
        hpoCriteria <- xml_text(xml_child(hpo_disorder_association, "DiagnosticCriteria/Name"))
        hpo_disorder_association_list <- rbind(hpo_disorder_association_list, tibble(OrphaCode = orphaCode,
                                                                                     HPOId = hpoId,
                                                                                     Frequency = hpoFrecuency,
                                                                                     Criteria = hpoCriteria))
      }
    }
    progress <- progress + 1
    setTxtProgressBar(pb, progress)
  }
  close(pb)
  hpo_disorder_association_list <- hpo_disorder_association_list |> unique()
  write.table(hpo_disorder_association_list, paste0("src/main/resources/static/rScripts/Versiones/", actual_date, "Orphanet_associated_with_phenotype_relations.csv"), row.names = F, sep = "|", na = "")
  return(hpo_disorder_association_list)
}

