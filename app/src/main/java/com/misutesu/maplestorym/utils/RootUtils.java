package com.misutesu.maplestorym.utils;

import android.content.Context;

import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author : 伍加全(姓名) wu_developer@outlook.com(邮箱)
 * @date : 2018/5/31 0031 14:25
 * @description :
 */
public class RootUtils {

    public interface OnRootListener {
        void onApply();

        void onRefuse();
    }

    public static boolean hasRoot() {
        return ShellUtils.execCmd("", true);
    }

    public static void getRootPermission(Context context, OnRootListener onRootListener) {
        if (hasRoot()) {
            onRootListener.onApply();
        } else {
            ShellUtils.execCmd("chmod 777" + context.getPackageCodePath(), true);
            if (hasRoot()) {
                onRootListener.onApply();
            } else {
                onRootListener.onRefuse();
            }
        }
    }
}
