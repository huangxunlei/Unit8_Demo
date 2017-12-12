package com.xingkong.unit8_demo

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException

/**
 * Created by hxl on 2017/10/14 0014.
 *
 *
 * 调用手机摄像头拍照
 */

class ChoosePicActivity : AppCompatActivity() {
    private var mBtnOpenCamer: Button? = null
    private var mBtnOpenAlbum: Button? = null
    private var mImgAvatar: ImageView? = null
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_pic)
        initView()

        mBtnOpenCamer!!.setOnClickListener {
            val outputImage = File(Environment.getExternalStorageDirectory(),
                    "avatar" + ".jpg")
            val intent = Intent()
            try {
                if (outputImage.exists()) {
                    outputImage.delete()
                }
                outputImage.createNewFile()
            } catch (e: IOException) {
                e.printStackTrace()
            }

            //7.0以上的手机进行处理
            if (Build.VERSION.SDK_INT >= 24) {
                imageUri = FileProvider.getUriForFile(this@ChoosePicActivity, "com.xingkong.unit8_demo", outputImage)//通过FileProvider创建一个content类型的Uri
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            } else {
                imageUri = Uri.fromFile(outputImage)
                Log.e("hxl", imageUri!!.toString())
            }

            intent.action = MediaStore.ACTION_IMAGE_CAPTURE
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
            startActivityForResult(intent, TAKE_PHOTO)
        }

        mBtnOpenAlbum!!.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, CROP_PHOTO)
        }
    }

    /**
     * 初始化布局
     */
    private fun initView() {
        mBtnOpenCamer = findViewById(R.id.btn_opnen_carmer) as Button
        mImgAvatar = findViewById(R.id.img_show_avatar) as ImageView
        mBtnOpenAlbum = findViewById(R.id.btn_opnen_album) as Button
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            TAKE_PHOTO -> if (resultCode == Activity.RESULT_OK) {
                try {
                    mImgAvatar!!.setImageBitmap(BitmapFactory.decodeStream(contentResolver.openInputStream(imageUri!!)))
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                }

            }
            CROP_PHOTO -> if (resultCode == Activity.RESULT_OK) {
                try {
                    if (data != null) {
                        val uri = data.data
                        imageUri = uri
                    }
                    val bitmap = BitmapFactory.decodeStream(contentResolver
                            .openInputStream(imageUri!!))
                    mImgAvatar!!.setImageBitmap(bitmap)
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                }

            }
        }
    }

    companion object {

        val TAKE_PHOTO = 1//拍照
        val CROP_PHOTO = 2//相册
    }
}
