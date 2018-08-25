package com.cebbank.partner.ui;

import android.os.Bundle;

import com.cebbank.partner.BaseActivity;
import com.cebbank.partner.R;

public class BecomePartnerActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_become_partner);
        initView();
        initData();
        setListener();
    }

    private void initView(){
        setTitle("成为阳光合伙人");
        setBackBtn();
    }

    private void initData(){

    }

    private void setListener(){

    }
}
