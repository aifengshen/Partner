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
import android.widget.TextView;
import android.widget.Toast;

import com.cebbank.partner.MyApplication;
import com.cebbank.partner.R;
import com.cebbank.partner.adapter.MessageFragmentAdapter;
import com.cebbank.partner.bean.AttentionPartnerBean;
import com.cebbank.partner.bean.MessageFragmentBean;
import com.cebbank.partner.interfaces.HttpCallbackListener;
import com.cebbank.partner.ui.ArticleCommentActivity;
import com.cebbank.partner.ui.MyCommentActivity;
import com.cebbank.partner.ui.MyPraiseActivity;
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
 * @ClassName: Omnipotent
 * @Description:
 * @Author Pjw
 * @date 2018/7/31 15:27
 */
public class MessageFragment extends Fragment implements View.OnClickListener {

    private View view;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private static final int PAGE_SIZE = 10;
    private int mNextRequestPage = 1;
    private MessageFragmentAdapter mAdapter;
    private List<MessageFragmentBean> data;
    private TextView tvWonThePraise, tvComment;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_message, container, false);
        initView();
        initData();
        setClickListener();
        return view;
    }

    private void initView() {
        tvWonThePraise = view.findViewById(R.id.tvWonThePraise);
        tvComment = view.findViewById(R.id.tvComment);
        recyclerView = view.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        mSwipeRefreshLayout = view.findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setColorSchemeColors(Color.rgb(47, 223, 189));

        data = new ArrayList<>();
        mAdapter = new MessageFragmentAdapter(R.layout.fragment_message_item, data);
        mAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_BOTTOM);
//        mAdapter.setPreLoadNumber(3);
//        mAdapter.setLoadMoreView(new CustomLoadMoreView());
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(mAdapter);
    }

    private void initData() {
        requestMessages(true);
    }

    private void requestMessages(final boolean isRefresh) {
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

        sendOkHttpRequest(getActivity(), UrlPath.SystemMessages, jo, mSwipeRefreshLayout, new HttpCallbackListener() {
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
                List<MessageFragmentBean> messageFragmentBeanList =
                        gson.fromJson(jsonArray.toString(), new TypeToken<List<MessageFragmentBean>>() {
                        }.getType());
                setData(isRefresh, messageFragmentBeanList);
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

    private void setData(boolean isRefresh, List<MessageFragmentBean> data) {
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

    private void setClickListener() {
        tvWonThePraise.setOnClickListener(this);
        tvComment.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvWonThePraise:
                /**
                 * 我的点赞页面
                 */
                MyPraiseActivity.actionStart(getActivity());
                break;
            case R.id.tvComment:
                /**
                 * 我的评论页面
                 */
                MyCommentActivity.actionStart(getActivity());
                break;
        }
    }
}
