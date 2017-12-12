/**
 * MusicService 2017-10-31
 * Copyright(c)2017 huangxunlei Co.Ltd. All right reserved
 */
package com.xingkong.unit8_demo

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder

import java.io.IOException

/**
 * class description here
 *
 * @author HuangXunLei
 * @version 1.0.0
 * @since 2017-10-31 00:07
 */
class MusicService : Service() {
    val mBinder: IBinder = MyBinder()
    val mp: MediaPlayer = MediaPlayer()
    private val musicStr = "/storage/sdcard1/MIUI/music/mp3/思美人兮(《思美人》电视剧插曲)_金玟岐_思美人兮.mp3"


    inner class MyBinder : Binder() {
        internal val service: MusicService
            get() = this@MusicService
    }

    override fun onBind(intent: Intent): IBinder? {
        return mBinder
    }

    /**
     * 初始化服务
     */
    init {
        try {
            mp!!.setDataSource(musicStr)
            mp!!.prepare()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    /**
     * 暂停或者播放
     */
    fun playOrPause() {
        if (mp!!.isPlaying) {
            mp!!.pause()
        } else {
            mp!!.start()
        }
    }

    /**
     * 停止播放
     */
    fun stop() {
        if (mp != null) {
            mp!!.stop()

            try {
                mp!!.prepare()
                mp!!.seekTo(0)
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
    }


}