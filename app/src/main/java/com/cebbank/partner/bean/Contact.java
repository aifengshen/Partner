package com.cebbank.partner.bean;

import android.content.Context;
import android.content.res.AssetManager;
import android.text.TextUtils;

import com.cebbank.partner.MyApplication;
import com.cebbank.partner.utils.ChineseCharToEn;
import com.cebbank.partner.utils.LogUtils;
import com.cebbank.partner.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Contact {

    private String index;
    private String name;
    private String code;

    public Contact(String index, String name, String code) {
        this.index = index;
        this.name = name;
        this.code = code;
    }

    public String getIndex() {
        return index;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public static List<Contact> getChineseContacts() {
        List<Contact> contacts = new ArrayList<>();
        String jo = getJson("citydata.json", MyApplication.getContext());
        try {
            JSONArray jsonArray = new JSONArray(jo);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                JSONArray jsonArray1 = jsonObject.getJSONArray("children");
                for (int j = 0; j < jsonArray1.length(); j++) {
                    JSONObject jsonObject1 = jsonArray1.getJSONObject(j);
                    String name = jsonObject1.optString("name");
                    String code = jsonObject1.optString("code");
                    contacts.add(new Contact(ChineseCharToEn.getFirstLetter(name.substring(0, 1)).toUpperCase(), name, code));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return bubbleSort(contacts);
    }

    public static String getJson(String fileName, Context context) {
        //将json数据变成字符串
        StringBuilder stringBuilder = new StringBuilder();
        try {
            //获取assets资源管理器
            AssetManager assetManager = context.getAssets();
            //通过管理器打开文件并读取
            BufferedReader bf = new BufferedReader(new InputStreamReader(
                    assetManager.open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    public static List<Contact> bubbleSort(List<Contact> contacts) {
        Comparator<Contact> comparator = new Comparator<Contact>() {
            public int compare(Contact s1, Contact s2) {
                if (!TextUtils.isEmpty(s1.getIndex()) && !TextUtils.isEmpty(s2.getIndex())) {
                    if (s1.getIndex() != s2.getIndex()) {
                        return s1.getIndex().compareTo(s2.getIndex());
                    }
                }
                return 0;
            }
        };
        Collections.sort(contacts, comparator);
        return contacts;
    }

}
