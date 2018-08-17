package com.cebbank.partner.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cebbank.partner.R;
import com.cebbank.partner.adapter.MyViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;


/**
 * @ClassName: Omnipotent
 * @Description:
 * @Author Pjw
 * @date 2018/7/31 15:27
 */
public class AttentionFragment extends Fragment {

    private View view;
    private MyViewPagerAdapter mMyViewPagerAdapter;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_attention, container, false);
        initView(view);
        initData();
        setClickListener();
        return view;
    }

    private void initView(View view) {
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new PartnerDynamicFragment());
        fragmentList.add(new AttentionPartnerFragment());
        fragmentList.add(new MyAcceptFragment());
        List<String> titleList = new ArrayList<>();
        titleList.add("合伙人动态");
        titleList.add("关注的合伙人");
        titleList.add("我的收纳");
        mMyViewPagerAdapter = new MyViewPagerAdapter(getActivity().getSupportFragmentManager(), fragmentList, titleList);
        mViewPager = view.findViewById(R.id.pager);
        mViewPager.setAdapter(mMyViewPagerAdapter);
        mTabLayout = view.findViewById(R.id.tablayout);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    private void initData() {

    }

    private void setClickListener() {
//        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//                Log.i("啊啊啊", "select page:" + position);
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//
//            }
//        });
    }


}
