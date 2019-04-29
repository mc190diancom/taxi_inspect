package com.miu360.taxi_check.model;

public class LoginModel {

	private int err;

	private String errorMsg;

	private int userId;

	private String sim_code;

	private String imei_code;
	
	private String rode;
	
	private String rname;

	private String userName;
	
	public String getRode() {
		return rode;
	}

	public void setRode(String rode) {
		this.rode = rode;
	}

	public String getRname() {
		return rname;
	}

	public void setRname(String rname) {
		this.rname = rname;
	}

	public void setErr(int err) {
		this.err = err;
	}

	public int getErr() {
		return this.err;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public String getErrorMsg() {
		return this.errorMsg;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getUserId() {
		return this.userId;
	}

	public void setSim_code(String sim_code) {
		this.sim_code = sim_code;
	}

	public String getSim_code() {
		return this.sim_code;
	}

	public void setImei_code(String imei_code) {
		this.imei_code = imei_code;
	}

	public String getImei_code() {
		return this.imei_code;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
}
