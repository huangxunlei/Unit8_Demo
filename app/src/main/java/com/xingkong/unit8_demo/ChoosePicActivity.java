package com.xingkong.unit8_demo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by hxl on 2017/10/14 0014.
 * <p>
 * 调用手机摄像头拍照
 */

public class ChoosePicActivity extends AppCompatActivity {

    public static final int TAKE_PHOTO = 1;//拍照
    public static final int CROP_PHOTO = 2;//相册
    private Button mBtnOpenCamer;
    private Button mBtnOpenAlbum;
    private ImageView mImgAvatar;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_pic);
        initView();

        mBtnOpenCamer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File outputImage = new File(Environment.getExternalStorageDirectory(),
                        "avatar" + ".jpg");
                Intent intent = new Intent();
                try {
                    if (outputImage.exists()) {
                        outputImage.delete();
                    }
                    outputImage.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //7.0以上的手机进行处理
                if (Build.VERSION.SDK_INT >= 24) {
                    imageUri = FileProvider.getUriForFile(ChoosePicActivity.this, "com.xingkong.unit8_demo", outputImage);//通过FileProvider创建一个content类型的Uri
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                } else {
                    imageUri = Uri.fromFile(outputImage);
                    Log.e("hxl", imageUri.toString());
                }

                intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent, TAKE_PHOTO);
            }
        });

        mBtnOpenAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, CROP_PHOTO);
            }
        });
    }

    /**
     * 初始化布局
     */
    private void initView() {
        mBtnOpenCamer = (Button) findViewById(R.id.btn_opnen_carmer);
        mImgAvatar = (ImageView) findViewById(R.id.img_show_avatar);
        mBtnOpenAlbum = (Button) findViewById(R.id.btn_opnen_album);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {
                        mImgAvatar.setImageBitmap(BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri)));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case CROP_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {
                        if (data != null) {
                            Uri uri = data.getData();
                            imageUri = uri;
                        }
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver()
                                .openInputStream(imageUri));
                        mImgAvatar.setImageBitmap(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
        }
    }
}
