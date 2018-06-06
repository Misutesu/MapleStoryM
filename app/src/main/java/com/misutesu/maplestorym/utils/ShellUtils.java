package com.misutesu.maplestorym.utils;

import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;

/**
 * @author : 伍加全(姓名) wu_developer@outlook.com(邮箱)
 * @date : 2018/5/31 0031 14:42
 * @description :
 */
public class ShellUtils {
    private static final String SH = "sh";
    private static final String SU = "su";

    public static boolean execCmd(String command, boolean isRoot) {
        return execCmd(new String[]{command}, isRoot);
    }

    public static boolean execCmd(String[] commands, boolean isRoot) {
        boolean result = false;
        Process process = null;
        DataOutputStream dos = null;
        BufferedReader errorResult = null;
        try {
            process = Runtime.getRuntime().exec(isRoot ? SU : SH);
            dos = new DataOutputStream(process.getOutputStream());
            for (String command : commands) {
                dos.writeBytes(command + "\n");
                dos.flush();
            }
            dos.writeBytes("exit\n");
            dos.flush();
            result = process.waitFor() != -1;
            errorResult = new BufferedReader(new InputStreamReader(process.getErrorStream(), "UTF-8"));
            StringBuilder errorMsg = new StringBuilder();
            String s;
            while ((s = errorResult.readLine()) != null) {
                if (!errorMsg.toString().equals("")) {
                    errorMsg.append("\n");
                }
                errorMsg.append(s);
            }
            result = TextUtils.isEmpty(errorMsg.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (dos != null) {
                    dos.close();
                }
                if (errorResult != null) {
                    errorResult.close();
                }
                if (process != null) {
                    process.destroy();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}
