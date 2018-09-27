package com.cebbank.partner.ui;

import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.content.FileProvider;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import com.cebbank.partner.BaseActivity;
import com.cebbank.partner.GlideApp;
import com.cebbank.partner.MyApplication;
import com.cebbank.partner.R;
import com.cebbank.partner.interfaces.HttpCallbackListener;
import com.cebbank.partner.utils.LogUtils;
import com.cebbank.partner.utils.ToastUtils;
import com.cebbank.partner.utils.UrlPath;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.cebbank.partner.utils.HttpUtil.sendOkHttpRequest;
import static com.cebbank.partner.utils.HttpUtil.sendOkHttpRequestUpLoad;

/**
 * 我的设置页面
 */
public class MySettingActivity extends CheckPermissionsActivity {


    private static final int TAKE_PHOTO = 1;
    private static final int TAKE_ALBUM = 2;
    private static final int CROP = 3;
    private CircleImageView imgAvatar;
    private File outputImage = null;
    private String path = "", avatar = "";
    private Uri imageUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_setting);
        initView();
        initData();
        setListener();
    }

    private void initView() {
        setTitle("设置");
        setBackBtn();
        imgAvatar = findViewById(R.id.imgAvatar);
    }

    private void initData() {
        ((EditText) findViewById(R.id.edtvNickname)).setText(getIntent().getStringExtra("name"));
        avatar = getIntent().getStringExtra("avatar");
        if (!TextUtils.isEmpty(avatar)) {
            GlideApp.with(this)
                    .load(avatar)
//                        .placeholder(R.mipmap.loading)
                    .centerCrop()
                    .into(imgAvatar);
        }
    }

    private void setListener() {
        imgAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show();
            }
        });

        findViewById(R.id.tvSubmit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modifyData();
            }
        });
    }

    private void show() {
        View view = getLayoutInflater().inflate(R.layout.bottom_layout, null);
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.setContentView(view);
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        dialog.show();
        view.findViewById(R.id.tvTakePhoto).setOnClickListener(new MySettingActivity.MineOnClick(dialog));
        view.findViewById(R.id.tvTakeAlbum).setOnClickListener(new MySettingActivity.MineOnClick(dialog));
        view.findViewById(R.id.tvCancel).setOnClickListener(new MySettingActivity.MineOnClick(dialog));
    }

    /**
     * 点击事件
     */
    class MineOnClick implements View.OnClickListener {
        private BottomSheetDialog dialog;

        public MineOnClick(BottomSheetDialog dialog) {
            this.dialog = dialog;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tvTakePhoto:
                    takePhoto();
                    break;
                case R.id.tvTakeAlbum:
                    chooseFromAlbum();
                    break;
                case R.id.tvCancel:
                    break;
            }
            dialog.dismiss();//关闭弹窗
        }

    }

    private void takePhoto() {
        // 拍照
        path = "Partner/Avatar.jpg";
        outputImage = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), path);
        try {
            if (outputImage.exists()) {
                outputImage.delete();
            }
            outputImage.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 兼容安卓7.0
        if (Build.VERSION.SDK_INT >= 24) {
            imageUri = FileProvider.getUriForFile(MySettingActivity.this,
                    "com.cebbank.partner.fileprovider", outputImage);
        } else {
            imageUri = Uri.fromFile(outputImage);
        }
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, TAKE_PHOTO);
    }

    private void chooseFromAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, TAKE_ALBUM);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case TAKE_PHOTO:
                /**
                 * 拍照
                 */
                if (resultCode == RESULT_OK) {
                    // 将拍摄的照片转化成bitmap
//                    Bitmap bitmap = PictureUtils.getScaledBitmap(outputImage.getPath(), this);
//                    try {
//                        FileOutputStream out = new FileOutputStream(outputImage);
//                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
//                        out.flush();
//                        out.close();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    Uri uri = data.getData();
                    crop(imageUri);
                }
                break;
            case TAKE_ALBUM:
                /**
                 * 相册
                 */
                if (resultCode == RESULT_OK) {
                    //判断手机系统版本号
                    if (Build.VERSION.SDK_INT >= 19) {
                        //4.4及以上系统使用这个方法处理图片
                        handleImageOnKitKat(data);
                    } else {
                        //4.4以下系统使用这个方法处理图片, 因为此项目基于5.0起步，故不存在这个问题
//                        handleImageBeforeKitKat(data);
                    }
                }
                break;
            case CROP:
                /**
                 * 裁剪
                 */
                upLoadAvatarImage(outputImage.getAbsolutePath());
                break;
            default:
                break;
        }

    }

    @TargetApi(19)
    private void handleImageOnKitKat(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(this, uri)) {
            //如果是document类型的uri，则通过document id 处理
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1];//解析出数字格式的id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            //如果是content类型的uri，则使用普通方式处理
            imagePath = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            //如果是file类型的uri，直接获取图片路径即可
            imagePath = uri.getPath();
        }
        displayImage(imagePath);
    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        //通过Uri和selection来获取真实的图片路径
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    private void displayImage(String imagePath) {
        if (imagePath != null) {
            upLoadAvatarImage(imagePath);
        } else {
            ToastUtils.showShortToast("failed to get image");
        }
    }

    private void upLoadAvatarImage(String path) {
        sendOkHttpRequestUpLoad(this, UrlPath.AvatarUpload, null, path, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) throws JSONException {
                JSONObject jsonObject = new JSONObject(response);
                String code = jsonObject.optString("code");
                avatar = jsonObject.optString("data");

                ToastUtils.showShortToast("上传成功");
                GlideApp.with(MySettingActivity.this)
                        .load(avatar)
//                        .placeholder(R.mipmap.loading)
                        .centerCrop()
                        .into(imgAvatar);
            }

            @Override
            public void onFailure() {


            }
        });
    }

    private void modifyData() {
        String nikeName = ((EditText) findViewById(R.id.edtvNickname)).getText().toString();
        String personalizedSignature = ((EditText) findViewById(R.id.edtvPersonalizedSignature)).getText().toString();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("token", MyApplication.getToken());
            jsonObject.put("avatar", avatar);
            jsonObject.put("username", nikeName);
            jsonObject.put("signature", personalizedSignature);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        sendOkHttpRequest(this, UrlPath.ModifyData, jsonObject, null, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) throws JSONException {
                JSONObject jsonObject = new JSONObject(response);
                ToastUtils.showShortToast("保存成功");
                finish();
//                JSONObject data = jsonObject.getJSONObject("data");
//                String name = data.optString("username");
//                String avatar = data.optString("avatar");

            }

            @Override
            public void onFailure() {

            }
        });

    }

    /**
     * 调用系统裁剪图片
     *
     * @param uri
     */
    private void crop(Uri uri) {
        LogUtils.e("URI", uri.getPath());
        LogUtils.e("URI", uri.toString());
        // 裁剪图片意图
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // 裁剪框的比例，1：1
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // 裁剪后输出图片的尺寸大小
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("scale", true);//黑边
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(outputImage));
        // 图片格式
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);// 取消人脸识别
        intent.putExtra("return-data", true);// true:不返回uri，false：返回uri
        startActivityForResult(intent, CROP);
    }

    public static void actionStart(Context context, String name, String avatar) {
        Intent intent = new Intent(context, MySettingActivity.class);
        intent.putExtra("name", name);
        intent.putExtra("avatar", avatar);
        context.startActivity(intent);
    }
}
