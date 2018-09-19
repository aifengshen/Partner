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
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.cebbank.partner.GlideApp;
import com.cebbank.partner.MyApplication;
import com.cebbank.partner.R;
import com.cebbank.partner.interfaces.HttpCallbackListener;
import com.cebbank.partner.utils.PictureUtils;
import com.cebbank.partner.utils.ToastUtils;
import com.cebbank.partner.utils.UrlPath;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static com.cebbank.partner.utils.HttpUtil.sendOkHttpRequest;
import static com.cebbank.partner.utils.HttpUtil.sendOkHttpRequestUpLoad;

public class UpLoadIDActivity extends CheckPermissionsActivity implements View.OnClickListener {

    private ImageView imgIDfront, imgIDback;
    private static final int TAKE_PHOTO = 1;
    private static final int TAKE_ALBUM = 2;
    private Uri imageUri;
    private File outputImage = null;
    private String path = "", type = "", front = "", back = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_up_load_id);
        initView();
        setListener();
    }

    private void initView() {
        setTitle("上传身份证");
        setBackBtn();
        imgIDfront = findViewById(R.id.imgIDfront);
        imgIDback = findViewById(R.id.imgIDback);
    }

    private void setListener() {
        findViewById(R.id.tvCancel).setOnClickListener(this);
        findViewById(R.id.tvSubmit).setOnClickListener(this);
        imgIDfront.setOnClickListener(this);
        imgIDback.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgIDfront:
                /**
                 * 身份证正面
                 */
                type = "front";
                show();
                break;
            case R.id.imgIDback:
                /**
                 * 身份证反面
                 */
                type = "back";
                show();
                break;
            case R.id.tvCancel:
                /**
                 * 取消
                 */
                finish();
                break;
            case R.id.tvSubmit:
                /**
                 * 提交
                 */
                submit();
                break;
        }
    }

    private void show() {
        View view = getLayoutInflater().inflate(R.layout.bottom_layout, null);
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.setContentView(view);
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        dialog.show();
        view.findViewById(R.id.tvTakePhoto).setOnClickListener(new MineOnClick(dialog));
        view.findViewById(R.id.tvTakeAlbum).setOnClickListener(new MineOnClick(dialog));
        view.findViewById(R.id.tvCancel).setOnClickListener(new MineOnClick(dialog));
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
                    takePhoto(type);
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

    private void takePhoto(String type) {
        // 拍照
        if (type.equals("front")) {
            path = "Partner/IMAGE_IDFront.jpg";
        } else {
            path = "Partner/IMAGE_IDBack.jpg";
        }
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
            imageUri = FileProvider.getUriForFile(UpLoadIDActivity.this,
                    "com.cebbank.partner.fileprovider", outputImage);
        } else {
            imageUri = Uri.fromFile(outputImage);
        }
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, TAKE_PHOTO);
    }

    private void submit() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("username", getIntent().getStringExtra("username"));
            jsonObject.put("phone", getIntent().getStringExtra("phone"));
            jsonObject.put("code", getIntent().getStringExtra("code"));
            jsonObject.put("idCard", getIntent().getStringExtra("idCard"));
            jsonObject.put("front", front);
            jsonObject.put("back", back);
            jsonObject.put("token", MyApplication.getToken());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        sendOkHttpRequest(this, UrlPath.Apply, jsonObject, null, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) throws JSONException {
                JSONObject jsonObject = new JSONObject(response);
                ToastUtils.showShortToast("提交成功~");
                finish();
            }

            @Override
            public void onFailure() {

            }
        });
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
            case 1:
                /**
                 * 拍照
                 */
                if (resultCode == RESULT_OK) {
                    // 将拍摄的照片转化成bitmap
                    Bitmap bitmap = PictureUtils.getScaledBitmap(outputImage.getPath(), this);
                    try {
                        FileOutputStream out = new FileOutputStream(outputImage);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                        out.flush();
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    upLoadIDImage(outputImage.getAbsolutePath());
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
            upLoadIDImage(imagePath);
        } else {
            ToastUtils.showShortToast("failed to get image");
        }
    }

    private void upLoadIDImage(String path) {
        String url = "";
        if (type.equals("front")) {
            url = UrlPath.IDcardHeadUpload;
        } else {
            url = UrlPath.IDcardTailUpload;
        }
        sendOkHttpRequestUpLoad(this, url, null, path, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) throws JSONException {
                JSONObject jsonObject = new JSONObject(response);
                String code = jsonObject.optString("code");
                String url = jsonObject.optString("data");
                ToastUtils.showShortToast("上传成功");
                if (type.equals("front")) {
                    GlideApp.with(UpLoadIDActivity.this)
                            .load(url)
//                        .placeholder(R.mipmap.loading)
                            .centerCrop()
                            .into(imgIDfront);
                    front = url;
                } else {
                    GlideApp.with(UpLoadIDActivity.this)
                            .load(url)
//                        .placeholder(R.mipmap.loading)
                            .centerCrop()
                            .into(imgIDback);
                    back = url;
                }

            }

            @Override
            public void onFailure() {


            }
        });
    }

    public static void actionStart(Context context, String username, String phone, String code,
                                   String idCard) {
        Intent intent = new Intent(context, UpLoadIDActivity.class);
        intent.putExtra("username", username);
        intent.putExtra("phone", phone);
        intent.putExtra("code", code);
        intent.putExtra("idCard", idCard);
        context.startActivity(intent);
    }
}
