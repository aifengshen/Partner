package com.cebbank.partner.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.cebbank.partner.BaseActivity;
import com.cebbank.partner.R;
import com.cebbank.partner.adapter.MyCommentAdapter;
import com.cebbank.partner.bean.MyCommentBean;
import com.cebbank.partner.interfaces.HttpCallbackListener;
import com.cebbank.partner.interfaces.OnReplyListener;
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
 * 我的评论页面
 */
public class MyCommentActivity extends BaseActivity implements View.OnClickListener, OnReplyListener {

    private RecyclerView recyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private MyCommentAdapter mAdapter;
    private List<MyCommentBean> data;
    private static final int PAGE_SIZE = 10;
    private int mNextRequestPage = 1;
    private TextView ectvComment;
    private String commentId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_comment);
        initView();
        initData();
        setListener();
    }

    private void initView() {
        setTitle("我的评论");
        setBackBtn();
        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        mSwipeRefreshLayout = findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setColorSchemeColors(Color.rgb(47, 223, 189));
        data = new ArrayList<>();
        mAdapter = new MyCommentAdapter(data);
        mAdapter.setOnReplyListener(this);
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
            jo.put("token", "5503eb72fe764ac7843c81017e767301");//5503eb72fe764ac7843c81017e767301
            jo.put("page", jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        sendOkHttpRequest(this, UrlPath.MyComment, jo, mSwipeRefreshLayout, new HttpCallbackListener() {
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
                List<MyCommentBean> myCommentBeanList =
                        gson.fromJson(jsonArray.toString(), new TypeToken<List<MyCommentBean>>() {
                        }.getType());
                List<MyCommentBean> data = new ArrayList<>();
                for (int i = 0; i < myCommentBeanList.size(); i++) {
                    MyCommentBean myCommentBean = myCommentBeanList.get(i);
                    if (TextUtils.isEmpty(myCommentBean.getReply())) {
                        myCommentBean.setType("Comment");
                        data.add(myCommentBean);
                    } else {
                        myCommentBean.setType("Comment");
                        data.add(myCommentBean);
                        MyCommentBean myCommentBean1 = null;
                        try {
                            myCommentBean1 = (MyCommentBean) myCommentBean.clone();
                        } catch (CloneNotSupportedException e) {
                            e.printStackTrace();
                        }
                        myCommentBean1.setType("Reply");
                        data.add(myCommentBean1);
                    }

                }
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


    private void setData(boolean isRefresh, List<MyCommentBean> data) {
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
                if (TextUtils.isEmpty(commentId)){
                    ToastUtils.showShortToast("请点击要回复的评论");
                    return;
                }
                sendComment(comment,commentId);
                break;
        }
    }

    private void sendComment(String comment,String commentId) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("content", comment);
            jsonObject.put("commentId", commentId);
            jsonObject.put("token", "5503eb72fe764ac7843c81017e767301");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        sendOkHttpRequest(this, UrlPath.Reply, jsonObject, null, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) throws JSONException {
                JSONObject jsonObject = new JSONObject(response);
                ToastUtils.showShortToast("回复成功~");
                ectvComment.setText("");
                requestComments(true);

            }

            @Override
            public void onFailure() {

            }
        });
    }

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, MyCommentActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void reply(String comment_Id, String name) {
        ectvComment.setHint("回复 " + name + ":");
        commentId = comment_Id;
    }
}
