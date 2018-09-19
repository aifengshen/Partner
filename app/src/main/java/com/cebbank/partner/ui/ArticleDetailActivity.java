package com.cebbank.partner.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.cebbank.partner.GlideApp;
import com.cebbank.partner.MyApplication;
import com.cebbank.partner.R;
import com.cebbank.partner.adapter.CardListAdapter;
import com.cebbank.partner.bean.CardInfoBean;
import com.cebbank.partner.interfaces.HttpCallbackListener;
import com.cebbank.partner.utils.DateTimeUtil;
import com.cebbank.partner.utils.LogUtils;
import com.cebbank.partner.utils.ToastUtils;
import com.cebbank.partner.utils.UrlPath;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.cebbank.partner.utils.HttpUtil.sendOkHttpRequest;

/**
 * @ClassName: Partner
 * @Description:
 * @Author Pjw
 * @date 2018/8/19 14:10
 */
public class ArticleDetailActivity extends CheckPermissionsActivity implements View.OnClickListener {


    private LinearLayout ll;
    private TextView tvPraise, tvComment;
    private RecyclerView recyclerView;
    private CardListAdapter mAdapter;
    private List<CardInfoBean> cardList;
    private TextSwitcher switcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_detail);
        initView();
        initData();
        setListener();
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
//        switcher = findViewById(R.id.switcher);
//        // 指定转换器的 ViewSwitcher.ViewFactory，ViewSwitcher.ViewFactory会为TextSwitcher提供转换的View
//        // 定义视图显示工厂，并设置
//        switcher.setFactory(new ViewSwitcher.ViewFactory() {
//
//            public View makeView() {
//                TextView tv = new TextView(ArticleDetailActivity.this);
//                tv.setTextSize(15);
//                tv.setBackgroundColor(getResources().getColor(R.color.text_color_orange));
//                tv.setTextColor(getResources().getColor(
//                        R.color.text_color_black));
//                Drawable leftDrawable = getResources().getDrawable(R.drawable.trumpet);
//                leftDrawable.setBounds(0, 0, leftDrawable.getMinimumWidth(), leftDrawable.getMinimumHeight());
//                tv.setCompoundDrawablePadding(20);
//                tv.setCompoundDrawables(leftDrawable, null, null, null);
//                tv.setPadding(30, 10, 0, 10);
//                tv.setGravity(Gravity.CENTER_VERTICAL);
//                tv.setText("风险提示风险提示风险提示风险提示风险提示风险提示风险提示风险提示风险提示风险提示风险提示风险提示风险提示风险提示风险提示风险提示风险提示风险提示");
//                return tv;
//            }
//        });
//        // 设置转换时的淡入和淡出动画效果（可选）
//        Animation in = AnimationUtils.loadAnimation(this, R.anim.slide_in_down);
//        Animation out = AnimationUtils.loadAnimation(this, R.anim.slide_out_top);
//        switcher.setInAnimation(in);
//        switcher.setOutAnimation(out);
//        Timer timer = new Timer();
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        switcher.setText("风险提示哦~");
//                    }
//                });
//            }
//        }, 2000, 2000);
    }

    private void initData() {
        cardList = new ArrayList<>();
        mAdapter = new CardListAdapter(cardList);
        mAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_BOTTOM);
        recyclerView.setAdapter(mAdapter);
        articleDetail();
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
                share();
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
            jsonObject.put("partnerId", getIntent().getStringExtra("articleId"));
            jsonObject.put("token", MyApplication.getToken());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        sendOkHttpRequest(this, UrlPath.Collection, jsonObject, null, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) throws JSONException {
                JSONObject jsonObject = new JSONObject(response);
                ToastUtils.showShortToast(jsonObject.optString("msg"));
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
        UMImage image = new UMImage(ArticleDetailActivity.this, R.drawable.aaa);//资源文件
        UMImage thumb = new UMImage(ArticleDetailActivity.this, R.drawable.bbb);
        image.setThumb(thumb);
        UMWeb web = new UMWeb("http://news.sina.com.cn/o/2018-08-19/doc-ihhxaafy5278620.shtml");
        web.setTitle("震惊，恭喜开发团队喜中2亿软妹币");//标题
        web.setThumb(thumb);  //缩略图
        web.setDescription("震惊，他竟然干这样的事...");//描述
        new ShareAction(ArticleDetailActivity.this).withText("嘛哩嘛哩哄").withMedia(web).setDisplayList(SHARE_MEDIA.SINA, SHARE_MEDIA.QQ, SHARE_MEDIA.WEIXIN)
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
