package com.cebbank.partner.adapter;

import android.content.Intent;
import android.view.View;

import com.cebbank.partner.R;
import com.cebbank.partner.bean.OfficialCourseBean;
import com.cebbank.partner.ui.OfficialCourseDetailActivity;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

public class OfficialCourseAdapter extends BaseQuickAdapter<OfficialCourseBean, BaseViewHolder> {

    private List<OfficialCourseBean> data;

    public OfficialCourseAdapter(int layoutResId, List data) {
        super(layoutResId, data);
        this.data = data;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final OfficialCourseBean item) {

        helper.setText(R.id.tvTitle, item.getTitle());

        helper.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, OfficialCourseDetailActivity.class);
                intent.putExtra("id", item.getId());
                mContext.startActivity(intent);
            }
        });

    }
}
