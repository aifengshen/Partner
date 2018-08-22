package com.cebbank.partner.adapter;

import android.view.View;

import com.cebbank.partner.R;
import com.cebbank.partner.bean.MessageFragmentBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * @ClassName: Partner
 * @Description:
 * @Author Pjw
 * @date 2018/8/19 15:41
 */
public class MessageFragmentAdapter extends BaseQuickAdapter<MessageFragmentBean, BaseViewHolder> {

    public MessageFragmentAdapter(int layoutResId, List data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final MessageFragmentBean item) {

        helper.setText(R.id.tvTitle, item.getTitle());


        helper.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
}
