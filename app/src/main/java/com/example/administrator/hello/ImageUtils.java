package com.example.administrator.hello;

import android.os.Environment;
import android.os.Handler;
import android.os.Message;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ImageUtils {
    private static final String imageServiceURL = "http://192.168.1.101:3333/good/img";
    // 从SD卡获取所有的图片资源
    public static List<String> getImagePathFromSD() {
        List<String> imagePathList = new ArrayList<String>();
        File path = new File(Environment.getExternalStorageDirectory().toString() + "/DCIM/Camera");
        traverseSDcard(path, imagePathList);
        return imagePathList;
    }
    // 遍历出SD卡相册所有照片的路径
    private static void traverseSDcard(File file, List<String> imagePathList) {
        if (file != null && file.exists() && file.isDirectory()) {
            File[] files = file.listFiles();
            for (File f : files) {
                if (f.listFiles() == null) {
                    String fileName = f.getName();
                    if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")
                            || fileName.endsWith(".bmp")
                            || fileName.endsWith(".gif")
                            || fileName.endsWith(".png")) {
                        imagePathList.add(f.getPath());
                    }
                } else {
                    traverseSDcard(f, imagePathList);
                }
            }
        }
    }
    // 批量上传照片
    public static void uploadImageBatch(List<String> fileNames, final Handler handlerUI){
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(500, TimeUnit.MILLISECONDS)
                .readTimeout(500, TimeUnit.MILLISECONDS)
                .build();
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        for (int i = 0; i < fileNames.size(); i++) {
            String localPath = fileNames.get(i);
            File file = new File(localPath);
            builder.addFormDataPart("file" + i,file.getName(), RequestBody.create(MediaType.parse("image/png"), file));
        }
        final MultipartBody requestBody = builder.build();
        Request request = new Request.Builder().url(imageServiceURL).post(requestBody).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("上传失败:" + e.getLocalizedMessage());
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                System.out.println("上传成功" + response.body().string());
                if (handlerUI != null) {
                    Message message = new Message();
                    message.what = 1;
                    handlerUI.sendMessage(message);
                }
            }
        });
    }
}
