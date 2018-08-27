package com.cebbank.partner.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.cebbank.partner.MyApplication;
import com.cebbank.partner.R;
import com.cebbank.partner.adapter.AttentionPartnerAdapter;
import com.cebbank.partner.adapter.PartnerDynamicAdapter;
import com.cebbank.partner.bean.AttentionPartnerBean;
import com.cebbank.partner.bean.PartnerDynamicBean;
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

/**
 *关注的合伙人
 */
public class AttentionPartnerFragment extends Fragment {

    private RecyclerView recyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private AttentionPartnerAdapter mAdapter;
    private List<AttentionPartnerBean> data;
    private static final int PAGE_SIZE = 10;
    private int mNextRequestPage = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_attention_partner, container, false);
        initView(rootView);
        initData();
        setListener();
        return rootView;
    }

    private void initView(View view){
        recyclerView = view.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        mSwipeRefreshLayout = view.findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setColorSchemeColors(Color.rgb(47, 223, 189));
        data = new ArrayList<>();
        mAdapter = new AttentionPartnerAdapter(R.layout.fragment_attention_partner_item, data);
        mAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_BOTTOM);
//        mAdapter.setPreLoadNumber(3);
        mAdapter.setLoadMoreView(new CustomLoadMoreView());
        recyclerView.setAdapter(mAdapter);
//        //添加自定义分割线
//        DividerItemDecoration divider = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
//        divider.setDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.custom_divider));
//        recyclerView.addItemDecoration(divider);
    }

    private void initData(){
        for (int i = 0; i < 10; i++) {
            AttentionPartnerBean attentionPartnerBean = new AttentionPartnerBean();
            attentionPartnerBean.setTitle(i + "啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊");
            data.add(attentionPartnerBean);
        }
        mAdapter.notifyDataSetChanged();
    }

    private void setListener(){
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

//        sendOkHttpRequest(getActivity(), UrlPath.Login, paramsMap, null, new HttpCallbackListener() {
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
//                List<AttentionPartnerBean> dataList = new ArrayList<>();
//                for (int i = 0; i < 10; i++) {
//                    AttentionPartnerBean attentionPartnerBean = new AttentionPartnerBean();
//                    attentionPartnerBean.setTitle(i + "啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊");
//                    dataList.add(attentionPartnerBean);
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


    private void setData(boolean isRefresh, List<AttentionPartnerBean> data) {
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
            Toast.makeText(getActivity(), "没有更多数据了...", Toast.LENGTH_SHORT).show();
        } else {
            mAdapter.loadMoreComplete();
        }
    }

}
