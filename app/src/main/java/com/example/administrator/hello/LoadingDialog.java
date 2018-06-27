package com.example.administrator.hello;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;

import com.wang.avi.AVLoadingIndicatorView;
/*
*  遮罩层+loading动画
* */
public class LoadingDialog extends AlertDialog {
    private AVLoadingIndicatorView avi;

    private LoadingDialog(Context context, int themeResId) {
        super(context,themeResId);
    }
    public static LoadingDialog getInstance(Context context) {
        LoadingDialog loadingDialog = new LoadingDialog(context, R.style.TransparentDialog);
        loadingDialog.setCancelable(false);
        loadingDialog.setCanceledOnTouchOutside(false);
        return loadingDialog;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dialog_loading);
        avi = this.findViewById(R.id.avi);
    }
    @Override
    public void show() {
        super.show();
        avi.show();
    }
    @Override
    public void dismiss() {
        super.dismiss();
        avi.hide();
    }
}
