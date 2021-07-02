package com.ttit.myapp.schedule;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.ttit.myapp.R;
import com.ttit.myapp.schedule.app.Constant;
import com.ttit.myapp.schedule.utils.ActivityUtil;
import com.ttit.myapp.schedule.utils.LogUtil;
import com.ttit.myapp.schedule.utils.Preferences;
import com.ttit.myapp.schedule.utils.ToastUtils;

public abstract class BaseActivity extends AppCompatActivity {
    protected String TAG = getClass().getSimpleName();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.d(TAG, "onCreate");

        if (canInitTheme()) {
            initTheme();
        }

        ActivityUtil.addActivity(this);
    }



    protected boolean canInitTheme() {
        return true;
    }

    /**
     * 初始化toolbar功能为返回
     *
     * @param title
     */
    protected void initBackToolbar(String title) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        /*setSupportActionBar(toolbar);*/
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            getSupportActionBar().setTitle(title);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }
    protected void initTheme() {
        int anInt = Preferences.getInt(getString(R.string.app_preference_theme), 0);
        setTheme(Constant.themeArray[anInt]);
    }

    public void toast(String msg) {
        ToastUtils.show(msg);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtil.d(TAG, "onDestroy");
        ActivityUtil.removeActivity(this);
    }
}
