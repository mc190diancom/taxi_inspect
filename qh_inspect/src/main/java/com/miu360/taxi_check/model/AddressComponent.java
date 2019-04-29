package com.miu360.taxi_check.model;

public class AddressComponent {
   private String formatted_address;
   
   private String city;
   
   private String district;

public String getFormatted_address() {
	return formatted_address;
}

public void setFormatted_address(String formatted_address) {
	this.formatted_address = formatted_address;
}

public String getCity() {
	return city;
}

public void setCity(String city) {
	this.city = city;
}

public String getDistrict() {
	return district;
}

public void setDistrict(String district) {
	this.district = district;
}
   
   
}
