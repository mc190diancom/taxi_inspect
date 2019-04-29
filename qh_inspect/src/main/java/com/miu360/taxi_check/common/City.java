package com.miu360.taxi_check.common;

public class City {
	private int id;

	private String name;
	private String province;

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public City(int id, String province, String name) {
		super();
		this.id = id;
		this.province = province;
		this.name = name;
	}

	public City() {
	}
}
