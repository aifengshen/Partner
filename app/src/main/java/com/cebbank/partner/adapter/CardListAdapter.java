package com.cebbank.partner.adapter;

import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.cebbank.partner.R;
import com.cebbank.partner.bean.CardInfoBean;
import com.cebbank.partner.ui.CardDetailActivity;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.List;

public class CardListAdapter extends BaseMultiItemQuickAdapter<CardInfoBean, BaseViewHolder> {

    private static final String IMAGE1 = "<h2>这是标题</h2><p>< img src= \\\" \\\"https://gw.alicdn.com/tps/TB1W_X6OXXXXXcZXVXXXXXXXXXX-400-400.png \\\"> \\\"></p ><p><strong>阿三快点回家啊圣诞狂欢</strong></p ><ol><li><p>123123</p ></li><li><p><strong>444</strong></p ></li><li><p><strong>55</strong></p ></li><li><p><strong>6</strong></p ></li></ol><p><strong><br/></strong></p ><p>打打杀杀打打啊打打杀杀打打啊打打杀杀打打啊打打杀杀打打啊打打杀杀打打啊打打杀杀打打啊打打杀杀打打啊打打杀杀打打啊打打杀杀打打啊打打杀杀打打啊打打杀杀打打啊打打杀杀打打啊打打杀杀打打啊打打杀杀打打啊打打杀杀打打啊打打杀杀打打啊打打杀杀打打啊打打杀杀打打啊打打杀杀打打啊打打杀杀打打啊打打杀杀打打啊打打杀杀打打啊</p ><br/><br/><br/><br/><h1>我是标题</h1><button>点击我啊啊啊啊 啊</button><div>打打杀杀打打啊打打杀杀打打啊打打杀杀打打啊打打杀杀打打啊打打杀杀打打啊打打杀杀打打啊打打杀杀打打啊打打杀杀打打啊打打杀杀打打啊打打杀杀打打啊打打杀杀打打啊打打杀杀打打啊打打杀杀打打啊打打杀杀打打啊打打杀杀打打啊打打杀杀打打啊打打杀杀打打啊打打杀杀打打啊打打杀杀打打啊打打杀杀打打啊打打杀杀打打啊打打杀杀打打啊</div><ol><li>111111</li><li>111111</li><li>111111</li><li>111111</li><li>111111</li><li>111111</li><li>111111</li><li>111111</li><li>111111</li><li>111111</li><li>111111</li><li>111111</li><li>111111</li><li>111111</li><li>111111</li><li>111111</li><li>111111</li><li>111111</li><li>111111</li><li>111111</li><embed src='http:\\/\\//\\/tb-video.bdstatic.com\\/ti\\/tieba-smallvideo-transcode\\/3612804_e50cb68f52adb3c4c3f6135c0edcc7b0_b0_3.mp4' a4' autostart=true/false loop=true/false width=100% height=\\\"500\\\"></embed></ol><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><br/><span>这是底部-----------------</span>";


    public CardListAdapter(List data) {
        super(data);
        addItemType(CardInfoBean.WebView, R.layout.activity_article_detail_webview_item);
        addItemType(CardInfoBean.Card, R.layout.activity_article_detail_cardlist_item);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final CardInfoBean item) {

        helper.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        switch (helper.getItemViewType()) {
            case CardInfoBean.WebView:
                ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                WebView webview = new WebView(mContext);
                webview.setLayoutParams(lp);
                LinearLayout llwebview = helper.getView(R.id.llwebview);
                llwebview.addView(webview);
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
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    webview.getSettings().setMixedContentMode(webview.getSettings().MIXED_CONTENT_ALWAYS_ALLOW);  //注意安卓5.0以上的权限
                }
//                int fontSize = (int) mContext.getResources().getDimension(R.dimen.samll);
//                webSettings.setTextZoom(fontSize);

//                String cssLayout = "<style>*{padding: 20px;margin: 20px}#webview_content_wrapper{margin: 20px 20px 20px 20px;} p{color: #333333;line-height: 2em;font-size:48px;opacity: 1;} img {margin-top: 13px;margin-bottom: 15px;width: 100%;}</style>";
//                String htmlModify = IMAGE1.replaceAll("<br/>", "");
//                String htmlcontent = cssLayout + "<body><div id='webview_content_wrapper'>" + htmlModify + "</div></body>";

                webview.loadDataWithBaseURL(null, getNewContent(item.getWebview_content()), "text/html", "UTF-8", null);
//                webview.loadUrl("http://news.sina.com.cn/o/2018-08-19/doc-ihhxaafy5278620.shtml");
                break;
            case CardInfoBean.Card:
                helper.setText(R.id.tvName, item.getName());
                helper.getView(R.id.tvBtn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CardDetailActivity.actionStart(mContext, item.getId());
                    }
                });
                break;

        }

    }

    private String getNewContent(String htmltext) {

        Document doc = Jsoup.parse(htmltext);
        Elements elements = doc.getElementsByTag("img");
        for (Element element : elements) {
            element.attr("width", "100%").attr("height", "auto");
        }
        return doc.toString();
    }


}
