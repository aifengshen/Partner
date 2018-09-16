package com.cebbank.partner.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.cebbank.partner.BaseActivity;
import com.cebbank.partner.MyApplication;
import com.cebbank.partner.R;
import com.cebbank.partner.adapter.ArticleCommentAdapter;
import com.cebbank.partner.bean.CommentBean;
import com.cebbank.partner.bean.MyCommentBean;
import com.cebbank.partner.interfaces.HttpCallbackListener;
import com.cebbank.partner.utils.ToastUtils;
import com.cebbank.partner.utils.UrlPath;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.cebbank.partner.utils.HttpUtil.sendOkHttpRequest;


/**
 * 文章评论页面
 */
public class ArticleCommentActivity extends BaseActivity implements View.OnClickListener {

    private RecyclerView recyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ArticleCommentAdapter mAdapter;
    private List<CommentBean> data;
    private static final int PAGE_SIZE = 10;
    private int mNextRequestPage = 1;
    private TextView ectvComment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_comment);
        initView();
        initData();
        setListener();
    }

    private void initView() {
        setTitle("评论");
        setBackBtn();
        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        mSwipeRefreshLayout = findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setColorSchemeColors(Color.rgb(47, 223, 189));
        data = new ArrayList<>();
        mAdapter = new ArticleCommentAdapter(data);
        mAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_BOTTOM);
//        mAdapter.setPreLoadNumber(3);
//        mAdapter.setLoadMoreView(new CustomLoadMoreView());
        recyclerView.setAdapter(mAdapter);
        ectvComment = findViewById(R.id.ectvComment);

    }

    private void initData() {
        requestComments(true);
    }

    private void setListener() {
        findViewById(R.id.tvSend).setOnClickListener(this);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestComments(true);
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                requestComments(false);
            }
        });
    }

    private void requestComments(final boolean isRefresh) {
        if (isRefresh) {
            mNextRequestPage = 1;
            mAdapter.setEnableLoadMore(false);//这里的作用是防止下拉刷新的时候还可以上拉加载
        }
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("size", PAGE_SIZE);
            jsonObject.put("current", mNextRequestPage);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONObject jo = new JSONObject();
        try {
            jo.put("articleId", getIntent().getStringExtra("articleId"));
            jo.put("token", MyApplication.getToken());
            jo.put("page", jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        sendOkHttpRequest(this, UrlPath.CommentList, jo, mSwipeRefreshLayout, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) throws JSONException {
                JSONObject jsonObject = new JSONObject(response);
                String code = jsonObject.optString("code");
                JSONObject jodata = jsonObject.getJSONObject("data");
                if (isRefresh) {
                    mAdapter.setEnableLoadMore(true);
                    mSwipeRefreshLayout.setRefreshing(false);
                }
                JSONArray jsonArray = jodata.getJSONArray("records");
                Gson gson = new Gson();
                List<CommentBean> commentBeanList =
                        gson.fromJson(jsonArray.toString(), new TypeToken<List<CommentBean>>() {
                        }.getType());
                List<CommentBean> data = new ArrayList<>();
                for (int i = 0; i < commentBeanList.size(); i++) {
                    CommentBean commentBean = commentBeanList.get(i);
                    if (TextUtils.isEmpty(commentBean.getReply())) {
                        commentBean.setType("Comment");
                        data.add(commentBean);
                    } else {
                        commentBean.setType("Comment");
                        data.add(commentBean);
                        CommentBean commentBean1 = null;
                        try {
                            commentBean1 = (CommentBean) commentBean.clone();
                        } catch (CloneNotSupportedException e) {
                            e.printStackTrace();
                        }
                        commentBean1.setType("Reply");
                        data.add(commentBean1);
                    }

                }
//                for (int i=0;i<commentBeanList.size();i++){
//                    CommentBean commentBean = commentBeanList.get(i);
//                    if (TextUtils.isEmpty(commentBean.getReply())){
//                        commentBean.setType("Comment");
//                    }else{
//                        commentBean.setType("Reply");
//                    }
//                    data.add(commentBean);
//                }
                setData(isRefresh, data);
                mAdapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure() {
                if (isRefresh) {
                    mAdapter.setEnableLoadMore(true);
                    mSwipeRefreshLayout.setRefreshing(false);
                } else {
                    mAdapter.loadMoreFail();
                }
            }
        });
    }


    private void setData(boolean isRefresh, List<CommentBean> data) {
        mNextRequestPage++;
        final int size = data == null ? 0 : data.size();
        if (isRefresh) {
            mAdapter.setNewData(data);
        } else {
            if (size > 0) {
                mAdapter.addData(data);
            }
        }
        if (size < PAGE_SIZE) {
            //第一页如果不够一页，那么就不显示没有更多数据布局
            mAdapter.loadMoreEnd(isRefresh);
            Toast.makeText(this, "没有更多数据了...", Toast.LENGTH_SHORT).show();
        } else {
            mAdapter.loadMoreComplete();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvSend:
                /**
                 * 发表评论
                 */
                String comment = ectvComment.getText().toString();
                if (TextUtils.isEmpty(comment)) {
                    ToastUtils.showShortToast("评论内容不能为空");
                    return;
                }
                sendComment(comment);
                break;
        }
    }

    private void sendComment(String comment) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("content", comment);
            jsonObject.put("articleId", getIntent().getStringExtra("articleId"));
            jsonObject.put("token", MyApplication.getToken());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        sendOkHttpRequest(this, UrlPath.Publish, jsonObject, null, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) throws JSONException {
                JSONObject jsonObject = new JSONObject(response);
                String code = jsonObject.optString("code");
                ToastUtils.showShortToast("回复成功~");
                ectvComment.setText("");
                requestComments(true);

            }

            @Override
            public void onFailure() {

            }
        });
    }

    public static void actionStart(Context context, String articleId) {
        Intent intent = new Intent(context, ArticleCommentActivity.class);
        intent.putExtra("articleId", articleId);
        context.startActivity(intent);
    }


}
