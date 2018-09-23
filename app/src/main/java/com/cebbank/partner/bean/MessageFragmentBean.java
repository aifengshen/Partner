package com.cebbank.partner.bean;

/**
 * @ClassName: Partner
 * @Description:
 * @Author Pjw
 * @date 2018/8/19 15:41
 */
public class MessageFragmentBean {

    private String id;
    private String content;
    private String read;
    private String createDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getRead() {
        return read;
    }

    public void setRead(String read) {
        this.read = read;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }
}
