package com.miu30.common.util;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.CoordinateConverter;
import com.miu30.common.config.MsgConfig;

import java.math.BigDecimal;

public class BaiduMapGPSUtil {

    public static String getDistance(double lat,double lon){
        LatLng ll = new LatLng(lat, lon);
        CoordinateConverter cover = new CoordinateConverter();
        cover.from(CoordinateConverter.CoordType.GPS);
        cover.coord(ll);
//			des是获取到的预警车辆位置坐标
        LatLng des = cover.convert();
//			des1是执法人员定位的坐标
        LatLng des1 = null;
        if(0 != MsgConfig.select_lat){
            des1 = new LatLng(MsgConfig.select_lat, MsgConfig.select_lng);
        }else if(0 != MsgConfig.lat){
            des1 = new LatLng(MsgConfig.select_lat, MsgConfig.select_lng);
        }else {
            return "-";
        }
        double distance = com.baidu.mapapi.utils.DistanceUtil.getDistance(des1, des);
        return String.format("%.2f", distance);
    }

    /**
     * 百度坐标转GPS坐标
     *
     * @param sourceLatLng
     * @return
     */
    public static LatLng convertBaiduToGPS(LatLng sourceLatLng) {
        // 将GPS设备采集的原始GPS坐标转换成百度坐标
        CoordinateConverter converter = new CoordinateConverter();
        converter.from(CoordinateConverter.CoordType.GPS);
        // sourceLatLng待转换坐标
        converter.coord(sourceLatLng);
        LatLng desLatLng = converter.convert();
        double latitude = 2 * sourceLatLng.latitude - desLatLng.latitude;
        double longitude = 2 * sourceLatLng.longitude - desLatLng.longitude;
        BigDecimal bdLatitude = new BigDecimal(latitude);
        bdLatitude = bdLatitude.setScale(6, BigDecimal.ROUND_HALF_UP);
        BigDecimal bdLongitude = new BigDecimal(longitude);
        bdLongitude = bdLongitude.setScale(6, BigDecimal.ROUND_HALF_UP);
        return new LatLng(bdLatitude.doubleValue(), bdLongitude.doubleValue());
    }

    /**
     * GPS转百度坐标
     *
     * @param sourceLatLng
     * @return
     */
    public static LatLng gpsToConvertBaidu(LatLng sourceLatLng) {
        // 将GPS设备采集的原始GPS坐标转换成百度坐标
        CoordinateConverter converter  = new CoordinateConverter();
        converter.from(CoordinateConverter.CoordType.GPS);
        //sourceLatLng待转换坐标
        converter.coord(sourceLatLng);
        LatLng desLatLng = converter.convert();
        return desLatLng;
    }
}
