package com.ttit.myapp.schedule.mvp.home;

import android.graphics.Bitmap;
import androidx.annotation.NonNull;

import com.ttit.myapp.schedule.BasePresenter;
import com.ttit.myapp.schedule.BaseView;
import com.ttit.myapp.schedule.data.beanv2.CourseGroup;
import com.ttit.myapp.schedule.data.beanv2.UserWrapper;

import java.util.List;

public interface HomeContract {
    interface Presenter extends BasePresenter {
        void loadUserInfo();

        void showGroup();
    }

    interface View extends BaseView<com.ttit.myapp.schedule.mvp.home.HomeContract.Presenter> {
        boolean isActive();

        void showMassage(String msg);

        void showCacheData();

        void showLoading(String msg);

        void stopLoading();

        /**
         * 未登录状态页面
         */
        void noSignInPage();

        /**
         * 登录状态页面
         */
        void signInPage(UserWrapper.User user);

        void showGroupDialog(List<CourseGroup> groups);

        void createQRCodeSucceed(Bitmap bitmap);

        void cloudToLocalSucceed();
    }
}
