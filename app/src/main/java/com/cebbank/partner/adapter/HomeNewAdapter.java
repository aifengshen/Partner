package com.cebbank.partner.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cebbank.partner.GlideApp;
import com.cebbank.partner.R;
import com.cebbank.partner.bean.ArticleNewBean;
import com.cebbank.partner.ui.ArticleDetailActivity;
import com.cebbank.partner.utils.DateTimeUtil;
import com.cebbank.partner.utils.LogUtils;

import java.util.List;

/**
 * @ClassName: Partner
 * @Description:
 * @Author Pjw
 * @date 2018/11/15 14:24
 */
public class HomeNewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int TYPE_ITEM_HEAD = 0; //头布局
    public static final int TYPE_ITEM_TITLE = 5; //标题
    public static final int TYPE_ITEM_SINGLE = 1; //单图
    public static final int TYPE_ITEM_THREE = 3; //三图
    public static final int TYPE_ITEM_BIG = 2; //大图
    public static final int TYPE_ITEM_VEDIO = 4; //视频

    private Context context;
    private List<ArticleNewBean> data;

    public HomeNewAdapter(Context context, List<ArticleNewBean> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        View view = null;
        if (viewType == TYPE_ITEM_HEAD) {
            view = LayoutInflater.from(context).inflate(R.layout.fragment_home_header, parent, false);
            HeaderViewHolder headerViewHolder = new HeaderViewHolder(view);
            return headerViewHolder;
        } else if (viewType == TYPE_ITEM_TITLE) {
            view = LayoutInflater.from(context).inflate(R.layout.fragment_home_title, parent, false);
            TitleViewHolder titleViewHolder = new TitleViewHolder(view);
            return titleViewHolder;
        } else if (viewType == TYPE_ITEM_SINGLE) {
            view = LayoutInflater.from(context).inflate(R.layout.fragment_home_image_text, parent, false);
            final SingleViewHolder singleViewHolder = new SingleViewHolder(view);
            singleViewHolder.rlsingle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ArticleDetailActivity.actionStart(context, data.get(singleViewHolder.getAdapterPosition()).getId());
                }
            });
            return singleViewHolder;
        } else if (viewType == TYPE_ITEM_THREE) {
            view = LayoutInflater.from(context).inflate(R.layout.fragment_home_three_image, parent, false);
            final ThreeViewHolder threeViewHolder = new ThreeViewHolder(view);
            threeViewHolder.rlsingle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ArticleDetailActivity.actionStart(context, data.get(threeViewHolder.getAdapterPosition()).getId());
                }
            });
            return threeViewHolder;
        } else if (viewType == TYPE_ITEM_BIG) {
            view = LayoutInflater.from(context).inflate(R.layout.fragment_home_big_image, parent, false);
            final BigViewHolder bigViewHolder = new BigViewHolder(view);
            bigViewHolder.rlsingle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ArticleDetailActivity.actionStart(context, data.get(bigViewHolder.getAdapterPosition()).getId());
                }
            });
            return bigViewHolder;
        } else if (viewType == TYPE_ITEM_VEDIO) {
            view = LayoutInflater.from(context).inflate(R.layout.fragment_home_big_image, parent, false);
            final VideoViewHolder videoViewHolder = new VideoViewHolder(view);
            videoViewHolder.rlsingle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ArticleDetailActivity.actionStart(context, data.get(videoViewHolder.getAdapterPosition()).getId());
                }
            });
            return videoViewHolder;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HeaderViewHolder) {

        } else if (holder instanceof TitleViewHolder) {
            ArticleNewBean articleBean = data.get(position);
            ((TitleViewHolder) holder).tvTitle.setText(articleBean.getTitle());
        } else if (holder instanceof SingleViewHolder) {
            ArticleNewBean articleBean = data.get(position);
            if (articleBean.getThumbnailList().size() == 1) {
                GlideApp.with(context)
                        .load(articleBean.getThumbnailList().get(0).getImage())
//                        .placeholder(R.mipmap.loading)
                        .centerCrop()
                        .into(((SingleViewHolder) holder).img);
            }
            ((SingleViewHolder) holder).tvTitle.setText(articleBean.getTitle());
            ((SingleViewHolder) holder).tvFrom.setText(articleBean.getAuthor());
            LogUtils.e("阅读数",articleBean.getUv()+"");
            ((SingleViewHolder) holder).tvUV.setText("阅读数:" + articleBean.getUv());
            ((SingleViewHolder) holder).tvDate.setText(DateTimeUtil.stampToDate(articleBean.getCreateDate()));

        } else if (holder instanceof ThreeViewHolder) {
            ArticleNewBean articleBean = data.get(position);
            if (articleBean.getThumbnailList().size() == 3) {
                GlideApp.with(context)
                        .load(articleBean.getThumbnailList().get(0).getImage())
//                        .placeholder(R.mipmap.loading)
                        .centerCrop()
                        .into(((ThreeViewHolder) holder).img1);
                GlideApp.with(context)
                        .load(articleBean.getThumbnailList().get(1).getImage())
//                        .placeholder(R.mipmap.loading)
                        .centerCrop()
                        .into(((ThreeViewHolder) holder).img2);
                GlideApp.with(context)
                        .load(articleBean.getThumbnailList().get(2).getImage())
//                        .placeholder(R.mipmap.loading)
                        .centerCrop()
                        .into(((ThreeViewHolder) holder).img3);
            }
            ((ThreeViewHolder) holder).tvTitle.setText(articleBean.getTitle());
            ((ThreeViewHolder) holder).tvFrom.setText(articleBean.getAuthor());
            ((ThreeViewHolder) holder).tvUV.setText("阅读数:" + articleBean.getUv());
            ((ThreeViewHolder) holder).tvDate.setText(DateTimeUtil.stampToDate(articleBean.getCreateDate()));
        } else if (holder instanceof BigViewHolder) {
            ArticleNewBean articleBean = data.get(position);
            if (articleBean.getThumbnailList().size() == 1) {
                GlideApp.with(context)
                        .load(articleBean.getThumbnailList().get(0).getImage())
//                        .placeholder(R.mipmap.loading)
                        .centerCrop()
                        .into(((BigViewHolder) holder).img);
            }
            ((BigViewHolder) holder).tvTitle.setText(articleBean.getTitle());
            ((BigViewHolder) holder).tvFrom.setText(articleBean.getAuthor());
            ((BigViewHolder) holder).tvUV.setText("阅读数:" + articleBean.getUv());
            ((BigViewHolder) holder).tvDate.setText(DateTimeUtil.stampToDate(articleBean.getCreateDate()));
        } else if (holder instanceof VideoViewHolder) {
            ArticleNewBean articleBean = data.get(position);
            if (articleBean.getThumbnailList().size() == 1) {
                GlideApp.with(context)
                        .load(articleBean.getThumbnailList().get(0).getImage())
//                        .placeholder(R.mipmap.loading)
                        .centerCrop()
                        .into(((VideoViewHolder) holder).img);
            }
            ((VideoViewHolder) holder).tvTitle.setText(articleBean.getTitle());
            ((VideoViewHolder) holder).tvFrom.setText(articleBean.getAuthor());
            ((VideoViewHolder) holder).tvUV.setText("阅读数:" + articleBean.getUv());
            ((VideoViewHolder) holder).tvDate.setText(DateTimeUtil.stampToDate(articleBean.getCreateDate()));
        }

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public int getItemViewType(int position) {
        switch (data.get(position).getType()) {
            case "HEADER":
                return TYPE_ITEM_HEAD;
            case "TITLE":
                return TYPE_ITEM_TITLE;
            case "SINGLE_SMALL":
                return TYPE_ITEM_SINGLE;
            case "SINGLE_LARGE":
                return TYPE_ITEM_BIG;
            case "TRIPLE":
                return TYPE_ITEM_THREE;
            case "VIDEO":
                return TYPE_ITEM_VEDIO;
        }
        return TYPE_ITEM_HEAD;
    }

    // 头布局布局ViewHolder
    public class HeaderViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTitle, tvHint;

        public HeaderViewHolder(View itemView) {
            super(itemView);
//            tvTitle = itemView.findViewById(R.id.tvTitle);
//            tvHint = itemView.findViewById(R.id.tvHint);
        }
    }

    // Title布局ViewHolder
    public class TitleViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTitle;

        public TitleViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
        }
    }

    // 单图布局ViewHolder
    public class SingleViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTitle, tvFrom, tvUV, tvDate;
        private ImageView img;
        private RelativeLayout rlsingle;

        public SingleViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvFrom = itemView.findViewById(R.id.tvFrom);
            tvUV = itemView.findViewById(R.id.tvUV);
            tvDate = itemView.findViewById(R.id.tvDate);
            img = itemView.findViewById(R.id.img);
            rlsingle = itemView.findViewById(R.id.rlsingle);
        }
    }

    // 三图布局ViewHolder
    public class ThreeViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTitle, tvFrom, tvUV, tvDate;
        private ImageView img1, img2, img3;
        private LinearLayout rlsingle;

        public ThreeViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvFrom = itemView.findViewById(R.id.tvFrom);
            tvUV = itemView.findViewById(R.id.tvUV);
            tvDate = itemView.findViewById(R.id.tvDate);
            img1 = itemView.findViewById(R.id.img1);
            img2 = itemView.findViewById(R.id.img2);
            img3 = itemView.findViewById(R.id.img3);
            rlsingle = itemView.findViewById(R.id.rlsingle);
        }
    }

    // 大图布局ViewHolder
    public class BigViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTitle, tvFrom, tvUV, tvDate;
        private ImageView img;
        private LinearLayout rlsingle;

        public BigViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvFrom = itemView.findViewById(R.id.tvFrom);
            tvUV = itemView.findViewById(R.id.tvUV);
            tvDate = itemView.findViewById(R.id.tvDate);
            img = itemView.findViewById(R.id.img);
            rlsingle = itemView.findViewById(R.id.rlsingle);
        }
    }

    // 视频布局ViewHolder
    public class VideoViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTitle, tvFrom, tvUV, tvDate;
        private ImageView img;
        private LinearLayout rlsingle;

        public VideoViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvFrom = itemView.findViewById(R.id.tvFrom);
            tvUV = itemView.findViewById(R.id.tvUV);
            tvDate = itemView.findViewById(R.id.tvDate);
            img = itemView.findViewById(R.id.img);
            rlsingle = itemView.findViewById(R.id.rlsingle);
        }
    }
}
