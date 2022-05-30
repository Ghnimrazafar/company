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
import com.cwiztech.model.CompanySubtype;
import com.cwiztech.repository.companySubtypeRepository;
import com.cwiztech.repository.companySubtypeRepository;
import com.cwiztech.token.AccessToken;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


@RestController
@CrossOrigin
@RequestMapping("/CompanySubtype")

public class companySubtypeController {

private static final Logger log = LoggerFactory.getLogger(companySubtypeController.class);
	
	@Autowired
	private companySubtypeRepository companysubtyperepository;
	
	@Autowired
	private apiRequestDataLogRepository apirequestdatalogRepository;
	
	@Autowired
	private tableDataLogRepository tbldatalogrepository;
	
	@Autowired
	private databaseTablesRepository databasetablesrepository;
		
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity get(@RequestHeader(value = "Authorization") String headToken) throws JsonProcessingException, JSONException, ParseException  {
		APIRequestDataLog apiRequest = checkToken("GET", "/company/all", null, null, headToken);
		if (apiRequest.getREQUEST_STATUS() != null) return new ResponseEntity(apiRequest.getREQUEST_OUTPUT(), HttpStatus.BAD_REQUEST);

		List<CompanySubtype> CompanySubtype = companysubtyperepository.findActive();		
		return new ResponseEntity(getAPIResponse(CompanySubtype, null, null, apiRequest, false).getREQUEST_OUTPUT(), HttpStatus.OK);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/all", method = RequestMethod.GET)
	public ResponseEntity getAll(@RequestHeader(value = "Authorization") String headToken) throws JsonProcessingException, JSONException, ParseException {
		APIRequestDataLog apiRequest = checkToken("GET", "/CompanySubtype/all", null, null, headToken);
		if (apiRequest.getREQUEST_STATUS() != null) return new ResponseEntity(apiRequest.getREQUEST_OUTPUT(), HttpStatus.BAD_REQUEST);

		List<CompanySubtype> CompanySubtype = companysubtyperepository.findAll();
		
		return new ResponseEntity(getAPIResponse(CompanySubtype, null, null, apiRequest, false).getREQUEST_OUTPUT(), HttpStatus.OK);
	}
	

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity getOne(@PathVariable Long id, @RequestHeader(value = "Authorization") String headToken) throws JsonProcessingException, JSONException, ParseException {
		APIRequestDataLog apiRequest = checkToken("GET", "/CompanySubtype/"+id, null, null, headToken);
		if (apiRequest.getREQUEST_STATUS() != null) return new ResponseEntity(apiRequest.getREQUEST_OUTPUT(), HttpStatus.BAD_REQUEST);

		CompanySubtype CompanySubtype = companysubtyperepository.findOne(id);
		
		return new ResponseEntity(getAPIResponse(null, CompanySubtype, null, apiRequest, false).getREQUEST_OUTPUT(), HttpStatus.OK);
	}


	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity insert(@RequestBody String data,@RequestHeader(value = "Authorization") String headToken)
			throws JsonProcessingException, JSONException, ParseException {
		APIRequestDataLog apiRequest = checkToken("POST", "/CompanySubtype", data, null, headToken);
		if (apiRequest.getREQUEST_STATUS() != null) return new ResponseEntity(apiRequest.getREQUEST_OUTPUT(), HttpStatus.BAD_REQUEST);
		
		return insertupdateAll(null, new JSONObject(data), apiRequest);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity update(@PathVariable Long id, @RequestBody String data, @RequestHeader(value = "Authorization") String headToken)
			throws JsonProcessingException, JSONException, ParseException {
		APIRequestDataLog apiRequest = checkToken("PUT", "/CompanySubtype/"+id, data, null, headToken);
		if (apiRequest.getREQUEST_STATUS() != null) return new ResponseEntity(apiRequest.getREQUEST_OUTPUT(), HttpStatus.BAD_REQUEST);
		
		JSONObject jsonObj = new JSONObject(data);
		jsonObj.put("id", id);
		
		return insertupdateAll(null, jsonObj, apiRequest);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(method = RequestMethod.PUT)
	public ResponseEntity insertupdate(@RequestBody String data, @RequestHeader(value = "Authorization") String headToken)
			throws JsonProcessingException, JSONException, ParseException {
		APIRequestDataLog apiRequest = checkToken("PUT", "/CompanySubtype", data, null, headToken);
		if (apiRequest.getREQUEST_STATUS() != null) return new ResponseEntity(apiRequest.getREQUEST_OUTPUT(), HttpStatus.BAD_REQUEST);
		
		return insertupdateAll(new JSONArray(data), null, apiRequest);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ResponseEntity insertupdateAll(JSONArray jsonCompanySubtypes, JSONObject jsonCompanySubtype, APIRequestDataLog apiRequest) throws JsonProcessingException, JSONException, ParseException {
	    SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();

		List<CompanySubtype> CompanySubtypes = new ArrayList<CompanySubtype>();
		if (jsonCompanySubtype != null) {
			jsonCompanySubtypes = new JSONArray();
			jsonCompanySubtypes.put(jsonCompanySubtype);
		}
		log.info(jsonCompanySubtypes.toString());
		
		for (int a=0; a<jsonCompanySubtypes.length(); a++) {
			JSONObject jsonObj = jsonCompanySubtypes.getJSONObject(a);
			CompanySubtype CompanySubtype = new CompanySubtype();
			long id = 0;

			if (jsonObj.has("id")) {
				id = jsonObj.getLong("id");
				if (id != 0) {
					CompanySubtype = companysubtyperepository.findOne(id);
					
					if (CompanySubtype == null)
						return new ResponseEntity(getAPIResponse(null, null, "Invalid CompanySubtype Data!", apiRequest, false).getREQUEST_OUTPUT(), HttpStatus.BAD_REQUEST);
				}
			}
			CompanySubtype companysubtype;
			if (id == 0) {
				if (!jsonObj.has("CompanySubtype_ID") || jsonObj.isNull("CompanySubtype_ID"))
					return new ResponseEntity(getAPIResponse(null, null, "CompanySubtype_ID is missing", apiRequest, false).getREQUEST_OUTPUT(), HttpStatus.BAD_REQUEST);
				
				
			}
			
			if (jsonObj.has("COMPANYSUBTYPE_ID") && !jsonObj.isNull("COMPANYSUBTYPE_ID"))
				CompanySubtype.setCOMPANYSUBTYPE_ID(jsonObj.getLong("COMPANYSUBTYPE_ID"));
			
			
			if (jsonObj.has("COMPANYTYPE_ID") && !jsonObj.isNull("COMPANYTYPE_ID"))
				CompanySubtype.setCOMPANYTYPE_ID(jsonObj.getLong("COMPANYTYPE_ID"));
			
			if (jsonObj.has("COMPANYSUBTYPE_NAME") && !jsonObj.isNull("COMPANYSUBTYPE_NAME"))
				CompanySubtype.setCOMPANYSUBTYPE_NAME(jsonObj.getString("COMPANYSUBTYPE_NAME"));

			if (jsonObj.has("COMPANYSUBTYPE_DESC") && !jsonObj.isNull("COMPANYSUBTYPE_DESC")) 
				CompanySubtype.setCOMPANYSUBTYPE_DESC(jsonObj.getString("COMPANYSUBTYPE_DESC"));
			
			
			
			if (jsonObj.has("isactive"))
			CompanySubtype.setISACTIVE(jsonObj.getString("isactive"));
		    else if (id == 0)
			CompanySubtype.setISACTIVE("Y");

			CompanySubtype.setMODIFIED_BY(apiRequest.getREQUEST_ID());
			CompanySubtype.setMODIFIED_WORKSTATION(apiRequest.getLOG_WORKSTATION());
			CompanySubtype.setMODIFIED_WHEN(dateFormat1.format(date));
			CompanySubtypes.add(CompanySubtype);
		}
	
		
		for (int a=0; a<CompanySubtypes.size(); a++) {
			CompanySubtype CompanySubtype = CompanySubtypes.get(a);
			CompanySubtype = companysubtyperepository.saveAndFlush(CompanySubtype);
			CompanySubtypes.get(a).setCOMPANYSUBTYPE_ID(CompanySubtype.getCOMPANYSUBTYPE_ID());
		}
		
		ResponseEntity responseentity;
		if (jsonCompanySubtype != null)
			responseentity = new ResponseEntity(getAPIResponse(null, CompanySubtypes.get(0), null, apiRequest, true).getREQUEST_OUTPUT(), HttpStatus.OK);
		else
			responseentity = new ResponseEntity(getAPIResponse(CompanySubtypes, null, null, apiRequest, true).getREQUEST_OUTPUT(), HttpStatus.OK);
		return responseentity;
	}
							
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity delete(@PathVariable Long id,@RequestHeader(value = "Authorization") String headToken) throws JsonProcessingException, JSONException, ParseException {
		APIRequestDataLog apiRequest = checkToken("GET", "/CompanySubtype/"+id, null, null, headToken);
		if (apiRequest.getREQUEST_STATUS() != null) return new ResponseEntity(apiRequest.getREQUEST_OUTPUT(), HttpStatus.BAD_REQUEST);

		CompanySubtype CompanySubtype = companysubtyperepository.findOne(id);
		companysubtyperepository.delete(CompanySubtype);
		
		return new ResponseEntity(getAPIResponse(null, CompanySubtype, null, apiRequest, true).getREQUEST_OUTPUT(), HttpStatus.OK);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/remove/{id}", method = RequestMethod.GET)
	public ResponseEntity remove(@PathVariable Long id,@RequestHeader(value = "Authorization") String headToken) throws JsonProcessingException, JSONException, ParseException {
		APIRequestDataLog apiRequest = checkToken("GET", "/CompanySubtype/"+id, null, null, headToken);
		if (apiRequest.getREQUEST_STATUS() != null) return new ResponseEntity(apiRequest.getREQUEST_OUTPUT(), HttpStatus.BAD_REQUEST);
		
		JSONObject CompanySubtype = new JSONObject();
		CompanySubtype.put("id", id);
		CompanySubtype.put("isactive", "N");
		
		return insertupdateAll(null, CompanySubtype, apiRequest);
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
		APIRequestDataLog apiRequest = checkToken("POST", "/CompanySubtype/search" + ((active == true) ? "" : "/all"), data, null, headToken);
		if (apiRequest.getREQUEST_STATUS() != null) return new ResponseEntity(apiRequest.getREQUEST_OUTPUT(), HttpStatus.BAD_REQUEST);

		JSONObject jsonObj = new JSONObject(data);

		List<CompanySubtype> CompanySubtypes = ((active == true)
				? companysubtyperepository.findBySearch("%" + jsonObj.getString("search") + "%")
				: companysubtyperepository.findAllBySearch("%" + jsonObj.getString("search") + "%"));
		
		return new ResponseEntity(getAPIResponse(CompanySubtypes, null, null, apiRequest, false).getREQUEST_OUTPUT(), HttpStatus.OK);
	}

	@SuppressWarnings({ "rawtypes" })
	@RequestMapping(value = "/advancedsearch", method = RequestMethod.POST)
	public ResponseEntity getByAdvancedSearch(@RequestBody String data, @RequestHeader(value = "Authorization") String headToken) throws JsonProcessingException, JSONException, ParseException {
		return ByAdvancedSearch(data, true, headToken);
	}

	@SuppressWarnings({ "rawtypes" })
	@RequestMapping(value = "/advancedsearch/all", method = RequestMethod.POST)
	public ResponseEntity getAllByAdvancedSearch(@RequestBody String data, @RequestHeader(value = "Authorization") String headToken) throws JsonProcessingException, JSONException, ParseException {
		return ByAdvancedSearch(data, false, headToken);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ResponseEntity ByAdvancedSearch(String data, boolean active, String headToken) throws JsonProcessingException, JSONException, ParseException {
		APIRequestDataLog apiRequest = checkToken("POST", "/CompanySubtype/advancedsearch" + ((active == true) ? "" : "/all"), data, null, headToken);
		if (apiRequest.getREQUEST_STATUS() != null) return new ResponseEntity(apiRequest.getREQUEST_OUTPUT(), HttpStatus.BAD_REQUEST);

		JSONObject jsonObj = new JSONObject(data);
		long CompanySubtype_ID = 0;

		if (jsonObj.has("CompanySubtype_ID"))
			CompanySubtype_ID = jsonObj.getLong("CompanySubtype_ID");

		List<CompanySubtype> CompanySubtype = ((active == true)
				? companysubtyperepository.findByAdvancedSearch(CompanySubtype_ID)
				: companysubtyperepository.findAllByAdvancedSearch(CompanySubtype_ID));

		return new ResponseEntity(getAPIResponse(CompanySubtype, null, null, apiRequest, false).getREQUEST_OUTPUT(), HttpStatus.OK);
	}



	public APIRequestDataLog checkToken(String requestType, String requestURI, String requestBody, String workstation, String accessToken) throws JsonProcessingException {
		JSONObject checkTokenResponse = AccessToken.checkToken(accessToken);
		DatabaseTables databaseTableID = databasetablesrepository.findOne(CompanySubtype.getDatabaseTableID());
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
		
		Long requestUser = checkTokenResponse.getLong("COMPANYSUBTYPE_ID");
		apiRequest = tableDataLogs.apiRequestDataLog(requestType, databaseTableID, requestUser, requestURI, requestBody, workstation);

		return apiRequest;
	}
	
	APIRequestDataLog getAPIResponse(List<CompanySubtype> CompanySubtypes, CompanySubtype CompanySubtype, String message, APIRequestDataLog apiRequest, boolean isTableLog) throws JSONException, JsonProcessingException, ParseException {
		ObjectMapper mapper = new ObjectMapper();
		long CompanySubtypeID = 0;
		
		if (message != null) {
			apiRequest = tableDataLogs.errorDataLog(apiRequest, "CompanySubtype", message);
			apirequestdatalogRepository.saveAndFlush(apiRequest);
		} else {
			if (CompanySubtype != null) {
				apiRequest.setREQUEST_OUTPUT(mapper.writeValueAsString(CompanySubtype));
				CompanySubtypeID = CompanySubtype.getCOMPANYSUBTYPE_ID();
			} else {
				apiRequest.setREQUEST_OUTPUT(mapper.writeValueAsString(CompanySubtype));
			}
			apiRequest.setREQUEST_STATUS("Success");
			apirequestdatalogRepository.saveAndFlush(apiRequest);
		}
		
		if (isTableLog)
			tbldatalogrepository.saveAndFlush(tableDataLogs.TableSaveDataLog(CompanySubtypeID, apiRequest.getDATABASETABLE_ID(), apiRequest.getREQUEST_ID(), apiRequest.getREQUEST_OUTPUT()));
		
		log.info("Output: " + apiRequest.getREQUEST_OUTPUT());
		log.info("--------------------------------------------------------");

		return apiRequest;
	}
}
