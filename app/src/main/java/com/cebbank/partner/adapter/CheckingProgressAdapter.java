package com.cebbank.partner.adapter;

import android.view.View;
import android.widget.ImageView;

import com.cebbank.partner.GlideApp;
import com.cebbank.partner.R;
import com.cebbank.partner.bean.ArticleBean;
import com.cebbank.partner.bean.CheckingProgressBean;
import com.cebbank.partner.bean.HomeFragmentBean;
import com.cebbank.partner.ui.ArticleDetailActivity;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

public class CheckingProgressAdapter extends BaseMultiItemQuickAdapter<CheckingProgressBean, BaseViewHolder> {

    public CheckingProgressAdapter(List data) {
        super(data);
        addItemType(CheckingProgressBean.Fodder, R.layout.activity_checking_progress_fodder_item);
        addItemType(CheckingProgressBean.Withdraw, R.layout.activity_checking_progress_withdraw_item);
    }

    @Override
    protected void convert(BaseViewHolder helper, final CheckingProgressBean item) {

        helper.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        switch (helper.getItemViewType()) {
            case CheckingProgressBean.Fodder:
                helper.setText(R.id.tvTitle, item.getTitle());
                helper.setText(R.id.tvDate, "提交时间:"+item.getCreateDate());
                switch (item.getStatus()){
                    case "UNDER":
                        helper.getView(R.id.tvChecking).setVisibility(View.VISIBLE);
                        break;
                    case "PASS":
                        helper.getView(R.id.tvPass).setVisibility(View.VISIBLE);
                        break;
                    case "REFUSE":
                        helper.getView(R.id.tvRefuse).setVisibility(View.VISIBLE);
                        helper.getView(R.id.tvReason).setVisibility(View.VISIBLE);
                        helper.setText(R.id.tvReason, item.getReason());
                        break;
                }
                break;
            case CheckingProgressBean.Withdraw:
                switch (item.getStatus()){
                    case "UNDER":
                        helper.getView(R.id.tvChecking).setVisibility(View.VISIBLE);
                        break;
                    case "PASS":
                        helper.getView(R.id.tvPass).setVisibility(View.VISIBLE);
                        break;
                    case "REFUSE":
                        helper.getView(R.id.tvRefuse).setVisibility(View.VISIBLE);
                        helper.getView(R.id.tvReason).setVisibility(View.VISIBLE);
                        helper.setText(R.id.tvReason, item.getReason());
                        break;
                }
                helper.setText(R.id.tvMoney, item.getAmount()+"阳光币");
//                helper.setText(R.id.tvUserName, item.getAuthor());
                helper.setText(R.id.tvUserAccount, item.getWithdrawId());
//                helper.setText(R.id.tvOpenAccountBank, item.getCreateDate());
                helper.setText(R.id.tvSubmitDate, item.getCreateDate());

                break;
        }

    }
}
