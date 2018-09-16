package com.cebbank.partner.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.cebbank.partner.BaseActivity;
import com.cebbank.partner.R;
import com.cebbank.partner.adapter.MessageFragmentAdapter;
import com.cebbank.partner.adapter.MyApplyAdapter;
import com.cebbank.partner.bean.MessageFragmentBean;
import com.cebbank.partner.bean.MyApplyBean;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

public class MyApplyActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeLayout;
    private MyApplyAdapter mAdapter;
    private List<MyApplyBean> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_apply);
        initView();
        initData();
        setListener();
    }

    private void initView() {
        setTitle("我的申请");
        setBackBtn();
        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        swipeLayout = findViewById(R.id.swipe_container);
        swipeLayout.setColorSchemeColors(Color.rgb(47, 223, 189));

        data = new ArrayList<>();
        mAdapter = new MyApplyAdapter(R.layout.activity_my_apply_item, data);
        mAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_BOTTOM);
//        mAdapter.setPreLoadNumber(3);
//        mAdapter.setLoadMoreView(new CustomLoadMoreView());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(mAdapter);
    }

    private void initData() {

    }

    private void setListener() {

    }
}
