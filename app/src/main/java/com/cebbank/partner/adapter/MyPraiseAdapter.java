package com.cebbank.partner.adapter;

import android.view.View;

import com.cebbank.partner.R;
import com.cebbank.partner.bean.MyPraiseBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

public class MyPraiseAdapter extends BaseQuickAdapter<MyPraiseBean, BaseViewHolder> {

    public MyPraiseAdapter(int layoutResId, List data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final MyPraiseBean item) {

        helper.setText(R.id.tvName, item.username);
        helper.setText(R.id.tvTitle, item.getTitle());


        helper.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
}
