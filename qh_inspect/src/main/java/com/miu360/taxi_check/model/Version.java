package com.miu360.taxi_check.model;

public class Version {



//[{"VERSION":"0.9.0","LOG":"修改了违法查询行业类别接口","SIZES":"44276973"}]

	private String VERSION;

	private String LOG;

	private String SIZES;

	public String getVERSION() {
		return VERSION;
	}

	public void setVERSION(String vERSION) {
		VERSION = vERSION;
	}

	public String getLOG() {
		return LOG;
	}

	public void setLOG(String lOG) {
		LOG = lOG;
	}

	public String getSIZES() {
		return SIZES;
	}

	public void setSIZES(String sIZES) {
		SIZES = sIZES;
	}

	@Override
	public String toString() {
		return "Version [VERSION=" + VERSION + ", LOG=" + LOG + ", SIZES="
				+ SIZES + "]";
	}

}
