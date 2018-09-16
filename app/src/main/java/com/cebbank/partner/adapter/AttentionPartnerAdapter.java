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
                cancel(item.isCancel(), item.getId(), getParentPosition(item));
            }
        });

    }

    private void cancel(boolean isCancel, String idolId, final int position) {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("idolId", idolId);
            jsonObject.put("token", MyApplication.getToken());
            if (isCancel) {

            } else {

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        sendOkHttpRequest((MainActivity) mContext, UrlPath.Cancel, jsonObject, null, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) throws JSONException {
                JSONObject jsonObject = new JSONObject(response);
//                String code = jsonObject.optString("code");
//                JSONArray jsonArray = jsonObject.getJSONArray("data");
//                Gson gson = new Gson();
                ToastUtils.showShortToast("取关成功");
//                data.get(position).setCancel(false);

//                notifyDataSetChanged();
            }

            @Override
            public void onFailure() {

            }
        });
    }
}
