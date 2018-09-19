package com.cebbank.partner.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.cebbank.partner.BaseActivity;
import com.cebbank.partner.GlideApp;
import com.cebbank.partner.MainActivity;
import com.cebbank.partner.MyApplication;
import com.cebbank.partner.R;
import com.cebbank.partner.interfaces.HttpCallbackListener;
import com.cebbank.partner.utils.LogUtils;
import com.cebbank.partner.utils.SharedPreferencesKey;
import com.cebbank.partner.utils.ToastUtils;
import com.cebbank.partner.utils.UrlPath;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareConfig;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import static com.cebbank.partner.utils.HttpUtil.sendOkHttpRequest;


public class LoginActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
    }

    private void initView() {
        hideToolbar(true);
    }

    public void getData(View view) {
        UMShareAPI mShareAPI = UMShareAPI.get(this);
        mShareAPI.getPlatformInfo(LoginActivity.this, SHARE_MEDIA.WEIXIN, new UMAuthListener() {
            /**
             * @desc 授权开始的回调
             * @param share_media 平台名称
             */
            @Override
            public void onStart(SHARE_MEDIA share_media) {

            }

            /**
             * @desc 授权成功的回调
             * @param share_media 平台名称
             * @param i 行为序号，开发者用不上
             * @param map 用户资料返回
             */
            @Override
            public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                String avatar = map.get("profile_image_url").replace(" ", "");
                login(map.get("screen_name"), map.get("unionid"), map.get("openid"), avatar);
            }

            /**
             * @desc 授权失败的回调
             * @param share_media 平台名称
             * @param i 行为序号，开发者用不上
             * @param throwable 错误原因
             */
            @Override
            public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {

            }

            /**
             * @desc 授权取消的回调
             * @param share_media 平台名称
             * @param i 行为序号，开发者用不上
             */
            @Override
            public void onCancel(SHARE_MEDIA share_media, int i) {

            }
        });
    }

    private void login(String wechatName, String unionId, String openId, String profile_image_url) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("wechatName", wechatName);
            jsonObject.put("unionId", unionId);
            jsonObject.put("openId", openId);
            jsonObject.put("avatar", profile_image_url);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        sendOkHttpRequest(this, UrlPath.WXLogin, jsonObject, null, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) throws JSONException {
                JSONObject jsonObject = new JSONObject(response);
                JSONObject data = jsonObject.getJSONObject("data");
                String token = data.optString("token");
                MyApplication.saveValue(SharedPreferencesKey.Token, token);
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            }

            @Override
            public void onFailure() {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

}
