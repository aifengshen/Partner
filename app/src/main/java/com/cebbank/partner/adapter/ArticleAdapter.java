package com.cebbank.partner.adapter;

import android.view.View;
import android.widget.ImageView;

import com.cebbank.partner.GlideApp;
import com.cebbank.partner.R;
import com.cebbank.partner.bean.ArticleBean;
import com.cebbank.partner.bean.HomeFragmentBean;
import com.cebbank.partner.ui.ArtcleDetailActivity;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;


import java.util.List;

/**
 * @ClassName: Omnipotent
 * @Description:
 * @Author Pjw
 * @date 2018/7/31 18:06
 */
public class ArticleAdapter extends BaseMultiItemQuickAdapter<ArticleBean, BaseViewHolder> {


    public ArticleAdapter(List data) {
        super(data);
        addItemType(HomeFragmentBean.ImageText, R.layout.fragment_home_image_text);
        addItemType(HomeFragmentBean.BigImage, R.layout.fragment_home_big_image);
        addItemType(HomeFragmentBean.ThreeImage, R.layout.fragment_home_three_image);
        addItemType(HomeFragmentBean.Video, R.layout.fragment_home_video);
    }

    @Override
    protected void convert(BaseViewHolder helper, final ArticleBean item) {

        helper.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArtcleDetailActivity.actionStart(mContext,item.getId());
            }
        });

        switch (helper.getItemViewType()) {
            case ArticleBean.ImageText:
                if (item.getThumbnailList().size() == 1) {
                    GlideApp.with(mContext)
                            .load(item.getThumbnailList().get(0).getImage())
//                        .placeholder(R.mipmap.loading)
                            .centerCrop()
                            .into((ImageView) helper.getView(R.id.img));
                }

                helper.setText(R.id.tvTitle, item.getTitle());
                helper.setText(R.id.tvFrom, item.getAuthor());
                helper.setText(R.id.tvDate, item.getCreateDate());
                break;
            case ArticleBean.BigImage:
                if (item.getThumbnailList().size() == 1) {
                    GlideApp.with(mContext)
                            .load(item.getThumbnailList().get(0).getImage())
//                        .placeholder(R.mipmap.loading)
                            .centerCrop()
                            .into((ImageView) helper.getView(R.id.img));
                }

                helper.setText(R.id.tvTitle, item.getTitle());
                helper.setText(R.id.tvFrom, item.getAuthor());
                helper.setText(R.id.tvDate, item.getCreateDate());
                break;
            case ArticleBean.ThreeImage:
                if (item.getThumbnailList().size() == 3) {
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
                }

                helper.setText(R.id.tvTitle, item.getTitle());
                helper.setText(R.id.tvFrom, item.getAuthor());
                helper.setText(R.id.tvDate, item.getCreateDate());
                break;
            case ArticleBean.Video:
                if (item.getThumbnailList().size() == 1) {
                    GlideApp.with(mContext)
                            .load(item.getThumbnailList().get(0).getImage())
//                        .placeholder(R.mipmap.loading)
                            .centerCrop()
                            .into((ImageView) helper.getView(R.id.img));
                }

                helper.setText(R.id.tvTitle, item.getTitle());
                helper.setText(R.id.tvFrom, item.getAuthor());
                helper.setText(R.id.tvDate, item.getCreateDate());
                break;
        }

    }
}
