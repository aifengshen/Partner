package com.cebbank.partner.adapter;

import android.view.View;

import com.cebbank.partner.R;
import com.cebbank.partner.bean.PersonalDataBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

public class PersonalDataAdapter extends BaseQuickAdapter<PersonalDataBean, BaseViewHolder> {

    public PersonalDataAdapter(int layoutResId, List data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final PersonalDataBean item) {

        helper.setText(R.id.tvSettlementType, item.getTitle());


        helper.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
}
