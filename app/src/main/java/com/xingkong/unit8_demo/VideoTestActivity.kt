package com.xingkong.unit8_demo

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View


/**
 * Created by hxl on 2018/1/11 0011.
 */
class VideoTestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.acitivity_video_test)
    }

    fun systemVideo(view: View?) {
        //Uri.parse(param)  param的值既可以是本地的视屏文件,也可以是一个视频的网页
        val uri = Uri.parse("http://p0ximlbal.bkt.clouddn.com/06A90160456424393C99A40234DCCF4D.mp4")
        //调用系统自带的播放器
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setDataAndType(uri, "video/mp4")
        startActivity(intent)
    }

    fun goVedioTest(view: View?) {
        startActivity(Intent(this, VideoViewActivity::class.java))
    }

    fun goVideoSurface(view: View?) {
        startActivity(Intent(this, VideoSurfaceActivity::class.java))
    }

}