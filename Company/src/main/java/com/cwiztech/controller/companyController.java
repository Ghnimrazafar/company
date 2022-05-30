package com.cwiztech.controller;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import org.json.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cwiztech.model.Company;
import com.cwiztech.model.CompanySubtype;
import com.cwiztech.model.Company;
import com.cwiztech.repository.companyRepository;
import com.cwiztech.datalogs.model.APIRequestDataLog;
import com.cwiztech.datalogs.model.DatabaseTables;
import com.cwiztech.datalogs.model.tableDataLogs;
import com.cwiztech.datalogs.repository.apiRequestDataLogRepository;
import com.cwiztech.datalogs.repository.databaseTablesRepository;
import com.cwiztech.datalogs.repository.tableDataLogRepository;
import com.cwiztech.token.AccessToken;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@CrossOrigin
@RequestMapping("/Company")

public class companyController {
	private static final Logger log = LoggerFactory.getLogger(companyController.class);
	
	@Autowired
	private companyRepository companyrepository;
	
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

		List<Company> Company = companyrepository.findActive();		
		return new ResponseEntity(getAPIResponse(Company, null, null, apiRequest, false).getREQUEST_OUTPUT(), HttpStatus.OK);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/all", method = RequestMethod.GET)
	public ResponseEntity getAll(@RequestHeader(value = "Authorization") String headToken) throws JsonProcessingException, JSONException, ParseException {
		APIRequestDataLog apiRequest = checkToken("GET", "/Company/all", null, null, headToken);
		if (apiRequest.getREQUEST_STATUS() != null) return new ResponseEntity(apiRequest.getREQUEST_OUTPUT(), HttpStatus.BAD_REQUEST);

		List<Company> Company = companyrepository.findAll();
		
		return new ResponseEntity(getAPIResponse(Company, null, null, apiRequest, false).getREQUEST_OUTPUT(), HttpStatus.OK);
	}
	

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity getOne(@PathVariable Long id, @RequestHeader(value = "Authorization") String headToken) throws JsonProcessingException, JSONException, ParseException {
		APIRequestDataLog apiRequest = checkToken("GET", "/Company/"+id, null, null, headToken);
		if (apiRequest.getREQUEST_STATUS() != null) return new ResponseEntity(apiRequest.getREQUEST_OUTPUT(), HttpStatus.BAD_REQUEST);

		Company Company = companyrepository.findOne(id);
		
		return new ResponseEntity(getAPIResponse(null, Company, null, apiRequest, false).getREQUEST_OUTPUT(), HttpStatus.OK);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/ids", method = RequestMethod.POST)
	public ResponseEntity getByIDs(@RequestBody String data, @RequestHeader(value = "Authorization") String headToken)
			throws JsonProcessingException, JSONException, ParseException {
		APIRequestDataLog apiRequest = checkToken("POST", "/Company/ids", data, null, headToken);
		if (apiRequest.getREQUEST_STATUS() != null) return new ResponseEntity(apiRequest.getREQUEST_OUTPUT(), HttpStatus.BAD_REQUEST);

		List<Integer> ids = new ArrayList<Integer>(); 
		JSONObject jsonObj = new JSONObject(data);
		JSONArray jsonCompanys = jsonObj.getJSONArray("Companys");
		for (int i=0; i<jsonCompanys.length(); i++) {
			ids.add((Integer) jsonCompanys.get(i));
		}
		List<Company> Companys = new ArrayList<Company>();
		if (jsonCompanys.length()>0)
			Companys = companyrepository.findByIDs(ids);
		
		return new ResponseEntity(getAPIResponse(Companys, null, null, apiRequest, false).getREQUEST_OUTPUT(), HttpStatus.OK);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity insert(@RequestBody String data,@RequestHeader(value = "Authorization") String headToken)
			throws JsonProcessingException, JSONException, ParseException {
		APIRequestDataLog apiRequest = checkToken("POST", "/Company", data, null, headToken);
		if (apiRequest.getREQUEST_STATUS() != null) return new ResponseEntity(apiRequest.getREQUEST_OUTPUT(), HttpStatus.BAD_REQUEST);
		
		return insertupdateAll(null, new JSONObject(data), apiRequest);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity update(@PathVariable Long id, @RequestBody String data, @RequestHeader(value = "Authorization") String headToken)
			throws JsonProcessingException, JSONException, ParseException {
		APIRequestDataLog apiRequest = checkToken("PUT", "/Company/"+id, data, null, headToken);
		if (apiRequest.getREQUEST_STATUS() != null) return new ResponseEntity(apiRequest.getREQUEST_OUTPUT(), HttpStatus.BAD_REQUEST);
		
		JSONObject jsonObj = new JSONObject(data);
		jsonObj.put("id", id);
		
		return insertupdateAll(null, jsonObj, apiRequest);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(method = RequestMethod.PUT)
	public ResponseEntity insertupdate(@RequestBody String data, @RequestHeader(value = "Authorization") String headToken)
			throws JsonProcessingException, JSONException, ParseException {
		APIRequestDataLog apiRequest = checkToken("PUT", "/Company", data, null, headToken);
		if (apiRequest.getREQUEST_STATUS() != null) return new ResponseEntity(apiRequest.getREQUEST_OUTPUT(), HttpStatus.BAD_REQUEST);
		
		return insertupdateAll(new JSONArray(data), null, apiRequest);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ResponseEntity insertupdateAll(JSONArray jsonCompanys, JSONObject jsonCompany, APIRequestDataLog apiRequest) throws JsonProcessingException, JSONException, ParseException {
	    SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();

		List<Company> Companys = new ArrayList<Company>();
		if (jsonCompany != null) {
			jsonCompanys = new JSONArray();
			jsonCompanys.put(jsonCompany);
		}
		log.info(jsonCompanys.toString());
		
		for (int a=0; a<jsonCompanys.length(); a++) {
			JSONObject jsonObj = jsonCompanys.getJSONObject(a);
			Company Company = new Company();
			long id = 0;

			if (jsonObj.has("id")) {
				id = jsonObj.getLong("id");
				if (id != 0) {
					Company = companyrepository.findOne(id);
					
					if (Company == null)
						return new ResponseEntity(getAPIResponse(null, null, "Invalid Company Data!", apiRequest, false).getREQUEST_OUTPUT(), HttpStatus.BAD_REQUEST);
				}
			}
			CompanySubtype companysubtype;
			if (id == 0) {
				if (!jsonObj.has("Company_ID") || jsonObj.isNull("Company_ID"))
					return new ResponseEntity(getAPIResponse(null, null, "Company_ID is missing", apiRequest, false).getREQUEST_OUTPUT(), HttpStatus.BAD_REQUEST);
				
				
				if (!jsonObj.has("RESOURCETYPE_ID") || jsonObj.isNull("RESOURCETYPE_ID"))
					return new ResponseEntity(getAPIResponse(null, null, "RESOURCETYPE_ID is missing", apiRequest, false).getREQUEST_OUTPUT(), HttpStatus.BAD_REQUEST);
				
				if (!jsonObj.has("Company_DESCRIPTION") || jsonObj.isNull("Company_DESCRIPTION"))
					return new ResponseEntity(getAPIResponse(null, null, "Company_DESCRIPTION is missing", apiRequest, false).getREQUEST_OUTPUT(), HttpStatus.BAD_REQUEST);
			}
			
			if (jsonObj.has("Company_ID") && !jsonObj.isNull("Company_ID"))
				Company.setCOMPANY_ID(jsonObj.getLong("Company_ID"));
			
			
			if (jsonObj.has("NETSUITE_ID") && !jsonObj.isNull("NETSUITE_ID"))
				Company.setNETSUITE_ID(jsonObj.getLong("NETSUITE_ID"));
			
			if (jsonObj.has("COMPANY_NUMBER") && !jsonObj.isNull("COMPANY_NUMBER"))
				Company.setCOMPANY_NUMBER(jsonObj.getString("COMPANY_NUMBER"));

			if (jsonObj.has("COMPANY_NAME") && !jsonObj.isNull("COMPANY_NAME")) 
				Company.setCOMPANY_NAME(jsonObj.getString("COMPANY_NAME"));
			
			if (jsonObj.has("COMPANY_DESC") && !jsonObj.isNull("COMPANY_DESC")) 
				Company.setCOMPANY_DESC(jsonObj.getString("COMPANY_DESC"));
			
			if (jsonObj.has("COMPANYSUBTYPE_ID") && !jsonObj.isNull("COMPANYSUBTYPE_ID")) 
				Company.setCOMPANYSUBTYPE_ID(jsonObj.getLong("COMPANYSUBTYPE_ID"));
			
			if (jsonObj.has("START_DATE") && !jsonObj.isNull("START_DATE")) 
				Company.setSTART_DATE(jsonObj.getString("START_DATE"));
			
			if (jsonObj.has("END_DATE") && !jsonObj.isNull("END_DATE")) 
				Company.setEND_DATE(jsonObj.getString("END_DATE"));
			
			if (jsonObj.has("COMPANYPARENT_ID") && !jsonObj.isNull("COMPANYPARENT_ID")) 
				Company.setCOMPANYPARENT_ID(jsonObj.getLong("COMPANYPARENT_ID"));
			
			if (jsonObj.has("ADDRESS_LINE1") && !jsonObj.isNull("ADDRESS_LINE1")) 
				Company.setADDRESS_LINE1(jsonObj.getString("ADDRESS_LINE1"));
			
			if (jsonObj.has("ADDRESS_LINE2") && !jsonObj.isNull("ADDRESS_LINE2")) 
				Company.setADDRESS_LINE2(jsonObj.getString("ADDRESS_LINE2"));
			
			
			if (jsonObj.has("ADDRESS_LINE3") && !jsonObj.isNull("ADDRESS_LINE3")) 
				Company.setADDRESS_LINE3(jsonObj.getString("ADDRESS_LINE3"));
			
			if (jsonObj.has("ADDRESS_LINE4") && !jsonObj.isNull("ADDRESS_LINE4")) 
				Company.setADDRESS_LINE4(jsonObj.getString("ADDRESS_LINE4"));
			
			
			if (jsonObj.has("ADDRESS_LINE5") && !jsonObj.isNull("ADDRESS_LINE5")) 
				Company.setADDRESS_LINE5(jsonObj.getString("ADDRESS_LINE5"));
			
			
			if (jsonObj.has("ADDRESS_POSTCODE") && !jsonObj.isNull("ADDRESS_POSTCODE")) 
				Company.setADDRESS_POSTCODE(jsonObj.getString("ADDRESS_POSTCODE"));
			
			if (jsonObj.has("ADDRESS_POSTCODE") && !jsonObj.isNull("ADDRESS_POSTCODE")) 
				Company.setADDRESS_POSTCODE(jsonObj.getString("ADDRESS_POSTCODE"));
			
			if (jsonObj.has("COMPANYLOGO_PATH") && !jsonObj.isNull("COMPANYLOGO_PATH")) 
				Company.setCOMPANYLOGO_PATH(jsonObj.getString("COMPANYLOGO_PATH"));
			
			if (jsonObj.has("COMPANYLOGO_PATH") && !jsonObj.isNull("COMPANYLOGO_PATH")) 
				Company.setCOMPANYLOGO_PATH(jsonObj.getString("COMPANYLOGO_PATH"));
			
			
			
			if (jsonObj.has("isactive"))
			Company.setISACTIVE(jsonObj.getString("isactive"));
		    else if (id == 0)
			Company.setISACTIVE("Y");

			Company.setMODIFIED_BY(apiRequest.getREQUEST_ID());
			Company.setMODIFIED_WORKSTATION(apiRequest.getLOG_WORKSTATION());
			Company.setMODIFIED_WHEN(dateFormat1.format(date));
			Companys.add(Company);
		}
	
		
		for (int a=0; a<Companys.size(); a++) {
			Company Company = Companys.get(a);
			Company = companyrepository.saveAndFlush(Company);
			Companys.get(a).setCOMPANY_ID(Company.getCOMPANY_ID());
		}
		
		ResponseEntity responseentity;
		if (jsonCompany != null)
			responseentity = new ResponseEntity(getAPIResponse(null, Companys.get(0), null, apiRequest, true).getREQUEST_OUTPUT(), HttpStatus.OK);
		else
			responseentity = new ResponseEntity(getAPIResponse(Companys, null, null, apiRequest, true).getREQUEST_OUTPUT(), HttpStatus.OK);
		return responseentity;
	}
							
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity delete(@PathVariable Long id,@RequestHeader(value = "Authorization") String headToken) throws JsonProcessingException, JSONException, ParseException {
		APIRequestDataLog apiRequest = checkToken("GET", "/Company/"+id, null, null, headToken);
		if (apiRequest.getREQUEST_STATUS() != null) return new ResponseEntity(apiRequest.getREQUEST_OUTPUT(), HttpStatus.BAD_REQUEST);

		Company Company = companyrepository.findOne(id);
		companyrepository.delete(Company);
		
		return new ResponseEntity(getAPIResponse(null, Company, null, apiRequest, true).getREQUEST_OUTPUT(), HttpStatus.OK);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/remove/{id}", method = RequestMethod.GET)
	public ResponseEntity remove(@PathVariable Long id,@RequestHeader(value = "Authorization") String headToken) throws JsonProcessingException, JSONException, ParseException {
		APIRequestDataLog apiRequest = checkToken("GET", "/Company/"+id, null, null, headToken);
		if (apiRequest.getREQUEST_STATUS() != null) return new ResponseEntity(apiRequest.getREQUEST_OUTPUT(), HttpStatus.BAD_REQUEST);
		
		JSONObject Company = new JSONObject();
		Company.put("id", id);
		Company.put("isactive", "N");
		
		return insertupdateAll(null, Company, apiRequest);
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
		APIRequestDataLog apiRequest = checkToken("POST", "/Company/search" + ((active == true) ? "" : "/all"), data, null, headToken);
		if (apiRequest.getREQUEST_STATUS() != null) return new ResponseEntity(apiRequest.getREQUEST_OUTPUT(), HttpStatus.BAD_REQUEST);

		JSONObject jsonObj = new JSONObject(data);

		List<		Company> Companys = ((active == true)
				? companyrepository.findBySearch("%" + jsonObj.getString("search") + "%")
				: companyrepository.findAllBySearch("%" + jsonObj.getString("search") + "%"));
		
		return new ResponseEntity(getAPIResponse(Companys, null, null, apiRequest, false).getREQUEST_OUTPUT(), HttpStatus.OK);
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
		APIRequestDataLog apiRequest = checkToken("POST", "/Company/advancedsearch" + ((active == true) ? "" : "/all"), data, null, headToken);
		if (apiRequest.getREQUEST_STATUS() != null) return new ResponseEntity(apiRequest.getREQUEST_OUTPUT(), HttpStatus.BAD_REQUEST);

		JSONObject jsonObj = new JSONObject(data);
		long Company_ID = 0;

		if (jsonObj.has("Company_ID"))
			Company_ID = jsonObj.getLong("Company_ID");

		List<Company> Company = ((active == true)
				? companyrepository.findByAdvancedSearch(Company_ID)
				: companyrepository.findAllByAdvancedSearch(Company_ID));

		return new ResponseEntity(getAPIResponse(Company, null, null, apiRequest, false).getREQUEST_OUTPUT(), HttpStatus.OK);
	}



	public APIRequestDataLog checkToken(String requestType, String requestURI, String requestBody, String workstation, String accessToken) throws JsonProcessingException {
		JSONObject checkTokenResponse = AccessToken.checkToken(accessToken);
		DatabaseTables databaseTableID = databasetablesrepository.findOne(Company.getDatabaseTableID());
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
		
		Long requestUser = checkTokenResponse.getLong("COMPANY_ID");
		apiRequest = tableDataLogs.apiRequestDataLog(requestType, databaseTableID, requestUser, requestURI, requestBody, workstation);

		return apiRequest;
	}
	
	APIRequestDataLog getAPIResponse(List<Company> Companys, Company Company, String message, APIRequestDataLog apiRequest, boolean isTableLog) throws JSONException, JsonProcessingException, ParseException {
		ObjectMapper mapper = new ObjectMapper();
		long CompanyID = 0;
		
		if (message != null) {
			apiRequest = tableDataLogs.errorDataLog(apiRequest, "Company", message);
			apirequestdatalogRepository.saveAndFlush(apiRequest);
		} else {
			if (Company != null) {
				apiRequest.setREQUEST_OUTPUT(mapper.writeValueAsString(Company));
				CompanyID = Company.getCOMPANY_ID();
			} else {
				apiRequest.setREQUEST_OUTPUT(mapper.writeValueAsString(Company));
			}
			apiRequest.setREQUEST_STATUS("Success");
			apirequestdatalogRepository.saveAndFlush(apiRequest);
		}
		
		if (isTableLog)
			tbldatalogrepository.saveAndFlush(tableDataLogs.TableSaveDataLog(CompanyID, apiRequest.getDATABASETABLE_ID(), apiRequest.getREQUEST_ID(), apiRequest.getREQUEST_OUTPUT()));
		
		log.info("Output: " + apiRequest.getREQUEST_OUTPUT());
		log.info("--------------------------------------------------------");

		return apiRequest;
	}
}