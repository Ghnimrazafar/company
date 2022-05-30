package com.cwiztech.services;

import java.text.ParseException;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.cwiztech.token.AccessToken;
import com.fasterxml.jackson.core.JsonProcessingException;

@Component
public class CompanyService {
	private static final Logger log = LoggerFactory.getLogger(AccessToken.class);
	private static String apigateway;
	private static String companyService;

	public CompanyService(Environment env) {
		CompanyService.apigateway = env.getRequiredProperty("file_path.apigatewayPath")+"service/apigateway";
		CompanyService.companyService = env.getRequiredProperty("file_path.COMPANYSERVICE");
	}

	public static String GET(String URI, String accessToken)
			throws JsonProcessingException, JSONException, ParseException {
		String rtnAPIResponse="Invalid Resonse";
		RestTemplate restTemplate = new RestTemplate();

		HttpHeaders headers = AccessToken.getHttpHeader(accessToken);

		log.info("GET: " + apigateway + ": " + URI);

		String body = "{'service_NAME': '" + companyService + "', 'request_TYPE': 'GET', " +
			  "'request_URI': '" + URI + "', 'request_BODY': ''}";
		
		HttpEntity<String> entity = new HttpEntity<String>(body, headers);
		ResponseEntity<String> response = restTemplate.exchange(apigateway,
				HttpMethod.POST, entity, String.class);
		rtnAPIResponse=response.getBody().toString();

		log.info("Response: " + rtnAPIResponse);
		log.info("----------------------------------------------------------------------------------");
		return rtnAPIResponse;
	}

	public static String POST(String URI, String body, String accessToken)
			throws JsonProcessingException, JSONException, ParseException {
		String rtnAPIResponse="Invalid Resonse";
		RestTemplate restTemplate = new RestTemplate();

		HttpHeaders headers = AccessToken.getHttpHeader(accessToken);

		log.info("POST: " + apigateway + ": " + URI);
		log.info("Body: " + body);

		body = "{\"service_NAME\": \"" + companyService + "\", \"request_TYPE\": \"POST\", " +
				  "\"request_URI\": \"" + URI + "\", \"request_BODY\": \"" + body + "\"}";
			
			HttpEntity<String> entity = new HttpEntity<String>(body, headers);
			ResponseEntity<String> response = restTemplate.exchange(apigateway,
					HttpMethod.POST, entity, String.class);
		rtnAPIResponse=response.getBody().toString();

		log.info("Response: " + rtnAPIResponse);
		log.info("----------------------------------------------------------------------------------");
		return rtnAPIResponse;
	}

	public static String PUT(String URI, String body, String UserName, String Password)
			throws JsonProcessingException, JSONException, ParseException {
		String rtnAPIResponse="Invalid Resonse";
		RestTemplate restTemplate = new RestTemplate();

		JSONObject serviceToken = new JSONObject(AccessToken.findToken(companyService, UserName, Password));
		HttpHeaders headers = AccessToken.getHttpHeader(serviceToken.getString("access_token"));
		String appPath = serviceToken.getString("applicationservice_PATH");

		log.info("PUT: " + apigateway + ": " + URI);
		log.info("Body: " + body);

		HttpEntity<String> entity = new HttpEntity<String>(body.toString(), headers);
		ResponseEntity<String> response = restTemplate.exchange(appPath + URI,
				HttpMethod.PUT, entity, String.class);
		rtnAPIResponse=response.getBody().toString();

		log.info("Response: " + rtnAPIResponse);
		log.info("----------------------------------------------------------------------------------");
		return rtnAPIResponse;
	}

	public static String DELETE(String URI, String UserName, String Password)
			throws JsonProcessingException, JSONException, ParseException {
		String rtnAPIResponse="Invalid Resonse";
		RestTemplate restTemplate = new RestTemplate();

		JSONObject serviceToken = new JSONObject(AccessToken.findToken(companyService, UserName, Password));
		HttpHeaders headers = AccessToken.getHttpHeader(serviceToken.getString("access_token"));
		String appPath = serviceToken.getString("applicationservice_PATH");

		log.info("DELETE: " + apigateway + ": " + URI);

		HttpEntity<String> entity = new HttpEntity<String>(headers);
		ResponseEntity<String> response = restTemplate.exchange(appPath + URI,
				HttpMethod.DELETE, entity, String.class);
		rtnAPIResponse=response.getBody().toString();

		log.info("Response: " + rtnAPIResponse);
		log.info("----------------------------------------------------------------------------------");
		return rtnAPIResponse;
	}
}
