package com.lubao.lubao.upgrade;

/**
 * @author shiner
 */
public class DownloadInfo {

	public static final int PREPARE = 0;
	public static final int DOWNLOADING = 1;
	public static final int PAUSE = 2;
	public static final int COMPLETE = 3;
	public static final int ERROR = 4;

	private long currentLength;
	private String filename;// 文件名
	private String title;// 显示的文件标题
	private String destPath;// 存储路径
	private double speed;// 下载速度
	private int state = PAUSE;// 状态
	private String url;// 下载地址
	private long fullLength;// 文件大小

	public long getCurrentLength() {
		return currentLength;
	}

	public void setCurrentLength(long currentLength) {
		this.currentLength = currentLength;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDestPath() {
		return destPath;
	}

	public void setDestPath(String destPath) {
		this.destPath = destPath;
	}

	public double getSpeed() {
		return speed;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public long getFullLength() {
		return fullLength;
	}

	public void setFullLength(long fullLength) {
		this.fullLength = fullLength;
	}

}
