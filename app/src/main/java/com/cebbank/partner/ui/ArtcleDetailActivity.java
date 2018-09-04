package com.cebbank.partner.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.webkit.WebView;
import android.widget.LinearLayout;

import com.cebbank.partner.R;
import com.cebbank.partner.adapter.CardListAdapter;
import com.cebbank.partner.bean.CardInfoBean;
import com.cebbank.partner.interfaces.HttpCallbackListener;
import com.cebbank.partner.utils.UrlPath;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.umeng.socialize.UMShareAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import static com.cebbank.partner.utils.HttpUtil.sendOkHttpRequest;

/**
 * @ClassName: Partner
 * @Description:
 * @Author Pjw
 * @date 2018/8/19 14:10
 */
public class ArtcleDetailActivity extends CheckPermissionsActivity {

    private WebView webview;
    private LinearLayout ll;
    private RecyclerView recyclerView;
    private CardListAdapter mAdapter;
    private List<CardInfoBean> cardList;
    private static final String IMAGE3 = "<p><font color=\\\"#ff0000\\\">富文本 this is a test</font></p>\n" + "<p><img src=\"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1534440083067&di=a04d46600bd5cfa3cf5b09f39de42f23&imgtype=0&src=http%3A%2F%2Fp16.qhimg.com%2Fbdr%2F__%2Fd%2F_open360%2Fbeauty0311%2F16.jpg\" alt=\"Image\"/></p>";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artcle_detail);
        initView();
        initData();
        setListener();
    }


    private void initView() {
        setTitle("XX信用卡阳光合伙人");
        setBackBtn();
        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setNestedScrollingEnabled(false);
        ll = findViewById(R.id.ll);
    }



    private void initData() {
        cardList = new ArrayList<>();
        mAdapter = new CardListAdapter(cardList);
        mAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_BOTTOM);
        recyclerView.setAdapter(mAdapter);
        articleDetail();
//        webview.loadDataWithBaseURL("http://avatar.csdn.net",getNewContent(IMAGE3),"text/html", "UTF-8", null);
//        webview.loadUrl("http://news.sina.com.cn/o/2018-08-19/doc-ihhxaafy5278620.shtml");
    }

    private void articleDetail() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("articleId", getIntent().getStringExtra("articleId"));
            jsonObject.put("token", "5503eb72fe764ac7843c810178763399");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        sendOkHttpRequest(this, UrlPath.Detail, jsonObject, null, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) throws JSONException {
                JSONObject jsonObject = new JSONObject(response);
                String code = jsonObject.optString("code");
                JSONObject jodata = jsonObject.getJSONObject("data");


                JSONArray jsonArray = jodata.getJSONArray("cardVoList");
                Gson gson = new Gson();
                List<CardInfoBean> cardInfoBeanList =
                        gson.fromJson(jsonArray.toString(), new TypeToken<List<CardInfoBean>>() {
                        }.getType());

                cardList.removeAll(cardInfoBeanList);
                CardInfoBean cardInfoBean = new CardInfoBean();
                cardInfoBean.setType("webview");
                cardList.add(cardInfoBean);
                for (int i=0;i<cardInfoBeanList.size();i++){
                    cardInfoBeanList.get(i).setType("cardinfo");
                }
                cardList.addAll(cardInfoBeanList);
                mAdapter.notifyDataSetChanged();


//                for (int i = 0; i < artcleDetailBean.getCardVoList().size(); i++) {
//                    TextView textView = new TextView(ArtcleDetailActivity.this);
//                    textView.setText("哈哈哈" + i);
//                    textView.setPadding(100, 100, 100, 100);
//                    ll.addView(textView);
//                    textView.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            UMImage image = new UMImage(ArtcleDetailActivity.this, R.drawable.aaa);//资源文件
//                            UMImage thumb = new UMImage(ArtcleDetailActivity.this, R.drawable.bbb);
//                            image.setThumb(thumb);
//                            UMWeb web = new UMWeb("http://news.sina.com.cn/o/2018-08-19/doc-ihhxaafy5278620.shtml");
//                            web.setTitle("震惊，恭喜开发团队喜中2亿软妹币");//标题
//                            web.setThumb(thumb);  //缩略图
//                            web.setDescription("震惊，他竟然干这样的事...");//描述
//                            new ShareAction(ArtcleDetailActivity.this).withText("嘛哩嘛哩哄").withMedia(web).setDisplayList(SHARE_MEDIA.SINA, SHARE_MEDIA.QQ, SHARE_MEDIA.WEIXIN)
//                                    .setCallback(new UMShareListener() {
//                                        @Override
//                                        public void onStart(SHARE_MEDIA share_media) {
//                                            LogUtils.e("onStart");
//                                        }
//
//                                        @Override
//                                        public void onResult(SHARE_MEDIA share_media) {
//                                            LogUtils.e("onResult");
//                                        }
//
//                                        @Override
//                                        public void onError(SHARE_MEDIA share_media, Throwable throwable) {
//                                            LogUtils.e("onError");
//                                        }
//
//                                        @Override
//                                        public void onCancel(SHARE_MEDIA share_media) {
//                                            LogUtils.e("onCancel");
//                                        }
//                                    }).open();
//                        }
//                    });
//                }

            }

            @Override
            public void onFailure() {

            }
        });
    }

    private String getNewContent(String htmltext) {

        Document doc = Jsoup.parse(htmltext);
        Elements elements = doc.getElementsByTag("img");
        for (Element element : elements) {
            element.attr("width", "100%").attr("height", "auto");
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

    public static void actionStart(Context context, String articleId) {
        Intent intent = new Intent(context, ArtcleDetailActivity.class);
        intent.putExtra("articleId", articleId);
        context.startActivity(intent);
    }
}
