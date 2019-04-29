package com.miu360.taxi_check.model;

import java.io.Serializable;

public class NetCarQrInfo implements Serializable{
	private String VName;
	private String CarOwn;
	private String CarModel;
	private String CarColor;
	private String PlatformName;
	private String Date;
	private String VIN;
	private String EngineNO;
	private String CarState;
	private String SecurityCode;
	
	public String getVName() {
		return VName;
	}
	public void setVName(String vName) {
		VName = vName;
	}
	public String getCarOwn() {
		return CarOwn;
	}
	public void setCarOwn(String carOwn) {
		CarOwn = carOwn;
	}
	public String getCarModel() {
		return CarModel;
	}
	public void setCarModel(String carModel) {
		CarModel = carModel;
	}
	public String getCarColor() {
		return CarColor;
	}
	public void setCarColor(String carColor) {
		CarColor = carColor;
	}
	public String getPlatformName() {
		return PlatformName;
	}
	public void setPlatformName(String platformName) {
		PlatformName = platformName;
	}
	public String getDate() {
		return Date;
	}
	public void setDate(String date) {
		Date = date;
	}
	public String getVIN() {
		return VIN;
	}
	public void setVIN(String vIN) {
		VIN = vIN;
	}
	public String getEngineNO() {
		return EngineNO;
	}
	public void setEngineNO(String engineNO) {
		EngineNO = engineNO;
	}
	public String getCarState() {
		return CarState;
	}
	public void setCarState(String carState) {
		CarState = carState;
	}
	public String getSecurityCode() {
		return SecurityCode;
	}
	public void setSecurityCode(String securityCode) {
		SecurityCode = securityCode;
	}
	@Override
	public String toString() {
		return "NetCarQrInfo [VName=" + VName + ", CarOwn=" + CarOwn
				+ ", CarModel=" + CarModel + ", CarColor=" + CarColor
				+ ", PlatformName=" + PlatformName + ", Date=" + Date
				+ ", VIN=" + VIN + ", EngineNO=" + EngineNO + ", CarState="
				+ CarState + ", SecurityCode=" + SecurityCode + "]";
	}
	
}
