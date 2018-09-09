package com.cebbank.partner.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * @ClassName: Partner
 * @Description:
 * @Author Pjw
 * @date 2018/9/8 18:57
 */
public class ArticleCommentBean implements MultiItemEntity {

    public static final int Comment = 1;
    public static final int Reply = 2;

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
    public String avatar;
    public String username;
    public String userId;
    public String title;
    public String content;
    public String createDate;
    public String reply;
    public String replyDate;
    public String replyable;

    public static int getComment() {
        return Comment;
    }

    public static int getReply() {
        return Reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public String getReplyDate() {
        return replyDate;
    }

    public void setReplyDate(String replyDate) {
        this.replyDate = replyDate;
    }

    public String getReplyable() {
        return replyable;
    }

    public void setReplyable(String replyable) {
        this.replyable = replyable;
    }

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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }
}
