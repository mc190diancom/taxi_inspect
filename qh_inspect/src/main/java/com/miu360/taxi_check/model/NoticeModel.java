package com.miu360.taxi_check.model;

public class NoticeModel {

	// "BT":"测试",
	// "RELEASE_PERSON":"",
	// "RELEASE_GROUP":"","STATUS":"",
	// "STARTTIME":"1476710000",
	// "ENDTIME":"1476730000",
	// "startIndex":0,
	// "endIndex":5}

	private String BT;
	private String RELEASE_PERSON;
	private String RELEASE_GROUP;
	private String STARTTIME;
	private String ENDTIME;
	private int startIndex;
	private int endIndex;
	private String STATUS;

	public String getBT() {
		return BT;
	}

	public void setBT(String bT) {
		BT = bT;
	}

	public void setSTATUS(String sTATUS) {
		STATUS = sTATUS;
	}

	public String getSTATUS() {
		return STATUS;
	}

	public String getRELEASE_PERSON() {
		return RELEASE_PERSON;
	}

	public void setRELEASE_PERSON(String rELEASE_PERSON) {
		RELEASE_PERSON = rELEASE_PERSON;
	}

	public String getRELEASE_GROUP() {
		return RELEASE_GROUP;
	}

	public void setRELEASE_GROUP(String rELEASE_GROUP) {
		RELEASE_GROUP = rELEASE_GROUP;
	}

	public int getStartIndex() {
		return startIndex;
	}

	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}

	public int getEndIndex() {
		return endIndex;
	}

	public void setEndIndex(int endIndex) {
		this.endIndex = endIndex;
	}

	public String getSTARTTIME() {
		return STARTTIME;
	}

	public void setSTARTTIME(String sTARTTIME) {
		STARTTIME = sTARTTIME;
	}

	public String getENDTIME() {
		return ENDTIME;
	}

	public void setENDTIME(String eNDTIME) {
		ENDTIME = eNDTIME;
	}

}
