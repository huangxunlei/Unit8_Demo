package com.xingkong.unit8_demo

import android.annotation.SuppressLint
import android.graphics.Bitmap.CompressFormat
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Environment
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.DisplayMetrics
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.SeekBar
import kotlinx.android.synthetic.main.activity_video_surface.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class VideoSurfaceActivity : AppCompatActivity(), MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener, MediaPlayer.OnInfoListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnSeekCompleteListener, MediaPlayer.OnVideoSizeChangedListener, SurfaceHolder.Callback, SeekBar.OnSeekBarChangeListener {


    private var holder: SurfaceHolder? = null
    private var player: MediaPlayer? = null
    private var currDisplay: Display? = null
    private var vWidth: Int = 0
    private var vHeight: Int = 0
    var surface: SurfaceView? = null

    private var playPosition: Int = 0

    private var videoTimeString: String? = null

    private var dataPath: String? = null

    private var screenWidth: Int = 0

    private var screenHeight: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_surface)
        val manager = this.windowManager
        val outMetrics = DisplayMetrics()
        manager.defaultDisplay.getMetrics(outMetrics)
        screenWidth = outMetrics.widthPixels
        screenHeight = outMetrics.heightPixels

        surface = (findViewById(R.id.video_surface) as SurfaceView?)!!
        holder = surface!!.holder as SurfaceHolder
        holder!!.addCallback(this)
        //为了可以播放视频或者使用Camera预览，我们需要指定其Buffer类型
        holder!!!!.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS)

        //下面开始实例化MediaPlayer对象
        player = MediaPlayer()
        // 重置mediaPaly,建议在初始滑mediaplay立即调用。
        player!!.reset()
        // 设置播放完成监听
        player!!.setOnCompletionListener(this)
        // 错误监听回调函数
        player!!.setOnErrorListener(this)
        player!!.setOnInfoListener(this)
        //设置媒体加载完成以后回调函数。
        player!!.setOnPreparedListener(this)
        player!!.setOnSeekCompleteListener(this)
        player!!.setOnVideoSizeChangedListener(this)
        seekbar.setOnSeekBarChangeListener(this)
        Log.v("Begin:::", "surfaceDestroyed called")
        //然后指定需要播放文件的路径，初始化MediaPlayer
        dataPath = "http://p0ximlbal.bkt.clouddn.com/06A90160456424393C99A40234DCCF4D.mp4"
        try {
            player!!.setDataSource(dataPath)
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


        // 暂停和播放
        button_play.setOnClickListener(View.OnClickListener {

            if (player!!.isPlaying) {
                player!!.pause()
            } else {
                player!!.start()
            }

        })
        // 重新播放
        button_replay.setOnClickListener(View.OnClickListener {
            player!!.seekTo(0)
            player!!.start()
            player!!.setDisplay(holder);
            // 设置surfaceView保持在屏幕上
            player!!.setScreenOnWhilePlaying(true);
            holder!!.setKeepScreenOn(true);

        })
        button_screenShot.setOnClickListener(View.OnClickListener {
            savaScreenShot(playPosition.toLong())
        })


        button_changeVedioSize.setOnClickListener(View.OnClickListener {
            enterFullScreen()
        })

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

        if (null != player) {
            player!!.release()
            player = null
        }
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

    private var seekBarAutoFlag: Boolean = false

    override fun onPrepared(player: MediaPlayer) {
        // 当prepare完成后，该方法触发，在这里我们播放视频
        progressBar.setVisibility(View.GONE);
        seekbar.visibility = View.VISIBLE
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
            surface!!.layoutParams = RelativeLayout.LayoutParams(vWidth, vHeight)
            if (playPosition >= 0) {
                player.seekTo(playPosition);
                playPosition = -1;
            }
            //然后开始播放视频
            player.start()
            // 设置显示到屏幕
            player.setDisplay(holder);
            // 设置surfaceView保持在屏幕上
            player.setScreenOnWhilePlaying(true);
            holder!!.setKeepScreenOn(true);
            // 设置控制条,放在加载完成以后设置，防止获取getDuration()错误
            seekbar.setProgress(0);
            seekbar.setMax(player.duration);
            videoTimeString = getShowTime(player.duration.toLong())
            textView_showTime.text = "00:00:00/" + videoTimeString

            seekBarAutoFlag = true;
            // 开启线程 刷新进度条  
            thread.start();


        }
    }

    /**
     * 滑动条变化线程
     */
    private val thread = object : Thread() {

        override fun run() {
            // TODO Auto-generated method stub
            super.run()
            // 增加对异常的捕获，防止在判断mediaPlayer.isPlaying的时候，报IllegalStateException异常
            try {
                while (seekBarAutoFlag) {
                    /*
                    * mediaPlayer不为空且处于正在播放状态时，使进度条滚动。
                    * 通过指定类名的方式判断mediaPlayer防止状态发生不一致
                    */
                    if (null != player && player!!.isPlaying()) {
                        playPosition = player!!.getCurrentPosition()
                        seekbar.setProgress(playPosition)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {
    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        // TODO Auto-generated method stub
        if (progress >= 0) {
            // 如果是用户手动拖动控件，则设置视频跳转。
            if (fromUser) {
                player!!.seekTo(progress)
            }
            // 设置当前播放时间
            textView_showTime.text = getShowTime(progress.toLong()) + "/" + videoTimeString
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

    /**
     * 转换播放时间
     *
     * @param milliseconds 传入毫秒值
     * @return 返回 hh:mm:ss或mm:ss格式的数据
     */
    @SuppressLint("SimpleDateFormat")
    fun getShowTime(milliseconds: Long): String {
        // 获取日历函数
        val calendar = Calendar.getInstance()
        calendar.setTimeInMillis(milliseconds)
        var dateFormat: SimpleDateFormat? = null
        // 判断是否大于60分钟，如果大于就显示小时。设置日期格式
        if (milliseconds / 60000 > 60) {
            dateFormat = SimpleDateFormat("hh:mm:ss")
        } else {
            dateFormat = SimpleDateFormat("mm:ss")
        }
        return dateFormat!!.format(calendar.getTime())
    }


    /**
     * 保存视频截图.该方法只能支持本地视频文件
     *
     * @param time视频当前位置
     */
    fun savaScreenShot(time: Long) {
        // 标记是否保存成功  
        var isSave = false
        // 获取文件路径  
        var path: String? = null
        // 文件名称  
        var fileName: String? = null
        if (time >= 0) {
            try {
                val mediaMetadataRetriever = MediaMetadataRetriever()
                mediaMetadataRetriever.setDataSource(dataPath)
                // 获取视频的播放总时长单位为毫秒  
                val timeString = mediaMetadataRetriever
                        .extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
                // 转换格式为微秒  
                val timelong = java.lang.Long.parseLong(timeString) * 1000
                // 计算当前视频截取的位置  
                val index = time * timelong / player!!.getDuration()
                // 获取当前视频指定位置的截图,时间参数的单位是微秒,做了*1000处理  
                // 第二个参数为指定位置，意思接近的位置截图  
                val bitmap = mediaMetadataRetriever.getFrameAtTime(time * 1000,
                        MediaMetadataRetriever.OPTION_CLOSEST_SYNC)
                // 释放资源  
                mediaMetadataRetriever.release()
                // 判断外部设备SD卡是否存在  
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    // 存在获取外部文件路径  
                    path = Environment.getExternalStorageDirectory().getPath()
                } else {
                    // 不存在获取内部存储  
                    path = Environment.getDataDirectory().getPath()
                }
                // 设置文件名称 ，以事件毫秒为名称  
                fileName = Calendar.getInstance().timeInMillis.toString() + ".jpg"
                // 设置保存文件  
                val file = File(path + "/shen/" + fileName)

                if (!file.exists()) {
                    file.createNewFile()
                }
                val fileOutputStream = FileOutputStream(file)
                bitmap.compress(CompressFormat.JPEG, 100, fileOutputStream)
                isSave = true
            } catch (e: Exception) {
                e.printStackTrace()
            }

            // 保存成功以后，展示图片  
            if (isSave) {
                val imageView = ImageView(this)
                imageView.setLayoutParams(ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT))
                imageView.setImageBitmap(BitmapFactory.decodeFile(path + "/shen/" + fileName))
                AlertDialog.Builder(this).setView(imageView).show()
            }
        }

    }

    // 设置全屏
    // 设置SurfaceView的大小并居中显示
    fun enterFullScreen() {
        val layoutParams = RelativeLayout.LayoutParams(screenWidth,
                screenHeight)
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT)
        surface!!.setLayoutParams(layoutParams)
    }
}
