package com.cwiztech.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cwiztech.datalogs.model.APIRequestDataLog;
import com.cwiztech.datalogs.model.DatabaseTables;
import com.cwiztech.datalogs.model.tableDataLogs;
import com.cwiztech.datalogs.repository.apiRequestDataLogRepository;
import com.cwiztech.datalogs.repository.databaseTablesRepository;
import com.cwiztech.datalogs.repository.tableDataLogRepository;
import com.cwiztech.model.CompanySubtype;
import com.cwiztech.model.Organization;
import com.cwiztech.repository.organizationRepository;
import com.cwiztech.token.AccessToken;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@CrossOrigin
@RequestMapping("/organization")
public class organizationController {
	
		
			private static final Logger log = LoggerFactory.getLogger(organizationController.class);
			
			@Autowired
			private organizationRepository organizationrepository;
			
			@Autowired
			private apiRequestDataLogRepository apirequestdatalogRepository;
			
			@Autowired
			private tableDataLogRepository tbldatalogrepository;
			
			@Autowired
			private databaseTablesRepository databasetablesrepository;
				
			@SuppressWarnings({ "unchecked", "rawtypes" })
			@RequestMapping(method = RequestMethod.GET)
			public ResponseEntity get(@RequestHeader(value = "Authorization") String headToken) throws JsonProcessingException, JSONException, ParseException  {
				APIRequestDataLog apiRequest = checkToken("GET", "/organization/all", null, null, headToken);
				if (apiRequest.getREQUEST_STATUS() != null) return new ResponseEntity(apiRequest.getREQUEST_OUTPUT(), HttpStatus.BAD_REQUEST);

				List<Organization> Organization = organizationrepository.findActive();		
				return new ResponseEntity(getAPIResponse(Organization, null, null, apiRequest, false).getREQUEST_OUTPUT(), HttpStatus.OK);
			}

			@SuppressWarnings({ "unchecked", "rawtypes" })
			@RequestMapping(value = "/all", method = RequestMethod.GET)
			public ResponseEntity getAll(@RequestHeader(value = "Authorization") String headToken) throws JsonProcessingException, JSONException, ParseException {
				APIRequestDataLog apiRequest = checkToken("GET", "/Organization/all", null, null, headToken);
				if (apiRequest.getREQUEST_STATUS() != null) return new ResponseEntity(apiRequest.getREQUEST_OUTPUT(), HttpStatus.BAD_REQUEST);

				List<Organization> Organization = organizationrepository.findAll();
				
				return new ResponseEntity(getAPIResponse(Organization, null, null, apiRequest, false).getREQUEST_OUTPUT(), HttpStatus.OK);
			}
			

			@SuppressWarnings({ "unchecked", "rawtypes" })
			@RequestMapping(value = "/{id}", method = RequestMethod.GET)
			public ResponseEntity getOne(@PathVariable Long id, @RequestHeader(value = "Authorization") String headToken) throws JsonProcessingException, JSONException, ParseException {
				APIRequestDataLog apiRequest = checkToken("GET", "/Organization/"+id, null, null, headToken);
				if (apiRequest.getREQUEST_STATUS() != null) return new ResponseEntity(apiRequest.getREQUEST_OUTPUT(), HttpStatus.BAD_REQUEST);

				Organization Organization = organizationrepository.findOne(id);
				
				return new ResponseEntity(getAPIResponse(null, Organization, null, apiRequest, false).getREQUEST_OUTPUT(), HttpStatus.OK);
			}



			@SuppressWarnings({ "unchecked", "rawtypes" })
			@RequestMapping(method = RequestMethod.POST)
			public ResponseEntity insert(@RequestBody String data,@RequestHeader(value = "Authorization") String headToken)
					throws JsonProcessingException, JSONException, ParseException {
				APIRequestDataLog apiRequest = checkToken("POST", "/Organization", data, null, headToken);
				if (apiRequest.getREQUEST_STATUS() != null) return new ResponseEntity(apiRequest.getREQUEST_OUTPUT(), HttpStatus.BAD_REQUEST);
				
				return insertupdateAll(null, new JSONObject(data), apiRequest);
			}

			@SuppressWarnings({ "unchecked", "rawtypes" })
			@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
			public ResponseEntity update(@PathVariable Long id, @RequestBody String data, @RequestHeader(value = "Authorization") String headToken)
					throws JsonProcessingException, JSONException, ParseException {
				APIRequestDataLog apiRequest = checkToken("PUT", "/Organization/"+id, data, null, headToken);
				if (apiRequest.getREQUEST_STATUS() != null) return new ResponseEntity(apiRequest.getREQUEST_OUTPUT(), HttpStatus.BAD_REQUEST);
				
				JSONObject jsonObj = new JSONObject(data);
				jsonObj.put("id", id);
				
				return insertupdateAll(null, jsonObj, apiRequest);
			}

