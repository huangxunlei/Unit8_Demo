package com.xingkong.unit8_demo;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    private NotificationManager notificationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public void simpleNoty(View view) {
        Bitmap btm = BitmapFactory.decodeResource(getResources(),
                R.mipmap.ic_smail);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        Intent intent = new Intent(this, MainActivity.class);  //需要跳转指定的页面
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);
        builder.setSmallIcon(R.mipmap.ic_smail);// 设置图标
        builder.setContentTitle("外卖订单的通知");// 设置通知的标题
        builder.setContentText("通知的内容");// 设置通知的内容
        builder.setWhen(System.currentTimeMillis());// 设置通知来到的时间
        //   builder.setAutoCancel(true); //自己维护通知的消失
        builder.setTicker("new message");// 第一次提示消失的时候显示在通知栏上的
        builder.setOngoing(true);
        builder.setLargeIcon(btm);
        builder.setNumber(20);

        Notification notification = builder.build();
        notification.flags = Notification.FLAG_NO_CLEAR;  //只有全部清除时，Notification才会清除
        notificationManager.notify(0, notification);
    }

    private void myNotify() {

        //实例化一个NotificationCompat.Builder对象
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setContentText("你有新的外卖订单")
                .setContentTitle("外卖通知")
                .setSmallIcon(R.mipmap.ic_smail);

        //定义并设置一个通知动作(Action)
        Intent resultIntent = new Intent(this, ResultActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pi);
        //生成Notification对象
        Notification notification = builder.build();

        //使用NotificationManager发送通知
        NotificationManager notifiManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notifiManager.notify(0, notification);

    }

    public void openCarmer(View view) {
        startActivity(new Intent(this, ChoosePicActivity.class));

    }

    public void openMusic(View view) {
        startActivity(new Intent(this, MusicActivity.class));

    }
}
