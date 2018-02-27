package com.example.multimedia;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class NotificationTest extends AppCompatActivity {

    private TextView mtvtest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_test);
        mtvtest = findViewById(R.id.tv_test);
    }
}
