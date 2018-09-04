package com.cebbank.partner.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.cebbank.partner.BaseActivity;
import com.cebbank.partner.R;
import com.cebbank.partner.interfaces.HttpCallbackListener;
import com.cebbank.partner.utils.UrlPath;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import static com.cebbank.partner.utils.HttpUtil.sendOkHttpRequest;

public class CardDetailActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_detail);
        initView();
        initData();
        setListener();
    }

    private void initView(){
        setTitle("信用卡介绍");
        setBackBtn();
    }

    private void initData(){
        cardDetail(getIntent().getStringExtra("cardId"));
    }

    private void setListener(){

    }

    private void cardDetail(String cardId) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", cardId);
            jsonObject.put("token", "5503eb72fe764ac7843c810178763399");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        sendOkHttpRequest(this, UrlPath.CardDetail, jsonObject, null, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) throws JSONException {
                JSONObject jsonObject = new JSONObject(response);
                String code = jsonObject.optString("code");
                JSONObject jodata = jsonObject.getJSONObject("data");


                JSONArray jsonArray = jodata.getJSONArray("cardVoList");
//                Gson gson = new Gson();
//                List<CardInfoBean> cardInfoBeanList =
//                        gson.fromJson(jsonArray.toString(), new TypeToken<List<CardInfoBean>>() {
//                        }.getType());
//
//                cardList.removeAll(cardInfoBeanList);
//                CardInfoBean cardInfoBean = new CardInfoBean();
//                cardInfoBean.setType("webview");
//                cardList.add(cardInfoBean);
//                for (int i=0;i<cardInfoBeanList.size();i++){
//                    cardInfoBeanList.get(i).setType("cardinfo");
//                }
//                cardList.addAll(cardInfoBeanList);
//                mAdapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure() {

            }
        });
    }

    public static void actionStart(Context context, String cardId) {
        Intent intent = new Intent(context, CardDetailActivity.class);
        intent.putExtra("cardId", cardId);
        context.startActivity(intent);
    }
}
