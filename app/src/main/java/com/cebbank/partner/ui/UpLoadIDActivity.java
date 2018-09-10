package com.cebbank.partner.ui;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.cebbank.partner.BaseActivity;
import com.cebbank.partner.MyApplication;
import com.cebbank.partner.R;
import com.cebbank.partner.utils.LogUtils;
import com.cebbank.partner.utils.PictureUtils;
import com.cebbank.partner.utils.ToastUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class UpLoadIDActivity extends BaseActivity implements View.OnClickListener {

    private ImageView imgIDfront, imgIDback;
    private static final int TAKE_PHOTO = 1;
    private static final int TAKE_ALBUM = 2;
    private Uri imageUri;
    private File outputImage = null;
    String path = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_up_load_id);
        initView();
        initData();
        setListener();
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        getPersimmions();
//    }

    private void initView() {
        imgIDfront = findViewById(R.id.imgIDfront);
        imgIDback = findViewById(R.id.imgIDback);
    }

    private void initData() {

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
                show("front");
                break;
            case R.id.imgIDback:
                /**
                 * 身份证反面
                 */
                show("back");
                break;
            case R.id.tvCancel:
                /**
                 * 取消
                 */

                break;
            case R.id.tvSubmit:
                /**
                 * 提交
                 */

                break;
        }
    }

    private void show(final String type) {
        View view = getLayoutInflater().inflate(R.layout.bottom_layout, null);
        view.findViewById(R.id.tvTakePhoto).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(UpLoadIDActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(UpLoadIDActivity.this, new String[]{Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                }else{
                    takePhoto(type);
                }

            }
        });
        view.findViewById(R.id.tvTakeAlbum).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showShortToast("相册");
            }
        });
        view.findViewById(R.id.tvCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showShortToast("取消");
            }
        });
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.setContentView(view);
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        dialog.show();
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
//        if (!outputImage.getParentFile().exists()) {
//            outputImage.getParentFile().mkdirs();
//        }
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
                    LogUtils.e("路径", outputImage.getPath() + "");
                    Bitmap bitmap = PictureUtils.getScaledBitmap(outputImage.getPath(), this);
                    File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), path);
                    if (!file.getParentFile().exists()) {
                        file.getParentFile().mkdirs();
                    }
                    Bitmap imgTemp = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(),
                            Bitmap.Config.ARGB_8888);
                    try {
                        FileOutputStream out = new FileOutputStream(file.getPath());
                        imgTemp.compress(Bitmap.CompressFormat.JPEG, 100, out);
                        out.flush();
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    imgIDfront.setImageBitmap(bitmap);
//                    try {
//                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
//
//                    } catch (FileNotFoundException e) {
//                        e.printStackTrace();
//                    }

                }
                break;
            case TAKE_ALBUM:
                /**
                 * 相册
                 */
                if (resultCode == RESULT_OK) {

                }
                break;
            default:
                break;
        }

    }

//    @TargetApi(23)
//    private void getPersimmions() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            ArrayList<String> permissionList = new ArrayList<String>();
//            /***
//             * 定位权限为必须权限，用户如果禁止，则每次进入都会申请
//             * 红米4X会永久禁止，解决方式：设置-授权管理-应用权限管理-找到app开启相关权限才行
//             * 魅族手机会绕过权限，永远可用。
//             */
//            // 定位精确位置
//            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
//            }
//            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                permissionList.add(Manifest.permission.ACCESS_COARSE_LOCATION);
//            }
//            // 拍照权限
//            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
//                permissionList.add(Manifest.permission.CAMERA);
//            }
//            // 音频权限
//            if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
//                permissionList.add(Manifest.permission.RECORD_AUDIO);
//            }
//            // 读写权限
//            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//                permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
//            }
//
//            if (permissionList.size() > 0) {
//                String[] permissions = permissionList.toArray(new String[permissionList.size()]);
//                ActivityCompat.requestPermissions(this, permissions, 1);
//            } else {
////                requestLocation();
//            }
//        } else {
////            requestLocation();
//        }
//    }
//
    @TargetApi(23)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    for (int result : grantResults) {
                        if (result != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(MyApplication.getContext(), "必须同意所有权限才能使用本程序", Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        }
                    }

                } else {
                    Toast.makeText(MyApplication.getContext(), "发生未知错误", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
        }

    }


}
