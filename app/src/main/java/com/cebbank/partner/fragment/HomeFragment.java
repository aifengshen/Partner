package com.cebbank.partner.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cebbank.partner.BaseFragment;
import com.cebbank.partner.R;
import com.cebbank.partner.adapter.ArticleAdapter;
import com.cebbank.partner.adapter.MyViewPagerAdapter;
import com.cebbank.partner.bean.ArticleBean;
import com.cebbank.partner.bean.BannerBean;
import com.cebbank.partner.bean.Contact;
import com.cebbank.partner.bean.TabIdBean;
import com.cebbank.partner.interfaces.HttpCallbackListener;
import com.cebbank.partner.interfaces.LocateListener;
import com.cebbank.partner.ui.ArticleDetailActivity;
import com.cebbank.partner.ui.CityActivity;
import com.cebbank.partner.utils.UrlPath;
import com.cebbank.partner.view.CustomLoadMoreView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import static com.cebbank.partner.utils.HttpUtil.sendOkHttpRequest;

/**
 * @ClassName: Omnipotent
 * @Description:
 * @Author Pjw
 * @date 2018/7/31 15:25
 */
public class HomeFragment extends BaseFragment implements View.OnClickListener, ViewPagerEx.OnPageChangeListener {

    private View view;
    boolean isFirst = true;
    private TextView tvLocateCity, tvCredit, tvHappy, tvActive;
    private EditText edittextClientName;
    private SliderLayout mDemoSlider;

    private RecyclerView recyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ArticleAdapter mAdapter;
    private List<ArticleBean> data;

