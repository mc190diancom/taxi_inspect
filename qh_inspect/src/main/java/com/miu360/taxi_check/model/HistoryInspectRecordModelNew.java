package com.miu360.taxi_check.model;

public class HistoryInspectRecordModelNew  extends Page{
	private String certificate_no;

	private String color;

	private String brand;

	private String vin;

	private String xh;

	private String model;

	private String vcompany;

	private String pcompany;

	private String phone;

	private String family_addr;

	private String frdb;

	private String wfxwId;

	private String wfqxId;

	private String sszd;

	private String sscz;

	private String CheckPlaceOrgan;

	private String id;

	private String hylb;

	private String jdkh;

	private String driverName;

	private String llh;
	
	private String zfry1;

	private String zfry2;

	private String address;

	private String zfsj;

	private String vname;

	private String sfzh;

	private String corpname;

	private String yyzh;

	private String status;

	private String vehicle_check_items;

	private String person_check_items;

	private String yehu_check_items;

	private String zfzh;

	private String zfdwmc;

	private String startTime;

	private String endTime;

	private String zwstatus;
	
	private String jclb;
	
	private String zcfstatus;
	
	
	
	public String getZcfstatus() {
		return zcfstatus;
	}
	
	public void setZcfstatus(String zcfstatus) {
		this.zcfstatus = zcfstatus;
	}
	
	public String getJclb() {
		return jclb;
	}

	public void setJclb(String jclb) {
		this.jclb = jclb;
	}

	public String getZwstatus() {
		return zwstatus;
	}

	public void setZwstatus(String zwstatus) {
		this.zwstatus = zwstatus;
	}

	private int normal;

	private int illegal;

	private int total;

	private int dataSource;

	private double lon;

	private double lat;

	private String jyxkz;
	
	private String jcbh;
	

	public String getJcbh() {
		return jcbh;
	}

	public void setJcbh(String jcbh) {
		this.jcbh = jcbh;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getHylb() {
		return hylb;
	}

	public void setHylb(String hylb) {
		this.hylb = hylb;
	}

	public String getJdkh() {
		return jdkh;
	}

	public void setJdkh(String jdkh) {
		this.jdkh = jdkh;
	}

	public String getDriverName() {
		return driverName;
	}

	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}

	public String getLlh() {
		return llh;
	}

	public void setLlh(String llh) {
		this.llh = llh;
	}

	public String getZfry1() {
		return zfry1;
	}

	public void setZfry1(String zfry1) {
		this.zfry1 = zfry1;
	}

	public String getZfry2() {
		return zfry2;
	}

	public void setZfry2(String zfry2) {
		this.zfry2 = zfry2;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getZfsj() {
		return zfsj;
	}

	public void setZfsj(String zfsj) {
		this.zfsj = zfsj;
	}

	public String getVname() {
		return vname;
	}

	public void setVname(String vname) {
		this.vname = vname;
	}

	public String getSfzh() {
		return sfzh;
	}

	public void setSfzh(String sfzh) {
		this.sfzh = sfzh;
	}

	public String getCorpname() {
		return corpname;
	}

	public void setCorpname(String corpname) {
		this.corpname = corpname;
	}

	public String getYyzh() {
		return yyzh;
	}

	public void setYyzh(String yyzh) {
		this.yyzh = yyzh;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getVehicle_check_items() {
		return vehicle_check_items;
	}

	public void setVehicle_check_items(String vehicle_check_items) {
		this.vehicle_check_items = vehicle_check_items;
	}

	public String getPerson_check_items() {
		return person_check_items;
	}

	public void setPerson_check_items(String person_check_items) {
		this.person_check_items = person_check_items;
	}

	public String getYehu_check_items() {
		return yehu_check_items;
	}

	public void setYehu_check_items(String yehu_check_items) {
		this.yehu_check_items = yehu_check_items;
	}

	public String getZfzh() {
		return zfzh;
	}

	public void setZfzh(String zfzh) {
		this.zfzh = zfzh;
	}

	public String getZfdwmc() {
		return zfdwmc;
	}

	public void setZfdwmc(String zfdwmc) {
		this.zfdwmc = zfdwmc;
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

	public int getNormal() {
		return normal;
	}

	public void setNormal(int normal) {
		this.normal = normal;
	}

	public int getIllegal() {
		return illegal;
	}

	public void setIllegal(int illegal) {
		this.illegal = illegal;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getDataSource() {
		return dataSource;
	}

	public void setDataSource(int dataSource) {
		this.dataSource = dataSource;
	}

	public double getLon() {
		return lon;
	}

	public void setLon(double lon) {
		this.lon = lon;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public String getJyxkz() {
		return jyxkz;
	}

	public void setJyxkz(String jyxkz) {
		this.jyxkz = jyxkz;
	}

	@Override
	public String toString() {
		return "HistoryInspectRecordModel [id=" + id + ", hylb=" + hylb + ", jdkh=" + jdkh + ", driverName="
				+ driverName + ", llh=" + llh + ", zfry1=" + zfry1 + ", zfry2=" + zfry2 + ", address=" + address
				+ ", zfsj=" + zfsj + ", vname=" + vname + ", sfzh=" + sfzh + ", corpname=" + corpname + ", yyzh=" + yyzh
				+ ", status=" + status + ", vehicle_check_items=" + vehicle_check_items + ", person_check_items="
				+ person_check_items + ", yehu_check_items=" + yehu_check_items + ", zfzh=" + zfzh + ", zfdwmc="
				+ zfdwmc + ", startTime=" + startTime + ", endTime=" + endTime + ", zwstatus=" + zwstatus + ", normal="
				+ normal + ", illegal=" + illegal + ", total=" + total + ", dataSource=" + dataSource + ", lon=" + lon
				+ ", lat=" + lat + ", jyxkz=" + jyxkz + ", jcbh=" + jcbh + "]";
	}
	
	
	
	public String getCertificate_no() {
		return certificate_no;
	}

	public void setCertificate_no(String certificate_no) {
		this.certificate_no = certificate_no;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getVin() {
		return vin;
	}

	public void setVin(String vin) {
		this.vin = vin;
	}

	public String getXh() {
		return xh;
	}

	public void setXh(String xh) {
		this.xh = xh;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getVcompany() {
		return vcompany;
	}

	public void setVcompany(String vcompany) {
		this.vcompany = vcompany;
	}

	public String getPcompany() {
		return pcompany;
	}

	public void setPcompany(String pcompany) {
		this.pcompany = pcompany;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getFamily_addr() {
		return family_addr;
	}

	public void setFamily_addr(String family_addr) {
		this.family_addr = family_addr;
	}

	public String getFrdb() {
		return frdb;
	}

	public void setFrdb(String frdb) {
		this.frdb = frdb;
	}

	public String getWfxwId() {
		return wfxwId;
	}

	public void setWfxwId(String wfxwId) {
		this.wfxwId = wfxwId;
	}

	public String getWfqxId() {
		return wfqxId;
	}

	public void setWfqxId(String wfqxId) {
		this.wfqxId = wfqxId;
	}

	public String getSszd() {
		return sszd;
	}

	public void setSszd(String sszd) {
		this.sszd = sszd;
	}

	public String getSscz() {
		return sscz;
	}

	public void setSscz(String sscz) {
		this.sscz = sscz;
	}

	public String getCheckPlaceOrgan() {
		return CheckPlaceOrgan;
	}

	public void setCheckPlaceOrgan(String checkPlaceOrgan) {
		CheckPlaceOrgan = checkPlaceOrgan;
	}	
}
