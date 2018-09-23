package com.cebbank.partner.ui;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cebbank.partner.BaseActivity;
import com.cebbank.partner.MyApplication;
import com.cebbank.partner.R;
import com.cebbank.partner.adapter.PersonalDataAdapter;
import com.cebbank.partner.bean.IncomeListBean;
import com.cebbank.partner.bean.PersonalDataBean;
import com.cebbank.partner.interfaces.HttpCallbackListener;
import com.cebbank.partner.utils.LogUtils;
import com.cebbank.partner.utils.ToastUtils;
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

public class PersonalDataActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private PersonalDataAdapter mAdapter;
    private List<IncomeListBean> data;
    private static final int PAGE_SIZE = 10;
    private int mNextRequestPage = 1;
    private TextView tvTotalIncome, tvBalanceIncome, tvLastMonthSunshineData, tvWithdraw;
    private Dialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_data);
        initView();
        initData();
        setListener();
    }

    private void initView() {
        setTitle("个人数据");
        setBackBtn();
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
        tvTotalIncome = findViewById(R.id.tvTotalIncome);
        tvBalanceIncome = findViewById(R.id.tvBalanceIncome);
        tvLastMonthSunshineData = findViewById(R.id.tvLastMonthSunshineData);
        tvWithdraw = findViewById(R.id.tvWithdraw);
    }

    private void initData() {
        income();
        list(true);
    }

    private void setListener() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                list(true);
            }
        });
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                list(false);
            }
        });
        findViewById(R.id.tvWithdraw).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
    }

    /**
     * 收益
     */
    private void income() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("token", MyApplication.getToken());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        sendOkHttpRequest(this, UrlPath.Income, jsonObject, null, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) throws JSONException {
                JSONObject jsonObject = new JSONObject(response);
                JSONObject jo = jsonObject.getJSONObject("data");
                Gson gson = new Gson();
                PersonalDataBean personalDataBean = gson.fromJson(jo.toString(), PersonalDataBean.class);
                tvTotalIncome.setText(personalDataBean.getTotal());
                tvBalanceIncome.setText(personalDataBean.getBalance());
                tvLastMonthSunshineData.setText(personalDataBean.getLastMonth());
            }

            @Override
            public void onFailure() {

            }
        });
    }

    private void showDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.popopwindow_layout, null);// 得到加载view
        final EditText tvAmount = v.findViewById(R.id.tvAmount);
        TextView tvWithdraw = v.findViewById(R.id.tvWithdraw);
        tvWithdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String amount = tvAmount.getText().toString();
                if (TextUtils.isEmpty(amount)) {
                    ToastUtils.showShortToast("请输入您要提现的金额");
                    return;
                }
                if (Integer.valueOf(amount) < 500) {
                    ToastUtils.showShortToast("请输入大于等于500的整数");
                    return;
                }
                LogUtils.e(amount);
                withdraw(amount);
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

    /**
     * 提现
     */
    private void withdraw(String amount) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("token", MyApplication.getToken());
            jsonObject.put("amount", amount);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        sendOkHttpRequest(this, UrlPath.Withdraw, jsonObject, null, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) throws JSONException {
                JSONObject jsonObject = new JSONObject(response);
                ToastUtils.showShortToast(jsonObject.optString("msg"));
                loadingDialog.dismiss();
            }

            @Override
            public void onFailure() {

            }
        });
    }

    /**
     * 收益列表
     *
     * @param isRefresh
     */
    private void list(final boolean isRefresh) {
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

        sendOkHttpRequest(this, UrlPath.IncomeList, jo, mSwipeRefreshLayout, new HttpCallbackListener() {
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
                List<IncomeListBean> attentionPartnerBeanList =
                        gson.fromJson(jsonArray.toString(), new TypeToken<List<IncomeListBean>>() {
                        }.getType());
                setData(isRefresh, attentionPartnerBeanList);
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


    private void setData(boolean isRefresh, List<IncomeListBean> data) {
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
