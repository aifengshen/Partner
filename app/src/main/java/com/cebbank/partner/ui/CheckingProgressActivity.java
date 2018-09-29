package com.cebbank.partner.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.cebbank.partner.BaseActivity;
import com.cebbank.partner.MyApplication;
import com.cebbank.partner.R;
import com.cebbank.partner.adapter.CheckingProgressAdapter;
import com.cebbank.partner.bean.CheckingProgressBean;
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
 * 审核进度页面
 */
public class CheckingProgressActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private CheckingProgressAdapter mAdapter;
    private List<CheckingProgressBean> data;
    private static final int PAGE_SIZE = 10;
    private int mNextRequestPage = 1;
    private String type = "fodder", url = UrlPath.CheckingProgressFodder;
    private TabLayout tablayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checking_progress);
        initView();
        initData();
        setListener();
    }

    private void initView() {
        setTitle("审核进度");
        setBackBtn();
        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        mSwipeRefreshLayout = findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setColorSchemeColors(Color.rgb(47, 223, 189));

        data = new ArrayList<>();
        mAdapter = new CheckingProgressAdapter(data);
        mAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_BOTTOM);
//        mAdapter.setPreLoadNumber(3);
//        mAdapter.setLoadMoreView(new CustomLoadMoreView());
//        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(mAdapter);
        tablayout = findViewById(R.id.tablayout);
    }

    private void initData() {
        checkingprogress(true);
    }

    private void setListener() {

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                checkingprogress(true);
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                checkingprogress(false);
            }
        });

        tablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    url = UrlPath.CheckingProgressFodder;
                    type = "fodder";
                    checkingprogress(true);
                } else if (tab.getPosition() == 1) {
                    url = UrlPath.CheckingProgressWithdraw;
                    type = "withdraw";
                    checkingprogress(true);
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void checkingprogress(final boolean isRefresh) {
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
        sendOkHttpRequest(this, url, jo, mSwipeRefreshLayout, new HttpCallbackListener() {
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
                List<CheckingProgressBean> articleBeanList =
                        gson.fromJson(jsonArray.toString(), new TypeToken<List<CheckingProgressBean>>() {
                        }.getType());
                for (int i = 0; i < articleBeanList.size(); i++) {
                    articleBeanList.get(i).setType(type);
                }
                setData(isRefresh, articleBeanList);
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

    private void setData(boolean isRefresh, List<CheckingProgressBean> data) {
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
//            Toast.makeText(this, "没有更多数据了...", Toast.LENGTH_SHORT).show();
        } else {
            mAdapter.loadMoreComplete();
        }
    }
}
