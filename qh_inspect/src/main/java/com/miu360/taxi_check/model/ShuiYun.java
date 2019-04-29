package com.miu360.taxi_check.model;

import java.io.Serializable;

public class ShuiYun implements Serializable{
	private String SHIP_NAME;
	
	private String SHIP_PORT;
	
	private String SHIP_TOTAL_WEIGHT;
	
	private String SHIP_CAPACITY;
	
	private String SHIP_OWNER;
	
	private String SHIP_MANAGER;
	
	private String SHIP_MANAGER_LICENSE_NUMBER;
	
	private String BUILD_DATE;
	
	private String SHIP_LICENSE_STARTTIME;
	
	private String SHIP_LICENSE_DEADLINE;
	
	private String SHIP_LICENSE_NUMBER;
	
	private String REGION_SJYHHY;

	public String getSHIP_NAME() {
		return SHIP_NAME;
	}

	public void setSHIP_NAME(String sHIP_NAME) {
		SHIP_NAME = sHIP_NAME;
	}

	public String getSHIP_PORT() {
		return SHIP_PORT;
	}

	public void setSHIP_PORT(String sHIP_PORT) {
		SHIP_PORT = sHIP_PORT;
	}

	public String getSHIP_TOTAL_WEIGHT() {
		return SHIP_TOTAL_WEIGHT;
	}

	public void setSHIP_TOTAL_WEIGHT(String sHIP_TOTAL_WEIGHT) {
		SHIP_TOTAL_WEIGHT = sHIP_TOTAL_WEIGHT;
	}

	public String getSHIP_CAPACITY() {
		return SHIP_CAPACITY;
	}

	public void setSHIP_CAPACITY(String sHIP_CAPACITY) {
		SHIP_CAPACITY = sHIP_CAPACITY;
	}

	public String getSHIP_OWNER() {
		return SHIP_OWNER;
	}

	public void setSHIP_OWNER(String sHIP_OWNER) {
		SHIP_OWNER = sHIP_OWNER;
	}

	public String getSHIP_MANAGER() {
		return SHIP_MANAGER;
	}

	public void setSHIP_MANAGER(String sHIP_MANAGER) {
		SHIP_MANAGER = sHIP_MANAGER;
	}

	public String getSHIP_MANAGER_LICENSE_NUMBER() {
		return SHIP_MANAGER_LICENSE_NUMBER;
	}

	public void setSHIP_MANAGER_LICENSE_NUMBER(String sHIP_MANAGER_LICENSE_NUMBER) {
		SHIP_MANAGER_LICENSE_NUMBER = sHIP_MANAGER_LICENSE_NUMBER;
	}

	public String getBUILD_DATE() {
		return BUILD_DATE;
	}

	public void setBUILD_DATE(String bUILD_DATE) {
		BUILD_DATE = bUILD_DATE;
	}

	public String getSHIP_LICENSE_STARTTIME() {
		return SHIP_LICENSE_STARTTIME;
	}

	public void setSHIP_LICENSE_STARTTIME(String sHIP_LICENSE_STARTTIME) {
		SHIP_LICENSE_STARTTIME = sHIP_LICENSE_STARTTIME;
	}

	public String getSHIP_LICENSE_DEADLINE() {
		return SHIP_LICENSE_DEADLINE;
	}

	public void setSHIP_LICENSE_DEADLINE(String sHIP_LICENSE_DEADLINE) {
		SHIP_LICENSE_DEADLINE = sHIP_LICENSE_DEADLINE;
	}

	public String getSHIP_LICENSE_NUMBER() {
		return SHIP_LICENSE_NUMBER;
	}

	public void setSHIP_LICENSE_NUMBER(String sHIP_LICENSE_NUMBER) {
		SHIP_LICENSE_NUMBER = sHIP_LICENSE_NUMBER;
	}

	public String getREGION_SJYHHY() {
		return REGION_SJYHHY;
	}

	public void setREGION_SJYHHY(String rEGION_SJYHHY) {
		REGION_SJYHHY = rEGION_SJYHHY;
	}

	@Override
	public String toString() {
		return "ShuiYun [SHIP_NAME=" + SHIP_NAME + ", SHIP_PORT=" + SHIP_PORT + ", SHIP_TOTAL_WEIGHT="
				+ SHIP_TOTAL_WEIGHT + ", SHIP_CAPACITY=" + SHIP_CAPACITY + ", SHIP_OWNER=" + SHIP_OWNER
				+ ", SHIP_MANAGER=" + SHIP_MANAGER + ", SHIP_MANAGER_LICENSE_NUMBER=" + SHIP_MANAGER_LICENSE_NUMBER
				+ ", BUILD_DATE=" + BUILD_DATE + ", SHIP_LICENSE_STARTTIME=" + SHIP_LICENSE_STARTTIME
				+ ", SHIP_LICENSE_DEADLINE=" + SHIP_LICENSE_DEADLINE + ", SHIP_LICENSE_NUMBER=" + SHIP_LICENSE_NUMBER
				+ ", REGION_SJYHHY=" + REGION_SJYHHY + "]";
	}
	
	
}
