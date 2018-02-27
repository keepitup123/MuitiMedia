package com.example.multimedia;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;

public class PlayVideoActivity extends AppCompatActivity implements View.OnClickListener {
    //声明视频控件，实际上它内部也是使用MediaPlayer对视频文件进行控制，VideoView只是封装好了让我们使用
    private VideoView videoView;

    private Button mbtnplay, mbtnpause, mbtnreplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_video);
        videoView = findViewById(R.id.video_view);
        mbtnplay = findViewById(R.id.btn_videoplay);
        mbtnpause = findViewById(R.id.btn_videopause);
        mbtnreplay = findViewById(R.id.btn_videoreplay);

        mbtnplay.setOnClickListener(this);
        mbtnpause.setOnClickListener(this);
        mbtnreplay.setOnClickListener(this);
        //动态权限请求
        if (ContextCompat.checkSelfPermission(PlayVideoActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(PlayVideoActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        } else {
            initVideoPath();//设置视频文件的路径
        }
    }
    //创建一个File对象，并指定文件的路径，我们实现将名为movie.mp4文件放在外部存储根目录
    //然后调用setVideoPath()将路径传入
    private void initVideoPath() {
        File file = new File(Environment.getExternalStorageDirectory(), "movie.mp4");
        videoView.setVideoPath(file.getPath());
    }
    //对权限请求结果进行处理
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    initVideoPath();
                } else {
                    Toast.makeText(this, "拒绝权限将无法使用程序", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }

    //模式选择
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_videoplay:
                if (!videoView.isPlaying()){
                    videoView.start();
                }
                break;
            case R.id.btn_videopause:
                if (videoView.isPlaying()){
                    videoView.pause();
                }
                break;
            case R.id.btn_videoreplay:
                if (videoView.isPlaying()){
                    videoView.resume();
                }
                break;
        }
    }
    //在acyivity销毁时先调用suspend()方法暂停视频，实现释放资源目的
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (videoView != null){
            videoView.suspend();
        }
    }
}
