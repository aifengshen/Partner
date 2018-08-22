package com.cebbank.partner.ui;

import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cebbank.partner.BaseActivity;
import com.cebbank.partner.R;

/**
 * @ClassName: Partner
 * @Description:
 * @Author Pjw
 * @date 2018/8/19 14:10
 */
public class ContentActivity extends BaseActivity {

    private WebView webview;
    private LinearLayout ll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        initView();
        initData();
        setListener();
    }


    private void initView() {
        setTitle("XX信用卡阳光合伙人");
        setBackBtn();
        webview = findViewById(R.id.webview);
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
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); //关闭webview中缓存
        webSettings.setAllowFileAccess(true); //设置可以访问文件
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        webSettings.setLoadsImagesAutomatically(true); //支持自动加载图片
        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式
        ll = findViewById(R.id.ll);
    }


    private void initData() {
        webview.loadUrl("http://news.sina.com.cn/o/2018-08-19/doc-ihhxaafy5278620.shtml");
        for (int i=0;i<5;i++){
            TextView textView = new TextView(this);
            textView.setText("哈哈哈"+i);
            textView.setPadding(100,100,100,100);
            ll.addView(textView);

        }
    }


    private void setListener() {

    }
}
