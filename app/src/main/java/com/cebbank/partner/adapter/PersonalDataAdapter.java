package com.cebbank.partner.adapter;

import com.cebbank.partner.R;
import com.cebbank.partner.bean.IncomeListBean;
import com.cebbank.partner.utils.DateTimeUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

public class PersonalDataAdapter extends BaseQuickAdapter<IncomeListBean, BaseViewHolder> {

    public PersonalDataAdapter(int layoutResId, List data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final IncomeListBean item) {

        helper.setText(R.id.tvSettlementType, item.getType());
        helper.setText(R.id.tvSunshineValue, "+" + item.getAmount());
        helper.setText(R.id.tvMessage, item.getMessage());
        helper.setText(R.id.tvSettlementTime, DateTimeUtil.stampToDate(item.getCreateDate()));

    }
}
