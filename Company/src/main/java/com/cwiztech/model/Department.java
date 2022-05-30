package com.cwiztech.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "TBLDEPARTMENT")
public class Department {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long DEPARTMENT_ID;
	
	@ManyToOne
	@JoinColumn(name = "DEPARTMENTPARENT_ID")
	private Long DEPARTMENTPARENT_ID;
	
	@Column(name = "NETSUITE_ID")
	private Long NETSUITE_ID;
	
	@ManyToOne
	@JoinColumn(name = "COMPANY_ID")
	private Company COMPANY_ID;
	
	@Column(name = "DEPARTMENT_NAME")
	private String DEPARTMENT_NAME ;
	
	@Column(name = "ISINCLUDECHILD")
	private String ISINCLUDECHILD;
	
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

	public long getDEPARTMENT_ID() {
		return DEPARTMENT_ID;
	}

	public void setDEPARTMENT_ID(long dEPARTMENT_ID) {
		DEPARTMENT_ID = dEPARTMENT_ID;
	}

	public Long getDEPARTMENTPARENT_ID() {
		return DEPARTMENTPARENT_ID;
	}

	public void setDEPARTMENTPARENT_ID(Long dEPARTMENTPARENT_ID) {
		DEPARTMENTPARENT_ID = dEPARTMENTPARENT_ID;
	}

	public Long getNETSUITE_ID() {
		return NETSUITE_ID;
	}

	public void setNETSUITE_ID(Long nETSUITE_ID) {
		NETSUITE_ID = nETSUITE_ID;
	}

	public Company getCOMPANY_ID() {
		return COMPANY_ID;
	}

	public void setCOMPANY_ID(Company cOMPANY_ID) {
		COMPANY_ID = cOMPANY_ID;
	}

	public String getDEPARTMENT_NAME() {
		return DEPARTMENT_NAME;
	}

	public void setDEPARTMENT_NAME(String dEPARTMENT_NAME) {
		DEPARTMENT_NAME = dEPARTMENT_NAME;
	}

	public String getISINCLUDECHILD() {
		return ISINCLUDECHILD;
	}

	public void setISINCLUDECHILD(String iSINCLUDECHILD) {
		ISINCLUDECHILD = iSINCLUDECHILD;
	}

	public String getISACTIVE() {
		return ISACTIVE;
	}

	public void setISACTIVE(String iSACTIVE) {
		ISACTIVE = iSACTIVE;
	}

	public Long getMODIFIED_BY() {
		return MODIFIED_BY;
	}

	public void setMODIFIED_BY(Long mODIFIED_BY) {
		MODIFIED_BY = mODIFIED_BY;
	}

	public String getMODIFIED_WHEN() {
		return MODIFIED_WHEN;
	}

	public void setMODIFIED_WHEN(String mODIFIED_WHEN) {
		MODIFIED_WHEN = mODIFIED_WHEN;
	}

	public String getMODIFIED_WORKSTATION() {
		return MODIFIED_WORKSTATION;
	}

	public void setMODIFIED_WORKSTATION(String mODIFIED_WORKSTATION) {
		MODIFIED_WORKSTATION = mODIFIED_WORKSTATION;
	}

	public static long getDatabaseTableID() {
		return (long) 6;
	}
}
