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
import com.cwiztech.model.CompanyAccount;
import com.cwiztech.model.CompanyAccount;

import com.cwiztech.repository.companyAccountRepository;
import com.cwiztech.repository.companyRepository;
import com.cwiztech.token.AccessToken;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


@RestController
@CrossOrigin
@RequestMapping("/CompanyAccount")

public class companyAccountController {

	
private static final Logger log = LoggerFactory.getLogger(companyAccountController.class);
	
	@Autowired
	private companyAccountRepository companyaccountrepository;
	
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

		List<CompanyAccount> CompanyAccount = companyaccountrepository.findActive();		
		return new ResponseEntity(getAPIResponse(CompanyAccount, null, null, apiRequest, false).getREQUEST_OUTPUT(), HttpStatus.OK);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/all", method = RequestMethod.GET)
	public ResponseEntity getAll(@RequestHeader(value = "Authorization") String headToken) throws JsonProcessingException, JSONException, ParseException {
		APIRequestDataLog apiRequest = checkToken("GET", "/CompanyAccount/all", null, null, headToken);
		if (apiRequest.getREQUEST_STATUS() != null) return new ResponseEntity(apiRequest.getREQUEST_OUTPUT(), HttpStatus.BAD_REQUEST);

		List<CompanyAccount> CompanyAccountaccount = companyaccountrepository.findAll();
		
		return new ResponseEntity(getAPIResponse(CompanyAccountaccount, null, null, apiRequest, false).getREQUEST_OUTPUT(), HttpStatus.OK);
	}
	

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity getOne(@PathVariable Long id, @RequestHeader(value = "Authorization") String headToken) throws JsonProcessingException, JSONException, ParseException {
		APIRequestDataLog apiRequest = checkToken("GET", "/CompanyAccount/"+id, null, null, headToken);
		if (apiRequest.getREQUEST_STATUS() != null) return new ResponseEntity(apiRequest.getREQUEST_OUTPUT(), HttpStatus.BAD_REQUEST);

		CompanyAccount CompanyAccount =companyaccountrepository.findOne(id);
		
		return new ResponseEntity(getAPIResponse(null, CompanyAccount, null, apiRequest, false).getREQUEST_OUTPUT(), HttpStatus.OK);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/ids", method = RequestMethod.POST)
	public ResponseEntity getByIDs(@RequestBody String data, @RequestHeader(value = "Authorization") String headToken)
			throws JsonProcessingException, JSONException, ParseException {
		APIRequestDataLog apiRequest = checkToken("POST", "/CompanyAccount/ids", data, null, headToken);
		if (apiRequest.getREQUEST_STATUS() != null) return new ResponseEntity(apiRequest.getREQUEST_OUTPUT(), HttpStatus.BAD_REQUEST);

		List<Integer> ids = new ArrayList<Integer>(); 
		JSONObject jsonObj = new JSONObject(data);
		JSONArray jsonCompanyAccounts = jsonObj.getJSONArray("CompanyAccounts");
		for (int i=0; i<jsonCompanyAccounts.length(); i++) {
			ids.add((Integer) jsonCompanyAccounts.get(i));
		}
		List<CompanyAccount> CompanyAccounts = new ArrayList<CompanyAccount>();
		if (jsonCompanyAccounts.length()>0)
			CompanyAccounts = companyaccountrepository.findByIDs(ids);
		
		return new ResponseEntity(getAPIResponse(CompanyAccounts, null, null, apiRequest, false).getREQUEST_OUTPUT(), HttpStatus.OK);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity insert(@RequestBody String data,@RequestHeader(value = "Authorization") String headToken)
			throws JsonProcessingException, JSONException, ParseException {
		APIRequestDataLog apiRequest = checkToken("POST", "/CompanyAccount", data, null, headToken);
		if (apiRequest.getREQUEST_STATUS() != null) return new ResponseEntity(apiRequest.getREQUEST_OUTPUT(), HttpStatus.BAD_REQUEST);
		
		return insertupdateAll(null, new JSONObject(data), apiRequest);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity update(@PathVariable Long id, @RequestBody String data, @RequestHeader(value = "Authorization") String headToken)
			throws JsonProcessingException, JSONException, ParseException {
		APIRequestDataLog apiRequest = checkToken("PUT", "/CompanyAccount/"+id, data, null, headToken);
		if (apiRequest.getREQUEST_STATUS() != null) return new ResponseEntity(apiRequest.getREQUEST_OUTPUT(), HttpStatus.BAD_REQUEST);
		
		JSONObject jsonObj = new JSONObject(data);
		jsonObj.put("id", id);
		
		return insertupdateAll(null, jsonObj, apiRequest);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(method = RequestMethod.PUT)
	public ResponseEntity insertupdate(@RequestBody String data, @RequestHeader(value = "Authorization") String headToken)
			throws JsonProcessingException, JSONException, ParseException {
		APIRequestDataLog apiRequest = checkToken("PUT", "/CompanyAccount", data, null, headToken);
		if (apiRequest.getREQUEST_STATUS() != null) return new ResponseEntity(apiRequest.getREQUEST_OUTPUT(), HttpStatus.BAD_REQUEST);
		
		return insertupdateAll(new JSONArray(data), null, apiRequest);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ResponseEntity insertupdateAll(JSONArray jsonCompanyAccounts, JSONObject jsonCompanyAccount, APIRequestDataLog apiRequest) throws JsonProcessingException, JSONException, ParseException {
	    SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();

		List<CompanyAccount> CompanyAccounts = new ArrayList<CompanyAccount>();
		if (jsonCompanyAccount != null) {
			jsonCompanyAccounts = new JSONArray();
			jsonCompanyAccounts.put(jsonCompanyAccount);
		}
		log.info(jsonCompanyAccounts.toString());
		
		for (int a=0; a<jsonCompanyAccounts.length(); a++) {
			JSONObject jsonObj = jsonCompanyAccounts.getJSONObject(a);
			CompanyAccount CompanyAccount = new CompanyAccount();
			long id = 0;

			if (jsonObj.has("id")) {
				id = jsonObj.getLong("id");
				if (id != 0) {
					CompanyAccount = companyaccountrepository.findOne(id);
					
					if (CompanyAccount == null)
						return new ResponseEntity(getAPIResponse(null, null, "Invalid CompanyAccount Data!", apiRequest, false).getREQUEST_OUTPUT(), HttpStatus.BAD_REQUEST);
				}
			}
			
			if (id == 0) {
				if (!jsonObj.has("CompanyAccount_ID") || jsonObj.isNull("CompanyAccount_ID"))
					return new ResponseEntity(getAPIResponse(null, null, "CompanyAccount_ID is missing", apiRequest, false).getREQUEST_OUTPUT(), HttpStatus.BAD_REQUEST);
				
				
			}
	
			
			
			if (jsonObj.has("COMPANYACCOUNT_ID") && !jsonObj.isNull("COMPANYACCOUNT_ID"))
				CompanyAccount.setCOMPANYACCOUNT_ID(jsonObj.getLong("COMPANYACCOUNT_ID"));
			
			if (jsonObj.has("COMPANYACCOUNT_TITLE") && !jsonObj.isNull("COMPANYACCOUNT_TITLE"))
				CompanyAccount.setCOMPANYACCOUNT_TITLE(jsonObj.getString("COMPANYACCOUNT_TITLE"));
			if (jsonObj.has("isactive"))
			CompanyAccount.setISACTIVE(jsonObj.getString("isactive"));
		    else if (id == 0)
			CompanyAccount.setISACTIVE("Y");

			CompanyAccount.setMODIFIED_BY(apiRequest.getREQUEST_ID());
			CompanyAccount.setMODIFIED_WORKSTATION(apiRequest.getLOG_WORKSTATION());
			CompanyAccount.setMODIFIED_WHEN(dateFormat1.format(date));
			CompanyAccounts.add(CompanyAccount);
		}
	
		
		for (int a=0; a<CompanyAccounts.size(); a++) {
			CompanyAccount CompanyAccount = CompanyAccounts.get(a);
			CompanyAccount = companyaccountrepository.saveAndFlush(CompanyAccount);
			CompanyAccounts.get(a).setCOMPANY_ID(CompanyAccount.getCOMPANY_ID());
		}
		
		ResponseEntity responseentity;
		if (jsonCompanyAccount != null)
			responseentity = new ResponseEntity(getAPIResponse(null, CompanyAccounts.get(0), null, apiRequest, true).getREQUEST_OUTPUT(), HttpStatus.OK);
		else
			responseentity = new ResponseEntity(getAPIResponse(CompanyAccounts, null, null, apiRequest, true).getREQUEST_OUTPUT(), HttpStatus.OK);
		return responseentity;
	}
							
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity delete(@PathVariable Long id,@RequestHeader(value = "Authorization") String headToken) throws JsonProcessingException, JSONException, ParseException {
		APIRequestDataLog apiRequest = checkToken("GET", "/CompanyAccount/"+id, null, null, headToken);
		if (apiRequest.getREQUEST_STATUS() != null) return new ResponseEntity(apiRequest.getREQUEST_OUTPUT(), HttpStatus.BAD_REQUEST);

		CompanyAccount CompanyAccount = companyaccountrepository.findOne(id);
		companyaccountrepository.delete(CompanyAccount);
		
		return new ResponseEntity(getAPIResponse(null, CompanyAccount, null, apiRequest, true).getREQUEST_OUTPUT(), HttpStatus.OK);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/remove/{id}", method = RequestMethod.GET)
	public ResponseEntity remove(@PathVariable Long id,@RequestHeader(value = "Authorization") String headToken) throws JsonProcessingException, JSONException, ParseException {
		APIRequestDataLog apiRequest = checkToken("GET", "/CompanyAccount/"+id, null, null, headToken);
		if (apiRequest.getREQUEST_STATUS() != null) return new ResponseEntity(apiRequest.getREQUEST_OUTPUT(), HttpStatus.BAD_REQUEST);
		
		JSONObject CompanyAccount = new JSONObject();
		CompanyAccount.put("id", id);
		CompanyAccount.put("isactive", "N");
		
		return insertupdateAll(null, CompanyAccount, apiRequest);
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
		APIRequestDataLog apiRequest = checkToken("POST", "/CompanyAccount/search" + ((active == true) ? "" : "/all"), data, null, headToken);
		if (apiRequest.getREQUEST_STATUS() != null) return new ResponseEntity(apiRequest.getREQUEST_OUTPUT(), HttpStatus.BAD_REQUEST);

		JSONObject jsonObj = new JSONObject(data);

		List<CompanyAccount> CompanyAccounts = ((active == true)
				?companyaccountrepository.findBySearch("%" + jsonObj.getString("search") + "%")
				: companyaccountrepository.findAllBySearch("%" + jsonObj.getString("search") + "%"));
		
		return new ResponseEntity(getAPIResponse(CompanyAccounts, null, null, apiRequest, false).getREQUEST_OUTPUT(), HttpStatus.OK);
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
		APIRequestDataLog apiRequest = checkToken("POST", "/CompanyAccount/advancedsearch" + ((active == true) ? "" : "/all"), data, null, headToken);
		if (apiRequest.getREQUEST_STATUS() != null) return new ResponseEntity(apiRequest.getREQUEST_OUTPUT(), HttpStatus.BAD_REQUEST);

		JSONObject jsonObj = new JSONObject(data);
		long CompanyAccount_ID = 0;

		if (jsonObj.has("CompanyAccount_ID"))
			CompanyAccount_ID = jsonObj.getLong("CompanyAccount_ID");

		List<CompanyAccount> companyaccount = ((active == true)
				? companyaccountrepository.findByAdvancedSearch(CompanyAccount_ID)
				: companyaccountrepository.findAllByAdvancedSearch(CompanyAccount_ID));

		return new ResponseEntity(getAPIResponse(companyaccount, null, null, apiRequest, false).getREQUEST_OUTPUT(), HttpStatus.OK);
	}



	public APIRequestDataLog checkToken(String requestType, String requestURI, String requestBody, String workstation, String accessToken) throws JsonProcessingException {
		JSONObject checkTokenResponse = AccessToken.checkToken(accessToken);
		DatabaseTables databaseTableID = databasetablesrepository.findOne(CompanyAccount.getDatabaseTableID());
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
		
		Long requestUser = checkTokenResponse.getLong("CompanyAccount_ID");
		apiRequest = tableDataLogs.apiRequestDataLog(requestType, databaseTableID, requestUser, requestURI, requestBody, workstation);

		return apiRequest;
	}
	
	APIRequestDataLog getAPIResponse(List<CompanyAccount> CompanyAccounts, CompanyAccount CompanyAccount, String message, APIRequestDataLog apiRequest, boolean isTableLog) throws JSONException, JsonProcessingException, ParseException {
		ObjectMapper mapper = new ObjectMapper();
		long CompanyAccountID = 0;
		
		if (message != null) {
			apiRequest = tableDataLogs.errorDataLog(apiRequest, "CompanyAccount", message);
			apirequestdatalogRepository.saveAndFlush(apiRequest);
		} else {
			if (CompanyAccount != null) {
				apiRequest.setREQUEST_OUTPUT(mapper.writeValueAsString(CompanyAccount));
				CompanyAccountID = CompanyAccount.getCOMPANY_ID();
			} else {
				apiRequest.setREQUEST_OUTPUT(mapper.writeValueAsString(CompanyAccount));
			}
			apiRequest.setREQUEST_STATUS("Success");
			apirequestdatalogRepository.saveAndFlush(apiRequest);
		}
		
		if (isTableLog)
			tbldatalogrepository.saveAndFlush(tableDataLogs.TableSaveDataLog(CompanyAccountID, apiRequest.getDATABASETABLE_ID(), apiRequest.getREQUEST_ID(), apiRequest.getREQUEST_OUTPUT()));
		
		log.info("Output: " + apiRequest.getREQUEST_OUTPUT());
		log.info("--------------------------------------------------------");

		return apiRequest;
	}
	
	
	
}
