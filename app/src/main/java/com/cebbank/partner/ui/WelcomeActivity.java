package com.cebbank.partner.ui;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;

import com.cebbank.partner.BaseActivity;
import com.cebbank.partner.MainActivity;
import com.cebbank.partner.MyApplication;
import com.cebbank.partner.R;
import com.cebbank.partner.interfaces.HttpCallbackListener;
import com.cebbank.partner.utils.LogUtils;
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
        hideToolbar(true);
    }

    private void initData() {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                LogUtils.e(MyApplication.getToken());
                if (!TextUtils.isEmpty(MyApplication.getToken())) {
                    autoLogin(MyApplication.getToken());
                } else {
                    startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
                    finish();
                }
            }
        }, 2000);
    }

    /**
     * 自动登录
     *
     * @param token token
     */
    private void autoLogin(String token) {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("token", token);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        sendOkHttpRequest(this, UrlPath.AuthLogin, jsonObject, null, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) throws JSONException {
                JSONObject jsonObject = new JSONObject(response);
                startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
                finish();
            }

            @Override
            public void onFailure() {

            }
        });
    }
}
