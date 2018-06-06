package com.misutesu.maplestorym.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.Toast;

import com.misutesu.maplestorym.R;

/**
 * @author : 伍加全(姓名) wu_developer@outlook.com(邮箱)
 * @date : 2018/5/31 0031 14:06
 * @description :
 */
@SuppressLint("ShowToast")
public class ToastUtils {
    private static Toast toast;

    public static void show(Context context, String msg) {
        if (toast == null) {
            toast = Toast.makeText(context, msg, Toast.LENGTH_LONG);
        }
        toast.setText(msg);
        toast.show();
    }

    public static void show(Context context, int strRes) {
        if (toast == null) {
            toast = Toast.makeText(context, strRes, Toast.LENGTH_LONG);
        }
        toast.setText(strRes);
        toast.show();
    }
}
