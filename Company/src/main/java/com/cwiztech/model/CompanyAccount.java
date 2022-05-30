package com.cwiztech.model;

import java.text.ParseException;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.json.JSONException;
import org.json.JSONObject;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.cwiztech.services.CompanyService;
import com.cwiztech.systemsetting.model.Lookup;


import javax.persistence.CascadeType;


@Entity
@Table(name = "TBLCOMPANYACCOUNT")
public class CompanyAccount {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long COMPANYACCOUNT_ID;
	
	@Column(name = "COMPANYACCOUNT_TITLE")
	private String COMPANYACCOUNT_TITLE;

	@Column(name = "COMPANY_ID")
	private Long COMPANY_ID;
	
	  @Transient
	    private String COMPANY_DETAIL;
	
	@Column(name = "ISACTIVE")
	private String ISACTIVE;
	
	@JsonIgnore
	@Column(name = "MODIFIED_BY")
	private Long MODIFIED_BY;

	@JsonIgnore 
	@Column(name = "MODIFIED_WHEN")
	private String MODIFIED_WHEN;

	@JsonIgnore 
	@Column(name = "MODIFIED_WORKSTATION")
	private String MODIFIED_WORKSTATION;


	public String getISACTIVE() {
		return ISACTIVE;
	}


	public void setISACTIVE(String iSACTIVE) {
		ISACTIVE = iSACTIVE;
	}



	public long getCOMPANYACCOUNT_ID() {
		return COMPANYACCOUNT_ID;
	}


	public void setCOMPANYACCOUNT_ID(long cOMPANYACCOUNT_ID) {
		COMPANYACCOUNT_ID = cOMPANYACCOUNT_ID;
	}


	public String getCOMPANYACCOUNT_TITLE() {
		return COMPANYACCOUNT_TITLE;
	}


	public void setCOMPANYACCOUNT_TITLE(String cOMPANYACCOUNT_TITLE) {
		COMPANYACCOUNT_TITLE = cOMPANYACCOUNT_TITLE;
	}


	public String getCOMPANY_DETAIL() throws JSONException, JsonProcessingException, ParseException {
        JSONObject companyObject = new JSONObject(
				CompanyService.GET("company/" + this.COMPANY_ID, "bearer"));
          	COMPANY_DETAIL = companyObject.toString();
           return COMPANY_DETAIL;
 	}
	
	public Long getCOMPANY_ID() {
		return COMPANY_ID;
	}

	public void setCOMPANY_ID(Long cOMPANY_ID) {
		COMPANY_ID = cOMPANY_ID;
	}

	@JsonIgnore
	public Long getMODIFIED_BY() {
		return MODIFIED_BY;
	}

	public void setMODIFIED_BY(Long mODIFIED_BY) {
		MODIFIED_BY = mODIFIED_BY;
	}

	@JsonIgnore
	public String getMODIFIED_WHEN() {
		return MODIFIED_WHEN;
	}

	public void setMODIFIED_WHEN(String mODIFIED_WHEN) {
		MODIFIED_WHEN = mODIFIED_WHEN;
	}

	@JsonIgnore
	public String getMODIFIED_WORKSTATION() {
		return MODIFIED_WORKSTATION;
	}

	public void setMODIFIED_WORKSTATION(String mODIFIED_WORKSTATION) {
		MODIFIED_WORKSTATION = mODIFIED_WORKSTATION;
	}

	public static long getDatabaseTableID() {
		return (long) 4;
	}


}
