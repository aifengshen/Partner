package com.cebbank.partner;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.cebbank.partner.interfaces.LocateListener;
import com.cebbank.partner.utils.LogUtils;
import com.cebbank.partner.utils.SharedPreferencesKey;
import com.cebbank.partner.utils.Utils;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.PlatformConfig;

import java.text.SimpleDateFormat;
import java.util.Date;


public class MyApplication extends Application implements AMapLocationListener {

    public static String token = "5503eb72fe764ac7843c810178763399";
    public static String EquipmentCode;//设备唯一标识码
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;
    public static final String ErrorCodeLoginFailure = "4000";//登录失败
    public static final String ErrorCodeTokenInvalid = "4001";//token无效
    public static final String ErrorCodeParameterException = "4002";//参数异常
    public static final String SuccessCode = "2000";//成功
    //声明mlocationClient对象
    public AMapLocationClient mlocationClient;
    //声明mLocationOption对象
    public AMapLocationClientOption mLocationOption = null;
    public LocateListener locateListener;
    boolean isFirst = true;

    public void setLocateListener(LocateListener locateListener) {
        this.locateListener = locateListener;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        init();
        initMap();
    }

    private void init() {
        /**
         * 初始化common库
         * 参数1:上下文，不能为空
         * 参数2:设备类型，UMConfigure.DEVICE_TYPE_PHONE为手机、UMConfigure.DEVICE_TYPE_BOX为盒子，默认为手机
         * 参数3:Push推送业务的secret
         */
        UMConfigure.init(this, UMConfigure.DEVICE_TYPE_PHONE, "");
        /**
         * 设置组件化的Log开关
         * 参数: boolean 默认为false，如需查看LOG设置为true
         */
        UMConfigure.setLogEnabled(true);
        PlatformConfig.setWeixin("wx0f62ce05bc2d64a0", "57860109c618afbe64033dd7ba2862b5");
        PlatformConfig.setSinaWeibo("2682311299", "5f923ea0b79f9ed23f0c49212fadce16", "http://sns.whalecloud.com");
        PlatformConfig.setQQZone("1107733541", "mzhu8UMkD1x4m0At");

        Utils.init(this);
        sharedPreferences = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        saveEquipmentCode();
        token = getValue(SharedPreferencesKey.Token);
    }

    private void initMap() {
        mlocationClient = new AMapLocationClient(this);
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        //设置定位监听
        mlocationClient.setLocationListener(this);
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(2000);
        //设置定位参数
        mlocationClient.setLocationOption(mLocationOption);
        // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
        // 注意设置合适的定位时间的间隔（最小间隔支持为1000ms），并且在合适时间调用stopLocation()方法来取消定位请求
        // 在定位结束后，在合适的生命周期调用onDestroy()方法
        // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
        //启动定位
        mlocationClient.startLocation();
    }

    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (amapLocation != null) {
            if (amapLocation.getErrorCode() == 0) {

                //定位成功回调信息，设置相关消息
                amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
                amapLocation.getLatitude();//获取纬度
                amapLocation.getLongitude();//获取经度
                amapLocation.getAccuracy();//获取精度信息
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date(amapLocation.getTime());
                df.format(date);//定位时间
                if (isFirst){
                    locateListener.OnLocate(String.valueOf(amapLocation.getLatitude()),
                            String.valueOf(amapLocation.getLongitude()), amapLocation.getCity(),amapLocation.getAdCode());
                }
                isFirst = false;
            } else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                Log.e("AmapError", "location Error, ErrCode:"
                        + amapLocation.getErrorCode() + ", errInfo:"
                        + amapLocation.getErrorInfo());
            }
        }
    }


    /**
     * 获取设备唯一标识码并保存到本地
     */
    private void saveEquipmentCode() {
        String androidID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        String equipmentCode = androidID + Build.SERIAL;
        saveValue(SharedPreferencesKey.EquipmentCode, equipmentCode);
        EquipmentCode = equipmentCode;
    }

    /**
     * SharedPreferences取值
     *
     * @param key key值
     * @return value值
     */
    public static String getValue(String key) {
        String value = sharedPreferences.getString(key, "");
        return value;
    }

    /**
     * SharedPreferences保存
     *
     * @param key   key值
     * @param value value值
     */
    public static void saveValue(String key, String value) {
        //通过editor对象写入数据
        editor.putString(key, value);
        //提交数据存入到xml文件中
        editor.commit();
    }
}
