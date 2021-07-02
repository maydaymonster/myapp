package com.ttit.myapp.schedule.mvp.mg;

import com.ttit.myapp.schedule.BasePresenter;
import com.ttit.myapp.schedule.BaseView;

/**
 * Created by mnnyang on 17-11-4.
 */

public interface MgContract {
    interface Presenter extends BasePresenter {
        void deleteCsName(long csNameId);
        void switchCsName(long csNameId);
        void reloadCsNameList();
        void addCsName(String csName);
        void editCsName(long id, String newCsName);
    }

    interface View extends BaseView<Presenter> {
        void showList();
        void showNotice(String notice);
        void gotoCourseActivity();
        void deleteFinish();
        void addCsNameSucceed();
        void editCsNameSucceed();
    }

}
