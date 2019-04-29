package com.miu360.taxi_check.model;

public class CompanyInfo extends Page{
	/**
	 * 单位名称
	 */
	private String companyName;
	/**
	 * 法人代表
	 */
	private String farenDb;

	/**
	 * 业户经营许可证号
	 */
	private String yehuLicenceNumber;
	/**
	 * 业户联系方式
	 */
	private String yehuTelphone;

	/**
	 * 业户地址
	 */
	private String yehuAddress;
	/**
	 * 立户号
	 */
	private String lhh;

	public String getLhh() {
		return lhh;
	}

	public void setLhh(String lhh) {
		this.lhh = lhh;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getFarenDb() {
		return farenDb;
	}

	public void setFarenDb(String farenDb) {
		this.farenDb = farenDb;
	}

	public String getYehuLicenceNumber() {
		return yehuLicenceNumber;
	}

	public void setYehuLicenceNumber(String yehuLicenceNumber) {
		this.yehuLicenceNumber = yehuLicenceNumber;
	}

	public String getYehuTelphone() {
		return yehuTelphone;
	}

	public void setYehuTelphone(String yehuTelphone) {
		this.yehuTelphone = yehuTelphone;
	}

	public String getYehuAddress() {
		return yehuAddress;
	}

	public void setYehuAddress(String yehuAddress) {
		this.yehuAddress = yehuAddress;
	}

	@Override
	public String toString() {
		return "CompanyInfo [companyName=" + companyName + ", farenDb="
				+ farenDb + ", yehuLicenceNumber=" + yehuLicenceNumber
				+ ", yehuTelphone=" + yehuTelphone + ", yehuAddress="
				+ yehuAddress + ", lhh=" + lhh + "]";
	}
}
