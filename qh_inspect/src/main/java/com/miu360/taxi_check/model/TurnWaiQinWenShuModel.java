package com.miu360.taxi_check.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TurnWaiQinWenShuModel implements Serializable {

	@JsonProperty
	private String IndustryType;// 行业类别
	@JsonProperty
	private String EnforcementNo1;// 执法人员证号1
	@JsonProperty
	private String EnforcementNo2;// 执法人员证号2
	@JsonProperty
	private String Qualification;// 监督卡号
	@JsonProperty
	private String ObjectPerson;// 当事人
	@JsonProperty
	private String IdentityNo;// 身份证号
	@JsonProperty
	private String LicenseNo;// 车牌号
	@JsonProperty
	private String CheckTime;// 检查时间
	@JsonProperty
	private String IllegalCauseDescription;// 违法情况（描述）
	@JsonProperty
	private String CreatePerson;// 创建人
	@JsonProperty
	private String CreateTime;// 创建时间



	@Override
	public String toString() {
		return "TurnWaiQinWenShuModel [IndustryType=" + IndustryType + ", EnforcementNo1=" + EnforcementNo1
				+ ", EnforcementNo2=" + EnforcementNo2 + ", Qualification=" + Qualification + ", ObjectPerson="
				+ ObjectPerson + ", IdentityNo=" + IdentityNo + ", LicenseNo=" + LicenseNo + ", CheckTime=" + CheckTime
				+ ", IllegalCauseDescription=" + IllegalCauseDescription + ", CreatePerson=" + CreatePerson
				+ ", CreateTime=" + CreateTime + "]";
	}

	@JsonIgnore
	public String getCreatePerson() {
		return CreatePerson;
	}

	@JsonIgnore
	public void setCreatePerson(String createPerson) {
		CreatePerson = createPerson;
	}

	@JsonIgnore
	public String getCreateTime() {
		return CreateTime;
	}

	@JsonIgnore
	public void setCreateTime(String createTime) {
		CreateTime = createTime;
	}

	@JsonIgnore
	public String getIndustryType() {
		return IndustryType;
	}

	@JsonIgnore
	public void setIndustryType(String industryType) {
		IndustryType = industryType;
	}

	@JsonIgnore
	public String getEnforcementNo1() {
		return EnforcementNo1;
	}

	@JsonIgnore
	public void setEnforcementNo1(String enforcementNo1) {
		EnforcementNo1 = enforcementNo1;
	}

	@JsonIgnore
	public String getEnforcementNo2() {
		return EnforcementNo2;
	}

	@JsonIgnore
	public void setEnforcementNo2(String enforcementNo2) {
		EnforcementNo2 = enforcementNo2;
	}

	@JsonIgnore
	public String getQualification() {
		return Qualification;
	}

	@JsonIgnore
	public void setQualification(String qualification) {
		Qualification = qualification;
	}

	@JsonIgnore
	public String getObjectPerson() {
		return ObjectPerson;
	}

	@JsonIgnore
	public void setObjectPerson(String objectPerson) {
		ObjectPerson = objectPerson;
	}

	@JsonIgnore
	public String getIdentityNo() {
		return IdentityNo;
	}

	@JsonIgnore
	public void setIdentityNo(String identityNo) {
		IdentityNo = identityNo;
	}

	@JsonIgnore
	public String getLicenseNo() {
		return LicenseNo;
	}

	@JsonIgnore
	public void setLicenseNo(String licenseNo) {
		LicenseNo = licenseNo;
	}

	@JsonIgnore
	public String getCheckTime() {
		return CheckTime;
	}

	@JsonIgnore
	public void setCheckTime(String checkTime) {
		CheckTime = checkTime;
	}

	@JsonIgnore
	public String getIllegalCauseDescription() {
		return IllegalCauseDescription;
	}

	@JsonIgnore
	public void setIllegalCauseDescription(String illegalCauseDescription) {
		IllegalCauseDescription = illegalCauseDescription;
	}

}
