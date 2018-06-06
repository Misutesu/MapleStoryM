package com.misutesu.maplestorym.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.telephony.TelephonyManager;
import android.util.Log;

/**
 * @author : 伍加全(姓名) wu_developer@outlook.com(邮箱)
 * @date : 2018/5/31 0031 14:59
 * @description :
 */
public class SIMUtils {

    private static final String SP_NAME = "SIM";
    private static final String KEY_CODE = "default_code";

    public static String CANADA_CODE = "3026103";

    public static String[] getCommands(String simCode) {
        return new String[]{"setprop gsm.sim.operator.numeric " + simCode, "busybox killall com.android.vending", "busybox rm -rf /data/data/com.android.vending/cache/AVMC_UGCR_P_", "busybox rm -rf /data/data/com.android.vending/cache/AVMC_UGCIR_", "busybox rm -rf /data/data/com.android.vending/cache/AVMC_UAR{*", "busybox rm -rf /data/data/com.android.vending/cache/AVMC_PUAR{*", "busybox killall com.android.vending"};
    }

    public static SharedPreferences getSimSP(Context context) {
        return context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
    }

    public static String getDefaultCode(SharedPreferences sp) {
        return sp.getString(KEY_CODE, null);
    }

    public static void setDefaultCode(SharedPreferences sp, String defaultCode) {
        sp.edit().putString(KEY_CODE, defaultCode).apply();
    }

    @SuppressLint("MissingPermission")
    public static String getSIMCode(Context context) {
        String code = null;
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (telephonyManager != null) {
            code = telephonyManager.getSimOperator();
        }
        return code;
    }
}
