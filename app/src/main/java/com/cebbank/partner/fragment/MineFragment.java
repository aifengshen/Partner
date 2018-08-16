package com.cebbank.partner.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.cebbank.partner.R;

/**
 * @ClassName: Omnipotent
 * @Description:
 * @Author Pjw
 * @date 2018/7/31 15:26
 */
public class MineFragment extends Fragment {

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_mine, container, false);
        initView();
        initData();
        setClickListener();
        return view;
    }

    private void initView(){

    }
    private void initData(){

    }
    private void setClickListener(){

    }
}
