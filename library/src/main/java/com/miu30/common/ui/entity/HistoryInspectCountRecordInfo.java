package com.miu30.common.ui.entity;

import java.io.Serializable;

public class HistoryInspectCountRecordInfo extends Page implements Serializable {

	private String vname;

	private String driverName;

	private String corpName;

	private String hylb;

	private String code;

	private String cyzgz;

	private String zfdwmc;

	private String startTime;

	private String endTime;
	
	private String zfzh;

	public String getZfzh() {
		return zfzh;
	}

	public void setZfzh(String zfzh) {
		this.zfzh = zfzh;
	}

	public String getVname() {
		return vname;
	}

	public void setVname(String vname) {
		this.vname = vname;
	}

	public String getDriverName() {
		return driverName;
	}

	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}

	public String getCorpName() {
		return corpName;
	}

	public void setCorpName(String corpName) {
		this.corpName = corpName;
	}

	public String getHylb() {
		return hylb;
	}

	public void setHylb(String hylb) {
		this.hylb = hylb;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCyzgz() {
		return cyzgz;
	}

	public void setCyzgz(String cyzgz) {
		this.cyzgz = cyzgz;
	}

	public String getZfdwmc() {
		return zfdwmc;
	}

	public void setZfdwmc(String zfdwmc) {
		this.zfdwmc = zfdwmc;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

}
