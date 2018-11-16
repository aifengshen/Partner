package com.cebbank.partner.bean;

import java.util.List;

/**
 * @ClassName: Partner
 * @Description:
 * @Author Pjw
 * @date 2018/11/15 17:39
 */
public class ArticleNewBean {

    public String id;
    public String title;
    public String author;
    public String uv;
    public String type;
    public String createDate;
    public List<ArticleBean.thumbnailList> thumbnailList;

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

    public class thumbnailList {
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
