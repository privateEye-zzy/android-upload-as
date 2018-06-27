package com.example.administrator.hello;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MyReceiver extends BroadcastReceiver {
    // 接收到广播后，则自动调用该方法
    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println("onReceive接受到广播...");
        OpenApp.OpenDingDing("com.alibaba.android.rimet", context);
        MyAccessibility.instance.setServiceEnable();
    }
}
