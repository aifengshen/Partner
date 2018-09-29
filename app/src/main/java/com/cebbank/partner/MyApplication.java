package com.cebbank.partner;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.provider.Settings;

import com.cebbank.partner.utils.SharedPreferencesKey;
import com.cebbank.partner.utils.Utils;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.PlatformConfig;

public class MyApplication extends Application {

    public static String EquipmentCode;//设备唯一标识码
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;
    public static final String ErrorCodeTokenInvalid = "4001";//token无效
    public static final String ErrorCodeParameterException = "4000";//参数异常
    public static final String SuccessCode = "2000";//成功

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        init();
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
        PlatformConfig.setWeixin("wxc641c74287e9984f", "2688e2970aeb355c0e0beb780e34ebf6");
//        PlatformConfig.setSinaWeibo("2682311299", "5f923ea0b79f9ed23f0c49212fadce16", "http://sns.whalecloud.com");
//        PlatformConfig.setQQZone("1107733541", "mzhu8UMkD1x4m0At");

        Utils.init(this);
        sharedPreferences = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        saveEquipmentCode();
    }

    public static Context getContext() {
        return context;
    }

    public static String getToken(){
        String token = getValue(SharedPreferencesKey.Token);
        return token;
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
