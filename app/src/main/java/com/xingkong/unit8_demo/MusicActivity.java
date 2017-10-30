package com.xingkong.unit8_demo;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;

public class MusicActivity extends AppCompatActivity {


    private TextView mTvBtnState;
    private TextView mTvTime;
    private SeekBar mProgressBar;

    private Button mBtnState;
    private SimpleDateFormat time = new SimpleDateFormat("mm:ss");
    ///storage/emulated/0/kgmusic/download/薛之谦 - 绅士.mp3


    private MusicService musicService;
    private ServiceConnection sc = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            musicService = ((MusicService.MyBinder) iBinder).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            musicService = null;
        }
    };

    public Handler mHandler = new Handler();

    public Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            if (musicService.mp.isPlaying()) {
                mTvBtnState.setText("播放中...");
                mBtnState.setText("暂停");
            } else {
                mTvBtnState.setText("暂停中...");
                mBtnState.setText("播放");
            }
            mTvTime.setText(time.format(musicService.mp.getCurrentPosition()) + "/"
                    + time.format(musicService.mp.getDuration()));
            mProgressBar.setProgress(musicService.mp.getCurrentPosition());

            mProgressBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if (fromUser) {
                        musicService.mp.seekTo(seekBar.getProgress());
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
            mHandler.postDelayed(mRunnable, 100);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);
        initiView();
        musicService = new MusicService();
        bindServiceConnection();

        mProgressBar.setProgress(musicService.mp.getDuration());
        mProgressBar.setMax(musicService.mp.getDuration());
    }

    private void initiView() {
        mTvBtnState = (TextView) findViewById(R.id.tv_btn_state);
        mTvTime = (TextView) findViewById(R.id.tv_time);
        mProgressBar = (SeekBar) findViewById(R.id.pb_play);
        mBtnState = (Button) findViewById(R.id.btn_state);
    }

    private void bindServiceConnection() {
        Intent intent = new Intent(MusicActivity.this, MusicService.class);
        startService(intent);
        bindService(intent, sc, this.BIND_AUTO_CREATE);
    }

    @Override
    protected void onResume() {
        if (musicService.mp.isPlaying()) {
            mTvBtnState.setText("播放中...");
            mBtnState.setText("暂停");

        } else {
            mTvBtnState.setText("暂停中...");
            mBtnState.setText("播放");
        }

        mProgressBar.setProgress(musicService.mp.getCurrentPosition());
        mProgressBar.setMax(musicService.mp.getDuration());
        mHandler.post(mRunnable);
        super.onResume();
        Log.d("hint", "handler post runnable");
    }


    public void playing(View view) {
        musicService.playOrPause();
    }

    public void pause(View view) {
        musicService.stop();
        mProgressBar.setProgress(0);
    }

    public void exit(View view) {
        mHandler.removeCallbacks(mRunnable);
        unbindService(sc);
        try {
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        unbindService(sc);
        super.onDestroy();
    }
}
