package com.ttit.myapp.schedule.mvp.home;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.ContentFrameLayout;

import android.text.TextUtils;
import android.view.MenuItem;

import com.ttit.myapp.schedule.BaseActivity;
import com.ttit.myapp.schedule.app.Cache;
import com.ttit.myapp.schedule.utils.ActivityUtil;

public class HomeActivity extends BaseActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        @SuppressLint("RestrictedApi")
        ContentFrameLayout contentFrameLayout = new ContentFrameLayout(this);
        setContentView(contentFrameLayout);

        ActivityUtil.replaceFragmentToActivity(getSupportFragmentManager(),
                HomeFragment.newInstance(), android.R.id.content);
    }

    @Override
    protected boolean canInitTheme() {
        if (TextUtils.isEmpty(Cache.instance().getEmail())) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //switch (item.getItemId()) {
        //    case android.R.id.home:
        //        finish();
        //        break;
        //}
        return super.onOptionsItemSelected(item);
    }
}
