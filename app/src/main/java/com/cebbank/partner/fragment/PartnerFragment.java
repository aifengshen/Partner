package com.cebbank.partner.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cebbank.partner.R;
import com.cebbank.partner.ui.UpLoadIDActivity;


/**
 * 成为合伙人fragment
 */
public class PartnerFragment extends Fragment implements View.OnClickListener{

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = LayoutInflater.from(getActivity()).inflate(R.layout.activity_become_partner, container, false);
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
        view.findViewById(R.id.tvNext).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tvNext:
                /**
                 * 下一步
                 */
                Intent intent = new Intent(getActivity(),UpLoadIDActivity.class);
                startActivity(intent);
                break;
        }
    }


}
