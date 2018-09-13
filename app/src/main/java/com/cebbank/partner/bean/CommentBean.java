package com.cebbank.partner.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

public class CommentBean implements MultiItemEntity,Cloneable {

    public static final int Comment = 1;
    public static final int Reply = 2;

    @Override
    public Object clone() throws CloneNotSupportedException
    {
        return super.clone();// 浅拷贝 step2
    }

    @Override
    public int getItemType() {
        switch (type){
            case "Comment":
                return 1;
            case "Reply":
                return 2;
        }
        return 0;
    }

    public String type;
    public String id;
    public String userId;
    public String avatar;
    public String username;
    public String content;
    public String reply;
    public String createDate;


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }
}
