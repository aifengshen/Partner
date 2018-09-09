package com.cebbank.partner.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.cebbank.partner.BaseActivity;
import com.cebbank.partner.MyApplication;
import com.cebbank.partner.R;
import com.cebbank.partner.adapter.MyViewPagerAdapter;
import com.cebbank.partner.adapter.PersonalDataAdapter;
import com.cebbank.partner.bean.PersonalDataBean;
import com.cebbank.partner.fragment.AttentionPartnerFragment;
import com.cebbank.partner.fragment.HomeFragment;
import com.cebbank.partner.fragment.MessageFragment;
import com.cebbank.partner.fragment.MyAcceptFragment;
import com.cebbank.partner.fragment.PartnerDynamicFragment;
import com.cebbank.partner.interfaces.HttpCallbackListener;
import com.cebbank.partner.utils.SharedPreferencesKey;
import com.cebbank.partner.utils.UrlPath;
import com.cebbank.partner.view.CustomLoadMoreView;
import com.chad.library.adapter.base.BaseQuickAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.cebbank.partner.utils.HttpUtil.sendOkHttpRequest;

public class PartnerActivity extends BaseActivity {

    private MyViewPagerAdapter mMyViewPagerAdapter;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;

    private RecyclerView recyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private PersonalDataAdapter mAdapter;
    private List<PersonalDataBean> data;
    private static final int PAGE_SIZE = 10;
    private int mNextRequestPage = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partner);
        initView();
        initData();
        setListener();
    }

    private void initView() {
        setTitle("关注人");
        setBackBtn();
        mTabLayout = findViewById(R.id.tablayout);
        mTabLayout.addTab(mTabLayout.newTab().setText("视频"));
        mTabLayout.addTab(mTabLayout.newTab().setText("信用卡"));


        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        mSwipeRefreshLayout = findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setColorSchemeColors(Color.rgb(47, 223, 189));
        data = new ArrayList<>();
        mAdapter = new PersonalDataAdapter(R.layout.activity_personal_data_item, data);
        mAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_BOTTOM);
//        mAdapter.setPreLoadNumber(3);
        mAdapter.setLoadMoreView(new CustomLoadMoreView());
        recyclerView.setAdapter(mAdapter);
    }

    private void initData() {
        for (int i = 0; i < 10; i++) {
            PersonalDataBean partnerDynamicBean = new PersonalDataBean();
            partnerDynamicBean.setTitle(i + "素材结算");
            data.add(partnerDynamicBean);
        }
        mAdapter.notifyDataSetChanged();
    }

    private void setListener() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                request(true);
            }
        });
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                request(false);
            }
        });
    }

    private void request(final boolean isRefresh) {
        if (isRefresh) {
            mNextRequestPage = 1;
            mAdapter.setEnableLoadMore(false);//这里的作用是防止下拉刷新的时候还可以上拉加载
        }
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("username", "admin");
        paramsMap.put("password", "11");
        paramsMap.put("token", MyApplication.getValue(SharedPreferencesKey.Token));
        paramsMap.put("current", String.valueOf(mNextRequestPage));
        paramsMap.put("size", String.valueOf(PAGE_SIZE));

//        sendOkHttpRequest(this, UrlPath.Login, paramsMap, mSwipeRefreshLayout, new HttpCallbackListener() {
//            @Override
//            public void onFinish(String response) throws JSONException {
//                JSONObject jsonObject = new JSONObject(response);
//                String code = jsonObject.optString("code");
//                String obj = jsonObject.optString("obj");
//                if (isRefresh) {
//                    mAdapter.setEnableLoadMore(true);
//                    mSwipeRefreshLayout.setRefreshing(false);
//                }
//
////                    Gson gson = new Gson();
////                    List<PartnerDynamicBean> partnerDynamicBeanList = gson.fromJson(obj, PartnerDynamicBean.class);
//                List<PersonalDataBean> dataList = new ArrayList<>();
//                for (int i = 0; i < 10; i++) {
//                    PersonalDataBean personalDataBean = new PersonalDataBean();
//                    personalDataBean.setTitle(i + "素材结算");
//                    dataList.add(personalDataBean);
//                }
//                setData(isRefresh, dataList);
//                mAdapter.notifyDataSetChanged();
//
//            }
//
//            @Override
//            public void onFailure() {
//                if (isRefresh) {
//                    mAdapter.setEnableLoadMore(true);
//                    mSwipeRefreshLayout.setRefreshing(false);
//                } else {
//                    mAdapter.loadMoreFail();
//                }
//
//            }
//        });
    }


    private void setData(boolean isRefresh, List<PersonalDataBean> data) {
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

}
