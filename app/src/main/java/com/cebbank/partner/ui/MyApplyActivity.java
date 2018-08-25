package com.cebbank.partner.ui;

import android.os.Bundle;

import com.cebbank.partner.BaseActivity;
import com.cebbank.partner.R;

public class MyApplyActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_apply);
        initView();
        initData();
        setListener();
    }

    private void initView(){
        setTitle("我的申请");
        setBackBtn();
    }

    private void initData(){

    }

    private void setListener(){

    }
}
