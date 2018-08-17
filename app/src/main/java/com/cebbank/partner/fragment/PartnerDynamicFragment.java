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
import android.widget.TextView;

import com.cebbank.partner.R;
import com.cebbank.partner.adapter.PartnerDynamicAdapter;
import com.cebbank.partner.bean.PartnerDynamicBean;
import com.cebbank.partner.utils.LogUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

public class PartnerDynamicFragment extends Fragment {


    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeLayout;
    private PartnerDynamicAdapter mAdapter;
    private List<PartnerDynamicBean> data;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_partner_dynamic, container, false);
        initView(rootView);
        initData();
        setListener();
        return rootView;
    }

    private void initView(View view){
        LogUtils.e("111111111111111111");
        recyclerView = view.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        swipeLayout = view.findViewById(R.id.swipe_container);
        swipeLayout.setColorSchemeColors(Color.rgb(47, 223, 189));
        data = new ArrayList<>();
        mAdapter = new PartnerDynamicAdapter(R.layout.fragment_partner_dynamic_item,data);
        mAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_BOTTOM);
//        mAdapter.setPreLoadNumber(3);
//        mAdapter.setLoadMoreView(new CustomLoadMoreView());
        recyclerView.setAdapter(mAdapter);
    }
    private void initData(){
        for (int i = 0; i < 10; i++) {
            PartnerDynamicBean partnerDynamicBean = new PartnerDynamicBean();
            partnerDynamicBean.setTitle(i + "啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊");
            data.add(partnerDynamicBean);
        }
        mAdapter.notifyDataSetChanged();
    }
    private void setListener(){
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
                swipeLayout.setRefreshing(false);
            }
        });
    }
}
