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
import com.cebbank.partner.adapter.MessageFragmentAdapter;
import com.cebbank.partner.bean.MessageFragmentBean;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: Omnipotent
 * @Description:
 * @Author Pjw
 * @date 2018/7/31 15:27
 */
public class MessageFragment extends Fragment {

    private View view;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeLayout;
    private MessageFragmentAdapter mAdapter;
    private List<MessageFragmentBean> data;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_message, container, false);
        initView();
        initData();
        setClickListener();
        return view;
    }

    private void initView() {
        recyclerView = view.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        swipeLayout = view.findViewById(R.id.swipe_container);
        swipeLayout.setColorSchemeColors(Color.rgb(47, 223, 189));

        data = new ArrayList<>();
        mAdapter = new MessageFragmentAdapter(R.layout.fragment_message_item, data);
        mAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_BOTTOM);
//        mAdapter.setPreLoadNumber(3);
//        mAdapter.setLoadMoreView(new CustomLoadMoreView());
        recyclerView.setAdapter(mAdapter);
    }

    private void initData() {
        for (int i = 1; i < 20; i++) {
            MessageFragmentBean messageFragmentBean = new MessageFragmentBean();
            messageFragmentBean.setTitle(i + "哈哈哈");
            data.add(messageFragmentBean);
        }
        mAdapter.notifyDataSetChanged();
    }

    private void setClickListener() {

    }
}
