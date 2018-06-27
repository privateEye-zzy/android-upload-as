package com.example.administrator.hello;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.util.List;

public class photoUploadActivity extends AppCompatActivity {
    private LoadingDialog loader;
    @SuppressLint("HandlerLeak")
    private Handler handlerUI = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
        switch (msg.what) {
            case 1:
                loader.hide();  // 更新UI
                break;
        }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_upload);
        loader = LoadingDialog.getInstance(this);
        Button upload_photo_btn = (Button) findViewById(R.id.upload_photo);
        upload_photo_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loader.show();
                List<String> imagePathList = ImageUtils.getImagePathFromSD();
                ImageUtils.uploadImageBatch(imagePathList, handlerUI);
            }
        });
    }
}
