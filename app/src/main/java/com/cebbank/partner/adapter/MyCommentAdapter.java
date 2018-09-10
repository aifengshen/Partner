package com.cebbank.partner.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.cebbank.partner.GlideApp;
import com.cebbank.partner.R;
import com.cebbank.partner.bean.MyCommentBean;
import com.cebbank.partner.interfaces.OnReplyListener;
import com.cebbank.partner.utils.LogUtils;
import com.cebbank.partner.utils.ToastUtils;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

public class MyCommentAdapter extends BaseMultiItemQuickAdapter<MyCommentBean, BaseViewHolder> {

    private OnReplyListener onReplyListener;

    public void setOnReplyListener(OnReplyListener onReplyListener) {
        this.onReplyListener = onReplyListener;
    }

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
                if (item.getReplyable().equals("true")) {
                    LogUtils.e("文章ID和评论人姓名", item.getId() + "===" + item.getUsername());
                    onReplyListener.reply(item.getId(), item.getUsername());
                }else{
                    ToastUtils.showShortToast("评论只能回复一次哦~");
                }
            }
        });

        switch (helper.getItemViewType()) {
            case MyCommentBean.Comment:
                helper.setText(R.id.tvName, item.getUsername());
                helper.setText(R.id.tvArticleTitle, item.getTitle());
                helper.setText(R.id.tvContent, item.getContent());
//                GlideApp.with(mContext)
//                        .load(item.getAvatar())
////                        .placeholder(R.mipmap.loading)
//                        .centerCrop()
//                        .into((ImageView) helper.getView(R.id.img));
                break;
            case MyCommentBean.Reply:
                helper.setText(R.id.tvReply, item.getReply());
                break;
        }

    }

}
