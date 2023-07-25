package com.orphanet.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.neo4j.driver.exceptions.ServiceUnavailableException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.TransientDataAccessResourceException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.orphanet.repositories.DeleteOldVersionRepository;
import com.orphanet.repositories.LoadNewVersionRepository;

@Service
public class VersionsService {

	@Autowired
	private DeleteOldVersionRepository deleteOldVersionRepository;
	
	@Autowired
	private LoadNewVersionRepository loadNewVersionRepository;
	
	@Scheduled(cron = "@monthly")
	public void loadNewVersion(){
		try {
			System.out.println("Iniciando comprobación de nuevas versiones de Orphanet");
			Process process;
			process = Runtime.getRuntime().exec("Rscript src/main/resources/static/rScripts/ParseOrphanet.R");
			BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String s = null;
			while((s = stdInput.readLine()) != null) {
				System.out.println(s);
			}
			if(s == null) {
				int exit = process.waitFor();
				if(exit == 0) {
					System.out.println("No hay versiones nuevas disponibles.");
				}else {
					System.out.println("Nueva versión disponible para cargar en base de datos");
					deleteOldVersionRepository.deleteAllGraph();
					loadNewVersionRepository.loadNewDisorders();
					loadNewVersionRepository.loadNewDisordersSynonyms();
					loadNewVersionRepository.loadNewDisordersFlags();
					loadNewVersionRepository.loadNewDisordersNaturalHistory();
					loadNewVersionRepository.loadNewGenes();
					loadNewVersionRepository.loadNewPhenotypes();
					loadNewVersionRepository.loadNewParentRelations();
					loadNewVersionRepository.loadNewPreferentialParentRelations();
					loadNewVersionRepository.loadNewAssociatedWithGeneRelations();
					loadNewVersionRepository.loadNewAssociatedWithPhenotypeRelations();
				}
			}
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		} catch (TransientDataAccessResourceException e) {
			throw new ServiceUnavailableException("Conexion con base de datos rechazada");
		}
	}
	
	public void getVersions() throws IOException{
		Path dir = Paths.get("src/main/resources/static/rScripts/Versiones");

		try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
		    for (@SuppressWarnings("unused") Path file : stream) {
		        
		    }
		}
	}
}
