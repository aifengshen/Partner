package com.cebbank.partner.adapter;

import android.view.View;
import android.widget.ImageView;

import com.cebbank.partner.GlideApp;
import com.cebbank.partner.MainActivity;
import com.cebbank.partner.MyApplication;
import com.cebbank.partner.R;
import com.cebbank.partner.bean.AttentionPartnerBean;
import com.cebbank.partner.interfaces.HttpCallbackListener;
import com.cebbank.partner.utils.ToastUtils;
import com.cebbank.partner.utils.UrlPath;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import static com.cebbank.partner.utils.HttpUtil.sendOkHttpRequest;

public class AttentionPartnerAdapter extends BaseQuickAdapter<AttentionPartnerBean, BaseViewHolder> {

    private List<AttentionPartnerBean> data;

    public AttentionPartnerAdapter(int layoutResId, List data) {
        super(layoutResId, data);
        this.data = data;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final AttentionPartnerBean item) {

        helper.setText(R.id.tvName, item.getUsername());
        helper.setText(R.id.tvContent, item.getSignature());
        GlideApp.with(mContext)
                .load(item.getAvatar())
//                        .placeholder(R.mipmap.loading)
                .centerCrop()
                .into((ImageView) helper.getView(R.id.profile_image));

        helper.getView(R.id.tvAttention).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * 取消关注
                 */
                cancel(item, helper);
            }
        });

    }

    private void cancel(final AttentionPartnerBean item, final BaseViewHolder helper) {
        String url = "";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("idolId", item.getId());
            jsonObject.put("token", MyApplication.getToken());

            if (item.isCancel()) {
                url = UrlPath.Cancel;
            } else {
                url = UrlPath.Concern;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        sendOkHttpRequest((MainActivity) mContext, url, jsonObject, null, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) throws JSONException {
                JSONObject jsonObject = new JSONObject(response);
                if (item.isCancel()) {
                    item.setCancel(false);
                    ToastUtils.showShortToast("取关成功");
                    helper.setText(R.id.tvAttention, "关注");
                } else {
                    item.setCancel(true);
                    ToastUtils.showShortToast("关注成功");
                    helper.setText(R.id.tvAttention, "取消关注");
                }

            }

            @Override
            public void onFailure() {

            }
        });
    }
}
