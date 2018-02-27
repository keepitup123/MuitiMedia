package com.example.multimedia;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.File;

public class NotificationActivity extends AppCompatActivity implements View.OnClickListener {
    private Button mbtn1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        mbtn1 = findViewById(R.id.btn_1);
        mbtn1.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            /*
            创建一个NotificationManager来管理通知，通过getSystemService()方法获取系统通知服务，
            会返回一个对象，并强转为Notification，完成获取NotificationManager实例
            为了规范，使用v4包中的NotificationCompat类的Builder构造器创建Notification对象，
            并通过builder()方法前的若干设置方法来创建一个丰富的Notification对象
            首先创建一个Intent 表达我们要启动NotificationTestActivity的意图，并将intent传入pendingIntent的getActivity中
            来获取pengdingIntent实例，最后把这个实例作为参数传入setContentIntent中
            最后调用NotificationManager的nitify()方法让通知显示出来

             */
            case R.id.btn_1:
                Intent intent = new Intent(this,NotificationTest.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,0);
                NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                Notification notification = new NotificationCompat.Builder(this)
                        .setContentTitle("This is content title")
                        .setContentText("Learn how to build notifications, " +
                                "send and synchronize data, and use sound actions, " +
                                "and how to get the official IDE and developer tools " +
                                "to create Android apps")//当文本过长时，会出现省略显示
                        .setStyle(new NotificationCompat.BigTextStyle().bigText("Learn how to build notifications, send and synchronize data, " +
                                "and use sound actions, and how to get the " +
                                "official IDE and developer tools" +
                                " to create Android apps"))//解决上面的问题，文本过长可以下拉显示完全
                        .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(BitmapFactory.
                                decodeResource(getResources(),R.drawable.big_image)))//显示大图片在通知
                        .setWhen(System.currentTimeMillis())//指定通知在何时显示，这里是当前
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher))//设置大图标
                        .setSmallIcon(R.mipmap.ic_launcher)//设置小图标
                        .setContentIntent(pendingIntent)//传入pendingIntent
                       // .setVibrate(new long[]{ 0,1000,1000,1000 })//设置振动，第一 三个是静止时间，二四是振动时间
                       // .setLights(Color.RED,1000,1000)//设置前置LED灯闪烁 第一个参数为颜色 第二个为亮时长，第三个暗时长
                        .setAutoCancel(true)//点击时让通知被取消
                       // .setSound(Uri.fromFile(new File("/system/media/audio/ringtones/Bounce.ogg")))//点击时播放音频
                        .setDefaults(NotificationCompat.DEFAULT_ALL)//默认的通知设置 音频  振动  LED灯
                        .setPriority(NotificationCompat.PRIORITY_MAX)//设置这条通知的重要程度
                        .build();//记得最后要调用builder()方法
               // manager.cancel(1);//点击取消通知的另一种方法
                assert manager != null;
                manager.notify(1,notification);//调用manager的notiy()方法，让通知显示出来
                break;
        }
    }
}
