package com.cebbank.partner.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.cebbank.partner.BaseActivity;
import com.cebbank.partner.MyApplication;
import com.cebbank.partner.R;
import com.cebbank.partner.adapter.MyCommentAdapter;
import com.cebbank.partner.adapter.RankingAdapter;
import com.cebbank.partner.bean.MyCommentBean;
import com.cebbank.partner.bean.RankingBean;
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

public class RankingActivity extends BaseActivity implements View.OnClickListener {

    private TextView tvInCome, tvUv;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RankingAdapter mAdapter;
    private List<RankingBean> data;
    private static final int PAGE_SIZE = 10;
    private int mNextRequestPage = 1;
    private String url = UrlPath.LeaderboardIncome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);
        initView();
        initData();
        setListener();
    }

    private void initView() {
        setTitle("排行榜");
        setBackBtn();
        tvInCome = findViewById(R.id.tvInCome);
        tvUv = findViewById(R.id.tvUv);
        tvInCome.setSelected(true);
        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        mSwipeRefreshLayout = findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setColorSchemeColors(Color.rgb(47, 223, 189));
        data = new ArrayList<>();
        mAdapter = new RankingAdapter(R.layout.activity_ranking_item, data);
        mAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_BOTTOM);
//        mAdapter.setPreLoadNumber(3);
//        mAdapter.setLoadMoreView(new CustomLoadMoreView());
        recyclerView.setAdapter(mAdapter);
    }

    private void initData() {
        requestData(true);
    }

    private void setListener() {
        tvInCome.setOnClickListener(this);
        tvUv.setOnClickListener(this);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestData(true);
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                requestData(false);
            }
        });
    }

    private void requestData(final boolean isRefresh) {
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
                List<RankingBean> rankingBeanList =
                        gson.fromJson(jsonArray.toString(), new TypeToken<List<RankingBean>>() {
                        }.getType());
                if (url.equals(UrlPath.LeaderboardIncome)) {
                    for (int i = 0; i < rankingBeanList.size(); i++) {
                        rankingBeanList.get(i).setType("0");
                    }
                } else {
                    for (int i = 0; i < rankingBeanList.size(); i++) {
                        rankingBeanList.get(i).setType("1");
                    }
                }
                setData(isRefresh, rankingBeanList);
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

    private void setData(boolean isRefresh, List<RankingBean> data) {
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvInCome:
                /**
                 * 收益排行榜
                 */
                tvInCome.setSelected(true);
                tvUv.setSelected(false);
                url = UrlPath.LeaderboardIncome;
                requestData(true);
                break;
            case R.id.tvUv:
                /**
                 * 阅读排行榜
                 */
                tvInCome.setSelected(false);
                tvUv.setSelected(true);
                url = UrlPath.LeaderboardUv;
                requestData(true);
                break;
        }
    }
}
