package com.cebbank.partner.ui;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;

import com.cebbank.partner.BaseActivity;
import com.cebbank.partner.MyApplication;
import com.cebbank.partner.R;
import com.cebbank.partner.interfaces.HttpCallbackListener;
import com.cebbank.partner.utils.SharedPreferencesKey;
import com.cebbank.partner.utils.ToastUtils;
import com.cebbank.partner.utils.UrlPath;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.cebbank.partner.utils.HttpUtil.sendOkHttpRequest;

/**
 * @ClassName: Omnipotent
 * @Description:
 * @Author Pjw
 * @date 2018/7/31 17:52
 */
public class WelcomeActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        initView();
        initData();
    }

    private void initView() {

    }

    private void initData() {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                if (!TextUtils.isEmpty(MyApplication.token)) {
                    autoLogin(MyApplication.getValue(SharedPreferencesKey.Token));
                } else {
                    startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
                    finish();
                }

            }
        }, 3000);
    }

    /**
     * 自动登录
     *
     * @param token token
     */
    private void autoLogin(String token) {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("token", "5503eb72fe764ac7843c810178763399");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        sendOkHttpRequest(this, UrlPath.AutoLogin, jsonObject, null,new HttpCallbackListener() {
            @Override
            public void onFinish(String response) throws JSONException {
                JSONObject jsonObject = new JSONObject(response);
                String code = jsonObject.optString("code");
                String obj = jsonObject.optString("obj");
                if (code.equals(MyApplication.SuccessCode)) {
                    Gson gson = new Gson();
//                    LoginBean loginBean = gson.fromJson(obj, LoginBean.class);
//                    MyApplication.saveValue(SharedPreferencesKey.Token, loginBean.getToken());
//                    MyApplication.token = loginBean.getToken();
//                    EquipmentListActivity.actionStart(WelcomeActivity.this);
//                    finish();
                } else if (code.equals(MyApplication.ErrorCodeTokenInvalid)) {
                    //token失效   跳转到登陆页
                    startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
                    finish();
                } else {
                    //参数异常、登录异常 吐司提示  跳转到登陆页
                    ToastUtils.showShortToast(jsonObject.optString("msg"));
                    startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
                    finish();
                }
            }

            @Override
            public void onFailure() {

            }
        });
    }
}
