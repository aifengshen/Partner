package com.cebbank.partner.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.cebbank.partner.BaseActivity;
import com.cebbank.partner.MyApplication;
import com.cebbank.partner.R;
import com.cebbank.partner.interfaces.HttpCallbackListener;
import com.cebbank.partner.utils.ToastUtils;
import com.cebbank.partner.utils.UrlPath;

import org.json.JSONException;
import org.json.JSONObject;

import static com.cebbank.partner.utils.HttpUtil.sendOkHttpRequest;

public class OpinionActivity extends BaseActivity {

    private EditText etOpinion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opinion);
        initView();
    }

    private void initView() {
        setTitle("意见反馈");
        setBackBtn();
        etOpinion = findViewById(R.id.etOpinion);
        findViewById(R.id.tvSubmit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String opinion = etOpinion.getText().toString();
                if (!TextUtils.isEmpty(opinion)) {
                    submit(opinion);
                }
            }
        });
    }

    private void submit(String opinion) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("token", MyApplication.getToken());
            jsonObject.put("content", opinion);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        sendOkHttpRequest(this, UrlPath.OpinionSubmit, jsonObject, null, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) throws JSONException {
                JSONObject jsonObject = new JSONObject(response);
                ToastUtils.showShortToast("提交成功");
            }

            @Override
            public void onFailure() {

            }
        });
    }

}
