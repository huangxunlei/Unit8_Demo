package com.xingkong.unit8_demo

import android.media.MediaPlayer
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Display
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.widget.FrameLayout
import java.io.IOException


class VideoSurfaceActivity : AppCompatActivity(), MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener, MediaPlayer.OnInfoListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnSeekCompleteListener, MediaPlayer.OnVideoSizeChangedListener, SurfaceHolder.Callback {
    private var holder: SurfaceHolder? = null
    private var player: MediaPlayer? = null
    private var currDisplay: Display? = null
    private var vWidth: Int = 0
    private var vHeight: Int = 0
    var surface: SurfaceView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_surface)
        //给SurfaceView添加CallBack监听

        surface = (findViewById(R.id.video_surface) as SurfaceView?)!!
        holder = surface!!.holder as SurfaceHolder
        holder!!.addCallback(this)
        //为了可以播放视频或者使用Camera预览，我们需要指定其Buffer类型
        holder!!!!.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS)

        //下面开始实例化MediaPlayer对象
        player = MediaPlayer()
        player!!!!.setOnCompletionListener(this)
        player!!!!.setOnErrorListener(this)
        player!!!!.setOnInfoListener(this)
        player!!!!.setOnPreparedListener(this)
        player!!!!.setOnSeekCompleteListener(this)
        player!!!!.setOnVideoSizeChangedListener(this)
        Log.v("Begin:::", "surfaceDestroyed called")
        //然后指定需要播放文件的路径，初始化MediaPlayer
        val dataPath = "http://p0ximlbal.bkt.clouddn.com/06A90160456424393C99A40234DCCF4D.mp4"
        try {
            player!!!!.setDataSource(dataPath)
            Log.v("Next:::", "surfaceDestroyed called")
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        //然后，我们取得当前Display对象
        currDisplay = this.windowManager.defaultDisplay
    }


    override fun surfaceChanged(arg0: SurfaceHolder, arg1: Int, arg2: Int, arg3: Int) {
        // 当Surface尺寸等参数改变时触发
        Log.v("Surface Change:::", "surfaceChanged called")
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        // 当SurfaceView中的Surface被创建的时候被调用
        //在这里我们指定MediaPlayer在当前的Surface中进行播放
        player!!.setDisplay(holder)
        //在指定了MediaPlayer播放的容器后，我们就可以使用prepare或者prepareAsync来准备播放了
        player!!.prepareAsync()

    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        Log.v("Surface Destory:::", "surfaceDestroyed called")
    }

    override fun onVideoSizeChanged(arg0: MediaPlayer, arg1: Int, arg2: Int) {
        // 当video大小改变时触发
        //这个方法在设置player的source后至少触发一次
        Log.v("Video Size Change", "onVideoSizeChanged called")
    }

    override fun onSeekComplete(arg0: MediaPlayer) {
        // seek操作完成时触发
        Log.v("Seek Completion", "onSeekComplete called")
    }

    override fun onPrepared(player: MediaPlayer) {
        // 当prepare完成后，该方法触发，在这里我们播放视频

        //首先取得video的宽和高
        vWidth = player.videoWidth
        vHeight = player.videoHeight

        if (vWidth > currDisplay!!.width || vHeight > currDisplay!!.height) {
            //如果video的宽或者高超出了当前屏幕的大小，则要进行缩放
            val wRatio = vWidth.toFloat() / currDisplay!!.width.toFloat()
            val hRatio = vHeight.toFloat() / currDisplay!!.height.toFloat()

            //选择大的一个进行缩放
            val ratio = Math.max(wRatio, hRatio)

            vWidth = Math.ceil((vWidth.toFloat() / ratio).toDouble()).toInt()
            vHeight = Math.ceil((vHeight.toFloat() / ratio).toDouble()).toInt()

            //设置surfaceView的布局参数
            surface!!.layoutParams = FrameLayout.LayoutParams(vWidth, vHeight)

            //然后开始播放视频

            player.start()
        }
    }

    override fun onInfo(player: MediaPlayer, whatInfo: Int, extra: Int): Boolean {
        // 当一些特定信息出现或者警告时触发
        when (whatInfo) {
            MediaPlayer.MEDIA_INFO_BAD_INTERLEAVING -> {
            }
            MediaPlayer.MEDIA_INFO_METADATA_UPDATE -> {
            }
            MediaPlayer.MEDIA_INFO_VIDEO_TRACK_LAGGING -> {
            }
            MediaPlayer.MEDIA_INFO_NOT_SEEKABLE -> {
            }
        }
        return false
    }

    override fun onError(player: MediaPlayer, whatError: Int, extra: Int): Boolean {
        Log.v("Play Error:::", "onError called")
        when (whatError) {
            MediaPlayer.MEDIA_ERROR_SERVER_DIED -> Log.v("Play Error:::", "MEDIA_ERROR_SERVER_DIED")
            MediaPlayer.MEDIA_ERROR_UNKNOWN -> Log.v("Play Error:::", "MEDIA_ERROR_UNKNOWN")
            else -> {
            }
        }
        return false
    }

    override fun onCompletion(player: MediaPlayer) {
        // 当MediaPlayer播放完成后触发
        Log.v("onCompletion", "播放完成了")
        //this.finish()
    }
}
