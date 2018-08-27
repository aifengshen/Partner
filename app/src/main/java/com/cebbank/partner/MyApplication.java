package com.cebbank.partner;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.provider.Settings;

import com.cebbank.partner.utils.SharedPreferencesKey;
import com.cebbank.partner.utils.Utils;


public class MyApplication extends Application {

    public static String token = "";
    public static String EquipmentCode;//设备唯一标识码
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;
    public static final String ErrorCodeLoginFailure = "4000";//登录失败
    public static final String ErrorCodeTokenInvalid = "4001";//token无效
    public static final String ErrorCodeParameterException = "4002";//参数异常
    public static final String SuccessCode = "2000";//成功



    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    private void init(){
        Utils.init(this);
        sharedPreferences = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        saveEquipmentCode();
        token = getValue(SharedPreferencesKey.Token);
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
