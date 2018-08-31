package com.cebbank.partner.ui;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cebbank.partner.R;
import com.cebbank.partner.utils.LogUtils;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * @ClassName: Partner
 * @Description:
 * @Author Pjw
 * @date 2018/8/19 14:10
 */
public class ContentActivity extends CheckPermissionsActivity {

    private WebView webview;
    private LinearLayout ll;
    private static final String IMAGE3 ="<p><font color=\\\"#ff0000\\\">富文本 this is a test</font></p>\n" + "<p><img src=\"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1534440083067&di=a04d46600bd5cfa3cf5b09f39de42f23&imgtype=0&src=http%3A%2F%2Fp16.qhimg.com%2Fbdr%2F__%2Fd%2F_open360%2Fbeauty0311%2F16.jpg\" alt=\"Image\"/></p>" ;
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
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            webview.getSettings().setMixedContentMode(webview.getSettings().MIXED_CONTENT_ALWAYS_ALLOW);  //注意安卓5.0以上的权限
        }
        ll = findViewById(R.id.ll);
    }


    private void initData() {
//        webview.loadUrl("http://news.sina.com.cn/o/2018-08-19/doc-ihhxaafy5278620.shtml");

        webview.loadDataWithBaseURL("http://avatar.csdn.net",getNewContent(IMAGE3),"text/html", "UTF-8", null);


        for (int i = 0; i < 5; i++) {
            TextView textView = new TextView(this);
            textView.setText("哈哈哈" + i);
            textView.setPadding(100, 100, 100, 100);
            ll.addView(textView);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UMImage image = new UMImage(ContentActivity.this, R.drawable.aaa);//资源文件
                    UMImage thumb = new UMImage(ContentActivity.this, R.drawable.bbb);
                    image.setThumb(thumb);
                    UMWeb web = new UMWeb("http://news.sina.com.cn/o/2018-08-19/doc-ihhxaafy5278620.shtml");
                    web.setTitle("震惊，恭喜开发团队喜中2亿软妹币");//标题
                    web.setThumb(thumb);  //缩略图
                    web.setDescription("震惊，他竟然干这样的事...");//描述
                    new ShareAction(ContentActivity.this).withText("嘛哩嘛哩哄").withMedia(web).setDisplayList(SHARE_MEDIA.SINA, SHARE_MEDIA.QQ, SHARE_MEDIA.WEIXIN)
                            .setCallback(new UMShareListener() {
                                @Override
                                public void onStart(SHARE_MEDIA share_media) {
                                    LogUtils.e("onStart");
                                }

                                @Override
                                public void onResult(SHARE_MEDIA share_media) {
                                    LogUtils.e("onResult");
                                }

                                @Override
                                public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                                    LogUtils.e("onError");
                                }

                                @Override
                                public void onCancel(SHARE_MEDIA share_media) {
                                    LogUtils.e("onCancel");
                                }
                            }).open();
                }
            });
        }
    }

    private String getNewContent(String htmltext){

        Document doc=Jsoup.parse(htmltext);
        Elements elements=doc.getElementsByTag("img");
        for (Element element : elements) {
            element.attr("width","100%").attr("height","auto");
        }

        return doc.toString();
    }



    private void setListener() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }
}
