package com.miu360.taxi_check.model;

public class Camera {
//	SN码
    private String SN_CODE;
    
    private int RN;
    private String CAMERA_NAME;
    
    private int startIndex;
    
    private int endIndex;
    
    private int totalNum;


    
	public String getSN_CODE() {
		return SN_CODE;
	}

	public void setSN_CODE(String sN_CODE) {
		SN_CODE = sN_CODE;
	}

	public void setCAMERA_NAME(String cAMERA_NAME) {
		CAMERA_NAME = cAMERA_NAME;
	}
	public String getCAMERA_NAME() {
		return CAMERA_NAME;
	}
	public int getRN() {
		return RN;
	}

	public void setRN(int rN) {
		RN = rN;
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

	public int getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(int totalNum) {
		this.totalNum = totalNum;
	}
    
    
}
