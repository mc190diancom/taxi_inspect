package com.miu360.taxi_check.model;

public class WeiFaInfo extends Page	{
	/**
	 * 车牌号
	 */
	private String vname;
	/**
	 * 当事人
	 */
	private String danshiren;
	/**
	 * 身份证
	 */
	private String id;
	/**
	 * 监督卡号
	 */
	private String jdkh;
	/**
	 * 住址
	 */
	private String address;
	/**
	 * 车辆颜色
	 */
	private String vColor;
	/**
	 * 车辆类型
	 */
	private String vType;
	/**
	 * 单位名称
	 */
	private String corpName;
	/**
	 * 检查时间
	 */
	private String checkTime;
	/**
	 * 检查地点
	 */
	private String checkAddress;
	/**
	 * 处理大队
	 */
	private String processGroup;
	/**
	 * 状态
	 */
	private String status;
	/**
	 * 结束时间
	 */
	private String overTime;
	/**
	 * 违法行为
	 */
	private String wfxw;
	/**
	 * 违章事由
	 */
	private String wzsy;

	/**
	 * 结案时间
	 */
	private String jaTime;

	/**
	 * 行业类别
	 */
	private String hylb;

	public String getHylb() {
		return hylb;
	}

	public void setHylb(String hylb) {
		this.hylb = hylb;
	}

	public String getJaTime() {
		return jaTime;
	}

	public void setJaTime(String jaTime) {
		this.jaTime = jaTime;
	}

	public String getVname() {
		return vname;
	}

	public void setVname(String vname) {
		this.vname = vname;
	}

	public String getDanshiren() {
		return danshiren;
	}

	public void setDanshiren(String danshiren) {
		this.danshiren = danshiren;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getJdkh() {
		return jdkh;
	}

	public void setJdkh(String jdkh) {
		this.jdkh = jdkh;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getvColor() {
		return vColor;
	}

	public void setvColor(String vColor) {
		this.vColor = vColor;
	}

	public String getvType() {
		return vType;
	}

	public void setvType(String vType) {
		this.vType = vType;
	}

	public String getCorpName() {
		return corpName;
	}

	public void setCorpName(String corpName) {
		this.corpName = corpName;
	}

	public String getCheckTime() {
		return checkTime;
	}

	public void setCheckTime(String checkTime) {
		this.checkTime = checkTime;
	}

	public String getCheckAddress() {
		return checkAddress;
	}

	public void setCheckAddress(String checkAddress) {
		this.checkAddress = checkAddress;
	}

	public String getProcessGroup() {
		return processGroup;
	}

	public void setProcessGroup(String processGroup) {
		this.processGroup = processGroup;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getOverTime() {
		return overTime;
	}

	public void setOverTime(String overTime) {
		this.overTime = overTime;
	}

	public String getWfxw() {
		return wfxw;
	}

	public void setWfxw(String wfxw) {
		this.wfxw = wfxw;
	}

	public String getWzsy() {
		return wzsy;
	}

	public void setWzsy(String wzsy) {
		this.wzsy = wzsy;
	}

	@Override
	public String toString() {
		return "WeiFaInfo [vname=" + vname + ", danshiren=" + danshiren + ", id=" + id + ", jdkh=" + jdkh + ", address="
				+ address + ", vColor=" + vColor + ", vType=" + vType + ", corpName=" + corpName + ", checkTime="
				+ checkTime + ", checkAddress=" + checkAddress + ", processGroup=" + processGroup + ", status=" + status
				+ ", overTime=" + overTime + ", wfxw=" + wfxw + ", wzsy=" + wzsy + ", jaTime=" + jaTime + "]";
	}

}
