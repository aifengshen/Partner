package com.cebbank.partner.adapter;

import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.cebbank.partner.GlideApp;
import com.cebbank.partner.MyApplication;
import com.cebbank.partner.R;
import com.cebbank.partner.bean.CardInfoBean;
import com.cebbank.partner.interfaces.HttpCallbackListener;
import com.cebbank.partner.ui.ArticleDetailActivity;
import com.cebbank.partner.ui.CardDetailActivity;
import com.cebbank.partner.ui.PartnerActivity;
import com.cebbank.partner.utils.DateTimeUtil;
import com.cebbank.partner.utils.LogUtils;
import com.cebbank.partner.utils.ToastUtils;
import com.cebbank.partner.utils.UrlPath;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.cebbank.partner.utils.HttpUtil.sendOkHttpRequest;

public class CardListAdapter extends BaseMultiItemQuickAdapter<CardInfoBean, BaseViewHolder> {
    WebView webview;
    private List<CardInfoBean> data;
    public CardListAdapter(List data) {
        super(data);
        this.data = data;
        addItemType(CardInfoBean.WebView, R.layout.activity_article_detail_webview_item);
        addItemType(CardInfoBean.Card, R.layout.activity_article_detail_cardlist_item);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final CardInfoBean item) {

        switch (helper.getItemViewType()) {
            case CardInfoBean.WebView:
                if (webview == null) {
                    ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    webview = new WebView(mContext);
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

                    String cssLayout = "<style>*{padding: 0px;margin: 10px}#webview_content_wrapper{margin: 10px 10px 10px 10px;} p{color: #000000;line-height: 1.5em;font-size:45px;opacity: 1;} img {margin-top: 0px;margin-bottom: 60px;width: 100%;} video { max-width: 100% !important; width: 100% !important;}</style>";
//                String htmlModify = IMAGE1.replaceAll("<br/>", "");
                    String htmlModify = item.getWebview_content();
                    String htmlcontent = cssLayout + "<body><div id='webview_content_wrapper'>" + htmlModify + "</div></body>";

                    webview.loadDataWithBaseURL(null, htmlcontent, "text/html", "UTF-8", null);
                    helper.setText(R.id.tvTitle, item.getWebview_title());
                    helper.setText(R.id.tvName, item.getWebview_author());
                    helper.setText(R.id.tvDate, DateTimeUtil.stampToDate(item.getWebview_createDate()));
                    GlideApp.with(mContext)
                            .load(item.getWebview_avatar())
//                        .placeholder(R.mipmap.loading)
                            .centerCrop()
                            .into((CircleImageView) helper.getView(R.id.avatar));
                    helper.getView(R.id.tvIsAttent).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            concernPartner(item, helper);
                        }
                    });
                    helper.getView(R.id.avatar).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            PartnerActivity.actionStart(mContext, item.getWebview_authorId());
                        }
                    });
                    helper.getView(R.id.tvName).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            PartnerActivity.actionStart(mContext, item.getWebview_authorId());
                        }
                    });
                }
                if (item.getWebview_attention().equals("false")) {
                    helper.setText(R.id.tvIsAttent, "已关注");
                } else {
                    helper.setText(R.id.tvIsAttent, "关注");
                }
                break;
            case CardInfoBean.Card:
                helper.setText(R.id.tvName, item.getName());
                GlideApp.with(mContext)
                        .load(item.getImage())
//                        .placeholder(R.mipmap.loading)
                        .centerCrop()
                        .into((ImageView) helper.getView(R.id.img));
                helper.getView(R.id.tvBtn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CardDetailActivity.actionStart(mContext, item.getId(), data.get(0).webview_authorId);
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

    /**
     * 关注
     */
    private void concernPartner(CardInfoBean item, final BaseViewHolder helper) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("idolId", item.getWebview_authorId());
            jsonObject.put("token", MyApplication.getToken());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        sendOkHttpRequest((ArticleDetailActivity) mContext, UrlPath.Concern, jsonObject, null, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) throws JSONException {
                JSONObject jsonObject = new JSONObject(response);
                ToastUtils.showShortToast("关注成功");
                helper.setText(R.id.tvIsAttent, "已关注");
            }

            @Override
            public void onFailure() {

            }
        });
    }

}
