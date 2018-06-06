package com.misutesu.maplestorym;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

/**
 * @author : 伍加全(姓名) wu_developer@outlook.com(邮箱)
 * @date : 2018/5/31 0031 14:14
 * @description :
 */
public abstract class BaseActivity extends AppCompatActivity {

    private final int PERMISSION_REQUEST_CODE = 100;

    private String mPermission;

    public interface OnPermissionListener {
        void onApply();

        void onRefuse();
    }

    private OnPermissionListener mOnPermissionListener;

    protected void checkPermission(String permission, OnPermissionListener onPermissionListener) {
        mPermission = permission;
        mOnPermissionListener = onPermissionListener;
        if (ContextCompat.checkSelfPermission(this, mPermission) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{mPermission}, PERMISSION_REQUEST_CODE);
        } else {
            mOnPermissionListener.onApply();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (ContextCompat.checkSelfPermission(this, mPermission) == PackageManager.PERMISSION_DENIED) {
                mOnPermissionListener.onRefuse();
            } else {
                mOnPermissionListener.onApply();
            }
        }
    }
}
