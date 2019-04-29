package com.miu360.taxi_check.model;

public class ExitStatus {
    private String zfzh;
    private String sign;

	public String getZfzh() {
		return zfzh;
	}

	public void setZfzh(String zfzh) {
		this.zfzh = zfzh;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	@Override
	public String toString() {
		return "ExitStatus{" +
				"zfzh='" + zfzh + '\'' +
				", sign='" + sign + '\'' +
				'}';
	}


}
