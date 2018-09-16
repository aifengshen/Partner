package com.cebbank.partner.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.cebbank.partner.BaseActivity;
import com.cebbank.partner.MyApplication;
import com.cebbank.partner.R;
import com.cebbank.partner.adapter.MyPraiseAdapter;
import com.cebbank.partner.bean.MyPraiseBean;
import com.cebbank.partner.interfaces.HttpCallbackListener;
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

public class MyPraiseActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private static final int PAGE_SIZE = 10;
    private int mNextRequestPage = 1;
    private MyPraiseAdapter mAdapter;
    private List<MyPraiseBean> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_praise);
        initView();
        initData();
        setClickListener();
    }
    private void initView() {
        setTitle("获赞");
        setBackBtn();
        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        mSwipeRefreshLayout = findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setColorSchemeColors(Color.rgb(47, 223, 189));

        data = new ArrayList<>();
        mAdapter = new MyPraiseAdapter(R.layout.activity_my_praise_item, data);
        mAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_BOTTOM);
//        mAdapter.setPreLoadNumber(3);
//        mAdapter.setLoadMoreView(new CustomLoadMoreView());
//        recyclerView.addItemDecoration(new DividerItemDecoration(MyPraiseActivity.this,DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(mAdapter);
    }

    private void initData() {
        myPraise(true);
    }

    private void myPraise(final boolean isRefresh) {
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
            jo.put("token", MyApplication.getToken());
            jo.put("page", jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        sendOkHttpRequest(this, UrlPath.Like, jo, mSwipeRefreshLayout, new HttpCallbackListener() {
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
                List<MyPraiseBean> myPraiseBeanList =
                        gson.fromJson(jsonArray.toString(), new TypeToken<List<MyPraiseBean>>() {
                        }.getType());
                setData(isRefresh, myPraiseBeanList);
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

    private void setData(boolean isRefresh, List<MyPraiseBean> data) {
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

    private void setClickListener() {

    }

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, MyPraiseActivity.class);
        context.startActivity(intent);
    }
}
