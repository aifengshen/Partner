package com.cebbank.partner.bean;


import java.util.List;

public class ArtcleDetailBean {

    public String id;
    public String title;
    public String author;
    public String createDate;
    public String content;
    public String authorId;
    public String avatar;
    public String attention;
    public String like;
    public String comment;
    public String forward;
    public String collection;
    public List<CardInfoBean> cardVoList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getAttention() {
        return attention;
    }

    public void setAttention(String attention) {
        this.attention = attention;
    }

    public String getLike() {
        return like;
    }

    public void setLike(String like) {
        this.like = like;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getForward() {
        return forward;
    }

    public void setForward(String forward) {
        this.forward = forward;
    }

    public String getCollection() {
        return collection;
    }

    public void setCollection(String collection) {
        this.collection = collection;
    }

    public List<CardInfoBean> getCardVoList() {
        return cardVoList;
    }

    public void setCardVoList(List<CardInfoBean> cardVoList) {
        this.cardVoList = cardVoList;
    }

    //    public List<ArtcleDetailBean.cardVoList> getCardVoList() {
//        return cardVoList;
//    }
//
//    public void setCardVoList(List<ArtcleDetailBean.cardVoList> cardVoList) {
//        this.cardVoList = cardVoList;
//    }

//    public static class cardVoList implements MultiItemEntity {
//
//        @Override
//        public int getItemType() {
//
//            return 0;
//        }
//
//
//        public String id;
//        public String image;
//        public String name;
//
//        public String getId() {
//            return id;
//        }
//
//        public void setId(String id) {
//            this.id = id;
//        }
//
//        public String getImage() {
//            return image;
//        }
//
//        public void setImage(String image) {
//            this.image = image;
//        }
//
//        public String getName() {
//            return name;
//        }
//
//        public void setName(String name) {
//            this.name = name;
//        }
//    }
}
