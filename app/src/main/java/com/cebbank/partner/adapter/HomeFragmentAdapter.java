package com.cebbank.partner.adapter;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.cebbank.partner.R;
import com.cebbank.partner.bean.HomeFragmentBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;


import java.util.List;

/**
 * @ClassName: Omnipotent
 * @Description:
 * @Author Pjw
 * @date 2018/7/31 18:06
 */
public class HomeFragmentAdapter extends BaseQuickAdapter<HomeFragmentBean, BaseViewHolder> {

    public HomeFragmentAdapter(int layoutResId, List<HomeFragmentBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, HomeFragmentBean item) {
        ((TextView) helper.getView(R.id.tvName)).setText(item.getName());
        helper.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
