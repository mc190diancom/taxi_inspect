package com.miu360.taxi_check.util;

import java.util.ArrayList;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.CoordinateConverter;
import com.baidu.mapapi.utils.DistanceUtil;
import com.baidu.mapapi.utils.CoordinateConverter.CoordType;
import com.miu360.taxi_check.model.VehiclePositionModex1;

public class SortUtil {
	/**
	 * 通过距离升序排序
	 */
	public static void sortListNyDistance(double bLat, double bLon, ArrayList<VehiclePositionModex1> arrayList) {
		System.out.println("开始排序");
		int[] distances = new int[arrayList.size()];
		LatLng des1 = new LatLng(bLat, bLon);
		/**
		 * 计算距离赋值
		 */
		for (int i = 0; i < arrayList.size(); i++) {
			LatLng ll = new LatLng(arrayList.get(i).getGSPDATA().getLat(), arrayList.get(i).getGSPDATA().getLon());
			CoordinateConverter cover = new CoordinateConverter();
			cover.from(CoordType.GPS);
			cover.coord(ll);
			LatLng des = cover.convert();
			distances[i] = (int) DistanceUtil.getDistance(des1, des);
		}
		// 快速排序
		quick_sort(distances, distances.length, arrayList);
	}

	/**
	 * 快速排序法
	 */
	protected static void quick_sort(int[] arrays, int lenght, ArrayList<VehiclePositionModex1> list) {
		if (null == arrays || lenght < 1) {
			System.out.println("input error!");
			return;
		}
		_quick_sort(arrays, 0, lenght - 1, list);
	}

	/**
	 * 快速排序法
	 */
	protected static void _quick_sort(int[] arrays, int start, int end, ArrayList<VehiclePositionModex1> list) {
		if (start >= end) {
			return;
		}
		int i = start;
		int j = end;
		int value = arrays[i];
		boolean flag = true;
		while (i != j) {
			if (flag) {
				if (value > arrays[j]) {
					swap(arrays, i, j, list);
					flag = false;
				} else {
					j--;
				}
			} else {
				if (value < arrays[i]) {
					swap(arrays, i, j, list);
					flag = true;
				} else {
					i++;
				}
			}
		}
		_quick_sort(arrays, start, j - 1, list);
		_quick_sort(arrays, i + 1, end, list);

	}

	/**
	 * 交换
	 */
	protected static void swap(int[] arrays, int i, int j, ArrayList<VehiclePositionModex1> list) {
		int temp;
		temp = arrays[i];
		arrays[i] = arrays[j];
		arrays[j] = temp;
		VehiclePositionModex1 temps = list.get(i);
		list.set(i, list.get(j));
		list.set(j, temps);
	}
}
