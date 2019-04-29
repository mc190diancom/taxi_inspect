package com.miu360.taxi_check.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.DecimalFormat;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;

public class SDInfo {
	private static File mSdFile = Environment.getExternalStorageDirectory();
	private static StatFs mSdStatfs = new StatFs(mSdFile.getPath());
	private static File mInternalFile = Environment.getDataDirectory();
	private static StatFs mInternalStatfs = new StatFs(mInternalFile.getPath());

	/**
	 * 锟节诧拷锟芥储锟斤拷锟矫的达拷小
	 *
	 * @return
	 */
	public static long getAvailableInternalMemorySize() {
		long blockSize = mInternalStatfs.getBlockSize();
		long availableBlocks = mInternalStatfs.getAvailableBlocks();
		return availableBlocks * blockSize;
	}

	/**
	 * 锟斤拷锟斤拷诖锟斤拷欠锟斤拷锟斤拷愎伙拷拇锟叫�
	 *
	 * @param size
	 *            锟斤拷要锟侥达拷小锟斤拷锟斤拷位MB
	 * @return
	 */
	public static boolean hasEnoughAvailableSizeInternal(int size) {
		long value = getAvailableInternalMemorySize() / 1024 / 1024; // MB
		return value > size ? true : false;
	}

	/*
	 * //锟斤拷取block锟斤拷SIZE private static long mBlocSize = mStatfs.getBlockSize();
	 * //锟斤拷取BLOCK锟斤拷锟斤拷 private static long mTotalBlocks = mStatfs.getBlockCount();
	 * //锟斤拷锟叫碉拷Block锟斤拷锟斤拷锟斤拷 private static long mAvailaBlocks=
	 * mStatfs.getAvailableBlocks();
	 */

	/**
	 * 锟解部锟芥储锟斤拷锟矫的达拷小
	 *
	 * @return
	 */
	public static long getAvailaleSize() {
		long blockSize = mSdStatfs.getBlockSize();
		long blocks = mSdStatfs.getAvailableBlocks();
		return blockSize * blocks;
		// (availableBlocks * blockSize)/1024 KIB 锟斤拷位
		// (availableBlocks * blockSize)/1024 /1024 MIB锟斤拷位
	}

	/**
	 * 锟解部锟芥储锟杰达拷小
	 *
	 * @return
	 */
	public static long getAllSize() {
		return mSdStatfs.getBlockSize() * mSdStatfs.getBlockCount();
	}

	/**
	 * 锟斤拷锟絊dcard锟角凤拷锟斤拷锟姐够锟侥达拷小
	 *
	 * @param size
	 *            锟斤拷要锟侥达拷小锟斤拷锟斤拷位MB
	 * @return
	 */
	public static boolean hasEnoughAvailableSize(int size) {
		long value = getAvailaleSize() / 1024 / 1024; // MB
		return value > size ? true : false;
	}

	public static boolean hasSDCard() {
		return Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED);
	}

	/**
	 * 锟斤拷锟絊dcard锟角凤拷锟斤拷锟姐够锟侥达拷小
	 *
	 * @param needSize
	 *            锟斤拷要锟侥达拷小锟斤拷锟斤拷位MB
	 * @return
	 */
	public static boolean checkSdcard(int needSize) {
		if (SDInfo.hasSDCard()) {
			if (SDInfo.hasEnoughAvailableSize(needSize)) {
				return true;
			}
		}
		return false;
	}

	public static FileOutputStream saveInMemory(Context context, String fileName)
			throws FileNotFoundException {
		// important: note the permission of the file in
		// memory---Context.MODE_WORLD_READABLE
		return context.openFileOutput(fileName, Context.MODE_WORLD_READABLE);
	}

	private static DecimalFormat percentFormate = new DecimalFormat("0.00");

	public static String showSize(long size) {
		String result = null;
		if (size / 1024 / 1024 > 0) {
			result = percentFormate.format(((double) size) / 1024 / 1024)
					+ "Mb";
		} else if (size / 1024 > 0) {
			result = (size / 1024) + "Kb";
		} else {
			result = "0Kb";
		}
		return result;
	}
}
