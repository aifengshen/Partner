package com.cebbank.partner.utils;

public class UrlPath {

    public static final String IP = "http://59.110.228.122:8080/cloudflow";

//    /**
//     * 获取版本信息接口,版本更新接口
//     */
//    public static final String GetAppVersion = "/mobilerisk-app/auth/getAppVersion";

    /**
     * 手动登录
     */
    public static final String Login = "/app/login/manually";
    /**
     * 自动登录
     */
    public static final String AutoLogin = "/app/login/auto";
    /**
     * 设备列表
     */
    public static final String EquipmentList = "/app/equipment/list";
    /**
     * 实时均值
     */
    public static final String RealTimeAverageDetail = "/app/realTimeAverage/detail";
    /**
     * 昨日均值
     */
    public static final String YdayAverage = "/app/ydayAverage/detail";
    /**
     * 剩余小时值
     */
    public static final String SurplusHour = "/app/surplusHour/detail";
    /**
     * 分钟值详情
     */
    public static final String MinValue = "/app/minValue/detail";
    /**
     * 小时值详情
     */
    public static final String HourValue = "/app/hourValue/detail";
}
