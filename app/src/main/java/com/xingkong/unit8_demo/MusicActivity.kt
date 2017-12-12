package com.xingkong.unit8_demo

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import java.text.SimpleDateFormat


class MusicActivity : AppCompatActivity() {


    private var mTvBtnState: TextView? = null
    private var mTvTime: TextView? = null
    private var mProgressBar: SeekBar? = null

    private var mBtnState: Button? = null
    private val time = SimpleDateFormat("mm:ss")
    ///storage/emulated/0/kgmusic/download/薛之谦 - 绅士.mp3

    private var musicService: MusicService? = null
    private val sc = object : ServiceConnection {
        override fun onServiceConnected(componentName: ComponentName, iBinder: IBinder) {
            musicService = (iBinder as MusicService.MyBinder).service
        }

        override fun onServiceDisconnected(componentName: ComponentName) {
            musicService = null
        }
    }

    var mHandler = Handler()

    var mRunnable: Runnable = object : Runnable {
        override fun run() {
            if (musicService!!.mp.isPlaying()) {
                mTvBtnState!!.text = "播放中..."
                mBtnState!!.text = "暂停"
            } else {
                mTvBtnState!!.text = "暂停中..."
                mBtnState!!.text = "播放"
            }
            mTvTime!!.text = (time.format(musicService!!.mp.getCurrentPosition()) + "/"
                    + time.format(musicService!!.mp.getDuration()))
            mProgressBar!!.progress = musicService!!.mp.getCurrentPosition()

            mProgressBar!!.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                    if (fromUser) {
                        musicService!!.mp.seekTo(seekBar.progress)
                    }
                }

                override fun onStartTrackingTouch(seekBar: SeekBar) {

                }

                override fun onStopTrackingTouch(seekBar: SeekBar) {

                }
            })
            mHandler.postDelayed(this, 100)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_music)
        initiView()
        musicService = MusicService()
        bindServiceConnection()
        mProgressBar!!.progress = musicService!!.mp.getDuration()
        mProgressBar!!.max = musicService!!.mp.getDuration()
    }

    private fun initiView() {
        mTvBtnState = findViewById(R.id.tv_btn_state) as TextView
        mTvTime = findViewById(R.id.tv_time) as TextView
        mProgressBar = findViewById(R.id.pb_play) as SeekBar
        mBtnState = findViewById(R.id.btn_state) as Button
    }

    private fun bindServiceConnection() {
        val intent = Intent(this@MusicActivity, MusicService::class.java)
        startService(intent)
        bindService(intent, sc, BIND_AUTO_CREATE)
    }

    override fun onResume() {
        if (musicService!!.mp.isPlaying()) {
            mTvBtnState!!.text = "播放中..."
            mBtnState!!.text = "暂停"

        } else {
            mTvBtnState!!.text = "暂停中..."
            mBtnState!!.text = "播放"
        }

        mProgressBar!!.progress = musicService!!.mp.getCurrentPosition()
        mProgressBar!!.max = musicService!!.mp.getDuration()
        mHandler.post(mRunnable)
        super.onResume()
        Log.d("hint", "handler post runnable")
    }


    fun playing(view: View) {
        musicService!!.playOrPause()
    }

    fun pause(view: View) {
        musicService!!.stop()
        mProgressBar!!.progress = 0
    }

    fun exit(view: View) {
        mHandler.removeCallbacks(mRunnable)
        unbindService(sc)
        try {
            System.exit(0)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    public override fun onDestroy() {
        unbindService(sc)
        super.onDestroy()
    }


}




