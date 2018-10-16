package com.cebbank.partner.adapter;


import com.cebbank.partner.GlideApp;
import com.cebbank.partner.R;
import com.cebbank.partner.bean.RankingBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RankingAdapter extends BaseQuickAdapter<RankingBean, BaseViewHolder> {

    public RankingAdapter(int layoutResId, List data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final RankingBean item) {

        if (item.getType().equals("0")) {
            helper.setText(R.id.tvNumber, item.getSunshine());
        } else {
            helper.setText(R.id.tvNumber, item.getUv());
        }
        helper.setText(R.id.tvIndex, helper.getAdapterPosition()+"");
        helper.setText(R.id.tvName, item.getUsername());
        GlideApp.with(mContext)
                .load(item.getAvatar())
//                        .placeholder(R.mipmap.loading)
                .centerCrop()
                .into((CircleImageView) helper.getView(R.id.profile_image));

    }
}
