package com.misutesu.maplestorym;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.misutesu.maplestorym.utils.MapleStroyUtil;
import com.misutesu.maplestorym.utils.RootUtils;
import com.misutesu.maplestorym.utils.SIMUtils;
import com.misutesu.maplestorym.utils.ShellUtils;
import com.misutesu.maplestorym.utils.ToastUtils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private Button mStartBtn, mChangeSIMBtn, mDefaultSIMBtn;
    private CheckBox mCbXposed;
    private TextView mDefaultTv, mNowTv;

    private ProgressDialog mPd;

    private ExecutorService mExecutorService;

    private SharedPreferences mSP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mStartBtn = findViewById(R.id.btn_start);
        mChangeSIMBtn = findViewById(R.id.btn_change_sim);
        mDefaultSIMBtn = findViewById(R.id.btn_default_sim);
        mCbXposed = findViewById(R.id.cb_xposed);
        mDefaultTv = findViewById(R.id.tv_default_code);
        mNowTv = findViewById(R.id.tv_now_code);

        mStartBtn.setOnClickListener(this);
        mChangeSIMBtn.setOnClickListener(this);
        mDefaultSIMBtn.setOnClickListener(this);

        mSP = SIMUtils.getSimSP(this);

        mExecutorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        showSIM();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_start:
                Intent intent = getPackageManager().getLaunchIntentForPackage(MapleStroyUtil.PACKAGE_NAME);
                if (intent == null) {
                    showMessage(R.string.not_find_game);
                    return;
                }
                startActivity(intent);
                break;
            case R.id.btn_change_sim:
                RootUtils.getRootPermission(this, new RootUtils.OnRootListener() {
                    @Override
                    public void onApply() {
                        changeSIM(SIMUtils.CANADA_CODE);
                    }

                    @Override
                    public void onRefuse() {
                        showMessage(R.string.no_root_permission);
                    }
                });
                break;
            case R.id.btn_default_sim:
                final String code = SIMUtils.getDefaultCode(mSP);
                if (TextUtils.isEmpty(code)) {
                    showMessage(R.string.no_old_sim_code);
                    return;
                }
                RootUtils.getRootPermission(this, new RootUtils.OnRootListener() {
                    @Override
                    public void onApply() {
                        changeSIM(code);
                    }

                    @Override
                    public void onRefuse() {
                        showMessage(R.string.no_root_permission);
                    }
                });
                break;
        }
    }

    private void showSIM() {
        String defaultCode = SIMUtils.getDefaultCode(mSP);
        String nowCode = SIMUtils.getSIMCode(MainActivity.this);

        SIMUtils.setDefaultCode(mSP, nowCode);

        defaultCode = TextUtils.isEmpty(defaultCode) ? getString(R.string.un_know) : defaultCode;
        nowCode = TextUtils.isEmpty(nowCode) ? getString(R.string.un_know) : nowCode;

        mDefaultTv.setText(getString(R.string.default_sim_code, defaultCode));
        mNowTv.setText(getString(R.string.now_sim_code, nowCode));
    }

    private void changeSIM(final String code) {
        mPd = new ProgressDialog(this);
        mPd.setCancelable(false);
        mPd.show();

        mExecutorService.submit(new Runnable() {
            @Override
            public void run() {
                ShellUtils.execCmd(SIMUtils.getCommands(code), true);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mPd.dismiss();
                        showMessage(R.string.change_sim_end);
                        showSIM();
                    }
                });
            }
        });
    }

    private void showMessage(@StringRes int strRes) {
        ToastUtils.show(this, strRes);
    }
}
