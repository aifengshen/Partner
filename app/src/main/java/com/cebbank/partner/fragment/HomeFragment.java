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

import com.cebbank.partner.BaseFragment;
import com.cebbank.partner.R;
import com.cebbank.partner.adapter.HomeFragmentAdapter;
import com.cebbank.partner.bean.HomeFragmentBean;
import com.cebbank.partner.ui.WelcomeActivity;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @ClassName: Omnipotent
 * @Description:
 * @Author Pjw
 * @date 2018/7/31 15:25
 */
public class HomeFragment extends Fragment implements View.OnClickListener, BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {

    private View view;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeLayout;
    private HomeFragmentAdapter mAdapter;
    private List<HomeFragmentBean> data;
    private EditText edittextClientName;
    private SliderLayout mDemoSlider;

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
        recyclerView = view.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        swipeLayout = view.findViewById(R.id.swipe_container);
        swipeLayout.setColorSchemeColors(Color.rgb(47, 223, 189));
        edittextClientName = view.findViewById(R.id.edittextClientName);
        data = new ArrayList<>();
        mAdapter = new HomeFragmentAdapter(data);
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

        for (int i = 1; i < 5; i++) {
            HomeFragmentBean homeFragmentBean = new HomeFragmentBean(i);
            homeFragmentBean.setName(i + "哈哈哈");
            data.add(homeFragmentBean);
        }
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


    @Override
    public void onSliderClick(BaseSliderView slider) {
        startActivity(new Intent(getActivity(), WelcomeActivity.class));
        Toast.makeText(getActivity(), slider.getBundle().get("extra") + "", Toast.LENGTH_SHORT).show();
    }


    private void setClickListener() {
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
                swipeLayout.setRefreshing(false);
            }
        });

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
