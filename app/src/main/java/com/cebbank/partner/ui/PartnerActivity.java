package com.cebbank.partner.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cebbank.partner.BaseActivity;
import com.cebbank.partner.GlideApp;
import com.cebbank.partner.MainActivity;
import com.cebbank.partner.MyApplication;
import com.cebbank.partner.R;
import com.cebbank.partner.adapter.ArticleAdapter;
import com.cebbank.partner.bean.ArticleBean;
import com.cebbank.partner.bean.AttentionPartnerBean;
import com.cebbank.partner.interfaces.HttpCallbackListener;
import com.cebbank.partner.utils.LogUtils;
import com.cebbank.partner.utils.ToastUtils;
import com.cebbank.partner.utils.UrlPath;
import com.cebbank.partner.view.CustomLoadMoreView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.cebbank.partner.utils.HttpUtil.sendOkHttpRequest;

public class PartnerActivity extends BaseActivity implements View.OnClickListener {

    private RecyclerView recyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ArticleAdapter mAdapter;
    private List<ArticleBean> data;
    private static final int PAGE_SIZE = 10;
    private int mNextRequestPage = 1;
    private String url = UrlPath.Collection, type = "", id = "", idol = "", avatar = "";
    private TabLayout mTabLayout;
    private TextView tvName, tvIsAttent, tvSignature, tvFans, tvPraise, tvCopy;
    private Dialog loadingDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partner);
        initView();
        initData();
        setListener();
    }

    private void initView() {
        setTitle("我的素材");
        setBackBtn();
        mTabLayout = findViewById(R.id.tablayout);
        mTabLayout.addTab(mTabLayout.newTab().setText("收纳"));
        mTabLayout.addTab(mTabLayout.newTab().setText("原创"));

        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        mSwipeRefreshLayout = findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setColorSchemeColors(Color.rgb(47, 223, 189));
        data = new ArrayList<>();
        mAdapter = new ArticleAdapter(data);
        mAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_BOTTOM);
//        mAdapter.setPreLoadNumber(3);
        mAdapter.setLoadMoreView(new CustomLoadMoreView());
        recyclerView.setAdapter(mAdapter);
        mTabLayout = findViewById(R.id.tablayout);
        tvName = findViewById(R.id.tvName);
        tvIsAttent = findViewById(R.id.tvIsAttent);
        tvSignature = findViewById(R.id.tvSignature);
        tvFans = findViewById(R.id.tvFans);
        tvPraise = findViewById(R.id.tvPraise);
        tvCopy = findViewById(R.id.tvCopy);
    }

    private void initData() {
        Info();
        requestArticle(true);
    }

    private void setListener() {
        tvCopy.setOnClickListener(this);
        tvIsAttent.setOnClickListener(this);
        tvCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        url = UrlPath.Collection;
                        type = "collection";
                        requestArticle(true);
                        break;
                    case 1:
                        url = UrlPath.Creation;
                        type = "creation";
                        requestArticle(true);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestArticle(true);
            }
        });
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                requestArticle(false);
            }
        });
    }

    private void Info() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("token", MyApplication.getToken());
            jsonObject.put("partnerId", getIntent().getStringExtra("userId"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        sendOkHttpRequest(this, UrlPath.Info, jsonObject, null, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) throws JSONException {
                JSONObject jsonObject = new JSONObject(response);
                JSONObject jo = jsonObject.getJSONObject("data");
                id = jo.optString("id");
                avatar = jo.optString("avatar");
                String username = jo.optString("username");
                String signature = jo.optString("signature");
                String oneself = jo.optString("oneself");
                idol = jo.optString("idol");
                String fans = jo.optString("fans");
                String like = jo.optString("like");

                GlideApp.with(PartnerActivity.this)
                        .load(avatar)
//                        .placeholder(R.mipmap.loading)
                        .centerCrop()
                        .into((CircleImageView) findViewById(R.id.profile_image));
                tvName.setText(username);

                tvSignature.setText(signature.equals("null") ? "暂无签名" : signature);
                tvFans.setText(fans + " 粉丝");
                tvPraise.setText(like + " 获赞");
                if (oneself.equals("true")) {
                    tvIsAttent.setVisibility(View.INVISIBLE);
                    tvCopy.setVisibility(View.INVISIBLE);
                } else {
                    tvIsAttent.setVisibility(View.VISIBLE);
                    tvCopy.setVisibility(View.VISIBLE);
                    if (idol.equals("false")) {
                        tvIsAttent.setText("已关注");
                    } else {
                        tvIsAttent.setText("关注");
                    }

                }
            }

            @Override
            public void onFailure() {

            }
        });
    }

    private void requestArticle(final boolean isRefresh) {
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
            jo.put("partnerId", getIntent().getStringExtra("userId"));
            jo.put("token", MyApplication.getToken());
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
//            Toast.makeText(this, "没有更多数据了...", Toast.LENGTH_SHORT).show();
        } else {
            mAdapter.loadMoreComplete();
        }
    }

    public static void actionStart(Context context, String userId) {
        Intent intent = new Intent(context, PartnerActivity.class);
        intent.putExtra("userId", userId);
        context.startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvIsAttent:
                /**
                 * 关注
                 */
                cancel();
                break;
            case R.id.tvCopy:
                /**
                 * 聊一聊复制微信号
                 */

                break;
        }
    }

    /**
     * 关注、取关
     */
    private void cancel() {
        String url = "";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("idolId", id);
            jsonObject.put("token", MyApplication.getToken());

            if (idol.equals("false")) {
                url = UrlPath.Cancel;
            } else {
                url = UrlPath.Concern;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        sendOkHttpRequest(this, url, jsonObject, null, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) throws JSONException {
                JSONObject jsonObject = new JSONObject(response);
                if (idol.equals("false")) {
                    idol = "true";
                    ToastUtils.showShortToast("取关成功");
                    tvIsAttent.setText("关注");
                } else {
                    idol = "false";
                    ToastUtils.showShortToast("关注成功");
                    tvIsAttent.setText("已关注");
                }

            }

            @Override
            public void onFailure() {

            }
        });
    }

    private void showDialog() {
        loadingDialog = new Dialog(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.popopwindow_layout_head_portrait, null);// 得到加载view
        ImageView imgAvatar = v.findViewById(R.id.imgAvatar);
        GlideApp.with(this)
                .load(avatar)
//                        .placeholder(R.mipmap.loading)
                .centerCrop()
                .into(imgAvatar);
        TextView tvAddwx = v.findViewById(R.id.tvAddwx);
        tvAddwx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showShortToast("复制成功");

                loadingDialog.dismiss();
            }
        });

        loadingDialog = new Dialog(this);// 创建自定义样式dialog
        loadingDialog.setCancelable(true);// 不可以用“返回键”取消
        loadingDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        loadingDialog.setContentView(v);
        Window win = loadingDialog.getWindow();
//        win.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        win.setAttributes(lp);
        loadingDialog.show();
    }
}
