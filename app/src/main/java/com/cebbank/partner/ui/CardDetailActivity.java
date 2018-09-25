package com.cebbank.partner.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.cebbank.partner.BaseActivity;
import com.cebbank.partner.GlideApp;
import com.cebbank.partner.MyApplication;
import com.cebbank.partner.R;
import com.cebbank.partner.interfaces.HttpCallbackListener;
import com.cebbank.partner.utils.UrlPath;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import de.hdodenhof.circleimageview.CircleImageView;

import static com.cebbank.partner.utils.HttpUtil.sendOkHttpRequest;

public class CardDetailActivity extends BaseActivity {

//    private ImageView img;
    private WebView webview;
    private TextView tvApply;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_detail);
        initView();
        initData();
        setListener();
    }

    private void initView() {
        setTitle("信用卡介绍");
        setBackBtn();
//        img = findViewById(R.id.img);
        webview = findViewById(R.id.webView);
        tvApply = findViewById(R.id.tvApply);
    }

    private void initData() {
        cardDetail();
    }

    private void setListener() {

    }

    private void cardDetail() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", getIntent().getStringExtra("cardId"));
            jsonObject.put("authorId", getIntent().getStringExtra("authorId"));
            jsonObject.put("token", MyApplication.getToken());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        sendOkHttpRequest(this, UrlPath.CardDetail, jsonObject, null, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) throws JSONException {
                JSONObject jsonObject = new JSONObject(response);
                String code = jsonObject.optString("code");
                JSONObject jo = jsonObject.getJSONObject("data");
                String image = jo.optString("image");
                String intro = jo.optString("intro");
                final String url = jo.optString("url");
//                if (!TextUtils.isEmpty(image)) {
//                    GlideApp.with(CardDetailActivity.this)
//                            .load(image)
////                        .placeholder(R.mipmap.loading)
//                            .centerCrop()
//                            .into(img);
//                }
                loadWebView(intro);
                tvApply.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        WebviewActivity.actionStart(CardDetailActivity.this, url);
                    }
                });

            }

            @Override
            public void onFailure() {

            }
        });
    }

    private void loadWebView(String intro) {
        webview.setWebViewClient(new WebViewClient());
        // 辅助处理各种通知、请求事件，如果不设置WebViewClient，请求会跳转系统浏览器
        webview.setWebChromeClient(new WebChromeClient());
        //声明WebSettings子类
        WebSettings webSettings = webview.getSettings();
        //如果访问的页面中要与Javascript交互，则webview必须设置支持Javascript
        webSettings.setJavaScriptEnabled(true);
        //支持插件
        //        webSettings.setPluginsEnabled(true);
        //设置自适应屏幕，两者合用
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        //缩放操作
        webSettings.setSupportZoom(true); //支持缩放，默认为true。是下面那个的前提。
        webSettings.setBuiltInZoomControls(true); //设置内置的缩放控件。若为false，则该WebView不可缩放
        webSettings.setDisplayZoomControls(false); //隐藏原生的缩放控件
        //其他细节操作
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE); //关闭webview中缓存
        webSettings.setAppCacheEnabled(false); //关闭webview中缓存
//                webSettings.setAllowFileAccess(true); //设置可以访问文件
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        webSettings.setLoadsImagesAutomatically(true); //支持自动加载图片
        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webview.getSettings().setMixedContentMode(webview.getSettings().MIXED_CONTENT_ALWAYS_ALLOW);  //注意安卓5.0以上的权限
        }
//                int fontSize = (int) mContext.getResources().getDimension(R.dimen.samll);
//                webSettings.setTextZoom(fontSize);

        String cssLayout = "<style>*{padding: 0px;margin: 10px}#webview_content_wrapper{margin: 10px 10px 10px 10px;} p{color: #000000;line-height: 1.5em;font-size:45px;opacity: 1;} img {margin-top: 0px;margin-bottom: 60px;width: 100%;}</style>";
//                String htmlModify = IMAGE1.replaceAll("<br/>", "");
//        String htmlModify = item.getWebview_content();
        String htmlcontent = cssLayout + "<body><div id='webview_content_wrapper'>" + intro + "</div></body>";

        webview.loadDataWithBaseURL(null, htmlcontent, "text/html", "UTF-8", null);
    }

    public static void actionStart(Context context, String cardId, String authorId) {
        Intent intent = new Intent(context, CardDetailActivity.class);
        intent.putExtra("cardId", cardId);
        intent.putExtra("authorId", authorId);
        context.startActivity(intent);
    }
}
