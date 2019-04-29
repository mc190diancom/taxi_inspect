package com.miu360.taxi_check.model;

public class LvyouDriverInfo extends Page {
	/**
	 * 姓名
	 */
	private String driverName;

	/**
	 * 身份证号
	 */
	private String id;

	/**
	 * 电话号码
	 */
	private String telphone;

	/**
	 * 地址
	 */
	private String address;

	/**
	 * 从业资格证号
	 */
	private String cyzgNumber;

	/**
	 * 公司名
	 */
	private String corpName;

	/**
	 * 生日
	 */
	private String birthday;

	/**
	 * 初次领证时间
	 */
	private String firstCardTime;

	/**
	 * 开始时间
	 */
	private String startTime;

	/**
	 * 结束时间
	 */
	private String endTime;

	/**
	 * 从业资格状态
	 */
	private String state;

	/**
	 * 类型
	 */
	private String type;

	public String getDriverName() {
		return driverName;
	}

	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTelphone() {
		return telphone;
	}

	public void setTelphone(String telphone) {
		this.telphone = telphone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCyzgNumber() {
		return cyzgNumber;
	}

	public void setCyzgNumber(String cyzgNumber) {
		this.cyzgNumber = cyzgNumber;
	}

	public String getCorpName() {
		return corpName;
	}

	public void setCorpName(String corpName) {
		this.corpName = corpName;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getFirstCardTime() {
		return firstCardTime;
	}

	public void setFirstCardTime(String firstCardTime) {
		this.firstCardTime = firstCardTime;
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

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
