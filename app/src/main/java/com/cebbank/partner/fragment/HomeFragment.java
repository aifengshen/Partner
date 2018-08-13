package com.cebbank.partner.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cebbank.partner.R;
import com.cebbank.partner.adapter.HomeFragmentAdapter;
import com.cebbank.partner.bean.HomeFragmentBean;
import com.chad.library.adapter.base.BaseQuickAdapter;


import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: Omnipotent
 * @Description:
 * @Author Pjw
 * @date 2018/7/31 15:25
 */
public class HomeFragment extends Fragment {

    private View view;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeLayout;
    private HomeFragmentAdapter mAdapter;
    private List<HomeFragmentBean> data;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_home, null);
        initView();
        initData();
        initAdapter();
        setClickListener();
        return view;
    }

    private void initView() {
        recyclerView = view.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        swipeLayout = view.findViewById(R.id.swipe_container);
        swipeLayout.setColorSchemeColors(Color.rgb(47, 223, 189));
    }

    private void initData() {
        data = new ArrayList<>();
        for (int i = 0; i < 60; i++) {
            HomeFragmentBean homeFragmentBean = new HomeFragmentBean();
            homeFragmentBean.setName(i+"哈哈哈");
            data.add(homeFragmentBean);
        }
    }

    private void initAdapter(){
        mAdapter = new HomeFragmentAdapter(R.layout.adapter_recyclerview_fragment_home, data);
        mAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_BOTTOM);
//        mAdapter.setPreLoadNumber(3);
//        mAdapter.setLoadMoreView(new CustomLoadMoreView());
        recyclerView.setAdapter(mAdapter);
    }

    private void setClickListener() {
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
                swipeLayout.setRefreshing(false);
            }
        });
    }

}
