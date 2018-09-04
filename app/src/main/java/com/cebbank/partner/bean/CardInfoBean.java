package com.cebbank.partner.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

public class CardInfoBean implements MultiItemEntity {

    public static final int WebView = 1;
    public static final int Card = 2;

    @Override
    public int getItemType() {
        switch (type){
            case "webview":
                return 1;
            case "cardinfo":
                return 2;
        }
        return 0;
    }


    public String id;
    public String image;
    public String name;
    public String type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
