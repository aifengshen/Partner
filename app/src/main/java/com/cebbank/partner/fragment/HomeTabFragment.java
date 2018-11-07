package com.cebbank.partner.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.cebbank.partner.R;
import com.cebbank.partner.adapter.ArticleAdapter;
import com.cebbank.partner.bean.ArticleBean;
import com.cebbank.partner.interfaces.HttpCallbackListener;
import com.cebbank.partner.utils.UrlPath;
import com.cebbank.partner.view.CustomLoadMoreView;
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
 * @ClassName: Partner
 * @Description:
 * @Author Pjw
 * @date 2018/11/2 15:48
 */
public class HomeTabFragment extends Fragment {
    private View view;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ArticleAdapter mAdapter;
    private List<ArticleBean> data;

    private static final int PAGE_SIZE = 10;
    private int mNextRequestPage = 1;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_hometab, container, false);
        initView();
        initData();
        setListener();
        return view;
    }

    private void initView() {
        recyclerView = view.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        mSwipeRefreshLayout = view.findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setColorSchemeColors(Color.rgb(47, 223, 189));
        data = new ArrayList<>();
        mAdapter = new ArticleAdapter(data);
        mAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_BOTTOM);
        mAdapter.setPreLoadNumber(3);
        mAdapter.setLoadMoreView(new CustomLoadMoreView());
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(mAdapter);
//        View head_view = getLayoutInflater().inflate(R.layout.headerview, (ViewGroup) recyclerView.getParent(), false);
//
//        mAdapter.addHeaderView(head_view);

    }

    private void initData() {
        requestArticle(true, "");
    }

    private void requestArticle(final boolean isRefresh, String keyword) {
        Bundle bundle = getArguments();
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
            jo.put("tabId", bundle.getString("id"));
            jo.put("keyword", keyword);
            jo.put("page", jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        sendOkHttpRequest(getActivity(), UrlPath.Article, jo, mSwipeRefreshLayout, new HttpCallbackListener() {
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
                List<ArticleBean> articleBeanList =
                        gson.fromJson(jsonArray.toString(), new TypeToken<List<ArticleBean>>() {
                        }.getType());
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

    private void setData(boolean isRefresh, List<ArticleBean> data) {
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
//            Toast.makeText(getActivity(), "没有更多数据了...", Toast.LENGTH_SHORT).show();
        } else {
            mAdapter.loadMoreComplete();
        }
    }



    private void setListener() {

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                requestArticle(false, "");
            }
        });

    }




}
