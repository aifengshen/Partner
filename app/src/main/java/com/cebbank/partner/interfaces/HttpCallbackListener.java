package com.cebbank.partner.interfaces;

import org.json.JSONException;

public interface HttpCallbackListener {

    void onFinish(String response) throws JSONException;

    void onFailure();
}
