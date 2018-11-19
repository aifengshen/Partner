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

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
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
     *                             //     * @param paramsEntry          接口参数
     * @param httpCallbackListener 接口回调
     */
    public static void sendOkHttpRequest(final Activity mActivity, final String address, final JSONObject jo,
                                         final SwipeRefreshLayout swipeLayout,
                                         final HttpCallbackListener httpCallbackListener) {
//        final CustomDialog customDialog = new CustomDialog(mActivity, true, null);
        final CustomDialog customDialog = new CustomDialog(mActivity);
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

                    sendOkHttpRequestDetail(address, jo, new Callback() {
                        @Override
                        public void onFailure(Call call, final IOException e) {
                            mActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    LogUtils.e("onFailure:", e.toString() + "");
                                    ToastUtils.showShortToast("连接服务器超时");
                                    customDialog.dismiss();
                                    if (httpCallbackListener != null) {
                                        httpCallbackListener.onFailure();
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
                                        LogUtils.e("返回的数据" + address + ":", responseData);
                                        String code = jsonObject.optString("code");
                                        String msg = jsonObject.optString("msg");
                                        customDialog.dismiss();
                                        if (code.equals(MyApplication.ErrorCodeTokenInvalid)) {
                                            //token失效——>跳转到登陆页面
                                            Intent intent = new Intent(mActivity, LoginActivity.class);
                                            mActivity.startActivity(intent);
                                            mActivity.finish();
                                        } else if (code.equals(MyApplication.ErrorCodeParameterException)) {
                                            //参数异常——>吐司、UI刷新
                                            ToastUtils.showShortToast(msg);
                                            if (httpCallbackListener != null) {
                                                httpCallbackListener.onFailure();
                                            }
                                        } else if (code.equals(MyApplication.SuccessCode)) {
                                            //正常——>返回数据
                                            if (httpCallbackListener != null) {
                                                httpCallbackListener.onFinish(responseData);
                                            }
                                        }

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
     * @param address  接口地址
     * @param jo       接口参数
     * @param callback 接口回调
     */

    public static void sendOkHttpRequestDetail(String address, JSONObject jo, okhttp3.Callback callback) {
        LogUtils.e("接口路径：", UrlPath.IP + address);
        LogUtils.e("接口参数：", jo.toString().replaceAll("\\\\", ""));
        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(15, TimeUnit.SECONDS).readTimeout(15, TimeUnit.SECONDS).
                writeTimeout(15, TimeUnit.SECONDS).build();
        String json = jo.toString().replaceAll("\\\\", "");
        RequestBody body =
                RequestBody.create(MediaType.parse("application/json;charset=utf-8"), json);
        Request request = new Request.Builder()
                .post(body)
                .url(UrlPath.IP + address)
                .build();
        client.newCall(request).enqueue(callback);
    }


    public static void sendOkHttpRequestUpLoad(final Activity mActivity, final String address,
                                               final SwipeRefreshLayout swipeLayout, final String path,
                                               final HttpCallbackListener httpCallbackListener) {
        final CustomDialog customDialog = new CustomDialog(mActivity, true, null);
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

                    sendOkHttpRequestUpLoadDetail(address, path, new Callback() {
                        @Override
                        public void onFailure(Call call, final IOException e) {
                            mActivity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    LogUtils.e("onFailure:", e.toString() + "");
                                    ToastUtils.showShortToast("连接服务器超时");
                                    customDialog.dismiss();
//                                    if (httpCallbackListener != null) {
//                                        httpCallbackListener.onFailure();
//                                    }
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
                                        String msg = jsonObject.optString("msg");
                                        if (code.equals(MyApplication.ErrorCodeTokenInvalid)) {
                                            //token失效——>跳转到登陆页面
                                            Intent intent = new Intent(mActivity, LoginActivity.class);
                                            mActivity.startActivity(intent);
                                            mActivity.finish();
                                        } else if (code.equals(MyApplication.ErrorCodeParameterException)) {
                                            //参数异常——>吐司、UI刷新
                                            ToastUtils.showShortToast(msg);
                                            if (httpCallbackListener != null) {
                                                httpCallbackListener.onFailure();
                                            }
                                        } else if (code.equals(MyApplication.SuccessCode)) {
                                            //正常——>返回数据
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
//                            if (swipeLayout != null) {
//                                swipeLayout.setRefreshing(false);
//                            }
                        }
                    });
                }
            }
        }).start();

    }

    public static void sendOkHttpRequestUpLoadDetail(String address,
                                                     String path, okhttp3.Callback callback) {
        LogUtils.e("接口路径：", UrlPath.IP + address);
        LogUtils.e("接口参数：", path + "");
        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(15, TimeUnit.SECONDS).readTimeout(15, TimeUnit.SECONDS).
                writeTimeout(15, TimeUnit.SECONDS).build();

        RequestBody fileBody = RequestBody.create(MediaType.parse("image/png"), new File(path));
        MultipartBody.Builder builder = new MultipartBody.Builder();
        RequestBody requestBody = builder.setType(MultipartBody.FORM)
                .addFormDataPart("file", (new File(path)).getName(), fileBody)
                .build();

        Request request = new Request.Builder()
                .url(UrlPath.IP + address)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }
}
