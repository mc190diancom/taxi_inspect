package com.miu360.taxi_check.model;

import java.io.Serializable;

public class QueryFaZhiBanResult implements Serializable{
	private String VNAME;

	private String ADDRESS;

	private String HYLB;

	private String SFZH;

	private String CHECK_LIST_CODE;

	private String CHECK_DATE;

	private String CHECK_PLACE;

	private String CITIZEN_NAME;

	private String CITIZEN_PHONE;

	private String LEGAL_PERSON;

	private String ORGANIZATION_PHONE;

	private int IS_CHARGE_REFORM;

	private String MODULE_NAME;

	private int IS_ENABLE;

	private int IS_STANDARD;

	private String PERSON_NAME;
	private int CHECK_RESULT;
	private String UNIT_NAME;

	public int getCHECK_RESULT() {
		return CHECK_RESULT;
	}

	public String getUNIT_NAME() {
		return UNIT_NAME;
	}

	public void setCHECK_RESULT(int cHECK_RESULT) {
		CHECK_RESULT = cHECK_RESULT;
	}

	public void setUNIT_NAME(String uNIT_NAME) {
		UNIT_NAME = uNIT_NAME;
	}
	public void setVNAME(String VNAME){
	this.VNAME = VNAME;
	}
	public String getVNAME(){
	return this.VNAME;
	}
	public void setADDRESS(String ADDRESS){
	this.ADDRESS = ADDRESS;
	}
	public String getADDRESS(){
	return this.ADDRESS;
	}
	public void setHYLB(String HYLB){
	this.HYLB = HYLB;
	}
	public String getHYLB(){
	return this.HYLB;
	}
	public void setSFZH(String SFZH){
	this.SFZH = SFZH;
	}
	public String getSFZH(){
	return this.SFZH;
	}
	public void setCHECK_LIST_CODE(String CHECK_LIST_CODE){
	this.CHECK_LIST_CODE = CHECK_LIST_CODE;
	}
	public String getCHECK_LIST_CODE(){
	return this.CHECK_LIST_CODE;
	}
	public void setCHECK_DATE(String CHECK_DATE){
	this.CHECK_DATE = CHECK_DATE;
	}
	public String getCHECK_DATE(){
	return this.CHECK_DATE;
	}
	public void setCHECK_PLACE(String CHECK_PLACE){
	this.CHECK_PLACE = CHECK_PLACE;
	}
	public String getCHECK_PLACE(){
	return this.CHECK_PLACE;
	}
	public void setCITIZEN_NAME(String CITIZEN_NAME){
	this.CITIZEN_NAME = CITIZEN_NAME;
	}
	public String getCITIZEN_NAME(){
	return this.CITIZEN_NAME;
	}
	public void setCITIZEN_PHONE(String CITIZEN_PHONE){
	this.CITIZEN_PHONE = CITIZEN_PHONE;
	}
	public String getCITIZEN_PHONE(){
	return this.CITIZEN_PHONE;
	}
	public void setLEGAL_PERSON(String LEGAL_PERSON){
	this.LEGAL_PERSON = LEGAL_PERSON;
	}
	public String getLEGAL_PERSON(){
	return this.LEGAL_PERSON;
	}
	public void setORGANIZATION_PHONE(String ORGANIZATION_PHONE){
	this.ORGANIZATION_PHONE = ORGANIZATION_PHONE;
	}
	public String getORGANIZATION_PHONE(){
	return this.ORGANIZATION_PHONE;
	}
	public void setIS_CHARGE_REFORM(int IS_CHARGE_REFORM){
	this.IS_CHARGE_REFORM = IS_CHARGE_REFORM;
	}
	public int getIS_CHARGE_REFORM(){
	return this.IS_CHARGE_REFORM;
	}
	public void setMODULE_NAME(String MODULE_NAME){
	this.MODULE_NAME = MODULE_NAME;
	}
	public String getMODULE_NAME(){
	return this.MODULE_NAME;
	}
	public void setIS_ENABLE(int IS_ENABLE){
	this.IS_ENABLE = IS_ENABLE;
	}
	public int getIS_ENABLE(){
	return this.IS_ENABLE;
	}
	public void setIS_STANDARD(int IS_STANDARD){
	this.IS_STANDARD = IS_STANDARD;
	}
	public int getIS_STANDARD(){
	return this.IS_STANDARD;
	}
	public void setPERSON_NAME(String PERSON_NAME){
	this.PERSON_NAME = PERSON_NAME;
	}
	public String getPERSON_NAME(){
	return this.PERSON_NAME;
	}
	
	
}
