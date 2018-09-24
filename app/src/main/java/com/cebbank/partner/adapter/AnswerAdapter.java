package com.cebbank.partner.adapter;

import com.cebbank.partner.R;
import com.cebbank.partner.bean.AnswerBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

public class AnswerAdapter extends BaseQuickAdapter<AnswerBean, BaseViewHolder> {

    private List<AnswerBean> data;

    public AnswerAdapter(int layoutResId, List data) {
        super(layoutResId, data);
        this.data = data;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final AnswerBean item) {

        helper.setText(R.id.tvQuestion, "Q:" + item.getQuestion());
        helper.setText(R.id.tvAnswer, item.getAnswer());

    }
}
