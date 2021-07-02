package com.ttit.myapp.fragment;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.ttit.myapp.R;
import com.ttit.myapp.schedule.RecyclerBaseAdapter;
import com.ttit.myapp.schedule.app.AppUtils;
import com.ttit.myapp.schedule.app.Cache;
import com.ttit.myapp.schedule.app.Constant;
import com.ttit.myapp.schedule.custom.course.CourseAncestor;
import com.ttit.myapp.schedule.custom.course.CourseView;
import com.ttit.myapp.schedule.custom.util.Utils;
import com.ttit.myapp.schedule.data.beanv2.CourseGroup;
import com.ttit.myapp.schedule.data.beanv2.CourseV2;
import com.ttit.myapp.schedule.data.greendao.CourseGroupDao;
import com.ttit.myapp.schedule.data.greendao.CourseV2Dao;
import com.ttit.myapp.schedule.mvp.add.AddActivity;
import com.ttit.myapp.schedule.mvp.course.CourseContract;
import com.ttit.myapp.schedule.mvp.course.CoursePresenter;
import com.ttit.myapp.schedule.mvp.course.SelectWeekAdapter;
import com.ttit.myapp.schedule.mvp.home.HomeActivity;
import com.ttit.myapp.schedule.mvp.mg.MgActivity;
import com.ttit.myapp.schedule.utils.DialogHelper;
import com.ttit.myapp.schedule.utils.DialogListener;
import com.ttit.myapp.schedule.utils.LogUtil;
import com.ttit.myapp.schedule.utils.Preferences;
import com.ttit.myapp.schedule.utils.ScreenUtils;
import com.ttit.myapp.schedule.utils.TimeUtils;
import com.ttit.myapp.schedule.utils.ToastUtils;
import com.ttit.myapp.schedule.utils.event.CourseDataChangeEvent;
import com.ttit.myapp.schedule.utils.spec.ShowDetailDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import static com.ttit.myapp.schedule.utils.ScreenUtils.dp2px;

public class PlanFragment extends BaseFragment implements CourseContract.View,View.OnClickListener {
    CourseContract.Presenter mPresenter;
    private TextView mTvWeekCount;
    private int mCurrentWeekCount;
    private int mCurrentMonth;
    private ShowDetailDialog mDialog;
    private CourseView mCourseViewV2;
    private LinearLayout mLayoutWeekGroup;
    private LinearLayout mLayoutNodeGroup;
    private int WEEK_TEXT_SIZE = 12;
    private int NODE_TEXT_SIZE = 11;
    private int NODE_WIDTH = 28;
    private TextView mMMonthTextView;
    private RecyclerView mRvSelectWeek;
    private int mHeightSelectWeek;
    private boolean mSelectWeekIsShow = false;
    private LinearLayout mLayoutCourse;

    public PlanFragment() {

    }

    public static PlanFragment newInstance() {
        PlanFragment fragment = new PlanFragment();
        return fragment;
    }

    @Override
    protected int initLayout() {
        ToastUtils.init(getActivity());
        Preferences.init(getActivity());
        ScreenUtils.init(getActivity());
        mPresenter = new CoursePresenter(this);
        return R.layout.fragment_plan;
    }

