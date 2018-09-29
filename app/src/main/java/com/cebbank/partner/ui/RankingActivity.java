package com.cebbank.partner.ui;

import android.os.Bundle;

import com.cebbank.partner.BaseActivity;
import com.cebbank.partner.R;

public class RankingActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);
        setTitle("排行榜");
        setBackBtn();
    }
}
