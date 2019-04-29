package com.miu30.common.ui.entity;

public class VehicleInfo extends Page{
	/**
	 * 车牌号
	 */
	private String vname;
	/**
	 * 所属公司
	 */
	private String company;
	/**
	 * 厂牌
	 */
	private String brand;

	/**
	 * 车辆类型
	 */
	private String vehicleType;

	/**
	 * 车辆型号
	 */
	private String vehicleXH;
	/**
	 * 车辆颜色
	 */
	private String vehicleColor;
	/**
	 * 单双班
	 */
	private String danshuangBan;

	/**
	 * 营运证号
	 */
	private String yingyunNumber;

	/**
	 * 车驾号
	 */
	private String chejiaNumber;

	/**
	 * 行驶里程
	 */
	private String driverMile;

	/**
	 * 经营许可证号
	 */
	private String jingyinLicenceNumber;

	/**
	 * 运营状态
	 */
	private String yunyingStatus;

	/**
	 * 行驶证编号
	 */
	private String driverNumber;

	public String getVname() {
		return vname;
	}

	public void setVname(String vname) {
		this.vname = vname;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getVehicleType() {
		return vehicleType;
	}

	public void setVehicleType(String vehicleType) {
		this.vehicleType = vehicleType;
	}

	public String getVehicleXH() {
		return vehicleXH;
	}

	public void setVehicleXH(String vehicleXH) {
		this.vehicleXH = vehicleXH;
	}

	public String getVehicleColor() {
		return vehicleColor;
	}

	public void setVehicleColor(String vehicleColor) {
		this.vehicleColor = vehicleColor;
	}

	public String getDanshuangBan() {
		return danshuangBan;
	}

	public void setDanshuangBan(String danshuangBan) {
		this.danshuangBan = danshuangBan;
	}

	public String getYingyunNumber() {
		return yingyunNumber;
	}

	public void setYingyunNumber(String yingyunNumber) {
		this.yingyunNumber = yingyunNumber;
	}

	public String getChejiaNumber() {
		return chejiaNumber;
	}

	public void setChejiaNumber(String chejiaNumber) {
		this.chejiaNumber = chejiaNumber;
	}

	public String getDriverMile() {
		return driverMile;
	}

	public void setDriverMile(String driverMile) {
		this.driverMile = driverMile;
	}

	public String getJingyinLicenceNumber() {
		return jingyinLicenceNumber;
	}

	public void setJingyinLicenceNumber(String jingyinLicenceNumber) {
		this.jingyinLicenceNumber = jingyinLicenceNumber;
	}

	public String getYunyingStatus() {
		return yunyingStatus;
	}

	public void setYunyingStatus(String yunyingStatus) {
		this.yunyingStatus = yunyingStatus;
	}

	public String getDriverNumber() {
		return driverNumber;
	}

	public void setDriverNumber(String driverNumber) {
		this.driverNumber = driverNumber;
	}

	@Override
	public String toString() {
		return "VehicleInfo [vname=" + vname + ", company=" + company
				+ ", brand=" + brand + ", vehicleType=" + vehicleType
				+ ", vehicleXH=" + vehicleXH + ", vehicleColor=" + vehicleColor
				+ ", danshuangBan=" + danshuangBan + ", yingyunNumber="
				+ yingyunNumber + ", chejiaNumber=" + chejiaNumber
				+ ", driverMile=" + driverMile + ", jingyinLicenceNumber="
				+ jingyinLicenceNumber + ", yunyingStatus=" + yunyingStatus
				+ ", driverNumber=" + driverNumber + "]";
	}


}