			@SuppressWarnings({ "unchecked", "rawtypes" })
			@RequestMapping(method = RequestMethod.PUT)
			public ResponseEntity insertupdate(@RequestBody String data, @RequestHeader(value = "Authorization") String headToken)
					throws JsonProcessingException, JSONException, ParseException {
				APIRequestDataLog apiRequest = checkToken("PUT", "/Organization", data, null, headToken);
				if (apiRequest.getREQUEST_STATUS() != null) return new ResponseEntity(apiRequest.getREQUEST_OUTPUT(), HttpStatus.BAD_REQUEST);
				
				return insertupdateAll(new JSONArray(data), null, apiRequest);
			}

			@SuppressWarnings({ "unchecked", "rawtypes" })
			public ResponseEntity insertupdateAll(JSONArray jsonCompanys, JSONObject jsonCompany, APIRequestDataLog apiRequest) throws JsonProcessingException, JSONException, ParseException {
			    SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date date = new Date();

				List<Organization> organizations = new ArrayList<Organization>();
				if (jsonCompany != null) {
					jsonCompanys = new JSONArray();
					jsonCompanys.put(jsonCompany);
				}
				log.info(jsonCompanys.toString());
				
				for (int a=0; a<jsonCompanys.length(); a++) {
					JSONObject jsonObj = jsonCompanys.getJSONObject(a);
					Organization Organization = new Organization();
					long id = 0;

					if (jsonObj.has("id")) {
						id = jsonObj.getLong("id");
						if (id != 0) {
							Organization = organizationrepository.findOne(id);
							
							if (Organization == null)
								return new ResponseEntity(getAPIResponse(null, null, "Invalid Organization Data!", apiRequest, false).getREQUEST_OUTPUT(), HttpStatus.BAD_REQUEST);
						}
					}
					if (id == 0) {
						if (!jsonObj.has("ORGANIZATION_ID") || jsonObj.isNull("ORGANIZATION_ID"))
							return new ResponseEntity(getAPIResponse(null, null, "dept_ID is missing", apiRequest, false).getREQUEST_OUTPUT(), HttpStatus.BAD_REQUEST);
						
						
					}
					
					if (jsonObj.has("ORGANIZATION_ID") && !jsonObj.isNull("ORGANIZATION_ID"))
						Organization.setORGANIZATION_ID(jsonObj.getLong("ORGANIZATION_ID"));
					
					
					if (jsonObj.has("ORGANIZATION_NAME") && !jsonObj.isNull("ORGANIZATION_NAME"))
						Organization.setORGANIZATION_NAME(jsonObj.getString("ORGANIZATION_NAME"));
					
					if (jsonObj.has("ORGANIZATION_DESC") && !jsonObj.isNull("ORGANIZATION_DESC"))
						Organization.setORGANIZATION_DESC(jsonObj.getString("ORGANIZATION_DESC"));

					if (jsonObj.has("ORGANIZATIONTYPE_ID") && !jsonObj.isNull("ORGANIZATIONTYPE_ID")) 
						Organization.setORGANIZATIONTYPE_ID(jsonObj.getLong("ORGANIZATIONTYPE_ID"));
					
					
					
					
					if (jsonObj.has("isactive"))
					Organization.setISACTIVE(jsonObj.getString("isactive"));
				    else if (id == 0)
					Organization.setISACTIVE("Y");

					Organization.setMODIFIED_BY(apiRequest.getREQUEST_ID());
					Organization.setMODIFIED_WORKSTATION(apiRequest.getLOG_WORKSTATION());
					Organization.setMODIFIED_WHEN(dateFormat1.format(date));
					organizations.add(Organization);
				}
			
				
				for (int a=0; a<organizations.size(); a++) {
					Organization Organization = organizations.get(a);
					Organization = organizationrepository.saveAndFlush(Organization);
					organizations.get(a).setORGANIZATION_ID(Organization.getORGANIZATION_ID());
				}
				
				ResponseEntity responseentity;
				if (jsonCompany != null)
					responseentity = new ResponseEntity(getAPIResponse(null, organizations.get(0), null, apiRequest, true).getREQUEST_OUTPUT(), HttpStatus.OK);
				else
					responseentity = new ResponseEntity(getAPIResponse(organizations, null, null, apiRequest, true).getREQUEST_OUTPUT(), HttpStatus.OK);
				return responseentity;
			}
									
			@SuppressWarnings({ "unchecked", "rawtypes" })
			@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
			public ResponseEntity delete(@PathVariable Long id,@RequestHeader(value = "Authorization") String headToken) throws JsonProcessingException, JSONException, ParseException {
				APIRequestDataLog apiRequest = checkToken("GET", "/Organization/"+id, null, null, headToken);
				if (apiRequest.getREQUEST_STATUS() != null) return new ResponseEntity(apiRequest.getREQUEST_OUTPUT(), HttpStatus.BAD_REQUEST);

				Organization Organization = organizationrepository.findOne(id);
				organizationrepository.delete(Organization);
				
				return new ResponseEntity(getAPIResponse(null, Organization, null, apiRequest, true).getREQUEST_OUTPUT(), HttpStatus.OK);
			}

