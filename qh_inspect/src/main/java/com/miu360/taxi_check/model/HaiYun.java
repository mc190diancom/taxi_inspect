package com.miu360.taxi_check.model;

import java.io.Serializable;

public class HaiYun implements Serializable{
   private String NAME;
   
   private String OWNER;
   
   private String BUILDER;

public String getNAME() {
	return NAME;
}

public void setNAME(String nAME) {
	NAME = nAME;
}

public String getOWNER() {
	return OWNER;
}

public void setOWNER(String oWNER) {
	OWNER = oWNER;
}

public String getBUILDER() {
	return BUILDER;
}

public void setBUILDER(String bUILDER) {
	BUILDER = bUILDER;
}
   
   
}
