package com.miu30.common.ui.entity;

public class DriverInfo extends Page {
	/**
	 * 监督卡号
	 */
	private String jianduNumber;
	/**
	 * 公司名称
	 */
//	private String hylb;
//	public String getHylb() {
//		return hylb;
//	}
//
//	public void setHylb(String hylb) {
//		this.hylb = hylb;
//	}

	private String companyName;
	private String imageID;

	public String getImageID() {
		return imageID;
	}

	public void setImageID(String imageID) {
		this.imageID = imageID;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	/**
	 * 司机姓名
	 */
	private String driverName;

	/**
	 * 身份证号
	 */
	private String id;

	/**
	 * 联系电话
	 */
	private String telphone;

	/**
	 * 住址
	 */
	private String address;

	/**
	 * 初次上岗日期
	 */
	private String firstSgDate;

	/**
	 * 所属公司立户号
	 */
	private String lhh;

	private String sex;

	/**
	 * 本次上岗合同开始日期
	 */
	private String currentSgHtStartDate;

	public String getJianduNumber() {
		return jianduNumber;
	}

	public String getLhh() {
		return lhh;
	}

	public void setLhh(String lhh) {
		this.lhh = lhh;
	}

	public void setJianduNumber(String jianduNumber) {
		this.jianduNumber = jianduNumber;
	}

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

	public String getFirstSgDate() {
		return firstSgDate;
	}

	public void setFirstSgDate(String firstSgDate) {
		this.firstSgDate = firstSgDate;
	}

	public String getCurrentSgHtStartDate() {
		return currentSgHtStartDate;
	}

	public void setCurrentSgHtStartDate(String currentSgHtStartDate) {
		this.currentSgHtStartDate = currentSgHtStartDate;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	@Override
	public String toString() {
		return "DriverInfo{" +
				"jianduNumber='" + jianduNumber + '\'' +
				", companyName='" + companyName + '\'' +
				", imageID='" + imageID + '\'' +
				", driverName='" + driverName + '\'' +
				", id='" + id + '\'' +
				", telphone='" + telphone + '\'' +
				", address='" + address + '\'' +
				", firstSgDate='" + firstSgDate + '\'' +
				", lhh='" + lhh + '\'' +
				", sex='" + sex + '\'' +
				", currentSgHtStartDate='" + currentSgHtStartDate + '\'' +
				'}';
	}
}
