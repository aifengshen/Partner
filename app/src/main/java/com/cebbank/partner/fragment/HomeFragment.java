package com.cebbank.partner.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.cebbank.partner.MyApplication;
import com.cebbank.partner.R;
import com.cebbank.partner.adapter.ArticleAdapter;
import com.cebbank.partner.bean.ArticleBean;
import com.cebbank.partner.bean.HomeFragmentBean;
import com.cebbank.partner.interfaces.HttpCallbackListener;
import com.cebbank.partner.interfaces.LocateListener;
import com.cebbank.partner.ui.WelcomeActivity;
import com.cebbank.partner.utils.UrlPath;
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
import java.util.HashMap;
import java.util.List;

import static com.cebbank.partner.utils.HttpUtil.sendOkHttpRequest;

/**
 * @ClassName: Omnipotent
 * @Description:
 * @Author Pjw
 * @date 2018/7/31 15:25
 */
public class HomeFragment extends Fragment implements View.OnClickListener, BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {

    private View view;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ArticleAdapter mAdapter;
    private List<ArticleBean> data;

    private EditText edittextClientName;
    private SliderLayout mDemoSlider;
    private MyApplication myApplication;
    private static final int PAGE_SIZE = 10;
    private int mNextRequestPage = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_home, container, false);
        initView();
        initData();
        setClickListener();
        return view;
    }

    private void initView() {
        myApplication = (MyApplication) getActivity().getApplication();
        myApplication.setLocateListener(new LocateListener() {
            @Override
            public void OnLocate(String Latitude, String Longitude, String cityName, String adCode) {
                locate(Latitude, Longitude);
                requestBanner(adCode);
            }
        });
        recyclerView = view.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        mSwipeRefreshLayout = view.findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setColorSchemeColors(Color.rgb(47, 223, 189));
        edittextClientName = view.findViewById(R.id.edittextClientName);
        data = new ArrayList<>();
        mAdapter = new ArticleAdapter(data);
        mAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_BOTTOM);
//        mAdapter.setPreLoadNumber(3);
//        mAdapter.setLoadMoreView(new CustomLoadMoreView());
        recyclerView.setAdapter(mAdapter);

        View view = getLayoutInflater().inflate(R.layout.headerview, (ViewGroup) recyclerView.getParent(), false);
        mAdapter.addHeaderView(view);
        mDemoSlider = view.findViewById(R.id.slider);
        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Default);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.setDuration(4000);
        mDemoSlider.addOnPageChangeListener(this);

    }

    private void initData() {
        requestArticle(true);
        mAdapter.notifyDataSetChanged();
        HashMap<String, String> url_maps = new HashMap<String, String>();
        url_maps.put("Hannibal", "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1534440083067&di=a04d46600bd5cfa3cf5b09f39de42f23&imgtype=0&src=http%3A%2F%2Fp16.qhimg.com%2Fbdr%2F__%2Fd%2F_open360%2Fbeauty0311%2F16.jpg");
        url_maps.put("Big Bang Theory", "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1534440083067&di=e63cfc6a75ce3857d0d4eff60586bc01&imgtype=0&src=http%3A%2F%2Fimg.ph.126.net%2FijQNs4q86Q2fn5UI7ezxuQ%3D%3D%2F733523789408400623.jpg");
        url_maps.put("House of Cards", "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1534440083067&di=83cf8d486adb35659ee5eb7038b85ac3&imgtype=0&src=http%3A%2F%2Fpic1.win4000.com%2Fwallpaper%2F1%2F572afc324b7f1.jpg");
        url_maps.put("Game of Thrones", "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1534440083067&di=ebcc5903ced82773e206a92806c23637&imgtype=0&src=http%3A%2F%2Fi8.download.fd.pchome.net%2Ft_600x1024%2Fg1%2FM00%2F07%2F18%2FoYYBAFNY5H2If_UQAA_d0sgFVhwAABfCAFKBJQAD93q339.jpg");

        for (String name : url_maps.keySet()) {
            TextSliderView textSliderView = new TextSliderView(getActivity());
            // initialize a SliderLayout
            textSliderView
//                    .description(name)
                    .image(url_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(this);
            //add your extra information
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra", name);

            mDemoSlider.addSlider(textSliderView);
        }
    }

    private void locate(String Latitude, String Longitude) {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("lng", Longitude);
            jsonObject.put("lat", Latitude);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        sendOkHttpRequest(getActivity(), UrlPath.Locate, jsonObject, null, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) throws JSONException {
                JSONObject jsonObject = new JSONObject(response);
                String code = jsonObject.optString("code");
                JSONObject jodata = jsonObject.getJSONObject("data");

//                JSONArray jsonArray = jodata.getJSONArray("records");
//                Gson gson = new Gson();
//                List<PartnerDynamicBean> partnerDynamicBeanList =
//                        gson.fromJson(jsonArray.toString(), new TypeToken<List<PartnerDynamicBean>>() {
//                        }.getType());

//                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure() {

            }
        });
    }

    private void requestBanner(String adCode) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("areaId ", adCode);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        sendOkHttpRequest(getActivity(), UrlPath.Banner, jsonObject, null, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) throws JSONException {
                JSONObject jsonObject = new JSONObject(response);
                String code = jsonObject.optString("code");
                JSONObject jodata = jsonObject.getJSONObject("data");

//                JSONArray jsonArray = jodata.getJSONArray("records");
//                Gson gson = new Gson();
//                List<PartnerDynamicBean> partnerDynamicBeanList =
//                        gson.fromJson(jsonArray.toString(), new TypeToken<List<PartnerDynamicBean>>() {
//                        }.getType());

//                mAdapter.notifyDataSetChanged();
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
            jo.put("keyword", "");
            jo.put("page", jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        sendOkHttpRequest(getActivity(), UrlPath.Article, jo, null, new HttpCallbackListener() {
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
            Toast.makeText(getActivity(), "没有更多数据了...", Toast.LENGTH_SHORT).show();
        } else {
            mAdapter.loadMoreComplete();
        }
    }


    @Override
    public void onSliderClick(BaseSliderView slider) {
        startActivity(new Intent(getActivity(), WelcomeActivity.class));
        Toast.makeText(getActivity(), slider.getBundle().get("extra") + "", Toast.LENGTH_SHORT).show();
    }


    private void setClickListener() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

//        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
//            @Override
//            public void onLoadMoreRequested() {
//                request(false);
//            }
//        });

    }

    @Override
    public void onClick(View v) {

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
