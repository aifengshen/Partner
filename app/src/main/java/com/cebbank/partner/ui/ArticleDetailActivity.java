package com.cebbank.partner.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cebbank.partner.BaseActivity;
import com.cebbank.partner.MyApplication;
import com.cebbank.partner.R;
import com.cebbank.partner.adapter.CardListAdapter;
import com.cebbank.partner.bean.CardInfoBean;
import com.cebbank.partner.interfaces.HttpCallbackListener;
import com.cebbank.partner.utils.ToastUtils;
import com.cebbank.partner.utils.UrlPath;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.umeng.socialize.UMShareAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.cebbank.partner.utils.HttpUtil.sendOkHttpRequest;

/**
 * @ClassName: Partner
 * @Description:
 * @Author Pjw
 * @date 2018/8/19 14:10
 */
public class ArticleDetailActivity extends BaseActivity implements View.OnClickListener {


    private LinearLayout ll;
    private TextView tvPraise, tvComment;
    private RecyclerView recyclerView;
    private CardListAdapter mAdapter;
    private List<CardInfoBean> cardList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_detail);
        initView();
        initData();
        setListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        articleDetail();
    }

    private void initView() {
        setTitle("阳光合伙人");
        setBackBtn();
        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setNestedScrollingEnabled(false);
        ll = findViewById(R.id.ll);
        tvPraise = findViewById(R.id.tvPraise);
        tvComment = findViewById(R.id.tvComment);
    }

    private void initData() {
        cardList = new ArrayList<>();
        mAdapter = new CardListAdapter(cardList);
        mAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_BOTTOM);
        recyclerView.setAdapter(mAdapter);
    }

    private void setListener() {
        findViewById(R.id.tvPraise).setOnClickListener(this);
        findViewById(R.id.tvComment).setOnClickListener(this);
        findViewById(R.id.tvShare).setOnClickListener(this);
        findViewById(R.id.tvCollect).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvPraise:
                /**
                 * 点赞
                 */
                praise();
                break;
            case R.id.tvComment:
                /**
                 * 文章评论页面
                 */
                ArticleCommentActivity.actionStart(this, cardList.get(0).getWebview_id());
                break;
            case R.id.tvShare:
                /**
                 * 分享
                 */
                transmit();
                break;
            case R.id.tvCollect:
                /**
                 * 收纳
                 */
                collectArtcle();
                break;
        }
    }

    private void articleDetail() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("articleId", getIntent().getStringExtra("articleId"));
            jsonObject.put("token", MyApplication.getToken());
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

                cardList.removeAll(cardList);
                CardInfoBean cardInfoBean = new CardInfoBean();
                cardInfoBean.setType("webview");
                cardInfoBean.setWebview_id(jodata.optString("id"));
                cardInfoBean.setWebview_title(jodata.optString("title"));
                cardInfoBean.setWebview_author(jodata.optString("author"));
                cardInfoBean.setWebview_createDate(jodata.optString("createDate"));
                cardInfoBean.setWebview_content(jodata.optString("content"));
                cardInfoBean.setWebview_authorId(jodata.optString("authorId"));
                cardInfoBean.setWebview_avatar(jodata.optString("avatar"));
                cardInfoBean.setWebview_attention(jodata.optString("attention"));
                cardInfoBean.setWebview_like(jodata.optString("like"));
                cardInfoBean.setWebview_liked(jodata.optString("liked"));
                cardInfoBean.setWebview_comment(jodata.optString("comment"));
                cardInfoBean.setWebview_forward(jodata.optString("forward"));
                cardInfoBean.setWebview_collection(jodata.optString("collection"));
                cardInfoBean.setWebview_collected(jodata.optString("collected"));
                cardList.add(cardInfoBean);
                for (int i = 0; i < cardInfoBeanList.size(); i++) {
                    cardInfoBeanList.get(i).setType("cardinfo");
                }
                cardList.addAll(cardInfoBeanList);
                mAdapter.notifyDataSetChanged();
                if (cardList.get(0).getWebview_liked().equals("true")) {
                    tvPraise.setSelected(true);
                } else {
                    tvPraise.setSelected(false);
                }
                tvPraise.setText(cardList.get(0).getWebview_like());
                tvComment.setText(cardList.get(0).getWebview_comment());
            }

            @Override
            public void onFailure() {

            }
        });
    }


    /**
     * 点赞
     */
    private void praise() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("articleId", getIntent().getStringExtra("articleId"));
            jsonObject.put("token", MyApplication.getToken());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        sendOkHttpRequest(this, UrlPath.Praise, jsonObject, null, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) throws JSONException {
                JSONObject jsonObject = new JSONObject(response);
                findViewById(R.id.tvPraise).setSelected(true);
            }

            @Override
            public void onFailure() {

            }
        });
    }

    /**
     * 收纳文章
     */
    private void collectArtcle() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("articleId", getIntent().getStringExtra("articleId"));
            jsonObject.put("token", MyApplication.getToken());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        sendOkHttpRequest(this, UrlPath.CollectsSend, jsonObject, null, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) throws JSONException {
                JSONObject jsonObject = new JSONObject(response);
                ToastUtils.showShortToast("收纳成功");
            }

            @Override
            public void onFailure() {

            }
        });
    }

    /**
     * 转发
     */
    private void transmit() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("articleId", getIntent().getStringExtra("articleId"));
            jsonObject.put("token", MyApplication.getToken());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        sendOkHttpRequest(this, UrlPath.Send, jsonObject, null, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) throws JSONException {
                JSONObject jsonObject = new JSONObject(response);
                ToastUtils.showShortToast("转发成功");
            }

            @Override
            public void onFailure() {

            }
        });
    }

    /**
     * 分享
     */
    private void share() {
//        UMImage image = new UMImage(ArticleDetailActivity.this, R.drawable.aaa);//资源文件
//        UMImage thumb = new UMImage(ArticleDetailActivity.this, R.drawable.bbb);
//        image.setThumb(thumb);
//        UMWeb web = new UMWeb("http://news.sina.com.cn/o/2018-08-19/doc-ihhxaafy5278620.shtml");
//        web.setTitle("震惊，恭喜开发团队喜中2亿软妹币");//标题
//        web.setThumb(thumb);  //缩略图
//        web.setDescription("震惊，他竟然干这样的事...");//描述
//        new ShareAction(ArticleDetailActivity.this).withText("嘛哩嘛哩哄").withMedia(web).setDisplayList(SHARE_MEDIA.SINA, SHARE_MEDIA.QQ, SHARE_MEDIA.WEIXIN)
//                .setCallback(new UMShareListener() {
//                    @Override
//                    public void onStart(SHARE_MEDIA share_media) {
//                        LogUtils.e("onStart");
//                    }
//
//                    @Override
//                    public void onResult(SHARE_MEDIA share_media) {
//                        LogUtils.e("onResult");
//                    }
//
//                    @Override
//                    public void onError(SHARE_MEDIA share_media, Throwable throwable) {
//                        LogUtils.e("onError");
//                    }
//
//                    @Override
//                    public void onCancel(SHARE_MEDIA share_media) {
//                        LogUtils.e("onCancel");
//                    }
//                }).open();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    public static void actionStart(Context context, String articleId) {
        Intent intent = new Intent(context, ArticleDetailActivity.class);
        intent.putExtra("articleId", articleId);
        context.startActivity(intent);
    }
}
