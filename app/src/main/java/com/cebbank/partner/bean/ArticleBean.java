package com.cebbank.partner.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

public class ArticleBean implements MultiItemEntity {

    public static final int ImageText = 1;
    public static final int BigImage = 2;
    public static final int ThreeImage = 3;
    public static final int Video = 4;
    private int itemType;

    public String id;
    public String title;
    public String author;
    public String uv;
    public String type;
    public String createDate;
    public List<thumbnailList> thumbnailList;

    @Override
    public int getItemType() {
        switch (type){
            case "SINGLE_SMALL":
                return 1;
            case "SINGLE_LARGE":
                return 2;
            case "TRIPLE":
                return 3;
            case "VIDEO":
                return 4;
        }
        return 0;
    }

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

    public String getUv() {
        return uv;
    }

    public void setUv(String uv) {
        this.uv = uv;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public List<ArticleBean.thumbnailList> getThumbnailList() {
        return thumbnailList;
    }

    public void setThumbnailList(List<ArticleBean.thumbnailList> thumbnailList) {
        this.thumbnailList = thumbnailList;
    }

    public class thumbnailList{
        public String id;
        public String image;
        public String sort;

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

        public String getSort() {
            return sort;
        }

        public void setSort(String sort) {
            this.sort = sort;
        }
    }


}
