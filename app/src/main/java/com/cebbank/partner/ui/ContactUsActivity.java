package com.cebbank.partner.ui;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.cebbank.partner.BaseActivity;
import com.cebbank.partner.GlideApp;
import com.cebbank.partner.MyApplication;
import com.cebbank.partner.R;
import com.cebbank.partner.interfaces.HttpCallbackListener;
import com.cebbank.partner.utils.UrlPath;

import org.json.JSONException;
import org.json.JSONObject;

import static com.cebbank.partner.utils.HttpUtil.sendOkHttpRequest;

public class ContactUsActivity extends BaseActivity {

    private ImageView img;
    private TextView tvPhone, tvMail, tvTechnicalSupport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        setTitle("联系我们");
        setBackBtn();
        img = findViewById(R.id.img);
        tvPhone = findViewById(R.id.tvPhone);
        tvMail = findViewById(R.id.tvMail);
        tvTechnicalSupport = findViewById(R.id.tvTechnicalSupport);
        detail();
    }

    private void detail() {
        JSONObject jsonObject = new JSONObject();
//        try {
//            jsonObject.put("token", MyApplication.getToken());
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

        sendOkHttpRequest(this, UrlPath.ContactDetail, jsonObject, null, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) throws JSONException {
                JSONObject jsonObject = new JSONObject(response);
                JSONObject data = jsonObject.getJSONObject("data");
                String image = data.optString("image");
                String phone = data.optString("phone");
                String email = data.optString("email");
                String support = data.optString("support");

                tvPhone.setText("电话:  " + phone);
                tvMail.setText("邮箱:  " + email);
                tvTechnicalSupport.setText("技术支持:  " + support);
                GlideApp.with(ContactUsActivity.this)
                        .load(image)
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
