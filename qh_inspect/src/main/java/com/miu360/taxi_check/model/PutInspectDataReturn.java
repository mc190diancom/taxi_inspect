package com.miu360.taxi_check.model;

public class PutInspectDataReturn {

	private int err;
	
	private String errorMsg;
	
	private int userId;
	
	private String imei_code;
	
	private String userName;
	
	private String rode;

	@Override
	public String toString() {
		return "PutInspectDataReturn [err=" + err + ", errorMsg=" + errorMsg
				+ ", userId=" + userId + ", imei_code=" + imei_code
				+ ", userName=" + userName + ", rode=" + rode + "]";
	}

	public int getErr() {
		return err;
	}

	public void setErr(int err) {
		this.err = err;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getImei_code() {
		return imei_code;
	}

	public void setImei_code(String imei_code) {
		this.imei_code = imei_code;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getRode() {
		return rode;
	}

	public void setRode(String rode) {
		this.rode = rode;
	}
	
	
}
