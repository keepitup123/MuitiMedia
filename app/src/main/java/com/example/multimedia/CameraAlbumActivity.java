package com.example.multimedia;

import android.Manifest;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class CameraAlbumActivity extends AppCompatActivity implements View.OnClickListener {


    //private Button mbtnTakePhoto, mbtnChoosePhoto;
    private ImageView mtvPhoto;

    public static final int TAKE_PHOTO = 1;
    private static final int CHOOSE_PHOTO = 2;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_album);
        Button mbtnTakePhoto = (Button) findViewById(R.id.btn_takephoto);
        Button mbtnChoosePhoto = (Button) findViewById(R.id.btn_choosephoto);
        mtvPhoto = (ImageView) findViewById(R.id.tv_photo);
        // Log.d("====ok====", "onCreate: ");
        mbtnTakePhoto.setOnClickListener(this);
        mbtnChoosePhoto.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_takephoto:
                //创建一个File对象，用于储存拍张后的图片
                File outputImage = new File(getExternalCacheDir(), "output_image.jpg");
                try { //判断文件是否已经存在。若存在则删除后重新创建，并捕捉IO异常
                    if (outputImage.exists()) {
                        outputImage.delete();
                    }
                    outputImage.createNewFile();//新建名为outputImage文件
                } catch (IOException e) {
                    e.printStackTrace();
                }

                //将File对象转换为Uri对象
                if (Build.VERSION.SDK_INT >= 24) {     //判断API是否高于24（7.0），则使用封装过的Uri,并通过内容提供器访问
                    imageUri = FileProvider.getUriForFile(CameraAlbumActivity.this,
                            "com.example.multimedia.fileprovider", outputImage);
                } else {  //API低于24 则直接将file对象转化为本地真实地址Uri
                    imageUri = Uri.fromFile(outputImage);
                }

                //启动相机程序
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");//通过Intent传入一个action，
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);//调用Intent的putExtra（）方法指定图片输出地址
                startActivityForResult(intent, TAKE_PHOTO);//然后通过startactivityForRrsult（）启动活动，隐式的Intent启动，系统自动找出能响应这个Intent的程序
                //  Log.d("onClick", "=====ok====: ");
                break;
            case R.id.btn_choosephoto:     //首先进行外部存储的动态权限请求
                if (ContextCompat.checkSelfPermission(CameraAlbumActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(CameraAlbumActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                } else {
                    oepnAlbum();
                }
                break;
        }
    }

    /*
    打开相册，对于华为手机并不是图库而是图片
    构建一个Intent,传入获取内容的action
    调用startactivityForResult（）方法启动
     */
    private void oepnAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, CHOOSE_PHOTO);
    }

    //对activity返回的结果进行处理
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PHOTO:  //当拍照请求匹配时
                if (resultCode == RESULT_OK) {  //如果拍照成功
                    try {    //调用BitmapFactory的decodeStream()方法将output_image.jpg这张照片转化为Bitmap对象，并设置到ImageView中显示
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver()
                                .openInputStream(imageUri));
                        mtvPhoto.setImageBitmap(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case CHOOSE_PHOTO:  //当打开相册请求
                if (resultCode == RESULT_OK) {    //打开成功
                    if (Build.VERSION.SDK_INT >= 19) {   //当API高于19（4.4）时，处理方式
                        handleImageOnKitKat(data);
                    } else {   //API低于19时处理方式
                        handleImageBeforeKitKat(data);
                    }
                }
                break;
            default:
                break;
        }
    }

    /*
   如果是4.4及以上版本，则需要解析封装过的Uri
   如果返回的是document类型，就取出ducument id 进行处理，如果不是，就使用普通的方式处理
   如果authority是media格式的话，ducumeng id还需要再进行一次解析，需要通过字符串分割的方式取出后半部分才能得到真正的数字id
   取出的id用于构建新的Uri和条件语句，然后把这些值作为参数传入到getImagePath（）方法中来获取到图片的真实路径
   最后调用displayImage()方法把真实路径传入就可以将图片显示出来

     */
    private void handleImageOnKitKat(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(this, uri)) {
            String docId = DocumentsContract.getDocumentId(uri);
            assert uri != null;
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            } else if ("content".equalsIgnoreCase(uri.getScheme())) {
                imagePath = getImagePath(uri, null);
            } else if ("file".equalsIgnoreCase(uri.getScheme())) {
                imagePath = uri.getPath();
            }
            displayImage(imagePath);
        }
    }

    /*
    如果是4.4版本一下，则直接处理未封装的Uri
    因为未封装。所以直接将Uri传入getImagePath()方法中就可以获取图片的真实路径，最后调用displayImage（）方法
     */
    private void handleImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
        displayImage(imagePath);

    }

    //将传入的图片路径进行解码转换为Bitmap对象，并用来设置ImageView
    private void displayImage(String imagePath) {
        if (imagePath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            mtvPhoto.setImageBitmap(bitmap);
        } else {
            Toast.makeText(this, "failed to get image", Toast.LENGTH_SHORT).show();
        }

    }

    /*
    获取图片路径，通过传入的Uri对象和约束条件，并调用query()方法查询，返回真实路径
     */
    private String getImagePath(Uri uri, String selection) {
        String path = null;
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }

        return path;
    }

    //对权限请求的结果进行处理
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    oepnAlbum();
                } else {
                    Toast.makeText(this, "You denied the permission", Toast.LENGTH_LONG).show();
                }
                break;
            default:
                break;
        }
    }
}
