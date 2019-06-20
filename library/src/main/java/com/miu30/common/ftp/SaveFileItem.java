package com.miu30.common.ftp;

import android.util.Log;

import java.io.IOException;
import java.io.RandomAccessFile;


/**
 * <b>function:</b> 写入文件、保存文件
 */
public class SaveFileItem {
    private static final String TAG = "SaveFileItem";

    //存储文件
    private RandomAccessFile itemFile;
    private String name;

    /**
     * @param name 文件路径、名称
     * @param pos  写入点位置position
     * @throws IOException
     */
    public SaveFileItem(String name, long pos) throws IOException {
        this.name = name;
        itemFile = new RandomAccessFile(name, "rwd");
        //在指定的pos位置写入数据
        itemFile.seek(pos);
    }

    /**
     * <b>function:</b> 同步方法写入文件
     *
     * @param buff   缓冲数组
     * @param start  起始位置
     * @param length 长度
     * @return
     *
     * @author hoojo
     * @createDate 2011-9-26 下午12:21:22
     */
    public synchronized int write(byte[] buff, int start, int length) {
        int i = -1;
        try {
            itemFile.write(buff, start, length);
            i = length;
        } catch (IOException e) {
            Log.e(TAG, name + " ... " + e.toString());
        }
        return i;
    }

    public void close() throws IOException {
        if (itemFile != null) {
            itemFile.close();
        }
    }

    public String getFileName() {
        return this.name;
    }

    /**
     * 设置文件大小，在old 文件大于新文件时特别有用
     *
     * @param newLength 新的文件长度
     */
    public void setLength(long newLength) {
        try {
            if (newLength != itemFile.length()) {
                itemFile.setLength(newLength);
            }
        } catch (IOException e) {
            Log.e(TAG, name + " ... " + e.toString());
        }
    }
}