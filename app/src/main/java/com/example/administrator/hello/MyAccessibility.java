package com.example.administrator.hello;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.List;

public class MyAccessibility extends AccessibilityService {
    public static MyAccessibility instance;
    private int index = 1;
    // 接收所监听的事件
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        System.out.println("index:"+index + event.getEventType());
        if(nodeInfo == null) {
            System.out.println("rootWindow为空");
            return;
        }
        switch (index) {
            // 模拟点击工作按钮进入工作界面
            case 1:
                OpenWork(event.getEventType(),nodeInfo);
                break;
            // 进入工作界面后上传相册图片
            case 2:
                List<String> imagePathList = ImageUtils.getImagePathFromSD();
                ImageUtils.uploadImageBatch(imagePathList,null);
                index = 3;
                break;
            default:
                break;
        }
    }
    @Override
    public void onInterrupt() {

    }
    // 进入钉钉工作页面
    private void OpenWork(int type,AccessibilityNodeInfo nodeInfo) {
        if(type == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED){
            List<AccessibilityNodeInfo> homeList = nodeInfo.findAccessibilityNodeInfosByText("工作");
            if(!homeList.isEmpty()){
                boolean ret = click("工作");
                index = 2;
                System.out.println("点击进入工作页面");
            }
        }
    }
    // 通过文字执行点击操作
    private boolean click(String viewText){
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        if(nodeInfo == null) {
            System.out.println("点击失败，rootWindow为空！");
            return false;
        }
        List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByText(viewText);
        if(list.isEmpty()){
            return false;
        }else {
            // 由于是模糊查找组件，需要循环精确匹配出组件
            int k = -1;
            for (int i = 0; i<list.size();i++){
                if(list.get(i).getText() instanceof String && list.get(i).getText().equals(viewText)){
                    k = i;
                    break;
                }
            }
            if (k!= -1){
                AccessibilityNodeInfo view = list.get(k);
                return onclick(view);
            }else {
             return false;
            }
        }
    }
    // 遍历可点击按钮
    private boolean onclick(AccessibilityNodeInfo view){
        if(view.isClickable()){
            view.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            System.out.println("点击成功");
            return true;
        }else{
            AccessibilityNodeInfo parent = view.getParent();
            if(parent == null){
                return false;
            }
            onclick(parent);
        }
        return false;
    }
    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        System.out.println("MyAccessibility连接成功！！");
        instance = this;
    }
    public void setServiceEnable(){
        System.out.println("服务开启！！");
        index = 1;
    }
}
