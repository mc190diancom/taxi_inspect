package com.miu360.taxi_check.common;

public class GetHead {
	/**
	 * 取得方向的中文描述
	 *
	 * @param angle
	 * @return
	 */
	public static String getHeadingDesc(double angle) {
		String head = "";
		if (angle > 337.5)
			head = "北";
		else if (angle > 292.5)
			head = "西北";
		else if (angle > 247.5)
			head = "西";
		else if (angle > 202.5)
			head = "西南";
		else if (angle > 157.5)
			head = "南";
		else if (angle > 112.5)
			head = "东南";
		else if (angle > 67.5)
			head = "东";
		else if (angle > 22.5)
			head = "东北";
		else
			head = "北";
		return head;
	}
}
