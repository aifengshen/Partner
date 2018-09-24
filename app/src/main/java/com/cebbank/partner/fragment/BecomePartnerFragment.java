package com.cebbank.partner.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.cebbank.partner.MyApplication;
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
public class BecomePartnerFragment extends Fragment implements View.OnClickListener {

    private View view;
    private TextView tvSend;
    private EditText edtvName, edtvNumber, edtvPhone, edtvCode;
    private String Code = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = LayoutInflater.from(getActivity()).inflate(R.layout.activity_become_partner, container, false);
        initView();
        initData();
        setClickListener();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        applyIndex();
    }

    private void initView() {
        edtvName = view.findViewById(R.id.edtvName);
        edtvNumber = view.findViewById(R.id.edtvNumber);
        edtvPhone = view.findViewById(R.id.edtvPhone);
        edtvCode = view.findViewById(R.id.edtvCode);
        tvSend = view.findViewById(R.id.tvSend);
        tvSend.setSelected(false);
    }

    private void initData() {

    }

    private void setClickListener() {
        view.findViewById(R.id.tvNext).setOnClickListener(this);
        tvSend.setOnClickListener(this);
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
                tvSend.setEnabled(false);
                tvSend.setSelected(true);
                new CountDownTimer(60000, 1000) { // 30秒倒计时，没秒执行一次
                    public void onTick(long millisUntilFinished) { // 每秒执行一次进该方法
                        tvSend.setText(millisUntilFinished / 1000 + "秒后重发");
                    }

                    public void onFinish() { // 倒计时结束。
                        tvSend.setEnabled(true);
                        tvSend.setSelected(false);
                        tvSend.setText("获取验证码");
                    }
                }.start();
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
                if (number.length() != 18) {
                    ToastUtils.showShortToast("身份证号填写不正确");
                    return;
                }
                if (phone.length() != 11) {
                    ToastUtils.showShortToast("手机号码填写不正确");
                    return;
                }
                if (code.length() != 4 || !code.equals(Code)) {
                    ToastUtils.showShortToast("验证码填写不正确");
                    return;
                }
                UpLoadIDActivity.actionStart(getActivity(), name, phone, code, number);

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
                JSONObject jo = new JSONObject(response);
                Code = jo.optString("data");
            }

            @Override
            public void onFailure() {

            }
        });
    }

    private void applyIndex() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("token", MyApplication.getToken());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        sendOkHttpRequest(getActivity(), UrlPath.ApplyIndex, jsonObject, null, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) throws JSONException {
                JSONObject jo = new JSONObject(response);
                JSONObject data = jo.getJSONObject("data");
                edtvName.setText(data.optString("username"));
                edtvNumber.setText(data.optString("idCard"));
                edtvPhone.setText(data.optString("phone"));
                edtvName.setText(data.optString("username"));
                if (data.optString("checkStatus").equals("PASS")) {
                    showdialog();
                }
            }

            @Override
            public void onFailure() {

            }
        });
    }

    private void showdialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("温馨提示");
        builder.setMessage("您已成为合伙人,重复提交需要重新审核!");
        builder.setNegativeButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }


}
