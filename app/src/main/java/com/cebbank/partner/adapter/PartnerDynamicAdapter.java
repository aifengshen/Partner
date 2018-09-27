package com.cebbank.partner.adapter;

import android.view.View;
import android.widget.ImageView;

import com.cebbank.partner.GlideApp;
import com.cebbank.partner.R;
import com.cebbank.partner.bean.PartnerDynamicBean;
import com.cebbank.partner.ui.ArticleDetailActivity;
import com.cebbank.partner.utils.DateTimeUtil;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

public class PartnerDynamicAdapter extends BaseMultiItemQuickAdapter<PartnerDynamicBean, BaseViewHolder> {

    public PartnerDynamicAdapter(List data) {
        super(data);
        addItemType(PartnerDynamicBean.ImageText, R.layout.fragment_home_image_text);
        addItemType(PartnerDynamicBean.BigImage, R.layout.fragment_home_big_image);
        addItemType(PartnerDynamicBean.ThreeImage, R.layout.fragment_home_three_image);
        addItemType(PartnerDynamicBean.Video, R.layout.fragment_home_video);
    }

    @Override
    protected void convert(BaseViewHolder helper, final PartnerDynamicBean item) {
        switch (helper.getItemViewType()) {
            case PartnerDynamicBean.ImageText:
                GlideApp.with(mContext)
                        .load(item.getThumbnailList().get(0).getImage())
//                        .placeholder(R.mipmap.loading)
                        .centerCrop()
                        .into((ImageView) helper.getView(R.id.img));
                helper.setText(R.id.tvTitle, item.getTitle());
                helper.setText(R.id.tvFrom, item.getAuthor());
                helper.setText(R.id.tvUV, "浏览量:" + item.getUv());
                helper.setText(R.id.tvDate, DateTimeUtil.stampToDate(item.getCreateDate()));
                break;
            case PartnerDynamicBean.BigImage:
                GlideApp.with(mContext)
                        .load(item.getThumbnailList().get(0).getImage())
//                        .placeholder(R.mipmap.loading)
                        .centerCrop()
                        .into((ImageView) helper.getView(R.id.img));
                helper.setText(R.id.tvTitle, item.getTitle());
                helper.setText(R.id.tvFrom, item.getAuthor());
                helper.setText(R.id.tvUV, "浏览量:" + item.getUv());
                helper.setText(R.id.tvDate, DateTimeUtil.stampToDate(item.getCreateDate()));
                break;
            case PartnerDynamicBean.ThreeImage:
                GlideApp.with(mContext)
                        .load(item.getThumbnailList().get(0).getImage())
//                        .placeholder(R.mipmap.loading)
                        .centerCrop()
                        .into((ImageView) helper.getView(R.id.img1));
                GlideApp.with(mContext)
                        .load(item.getThumbnailList().get(1).getImage())
//                        .placeholder(R.mipmap.loading)
                        .centerCrop()
                        .into((ImageView) helper.getView(R.id.img2));
                GlideApp.with(mContext)
                        .load(item.getThumbnailList().get(2).getImage())
//                        .placeholder(R.mipmap.loading)
                        .centerCrop()
                        .into((ImageView) helper.getView(R.id.img3));
                helper.setText(R.id.tvTitle, item.getTitle());
                helper.setText(R.id.tvFrom, item.getAuthor());
                helper.setText(R.id.tvUV, "浏览量:" + item.getUv());
                helper.setText(R.id.tvDate, DateTimeUtil.stampToDate(item.getCreateDate()));
                break;
            case PartnerDynamicBean.Video:
                GlideApp.with(mContext)
                        .load(item.getThumbnailList().get(0).getImage())
//                        .placeholder(R.mipmap.loading)
                        .centerCrop()
                        .into((ImageView) helper.getView(R.id.img));
                helper.setText(R.id.tvTitle, item.getTitle());
                helper.setText(R.id.tvFrom, item.getAuthor());
                helper.setText(R.id.tvUV, "浏览量:" + item.getUv());
                helper.setText(R.id.tvDate, DateTimeUtil.stampToDate(item.getCreateDate()));
                break;
        }

        helper.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArticleDetailActivity.actionStart(mContext, item.getId());
            }
        });

    }

}
