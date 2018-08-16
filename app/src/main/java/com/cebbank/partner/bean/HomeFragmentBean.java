package com.cebbank.partner.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * @ClassName: Omnipotent
 * @Description:
 * @Author Pjw
 * @date 2018/7/31 18:07
 */
public class HomeFragmentBean implements MultiItemEntity {

    private String name;
    public static final int ImageText = 1;
    public static final int BigImage = 2;
    public static final int ThreeImage = 3;
    public static final int Video = 4;
    private int itemType;

    public HomeFragmentBean(int itemType) {
        this.itemType = itemType;
    }

    @Override
    public int getItemType() {
        return itemType;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
