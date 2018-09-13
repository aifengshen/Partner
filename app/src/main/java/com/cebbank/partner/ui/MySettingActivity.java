package com.cebbank.partner.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.cebbank.partner.R;

/**
 * 我的设置页面
 */
public class MySettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_setting);
        initView();
        initData();
        setListener();
    }

    private void initView(){
//        crop(Uri.fromFile(outputImage));
    }

    private void initData(){

    }

    private void setListener(){

    }

    private void upLoadPortrait(){

    }

    /**
     * 调用系统裁剪图片
     * @param uri
     */
    private void crop(Uri uri) {
//        Log.e("URI", uri.getPath());
//        Log.e("URI", uri.toString());
//        // 裁剪图片意图
//        Intent intent = new Intent("com.android.camera.action.CROP");
//        intent.setDataAndType(uri, "image/*");
//        intent.putExtra("crop", "true");
//        // 裁剪框的比例，1：1
//        intent.putExtra("aspectX", 1);
//        intent.putExtra("aspectY", 1);
//        // 裁剪后输出图片的尺寸大小
//        intent.putExtra("outputX", 150);
//        intent.putExtra("outputY", 150);
//        intent.putExtra("scale", true);//黑边
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(outputImage));
//        // 图片格式
//        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
//        intent.putExtra("noFaceDetection", true);// 取消人脸识别
//        intent.putExtra("return-data", true);// true:不返回uri，false：返回uri
//        startActivityForResult(intent, 2000);
    }
}
