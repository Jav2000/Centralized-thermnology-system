package com.orphanet.services;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import org.json.JSONObject;
import org.neo4j.driver.exceptions.ServiceUnavailableException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.TransientDataAccessResourceException;
import org.springframework.stereotype.Service;

import com.orphanet.neo4j.data.nodes.Disorder;
import com.orphanet.neo4j.data.relationships.DisorderGeneRelation;
import com.orphanet.neo4j.data.relationships.DisorderPhenotypeRelation;
import com.orphanet.neo4j.data.relationships.ParentRelation;
import com.orphanet.repositories.DisorderRepository;

@Service
public class DisorderService {
	
	private final String TOKEN_ENPOINT = "https://icdaccessmanagement.who.int/connect/token";
	private final String CLIENT_ID = "e72c241e-2a29-4638-b9d7-1908680ccec3_b7f65dd9-37ce-455c-8108-fa70b12e2fc2";
	private final String CLIENT_SECRET = "p0/ZzDHSkeGkxTfXWz9UJAmRlQfxZ6NsAoRylxBDbuA=";
	private final String SCOPE = "icdapi_access";
	private final String GRANT_TYPE = "client_credentials";

	@Autowired
	private DisorderRepository disorderRepository;
	
	public List<String> findAllDisorderNamesAndSynonyms() throws IOException {
		try {
			List<Disorder> disorders;
			List<String> result = new ArrayList<>();
			disorders = disorderRepository.findAllDisorders();
			for(int i = 0; i < disorders.size(); i++) {
				Disorder disorder = disorders.get(i);
				result.add(disorder.getName());
				if(disorder.getSynonyms() != null && !disorder.getSynonyms().isEmpty()) {
					for(int j= 0; j < disorder.getSynonyms().size(); j++) {
						result.add(disorder.getSynonyms().get(j));
					}
				}
			}
			return result;
		}catch (TransientDataAccessResourceException e) {
			throw new ServiceUnavailableException("Conexion con base de datos rechazada");
		}
	}
	
	public Disorder findDisorderSearchInformationByNameOrSynonym(String nameOrSynonym) throws Exception {
		try {
			Disorder disorder = disorderRepository.findDisorderByNameOrSynonym(nameOrSynonym);
			if(disorder.getOrphaCode() != null) {
				disorder = disorderRepository.findOrphanetDisorderGenesAndPhenotypesByNameOrSynonym(nameOrSynonym);
				Disorder preferentialClassification = disorderRepository.findDisorderPreferentialClassification(disorder.getOrphaCode());
				Disorder disorderHierarchy = disorderRepository.findDisorderAscendantsAndDescendants(disorder.getOrphaCode());
				disorder.setAscendants(disorderHierarchy.getAscendants());
				disorder.setDescendants(disorderHierarchy.getDescendants());
				if(preferentialClassification != null) {
					disorder.setPreferentialClassification(preferentialClassification.getName());
				}
			}else {
				
			}
			return disorder;
		}catch (TransientDataAccessResourceException e) {
			throw new ServiceUnavailableException("Conexion con base de datos rechazada");
		}
	}
	
	public Disorder findOrphanetDisorderByOrphaCode(Integer orphaCode) throws IOException {
		try{
			Disorder disorder = disorderRepository.findOrphanetDisorderGenesAndPhenotypesByOrphaCode(orphaCode);
			Disorder preferentialClassification = disorderRepository.findDisorderPreferentialClassification(orphaCode);
			Disorder disorderHierarchy = disorderRepository.findDisorderAscendantsAndDescendants(orphaCode);
			disorder.setAscendants(disorderHierarchy.getAscendants());
			disorder.setDescendants(disorderHierarchy.getDescendants());
			if(preferentialClassification != null) {
				disorder.setPreferentialClassification(preferentialClassification.getName());
			}
			return disorder;
		}catch (TransientDataAccessResourceException e) {
			throw new ServiceUnavailableException("Conexion con base de datos rechazada");
		}
	}
	
	public Disorder findICDDisorderByICDCode(String ICDCode) throws Exception {
		try {
			Disorder disorder = disorderRepository.findICDDisorderByICDCode(ICDCode);
			String uri = disorder.getICD_URI();
			String token = getToken();
			System.out.println("URI Response JSON : \n" + getURI(token, uri));
			return disorder;
		}catch (TransientDataAccessResourceException e) {
			throw new ServiceUnavailableException("Conexion con base de datos rechazada");
		}
	}
	
