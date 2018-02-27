package com.example.multimedia;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MediaActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mbtnaudio,mbtnvideo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media);
        mbtnaudio = findViewById(R.id.btn_audio);
        mbtnvideo = findViewById(R.id.btn_video);
        mbtnaudio.setOnClickListener(this);
        mbtnvideo.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()){
            case R.id.btn_audio:
                intent = new Intent(MediaActivity.this,PlayAudioActivity.class);
                break;
            case R.id.btn_video:
                intent = new Intent(MediaActivity.this,PlayVideoActivity.class);
                break;
        }
        startActivity(intent);
    }
}
