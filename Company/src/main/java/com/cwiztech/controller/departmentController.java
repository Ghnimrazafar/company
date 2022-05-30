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
import com.cwiztech.model.Department;
import com.cwiztech.model.CompanySubtype;
import com.cwiztech.repository.departmentRepository;
import com.cwiztech.token.AccessToken;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
@RestController
@CrossOrigin
@RequestMapping("/department")
public class departmentController {
	
		private static final Logger log = LoggerFactory.getLogger(departmentController.class);
		
		@Autowired
		private departmentRepository departmentrepository;
		
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

			List<Department> Department = departmentrepository.findActive();		
			return new ResponseEntity(getAPIResponse(Department, null, null, apiRequest, false).getREQUEST_OUTPUT(), HttpStatus.OK);
		}

		@SuppressWarnings({ "unchecked", "rawtypes" })
		@RequestMapping(value = "/all", method = RequestMethod.GET)
		public ResponseEntity getAll(@RequestHeader(value = "Authorization") String headToken) throws JsonProcessingException, JSONException, ParseException {
			APIRequestDataLog apiRequest = checkToken("GET", "/Department/all", null, null, headToken);
			if (apiRequest.getREQUEST_STATUS() != null) return new ResponseEntity(apiRequest.getREQUEST_OUTPUT(), HttpStatus.BAD_REQUEST);

			List<Department> Department = departmentrepository.findAll();
			
			return new ResponseEntity(getAPIResponse(Department, null, null, apiRequest, false).getREQUEST_OUTPUT(), HttpStatus.OK);
		}
		

		@SuppressWarnings({ "unchecked", "rawtypes" })
		@RequestMapping(value = "/{id}", method = RequestMethod.GET)
		public ResponseEntity getOne(@PathVariable Long id, @RequestHeader(value = "Authorization") String headToken) throws JsonProcessingException, JSONException, ParseException {
			APIRequestDataLog apiRequest = checkToken("GET", "/Department/"+id, null, null, headToken);
			if (apiRequest.getREQUEST_STATUS() != null) return new ResponseEntity(apiRequest.getREQUEST_OUTPUT(), HttpStatus.BAD_REQUEST);

			Department Department = departmentrepository.findOne(id);
			
			return new ResponseEntity(getAPIResponse(null, Department, null, apiRequest, false).getREQUEST_OUTPUT(), HttpStatus.OK);
		}



		@SuppressWarnings({ "unchecked", "rawtypes" })
		@RequestMapping(method = RequestMethod.POST)
		public ResponseEntity insert(@RequestBody String data,@RequestHeader(value = "Authorization") String headToken)
				throws JsonProcessingException, JSONException, ParseException {
			APIRequestDataLog apiRequest = checkToken("POST", "/Department", data, null, headToken);
			if (apiRequest.getREQUEST_STATUS() != null) return new ResponseEntity(apiRequest.getREQUEST_OUTPUT(), HttpStatus.BAD_REQUEST);
			
			return insertupdateAll(null, new JSONObject(data), apiRequest);
		}

		@SuppressWarnings({ "unchecked", "rawtypes" })
		@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
		public ResponseEntity update(@PathVariable Long id, @RequestBody String data, @RequestHeader(value = "Authorization") String headToken)
				throws JsonProcessingException, JSONException, ParseException {
			APIRequestDataLog apiRequest = checkToken("PUT", "/Department/"+id, data, null, headToken);
			if (apiRequest.getREQUEST_STATUS() != null) return new ResponseEntity(apiRequest.getREQUEST_OUTPUT(), HttpStatus.BAD_REQUEST);
			
			JSONObject jsonObj = new JSONObject(data);
			jsonObj.put("id", id);
			
			return insertupdateAll(null, jsonObj, apiRequest);
		}

		@SuppressWarnings({ "unchecked", "rawtypes" })
		@RequestMapping(method = RequestMethod.PUT)
		public ResponseEntity insertupdate(@RequestBody String data, @RequestHeader(value = "Authorization") String headToken)
				throws JsonProcessingException, JSONException, ParseException {
			APIRequestDataLog apiRequest = checkToken("PUT", "/Department", data, null, headToken);
			if (apiRequest.getREQUEST_STATUS() != null) return new ResponseEntity(apiRequest.getREQUEST_OUTPUT(), HttpStatus.BAD_REQUEST);
			
			return insertupdateAll(new JSONArray(data), null, apiRequest);
		}

		@SuppressWarnings({ "unchecked", "rawtypes" })
		public ResponseEntity insertupdateAll(JSONArray jsonCompanys, JSONObject jsonCompany, APIRequestDataLog apiRequest) throws JsonProcessingException, JSONException, ParseException {
		    SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = new Date();

			List<Department> Companys = new ArrayList<Department>();
			if (jsonCompany != null) {
				jsonCompanys = new JSONArray();
				jsonCompanys.put(jsonCompany);
			}
			log.info(jsonCompanys.toString());
			
			for (int a=0; a<jsonCompanys.length(); a++) {
				JSONObject jsonObj = jsonCompanys.getJSONObject(a);
				Department Department = new Department();
				long id = 0;

				if (jsonObj.has("id")) {
					id = jsonObj.getLong("id");
					if (id != 0) {
						Department = departmentrepository.findOne(id);
						
						if (Department == null)
							return new ResponseEntity(getAPIResponse(null, null, "Invalid Department Data!", apiRequest, false).getREQUEST_OUTPUT(), HttpStatus.BAD_REQUEST);
					}
				}
				CompanySubtype companysubtype;
				if (id == 0) {
					if (!jsonObj.has("DEPARTMENT_ID") || jsonObj.isNull("DEPARTMENT_ID"))
						return new ResponseEntity(getAPIResponse(null, null, "dept_ID is missing", apiRequest, false).getREQUEST_OUTPUT(), HttpStatus.BAD_REQUEST);
					
					
				}
				
				if (jsonObj.has("DEPARTMENT_ID") && !jsonObj.isNull("DEPARTMENT_ID"))
					Department.setDEPARTMENT_ID(jsonObj.getLong("DEPARTMENT_ID"));
				
				
				if (jsonObj.has("NETSUITE_ID") && !jsonObj.isNull("NETSUITE_ID"))
					Department.setNETSUITE_ID(jsonObj.getLong("NETSUITE_ID"));
				
				if (jsonObj.has("DEPARTMENT_NAME") && !jsonObj.isNull("DEPARTMENT_NAME"))
					Department.setDEPARTMENT_NAME(jsonObj.getString("DEPARTMENT_NAME"));

				if (jsonObj.has("ISINCLUDECHILD") && !jsonObj.isNull("ISINCLUDECHILD")) 
					Department.setISINCLUDECHILD(jsonObj.getString("ISINCLUDECHILD"));
				
				
				
				
				if (jsonObj.has("isactive"))
				Department.setISACTIVE(jsonObj.getString("isactive"));
			    else if (id == 0)
				Department.setISACTIVE("Y");

				Department.setMODIFIED_BY(apiRequest.getREQUEST_ID());
				Department.setMODIFIED_WORKSTATION(apiRequest.getLOG_WORKSTATION());
				Department.setMODIFIED_WHEN(dateFormat1.format(date));
				Companys.add(Department);
			}
		
			
			for (int a=0; a<Companys.size(); a++) {
				Department Department = Companys.get(a);
				Department = departmentrepository.saveAndFlush(Department);
				Companys.get(a).setCOMPANY_ID(Department.getCOMPANY_ID());
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
			APIRequestDataLog apiRequest = checkToken("GET", "/Department/"+id, null, null, headToken);
			if (apiRequest.getREQUEST_STATUS() != null) return new ResponseEntity(apiRequest.getREQUEST_OUTPUT(), HttpStatus.BAD_REQUEST);

			Department Department = departmentrepository.findOne(id);
			departmentrepository.delete(Department);
			
			return new ResponseEntity(getAPIResponse(null, Department, null, apiRequest, true).getREQUEST_OUTPUT(), HttpStatus.OK);
		}

		@SuppressWarnings({ "unchecked", "rawtypes" })
		@RequestMapping(value = "/remove/{id}", method = RequestMethod.GET)
		public ResponseEntity remove(@PathVariable Long id,@RequestHeader(value = "Authorization") String headToken) throws JsonProcessingException, JSONException, ParseException {
			APIRequestDataLog apiRequest = checkToken("GET", "/Department/"+id, null, null, headToken);
			if (apiRequest.getREQUEST_STATUS() != null) return new ResponseEntity(apiRequest.getREQUEST_OUTPUT(), HttpStatus.BAD_REQUEST);
			
			JSONObject Department = new JSONObject();
			Department.put("id", id);
			Department.put("isactive", "N");
			
			return insertupdateAll(null, Department, apiRequest);
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
			APIRequestDataLog apiRequest = checkToken("POST", "/Department/search" + ((active == true) ? "" : "/all"), data, null, headToken);
			if (apiRequest.getREQUEST_STATUS() != null) return new ResponseEntity(apiRequest.getREQUEST_OUTPUT(), HttpStatus.BAD_REQUEST);

			JSONObject jsonObj = new JSONObject(data);

			List<		Department> Companys = ((active == true)
					? departmentrepository.findBySearch("%" + jsonObj.getString("search") + "%")
					: departmentrepository.findAllBySearch("%" + jsonObj.getString("search") + "%"));
			
			return new ResponseEntity(getAPIResponse(Companys, null, null, apiRequest, false).getREQUEST_OUTPUT(), HttpStatus.OK);
		}

		public APIRequestDataLog checkToken(String requestType, String requestURI, String requestBody, String workstation, String accessToken) throws JsonProcessingException {
			JSONObject checkTokenResponse = AccessToken.checkToken(accessToken);
			DatabaseTables databaseTableID = databasetablesrepository.findOne(Department.getDatabaseTableID());
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
			
			Long requestUser = checkTokenResponse.getLong("DEPARTMENT_ID");
			apiRequest = tableDataLogs.apiRequestDataLog(requestType, databaseTableID, requestUser, requestURI, requestBody, workstation);

			return apiRequest;
		}
		
		APIRequestDataLog getAPIResponse(List<Department> Companys, Department Department, String message, APIRequestDataLog apiRequest, boolean isTableLog) throws JSONException, JsonProcessingException, ParseException {
			ObjectMapper mapper = new ObjectMapper();
			long dept_Id = 0;
			
			if (message != null) {
				apiRequest = tableDataLogs.errorDataLog(apiRequest, "Department", message);
				apirequestdatalogRepository.saveAndFlush(apiRequest);
			} else {
				if (Department != null) {
					apiRequest.setREQUEST_OUTPUT(mapper.writeValueAsString(Department));
					dept_Id = Department.getDEPARTMENTPARENT_ID();
				} else {
					apiRequest.setREQUEST_OUTPUT(mapper.writeValueAsString(Department));
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
