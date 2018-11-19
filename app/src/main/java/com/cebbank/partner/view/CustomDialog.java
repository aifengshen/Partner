package com.cebbank.partner.view;

import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.cebbank.partner.R;


public class CustomDialog extends Dialog {

    private Context context;
    private ImageView img;

    public CustomDialog(@NonNull Context context) {
        super(context, R.style.MyDialog);
        this.context = context;
    }

    public CustomDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
    }

    public CustomDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCanceledOnTouchOutside(false);//点击外侧不关闭Dialog
        setContentView(R.layout.loading_dialog);//加载布局
//        View view = View.inflate(context, R.layout.purchase_items, null);
//        img = view.findViewById(R.id.img);
//        initView();
//        Window win = getWindow();
//        WindowManager.LayoutParams lp = win.getAttributes();
//        lp.height = LinearLayout.LayoutParams.MATCH_PARENT;
//        lp.width = LinearLayout.LayoutParams.MATCH_PARENT;
//        win.setAttributes(lp);
//        setContentView(view);
    }

    private void initView(){
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(img, "rotation", 1f, 361f);//添加旋转动画，旋转中心默认为控件中点
        objectAnimator.setDuration(3000);//设置动画时间
        objectAnimator.setInterpolator(new LinearInterpolator());//动画时间线性渐变
        objectAnimator.setRepeatCount(ObjectAnimator.INFINITE);
//        objectAnimator.setRepeatMode(ObjectAnimator.RESTART);
        objectAnimator.start();
    }

}
