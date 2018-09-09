package com.cebbank.partner.ui;

import android.content.Context;
import android.content.res.AssetManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.cebbank.partner.BaseActivity;
import com.cebbank.partner.R;
import com.cebbank.partner.adapter.ContactsAdapter;
import com.cebbank.partner.bean.Contact;
import com.cebbank.partner.utils.LogUtils;
import com.gjiazhe.wavesidebar.WaveSideBar;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class CityActivity extends BaseActivity {

    private WaveSideBar sideBar;
    private RecyclerView rvContacts;
    private ArrayList<Contact> contacts = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);
        initView();
        initData();
        setListener();
    }

    private void initView(){
        setTitle("城市选择");
        setBackBtn();
        rvContacts = findViewById(R.id.rv_contacts);
        rvContacts.setLayoutManager(new LinearLayoutManager(this));
        rvContacts.setAdapter(new ContactsAdapter(contacts, R.layout.item_contacts,this));
        sideBar = findViewById(R.id.side_bar);
        rvContacts.setRecyclerListener(new RecyclerView.RecyclerListener() {
            @Override
            public void onViewRecycled(RecyclerView.ViewHolder holder) {
                int position = holder.getAdapterPosition();
                LogUtils.e(position+"哈哈哈");
            }
        });
    }

    private void initData(){
//        sideBar.setDataResource(data);
        contacts.addAll(Contact.getChineseContacts());
    }

    private void setListener(){
        sideBar.setOnSelectIndexItemListener(new WaveSideBar.OnSelectIndexItemListener() {
            @Override
            public void onSelectIndexItem(String index) {
                Log.d("WaveSideBar", index);
                for (int i=0; i<contacts.size(); i++) {
                    if (contacts.get(i).getIndex().equals(index)) {
                        ((LinearLayoutManager) rvContacts.getLayoutManager()).scrollToPositionWithOffset(i, 0);
                        return;
                    }
                }
            }
        });



    }


}
