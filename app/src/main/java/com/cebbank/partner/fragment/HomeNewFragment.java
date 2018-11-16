package com.cebbank.partner.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
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
import com.cebbank.partner.adapter.HomeNewAdapter;
import com.cebbank.partner.bean.ArticleNewBean;
import com.cebbank.partner.bean.BannerBean;
import com.cebbank.partner.bean.Contact;
import com.cebbank.partner.interfaces.HttpCallbackListener;
import com.cebbank.partner.interfaces.LocateListener;
import com.cebbank.partner.ui.ArticleDetailActivity;
import com.cebbank.partner.ui.CityActivity;
import com.cebbank.partner.utils.UrlPath;
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
import java.util.HashMap;
import java.util.List;

import static com.cebbank.partner.utils.HttpUtil.sendOkHttpRequest;

/**
 * @ClassName: Partner
 * @Description:
 * @Author Pjw
 * @date 2018/11/15 14:01
 */
public class HomeNewFragment extends BaseFragment implements View.OnClickListener, ViewPagerEx.OnPageChangeListener {

    private View view;
    boolean isFirst = true;
    private TextView tvLocateCity;
    private EditText edittextClientName;
    private SliderLayout mDemoSlider;

    private RecyclerView recyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private HomeNewAdapter mAdapter;
    private List<ArticleNewBean> data;
    private static final int PAGE_SIZE = 3;
    private int mNextRequestPage = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_home_new, container, false);
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

        recyclerView = view.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        mSwipeRefreshLayout = view.findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setColorSchemeColors(Color.rgb(47, 223, 189));
        data = new ArrayList<>();
        mAdapter = new HomeNewAdapter(getActivity(), data);

//        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(mAdapter);
    }

    private void initData() {
        requestArticle(edittextClientName.getText().toString(), "010e9dd75cd04172a45441bf3485fc48");

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

    private void requestArticle(String keyword, final String tabId) {

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
                mSwipeRefreshLayout.setRefreshing(false);
                JSONArray jsonArray = jodata.getJSONArray("records");
                Gson gson = new Gson();
                List<ArticleNewBean> articleBeanList =
                        gson.fromJson(jsonArray.toString(), new TypeToken<List<ArticleNewBean>>() {
                        }.getType());
                if (tabId.equals("010e9dd75cd04172a45441bf3485fc48")) {
                    data.removeAll(data);
                    ArticleNewBean articleBean = new ArticleNewBean();
                    articleBean.setType("HEADER");
                    data.add(articleBean);
                    ArticleNewBean articleBean1 = new ArticleNewBean();
                    articleBean1.setType("TITLE");
                    articleBean1.setTitle("热推活动");
                    data.add(articleBean1);
                    data.addAll(articleBeanList);
                    ArticleNewBean articleBean2 = new ArticleNewBean();
                    articleBean2.setType("TITLE");
                    articleBean2.setTitle("猜你想推");
                    data.add(articleBean2);
                    requestArticle(edittextClientName.getText().toString(), "a6a71753262a478881d7eb3b7d96f890");
                } else {
                    data.addAll(articleBeanList);
                    mAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onFailure() {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void setListener() {
        view.findViewById(R.id.tvSearch).setOnClickListener(this);
        tvLocateCity.setOnClickListener(this);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvSearch:
                requestBanner(edittextClientName.getText().toString());
                break;
            case R.id.tvLocateCity:
                Intent intent = new Intent(getActivity(), CityActivity.class);
                startActivityForResult(intent, 0);
                break;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == getActivity().RESULT_OK) {
            String name = data.getStringExtra("name");
            tvLocateCity.setText(name);
//            requestBanner(name);
        }
    }

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
