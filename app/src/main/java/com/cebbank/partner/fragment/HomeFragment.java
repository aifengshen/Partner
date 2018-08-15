package com.cebbank.partner.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.cebbank.partner.R;
import com.cebbank.partner.adapter.HomeFragmentAdapter;
import com.cebbank.partner.bean.HomeFragmentBean;
import com.cebbank.partner.utils.Utils;
import com.chad.library.adapter.base.BaseQuickAdapter;


import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: Omnipotent
 * @Description:
 * @Author Pjw
 * @date 2018/7/31 15:25
 */
public class HomeFragment extends Fragment implements View.OnClickListener {

    private View view;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeLayout;
    private HomeFragmentAdapter mAdapter;
    private List<HomeFragmentBean> data;
    private EditText edittextClientName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_home, container, false);

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
        edittextClientName = view.findViewById(R.id.edittextClientName);

//        edittextClientName.setFilters(new InputFilter[]{
//
//                new InputFilter() {
//                    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart,
//                                               int dend) {
//                        for (int i = start; i < end; i++) {
//                            if (!Character.isLetterOrDigit(source.charAt(i)) && !Character.toString(source.charAt(i)).equals("_") && !Character.toString(source.charAt(i)).equals("-")) {
//                                return "";
//                            }
//                        }
//                        return null;
//                    }
//                }
//        });
//        edittextClientName.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if (!edittextClientName.isFocusable()) {
//                    edittextClientName.setFocusable(true);
//                    edittextClientName.setFocusableInTouchMode(true);
//                    edittextClientName.requestFocus();
////                    //打开软键盘
////                    Utils.openInputKeyBoard(getActivity());
//
//
//                }
//            }
//        });
    }

    private void initData() {
        data = new ArrayList<>();
        for (int i = 0; i < 60; i++) {
            HomeFragmentBean homeFragmentBean = new HomeFragmentBean();
            homeFragmentBean.setName(i + "哈哈哈");
            data.add(homeFragmentBean);
        }
    }

    private void initAdapter() {
        mAdapter = new HomeFragmentAdapter(R.layout.adapter_recyclerview_fragment_home, data);
        mAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_BOTTOM);
//        mAdapter.setPreLoadNumber(3);
//        mAdapter.setLoadMoreView(new CustomLoadMoreView());

        recyclerView.setAdapter(mAdapter);

        View view = getLayoutInflater().inflate(R.layout.aaa, (ViewGroup) recyclerView.getParent(), false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "点击头布局", Toast.LENGTH_SHORT).show();
            }
        });
        mAdapter.addHeaderView(view);
    }


    private void setClickListener() {
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
                swipeLayout.setRefreshing(false);
            }
        });
//        view.findViewById(R.id.llbackground).setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                try {
//                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(
//                            Context.INPUT_METHOD_SERVICE);
//                    return imm.hideSoftInputFromWindow(
//                            getActivity().getCurrentFocus().getWindowToken(), 0);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    return false;
//                }
//            }
//        });

        view.findViewById(R.id.llbackground).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.closeInputKeyBoard(getActivity());
            }
        });
    }

    @Override
    public void onClick(View v) {

    }
}
