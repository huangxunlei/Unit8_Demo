package com.xingkong.unit8_demo

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v4.app.NotificationCompat
import android.support.v7.app.AppCompatActivity
import android.view.View

class MainActivity : AppCompatActivity() {
    private var notificationManager: NotificationManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    fun simpleNoty(view: View) {
        val btm = BitmapFactory.decodeResource(resources,
                R.mipmap.ic_smail)
        val builder = NotificationCompat.Builder(this)
        val intent = Intent(this, MainActivity::class.java)  //需要跳转指定的页面
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        builder.setContentIntent(pendingIntent)
        builder.setSmallIcon(R.mipmap.ic_smail)// 设置图标
        builder.setContentTitle("外卖订单的通知")// 设置通知的标题
        builder.setContentText("通知的内容")// 设置通知的内容
        builder.setWhen(System.currentTimeMillis())// 设置通知来到的时间
        builder.setAutoCancel(true) //自己维护通知的消失
        builder.setTicker("new message")// 第一次提示消失的时候显示在通知栏上的
        builder.setOngoing(true)
        builder.setLargeIcon(btm)
        builder.setNumber(20)

        val notification = builder.build()
        notification.flags = Notification.FLAG_NO_CLEAR  //只有全部清除时，Notification才会清除
        notificationManager!!.notify(0, notification)
    }

    private fun myNotify() {

        //实例化一个NotificationCompat.Builder对象
        val builder = NotificationCompat.Builder(this)
                .setContentText("你有新的外卖订单")
                .setContentTitle("外卖通知")
                .setSmallIcon(R.mipmap.ic_smail)

        //定义并设置一个通知动作(Action)
        val resultIntent = Intent(this, ResultActivity::class.java)
        val pi = PendingIntent.getActivity(this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        builder.setContentIntent(pi)
        //生成Notification对象
        val notification = builder.build()

        //使用NotificationManager发送通知
        val notifiManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notifiManager.notify(0, notification)

    }

    private fun v2(view: View) {

    }

    /**
     * 打开图片选择
     *
     * @param view
     */
    fun openCarmer(view: View) {
        startActivity(Intent(this, ChoosePicActivity::class.java))

    }

    /**
     * 打开音乐播放
     *
     * @param view
     */
    fun openMusic(view: View) {
        startActivity(Intent(this, MusicActivity::class.java))

    }

    /**
     * 打开视屏
     *
     * @param view
     */
    fun openVideo(view: View) {
        startActivity(Intent(this, VideoSurfaceActivity::class.java))
    }
}