    @Override
    protected void initView() {
        EventBus.getDefault().register(this);
    }
    @Override
    protected void initData() {
        mLayoutWeekGroup = getView().findViewById(R.id.layout_week_group);
        mLayoutNodeGroup =getView().findViewById(R.id.layout_node_group);
        mLayoutCourse =  getView().findViewById(R.id.layout_course);
        initFirstStart();
        initToolbar();
        initWeek();
        initCourseView();
        initWeekNodeGroup();
        updateView();
    }
    private void initWeek() {
        initWeekTitle();
        initSelectWeek();
    }
    private void initSelectWeek() {
        mRvSelectWeek = getView().findViewById(R.id.recycler_view_select_week);

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mRvSelectWeek.getLayoutParams();
        params.topMargin = -ScreenUtils.dp2px(45);
        mRvSelectWeek.setLayoutParams(params);

        mRvSelectWeek.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext(),
                RecyclerView.HORIZONTAL, false));
        ArrayList<String> strings = new ArrayList<>();
        for (int i = 1; i <= 25; i++) {
            strings.add("第" + i + "周");
        }
        SelectWeekAdapter selectWeekAdapter = new SelectWeekAdapter(R.layout.adapter_select_week, strings);
        mRvSelectWeek.setAdapter(selectWeekAdapter);
        mRvSelectWeek.scrollToPosition(AppUtils.getCurrentWeek(getActivity().getBaseContext())-1);

        mRvSelectWeek.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom,
                                       int oldLeft, int oldTop, int oldRight, int oldBottom) {
                mHeightSelectWeek = bottom - top;
            }
        });


        selectWeekAdapter.setItemClickListener(new RecyclerBaseAdapter.ItemClickListener() {
            @Override
            public void onItemClick(View view, final RecyclerBaseAdapter.ViewHolder holder) {
                mCurrentWeekCount = holder.getAdapterPosition() + 1;

                AppUtils.PreferencesCurrentWeek(getActivity().getBaseContext(), mCurrentWeekCount);
                mCourseViewV2.setCurrentIndex(mCurrentWeekCount);
                mCourseViewV2.resetView();
                mTvWeekCount.setText("第" + mCurrentWeekCount + "周");

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        animSelectWeek(false);
                    }
                }, 150);
            }

            @Override
            public void onItemLongClick(View view, RecyclerBaseAdapter.ViewHolder holder) {

            }
        });
    }


    private void animSelectWeek(boolean show) {
        mSelectWeekIsShow = show;

        int start = 0, end = 0;
        if (show) {
            start = -mHeightSelectWeek;
        } else {
            end = -mHeightSelectWeek;
        }

        ValueAnimator animator = ValueAnimator.ofInt(start, end);
        animator.setDuration(300);
        animator.setInterpolator(new DecelerateInterpolator());

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mRvSelectWeek.getLayoutParams();
                params.topMargin = (int) animation.getAnimatedValue();
                mRvSelectWeek.setLayoutParams(params);
            }
        });
        animator.start();
    }


    private void initWeekTitle() {
        mTvWeekCount = getView().findViewById(R.id.tv_toolbar_subtitle);
        mTvWeekCount.setOnClickListener(this);
        TextView tvTitle = getView().findViewById(R.id.tv_toolbar_title);
        tvTitle.setText("课程表");
    }

    private void initWeekNodeGroup() {
        mLayoutNodeGroup.removeAllViews();
        mLayoutWeekGroup.removeAllViews();

        for (int i = -1; i < 7; i++) {
            TextView textView = new TextView(getContext());
            textView.setGravity(Gravity.CENTER);

            textView.setWidth(0);
            textView.setTextColor(getResources().getColor(R.color.primary_text));
            LinearLayout.LayoutParams params;

            if (i == -1) {
                params = new LinearLayout.LayoutParams(
                        Utils.dip2px(getContext(), NODE_WIDTH),
                        ViewGroup.LayoutParams.MATCH_PARENT);
                textView.setTextSize(NODE_TEXT_SIZE);
                textView.setText(mCurrentMonth + "\n月");

                mMMonthTextView = textView;
            } else {
                params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
                params.weight = 1;
                textView.setTextSize(WEEK_TEXT_SIZE);
                textView.setText(Constant.WEEK_SINGLE[i]);
            }

            mLayoutWeekGroup.addView(textView, params);
        }

        int nodeItemHeight = Utils.dip2px(getContext(), 55);
        for (int i = 1; i <= 16; i++) {
            TextView textView = new TextView(getContext());
            textView.setTextSize(NODE_TEXT_SIZE);
            textView.setGravity(Gravity.CENTER);
            textView.setTextColor(Color.GRAY);
            textView.setText(String.valueOf(i));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, nodeItemHeight);
            mLayoutNodeGroup.addView(textView, params);
        }
    }

    private void initToolbar() {
        Toolbar toolbar = getView().findViewById(R.id.toolbar);
        toolbar.setTitle("");
        toolbar.inflateMenu(R.menu.toolbar_main);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.action_set) {
                    Intent intent = new Intent(getActivity(),
                            HomeActivity.class);
                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initCourseView() {
        mCourseViewV2 = getActivity().findViewById(R.id.course_view_v2);
        mCourseViewV2.setCourseItemRadius(3)
                .setTextTBMargin(dp2px(1), dp2px(1));

        mCourseViewV2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                System.out.println("touch");
                return false;
            }
        });
        initCourseViewEvent();
    }

    /**
     * courseVIew事件
     */
    private void initCourseViewEvent() {
        mCourseViewV2.setOnItemClickListener(new CourseView.OnItemClickListener() {
            @Override
            public void onClick(List<CourseAncestor> course, View itemView) {
                mDialog = new ShowDetailDialog();
                mDialog.show(getActivity(), (CourseV2) course.get(0), new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        mDialog = null;
                    }
                });
            }

            @Override
            public void onLongClick(List<CourseAncestor> courses, View itemView) {
                final CourseV2 course = (CourseV2) courses.get(0);
                DialogHelper dialogHelper = new DialogHelper();
                dialogHelper.showNormalDialog(getActivity(), "确定删除？",
                        "课程 【" + course.getCouName() + "】" + Constant.WEEK[course.getCouWeek()]
                                + "第" + course.getCouStartNode() + "节 ", new DialogListener() {
                            @Override
                            public void onPositive(DialogInterface dialog, int which) {
                                super.onPositive(dialog, which);
                                deleteCancelSnackBar(course);
                            }
                        });
            }

            public void onAdd(CourseAncestor course, View addView) {
                Intent intent = new Intent(getActivity(), AddActivity.class);
                intent.putExtra(Constant.INTENT_ADD_COURSE_ANCESTOR, course);
                intent.putExtra(Constant.INTENT_ADD, true);
                startActivity(intent);
            }

        });
    }

    /**
     * 撤销删除提示
     */
    private void deleteCancelSnackBar(final CourseV2 course) {
        course.setDisplayable(false);
        mCourseViewV2.resetView();
        Snackbar.make(mMMonthTextView, "删除成功！☆\\(￣▽￣)/", Snackbar.LENGTH_LONG).setAction("撤销",
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                }).addCallback(new Snackbar.Callback() {
            @Override
            public void onDismissed(Snackbar snackbar, int event) {
                switch (event) {
                    case Snackbar.Callback.DISMISS_EVENT_CONSECUTIVE:
                    case Snackbar.Callback.DISMISS_EVENT_MANUAL:
                    case Snackbar.Callback.DISMISS_EVENT_SWIPE:
                    case Snackbar.Callback.DISMISS_EVENT_TIMEOUT:
                        //to do delete
                        mPresenter.deleteCourse(course.getCouId());
                        break;
                    case Snackbar.Callback.DISMISS_EVENT_ACTION:
                        //cancel
                        course.setDisplayable(true);
                        mCourseViewV2.resetView();
                        break;
                }
            }
        }).show();
    }

    private void updateView() {
        updateCoursePreference();
    }

    @SuppressLint("SetTextI18n")
    public void updateCoursePreference() {
        updateCurrentWeek();
        mCurrentMonth = TimeUtils.getNowMonth();
        mMMonthTextView.setText(mCurrentMonth + "\n月");

        //get id
        long currentCsNameId = Preferences.getLong(
                getString(R.string.app_preference_current_cs_name_id), 0L);

        LogUtil.i(this, "当前课表-->" + currentCsNameId);
        mPresenter.updateCourseViewData(currentCsNameId);
    }

    @SuppressLint("SetTextI18n")
    private void updateCurrentWeek() {
        mCurrentWeekCount = AppUtils.getCurrentWeek(getActivity().getBaseContext());
        mTvWeekCount.setText("第" + mCurrentWeekCount + "周");
        mCourseViewV2.setCurrentIndex(mCurrentWeekCount);
    }

    @Override
    public void initFirstStart() {
        boolean isFirst = Preferences.getBoolean(getString(R.string.app_preference_app_is_first_start), true);
        if (!isFirst) {
            return;
        }
        Preferences.putBoolean(getString(R.string.app_preference_app_is_first_start), false);

        CourseGroupDao groupDao = Cache.instance().getCourseGroupDao();
        CourseGroup defaultGroup = groupDao
                .queryBuilder()
                .where(CourseGroupDao.Properties.CgName.eq("默认课表"))
                .unique();

        long insert;
        if (defaultGroup == null) {
            insert = groupDao.insert(new CourseGroup(0L, "默认", ""));
        } else {
            insert = defaultGroup.getCgId();
        }
        AppUtils.copyOldData(getActivity());
        Preferences.putLong(getString(R.string.app_preference_current_cs_name_id), insert);
    }


    private void isV1Update() {
        LogUtil.e(this, "属于从1.x升级上来");
        showToast("请选择正在使用的课表");
        startActivity(new Intent(getActivity(), MgActivity.class));
    }
    @Override
    public void setCourseData(List<CourseV2> courses) {
        mCourseViewV2.clear();

        CourseV2Dao courseV2Dao = Cache.instance().getCourseV2Dao();
        LogUtil.d(this, "当前课程数：" + courses.size());

        for (CourseV2 course : courses) {
            if (course.getCouColor() == null || course.getCouColor() == -1) {
                course.setCouColor(Utils.getRandomColor());
                courseV2Dao.update(course);
            }
            course.init();

            LogUtil.e(this, "即将显示：" + course.toString());

            mCourseViewV2.addCourse(course);
        }
        if (courses.isEmpty()) {
            mLayoutCourse.setBackgroundResource(R.drawable.svg_bg);
        } else {
            mLayoutCourse.setBackgroundResource(0);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_toolbar_subtitle:
                weekTitle(v);
                break;
        }
    }

    private void weekTitle(View v) {
        animSelectWeek(!mSelectWeekIsShow);
    }


    public boolean onTouchEvent(MotionEvent event) {
        if (mDialog != null) mDialog.dismiss();
        return onTouchEvent(event);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void courseChangeEvent(CourseDataChangeEvent event) {
        //更新主界面
        updateView();
    }
    @Override
    public void setPresenter(CourseContract.Presenter presenter) {
        mPresenter = presenter;
    }
}