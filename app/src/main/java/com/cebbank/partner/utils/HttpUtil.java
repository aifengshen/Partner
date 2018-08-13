package com.cebbank.partner.utils;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;


import com.cebbank.partner.MyApplication;
import com.cebbank.partner.interfaces.HttpCallbackListener;
import com.cebbank.partner.ui.LoginActivity;
import com.cebbank.partner.view.CustomDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * @ClassName: PJW
 * @Description:
 * @Author Pjw
 * @date 2018/6/26 17:58
 */
public class HttpUtil {

    /**
     * 将返回的数据封装后返给activity
     *
     * @param address              接口地址
     * @param paramsEntry          接口参数
     * @param httpCallbackListener 接口回调
     */
    public static void sendOkHttpRequest(final Activity mActivity, final String address,
                                         final Map<String, String> paramsEntry,
                                         final SwipeRefreshLayout swipeLayout,
                                         final HttpCallbackListener httpCallbackListener) {
        final CustomDialog customDialog = new CustomDialog(mActivity, false, null);
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (NetworkType.getNetworkType(mActivity) != 0) {
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            customDialog.show();
                        }
                    });

                    sendOkHttpRequestDetail(address, paramsEntry, new Callback() {
                        @Override
                        public void onFailure(Call call, final IOException e) {
                            mActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    LogUtils.e("eeee:", e.toString() + "");
                                    ToastUtils.showShortToast("网络超时");
                                    customDialog.dismiss();
                                    if (httpCallbackListener != null) {
                                        httpCallbackListener.onError(e);
                                    }
                                }
                            });

                        }

                        @Override
                        public void onResponse(Call call, final Response response) throws IOException {

                            final String responseData = response.body().string();
                            mActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        JSONObject jsonObject = new JSONObject(responseData);
                                        LogUtils.e("返回的数据:", responseData);
                                        String code = jsonObject.optString("code");
                                        if (code.equals(MyApplication.ErrorCodeTokenInvalid)) {
                                            Intent intent = new Intent(mActivity, LoginActivity.class);
                                            mActivity.startActivity(intent);
                                            mActivity.finish();
                                        } else {
                                            if (httpCallbackListener != null) {
                                                httpCallbackListener.onFinish(responseData);
                                            }
                                        }
                                        customDialog.dismiss();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            });

                        }

                    });
                } else {
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (swipeLayout != null) {
                                swipeLayout.setRefreshing(false);
                            }
                        }
                    });
                }
            }
        }).start();

    }

    /**
     * OkHttp访问接口
     *
     * @param address     接口地址
     * @param paramsEntry 接口参数
     * @param callback    接口回调
     */

    public static void sendOkHttpRequestDetail(String address, final Map<String, String> paramsEntry, okhttp3.Callback callback) {
        LogUtils.e("当前页面：", ActivityCollector.getCurrentActivity().getClass().getSimpleName() + "");
        LogUtils.e("接口路径：", UrlPath.IP + address);
        LogUtils.e("接口参数：", paramsEntry.toString());
        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(15, TimeUnit.SECONDS).readTimeout(15, TimeUnit.SECONDS).
                writeTimeout(15, TimeUnit.SECONDS).build();


        //循环form表单，将表单内容添加到form builder中
        FormBody.Builder builder = new FormBody.Builder();
        for (Map.Entry<String, String> entry : paramsEntry.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            builder.add(key, value);
        }

        RequestBody requestBody = builder.build();
        Request request = new Request.Builder()
                .url(UrlPath.IP + address)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }
}
