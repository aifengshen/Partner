package com.cebbank.partner.ui;

import android.os.Bundle;

import com.cebbank.partner.BaseActivity;
import com.cebbank.partner.R;

public class PersonalDataActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_data);
        initView();
        initData();
        setListener();
    }

    private void initView(){
        setTitle("个人数据");
        setBackBtn();
    }

    private void initData(){

    }

    private void setListener(){

    }
}
