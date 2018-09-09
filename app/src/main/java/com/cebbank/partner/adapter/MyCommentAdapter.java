package com.cebbank.partner.adapter;

import android.view.View;
import android.widget.ImageView;

import com.cebbank.partner.GlideApp;
import com.cebbank.partner.R;
import com.cebbank.partner.bean.MyCommentBean;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

public class MyCommentAdapter extends BaseMultiItemQuickAdapter<MyCommentBean, BaseViewHolder> {

    public MyCommentAdapter(List data) {
        super(data);
        addItemType(MyCommentBean.Comment, R.layout.activity_my_commet_item);
        addItemType(MyCommentBean.Reply, R.layout.activity_reply_item);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final MyCommentBean item) {

        helper.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        switch (helper.getItemViewType()) {
            case MyCommentBean.Comment:
                helper.setText(R.id.tvName,item.getUsername());
                helper.setText(R.id.tvDate,item.getCreateDate());
                helper.setText(R.id.tvContent,item.getContent());
                GlideApp.with(mContext)
                        .load(item.getAvatar())
//                        .placeholder(R.mipmap.loading)
                        .centerCrop()
                        .into((ImageView) helper.getView(R.id.img));
                break;
            case MyCommentBean.Reply:
                helper.setText(R.id.tvReply,item.getContent());
                break;
        }

    }

}
