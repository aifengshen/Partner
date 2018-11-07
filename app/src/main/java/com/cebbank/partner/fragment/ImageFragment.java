package com.cebbank.partner.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.cebbank.partner.GlideApp;
import com.cebbank.partner.R;
import com.cebbank.partner.bean.ImageBean;
import com.cebbank.partner.interfaces.HttpCallbackListener;
import com.cebbank.partner.utils.UrlPath;

import org.json.JSONException;
import org.json.JSONObject;

import static com.cebbank.partner.utils.HttpUtil.sendOkHttpRequest;

/**
 * @ClassName: Partner
 * @Description:
 * @Author Pjw
 * @date 2018/9/29 17:02
 */
public class ImageFragment extends Fragment {

    private ImageView img;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image, null);
        initView(view);
        initData();
        return view;
    }

    private void initView(View view) {
        img = view.findViewById(R.id.img);
    }

    private void initData() {
        requestImageUrl();
    }

    private void requestImageUrl() {
        JSONObject jsonObject = new JSONObject();
        sendOkHttpRequest(getActivity(), UrlPath.PageCardindex, jsonObject, null, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) throws JSONException {
                JSONObject jsonObject = new JSONObject(response);
                String code = jsonObject.optString("code");
                JSONObject jo = jsonObject.getJSONObject("data");
                ImageBean imageBean = new ImageBean();
                imageBean.setDate(jo.optString("date"));
                imageBean.setImage(jo.optString("image"));
                GlideApp.with(getActivity())
                        .load(imageBean.getImage())
//                        .placeholder(R.mipmap.loading)
                        .centerCrop()
                        .into(img);


            }

            @Override
            public void onFailure() {

            }
        });
    }
}
