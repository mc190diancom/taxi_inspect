package com.miu360.taxi_check.model;

public class ZFRYDetailInfo {
	private String zfzh;

	private String name;

	private String sex;

	private String zfdwmc;

	private String sjssdwmc;

	private String password;

	private String phone;

	private String photo;

	private String feedback;
	private String sscz;

	private String sszd;

	@Override
	public String toString() {
		return "ZFRYDetailInfo [zfzh=" + zfzh + ", name=" + name + ", sex=" + sex + ", zfdwmc=" + zfdwmc + ", sjssdwmc="
				+ sjssdwmc + ", password=" + password + ", phone=" + phone + ", photo=" + photo + ", feedback="
				+ feedback + ", sscz=" + sscz + ", sszd=" + sszd + "]";
	}

	public String getSscz() {
		return sscz;
	}

	public void setSscz(String sscz) {
		this.sscz = sscz;
	}

	public String getSszd() {
		return sszd;
	}

	public void setSszd(String sszd) {
		this.sszd = sszd;
	}

	public String getZfzh() {
		return zfzh;
	}

	public void setZfzh(String zfzh) {
		this.zfzh = zfzh;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getZfdwmc() {
		return zfdwmc;
	}

	public void setZfdwmc(String zfdwmc) {
		this.zfdwmc = zfdwmc;
	}

	public String getSjssdwmc() {
		return sjssdwmc;
	}

	public void setSjssdwmc(String sjssdwmc) {
		this.sjssdwmc = sjssdwmc;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public String getFeedback() {
		return feedback;
	}

	public void setFeedback(String feedback) {
		this.feedback = feedback;
	}

}
