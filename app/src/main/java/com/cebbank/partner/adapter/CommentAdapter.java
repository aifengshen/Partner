package com.cebbank.partner.adapter;

import android.view.View;

import com.cebbank.partner.R;
import com.cebbank.partner.bean.CommentBean;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;


import java.util.List;

public class CommentAdapter extends BaseMultiItemQuickAdapter<CommentBean, BaseViewHolder> {


    public CommentAdapter(List data) {
        super(data);
        addItemType(CommentBean.Comment, R.layout.activity_commet_item);
        addItemType(CommentBean.Reply, R.layout.activity_reply_item);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final CommentBean item) {

        helper.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        switch (helper.getItemViewType()) {
            case CommentBean.Comment:

                break;
            case CommentBean.Reply:

                break;

        }

    }

}
