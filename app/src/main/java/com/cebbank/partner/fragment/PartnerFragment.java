package com.cebbank.partner.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.cebbank.partner.R;
import com.cebbank.partner.interfaces.HttpCallbackListener;
import com.cebbank.partner.ui.UpLoadIDActivity;
import com.cebbank.partner.utils.ToastUtils;
import com.cebbank.partner.utils.UrlPath;

import org.json.JSONException;
import org.json.JSONObject;

import static com.cebbank.partner.utils.HttpUtil.sendOkHttpRequest;


/**
 * 成为合伙人fragment
 */
public class PartnerFragment extends Fragment implements View.OnClickListener {

    private View view;
    private EditText edtvName, edtvNumber, edtvPhone, edtvCode;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = LayoutInflater.from(getActivity()).inflate(R.layout.activity_become_partner, container, false);
        initView();
        initData();
        setClickListener();
        return view;
    }

    private void initView() {
        edtvName = view.findViewById(R.id.edtvName);
        edtvNumber = view.findViewById(R.id.edtvNumber);
        edtvPhone = view.findViewById(R.id.edtvPhone);
        edtvCode = view.findViewById(R.id.edtvCode);
    }

    private void initData() {

    }

    private void setClickListener() {
        view.findViewById(R.id.tvNext).setOnClickListener(this);
        view.findViewById(R.id.tvSend).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvSend:
                String phone1 = edtvPhone.getText().toString();
                if (TextUtils.isEmpty(phone1)) {
                    ToastUtils.showShortToast("请填写手机号");
                    return;
                }
                verificationCode(phone1);
                break;
            case R.id.tvNext:
                /**
                 * 下一步
                 */
                String name = edtvName.getText().toString();
                String number = edtvNumber.getText().toString();
                String phone = edtvPhone.getText().toString();
                String code = edtvCode.getText().toString();
                if (TextUtils.isEmpty(name)) {
                    ToastUtils.showShortToast("请填写姓名");
                    return;
                }
                if (TextUtils.isEmpty(number)) {
                    ToastUtils.showShortToast("请填写证件号码");
                    return;
                }
                if (TextUtils.isEmpty(phone)) {
                    ToastUtils.showShortToast("请填写手机号");
                    return;
                }
                if (TextUtils.isEmpty(code)) {
                    ToastUtils.showShortToast("请填写验证码");
                    return;
                }
                UpLoadIDActivity.actionStart(getActivity(), name, number, phone, code);

                break;
        }
    }

    private void verificationCode(String phone) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("phone", phone);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        sendOkHttpRequest(getActivity(), UrlPath.GetCode, jsonObject, null, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) throws JSONException {

            }

            @Override
            public void onFailure() {

            }
        });
    }


}
