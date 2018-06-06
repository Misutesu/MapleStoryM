package com.misutesu.maplestorym.xposed;

import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.widget.Button;
import android.widget.CheckBox;

import com.misutesu.maplestorym.R;
import com.misutesu.maplestorym.utils.MapleStroyUtil;
import com.misutesu.maplestorym.utils.SIMUtils;

import java.lang.reflect.Field;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * @author : 伍加全(姓名) wu_developer@outlook.com(邮箱)
 * @date : 2018/6/6 0006 13:32
 * @description :
 */
public class XposedInit implements IXposedHookLoadPackage {

    @Override
    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam lpparam) {
        if (lpparam.packageName.equals(MapleStroyUtil.PACKAGE_NAME) || lpparam.packageName.equals("com.misutesu.maplestorym")) {
            XposedBridge.hookAllMethods(TelephonyManager.class, "getSimOperator", new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                    param.setResult(SIMUtils.CANADA_CODE);
                }
            });

            if (lpparam.packageName.equals("com.misutesu.maplestorym")) {
                XposedHelpers.findAndHookMethod("com.misutesu.maplestorym.MainActivity",
                        lpparam.classLoader, "onCreate", Bundle.class, new XC_MethodHook() {
                            @Override
                            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                                super.afterHookedMethod(param);
                                Class c = lpparam.classLoader.loadClass("com.misutesu.maplestorym.MainActivity");

                                Field field = c.getDeclaredField("mChangeSIMBtn");
                                field.setAccessible(true);
                                Button btn = (Button) field.get(param.thisObject);
                                btn.setEnabled(false);

                                field = c.getDeclaredField("mDefaultSIMBtn");
                                field.setAccessible(true);
                                btn = (Button) field.get(param.thisObject);
                                btn.setEnabled(false);

                                field = c.getDeclaredField("mCbXposed");
                                field.setAccessible(true);
                                CheckBox cb = (CheckBox) field.get(param.thisObject);
                                cb.setText(R.string.xposed_is_open);
                                cb.setEnabled(true);
                                cb.setChecked(true);
                                cb.setClickable(false);
                                cb.setFocusable(false);
                                cb.setFocusableInTouchMode(false);
                            }
                        });
            }
        }
    }
}