			@SuppressWarnings({ "unchecked", "rawtypes" })
			@RequestMapping(value = "/remove/{id}", method = RequestMethod.GET)
			public ResponseEntity remove(@PathVariable Long id,@RequestHeader(value = "Authorization") String headToken) throws JsonProcessingException, JSONException, ParseException {
				APIRequestDataLog apiRequest = checkToken("GET", "/Organization/"+id, null, null, headToken);
				if (apiRequest.getREQUEST_STATUS() != null) return new ResponseEntity(apiRequest.getREQUEST_OUTPUT(), HttpStatus.BAD_REQUEST);
				
				JSONObject Organization = new JSONObject();
				Organization.put("id", id);
				Organization.put("isactive", "N");
				
				return insertupdateAll(null, Organization, apiRequest);
			}

			@SuppressWarnings({ "rawtypes" })
			@RequestMapping(value = "/search", method = RequestMethod.POST)
			public ResponseEntity getBySearch(@RequestBody String data, @RequestHeader(value = "Authorization") String headToken) throws JsonProcessingException, JSONException, ParseException {
				return BySearch(data, true, headToken);
			}

			@SuppressWarnings({ "rawtypes" })
			@RequestMapping(value = "/search/all", method = RequestMethod.POST)
			public ResponseEntity getAllBySearch(@RequestBody String data, @RequestHeader(value = "Authorization") String headToken) throws JsonProcessingException, JSONException, ParseException {
				return BySearch(data, false, headToken);
			}

			@SuppressWarnings({ "unchecked", "rawtypes" })
			public ResponseEntity BySearch(String data, boolean active, String headToken) throws JsonProcessingException, JSONException, ParseException {
				APIRequestDataLog apiRequest = checkToken("POST", "/Organization/search" + ((active == true) ? "" : "/all"), data, null, headToken);
				if (apiRequest.getREQUEST_STATUS() != null) return new ResponseEntity(apiRequest.getREQUEST_OUTPUT(), HttpStatus.BAD_REQUEST);

				JSONObject jsonObj = new JSONObject(data);

				List<		Organization> organizations = ((active == true)
						? organizationrepository.findBySearch("%" + jsonObj.getString("search") + "%")
						: organizationrepository.findAllBySearch("%" + jsonObj.getString("search") + "%"));
				
				return new ResponseEntity(getAPIResponse(organizations, null, null, apiRequest, false).getREQUEST_OUTPUT(), HttpStatus.OK);
			}

			public APIRequestDataLog checkToken(String requestType, String requestURI, String requestBody, String workstation, String accessToken) throws JsonProcessingException {
				JSONObject checkTokenResponse = AccessToken.checkToken(accessToken);
				DatabaseTables databaseTableID = databasetablesrepository.findOne(Organization.getDatabaseTableID());
				APIRequestDataLog apiRequest;
				
				log.info(requestType + ": " + requestURI);
				if (requestBody != null)
					log.info("Input: " + requestBody);

				if (checkTokenResponse.has("error")) {
					apiRequest = tableDataLogs.apiRequestDataLog(requestType, databaseTableID, (long) 0, requestURI, requestBody, workstation);
					apiRequest = tableDataLogs.errorDataLog(apiRequest, "invalid_token", "Token was not recognised");
					apirequestdatalogRepository.saveAndFlush(apiRequest);
					return apiRequest;
				}
				
				Long requestUser = checkTokenResponse.getLong("ORGANIZATION_ID");
				apiRequest = tableDataLogs.apiRequestDataLog(requestType, databaseTableID, requestUser, requestURI, requestBody, workstation);

				return apiRequest;
			}
			
			APIRequestDataLog getAPIResponse(List<Organization> organizations, Organization Organization, String message, APIRequestDataLog apiRequest, boolean isTableLog) throws JSONException, JsonProcessingException, ParseException {
				ObjectMapper mapper = new ObjectMapper();
				long dept_Id = 0;
				
				if (message != null) {
					apiRequest = tableDataLogs.errorDataLog(apiRequest, "Organization", message);
					apirequestdatalogRepository.saveAndFlush(apiRequest);
				} else {
					if (Organization != null) {
						apiRequest.setREQUEST_OUTPUT(mapper.writeValueAsString(Organization));
						dept_Id = Organization.getORGANIZATION_ID();
					} else {
						apiRequest.setREQUEST_OUTPUT(mapper.writeValueAsString(Organization));
					}
					apiRequest.setREQUEST_STATUS("Success");
					apirequestdatalogRepository.saveAndFlush(apiRequest);
				}
				
				if (isTableLog)
					tbldatalogrepository.saveAndFlush(tableDataLogs.TableSaveDataLog( dept_Id , apiRequest.getDATABASETABLE_ID(), apiRequest.getREQUEST_ID(), apiRequest.getREQUEST_OUTPUT()));
				
				log.info("Output: " + apiRequest.getREQUEST_OUTPUT());
				log.info("--------------------------------------------------------");

				return apiRequest;
			}

}
