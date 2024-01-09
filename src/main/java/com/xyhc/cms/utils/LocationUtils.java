package com.xyhc.cms.utils;

import org.springframework.stereotype.Component;

/**
 * @Desc 根据经纬度坐标计算两点的距离算法<br>
 * @Author yangzhenlong <br>
 * @Data 2018/5/9 18:38
 */
@Component
public class LocationUtils {

    private static final Double PI = Math.PI;
    private static final Double PK = 180 / PI;

    /**
     * @param lat_a a的经度
     * @param lng_a a的维度
     * @param lat_b b的经度
     * @param lng_b b的维度
     * @return 距离
     * @Description: 根据经纬度计算两点之间的距离
     * @author yangzhenlong
     */
    public static float getDistance(double lat_a, double lng_a, double lat_b, double lng_b) {
        double t1 =
                Math.cos(lat_a / PK) * Math.cos(lng_a / PK) * Math.cos(lat_b / PK) * Math.cos(lng_b / PK);
        double t2 =
                Math.cos(lat_a / PK) * Math.sin(lng_a / PK) * Math.cos(lat_b / PK) * Math.sin(lng_b / PK);
        double t3 = Math.sin(lat_a / PK) * Math.sin(lat_b / PK);

        double tt = Math.acos(t1 + t2 + t3);
        String res = (6366000 * tt) + "";
        return Float.parseFloat( res.substring(0, res.indexOf(".")));
    }
}