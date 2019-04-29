package com.miu30.common.ui.entity;

import java.io.Serializable;

/**
 * 分页
 * 
 * @author Administrator
 * 
 */
public class Page implements Serializable {
	/**
	 * 记录总数
	 */
	private int totalNum;
	/**
	 * 起始索引
	 */
	private int startIndex;
	/**
	 * 结束索引
	 */
	private int endIndex;

	public int getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(int totalNum) {
		this.totalNum = totalNum;
	}

	public int getStartIndex() {
		return startIndex;
	}

	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}

	public int getEndIndex() {
		return endIndex;
	}

	public void setEndIndex(int endIndex) {
		this.endIndex = endIndex;
	}

	@Override
	public String toString() {
		return "Page [totalNum=" + totalNum + ", startIndex=" + startIndex + ", endIndex=" + endIndex + "]";
	}

}
