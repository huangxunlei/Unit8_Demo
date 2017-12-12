package com.xingkong.unit8_demo

import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.support.v7.app.AppCompatActivity
import android.widget.MediaController
import android.widget.Toast
import android.widget.VideoView

class VideoSurfaceActivity : AppCompatActivity() {

    private var mVideoView: VideoView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_surface)

        //本地的视频 需要在手机SD卡根目录添加一个 fl1234.mp4 视频
        val videoUrl1 = Environment.getExternalStorageDirectory().path + "/fl1234.mp4"

        //网络视频
        val videoUrl2 = "http://219.239.26.6/sh.yinyuetai.com/uploads/videos/common/1F7D015F8F1D8A4277F8260691A5D80F.mp4"

        val uri = Uri.parse(videoUrl2)
        mVideoView = findViewById(R.id.videoView) as VideoView
        //设置视频控制器
        mVideoView!!.setMediaController(MediaController(this))

        //播放完成回调
        mVideoView!!.setOnCompletionListener(MyPlayerOnCompletionListener())

        //设置视频路径
        mVideoView!!.setVideoURI(uri)

        //开始播放视频
        mVideoView!!.start()
    }

    internal inner class MyPlayerOnCompletionListener : MediaPlayer.OnCompletionListener {

        override fun onCompletion(mp: MediaPlayer) {
            Toast.makeText(this@VideoSurfaceActivity, "播放完成了", Toast.LENGTH_SHORT).show()
        }
    }
}
