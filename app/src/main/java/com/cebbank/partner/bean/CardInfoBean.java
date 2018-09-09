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
    public String webview_id;
    public String webview_title;
    public String webview_author;
    public String webview_createDate;
    public String webview_content;
    public String webview_authorId;
    public String webview_avatar;
    public String webview_attention;
    public String webview_like;
    public String webview_comment;
    public String webview_forward;
    public String webview_collection;


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

    public String getWebview_id() {
        return webview_id;
    }

    public void setWebview_id(String webview_id) {
        this.webview_id = webview_id;
    }

    public String getWebview_title() {
        return webview_title;
    }

    public void setWebview_title(String webview_title) {
        this.webview_title = webview_title;
    }

    public String getWebview_author() {
        return webview_author;
    }

    public void setWebview_author(String webview_author) {
        this.webview_author = webview_author;
    }

    public String getWebview_createDate() {
        return webview_createDate;
    }

    public void setWebview_createDate(String webview_createDate) {
        this.webview_createDate = webview_createDate;
    }

    public String getWebview_content() {
        return webview_content;
    }

    public void setWebview_content(String webview_content) {
        this.webview_content = webview_content;
    }

    public String getWebview_authorId() {
        return webview_authorId;
    }

    public void setWebview_authorId(String webview_authorId) {
        this.webview_authorId = webview_authorId;
    }

    public String getWebview_avatar() {
        return webview_avatar;
    }

    public void setWebview_avatar(String webview_avatar) {
        this.webview_avatar = webview_avatar;
    }

    public String getWebview_attention() {
        return webview_attention;
    }

    public void setWebview_attention(String webview_attention) {
        this.webview_attention = webview_attention;
    }

    public String getWebview_like() {
        return webview_like;
    }

    public void setWebview_like(String webview_like) {
        this.webview_like = webview_like;
    }

    public String getWebview_comment() {
        return webview_comment;
    }

    public void setWebview_comment(String webview_comment) {
        this.webview_comment = webview_comment;
    }

    public String getWebview_forward() {
        return webview_forward;
    }

    public void setWebview_forward(String webview_forward) {
        this.webview_forward = webview_forward;
    }

    public String getWebview_collection() {
        return webview_collection;
    }

    public void setWebview_collection(String webview_collection) {
        this.webview_collection = webview_collection;
    }
}
