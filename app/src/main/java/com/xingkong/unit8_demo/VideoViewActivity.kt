package com.xingkong.unit8_demo

import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.MediaController
import kotlinx.android.synthetic.main.activity_video_view.*

class VideoViewActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_view)
        val uri: Uri? = Uri.parse("http://p0ximlbal.bkt.clouddn.com/465C0160E04D7B51F59FA32C5F0A7882.mp4")
        //置视屏控制器
        videoView.setMediaController(MediaController(this))
        //设置播放视频地址 //可以本地视频和网页视频都可以
        videoView.setVideoURI(uri)
        //开始播放视频
        videoView.start()
        videoView.requestFocus()
    }
}
