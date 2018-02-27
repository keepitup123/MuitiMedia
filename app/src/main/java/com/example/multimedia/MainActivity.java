package com.example.multimedia;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mtvNotification, mtvCamera, mtvMedia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mtvNotification = findViewById(R.id.tv_notification);
        mtvCamera = findViewById(R.id.tv_camera);
        mtvMedia = findViewById(R.id.tv_media);

        mtvNotification.setOnClickListener(this);
        mtvCamera.setOnClickListener(this);
        mtvMedia.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.tv_notification:
                intent = new Intent(MainActivity.this, NotificationActivity.class);
                break;
            case R.id.tv_camera:
                intent = new Intent(MainActivity.this,CameraAlbumActivity.class);
                break;
            case R.id.tv_media:
                intent = new Intent(MainActivity.this,MediaActivity.class);
                break;
        }
        startActivity(intent);
    }
}