	public Map<String, List<Map<String, Object>>> findOrphanetDisorderGraph(Integer orphaCode) throws IOException{
		try {
			List<Map<String, Object>> nodes = new ArrayList<>();
			List<Map<String, Object>> links = new ArrayList<>();
			
			Disorder rootDisorder = findOrphanetDisorderByOrphaCode(orphaCode);
			nodes.add(Map.of(	"id", 0,"typeOfNode", "disorder", "name", rootDisorder.getName(),
								"orphaCode", rootDisorder.getOrphaCode(), "type", rootDisorder.getType(),
								"group", rootDisorder.getGroup()));
			int currentIndex = 1;
			for(int i = 0; i < rootDisorder.getDescendants().size(); i++) {
				ParentRelation parentRel = rootDisorder.getDescendants().get(i);
				nodes.add(Map.of(	"id", currentIndex, "typeOfNode", "disorder", "name", parentRel.getSon().getName(),
									"orphaCode", parentRel.getSon().getOrphaCode(), "type", parentRel.getSon().getType(),
									"group", parentRel.getSon().getGroup(), "OMIM", parentRel.getSon().getOMIM()));
				links.add(Map.of("source", 0, "target", currentIndex, "type", parentRel.getType()));
				currentIndex++;
			}
			
			for(int i = 0; i < rootDisorder.getGenes().size(); i++) {
				DisorderGeneRelation geneRel = rootDisorder.getGenes().get(i);
				nodes.add(Map.of(	"id", currentIndex, "typeOfNode", "gene", "name", geneRel.getGene().getName(),
									"symbol", geneRel.getGene().getSymbol(), "type", geneRel.getGene().getType(),
									"locus", geneRel.getGene().getLocus(), "OMIM", geneRel.getGene().getOMIM()));
				links.add(Map.of("source", 0, "target", currentIndex, "status", geneRel.getStatus(), "type", geneRel.getType()));
				currentIndex++;
			}
			
			for(int i = 0; i < rootDisorder.getPhenotypes().size(); i++) {
				DisorderPhenotypeRelation phenotypeRel = rootDisorder.getPhenotypes().get(i);
				nodes.add(Map.of(	"id", currentIndex, "typeOfNode", "phenotype", "HPOId", phenotypeRel.getPhenotype().getHPOId(),
									"term", phenotypeRel.getPhenotype().getTerm()));
				links.add(Map.of("source", 0, "target", currentIndex, "criteria", phenotypeRel.getCriteria(), "frequency", phenotypeRel.getFrequency()));
				currentIndex++;
			}
			return Map.of("nodes", nodes, "links", links);
		}catch (TransientDataAccessResourceException e) {
			throw new ServiceUnavailableException("Conexion con base de datos rechazada");
		}
	}
	
	// get the OAUTH2 token
	private String getToken() throws Exception {

		System.out.println("Getting token...");

		URL url = new URL(TOKEN_ENPOINT);
		HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
		con.setRequestMethod("POST");

		// set parameters to post
		String urlParameters =
	        	"client_id=" + URLEncoder.encode(CLIENT_ID, "UTF-8") +
	        	"&client_secret=" + URLEncoder.encode(CLIENT_SECRET, "UTF-8") +
				"&scope=" + URLEncoder.encode(SCOPE, "UTF-8") +
				"&grant_type=" + URLEncoder.encode(GRANT_TYPE, "UTF-8");
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(urlParameters);
		wr.flush();
		wr.close();

		// response
		int responseCode = con.getResponseCode();
		System.out.println("Token Response Code : " + responseCode + "\n");

		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		// parse JSON response
		JSONObject jsonObj = new JSONObject(response.toString());
		return jsonObj.getString("access_token");
	}
	// access ICD API
	private String getURI(String token, String uri) throws Exception {

		System.out.println("Getting URI...");

		URL url = new URL(uri);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("GET");

		// HTTP header fields to set
		con.setRequestProperty("Authorization", "Bearer "+ token);
		con.setRequestProperty("Accept", "application/json");
		con.setRequestProperty("Accept-Language", "en");
		con.setRequestProperty("API-Version", "v2");

		// response
		int responseCode = con.getResponseCode();
		System.out.println("URI Response Code : " + responseCode + con.getResponseMessage() + con.getHeaderField("Location") + "\n");

		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		return response.toString();
	}
}
