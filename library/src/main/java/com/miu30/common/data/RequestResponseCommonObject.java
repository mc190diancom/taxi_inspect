package com.miu30.common.data;
/**
 * @author Administrator
 * 
 */
public class RequestResponseCommonObject  {
	private String type;
	private Object jsonStr;
	public Object getJsonStr() {
		return jsonStr;
	}
	public void setJsonStr(Object jsonStr) {
		this.jsonStr = jsonStr;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
}
