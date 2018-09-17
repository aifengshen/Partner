package com.cebbank.partner.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

public class CheckingProgressBean implements MultiItemEntity {

    public static final int Fodder = 1;
    public static final int Withdraw = 2;

    private int itemType;

    public String type;

    public String articleId;
    public String title;
    public String status;
    public String reason;
    public String createDate;

    public String amount;
    public String withdrawId;



    @Override
    public int getItemType() {
        switch (type){
            case "fodder":
                return Fodder;
            case "withdraw":
                return Withdraw;
        }
        return 0;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getWithdrawId() {
        return withdrawId;
    }

    public void setWithdrawId(String withdrawId) {
        this.withdrawId = withdrawId;
    }
}
