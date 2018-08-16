package com.cebbank.partner.adapter;

import com.cebbank.partner.R;
import com.cebbank.partner.bean.HomeFragmentBean;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;


import java.util.List;

/**
 * @ClassName: Omnipotent
 * @Description:
 * @Author Pjw
 * @date 2018/7/31 18:06
 */
public class HomeFragmentAdapter extends BaseMultiItemQuickAdapter<HomeFragmentBean, BaseViewHolder> {


    public HomeFragmentAdapter(List data) {
        super(data);
        addItemType(HomeFragmentBean.ImageText, R.layout.fragment_home_image_text);
        addItemType(HomeFragmentBean.BigImage, R.layout.fragment_home_big_image);
        addItemType(HomeFragmentBean.ThreeImage, R.layout.fragment_home_three_image);
        addItemType(HomeFragmentBean.Video, R.layout.fragment_home_video);
    }

    @Override
    protected void convert(BaseViewHolder helper, HomeFragmentBean item) {
        switch (helper.getItemViewType()) {
//            case HomeFragmentBean.ImageText:
//                helper.setImageUrl(R.id.tv, item.getContent());
//                ((TextView) helper.getView(R.id.tvName)).setText(item.getName());
//                break;
//            case HomeFragmentBean.BigImage:
//                helper.setImageUrl(R.id.iv, item.getContent());
//                break;
//            case HomeFragmentBean.ThreeImage:
//                helper.setImageUrl(R.id.iv, item.getContent());
//                break;
//            case HomeFragmentBean.Video:
//                helper.setImageUrl(R.id.iv, item.getContent());
//                break;
        }
    }
}
