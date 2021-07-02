package com.ttit.myapp.schedule.mvp.home;


import androidx.annotation.NonNull;
import android.text.TextUtils;

import com.ttit.myapp.schedule.app.AppUtils;
import com.ttit.myapp.schedule.app.Cache;
import com.ttit.myapp.schedule.data.beanv2.CourseGroup;
import com.ttit.myapp.schedule.data.beanv2.CourseV2;
import com.ttit.myapp.schedule.data.beanv2.DownCourseWrapper;
import com.ttit.myapp.schedule.data.greendao.CourseGroupDao;
import com.ttit.myapp.schedule.data.greendao.CourseV2Dao;
import com.ttit.myapp.schedule.utils.LogUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomePresenter implements com.ttit.myapp.schedule.mvp.home.HomeContract.Presenter {

    private com.ttit.myapp.schedule.mvp.home.HomeContract.View mView;

    private Map<String, Long> mCacheGroup;

    public HomePresenter(com.ttit.myapp.schedule.mvp.home.HomeContract.View view) {
        mView = view;
        mView.setPresenter(this);
    }

    @Override
    public void start() {
        loadUserInfo();
    }

    /**
     * 加载用户信息
     */
    @Override
    public void loadUserInfo() {

        if (TextUtils.isEmpty(Cache.instance().getEmail())) {
            if (mView == null) {
                //检查到view已经被销毁
                return;
            }
            mView.noSignInPage();
            return;
        }
    }

    /**
     * 显示所有课程表
     */
    @Override
    public void showGroup() {
        List<CourseGroup> groups = Cache.instance().getCourseGroupDao().loadAll();
        if (groups == null || groups.isEmpty()) {
            mView.showMassage("没有课程数据");
            return;
        }
        mView.showGroupDialog(groups);
    }

    /**
     * 分享写入本地
     */
    private long writeShare(List<DownCourseWrapper.DownCourse> data) {
        CourseV2Dao courseV2Dao = Cache.instance().getCourseV2Dao();

        CourseGroup group = new CourseGroup();
        group.setCgName("来自热心网友分享" + AppUtils.createUUID().substring(0, 8));
        long newGroupId = Cache.instance().getCourseGroupDao().insert(group);

        for (DownCourseWrapper.DownCourse downCourse : data) {
            CourseV2 courseV2 = new CourseV2()
                    .setCouOnlyId(AppUtils.createUUID()) //new only_id
                    .setCouCgId(newGroupId) //new group
                    .setCouName(downCourse.getName())
                    .setCouTeacher(downCourse.getTeacher())
                    .setCouLocation(downCourse.getLocation())
                    .setCouColor(downCourse.getColor())
                    .setCouWeek(downCourse.getWeek())
                    .setCouStartNode(downCourse.getStart_node())
                    .setCouNodeCount(downCourse.getNode_count())
                    .setCouAllWeek(downCourse.getAll_week());

            courseV2Dao.insert(courseV2);
        }

        return newGroupId;
    }

    /**
     * 建立json
     */
    private String buildJsonOfGroups(List<CourseV2> courseV2s, String groupName) {
        JSONObject result = new JSONObject();
        JSONArray jsonArray = new JSONArray();

        try {
            result.put("data", jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        for (CourseV2 course : courseV2s) {
            try {
                JSONObject jsonItem = new JSONObject();
                jsonItem.put("id", course.getCouId());
                jsonItem.put("name", course.getCouName());
                jsonItem.put("location", course.getCouLocation() == null ? "" : course.getCouLocation());
                jsonItem.put("week", course.getCouWeek());
                jsonItem.put("teacher", course.getCouTeacher() == null ? "" : course.getCouTeacher());
                jsonItem.put("all_week", course.getCouAllWeek());
                jsonItem.put("start_node", course.getCouStartNode());
                jsonItem.put("node_count", course.getCouNodeCount());
                jsonItem.put("color", course.getCouColor() == null ? "-1" : course.getCouColor());
                jsonItem.put("group_name", groupName);

                jsonArray.put(jsonItem);
            } catch (JSONException e) {
                LogUtil.e(this, "buildJsonOfAllCourse() failed--->" + course.toString());
                e.printStackTrace();
            }
        }
        return result.toString();
    }
    /**
     * 对所有course建立json
     */
    @NonNull
    private JSONObject buildJsonOfAllCourse() {
        List<CourseGroup> groups = Cache.instance().getCourseGroupDao()
                .queryBuilder().list();
        CourseV2Dao courseV2Dao = Cache.instance().getCourseV2Dao();

        JSONObject result = new JSONObject();
        JSONArray jsonArray = new JSONArray();

        try {
            result.put("data", jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        for (CourseGroup group : groups) {
            List<CourseV2> courseV2s = courseV2Dao.queryBuilder()
                    .where(CourseV2Dao.Properties.CouCgId.eq(group.getCgId()))
                    .list();

            for (CourseV2 course : courseV2s) {
                try {
                    JSONObject jsonItem = new JSONObject();
                    jsonItem.put("id", course.getCouId());
                    jsonItem.put("name", course.getCouName());
                    jsonItem.put("location", course.getCouLocation() == null ? "" : course.getCouLocation());
                    jsonItem.put("week", course.getCouWeek());
                    jsonItem.put("teacher", course.getCouTeacher() == null ? "" : course.getCouTeacher());
                    jsonItem.put("all_week", course.getCouAllWeek());
                    jsonItem.put("start_node", course.getCouStartNode());
                    jsonItem.put("node_count", course.getCouNodeCount());
                    jsonItem.put("color", course.getCouColor() == null ? "-1" : course.getCouColor());
                    jsonItem.put("group_name", group.getCgName());
                    jsonItem.put("only_id", course.getCouOnlyId());
                    jsonItem.put("deleted", course.getCouDeleted());

                    jsonArray.put(jsonItem);
                } catch (JSONException e) {
                    LogUtil.e(this, "buildJsonOfAllCourse() failed--->" + course.toString());
                    e.printStackTrace();
                }
            }
        }

        return result;
    }
    /**
     * 覆盖本地
     */
    private void overWriteLocal(List<DownCourseWrapper.DownCourse> downCourses) {
        mCacheGroup = new HashMap<>();
        CourseV2Dao courseDao = Cache.instance().getCourseV2Dao();

        for (DownCourseWrapper.DownCourse downCourse : downCourses) {
            Long groupId = getGroupId(downCourse);
            if (groupId != null) {
                CourseV2 oldCourse = courseDao.queryBuilder()
                        .where(CourseV2Dao.Properties.CouOnlyId.eq(downCourse.getOnly_id()))
                        .unique();
                if (oldCourse != null) {
                    // 删除手机上的数据 （覆盖）
                    courseDao.delete(oldCourse);
                }
                addCourse(downCourse, groupId);
            }
        }
    }

    /**
     * 添加
     */
    private void addCourse(DownCourseWrapper.DownCourse downCourse, Long groupId) {
        CourseV2Dao courseDao = Cache.instance().getCourseV2Dao();

        CourseV2 oldCourse = new CourseV2()
                .setCouOnlyId(downCourse.getOnly_id())
                .setCouCgId(groupId)
                .setCouName(downCourse.getName())
                .setCouTeacher(downCourse.getTeacher())
                .setCouLocation(downCourse.getLocation())
                .setCouColor(downCourse.getColor())
                .setCouWeek(downCourse.getWeek())
                .setCouStartNode(downCourse.getStart_node())
                .setCouNodeCount(downCourse.getNode_count())
                .setCouAllWeek(downCourse.getAll_week());

        courseDao.insert(oldCourse);
    }

    /**
     * 获取group_id
     */
    private Long getGroupId(DownCourseWrapper.DownCourse downCourse) {
        if (downCourse == null) {
            return null;
        }

        CourseGroupDao groupDao = Cache.instance().getCourseGroupDao();

        String groupName = downCourse.getGroup_name();
        if (!TextUtils.isEmpty(groupName)) {
            Long groupId = mCacheGroup.get(groupName);
            if (null == groupId) {
                CourseGroup dbGroup = groupDao.queryBuilder()
                        .where(CourseGroupDao.Properties.CgName.eq(groupName))
                        .unique();
                if (dbGroup == null) {
                    CourseGroup newGroup = new CourseGroup().setCgName(groupName);
                    groupId = groupDao.insert(newGroup);
                } else {
                    groupId = dbGroup.getCgId();
                }
                mCacheGroup.put(groupName, groupId);
            }
            return groupId;
        } else {
            LogUtil.e(this, "下载的数据未找到group_name");
            return null;
        }
    }

    @Override
    public void onDestroy() {
        mView = null;
        System.gc();
    }
}
