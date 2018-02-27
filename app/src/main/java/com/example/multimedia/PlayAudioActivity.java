package com.example.multimedia;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

public class PlayAudioActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mbtnPlay, mbtnPause, mbtnStop;
    //创建一个MediaPlayer实例
    private MediaPlayer mediaPlayer = new MediaPlayer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_audio);
        mbtnPlay = findViewById(R.id.btn_play);
        mbtnPause = findViewById(R.id.btn_pause);
        mbtnStop = findViewById(R.id.btn_stop);

        mbtnPlay.setOnClickListener(this);
        mbtnPause.setOnClickListener(this);
        mbtnStop.setOnClickListener(this);

        //动态请求外部存储权限
        if (ContextCompat.checkSelfPermission(PlayAudioActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(PlayAudioActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        } else {
            initMediaPlayer();//初始化MediaPlayer
        }
    }
    //创建一个File对象来指定音频文件的路径，并捕获异常
    //实现将名为music.mp3文件放在外部存储根目录
    //调用setDataSourse ()方法传入file对象带的路径
    //调用prepare()方法   为播放做准备

    private void initMediaPlayer() {
        File file = new File(Environment.getExternalStorageDirectory(), "music.mp3");
        try {
            mediaPlayer.setDataSource(file.getPath());
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //对权限请求结果处理
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    initMediaPlayer();
                } else {
                    Toast.makeText(this, "拒绝权限将无法使用程序", Toast.LENGTH_LONG).show();
                    finish();
                }
                break;
            default:
        }
    }
    //模式选择处理
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_play:
                if (!mediaPlayer.isPlaying()) {
                    mediaPlayer.start();
                }
                break;
            case R.id.btn_pause:
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                }
                break;
            case R.id.btn_stop:
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                }
                break;
            default:

        }
    }
    //当activity销毁时停止播放并释放空间
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }
}
