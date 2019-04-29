package com.miu360.taxi_check.model;

import java.io.Serializable;

public class Driver implements Serializable {
	/**
	 * 姓名
	 */
	String name;
	/**
	 * 准驾号
	 */
	String driverLicence;
	/**
	 * 所在公司
	 */
	String company;

	String sex;
	String phoneNumber;
	String fangweima;
	long time;

	private String head;

	@Override
	public String toString() {
		return "Driver [name=" + name + ", driverLicence=" + driverLicence + ", company=" + company + ", sex=" + sex
				+ ", phoneNumber=" + phoneNumber + ", fangweima=" + fangweima + ", time=" + time + ", head=" + head
				+ "]";
	}

	public String getHead() {
		return head;
	}

	public void setHead(String head) {
		this.head = head;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getFangweima() {
		return fangweima;
	}

	public void setFangweima(String fangweima) {
		this.fangweima = fangweima;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDriverLicence() {
		return driverLicence;
	}

	public void setDriverLicence(String driverLicence) {
		this.driverLicence = driverLicence;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}
}
