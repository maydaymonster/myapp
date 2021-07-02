package com.ttit.myapp.schedule.mvp.add;

import com.ttit.myapp.schedule.BasePresenter;
import com.ttit.myapp.schedule.BaseView;
import com.ttit.myapp.schedule.data.beanv2.CourseV2;

/**
 * Created by mnnyang on 17-11-3.
 */

public interface AddContract {
    interface Presenter extends BasePresenter {
        void addCourse(CourseV2 courseV2);
        void removeCourse(long courseId);
        void updateCourse(CourseV2 courseV2);
    }

    interface View extends BaseView<com.ttit.myapp.schedule.mvp.add.AddContract.Presenter> {
        void showAddFail(String msg);
        void onAddSucceed(CourseV2 courseV2);
        void onDelSucceed();
        void onUpdateSucceed(CourseV2 courseV2);
    }
}
