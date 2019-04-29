package com.miu360.taxi_check.model;

public class CardQr {

	private String id;
	
	private String path;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	@Override
	public String toString() {
		return "CardQr [id=" + id + ", path=" + path + "]";
	}
	
	
}
