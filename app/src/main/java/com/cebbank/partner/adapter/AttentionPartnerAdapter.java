package com.cebbank.partner.adapter;

import android.view.View;

import com.cebbank.partner.R;
import com.cebbank.partner.bean.AttentionPartnerBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

public class AttentionPartnerAdapter extends BaseQuickAdapter<AttentionPartnerBean, BaseViewHolder> {

    public AttentionPartnerAdapter(int layoutResId, List data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final AttentionPartnerBean item) {

        helper.setText(R.id.tvTitle, item.getTitle());


        helper.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
}
