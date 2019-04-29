package com.miu360.taxi_check.model;

public class FazhiBanAllQuenyModel {
	private String HYLB;

	private String CITIZEN_NAME;

	private String STARTDATE;

	private String ENDDATE;

	private String MODULE_NAME;

	private String IS_ENABLE;
	

	public void setHYLB(String HYLB){
	this.HYLB = HYLB;
	}
	
	public String getHYLB(){
	return this.HYLB;
	}
	
	public void setCITIZEN_NAME(String CITIZEN_NAME){
	this.CITIZEN_NAME = CITIZEN_NAME;
	}
	
	public String getCITIZEN_NAME(){
	return this.CITIZEN_NAME;
	}
	public void setSTARTDATE(String STARTDATE){
	this.STARTDATE = STARTDATE;
	}
	public String getSTARTDATE(){
	return this.STARTDATE;
	}
	public void setENDDATE(String ENDDATE){
	this.ENDDATE = ENDDATE;
	}
	public String getENDDATE(){
	return this.ENDDATE;
	}
	public void setMODULE_NAME(String MODULE_NAME){
	this.MODULE_NAME = MODULE_NAME;
	}
	public String getMODULE_NAME(){
	return this.MODULE_NAME;
	}
	public void setIS_ENABLE(String IS_ENABLE){
	this.IS_ENABLE = IS_ENABLE;
	}
	public String getIS_ENABLE(){
	return this.IS_ENABLE;
	}
}