    private static final int PAGE_SIZE = 10;
    private int mNextRequestPage = 1;
    private String tabbId = "8ba25b6abd154b7daf8e6fd01b5e8201";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_home, container, false);
        initView();
        initData();
        setListener();
        return view;
    }

    private void initView() {
        setLocateListener(new LocateListener() {
            @Override
            public void OnLocate(String Latitude, String Longitude, String cityName, String adCode) {
                if (isFirst) {
                    tvLocateCity.setText(cityName);
                    requestBanner(cityName);
                }
                isFirst = false;
            }
        });
        mDemoSlider = view.findViewById(R.id.slider);
        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Default);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.setDuration(4000);
        mDemoSlider.addOnPageChangeListener(this);
        edittextClientName = view.findViewById(R.id.edittextClientName);
        tvLocateCity = view.findViewById(R.id.tvLocateCity);
        tvCredit = view.findViewById(R.id.tvCredit);
        tvHappy = view.findViewById(R.id.tvHappy);
        tvActive = view.findViewById(R.id.tvActive);
        tvCredit.setSelected(true);

        recyclerView = view.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        mSwipeRefreshLayout = view.findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setColorSchemeColors(Color.rgb(47, 223, 189));
        data = new ArrayList<>();
        mAdapter = new ArticleAdapter(data);
        mAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_BOTTOM);
        mAdapter.setPreLoadNumber(3);
        mAdapter.setLoadMoreView(new CustomLoadMoreView());
        recyclerView.setAdapter(mAdapter);
    }

    private void initData() {
        requestArticle(true, edittextClientName.getText().toString(), tabbId);
    }

    private void requestBanner(String cityName) {
        String cityCode = "";
        List<Contact> contactList = new ArrayList<>();
        contactList.addAll(Contact.getChineseContacts());
        for (int i = 0; i < contactList.size(); i++) {
            if (contactList.get(i).getName().equals(cityName)) {
                cityCode = contactList.get(i).getCode();
            }
        }
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("areaId", cityCode);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        sendOkHttpRequest(getActivity(), UrlPath.Banner, jsonObject, null, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) throws JSONException {
                JSONObject jsonObject = new JSONObject(response);
                String code = jsonObject.optString("code");
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                Gson gson = new Gson();
                final List<BannerBean> bannerBeanList = gson.fromJson(jsonArray.toString(), new TypeToken<List<BannerBean>>() {
                }.getType());
                HashMap<String, String> url_maps = new HashMap<>();
                for (int i = 0; i < bannerBeanList.size(); i++) {
                    final BannerBean bannerBean = bannerBeanList.get(i);
                    TextSliderView textSliderView = new TextSliderView(getActivity());
                    url_maps.put("", bannerBean.getImage());
                    // initialize a SliderLayout
                    textSliderView
//                    .description(name)
                            .image(bannerBean.getImage())
                            .setScaleType(BaseSliderView.ScaleType.Fit)
                            .setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                                @Override
                                public void onSliderClick(BaseSliderView slider) {
                                    ArticleDetailActivity.actionStart(getActivity(), bannerBean.getArticleId());
                                    Toast.makeText(getActivity(), "点击了第" + bannerBean.getSort() + "条banner", Toast.LENGTH_SHORT).show();
                                }
                            });
                    //add your extra information
                    textSliderView.bundle(new Bundle());
                    textSliderView.getBundle().putString("extra", "");
                    mDemoSlider.addSlider(textSliderView);
                }
            }

            @Override
            public void onFailure() {

            }
        });
    }

    private void requestArticle(final boolean isRefresh, String keyword, String tabId) {
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
            jo.put("tabId", tabId);
            jo.put("keyword", keyword);
            jo.put("page", jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        sendOkHttpRequest(getActivity(), UrlPath.Article, jo, mSwipeRefreshLayout, new HttpCallbackListener() {
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
//            Toast.makeText(getActivity(), "没有更多数据了...", Toast.LENGTH_SHORT).show();
        } else {
            mAdapter.loadMoreComplete();
        }
    }

//    private void requestTabId() {
//        JSONObject jsonObject = new JSONObject();
//        sendOkHttpRequest(getActivity(), UrlPath.TabList, jsonObject, null, new HttpCallbackListener() {
//            @Override
//            public void onFinish(String response) throws JSONException {
//                JSONObject jsonObject = new JSONObject(response);
//                String code = jsonObject.optString("code");
//                JSONArray jsonArray = jsonObject.getJSONArray("data");
//                Gson gson = new Gson();
//                final List<TabIdBean> tabIdBeanList = gson.fromJson(jsonArray.toString(), new TypeToken<List<TabIdBean>>() {
//                }.getType());
//                bubbleSort(tabIdBeanList);
//                for (int i = 0; i < tabIdBeanList.size(); i++) {
//                    TabIdBean tabIdBean = tabIdBeanList.get(i);
//                    HomeTabFragment homeTabFragment = new HomeTabFragment();
//                    Bundle bundle = new Bundle();
//                    bundle.putString("id",tabIdBean.getId());
//                    bundle.putString("name",tabIdBean.getName());
//                    homeTabFragment.setArguments(bundle);
//                    fragmentList.add(homeTabFragment);
//                    titleList.add(tabIdBean.getName());
//                }
//                mMyViewPagerAdapter = new MyViewPagerAdapter(getActivity().getSupportFragmentManager(), fragmentList, titleList);
//                mViewPager = view.findViewById(R.id.pager);
//                mViewPager.setAdapter(mMyViewPagerAdapter);
//                mTabLayout = view.findViewById(R.id.tablayout);
//                mTabLayout.setupWithViewPager(mViewPager);
//
//            }
//
//            @Override
//            public void onFailure() {
//
//            }
//        });
//    }

    private void setListener() {
        view.findViewById(R.id.tvSearch).setOnClickListener(this);
        tvLocateCity.setOnClickListener(this);
        tvCredit.setOnClickListener(this);
        tvHappy.setOnClickListener(this);
        tvActive.setOnClickListener(this);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                requestArticle(false, edittextClientName.getText().toString(), tabbId);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvSearch:
                requestArticle(true, edittextClientName.getText().toString(), tabbId);
                break;
            case R.id.tvLocateCity:
                Intent intent = new Intent(getActivity(), CityActivity.class);
                startActivityForResult(intent, 0);
                break;
            case R.id.tvCredit:
                tvCredit.setSelected(true);
                tvHappy.setSelected(false);
                tvActive.setSelected(false);
                tabbId = "8ba25b6abd154b7daf8e6fd01b5e8201";
                requestArticle(true, edittextClientName.getText().toString(), tabbId);
                break;
            case R.id.tvHappy:
                tvCredit.setSelected(false);
                tvHappy.setSelected(true);
                tvActive.setSelected(false);
                tabbId = "a6a71753262a478881d7eb3b7d96f890";
                requestArticle(true, edittextClientName.getText().toString(), tabbId);
                break;
            case R.id.tvActive:
                tvCredit.setSelected(false);
                tvHappy.setSelected(false);
                tvActive.setSelected(true);
                tabbId = "010e9dd75cd04172a45441bf3485fc48";
                requestArticle(true, edittextClientName.getText().toString(), tabbId);
                break;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == getActivity().RESULT_OK) {
            String name = data.getStringExtra("name");
            tvLocateCity.setText(name);
            requestBanner(name);
        }
    }


//    public void bubbleSort(List<TabIdBean> dataBatchBeans) {
//        Comparator<TabIdBean> comparator = new Comparator<TabIdBean>() {
//            public int compare(TabIdBean s1, TabIdBean s2) {
//                if (s1.getSort() > s2.getSort()) {
//                    return 1;
//                } else if (s1.getSort() == s2.getSort()) {
//                    return 0;
//                } else {
//                    return -1;
//                }
//            }
//        };
//        Collections.sort(dataBatchBeans, comparator);
//    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        Log.e("Slider Demo", "Page Changed: " + position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    public void onStart() {
        super.onStart();
        mDemoSlider.startAutoCycle();
    }

    @Override
    public void onStop() {
        // To prevent a memory leak on rotation, make sure to call stopAutoCycle() on the slider before activity or fragment is destroyed
        mDemoSlider.stopAutoCycle();
        super.onStop();
    }
}
