/**
 * MusicService 2017-10-31
 * Copyright(c)2017 huangxunlei Co.Ltd. All right reserved
 */
package com.xingkong.unit8_demo;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;

import java.io.IOException;

/**
 * class description here
 *
 * @author HuangXunLei
 * @version 1.0.0
 * @since 2017-10-31 00:07
 */
public class MusicService extends Service {
    public final IBinder mBinder = new MyBinder();
    private String musicStr = Environment.getExternalStorageDirectory().getAbsolutePath()
            + "/kgmusic/download/薛之谦 - 绅士.mp3";

    public static MediaPlayer mp = new MediaPlayer();

    public class MyBinder extends Binder {
        MusicService getService() {
            return MusicService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    /**
     * 初始化服务
     */
    public MusicService() {
        try {
            mp.setDataSource(musicStr);
            mp.prepare();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 暂停或者播放
     */
    public void playOrPause() {
        if (mp.isPlaying()) {
            mp.pause();
        } else {
            mp.start();
        }
    }

    /**
     * 停止播放
     */
    public void stop() {
        if (mp != null) {
            mp.stop();

            try {
                mp.prepare();
                mp.seekTo(0);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

}