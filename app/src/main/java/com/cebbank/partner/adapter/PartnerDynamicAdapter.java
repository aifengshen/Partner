package com.cebbank.partner.adapter;

import android.view.View;

import com.cebbank.partner.R;
import com.cebbank.partner.bean.PartnerDynamicBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

public class PartnerDynamicAdapter extends BaseQuickAdapter<PartnerDynamicBean, BaseViewHolder> {

    public PartnerDynamicAdapter(int layoutResId, List data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final PartnerDynamicBean item) {

        helper.setText(R.id.tvTitle, item.getTitle());


        helper.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

}
