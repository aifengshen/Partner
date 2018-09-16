package com.cebbank.partner.adapter;

import android.view.View;

import com.cebbank.partner.bean.MyApplyBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

public class MyApplyAdapter extends BaseQuickAdapter<MyApplyBean, BaseViewHolder> {

    public MyApplyAdapter(int layoutResId, List data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final MyApplyBean item) {

//        helper.setText(R.id.tvTitle, item.getTitle());


        helper.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
}
